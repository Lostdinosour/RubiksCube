package me.rubik.rubikscube.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import me.rubik.rubikscube.R;
import me.rubik.rubikscube.databinding.ActivityInsertCameraCubeBinding;

public class InsertCameraCubeActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private ActivityInsertCameraCubeBinding binding;

    private JavaCameraView camera;

    private Mat mat1, mat2;
    private Scalar scalarLow, scalarHigh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_camera_cube);

        camera = (JavaCameraView) findViewById(R.id.JavaCameraView_camera);
        camera.setCameraIndex(0);
        scalarLow = new Scalar(45, 20, 10);
        scalarHigh = new Scalar(75, 255, 255);
        camera.setCvCameraViewListener(this);
        camera.enableView();
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
        Imgproc.cvtColor(frame.rgba(), mat1, Imgproc.COLOR_BGR2HSV);
        Core.inRange(mat1, scalarLow, scalarHigh, mat2);

        return mat2;
    }

}