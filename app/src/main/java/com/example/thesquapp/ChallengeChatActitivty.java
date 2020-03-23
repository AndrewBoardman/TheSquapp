package com.example.thesquapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.amplify.generated.graphql.CreateChallengeMutation;
import com.amazonaws.amplify.generated.graphql.CreateChatMutation;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import javax.annotation.Nonnull;

import type.CreateChallengeInput;
import type.CreateChatInput;

public class ChallengeChatActitivty extends AppCompatActivity {

    private Context context;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_chat_actitivty);

        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.e("INIT", "Initialization error.", e);
                    }
                }
        );
    }

    public void challengePlayerBtn(View view) {
        Toast.makeText(ChallengeChatActitivty.this, "you challenged penis " + MyAdapter.challengeeName, Toast.LENGTH_LONG).show();

        //challengeeName = mData.get(position).id();

        CreateChallengeInput input = CreateChallengeInput.builder()
                .challengee(MyAdapter.challengeeName)
                .challenger(AWSMobileClient.getInstance().getUsername())
                .status("pending")
                //.challenge_id("titz")
                //.challenger(mData.get(position).id())
                //.challengee()
                //.date_sent();
                .build();

        CreateChallengeMutation addChallengeMutation = CreateChallengeMutation.builder()
                .input(input)
                .build();
        ClientFactory.appSyncClient().mutate(addChallengeMutation).enqueue(mutateCallback);

      /*  final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_see_challenges);
        dialog.show(); */

    }

    private GraphQLCall.Callback<CreateChallengeMutation.Data> mutateCallback = new GraphQLCall.Callback<CreateChallengeMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateChallengeMutation.Data> response) {
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
                    Log.e("", "Failed to perform AddPetMutation", e);
                }
            });
        }

    };

    public void test1(View view){
        Toast.makeText(ChallengeChatActitivty.this, "you challenged penis " + MyAdapter.challengeeName, Toast.LENGTH_LONG).show();

        //challengeeName = mData.get(position).id();

        CreateChatInput input = CreateChatInput.builder()
                //.id("flava")
                .text("string")
                //.challengee(MyAdapter.challengeeName)
                //.challenger(AWSMobileClient.getInstance().getUsername())
                //.status("pending")
                //.challenge_id("titz")
                //.challenger(mData.get(position).id())
                //.challengee()
                //.date_sent();
                .build();

        CreateChatMutation addChallengeMutation = CreateChatMutation.builder()
                .input(input)
                .build();
        ClientFactory.appSyncClient().mutate(addChallengeMutation).enqueue(mutateCallback2);

      /*  final Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_see_challenges);
        dialog.show(); */

    }

    private GraphQLCall.Callback<CreateChatMutation.Data> mutateCallback2 = new GraphQLCall.Callback<CreateChatMutation.Data>() {
            @Override
            public void onResponse(@Nonnull final Response<CreateChatMutation.Data> response) {
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
                    Log.e("", "Failed to perform AddPetMutation", e);
                }
            });
        }

    };


}


