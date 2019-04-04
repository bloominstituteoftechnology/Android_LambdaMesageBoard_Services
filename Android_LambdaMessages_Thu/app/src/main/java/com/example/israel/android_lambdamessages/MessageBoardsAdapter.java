package com.example.israel.android_lambdamessages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class MessageBoardsAdapter extends RecyclerView.Adapter<MessageBoardsAdapter.ViewHolder> {

    public static final String KEY_EXTRA_IS_SUBSCRIBING = "subscribing";
    public static final String KEY_EXTRA_MESSAGE_BOARD = "message_board";

    public MessageBoardsAdapter() {
        this.messageBoards = new ArrayList<>();
    }

    private ArrayList<MessageBoard> messageBoards;

    public ArrayList<MessageBoard> getMessageBoards() {
        return messageBoards;
    }

    public void setMessageBoards(ArrayList<MessageBoard> messageBoards) {
        this.messageBoards = messageBoards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_board_layout, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final MessageBoard messageBoard = messageBoards.get(i);

        viewHolder.titleTextView.setText(messageBoard.getTitle());
        viewHolder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = viewHolder.titleTextView.getContext();
                Intent intent = new Intent(context, MessagesActivity.class);
                intent.putExtra(KEY_EXTRA_MESSAGE_BOARD, messageBoard);
                context.startActivity(intent);
            }
        });

        viewHolder.titleTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Context context = viewHolder.titleTextView.getContext();
                Intent intent = new Intent(context, SubscriptionMonitorService.class);

                Object tagObj = viewHolder.titleTextView.getTag();
                if (tagObj != null) { // currently subscribed
                    viewHolder.titleTextView.setTag(null); // unsubscribe
                    viewHolder.titleTextView.setBackgroundColor(Color.argb(0,255,255,255));

                    intent.putExtra(KEY_EXTRA_IS_SUBSCRIBING, false);
                } else {
                    viewHolder.titleTextView.setTag("subscribed"); // subscribe
                    viewHolder.titleTextView.setBackgroundColor(Color.argb(100,255,255,0));

                    intent.putExtra(KEY_EXTRA_IS_SUBSCRIBING, true);
                }

                intent.putExtra(KEY_EXTRA_MESSAGE_BOARD, messageBoard);
                context.startService(intent);

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageBoards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.text_view_title);
        }

        TextView titleTextView;
    }
}
