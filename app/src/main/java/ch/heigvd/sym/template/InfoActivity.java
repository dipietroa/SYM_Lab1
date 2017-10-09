package ch.heigvd.sym.template;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class InfoActivity extends AppCompatActivity {

    private TextView email = null;
    private TextView imei = null;
    private ImageView persoImg = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        this.email = (TextView) findViewById(R.id.email);
        this.imei = (TextView) findViewById(R.id.imei);
        this.persoImg = (ImageView) findViewById(R.id.persoImg);

        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        email.setText(userEmail);

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String userImei = telephonyManager.getImei();
        imei.setText(userImei);

        File imgPath =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File imgFile = new File(imgPath, "perso.jpg");
        if(imgFile.exists()) {
            persoImg.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        }

    }

}
