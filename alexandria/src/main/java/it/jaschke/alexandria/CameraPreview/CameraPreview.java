package it.jaschke.alexandria.CameraPreview;

/*
 * Barebones implementation of displaying camera preview.
 *
 * Created by lisah0 on 2012-02-24
 */

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, View.OnClickListener {
    private static final String LOG_TAG = CameraPreview.class.getSimpleName();

    /**
     * The id of the camera. 0 should be the rear camera.
     * 1 seems to be the face camera.
     */
    private static final int CAMERA_ID = 0x0;

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Camera.PreviewCallback mPreviewCallback;
    private Camera.AutoFocusCallback mAutoFocusCallback;


    public CameraPreview(Context context, AttributeSet attrs) {
        // Edit JG: use this constructor instead of the default so that the view-class can be used
        // in layout.xml
        super(context, attrs);

        try {
            mCamera = Camera.open(CAMERA_ID);
            if (mCamera != null) {
                if (context instanceof Camera.PreviewCallback) {
                    mPreviewCallback = (Camera.PreviewCallback) context;
                }

                if (context instanceof Camera.AutoFocusCallback) {
                    mAutoFocusCallback = (Camera.AutoFocusCallback) context;
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Unable to setup camera: " + e.getMessage());
        }


        mHolder = getHolder();
        if (mHolder != null) {
            mHolder.addCallback(this);
            // Edit JG: make sure this method is called on versions prior to 3.0; since we do the
            // version check here we can suppress the deprecation check
            // deprecated setting, but required on Android versions prior to 3.0
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                //noinspection deprecation
                mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
        }

        setOnClickListener(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(LOG_TAG, "Surface created.");
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            if (mCamera != null) {
                mCamera.setPreviewDisplay(holder);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(LOG_TAG, "Surface destroyed.");
        // Camera preview released in activity
        // also release the camera
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(LOG_TAG, "Surface changed.");
        /*
         * If your preview can change or rotate, take care of those events here.
         * Make sure to stop the preview before resizing or reformatting it.
         */
        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        try {
            // Hard code camera surface rotation 90 degs to match Activity view in portrait
            mCamera.setDisplayOrientation(90);

            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(mPreviewCallback);
            mCamera.startPreview();
            mCamera.autoFocus(mAutoFocusCallback);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override public void onClick(View v) {
        if (mCamera != null && mAutoFocusCallback != null) {
            mCamera.autoFocus(mAutoFocusCallback);
        }
    }
}
