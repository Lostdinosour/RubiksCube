package me.rubik.rubikscube.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.List;

import me.rubik.rubikscube.MainActivity;

public class PortraitCameraView extends CameraBridgeViewBase implements Camera.PreviewCallback {

    private static final int MAGIC_TEXTURE_ID = 10;
    private static final String TAG = "JavaCameraView";

    private byte mBuffer[];
    private Mat[] mFrameChain;
    private int mChainIdx = 0;
    private Thread mThread;
    private boolean mStopThread;

    public Camera mCamera;
    protected JavaCameraFrame[] mCameraFrame;
    private SurfaceTexture mSurfaceTexture;
    private int mCameraId;
    Handler handler;
    Camera.Size bestSize = null;
    Camera.Size pictureSize = null;

    public static class JavaCameraSizeAccessor implements ListItemAccessor {

        public int getWidth(Object obj) {
            Camera.Size size = (Camera.Size) obj;
            return size.width;
        }

        public int getHeight(Object obj) {
            Camera.Size size = (Camera.Size) obj;
            return size.height;
        }
    }


    public PortraitCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected boolean initializeCamera(int width, int height) {
        handler = new Handler();
        Log.d(TAG, "Initialize java camera");
        boolean result = true;
        synchronized (this) {
            mCamera = null;

            boolean connected = false;
            int numberOfCameras = android.hardware.Camera.getNumberOfCameras();
            android.hardware.Camera.CameraInfo cameraInfo = new android.hardware.Camera.CameraInfo();
            for (int i = 0; i < numberOfCameras; i++) {
                android.hardware.Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK) {
                    try {
                        mCamera = Camera.open(i);
                        mCameraId = i;
                        connected = true;
                    } catch (RuntimeException e) {
                        Log.e(TAG, "Camera #" + i + "failed to open: " + e.getMessage());
                    }
                    if (connected) break;
                }
            }

            if (mCamera == null) return false;

            try {
                Camera.Parameters params = mCamera.getParameters();
                List<Camera.Size> sizes = params.getSupportedPreviewSizes();
                List<Camera.Size> Picturesizes = params.getSupportedPictureSizes();
                pictureSize = Picturesizes.get(0);

                List<Camera.Size> sizeList = sizes;
                bestSize = sizeList.get(0);
                Log.d(TAG, "getSupportedPreviewSizes()  " + bestSize.width + "  " + bestSize.height);
                Log.d(TAG, "Picturesizes()  " + pictureSize.width + "  " + pictureSize.height);

                for (int i = 1; i < sizeList.size(); i++) {
                    if ((sizeList.get(i).width * sizeList.get(i).height) > (bestSize.width * bestSize.height)) {
                        Log.d(TAG, "getSupportedPreviewSizes()   " + sizeList.get(i).width + "  " + sizeList.get(i).height);
                        bestSize = sizeList.get(i);
                    }
                }

                params.setPreviewFormat(ImageFormat.NV21);
                Log.e(TAG, "Set preview size to " + bestSize.width + " x " + bestSize.height);
                Log.e(TAG, "Set preview size to " + width + " x " + height);
                params.setPreviewSize(bestSize.width, bestSize.height);
                params.setPictureSize(pictureSize.width, pictureSize.height);

                params.setRecordingHint(true);

                List<String> FocusModes = params.getSupportedFocusModes();
                if (FocusModes != null && FocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                    params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }

                List<int[]> ints = params.getSupportedPreviewFpsRange();
                for (int i = 0; i < ints.size(); i++) {
                    Log.e("privew size", String.valueOf(ints.get(i).length));
                }

                mCamera.setParameters(params);

                params = mCamera.getParameters();
                mFrameWidth = params.getPreviewSize().height;
                mFrameHeight = params.getPreviewSize().width;

                int realWidth = mFrameHeight;
                int realHeight = mFrameWidth;
                if ((getLayoutParams().width == LinearLayout.LayoutParams.MATCH_PARENT) && (getLayoutParams().height == LinearLayout.LayoutParams.MATCH_PARENT))
                    mScale = Math.min(((float) height) / mFrameHeight, ((float) width) / mFrameWidth);
                else
                    mScale = 0;

                if (mFpsMeter != null) {
                    mFpsMeter.setResolution(pictureSize.width, pictureSize.height);
                }

                int size = mFrameWidth * mFrameHeight;
                size = size * ImageFormat.getBitsPerPixel(params.getPreviewFormat()) / 8;
                mBuffer = new byte[size];

                mCamera.addCallbackBuffer(mBuffer);
                mCamera.setPreviewCallbackWithBuffer(this);

                mFrameChain = new Mat[2];
                mFrameChain[0] = new Mat(realHeight + (realHeight / 2), realWidth, CvType.CV_8UC1);
                mFrameChain[1] = new Mat(realHeight + (realHeight / 2), realWidth, CvType.CV_8UC1);

                AllocateCache();

                mCameraFrame = new JavaCameraFrame[2];
                mCameraFrame[0] = new JavaCameraFrame(mFrameChain[0], mFrameWidth, mFrameHeight);
                mCameraFrame[1] = new JavaCameraFrame(mFrameChain[1], mFrameWidth, mFrameHeight);

                mSurfaceTexture = new SurfaceTexture(MAGIC_TEXTURE_ID);
                mCamera.setPreviewTexture(mSurfaceTexture);

                Log.d(TAG, "startPreview");
                mCamera.startPreview();
            } catch (Exception e) {
                result = false;
                e.printStackTrace();
            }
        }

        return result;
    }

    protected void releaseCamera() {
        synchronized (this) {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
            }
            mCamera = null;
            if (mFrameChain != null) {
                mFrameChain[0].release();
                mFrameChain[1].release();
            }
            if (mCameraFrame != null) {
                mCameraFrame[0].release();
                mCameraFrame[1].release();
            }
        }
    }

    @Override
    protected boolean connectCamera(int width, int height) {

        Log.d(TAG, "Connecting to camera");
        if (!initializeCamera(width, height))
            return false;

        Log.d(TAG, "Starting processing thread");
        mStopThread = false;
        mThread = new Thread(new CameraWorker());
        mThread.start();

        return true;
    }

    protected void disconnectCamera() {
        Log.d(TAG, "Disconnecting from camera");
        try {
            mStopThread = true;
            Log.d(TAG, "Notify thread");
            synchronized (this) {
                this.notify();
            }
            Log.d(TAG, "Wating for thread");
            if (mThread != null)
                mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mThread = null;
        }

        releaseCamera();
    }

    public void onPreviewFrame(byte[] frame, Camera arg1) {
        synchronized (this) {
            mFrameChain[1 - mChainIdx].put(0, 0, frame);
            this.notify();
        }
        if (mCamera != null)
            mCamera.addCallbackBuffer(mBuffer);
    }

    private static class JavaCameraFrame implements CvCameraViewFrame {
        private final Mat mYuvFrameData;
        private final Mat mRgba;
        private final int mWidth;
        private final int mHeight;
        private Mat mRotated;

        public Mat gray() {
            if (mRotated != null) mRotated.release();
            mRotated = mYuvFrameData.submat(0, mWidth, 0, mHeight);
            mRotated = mRotated.t();
            Core.flip(mRotated, mRotated, 1);
            return mRotated;
        }

        public Mat rgba() {
            Imgproc.cvtColor(mYuvFrameData, mRgba, Imgproc.COLOR_YUV2BGR_NV12, 4);
            if (mRotated != null) mRotated.release();
            mRotated = mRgba.t();
            Core.flip(mRotated, mRotated, 1);
            return mRotated;
        }

        public JavaCameraFrame(Mat Yuv420sp, int width, int height) {
            super();
            mWidth = width;
            mHeight = height;
            mYuvFrameData = Yuv420sp;
            mRgba = new Mat();
        }

        public void release() {
            mRgba.release();
            if (mRotated != null) mRotated.release();
        }
    }

    private class CameraWorker implements Runnable {
        public void run() {
            do {
                synchronized (PortraitCameraView.this) {
                    try {
                        PortraitCameraView.this.wait();
                    } catch (InterruptedException e) {
                        Log.e(TAG, "CameraWorker interrupted", e);
                    }
                }

                if (!mStopThread) {
                    if (!mFrameChain[mChainIdx].empty())
                        deliverAndDrawFrame(mCameraFrame[mChainIdx]);
                    mChainIdx = 1 - mChainIdx;
                }
            } while (!mStopThread);
            Log.d(TAG, "Finish processing thread");
        }
    }
}
