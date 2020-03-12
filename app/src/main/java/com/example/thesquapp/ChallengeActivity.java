package com.example.thesquapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.amplify.generated.graphql.CreateChallengeMutation;
import com.amazonaws.amplify.generated.graphql.ListUsersQuery;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import type.CreateChallengeInput;

public class ChallengeActivity extends AppCompatActivity {

    private List<ListUsersQuery.Item> mData = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

    }

    public void challengeBtn(View view){

        CreateChallengeInput input = CreateChallengeInput.builder()
                //.challenger(mData.id())
                //.challengee()
                //.date_sent();
                .build();

        CreateChallengeMutation addChallengeMutation = CreateChallengeMutation.builder()
                .input(input)
                .build();
        ClientFactory.appSyncClient().mutate(addChallengeMutation).enqueue(mutateCallback);
    }

    // Mutation callback code
    private GraphQLCall.Callback<CreateChallengeMutation.Data> mutateCallback = new GraphQLCall.Callback<CreateChallengeMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateChallengeMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(AddCh.this, "Added pet", Toast.LENGTH_SHORT).show();
                   // AddPetActivity.this.finish();
                }
            });
        }

        @Override
        public void onFailure(@Nonnull final ApolloException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("", "Failed to perform AddPetMutation", e);
                    //Toast.makeText(AddPetActivity.this, "Failed to add pet", Toast.LENGTH_SHORT).show();
                    //AddPetActivity.this.finish();
                }
            });
        }
    };
}
