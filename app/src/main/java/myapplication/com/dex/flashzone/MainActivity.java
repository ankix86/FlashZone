package myapplication.com.dex.flashzone;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.hardware.Camera;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Camera camera;
    Camera.Parameters para;
    private static boolean IsOFF;
    private static boolean HasFlash;
    ImageView Leaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Leaver = (ImageView) findViewById(R.id.leaver);
        checkCameraExcite();
        GetCamera();
        //cheackCameraAlloworNot
        if (cameraAllowed() != true) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void clickleaver(View view) {
        if (!IsOFF) {
            FlashOff();
        } else {
            FlashOn();
        }
    }

    private void checkCameraExcite() {

        HasFlash = getApplication().getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!HasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            android.app.AlertDialog alert = new android.app.AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Warning");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            Log.v("Warning","Sorry, your device doesn't support flash light!");

            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                }
            });
            alert.show();
            return;
        }
    }

    private Boolean cameraAllowed() {

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void GetCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                para = camera.getParameters();
                Log.v("Log","Camera = True");
            } catch (Exception e) {
                Log.e("Error! Failed to open", e.getMessage());
            }
        }
    }

    private void FlashOn() {
        if (IsOFF) {
            if (camera == null || para == null) {
                return;
            }
            para = camera.getParameters();
            para.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(para);
            camera.startPreview();
            IsOFF = false;
            Leaver.setImageResource(R.drawable.imgon);
            Log.v("Message", "show =" + IsOFF);
        }
    }

    private void FlashOff() {
        if (!IsOFF) {
            if (camera == null || para == null) {
                return;
            }
            para = camera.getParameters();
            para.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(para);
            camera.stopPreview();
            IsOFF = true;
            Leaver.setImageResource(R.drawable.imgoff);
            Log.v("Message", "show =" + IsOFF);
        }
    }
}






