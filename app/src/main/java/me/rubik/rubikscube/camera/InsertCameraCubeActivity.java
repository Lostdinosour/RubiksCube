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

import java.util.Arrays;

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
        Mat mat = frame.rgba();
        int size = 500;

        int per = 500 / 3;

        int YTop = (int) (getY(screenHeight, mat.height()) - size / 2f);
        int YBottom = (int) (getY(screenHeight, mat.height()) + size / 2f);

        int XTop = (int) (getX(screenWidth, mat.width()) - size / 2f);
        int XBottom = (int) (getX(screenWidth, mat.width()) + size / 2f);

        System.out.println(YTop);
        System.out.println(YBottom);
        System.out.println(XTop);
        System.out.println(XBottom);

        for (int i = 0; i < 3; i++) {
            int YTop2 = YTop + i * per;
            int YBottom2 = YBottom + (3 - i + 1) * per;

            for (int j = 0; j < 3; j++) {
                int XTop2 = XTop + j * per;
                int XBottom2 = XBottom + (3 - j + 1) * per;

                System.out.println(YTop2);
                System.out.println(YBottom2);
                System.out.println(XTop2);
                System.out.println(XBottom2);


                //double[] color = getAverage(mat, YTop2, YBottom2, XTop2, XBottom2);
                //System.out.println(Arrays.toString(color));
            }

        }
        return frame.rgba();
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
                if (rgb == null) {
                    System.out.println("wtf");
                    continue;
                }
                RTotal += rgb[0];
                GTotal += rgb[1];
                BTotal += rgb[2];
            }
        }

        return new double[] {RTotal / 250000, GTotal / 250000, BTotal / 250000};
    }
}