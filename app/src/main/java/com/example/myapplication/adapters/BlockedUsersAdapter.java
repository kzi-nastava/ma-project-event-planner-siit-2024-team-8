package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.dto.user.BlockedUserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BlockedUsersAdapter extends RecyclerView.Adapter<BlockedUsersAdapter.ViewHolder> {

    private List<BlockedUserResponse> blocked = new ArrayList<>();

    private UnblockListener listener;

    public BlockedUsersAdapter(UnblockListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public BlockedUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blocked_user_item,parent,false);
        return new BlockedUsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockedUsersAdapter.ViewHolder holder, int position) {
        BlockedUserResponse item = blocked.get(position);
        holder.fullNameTV.setText(item.getFullName());
        holder.unblockButton.setOnClickListener(v -> {
            blocked = blocked.stream().filter(blocked -> !Objects.equals(blocked.getId(), item.getId())).collect(Collectors.toList());
            notifyItemChanged(position);
            listener.onUnblockClicked(blocked.stream().map(BlockedUserResponse::getId).collect(Collectors.toList()));
        });
    }

    @Override
    public int getItemCount() {
        return blocked.size();
    }

    public void setBlocked(List<BlockedUserResponse> blocked){
        this.blocked = blocked;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView fullNameTV;
        Button unblockButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fullNameTV = itemView.findViewById(R.id.emailTV);
            unblockButton = itemView.findViewById(R.id.unblockButton);
        }
    }
    public interface UnblockListener {
        void onUnblockClicked(List<String> blockedIds);
    }
}
