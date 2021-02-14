package com.example.hackasoton2021recipe.frontend.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackasoton2021recipe.R;
import com.example.hackasoton2021recipe.backend.OccurancePercentage;

import java.util.ArrayList;

public class AllergenAdapter extends RecyclerView.Adapter<AllergenAdapter.ViewHolder>{
    Context context;
    ArrayList<OccurancePercentage> percentages;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ProgressBar progressBar;
        TextView progress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.percentageItemName);
            progressBar = itemView.findViewById(R.id.progressBar);
            progress = itemView.findViewById(R.id.percenatgeText);
        }
    }

    public AllergenAdapter(Context context, ArrayList<OccurancePercentage> percentages){
        this.context = context;
        this.percentages = percentages;
    }

    @NonNull
    @Override
    public AllergenAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.percentageitem, parent, false);
        AllergenAdapter.ViewHolder holder = new AllergenAdapter.ViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllergenAdapter.ViewHolder holder, int position) {
        holder.name.setText(percentages.get(position).occuranceName);
        holder.progress.setText(percentages.get(position).percentage.toString());
        holder.progressBar.setProgress(percentages.get(position).percentage);
    }

    @Override
    public int getItemCount() {
        return percentages.size();
    }
}
