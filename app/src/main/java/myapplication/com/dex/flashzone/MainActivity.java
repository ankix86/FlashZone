package myapplication.com.dex.flashzone;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.media.MediaPlayer;
import android.hardware.Camera;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements savedata {

    Camera camera;
    Camera.Parameters para;
    private Boolean IsOFF = true;
    private Boolean HasFlash;
    private MediaPlayer mediaPlayer;
    private ImageView Leaver;
    private Button DisplayColorBtn, BlinkLight;
    int blinkDelay = 50;
    private LinearLayout background;
    private SeekBar blinkspeed;
    private static final String PREFS = "storeddata";
    public volatile boolean isBlinking;
    public Handler handler = new Handler();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isBlinking = false;
        Leaver = (ImageView) findViewById(R.id.leaver);
        DisplayColorBtn = (Button) findViewById(R.id.discolor);
        background = (LinearLayout) findViewById(R.id.background);
        BlinkLight = (Button) findViewById(R.id.Blinking);
        blinkspeed = (SeekBar) findViewById(R.id.seekbar);
        blinkspeed.setMax(250);
        checkCameraExcite();
        GetCamera();
        SetBlinkSpeed();
        LoadSettings();



        //cheackCameraAlloworNot
        if (cameraAllowed() != true) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void SetBlinkSpeed() {

        blinkspeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int MIN = 90;
                if (i < MIN) {
                    Log.v("msg", "No More Decrement");
                } else {
                    blinkDelay = i;
                    SaveSettings();
                    Log.v("msg", "speed" + blinkDelay);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }



    public void clickopencolor(View view) {
        PlaySound();
        Intent intentC = new Intent(MainActivity.this, displaylight.class);
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
            Log.v("Warning", "Sorry, your device doesn't support flash light!");

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
                Log.v("Log", "Camera = True");
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

    private void ImageExchanger() {
        if (!IsOFF) {
            Leaver.setImageResource(R.drawable.imgon);
            background.setBackgroundResource(R.drawable.bgon);

            Log.v("image :", "image = Off");
        } else {
            Leaver.setImageResource(R.drawable.imgoff);
            background.setBackgroundResource(R.drawable.bgoff);
            Log.v("image :", "image = on");
        }
    }

    private void PlaySound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.click);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer m) {
                m.release();
            }
        });
        mediaPlayer.start();
    }

    @Override
    public void SaveSettings() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
        editor.putInt("blinkDelay", blinkDelay);
        Log.v("show", "saved = " + blinkDelay);
        editor.apply();
    }

    @Override
    public void LoadSettings() {
        SharedPreferences RestorData = getSharedPreferences(PREFS, MODE_PRIVATE);
        blinkDelay = RestorData.getInt("blinkDelay", 0);
        blinkspeed.setProgress(blinkDelay);
        Log.e("show", "saved = " + blinkDelay);
    }
    public void BlickBtn(View view) {
        BlinkThread Bt = new BlinkThread();
        if(isBlinking){
            isBlinking = false;
        }else {
            isBlinking = true;
        }
        BlinkThread bt = new BlinkThread();
        new Thread(bt).start();
    }
    public class BlinkThread implements Runnable{
        public void run(){
            PlaySound();
            for (;;) {
                if (isBlinking)
                    return;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        FlashOn();
                        FlashOff();
                    }
                });
                try {
                    Thread.sleep(blinkDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}






