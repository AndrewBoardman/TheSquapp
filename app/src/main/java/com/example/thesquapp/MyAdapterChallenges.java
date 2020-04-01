package com.example.thesquapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.amplify.generated.graphql.CreateChallengeMutation;
import com.amazonaws.amplify.generated.graphql.ListChallengesQuery;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class MyAdapterChallenges extends RecyclerView.Adapter<MyAdapterChallenges.ViewHolder> {

    private List<ListChallengesQuery.Item> mData = new ArrayList<>();
    private Context context;
    private LayoutInflater mInflater;
    private int selected_position = -1;

    // data is passed into the constructor
    MyAdapterChallenges(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_challenges, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mData.get(position));

        if (selected_position == position) {

            //challengeeName = mData.get(position).id();
            Toast.makeText(context, "you clicked an item " + mData.get(position).challengee(), Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(context.getApplicationContext(), ChallengeChatActitivty.class);
            //context.startActivity(intent);
        } else {
            // do your stuff here like
            //Change  unselected item background color and Hide sub item views
        }
        // rest of the code here

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_position == position) {
                    selected_position = -1;
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

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // resets the list with a new set of data
    public void setItems(List<ListChallengesQuery.Item> items) {
        mData = items;
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_challenger;
        TextView txt_challengee;
        public LinearLayout linearLayout;

        ViewHolder(View itemView) {
            super(itemView);
            txt_challenger = itemView.findViewById(R.id.txt_challenger);
            txt_challengee = itemView.findViewById(R.id.txt_challengee);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }

        public void bindData(ListChallengesQuery.Item item) {
            txt_challenger.setText(item.challenger());
            txt_challengee.setText(item.challengee());

        }
    }
}
