package me.rubik.rubikscube.ui.solver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.rubik.rubikscube.MainActivity;
import me.rubik.rubikscube.R;
import me.rubik.rubikscube.databinding.ActivityInsertCubeBinding;
import me.rubik.rubikscube.utils.Side;

public class InsertCubeActivity extends AppCompatActivity {

    ActivityInsertCubeBinding binding;

    public static Map<String, ArrayList<Integer>> cubeArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertCubeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setButtonListeners();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Insert cube");

        if (cubeArray == null) {
            cubeArray = createDefaultCubeArray();
        }
    }

    private void setButtonListeners() {
        binding.buttonCubeUp.setOnClickListener(new ClickListener(Side.up));
        binding.buttonCubeLeft.setOnClickListener(new ClickListener(Side.left));
        binding.buttonCubeFront.setOnClickListener(new ClickListener(Side.front));
        binding.buttonCubeRight.setOnClickListener(new ClickListener(Side.right));
        binding.buttonCubeDown.setOnClickListener(new ClickListener(Side.down));
        binding.buttonCubeBack.setOnClickListener(new ClickListener(Side.back));
    }

    public Map<String, ArrayList<Integer>> createDefaultCubeArray() {
        Map<String, ArrayList<Integer>> cubeArray = new HashMap<>();
        int[] colors = new int[] {getColor(R.color.white), getColor(R.color.orange), getColor(R.color.green), getColor(R.color.red), getColor(R.color.yellow), getColor(R.color.blue)};
        String[] names = new String[] {"up", "left", "front", "right", "down", "back"};

        for (int i = 0; i < 6; i++) {
            int color = colors[i];
            ArrayList<Integer> list = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                list.add(color);
            }
            cubeArray.put(names[i], list);
        }

        return cubeArray;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ClickListener implements View.OnClickListener {
        private final Side side;

        public ClickListener(Side side) {
            this.side = side;
        }

        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(InsertCubeActivity.this, InsertSideActivity.class);
            myIntent.putExtra("side", side.integerValue());
            InsertCubeActivity.this.startActivity(myIntent);
        }
    }
}