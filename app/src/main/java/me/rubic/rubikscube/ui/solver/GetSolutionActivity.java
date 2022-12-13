package me.rubic.rubikscube.ui.solver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import me.rubic.rubikscube.R;
import me.rubic.rubikscube.databinding.ActivityGetSolutionBinding;
import me.rubic.rubikscube.databinding.ActivityInsertCubeBinding;
import me.rubic.rubikscube.databinding.ActivityInsertSideBinding;
import me.rubic.rubikscube.solver.Solver;

public class GetSolutionActivity extends AppCompatActivity {

    private ActivityGetSolutionBinding binding;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetSolutionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        generateSolution();

        actionBar = getSupportActionBar();
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
            if (color == getColor(R.color.red)) {
               scrambledCube.append("R");
            } else if (color == getColor(R.color.green)) {
                scrambledCube.append("F");
            } else if (color == getColor(R.color.white)) {
                scrambledCube.append("U");
            } else if (color == getColor(R.color.blue)) {
                scrambledCube.append("B");
            } else if (color == getColor(R.color.orange)) {
                scrambledCube.append("L");
            } else if (color == getColor(R.color.yellow)) {
                scrambledCube.append("D");
            }
        }

        for (Integer color : right) {
            if (color == getColor(R.color.red)) {
                scrambledCube.append("R");
            } else if (color == getColor(R.color.green)) {
                scrambledCube.append("F");
            } else if (color == getColor(R.color.white)) {
                scrambledCube.append("U");
            } else if (color == getColor(R.color.blue)) {
                scrambledCube.append("B");
            } else if (color == getColor(R.color.orange)) {
                scrambledCube.append("L");
            } else if (color == getColor(R.color.yellow)) {
                scrambledCube.append("D");
            }
        }

        for (Integer color : front) {
            if (color == getColor(R.color.red)) {
                scrambledCube.append("R");
            } else if (color == getColor(R.color.green)) {
                scrambledCube.append("F");
            } else if (color == getColor(R.color.white)) {
                scrambledCube.append("U");
            } else if (color == getColor(R.color.blue)) {
                scrambledCube.append("B");
            } else if (color == getColor(R.color.orange)) {
                scrambledCube.append("L");
            } else if (color == getColor(R.color.yellow)) {
                scrambledCube.append("D");
            }
        }

        for (Integer color : down) {
            if (color == getColor(R.color.red)) {
                scrambledCube.append("R");
            } else if (color == getColor(R.color.green)) {
                scrambledCube.append("F");
            } else if (color == getColor(R.color.white)) {
                scrambledCube.append("U");
            } else if (color == getColor(R.color.blue)) {
                scrambledCube.append("B");
            } else if (color == getColor(R.color.orange)) {
                scrambledCube.append("L");
            } else if (color == getColor(R.color.yellow)) {
                scrambledCube.append("D");
            }
        }

        for (Integer color : left) {
            if (color == getColor(R.color.red)) {
                scrambledCube.append("R");
            } else if (color == getColor(R.color.green)) {
                scrambledCube.append("F");
            } else if (color == getColor(R.color.white)) {
                scrambledCube.append("U");
            } else if (color == getColor(R.color.blue)) {
                scrambledCube.append("B");
            } else if (color == getColor(R.color.orange)) {
                scrambledCube.append("L");
            } else if (color == getColor(R.color.yellow)) {
                scrambledCube.append("D");
            }
        }

        StringBuilder tempString = new StringBuilder();
        for (Integer color : back) {

            if (color == getColor(R.color.red)) {
                tempString.append("R");
            } else if (color == getColor(R.color.green)) {
                tempString.append("F");
            } else if (color == getColor(R.color.white)) {
                tempString.append("U");
            } else if (color == getColor(R.color.blue)) {
                tempString.append("B");
            } else if (color == getColor(R.color.orange)) {
                tempString.append("L");
            } else if (color == getColor(R.color.yellow)) {
                tempString.append("D");
            }
        }
        scrambledCube.append(tempString.reverse());

        return scrambledCube.toString();
    }
}