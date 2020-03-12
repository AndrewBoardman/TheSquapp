package com.example.thesquapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.amplify.generated.graphql.CreateChallengeMutation;
import com.amazonaws.amplify.generated.graphql.ListUsersQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import type.CreateChallengeInput;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListUsersQuery.Item> mData = new ArrayList<>();;
    private Context context;
    private LayoutInflater mInflater;
    private int selected_position = -1;

    // data is passed into the constructor
   MyAdapter(Context context) {
       this.mInflater = LayoutInflater.from(context);
       this.context = context;

   }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mData.get(position));


        if (selected_position == position) {
            // do your stuff here like
            //Change selected item background color and Show sub item views

            Toast.makeText(context,"you clicked an item " + mData.get(position).id(), Toast.LENGTH_LONG).show();

            CreateChallengeInput input = CreateChallengeInput.builder()
                    .challengee(mData.get(position).id())
                    .challenger(AWSMobileClient.getInstance().getUsername())
                    //.challenge_id("titz")
                    //.challenger(mData.get(position).id())
                    //.challengee()
                    //.date_sent();
                    .build();

            CreateChallengeMutation addChallengeMutation = CreateChallengeMutation.builder()
                    .input(input)
                    .build();
            ClientFactory.appSyncClient().mutate(addChallengeMutation).enqueue(mutateCallback);

            Intent intent = new Intent(context.getApplicationContext(), ChallengeActivity.class);
            context.startActivity(intent);

        } else {
            // do your stuff here like
            //Change  unselected item background color and Hide sub item views
        }
        // rest of the code here

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected_position== position){
                    selected_position=-1;
                    notifyDataSetChanged();
                    return;
                }
                selected_position = position;
                notifyDataSetChanged();

            }
        });
    }

    private GraphQLCall.Callback<CreateChallengeMutation.Data> mutateCallback = new GraphQLCall.Callback<CreateChallengeMutation.Data>() {
        @Override
        public void onResponse(@Nonnull final Response<CreateChallengeMutation.Data> response) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(AddPetActivity.this, "Added pet", Toast.LENGTH_SHORT).show();
                    //AddPetActivity.this.finish();
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

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // resets the list with a new set of data
    public void setItems(List<ListUsersQuery.Item> items) {
        mData = items;
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_username;
        TextView txt_id;
        public LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            txt_username = itemView.findViewById(R.id.txt_username);
            txt_id = itemView.findViewById(R.id.txt_id);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }

       public  void bindData(ListUsersQuery.Item item) {
            txt_username.setText(item.id());
            txt_id.setText(item.email());

        }
    }
}