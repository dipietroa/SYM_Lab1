package ch.heigvd.sym.template;

import android.Manifest;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.greysonparrelli.permiso.Permiso;
import java.io.File;

public class InfoActivity extends AppCompatActivity {

    private TextView email = null;
    private TextView imei = null;
    private ImageView persoImg = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Permiso.getInstance().setActivity(this);
        setContentView(R.layout.activity_info);

        this.email = (TextView) findViewById(R.id.email);
        this.imei = (TextView) findViewById(R.id.imei);
        this.persoImg = (ImageView) findViewById(R.id.persoImg);

        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.isPermissionGranted(Manifest.permission.READ_PHONE_STATE)) {
                    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    String userImei = telephonyManager.getImei();
                    imei.setText(userImei);
                }
                else {
                    imei.setText("Permission denied");
                }
                if (resultSet.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    File imgPath =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File imgFile = new File(imgPath, "perso.jpg");
                    if(imgFile.exists()) {
                        persoImg.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
                    }
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog("Permission denied", "Without the following permissions we can't show the IMEI and/or the user picture", null, callback);
            }
        }, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE);

        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        email.setText(userEmail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

}
