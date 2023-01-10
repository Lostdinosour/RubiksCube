package me.rubik.rubikscube.ui.solver;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.rubik.rubikscube.R;
import me.rubik.rubikscube.databinding.FragmentSolverBinding;
import me.rubik.rubikscube.utils.Side;

public class SolverFragment extends Fragment {

    private FragmentSolverBinding binding;

    public static Map<String, ArrayList<Integer>> cubeArray;
    private OnClickListener listener;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        firstDraw = true;
        binding = FragmentSolverBinding.inflate(inflater, container, false);
        initDrawing();
        setButtonListeners();

        listener = new OnClickListener();
        binding.getRoot().setOnTouchListener(listener);
        if (cubeArray == null) {
            cubeArray = createDefaultCubeArray();
        }
        redrawSquares();

        return binding.getRoot();
    }

    public void setButtonListeners() {
        binding.buttonGetSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIfPossible()) {
                    Toast toast = Toast.makeText(getContext(), "Impossible cube", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Intent myIntent = new Intent(getActivity(), GetSolutionActivity.class);
                    getActivity().startActivity(myIntent);
                }
            }
        });
    }

    private boolean checkIfPossible() {
        int redCount = 0;
        int greenCount = 0;
        int whiteCount = 0;
        int blueCount = 0;
        int orangeCount = 0;
        int yellowCount = 0;

        Iterator<ArrayList<Integer>> iterator = cubeArray.values().iterator();

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

    public Map<String, ArrayList<Integer>> createDefaultCubeArray() {
        Map<String, ArrayList<Integer>> cubeArray = new HashMap<>();
        int[] colors = new int[] {getActivity().getColor(R.color.white), getActivity().getColor(R.color.orange), getActivity().getColor(R.color.green), getActivity().getColor(R.color.red), getActivity().getColor(R.color.yellow), getActivity().getColor(R.color.blue)};
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

    private boolean firstDraw = true;

    int screenWidth, screenHeight;
    int squareSize;

    DisplayMetrics metrics;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    Paint border;

    private void initDrawing() {
        metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        squareSize = (int) (screenWidth * 0.29 / 3);

        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new ConstraintLayout.LayoutParams(screenWidth, screenHeight));
        binding.getRoot().addView(imageView);
        bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        imageView.setImageBitmap(bitmap);
        canvas = new Canvas(bitmap);
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        border = new Paint();
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(5);
        border.setColor(0xFF000000);
    }

    private void redrawSquares() {
        System.out.println("test");
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
        if (firstDraw) {
        for (int i = 0; i < 6; i++) {
            int topX;
            int topY;
            Side side;

            if (i == 0) {
                topX = (int) ((screenWidth / 2f) - (squareSize * 3 / 2f));
                topY = (int) (screenHeight * 0.15);
                side = Side.up;
            } else if (i == 1) {
                topX = (int) ((screenWidth / 2f) - (squareSize * 3 / 2f) - (squareSize * 3));
                topY = (int) (screenHeight * 0.15) + (squareSize * 3);
                side = Side.left;
            } else if (i == 2) {
                topX = (int) ((screenWidth / 2f) - (squareSize * 3 / 2f));
                topY = (int) (screenHeight * 0.15) + (squareSize * 3);
                side = Side.front;
            } else if (i == 3) {
                topX = (int) ((screenWidth / 2f) - (squareSize * 3 / 2f) + (squareSize * 3));
                topY = (int) (screenHeight * 0.15) + (squareSize * 3);
                side = Side.right;
            } else if (i == 4) {
                topX = (int) ((screenWidth / 2f) - (squareSize * 3 / 2f));
                topY = (int) (screenHeight * 0.15) + (squareSize * 3 * 2);
                side = Side.down;
            } else {
                topX = (int) ((screenWidth / 2f) - (squareSize * 3 / 2f));
                topY = (int) (screenHeight * 0.15) + (squareSize * 3 * 3);
                side = Side.back;
            }

            ArrayList<Map<String, Integer>> squares = new ArrayList<>();
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
                        Map<String, Integer> map = new HashMap<>();
                        map.put("left", left);
                        map.put("top", top);
                        map.put("right", right);
                        map.put("bottom", bottom);
                        map.put("color", color);
                        map.put("side", side.integerValue());
                        map.put("square", square);
                        squares.add(map);
                    }

                }

            }
            if (firstDraw) {
                listener.addSide(topX, topY, topX + squareSize * 3, topY + squareSize * 3, squares);
            }
        }
        }

        firstDraw = false;
    }

    private class OnClickListener implements View.OnTouchListener {
        private final ArrayList<ArrayList<Map<String, Integer>>> list = new ArrayList<>();


        public void addSide(int left, int top, int right, int bottom, ArrayList<Map<String, Integer>> squares) {;
            Map<String, Integer> coordsMap = new HashMap<>();

            coordsMap.put("left", left);
            coordsMap.put("top", top);
            coordsMap.put("right", right);
            coordsMap.put("bottom", bottom);

            squares.add(0, coordsMap);

            list.add(squares);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int x = (int) event.getX();
                        int y = (int) event.getY();

                        if (x < ((screenWidth / 2f) - (squareSize * 3 / 2f) - (squareSize * 3)) || x > ((screenWidth / 2f) - (squareSize * 3 / 2f) + (squareSize * 3)) + squareSize * 3) {
                            return;
                        } else if (y < (screenHeight * 0.15) || y > (screenHeight * 0.15) + (squareSize * 3 * 3) + squareSize * 3) {
                            return;
                        }

                        int count = 0;
                        for (ArrayList<Map<String, Integer>> side : list) {
                            count++;
                            if (x >= side.get(0).get("left") && x <= side.get(0).get("right")) {
                                if (y >= side.get(0).get("top") && y <= side.get(0).get("bottom")) {
                                    count++;
                                    for (Map<String, Integer> map : side) {
                                        if (map.size() == 4) continue;
                                        if (x >= map.get("left") && x <= map.get("right")) {
                                            if (y >= map.get("top") && y <= map.get("bottom")) {
                                                int color = OnClickListener.this.getNextColor(map.get("color"));

                                                cubeArray.get(Side.intToSide(map.get("side")).name()).set(map.get("square"), color);
                                                map.replace("color", color);
                                                redrawSquares();}
                                        }
                                    }
                                }
                            }
                        }
                        System.out.println(count);
                    }
                });
            }
            return true;
        }

        private int getNextColor(int currentColor){
            if (currentColor == getActivity().getColor(R.color.white)) {
                return getActivity().getColor(R.color.orange);
            } else if (currentColor == getActivity().getColor(R.color.orange)) {
                return getActivity().getColor(R.color.green);
            } else if (currentColor == getActivity().getColor(R.color.green)) {
                return getActivity().getColor(R.color.red);
            } else if (currentColor == getActivity().getColor(R.color.red)) {
                return getActivity().getColor(R.color.yellow);
            } else if (currentColor == getActivity().getColor(R.color.yellow)) {
                return getActivity().getColor(R.color.blue);
            } else {
                return getActivity().getColor(R.color.white);
            }
        }
    }
}