package me.rubic.rubikscube.ui.solver;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import me.rubic.rubikscube.InsertCubeActivity;
import me.rubic.rubikscube.databinding.FragmentSolverBinding;

public class SolverFragment extends Fragment {

    private FragmentSolverBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SolverViewModel solverViewModel = new ViewModelProvider(this).get(SolverViewModel.class);

        binding = FragmentSolverBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSolver;
        solverViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        binding.buttonInsertCube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), InsertCubeActivity.class);
                getActivity().startActivity(myIntent);
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