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
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 *
 * Originally Created by lisah0 on 2012-02-24
 * https://github.com/ZBar/ZBar/blob/master/android/examples/CameraTest/src/net/sourceforge/zbar/android/CameraTest/CameraTestActivity.java
 *
 * Modified by Jan Grünewald to suite Super-Duo Alexandria on 2015-09-26
 */
package de.gruenewald.android.alexandria;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import it.jaschke.alexandria.R;

public class BarcodeScannerActivity extends AppCompatActivity {
    private static final String LOG_TAG = BarcodeScannerActivity.class.getSimpleName();

    public static final String ACTIVITY_RESULT_KEY_BARCODE = "RESULT_BARCODE";

    public static final int ACTIVITY_REQUEST_CODE_BARCODE = 1;

    public static final int ACTIVITY_RESULT_CODE_SUCCESS = 0;
    public static final int ACTIVITY_RESULT_CODE_ERROR_CAMERA = -1;

    @SuppressWarnings("deprecation") private Camera mCamera;
    private Handler autoFocusHandler;

    ImageScanner scanner;

    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_barcode_scanner);

        // forcing orientation to portrait really required?!
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();
        // if camera-instance could not be fetched there's no reason to keep this activity
        // up; finish it with an error-result
        if (mCamera == null) {
            setResult(ACTIVITY_RESULT_CODE_ERROR_CAMERA);
            finishActivity(ACTIVITY_REQUEST_CODE_BARCODE);
            finish();
        }

        /* Instance barcode scanner */
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        CameraPreview myCameraPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        if (findViewById(R.id.cameraPreview) instanceof FrameLayout) {
            FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
            preview.addView(myCameraPreview);
        }
    }

    @Override protected void onPause() {
        Log.d(LOG_TAG, "Pausing BarcodeScannerActivity.");
        super.onPause();
        releaseCamera();
    }

    @Override protected void onResume() {
        Log.d(LOG_TAG, "Resuming BarcodeScannerActivity");
        super.onResume();
        setupCamera();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    @SuppressWarnings("deprecation") public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Unable to instantiate camera: " + e.getMessage());
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private void setupCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(previewCb);
            mCamera.startPreview();
            previewing = true;
            mCamera.autoFocus(autoFocusCB);
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    @SuppressWarnings("deprecation") Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();

                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    Log.d(LOG_TAG, "Barcode-Result  2: " + sym.getData());
                    Intent myResultIntent = new Intent();
                    myResultIntent.putExtra(ACTIVITY_RESULT_KEY_BARCODE, sym.getData());
                    setResult(ACTIVITY_RESULT_CODE_SUCCESS, myResultIntent);
                    finishActivity(ACTIVITY_REQUEST_CODE_BARCODE);
                    finish();
                }
            }
        }
    };

    // Mimic continuous auto-focusing
    @SuppressWarnings("deprecation") Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };
}
