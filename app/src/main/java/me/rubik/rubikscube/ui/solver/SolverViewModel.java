package me.rubik.rubikscube.ui.solver;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SolverViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SolverViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Solver");
    }

    public LiveData<String> getText() {
        return mText;
    }
}