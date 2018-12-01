package com.example.jacob.android_lambdamessages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageSender, messageText, messageTimestamp;
        ViewGroup parentView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            messageSender = itemView.findViewById(R.id.message_element_sender);
            messageText = itemView.findViewById(R.id.message_element_text);
            messageTimestamp = itemView.findViewById(R.id.message_element_timestamp);
            parentView = itemView.findViewById(R.id.message_element_parent_layout);
        }
    }

    private ArrayList<Message> dataList;
    private Context context;
    private Activity activity;

    MessageListAdapter(ArrayList<Message> dataList, Activity activity) {
        this.dataList = dataList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(
                viewGroup.getContext())
                .inflate(
                        R.layout.message_element_layout,
                        viewGroup,
                        false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Message data = dataList.get(i);

        viewHolder.messageSender.setText(data.getSender());
        viewHolder.messageText.setText(data.getText());
        Date date = new Date((new Double(data.getTimestamp())).longValue()*1000);
        viewHolder.messageTimestamp.setText(String.valueOf(date));
    }

    @Override
    public int getItemCount() {
        int a;
        if (dataList != null && !dataList.isEmpty()) {
            a = dataList.size();
        } else {
            a = 0;
        }
        return a;
    }
}
