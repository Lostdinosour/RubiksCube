package me.rubik.rubikscube.ui.solver;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Iterator;

import me.rubik.rubikscube.R;
import me.rubik.rubikscube.databinding.FragmentSolverBinding;

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

        binding.buttonGetSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InsertCubeActivity.cubeArray == null) {
                    Toast toast = Toast.makeText(getContext(),"Insert a cube", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (!checkIfPossible()) {
                    Toast toast = Toast.makeText(getContext(), "Impossible cube", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Intent myIntent = new Intent(getActivity(), GetSolutionActivity.class);
                    getActivity().startActivity(myIntent);
                }
            }
        });
        return root;
    }

    private boolean checkIfPossible() {
        int redCount = 0;
        int greenCount = 0;
        int whiteCount = 0;
        int blueCount = 0;
        int orangeCount = 0;
        int yellowCount = 0;

        Iterator<ArrayList<Integer>> iterator = InsertCubeActivity.cubeArray.values().iterator();

        while (iterator.hasNext()) {
            for (Integer color : iterator.next()) {
                if (color == getActivity().getColor(R.color.red)) {
                    redCount++;
                } else if (color == getActivity().getColor(R.color.green)) {
                    greenCount++;
                } else if (color == getActivity().getColor(R.color.white)) {
                    whiteCount++;
                } else if (color == getActivity().getColor(R.color.blue)) {
                    blueCount++;
                } else if (color == getActivity().getColor(R.color.orange)) {
                    orangeCount++;
                } else if (color == getActivity().getColor(R.color.yellow)) {
                    yellowCount++;
                }
            }
        }

        return redCount == 9 && greenCount == 9 && whiteCount == 9 && blueCount == 9 && orangeCount == 9 && yellowCount == 9;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}