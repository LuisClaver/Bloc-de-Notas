package com.example.practicaonce;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<String> notes;
    private OnNoteListener onNoteListener;


    public interface OnNoteListener {
        void onDeleteClick(int position);
        void onNoteClick(int position);
    }

    public NoteAdapter(List<String> notes, OnNoteListener onNoteListener) {
        this.notes = notes;
        this.onNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        String note = notes.get(position);
        String firstLine = note.split("\n")[0];
        holder.notePreviewTextView.setText(firstLine);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView notePreviewTextView;
        ImageButton deleteButton;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notePreviewTextView = itemView.findViewById(R.id.notePreviewTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            deleteButton.setOnClickListener(v -> {
                if (onNoteListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onNoteListener.onDeleteClick(position);
                    }
                }
            });


            itemView.setOnClickListener(v -> {
                if (onNoteListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onNoteListener.onNoteClick(position);
                    }
                }
            });
        }
    }
}