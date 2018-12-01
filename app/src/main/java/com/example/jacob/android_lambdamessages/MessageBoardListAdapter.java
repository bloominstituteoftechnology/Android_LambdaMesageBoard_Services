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

public class MessageBoardListAdapter extends RecyclerView.Adapter<MessageBoardListAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageBoardTitle, messageBoardContent;
        ViewGroup parentView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageBoardTitle = itemView.findViewById(R.id.messageboard_element_title);
            parentView = itemView.findViewById(R.id.messageboard_element_parent_layout);
        }
    }

    private ArrayList<MessageBoard> dataList;
    private Context context;
    private Activity activity;

    MessageBoardListAdapter(ArrayList<MessageBoard> dataList, Activity activity) {
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
                        R.layout.messageboard_element_layout,
                        viewGroup,
                        false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final MessageBoard data = dataList.get(i);

        viewHolder.messageBoardTitle.setText(data.getTitle());
        viewHolder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageViewActivity.class);
                intent.putExtra(MessageViewActivity.VIEW_BOARD_KEY, data);
                activity.startActivity(intent);
            }
        });
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
