package me.rubik.rubikscube.ui.solver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

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

    private OnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertCubeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Insert cube");

        listener = new OnClickListener();
        binding.getRoot().setOnTouchListener(listener);
        if (cubeArray == null) {
            cubeArray = createDefaultCubeArray();
        }
        redrawSquares();
    }
    private boolean firstDraw = true;

    private void redrawSquares() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        int squareSize = (int) (screenWidth * 0.29 / 3);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ConstraintLayout.LayoutParams(screenWidth, screenHeight));
        binding.getRoot().addView(imageView);
        Bitmap bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        imageView.setImageBitmap(bitmap);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        Paint border = new Paint();
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(5);
        border.setColor(0xFF000000);

        for (int i = 0; i < 6; i++) {
            int topX;
            int topY;
            Side side;

            if (i == 0) {
                topX = (int) ((screenWidth / 2f) - (squareSize * 3 / 2f));
                topY = (int) (screenHeight * 0.20);
                side = Side.up;
            } else if (i == 1) {
                topX = (int) ((screenWidth / 2f) - (squareSize * 3 / 2f) - (squareSize * 3));
                topY = (int) (screenHeight * 0.20) + (squareSize * 3);
                side = Side.left;
            } else if (i == 2) {
                topX = (int) ((screenWidth / 2f) - (squareSize * 3 / 2f));
                topY = (int) (screenHeight * 0.20) + (squareSize * 3);
                side = Side.front;
            } else if (i == 3) {
                topX = (int) ((screenWidth / 2f) - (squareSize * 3 / 2f) + (squareSize * 3));
                topY = (int) (screenHeight * 0.20) + (squareSize * 3);
                side = Side.right;
            } else if (i == 4) {
                topX = (int) ((screenWidth / 2f) - (squareSize * 3 / 2f));
                topY = (int) (screenHeight * 0.20) + (squareSize * 3 * 2);
                side = Side.down;
            } else {
                topX = (int) ((screenWidth / 2f) - (squareSize * 3 / 2f));
                topY = (int) (screenHeight * 0.20) + (squareSize * 3 * 3);
                side = Side.back;
            }


            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    int square = y * 3 + x;

                    int color = cubeArray.get(side.name()).get(square);
                    paint.setColor(color);

                    int left = topX + (squareSize * x);
                    int top = topY + (squareSize * y);
                    int right = topX + (squareSize * x) + squareSize;
                    int bottom = topY + (squareSize * y) + squareSize;

                    canvas.drawRect(left, top, right, bottom, paint);
                    canvas.drawRect(left, top, right, bottom, border);

                    if (firstDraw) {
                        listener.addSquare(left, top, right, bottom, color, side, square);
                    }

                }

            }
        }
        firstDraw = false;
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

    private class OnClickListener implements View.OnTouchListener {
        private final ArrayList<Map<String, Integer>> squares = new ArrayList<>();


        public void addSquare(int left, int top, int right, int bottom, int color, Side side, int square) {
            Map<String, Integer> map = new HashMap<>();
            map.put("left", left);
            map.put("top", top);
            map.put("right", right);
            map.put("bottom", bottom);
            map.put("side", side.integerValue());
            map.put("square", square);
            map.put("color", color);
            squares.add(map);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                for (Map<String, Integer> map : squares) {
                    if (x >= map.get("left") && x <= map.get("right")) {
                        if (y >= map.get("top") && y <= map.get("bottom")) {
                            int color = getNextColor(map.get("color"));

                            cubeArray.get(Side.intToSide(map.get("side")).name()).set(map.get("square"), color);
                            map.replace("color", color);
                            redrawSquares();
                            return true;
                        }
                    }
                }
                return true;
            }
            return false;
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
}