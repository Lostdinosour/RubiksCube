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

import me.rubik.rubikscube.MainActivity;
import me.rubik.rubikscube.R;
import me.rubik.rubikscube.databinding.ActivityInsertCameraCubeBinding;
import me.rubik.rubikscube.ui.solver.InsertCubeActivity;

public class InsertCameraCubeActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private PortraitCameraView camera;

    private ActivityInsertCameraCubeBinding binding;

    private Mat mat1, mat2;
    private Scalar scalarLow, scalarHigh;

    private ImageView imageView;
    private Bitmap bitmap;
    private Canvas canvas;

    private int screenWidth, screenHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInsertCameraCubeBinding.inflate(getLayoutInflater());


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
        scalarLow = new Scalar(45, 20, 10);
        scalarHigh = new Scalar(75, 255, 255);
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

        mat1 = new Mat(width, height, CvType.CV_16UC4);
        mat2 = new Mat(width, height, CvType.CV_16UC4);
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame frame) {
//        Imgproc.cvtColor(frame.rgba(), mat1, Imgproc.COLOR_BGR2HSV);
//        Core.inRange(mat1, scalarLow, scalarHigh, mat2);

        return frame.rgba();
    }





}