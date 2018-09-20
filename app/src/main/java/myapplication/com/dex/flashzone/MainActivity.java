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
import android.media.MediaPlayer;
import android.hardware.Camera;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Camera camera;
    Camera.Parameters para;
    private Boolean IsOFF=true;
    private Boolean HasFlash;
    private MediaPlayer mediaPlayer;
    private ImageView Leaver;
    private Button DisplayColorBtn,BlinkLight;
    String Mystr = "0101010101010101010101010101010101010101";
    long blinkDelay = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Leaver = (ImageView) findViewById(R.id.leaver);
        DisplayColorBtn = (Button) findViewById(R.id.discolor);
        BlinkLight = (Button) findViewById(R.id.Blinking);
        checkCameraExcite();
        GetCamera();
        //cheackCameraAlloworNot
        if (cameraAllowed() != true) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void BlickBtn(View view){
                BlinkLight();
    }
    private void BlinkLight(){
        //Delay in ms
        for (int i = 0; i < Mystr.length(); i++) {
            if (Mystr.charAt(i) == '0') {
               FlashOn();
            } else {
                FlashOff();
            }
            try {
                Thread.sleep(blinkDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void clickopencolor(View view){
        Intent intentC = new Intent(MainActivity.this,displaylight.class);
        startActivity(intentC);
    }

    public void clickleaver(View view) {
        if (!IsOFF) {
            FlashOff();
        } else {
            FlashOn();
        }
        PlaySound();
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
            ImageExchanger();
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
            ImageExchanger();
            Log.v("Message", "show =" + IsOFF);
        }
    }

    private void ImageExchanger(){
        if(!IsOFF){
            Leaver.setImageResource(R.drawable.imgon);
            Log.v("image :","image = Off" );
        }else {
            Leaver.setImageResource(R.drawable.imgoff);
            Log.v("image :","image = on" );
        }
    }

  private void PlaySound(){
        mediaPlayer = MediaPlayer.create(this,R.raw.click);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer m) {
                m.release();
            }
        });
        mediaPlayer.start();
    }

}






