package com.example.battleship.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.battleship.R;
import com.example.battleship.models.Statistic;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.ViewHolder> {
    List<Statistic> statistics;
    private final LayoutInflater inflater;

    public StatisticAdapter(List<Statistic> statistics, Context context){
        this.statistics = statistics;
        this.inflater = LayoutInflater.from(context);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StatisticAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.statistic_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.winnerImage.setImageURI(statistics.get(position).getWinner().profileImageUrl);
        holder.loserImage.setImageURI(statistics.get(position).getLoser().profileImageUrl);
        holder.winnerUsername.setText(statistics.get(position).getWinner().username);
        holder.loserUsername.setText(statistics.get(position).getLoser().username);
        holder.dateTextView.setText(statistics.get(position).getDate());
        holder.timeTextView.setText(statistics.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return statistics.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView winnerImage;
        SimpleDraweeView loserImage;
        TextView winnerUsername;
        TextView loserUsername;
        TextView dateTextView;
        TextView timeTextView;

        ViewHolder(View view) {
            super(view);
            winnerImage = view.findViewById(R.id.winnerImage);
            loserImage = view.findViewById(R.id.loserImage);
            loserUsername = view.findViewById(R.id.loserUsername);
            winnerUsername = view.findViewById(R.id.winnerUsername);
            dateTextView = view.findViewById(R.id.dateTextView);
            timeTextView = view.findViewById(R.id.timeTextView);
        }

    }
}
