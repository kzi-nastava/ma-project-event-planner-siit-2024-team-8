package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.dto.GuestResponse;
import com.example.myapplication.domain.dto.ReportResponse;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {
    private List<ReportResponse> reports = new ArrayList<>();

    private OnSuspendUserListener listener;

    public ReportsAdapter(OnSuspendUserListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReportsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportsAdapter.ViewHolder holder, int position) {
        ReportResponse response = reports.get(position);
        holder.userEmail.setText(response.getUserEmail());
        holder.reportedUserEmail.setText(response.getReportedEmail());
        holder.reason.setText(response.getReason());
        holder.date.setText(response.getDate().toString());

        holder.suspend.setOnClickListener(v -> {
            listener.suspendUserClicked(response.getId());
        });
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userEmail;
        TextView reportedUserEmail;
        TextView reason;
        TextView date;

        MaterialButton suspend;
        public ViewHolder(View itemView) {
            super(itemView);
            userEmail = itemView.findViewById(R.id.userEmail);
            reportedUserEmail = itemView.findViewById(R.id.reportedUserEmail);
            reason = itemView.findViewById(R.id.reasonTV);
            date = itemView.findViewById(R.id.tvReportDate);

            suspend = itemView.findViewById(R.id.suspendButton);
        }
    }

    public void setReports(List<ReportResponse> reports) {
        this.reports = reports;
        notifyDataSetChanged();
    }

    public interface OnSuspendUserListener{
        void suspendUserClicked(String reportId);
    }
}
