package com.android.example.decode;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    Context context;
    ArrayList<Message> messages;

    FirebaseAuth mAuth;

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        mAuth = FirebaseAuth.getInstance();
        if(messages.get(position).getFrom().equals(mAuth.getCurrentUser().getUid())) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;

        switch(viewType) {
            case 1 : v = LayoutInflater.from(context).inflate(R.layout.message_to, parent, false);
                     break;
            case 2 : v = LayoutInflater.from(context).inflate(R.layout.message_from, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvMessage.setText(messages.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMessage;

        public ViewHolder(View itemView) {
            super(itemView);

            tvMessage = (TextView) itemView.findViewById(R.id.textView_message_from);
        }
    }
}
