package myapplication.com.dex.flashzone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import yuku.ambilwarna.AmbilWarnaDialog;

public class displaylight extends AppCompatActivity {

    private int DefaultColor;
    private Button ColorPicker;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displaylight);

        DefaultColor = ContextCompat.getColor(this,R.color.colorPrimary);
        relativeLayout = (RelativeLayout) findViewById(R.id.Display);
        ColorPicker  = (Button) findViewById(R.id.colorpicker);


        ColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });
    }

    private void openColorPicker(){
        AmbilWarnaDialog colorpicker = new AmbilWarnaDialog(this, DefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
            DefaultColor = color;
            relativeLayout.setBackgroundColor(DefaultColor);
            }
        });
              colorpicker.show();
    }
}
