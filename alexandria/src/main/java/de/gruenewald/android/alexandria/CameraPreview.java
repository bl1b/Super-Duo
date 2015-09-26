/******************************************************************************
 * Copyright (c) 2015 by Jan Grünewald.                                       *
 * ****************************************************************************
 * This file is part of 'Super-Duo. 'Super-Duo' was developed as  part of the *
 * Android Developer Nanodegree by Udacity. For further information see:      *
 * https://www.udacity.com/course/android%2Ddeveloper%2Dnanodegree%2D%2Dnd801 *
 * *
 * 'Super-Duo' is free software: you can redistribute it and/or modify it     *
 * under the terms of the GNU General Public License as published by the      *
 * Free Software Foundation, either version 3 of the License, or (at your     *
 * option) any later version.                                                 *
 * *
 * 'Super-Duo' is distributed in the hope that it will be useful, but         *
 * WITHOUT ANY WARRANTY; without even the implied warranty of                 *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 * GNU General Public License for more details.                               *
 * *
 * You should have received a copy of the GNU General Public License          *
 * along with 'Super-Duo'.  If not, see <http://www.gnu.org/licenses/>.       *
 ******************************************************************************/

/*
 * Barebones implementation of displaying camera preview.
 * 
 * Originally Created by lisah0 on 2012-02-24
 * https://github.com/ZBar/ZBar/blob/master/android/examples/CameraTest/src/net/sourceforge/zbar/android/CameraTest/CameraPreview.java
 * Modified to suite the Super-Duo Alexandria Project by
 * Jan Grünewald on 2015-09-25.
 */
package de.gruenewald.android.alexandria;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

/**
 * A basic Camera preview class
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, View.OnClickListener {
    private static final String LOG_TAG = CameraPreview.class.getSimpleName();

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private PreviewCallback previewCallback;
    private AutoFocusCallback autoFocusCallback;

    public CameraPreview(Context context, Camera camera, PreviewCallback previewCb, AutoFocusCallback autoFocusCb) {
        super(context);
        mCamera = camera;
        previewCallback = previewCb;
        autoFocusCallback = autoFocusCb;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
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
            mCamera.setPreviewCallback(previewCallback);
            mCamera.startPreview();
            mCamera.autoFocus(autoFocusCallback);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    /**
     * Method has been added to be able to trigger an auto-focus when tapping
     * on the surfaceview.
     *
     * @param v The view-object the OnClick-Event is triggered on.
     */
    @Override public void onClick(View v) {
        if (mCamera != null && autoFocusCallback != null) {
            mCamera.autoFocus(autoFocusCallback);
        }
    }
}
