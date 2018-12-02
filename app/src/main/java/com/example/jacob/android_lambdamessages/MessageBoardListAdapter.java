package com.example.jacob.android_lambdamessages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final MessageBoard data = dataList.get(i);
        viewHolder.messageBoardTitle.setText(data.getTitle());
        String subscriptionStatus = MainActivity.preferences.getString(data.getIdentifier(), "");
        if (!subscriptionStatus.equals("")) {
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            Intent intent = new Intent(context, SubscriptionMonitorService.class);
            intent.putExtra(Constants.SERVICE_KEY, data.getIdentifier());
            activity.startService(intent);
        } else {
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
        viewHolder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageViewActivity.class);
                intent.putExtra(MessageViewActivity.VIEW_BOARD_KEY, data);
                activity.startActivity(intent);
            }
        });
        viewHolder.parentView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String id = data.getIdentifier();
                String status = MainActivity.preferences.getString(id, "");
                SharedPreferences.Editor editor = MainActivity.preferences.edit();
                Intent intent = new Intent(context, SubscriptionMonitorService.class);
                if (status.equals("")) {
                    editor.putString(id, id);
                    viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    intent.putExtra(Constants.SERVICE_KEY, data.getIdentifier());
                    activity.startService(intent);
                } else {
                    editor.remove(id);
                    viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                    intent.putExtra(Constants.SERVICE_KEY, "");
                    activity.stopService(intent);
                }
                editor.apply();
                return true;
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
