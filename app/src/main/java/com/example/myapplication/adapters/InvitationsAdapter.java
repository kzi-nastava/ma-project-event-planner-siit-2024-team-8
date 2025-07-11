package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.BudgetItem;
import com.example.myapplication.domain.Invitation;
import com.google.android.material.card.MaterialCardView;

import java.time.LocalDate;
import java.util.List;

public class InvitationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_NORMAL = 0;
    public static final int VIEW_TYPE_INPUT = 1;
    private List<Invitation> invitations;

    private OnNewInvitationsListener newInvitationListener;

    private Boolean isMyEvent = null;

    private Boolean isPrivate = null;


    // Constructor
    public InvitationsAdapter(List<Invitation> invitations, OnNewInvitationsListener listener,Boolean isMyEvent,Boolean isPrivate) {
        this.invitations = invitations;
        this.newInvitationListener = listener;
        this.isMyEvent = isMyEvent;
        this.isPrivate = isPrivate;
    }

    @Override
    public int getItemViewType(int position) {
        Invitation invitation = invitations.get(position);
        if (invitation.isInput()) {
            return VIEW_TYPE_INPUT;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_INPUT:
                View inputView = LayoutInflater.from(parent.getContext()).inflate(R.layout.invitation_card_input, parent, false);
                return new InputInvitationViewHolder(inputView);

            case VIEW_TYPE_NORMAL:
            default:
                View normalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.invitation_card, parent, false);
                return new InvitationsAdapter.InvitationViewHolder(normalView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InvitationsAdapter.InputInvitationViewHolder) {
            ((InvitationsAdapter.InputInvitationViewHolder) holder).bind(invitations.get(position));
        } else if (holder instanceof InvitationsAdapter.InvitationViewHolder) {
            Invitation invitation = invitations.get(position);
            ((InvitationsAdapter.InvitationViewHolder)holder).bind(invitation);
        }
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }

    public void removeInvitation (Invitation item){
        int position = invitations.indexOf(item);
        if (position != -1) {
            invitations.remove(position);
            notifyItemRemoved(position);
        }
    }
    public class InputInvitationViewHolder extends RecyclerView.ViewHolder{

        private final EditText invitationEmailEditText;

        private Invitation invitation;
        public InputInvitationViewHolder(@NonNull View itemView) {
            super(itemView);

            invitationEmailEditText = itemView.findViewById(R.id.invitationEmailEditText);
            Button save = itemView.findViewById(R.id.saveInvitationButton);

            save.setOnClickListener( v -> {
                onSaveClicked();
            });

            Button removeBtn = itemView.findViewById(R.id.deleteInvitationButton);
            removeBtn.setOnClickListener(v -> onRemoveInvitationClicked());
        }

        private void onRemoveInvitationClicked() {
            removeInvitation(this.invitation);
        }

        public void onSaveClicked(){
            Invitation updatedInvitation = new Invitation(LocalDate.now(),invitationEmailEditText.getText().toString(),false);
            int position = getAdapterPosition();
            invitations.set(position, updatedInvitation);
            notifyItemChanged(position);
        }

        public void bind(Invitation invitation){
            this.invitation = invitation;
            invitationEmailEditText.setText(invitation.getEmail());
        }
    }

    public class InvitationViewHolder extends RecyclerView.ViewHolder{
        private final TextView invitationEmailTextView;

        private final MaterialCardView invitationCard;
        public InvitationViewHolder(@NonNull View itemView) {
            super(itemView);

            invitationEmailTextView = itemView.findViewById(R.id.invitationEmail);
            invitationCard = itemView.findViewById(R.id.invitationMaterialCard);

            invitationCard.setOnClickListener(v -> {
                    onInvitationClicked();
            });
        }

        public void onInvitationClicked(){
            if (isMyEvent && isPrivate){
                Invitation updatedInvitation = new Invitation( LocalDate.now(),invitationEmailTextView.getText().toString(),true);
                int position = getAdapterPosition();
                invitations.set(position, updatedInvitation);
                notifyItemChanged(position);
            }else{
                newInvitationListener.onInvitationClickedUser(invitations.get(getAdapterPosition()).getUserId());
            }
        }

        public void bind(Invitation invitation){
            invitationEmailTextView.setText(invitation.getEmail());

        }

    }

    public void setInvitations(List<Invitation> invitations){
        this.invitations = invitations;
        notifyDataSetChanged();
    }

    public interface OnNewInvitationsListener {
        void onCreateNewInvitation();
        void onInvitationClickedUser(String userId);
    }
}
