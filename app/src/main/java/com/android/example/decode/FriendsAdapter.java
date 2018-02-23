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

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    Context context;
    ArrayList<User> list;

    public FriendsAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvUserName.setText(list.get(position).getName());
        Glide.with(context).setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.user)).load(list.get(position).getDpUrl()).into(holder.civUserDp);

    }

    @Override
    public int getItemCount() {
        return list.size();
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
