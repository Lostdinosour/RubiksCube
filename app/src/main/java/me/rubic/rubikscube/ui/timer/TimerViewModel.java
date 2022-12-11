package me.rubic.rubikscube.ui.timer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TimerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Timer");
    }

    public LiveData<String> getText() {
        return mText;
    }
}