package com.example.timer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timer.models.TimerData;

import java.util.ArrayList;
import java.util.Random;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<TimerData> timerData;
    Context context;
    String[] cardViewColors;
    private final OnTimerEditListener mOnTimerEditListener;
    private final OnItemDeleteListener mOnDeleteListener;

    public RecyclerViewAdapter(Context ct, ArrayList<TimerData> data,
                               OnItemDeleteListener mOnDeleteListener, OnTimerEditListener mOnTimerEditListener) {
        this.context = ct;
        this.timerData = data;
        this.mOnDeleteListener = mOnDeleteListener;
        this.mOnTimerEditListener = mOnTimerEditListener;
        this.cardViewColors = context.getResources().getStringArray(R.array.card_view_colors);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;

        ImageButton playButton, optionsButton;

        TextView titleValue, workValue, preparingValue, restValue, setsValue, cyclesValue,
                calmDownValue, betweenSetsValue;

        OnTimerEditListener onTimerEditListener;
        OnItemDeleteListener onItemDeleteListener;

        public ViewHolder(@NonNull final View itemView, OnItemDeleteListener mOnDeleteListener,
                          OnTimerEditListener mOnTimerEditListener) {
            super(itemView);

            playButton = itemView.findViewById(R.id.playButton);
            optionsButton = itemView.findViewById(R.id.optionsButton);

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ActiveTimerActivity.class);
                    intent.putExtra("timer", timerData.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

            onItemDeleteListener = mOnDeleteListener;
            onTimerEditListener = mOnTimerEditListener;

            optionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, view);

                    popupMenu.getMenuInflater().inflate(R.menu.options_popup, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch(menuItem.getItemId()) {
                                case R.id.deleteItem:
                                    int position = getAdapterPosition();
                                    onItemDeleteListener.onItemDelete(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, getItemCount());
                                    return true;
                                case R.id.editItem:
                                    onTimerEditListener.onTimerEditClick(getAdapterPosition());
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });

            cardView = itemView.findViewById(R.id.cardView);
            cardView.setCardBackgroundColor(Color.parseColor
                    (cardViewColors[new Random().nextInt(cardViewColors.length)]));

            titleValue = itemView.findViewById(R.id.titleValue);
            workValue = itemView.findViewById(R.id.workValue);
            preparingValue = itemView.findViewById(R.id.preparingValue);
            restValue = itemView.findViewById(R.id.restValue);
            setsValue = itemView.findViewById(R.id.setsValue);
            cyclesValue = itemView.findViewById(R.id.cyclesValue);
            calmDownValue = itemView.findViewById(R.id.calmDownValue);
            betweenSetsValue = itemView.findViewById(R.id.betweenSetsValue);
        }
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycle_view_row, parent, false);
        return new ViewHolder(view, mOnDeleteListener, mOnTimerEditListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        TimerData timerSequence = timerData.get(position);
        Resources resources = context.getResources();
        holder.titleValue.setText(resources.getString(R.string.training_name) + ": " + timerSequence.getTitle());
        holder.preparingValue.setText(resources.getString(R.string.preparing_time) + ": " + timerSequence.getPreparationTime());
        holder.workValue.setText(resources.getString(R.string.working_time) + ": " + timerSequence.getWorkingTime());
        holder.restValue.setText(resources.getString(R.string.rest_time) + ": " + timerSequence.getRestTime());
        holder.cyclesValue.setText(resources.getString(R.string.cycles_amount) + ": " + timerSequence.getCyclesAmount());
        holder.setsValue.setText(resources.getString(R.string.sets_amount) + ": " + timerSequence.getSetsAmount());
        holder.betweenSetsValue.setText(resources.getString(R.string.rest_between_sets) + ": " + timerSequence.getBetweenSetsRest());
        holder.calmDownValue.setText(resources.getString(R.string.calmdown_time) + ": " + timerSequence.getCalmDown());
//        holder.setBackgroundColor(timerSequence.getColor());
        holder.cardView.setCardBackgroundColor(timerSequence.getColor());
    }

    public void setTimer(ArrayList<TimerData> timer) {
        this.timerData = timer;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return timerData.size();
    }

    interface OnTimerEditListener {
        void onTimerEditClick(int position);
    }

    interface OnItemDeleteListener {
        void onItemDelete(int position);
    }
}
