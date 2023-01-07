package me.rubik.rubikscube.ui.solver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import me.rubik.rubikscube.R;
import me.rubik.rubikscube.camera.InsertCameraCubeActivity;
import me.rubik.rubikscube.databinding.ActivityInsertSideBinding;
import me.rubik.rubikscube.utils.Side;
import me.rubik.rubikscube.utils.Squares;

public class InsertSideActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;

    private ActivityInsertSideBinding binding;
    private Side side;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertSideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        intent = getIntent();
        side = Side.intToSide(intent.getIntExtra("side", 1));
        this.setColors();

        setButtonListeners();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Insert side");

        gestureDetector = new GestureDetector(this, new GestureListener());
    }

    private void setButtonListeners() {
        binding.buttonTopLeft.setOnClickListener(new ClickListener(Squares.topLeft));
        binding.buttonTopCenter.setOnClickListener(new ClickListener(Squares.topCenter));
        binding.buttonTopRight.setOnClickListener(new ClickListener(Squares.topRight));
        binding.buttonLeftCenter.setOnClickListener(new ClickListener(Squares.leftCenter));
        binding.buttonRightCenter.setOnClickListener(new ClickListener(Squares.rightCenter));
        binding.buttonBottomLeft.setOnClickListener(new ClickListener(Squares.bottomLeft));
        binding.buttonBottomCenter.setOnClickListener(new ClickListener(Squares.bottomCenter));
        binding.buttonBottomRight.setOnClickListener(new ClickListener(Squares.bottomRight));

        binding.buttonCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(InsertSideActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(InsertSideActivity.this, new String[] { Manifest.permission.CAMERA }, 100);
            }


            Intent myIntent = new Intent(InsertSideActivity.this, InsertCameraCubeActivity.class);
            myIntent.putExtra("centerColor", binding.buttonCenter.getCurrentTextColor());
            startActivity(myIntent);

        });
    }

    private void setColors() {
        ArrayList<Integer> colors = InsertCubeActivity.cubeArray.get(side.name());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent myIntent = new Intent(this, InsertCubeActivity.class);
            startActivity(myIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ClickListener implements View.OnClickListener {

        private final Squares square;

        public ClickListener(Squares square) {
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
                InsertCubeActivity.cubeArray.get(side.name()).set(clickedSquare, color);
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