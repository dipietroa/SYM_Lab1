/**
 * Author: Di Pietro Adrian, Cotza Andrea, Moreira Kevin
 */
package ch.heigvd.sym.lab1;

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

        /**
         * Gestion des permissions d'accès aux données ou non par l'utilisateur
         */
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                /**
                 * Vérfie que l'utilisateur ait accepté que l'application consulte
                 * "l'état" de l'appareil
                 */
                if (resultSet.isPermissionGranted(Manifest.permission.READ_PHONE_STATE)) {
                    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

                    String userImei = telephonyManager.getDeviceId();
                    // From android api level 26 (Android 8.0 Oreo)
                    // String userImei = telephonyManager.getImei();

                    imei.setText(userImei);
                }
                else {
                    imei.setText(getResources().getString(R.string.perm_denied));
                }
                /**
                 * Vérifie que l'utilisateur ait donné les autorisations pour l'accès aux fichiers
                 */
                if (resultSet.isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    File imgPath =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    //Fonctionne s'il existe perso.jpg dans le dossier downloads de l'appareil
                    File imgFile = new File(imgPath, "perso.jpg");
                    if(imgFile.exists()) {
                        persoImg.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
                    }
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog(getResources().getString(R.string.perm_denied), getResources().getString(R.string.perm_denied_msg), null, callback);
            }
        }, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE);

        //Affichage de l'email précédemment entré
        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        email.setText(userEmail);
    }

    /**
     * Override de la méthode onResume() nécessaire lorsqu'on utilise la librairie permiso
     * pour la gestion des permissions au runtime (change le focus activité)
     */
    @Override
    protected void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(this);
    }

    /**
     * Même chose, nécessaire pour l'utilisation de la librairie permiso
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    /**
     * Test suite à la question n° 3, empêcher le retour à la précédente activité qui n'a pas
     * de sens dans notre contexte
     */
    @Override
    public void onBackPressed() {
        //exemple surcharge bouton back, dans ce cas-ci on ne fait rien
        //impossible de retrouver l'écran de connexion
    }

}
