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

public class PatientExerciseListAdapter extends RecyclerView.Adapter<PatientExerciseListAdapter.PatientExerciseListViewHolder> {
    private ExerciseListAdapter.OnItemClickListener mListener;
    private List<CreateExerciseNote> createExerciseNotes = new ArrayList<>();

    @NonNull
    @NotNull
    @Override
    public PatientExerciseListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_exercise_patient_view,parent,false);
        return new PatientExerciseListAdapter.PatientExerciseListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PatientExerciseListViewHolder holder, int position) {
        CreateExerciseNote currentNote = createExerciseNotes.get(position);
        holder.exerciseText.setText(currentNote.getExerciseTitle());
        holder.jointText.setText(currentNote.getJoint());
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
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = (ExerciseListAdapter.OnItemClickListener) listener;
    }



    class PatientExerciseListViewHolder extends RecyclerView.ViewHolder {
        private TextView exerciseText, jointText;


        public PatientExerciseListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            exerciseText = itemView.findViewById(R.id.patient_exercise_name);
            jointText = itemView.findViewById(R.id.patient_exercise_joint);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (mListener!= null && position!=RecyclerView.NO_POSITION) {
                        mListener.onItemClick(createExerciseNotes.get(position));
                    }
                }
            });
        }
    }
}
