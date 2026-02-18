package com.example.libreman;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlertsAdapter
        extends RecyclerView.Adapter<AlertsAdapter.ViewHolder> {

    private final List<String> alerts;

    public AlertsAdapter(List<String> alerts) {
        this.alerts = alerts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alert, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.alertText.setText(alerts.get(position));
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView alertText;

        ViewHolder(View itemView) {
            super(itemView);
            alertText = itemView.findViewById(R.id.tvAlert);
        }
    }
}
