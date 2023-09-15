package com.colinelliott.newscratch;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseListViewHolder> {
    private OnItemClickListener mListener;
    private List<CreateExerciseNote> createExerciseNotes = new ArrayList<>();

    public ExerciseListAdapter() {

    }
    @NonNull
    @NotNull
    @Override
    public ExerciseListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_exercise,parent,false);
        return new ExerciseListViewHolder(v);
        //ExerciseListViewHolder elvh = new ExerciseListViewHolder(v,mListener);
        //return elvh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ExerciseListViewHolder holder, int position) {
        /*ExerciseListItem currentItem = mArrayList.get(position);
        holder.exerciseText.setText(currentItem.getExercise());*/
        CreateExerciseNote currentNote = createExerciseNotes.get(position);
        holder.exerciseText.setText(currentNote.getExerciseTitle());
        holder.jointText.setText(currentNote.getJoint());
        holder.motionText.setText(currentNote.getMotion());

    }

    @Override
    public int getItemCount() {
        return createExerciseNotes.size();
    }

    public void setCreateExerciseNotes(List<CreateExerciseNote> createExerciseNotes) {
        this.createExerciseNotes=createExerciseNotes;
        notifyDataSetChanged();
    }

    public CreateExerciseNote getNoteAt(int position) {
        return createExerciseNotes.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(CreateExerciseNote createExerciseNote);
        //void onItemClicks(CreateExerciseFragment note);
        //void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener=listener;
    }

    class ExerciseListViewHolder extends RecyclerView.ViewHolder {
        private TextView exerciseText;
        private TextView jointText;
        private TextView motionText;

        public ExerciseListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            exerciseText = itemView.findViewById(R.id.list_exercise_name);
            jointText=itemView.findViewById(R.id.list_exercise_joint);
            motionText=itemView.findViewById(R.id.list_exercise_motion);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (mListener!= null && position!=RecyclerView.NO_POSITION) {
                            mListener.onItemClick(createExerciseNotes.get(position));
                        }
                }
            });
            /*delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null) {
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });*/


        }
    }
}
