package me.rubic.rubikscube.ui.solver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.rubic.rubikscube.MainActivity;
import me.rubic.rubikscube.R;
import me.rubic.rubikscube.databinding.ActivityInsertCubeBinding;
import me.rubic.rubikscube.utils.CubeEnum;

public class InsertCubeActivity extends AppCompatActivity {
    private ActionBar actionBar;

    private ActivityInsertCubeBinding binding;

    public static Map<String, ArrayList<Integer>> cubeArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertCubeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonCubeUp.setOnClickListener(new ClickListener(CubeEnum.sideUp));
        binding.buttonCubeLeft.setOnClickListener(new ClickListener(CubeEnum.sideLeft));
        binding.buttonCubeFront.setOnClickListener(new ClickListener(CubeEnum.sideFront));
        binding.buttonCubeRight.setOnClickListener(new ClickListener(CubeEnum.sideRight));
        binding.buttonCubeDown.setOnClickListener(new ClickListener(CubeEnum.sideDown));
        binding.buttonCubeBack.setOnClickListener(new ClickListener(CubeEnum.sideBack));
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Insert cube");

        if (cubeArray == null) {
            cubeArray = createDefaultCubeArray();
        }
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
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent myIntent = new Intent(this, MainActivity.class);
                startActivity(myIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ClickListener implements View.OnClickListener {
        private final CubeEnum side;

        public ClickListener(CubeEnum side) {
            this.side = side;
        }

        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(InsertCubeActivity.this, InsertSideActivity.class);
            myIntent.putExtra("side", SideToInt(side));
            InsertCubeActivity.this.startActivity(myIntent);
        }
    }

    private int SideToInt(CubeEnum side) {
        switch (side) {
            case sideUp:
                return 1;
            case sideLeft:
                return 2;
            case sideFront:
                return 3;
            case sideRight:
                return 4;
            case sideDown:
                return 5;
            case sideBack:
                return 6;
        }
        return 1;
    }
}