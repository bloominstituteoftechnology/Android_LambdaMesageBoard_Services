package com.example.israel.android_lambdamessages;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    public MessagesAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }

    private ArrayList<Message> messages;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_layout, viewGroup, false);

        return new ViewHolder(itemView);
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Message message = messages.get(i);

        viewHolder.displayNameTextView.setText(message.getDisplayName() + ": ");
        viewHolder.textTextView.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            displayNameTextView = itemView.findViewById(R.id.text_view_display_name);
            textTextView = itemView.findViewById(R.id.text_view_text);
        }

        TextView displayNameTextView;
        TextView textTextView;
    }
}
