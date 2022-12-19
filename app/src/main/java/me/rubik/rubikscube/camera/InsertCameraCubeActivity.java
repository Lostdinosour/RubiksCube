package me.rubik.rubikscube.camera;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import me.rubik.rubikscube.MainActivity;
import me.rubik.rubikscube.R;
import me.rubik.rubikscube.databinding.ActivityInsertCameraCubeBinding;
import me.rubik.rubikscube.ui.solver.InsertCubeActivity;

public class InsertCameraCubeActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private PortraitCameraView camera;

    private ActivityInsertCameraCubeBinding binding;

    private ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;

    private int screenWidth, screenHeight;

    private int centerColor;


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

        int size = 500;

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

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
        Mat mat = frame.rgba();
        int size = 500;

        int per = 500 / 3;

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
                color[0] *= 13;
                color[1] *= 13;
                color[2] *= 13;
                int col = getColor(color);
                if (col == 0) {
                    i = 1000;
                    break;
                }

                squares.add(col);

            }
        }

        if (squares.size() == 9) {
            System.out.println(centerColor);
            if (squares.get(4) == centerColor) {
                Logger.getLogger("global").warning(String.valueOf(squares.get(0)));
                Logger.getLogger("global").warning(String.valueOf(squares.get(1)));
                Logger.getLogger("global").warning(String.valueOf(squares.get(2)));
                Logger.getLogger("global").warning(String.valueOf(squares.get(3)));
                Logger.getLogger("global").warning(String.valueOf(squares.get(4)));
                Logger.getLogger("global").warning(String.valueOf(squares.get(5)));
                Logger.getLogger("global").warning(String.valueOf(squares.get(6)));
                Logger.getLogger("global").warning(String.valueOf(squares.get(7)));
                Logger.getLogger("global").warning(String.valueOf(squares.get(8)));
            }
        }

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

    final int offset = 50;

    private int getColor(double[] color) {



        if (color[0] - 255 > -offset) { // white, orange, yellow
            if (color[1] - 255 > -offset) { // white
                if (color[2] - 255 > -offset) {
                    return getColor(R.color.white);
                }
            } else if (color[1] - 213 > -offset && color[1] - 213 < offset) { // yellow
                if (color[2] < offset) {
                    return getColor(R.color.yellow);
                }
            } else if (color[1] - 88 > -offset && color[1] - 88 < offset) { // orange
                if (color[2] < offset) {
                    return getColor(R.color.orange);
                }
            }
        }

        else if (color[0] < offset) { // green, blue
            if (color[1] - 155 > -offset && color[1] - 155 < offset) { // green
                if (color[2] - 72 > -offset && color[2] - 72 < offset) {
                    return getColor(R.color.green);
                }
            } else if (color[1] - 70 > -offset && color[1] - 70 < offset) { // blue
                if (color[2] - 173 > -offset && color[2] - 173 < offset) {
                    return getColor(R.color.blue);
                }
            }
        }

        else if (color[0] - 183 > -offset && color[0] - 183 < offset) { // red
            if (color[1] - 18 > -offset && color[1] - 18 < offset) {
                if (color[2] - 52 > -offset && color[2] - 52 < offset) {
                    return getColor(R.color.red);
                }
            }
        }

        return 0;
    }
}