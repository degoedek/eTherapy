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

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {
    private OnItemClickListener mListener;
    private List<Patient> patients = new ArrayList<>();

    public PatientAdapter() {

    }
    @NonNull
    @NotNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_patient,parent,false);
        return new PatientViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PatientViewHolder holder, int position) {
        Patient currentPatient = patients.get(position);
        holder.patientName.setText(currentPatient.getLastName()+", "+currentPatient.getFirstName());
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public void setPatients(List<Patient> patients) {
        this.patients=patients;
        notifyDataSetChanged();
    }

    public Patient getPatientAt(int position) {
        return patients.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(Patient patient);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener=listener;
    }

    public class PatientViewHolder extends  RecyclerView.ViewHolder{
        private TextView patientName;

        public PatientViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            patientName= itemView.findViewById(R.id.patient_item_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (mListener!= null && position!=RecyclerView.NO_POSITION) {
                        mListener.onItemClick(patients.get(position));
                    }
                }
            });

        }
    }
}
