package me.rubic.rubikscube.ui.timer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import me.rubic.rubikscube.databinding.FragmentTimerBinding;
import me.rubic.rubikscube.solver.Solver;

public class TimerFragment extends Fragment {

    private FragmentTimerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TimerViewModel timerViewModel = new ViewModelProvider(this).get(TimerViewModel.class);

        binding = FragmentTimerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textTimer;
        timerViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}