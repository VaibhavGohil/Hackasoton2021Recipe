package com.example.hackasoton2021recipe.frontend.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackasoton2021recipe.R;
import com.example.hackasoton2021recipe.backend.DiaryLog;
import java.util.ArrayList;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    Context context;
    ArrayList<DiaryLog> logs;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView ingreds;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            ingreds = itemView.findViewById(R.id.ingred);


        }
    }

    public LogAdapter(Context context, ArrayList<DiaryLog> logs){
        this.context = context;
        this.logs = logs;
    }


    @NonNull
    @Override
    public LogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.logitem, parent, false);
        ViewHolder holder = new ViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LogAdapter.ViewHolder holder, int position) {
        holder.date.setText(logs.get(position).first);
        holder.ingreds.setText(logs.get(position).last);
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }
}
