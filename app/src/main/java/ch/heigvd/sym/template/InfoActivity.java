package ch.heigvd.sym.template;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    private TextView email = null;
    private TextView imei = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        this.email = (TextView) findViewById(R.id.email);
        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        email.setText(userEmail);

        this.imei = (TextView) findViewById(R.id.imei);
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String userImei = telephonyManager.getImei();
        imei.setText(userImei);

    }

}
