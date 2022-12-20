package me.rubik.rubikscube.camera;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.Map;

import me.rubik.rubikscube.databinding.ActivityInsertCameraCubeBinding;
import me.rubik.rubikscube.ui.solver.InsertCubeActivity;
import me.rubik.rubikscube.ui.solver.InsertSideActivity;

public class InsertCameraCubeActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private ActionBar actionBar;
    private PortraitCameraView camera;

    private ActivityInsertCameraCubeBinding binding;

    private ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;

    private int screenWidth, screenHeight;

    private int centerColor;

    int size = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertCameraCubeBinding.inflate(getLayoutInflater());
        Intent intent = getIntent();
        centerColor = intent.getIntExtra("centerColor", 0);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        imageView = new ImageView(this);
        imageView.setLayoutParams(new ConstraintLayout.LayoutParams(screenWidth, screenHeight));
        binding.getRoot().addView(imageView);
        bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        imageView.setImageBitmap(bitmap);
        canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        canvas.drawRect((screenWidth / 2f) - (size / 2f), (screenHeight / 2f) - (size / 2f), (screenWidth / 2f) + (size / 2f), (screenHeight / 2f) + (size / 2f), paint);

        setContentView(binding.getRoot());

        camera = binding.JavaCameraViewCamera;
        camera.setCameraIndex(0);
        camera.setCvCameraViewListener(this);
        camera.enableView();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Intent myIntent = new Intent(this, InsertCubeActivity.class);
            startActivity(myIntent);
            Toast.makeText(this, "Camera permission required", Toast.LENGTH_LONG).show();
        } else {
            camera.setCameraPermissionGranted();
        }

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Camera");

        binding.buttonTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int per = size / 3;

                int YTop = (int) (getY(screenHeight, mat.height()) - size / 2f);
                int YBottom = (int) (getY(screenHeight, mat.height()) + size / 2f);

                int XTop = (int) (getX(screenWidth, mat.width()) - size / 2f);
                int XBottom = (int) (getX(screenWidth, mat.width()) + size / 2f);

                ArrayList<Integer> squares = new ArrayList<>();

                for (int i = 0; i < 3; i++) {
                    int YTop2 = YTop + i * per;
                    int YBottom2 = YBottom - (3 - (i + 1)) * per;
                    for (int j = 0; j < 3; j++) {
                        int XTop2 = XTop + j * per;
                        int XBottom2 = XBottom - (3 - (j + 1)) * per;

                        double[] color = getAverage(mat, XTop2, XBottom2, YTop2, YBottom2);
                        System.out.println(color[0]);
                        System.out.println(color[1]);
                        System.out.println(color[2]);
                        color[0] *= 13;
                        color[1] *= 13;
                        color[2] *= 13;
                        int col = getColor(color);
                        squares.add(col);

                    }
                }

                    Map<String, ArrayList<Integer>> cubeArray = InsertCubeActivity.cubeArray;
                    ArrayList<Integer> side = cubeArray.get(ColorToSide(centerColor));

                    side.set(0, squares.get(0));
                    side.set(1, squares.get(1));
                    side.set(2, squares.get(2));
                    side.set(3, squares.get(3));
                    side.set(5, squares.get(5));
                    side.set(6, squares.get(6));
                    side.set(7, squares.get(7));
                    side.set(8, squares.get(8));

                    Intent myIntent = new Intent(InsertCameraCubeActivity.this, InsertSideActivity.class);
                    myIntent.putExtra("side", ColorToInt(centerColor));
                    startActivity(myIntent);

            }
        });
    }

    private String ColorToSide(int color) {
        switch (color) {
            case 16777215:
                return "up";
            case 16734208:
                return "left";
            case 39752:
                return "front";
            case 11997748:
                return "right";
            case 16766208:
                return "bottom";
            case 18093:
                return "back";
        }
        return "up";
    }

    private int ColorToInt(int color) {
        switch (color) {
            case 16777215:
                return 1;
            case 16734208:
                return 2;
            case 39752:
                return 3;
            case 11997748:
                return 4;
            case 16766208:
                return 5;
            case 18093:
                return 6;
        }
        return 1;
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

    @Override
    protected void onPause() {
        super.onPause();
        camera.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.enableView();
    }

    @Override
    protected void onDestroy() {
        camera.disableView();
        super.onDestroy();
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    private Mat mat;

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
        mat = frame.rgba();
        return mat;
    }

    private int getX(int screenWidth, int cameraWidth) {
        float verhouding = ((float) cameraWidth) / screenWidth;

        return (int) (cameraWidth * verhouding / 2f);
    }

    private int getY(int screenHeight, int cameraHeight) {
        float verhouding = ((float) cameraHeight) / screenHeight;

        return (int) (cameraHeight * verhouding / 2f - 500 / 2f);
    }

    private double[] getAverage(Mat mat, int YTop, int YBottom, int XTop, int XBottom) {
        double RTotal = 0;
        double GTotal = 0;
        double BTotal = 0;

        for (int y = YTop; y <= YBottom; y++) {
            for (int x = XTop; x <= XBottom; x++) {
                double[] rgb = mat.get(y, x);
                RTotal += rgb[0];
                GTotal += rgb[1];
                BTotal += rgb[2];
            }
        }

        return new double[] {RTotal / 250000, GTotal / 250000, BTotal / 250000};
    }

    private int getColor(double[] color) {
        double closest = 999999999;
        int[] closestColor = new int[] {0, 0, 0};

        for (int i = 1; i <= 6; i++) {
            int[] color2 = IntToColor(i);
            double d = (color[0] - color2[0]) * (color[0] - color2[0]) + (color[1] - color2[1]) * (color[1] - color2[1]) + (color[2] - color2[2]) * (color[2] - color2[2]);
            if (d < closest) {
                closestColor = color2;
                closest = d;
            }
        }

        String hex1 = Integer.toHexString(closestColor[0]);
        String hex2 = Integer.toHexString(closestColor[1]);
        String hex3 = Integer.toHexString(closestColor[2]);

        if (hex1.length() != 2) {
            hex1 = "0" + hex1;
        }
        if (hex2.length() != 2) {
            hex2 = "0" + hex2;
        }
        if (hex3.length() != 2) {
            hex3 = "0" + hex3;
        }

        String colorHex = "ff" + hex1 + hex2 + hex3;
        return (int) Long.parseLong(colorHex, 16);
    }

    private int[] IntToColor(int integer) {
        switch (integer) {
            case 1:
                return new int[] {255, 255, 255};
            case 2:
                return new int[] {255, 88, 0};
            case 3:
                return new int[] {0, 155, 72};
            case 4:
                return new int[] {183, 18, 52};
            case 5:
                return new int[] {255, 213, 0};
            case 6:
                return new int[] {0, 70, 173};
        }

        return new int[] {255, 255, 255};
    }
}