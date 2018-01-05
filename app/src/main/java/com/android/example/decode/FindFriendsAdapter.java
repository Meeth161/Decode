package com.android.example.decode;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FindFriendsAdapter extends RecyclerView.Adapter<FindFriendsAdapter.ViewHolder> {

    ArrayList<User> users;
    Context context;

    public FindFriendsAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvUserName.setText(users.get(position).getName());
        Glide.with(context).setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.user)).load(users.get(position).getDpUrl()).into(holder.civUserDp);

        holder.rvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserProfile.class).putExtra("uid", users.get(position).getUid()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civUserDp;
        TextView tvUserName;
        RelativeLayout rvUser;

        public ViewHolder(View itemView) {
            super(itemView);

            civUserDp = (CircleImageView) itemView.findViewById(R.id.civ_userDp);
            tvUserName = (TextView) itemView.findViewById(R.id.textView_userName);
            rvUser = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_user);
        }
    }

}
