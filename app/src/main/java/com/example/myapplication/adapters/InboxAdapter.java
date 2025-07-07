package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.InboxUser;

import java.util.List;
import java.util.function.Consumer;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    private List<InboxUser> users;
    private Consumer<InboxUser> clickListener;

    public InboxAdapter(List<InboxUser> users, Consumer<InboxUser> clickListener) {
        this.users = users;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox_user, parent, false);
        return new InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder holder, int position) {
        InboxUser user = users.get(position);
        holder.nameText.setText(user.getFullName());
        holder.roleText.setText(user.getRole());
        holder.unreadText.setVisibility(user.hasUnreadMessage() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> clickListener.accept(user));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class InboxViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, roleText, unreadText;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.inboxUserName);
            roleText = itemView.findViewById(R.id.inboxUserRole);
            unreadText = itemView.findViewById(R.id.inboxUnreadText);
        }
    }
}
