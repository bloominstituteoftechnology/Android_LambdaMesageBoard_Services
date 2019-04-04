package com.jakeesveld.android_lambdamessages;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    ArrayList<Message> dataList;

    public MessageListAdapter(ArrayList<Message> dataList){
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {

        final Message data = dataList.get(i);

        messageViewHolder.senderTextView.setText(data.getSender());
        messageViewHolder.contentTextView.setText(data.getText());
        Integer timestamp =(int) data.getTimestamp();
        messageViewHolder.timeStampTextView.setText(timestamp.toString());


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView senderTextView;
        TextView timeStampTextView;
        TextView contentTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.text_view_sender);
            timeStampTextView = itemView.findViewById(R.id.text_view_timestamp);
            contentTextView = itemView.findViewById(R.id.text_view_content);
        }
    }
}
