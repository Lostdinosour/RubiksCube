package me.rubik.rubikscube.ui.solver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

import me.rubik.rubikscube.R;
import me.rubik.rubikscube.databinding.ActivityGetSolutionBinding;
import me.rubik.rubikscube.solver.Solver;

public class GetSolutionActivity extends AppCompatActivity {
    // TODO: error codes in normale text veranderen en zeggen tegen gebruiker
    private ActivityGetSolutionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetSolutionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        generateSolution();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Get solution");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void generateSolution() {
        new Thread(() -> {
            String cube = generateCube();
            String solution = Solver.simpleSolve(cube);
            binding.textViewSolution.setText(solution);
        }).start();
    }

    private String generateCube() {
        StringBuilder scrambledCube = new StringBuilder();

        ArrayList<Integer> up = InsertCubeActivity.cubeArray.get("up");
        ArrayList<Integer> left = InsertCubeActivity.cubeArray.get("left");
        ArrayList<Integer> front = InsertCubeActivity.cubeArray.get("front");
        ArrayList<Integer> right = InsertCubeActivity.cubeArray.get("right");
        ArrayList<Integer> down = InsertCubeActivity.cubeArray.get("down");
        ArrayList<Integer> back = InsertCubeActivity.cubeArray.get("back");

        for (Integer color : up) {
            scrambledCube.append(getMove(color));
        }

        for (Integer color : right) {
            scrambledCube.append(getMove(color));
        }

        for (Integer color : front) {
            scrambledCube.append(getMove(color));
        }

        for (Integer color : down) {
            scrambledCube.append(getMove(color));
        }

        for (Integer color : left) {
            scrambledCube.append(getMove(color));
        }

        StringBuilder tempString = new StringBuilder();
        for (Integer color : back) {
            tempString.append(getMove(color));
        }
        scrambledCube.append(tempString.reverse());

        return scrambledCube.toString();
    }

    private String getMove(int color) {
        if (color == getColor(R.color.red)) {
            return "R";
        } else if (color == getColor(R.color.green)) {
            return "F";
        } else if (color == getColor(R.color.white)) {
            return "U";
        } else if (color == getColor(R.color.blue)) {
            return "B";
        } else if (color == getColor(R.color.orange)) {
            return "L";
        } else if (color == getColor(R.color.yellow)) {
            return"D";
        }

        return "";
    }
}