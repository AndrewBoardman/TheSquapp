package com.example.thesquapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amazonaws.amplify.generated.graphql.ListUsersQuery;
import com.amazonaws.amplify.generated.graphql.UpdateUserMutation;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import type.UpdateUserInput;

public class ChoosePhotoActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private String photoPath;
    public static final String TAG = "Photo";
    private ImageView profilePic;
    private EditText textTest;
    private AWSAppSyncClient mAWSAppSyncClient;
    private List<ListUsersQuery.Item> mUserD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_photo);

        ClientFactory.init(this);
    }

    public void choosePhotoBtn(View view) {
        choosePhoto();
    }

    public void cPhSavRet(View view) {
        uploadAndSave();
    }

    // Mutation callback code
    private GraphQLCall.Callback<UpdateUserMutation.Data> mutateCallback = new GraphQLCall.Callback<UpdateUserMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<UpdateUserMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform UpdateUserMutation", e);
                }
            });
        }
    };

    // Photo selector code
    public void choosePhoto() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            // String picturePath contains the path of selected Image
            photoPath = picturePath;
        }
    }

    private String getS3Key(String localPath) {
        //We have read and write ability under the public folder
        return "public/" + new File(localPath).getName();
    }

    public void uploadWithTransferUtility(String localPath) {
        String key = getS3Key(localPath);

        Log.d(TAG, "Uploading file from " + localPath + " to " + key);

        TransferObserver uploadObserver =
                ClientFactory.transferUtility().upload(
                        key,
                        new File(localPath));

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                    Log.d(TAG, "Upload is completed. ");

                    // Upload is successful. Save the rest and send the mutation to server.
                    save();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;

                Log.d(TAG, "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
                Log.e(TAG, "Failed to upload photo. ", ex);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChoosePhotoActivity.this, "Failed to upload photo", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private UpdateUserInput getUpdateUserInput() {

        if (photoPath != null && !photoPath.isEmpty()) {
            return UpdateUserInput.builder()
                    .id(AWSMobileClient.getInstance().getUsername())
                    .photo(getS3Key(photoPath))
                    .build();
        } else {
            return UpdateUserInput.builder()
                    .id(AWSMobileClient.getInstance().getUsername())
                    .build();
        }
    }

    private void save() {
        UpdateUserInput input = getUpdateUserInput();

        UpdateUserMutation editUserMutation = UpdateUserMutation.builder()
                .input(input)
                .build();

        ClientFactory.appSyncClient().mutate(editUserMutation).
                refetchQueries(ListUsersQuery.builder().build()).
                enqueue(mutateCallback);

        // Enables offline support via an optimistic update
        // Add to event list while offline or before request returns
        //addPicOffline(input);
    }

   /* private void addPicOffline(final UpdateUserInput input) {

        final UpdateUserMutation.UpdateUser expected =
                new UpdateUserMutation.UpdateUser(
                        //"User",
                        //UUID.randomUUID().toString(),
                        //input.name(),
                        //input.description(),
                        input.photo());

        final AWSAppSyncClient awsAppSyncClient = ClientFactory.appSyncClient();
        final ListUsersQuery listUsersQuery = ListUsersQuery.builder().build();

        awsAppSyncClient.query(listUsersQuery)
                .responseFetcher(AppSyncResponseFetchers.CACHE_ONLY)
                .enqueue(new GraphQLCall.Callback<ListUsersQuery.Data>() {
                    @Override
                    public void onResponse(@Nonnull Response<ListUsersQuery.Data> response) {
                        List<ListUsersQuery.Item> items = new ArrayList<>();
                        if (response.data() != null) {
                            items.addAll(response.data().listUsers().items());
                        }

                        items.add(new ListUsersQuery.Item(expected.__typename(),
                                expected.id(),
                                expected.name(),
                                expected.description(),
                                expected.photo()));
                        ListUsersQuery.Data data = new ListUsersQuery.Data(
                                new ListUsersQuery.ListUsers("ModelPetConnection", items, null));
                        awsAppSyncClient.getStore().write(listUsersQuery, data).enqueue(null);
                        Log.d(TAG, "Successfully wrote item to local store while being offline.");

                        finishIfOffline();
                    }

                    @Override
                    public void onFailure(@Nonnull ApolloException e) {
                        Log.e(TAG, "Failed to update event query list.", e);
                    }
                });
    } */

    private void uploadAndSave() {

        if (photoPath != null) {
            // For higher Android levels, we need to check permission at runtime
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission not granted! Requesting...");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
            // Upload a photo first. We will only call save on its successful callback.
            uploadWithTransferUtility(photoPath);
        } else {
            save();
        }
    }

  /*  class imageView extends ImageView{

        imageView(View itemView)

    }

    public void bind(ListUsersQuery.Item item){
       //if (item.id().equals(AWSMobileClient.getInstance().getUsername())){
            profilePic=(ImageView) findViewById(R.id.chooseProfilePic);
            profilePic.setImageDrawable(item.photo(getS3Key(photoPath)));

       // }
    } */

    public void cTestThing(View view) {

        textTest = (EditText) findViewById(R.id.text_test);
        query();
    }

    private void query(){

        ClientFactory.appSyncClient().query(ListUsersQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(queryCallback1);
    }

    private GraphQLCall.Callback<ListUsersQuery.Data> queryCallback1 = new GraphQLCall.Callback<ListUsersQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListUsersQuery.Data> response) {

            mUserD = new ArrayList<>(response.data().listUsers().items());

            textTest.setText(mUserD.get(2).name());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //textTest.setText(mUserD.get(2).name());
                }
            });
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e(TAG, e.toString());
        }
    };

}





