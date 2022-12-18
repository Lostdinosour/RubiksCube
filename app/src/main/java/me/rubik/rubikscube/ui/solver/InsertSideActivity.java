package me.rubik.rubikscube.ui.solver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import me.rubik.rubikscube.MainActivity;
import me.rubik.rubikscube.R;
import me.rubik.rubikscube.camera.InsertCameraCubeActivity;
import me.rubik.rubikscube.databinding.ActivityInsertSideBinding;
import me.rubik.rubikscube.utils.CubeEnum;

public class InsertSideActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private GestureDetector gestureDetector;

    private ActivityInsertSideBinding binding;
    private CubeEnum side;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertSideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();
        side = IntToSide(intent.getIntExtra("side", 1));
        this.setColors();

        binding.buttonTopLeft.setOnClickListener(new ClickListener(CubeEnum.topLeft));
        binding.buttonTopCenter.setOnClickListener(new ClickListener(CubeEnum.topCenter));
        binding.buttonTopRight.setOnClickListener(new ClickListener(CubeEnum.topRight));
        binding.buttonLeftCenter.setOnClickListener(new ClickListener(CubeEnum.leftCenter));
        binding.buttonRightCenter.setOnClickListener(new ClickListener(CubeEnum.rightCenter));
        binding.buttonBottomLeft.setOnClickListener(new ClickListener(CubeEnum.bottomLeft));
        binding.buttonBottomCenter.setOnClickListener(new ClickListener(CubeEnum.bottomCenter));
        binding.buttonBottomRight.setOnClickListener(new ClickListener(CubeEnum.bottomRight));

        binding.buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(InsertSideActivity.this, InsertCameraCubeActivity.class);
                startActivity(myIntent);
            }
        });

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Insert side");

        gestureDetector = new GestureDetector(this, new GestureListener());
    }

    private void setColors() {
        ArrayList<Integer> colors = InsertCubeActivity.cubeArray.get(SideToString(side));

        binding.buttonTopLeft.setBackgroundColor(colors.get(0));
        binding.buttonTopLeft.setTextColor(colors.get(0));
        binding.buttonTopCenter.setBackgroundColor(colors.get(1));
        binding.buttonTopCenter.setTextColor(colors.get(1));
        binding.buttonTopRight.setBackgroundColor(colors.get(2));
        binding.buttonTopRight.setTextColor(colors.get(2));
        binding.buttonLeftCenter.setBackgroundColor(colors.get(3));
        binding.buttonLeftCenter.setTextColor(colors.get(3));
        binding.buttonCenter.setBackgroundColor(colors.get(4));
        binding.buttonCenter.setTextColor(colors.get(4));
        binding.buttonRightCenter.setBackgroundColor(colors.get(5));
        binding.buttonRightCenter.setTextColor(colors.get(5));
        binding.buttonBottomLeft.setBackgroundColor(colors.get(6));
        binding.buttonBottomLeft.setTextColor(colors.get(6));
        binding.buttonBottomCenter.setBackgroundColor(colors.get(7));
        binding.buttonBottomCenter.setTextColor(colors.get(7));
        binding.buttonBottomRight.setBackgroundColor(colors.get(8));
        binding.buttonBottomRight.setTextColor(colors.get(8));
    }

    private CubeEnum IntToSide(int number) {
        switch (number) {
            case 1:
                return CubeEnum.sideUp;
            case 2:
                return CubeEnum.sideLeft;
            case 3:
                return CubeEnum.sideFront;
            case 4:
                return CubeEnum.sideRight;
            case 5:
                return CubeEnum.sideDown;
            case 6:
                return CubeEnum.sideBack;
        }

        return CubeEnum.sideUp;
    }
    private String SideToString(CubeEnum side) {
        switch (side) {
            case sideUp:
                return "up";
            case sideLeft:
                return "left";
            case sideFront:
                return "front";
            case sideRight:
                return "right";
            case sideDown:
                return "down";
            case sideBack:
                return "back";
        }

        return "up";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent myIntent = new Intent(this, InsertCubeActivity.class);
                startActivity(myIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ClickListener implements View.OnClickListener {

        private final CubeEnum square;

        public ClickListener(CubeEnum square) {
            this.square = square;
        }

        @Override
        public void onClick(View view) {
            int color;

            Button button = null;
            int clickedSquare = 0;

            switch (square) {
                case topLeft:
                    button = binding.buttonTopLeft;
                    break;
                case topCenter:
                    button = binding.buttonTopCenter;
                    clickedSquare = 1;
                    break;
                case topRight:
                    button = binding.buttonTopRight;
                    clickedSquare = 2;
                    break;
                case leftCenter:
                    button = binding.buttonLeftCenter;
                    clickedSquare = 3;
                    break;
                case rightCenter:
                    button = binding.buttonRightCenter;
                    clickedSquare = 5;
                    break;
                case bottomLeft:
                    button = binding.buttonBottomLeft;
                    clickedSquare = 6;
                    break;
                case bottomCenter:
                    button = binding.buttonBottomCenter;
                    clickedSquare = 7;
                    break;
                case bottomRight:
                    button = binding.buttonBottomRight;
                    clickedSquare = 8;
                    break;

            }
            if (button != null) {
                int currentColor = button.getCurrentTextColor();
                color = getNextColor(currentColor);
                button.setBackgroundColor(color);
                button.setTextColor(color);
                InsertCubeActivity.cubeArray.get(SideToString(side)).set(clickedSquare, color);
            }
        }

        private int getNextColor(int currentColor) {
            if (currentColor == getColor(R.color.white)) {
                return getColor(R.color.orange);
            } else if (currentColor == getColor(R.color.orange)) {
                return getColor(R.color.green);
            } else if (currentColor == getColor(R.color.green)) {
                return getColor(R.color.red);
            } else if (currentColor == getColor(R.color.red)) {
                return getColor(R.color.yellow);
            } else if (currentColor == getColor(R.color.yellow)) {
                return getColor(R.color.blue);
            } else {
                return getColor(R.color.white);
            }
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (velocityX > 1) {
                int nextSide = getNextSide("right");
                Intent myIntent = new Intent(InsertSideActivity.this, InsertSideActivity.class);
                myIntent.putExtra("side", nextSide);
                InsertSideActivity.this.startActivity(myIntent);
                overridePendingTransition(R.anim.animation_slide_right_enter, R.anim.animation_slide_right_exit);
            }

            else if (velocityX < -1) {
                int nextSide = getNextSide("left");
                Intent myIntent = new Intent(InsertSideActivity.this, InsertSideActivity.class);
                myIntent.putExtra("side", nextSide);
                InsertSideActivity.this.startActivity(myIntent);
                overridePendingTransition(R.anim.animation_slide_left_enter, R.anim.animation_slide_left_exit);
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private int getNextSide(String direction) {

        int currentSide = intent.getIntExtra("side", 1);
        int nextSide;

        if (direction.equals("right")) {
            if (currentSide == 6) {
                nextSide = 1;
            } else {
                nextSide = currentSide + 1;
            }
        }

        else {
            if (currentSide == 1) {
                nextSide = 6;
            } else {
                nextSide = currentSide - 1;
            }
        }

        return nextSide;
    }
}