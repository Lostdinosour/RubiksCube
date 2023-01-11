package me.rubik.rubikscube.ui.solver;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.rubik.rubikscube.R;
import me.rubik.rubikscube.databinding.FragmentSolverBinding;
import me.rubik.rubikscube.solver.Search;
import me.rubik.rubikscube.solver.Solver;
import me.rubik.rubikscube.utils.Side;

public class SolverFragment extends Fragment {

    private static final String TAG = "AdsActivity";

    private RewardedInterstitialAd mInterstitialAd;
    private Button mGetSolutionButton;
    private FragmentSolverBinding binding;

    public static Map<String, ArrayList<Integer>> cubeArray;
    private OnClickListener listener;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        firstDraw = true;
        binding = FragmentSolverBinding.inflate(inflater, container, false);
        mGetSolutionButton = binding.buttonGetSolution;
        mGetSolutionButton.setEnabled(false);

        setButtonListeners();

        listener = new OnClickListener();
        binding.getRoot().setOnTouchListener(listener);
        if (cubeArray == null) {
            cubeArray = createDefaultCubeArray();
        }
        redrawSquares();

        loadInterstitialAd();
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
                    int check = new Search().verify(generateCube());
                    if (check != 0) {
                        String error;
                        if (check == -2) {
                            error = "Not all 12 edges exist exactly once";
                        } else if (check == -3) {
                            error = "One edge has to be flipped";
                        } else if (check == -4) {
                            error = "Not all corners exist exactly once";
                        } else if (check == -5) {
                            error = "One corner has to be twisted";
                        } else if (check == -6) {
                            error = "Two corners or two edges have to be exchanged";
                        } else {
                            error = "An unknown error has occurred";
                        }

                        Toast toast = Toast.makeText(getContext(), error, Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        showInterstitial();
                    }
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

    private ImageView imageView;

    private void redrawSquares() {
        binding.getRoot().removeView(imageView);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        squareSize = (int) (screenWidth * 0.29 / 3);

        imageView = new ImageView(getActivity());
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

    public void loadInterstitialAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedInterstitialAd.load(getActivity(), getString(R.string.interstitial_ad_unit_id), adRequest, new RewardedInterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedInterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                mGetSolutionButton.setEnabled(true);

                interstitialAd.setFullScreenContentCallback(
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d(TAG, "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d(TAG, "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                Log.d(TAG, "The ad was shown.");
                            }
                        });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mGetSolutionButton.setEnabled(true);
                mInterstitialAd = null;
                Toast.makeText(getActivity(), "ad failed to load", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showInterstitial() {
        // Show the ad if it"s ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null) {
            mInterstitialAd.show(getActivity(), new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    generateSolution();
                }
            });

            loadInterstitialAd();
        } else {
            Toast.makeText(getActivity(), "Ad failed to load, try again later", Toast.LENGTH_LONG).show();
            loadInterstitialAd();
        }
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

        ArrayList<Integer> up = SolverFragment.cubeArray.get("up");
        ArrayList<Integer> left = SolverFragment.cubeArray.get("left");
        ArrayList<Integer> front = SolverFragment.cubeArray.get("front");
        ArrayList<Integer> right = SolverFragment.cubeArray.get("right");
        ArrayList<Integer> down = SolverFragment.cubeArray.get("down");
        ArrayList<Integer> back = SolverFragment.cubeArray.get("back");

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
        if (color == getActivity().getColor(R.color.red)) {
            return "R";
        } else if (color == getActivity().getColor(R.color.green)) {
            return "F";
        } else if (color == getActivity().getColor(R.color.white)) {
            return "U";
        } else if (color == getActivity().getColor(R.color.blue)) {
            return "B";
        } else if (color == getActivity().getColor(R.color.orange)) {
            return "L";
        } else if (color == getActivity().getColor(R.color.yellow)) {
            return"D";
        }

        return "";
    }


}