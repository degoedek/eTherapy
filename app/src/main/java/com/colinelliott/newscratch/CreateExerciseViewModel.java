package com.colinelliott.newscratch;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CreateExerciseViewModel extends AndroidViewModel {
    private NoteRepository repository;
    private LiveData<List<CreateExerciseNote>> allCreateExerciseNotes;

    public CreateExerciseViewModel(@NonNull @NotNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allCreateExerciseNotes = repository.getAllCreateExerciseNotes();
    }
     public void insert(CreateExerciseNote createExerciseNote) {
        repository.insert(createExerciseNote);
     }

     public void update (CreateExerciseNote createExerciseNote) {
        repository.update(createExerciseNote);
     }

    public void delete (CreateExerciseNote createExerciseNote) {
        repository.delete(createExerciseNote);
    }

    public void deleteAllNotes (CreateExerciseNote createExerciseNote) {
        repository.deleteAllNotes();
    }

    public LiveData<List<CreateExerciseNote>> getAllCreateExerciseNotes() {
        return allCreateExerciseNotes;
    }


}
