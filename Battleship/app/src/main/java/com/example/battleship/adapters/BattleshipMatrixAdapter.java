package com.example.battleship.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.battleship.R;
import com.example.battleship.helpers.Constants;
import com.example.battleship.models.BattleshipMatrix;

public class BattleshipMatrixAdapter extends RecyclerView.Adapter<BattleshipMatrixAdapter.ViewHolder> {
    BattleshipMatrix matrix;
    Context context;
    OnCellClickListener mOnCellClickListener;
    boolean isOpponentMatrix;
    boolean clickAllowed;

    public void UpdateMatrix(BattleshipMatrix matrix){
        this.matrix = matrix;
        notifyDataSetChanged();
    }

    public void SetClickable(boolean clickable){
        this.clickAllowed = clickable;
        notifyDataSetChanged();
    }

    public BattleshipMatrixAdapter (Context context, BattleshipMatrix matrix, boolean isOpponentMatrix,
                         OnCellClickListener onCellClickListener, boolean clickAllowed){
        this.isOpponentMatrix = isOpponentMatrix;
        this.matrix = matrix;
        this.context = context;
        this.mOnCellClickListener = onCellClickListener;
        this.clickAllowed = clickAllowed;
    }

    @NonNull
    @Override
    public BattleshipMatrixAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.battleship_matrix_cell_item,
                parent, false);
        int width = parent.getWidth() / 10;
        int height = parent.getHeight() / 10;
        view.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        return new ViewHolder(view, isOpponentMatrix);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(BattleshipMatrixAdapter.ViewHolder holder, int position) {
        int row = position / 10;
        int column = position % 10;

        holder.cellImageView.setImageResource(R.drawable.square);
        View.OnClickListener onCellClicked = v -> {
            if (matrix.matrix[row][column].type == Constants.SHIP_CELL) {
                holder.cellImageView.setImageResource(R.drawable.hit_marker);
                mOnCellClickListener.onCellClicked(row, column, Constants.RESULT_HIT);
                matrix.matrix[row][column].type = Constants.HIT_CELL;
            } else if (matrix.matrix[row][column].type == Constants.NEARBY_CELL ||
                    matrix.matrix[row][column].type == Constants.EMPTY_CELL) {
                holder.cellImageView.setImageResource(R.drawable.miss_marker);
                mOnCellClickListener.onCellClicked(row, column, Constants.RESULT_MISS);
            }

        };

        if (!isOpponentMatrix) {
            if (matrix.matrix[row][column].type == Constants.SHIP_CELL)
                holder.cellImageView.setBackgroundColor(R.color.black);
        }
        else {
            if (clickAllowed)
                holder.cellImageView.setOnClickListener(onCellClicked);
        }

        if (matrix.matrix[row][column].type == Constants.HIT_CELL) {
            holder.cellImageView.setImageResource(R.drawable.hit_marker);
        } else if (matrix.matrix[row][column].type == Constants.CHECKED_CELL) {
            holder.cellImageView.setImageResource(R.drawable.miss_marker);
        }
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cellImageView;

        ViewHolder(View view, boolean isOpponentMatrix) {
            super(view);
            cellImageView = view.findViewById(R.id.cell_image_view);
            view.setClickable(isOpponentMatrix);
        }

    }
}

interface OnCellClickListener{
    void onCellClicked(int row, int column, int result);
}