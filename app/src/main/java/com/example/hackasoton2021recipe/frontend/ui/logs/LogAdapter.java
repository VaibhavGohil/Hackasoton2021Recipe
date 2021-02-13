package com.example.hackasoton2021recipe.frontend.ui.logs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackasoton2021recipe.R;
import com.example.hackasoton2021recipe.backend.DiaryLog;
import com.example.hackasoton2021recipe.backend.FireBaseService;

import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    Context context;
    List<DiaryLog> logs;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView ingreds;
        TextView rating;
        ImageButton delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            ingreds = itemView.findViewById(R.id.ingred);
            rating = itemView.findViewById(R.id.rating);
            delete = itemView.findViewById(R.id.deletebutton);

        }
    }

    public LogAdapter(Context context, List<DiaryLog> logs){
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
        holder.date.setText(logs.get(position).date);
        holder.ingreds.setText(logs.get(position).ingredients.toString());
        holder.rating.setText(logs.get(position).rating);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireBaseService.getInstance().deleteLog(logs.get(position).path,position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }
}
