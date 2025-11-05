package com.example.piecehuntkoshi_ver1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

public class home_screen extends AppCompatActivity {
    private boolean requestingLocationUpdates = false;
    private boolean locationAuthority = false;

    private final ActivityResultLauncher<String>
            requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                //ユーザーが権限を許可したか
                if (isGranted) {
                    //制限された機能にアクセスする
                    requestingLocationUpdates = true;
                } else {
                    //制限された機能が無いままで継続
                    toastMake(R.string.message2);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

            //権限が既に付与されているか
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                //制限された機能にアクセスする
                requestingLocationUpdates = true;
            }
            //権限の根拠を示す必要があるか
            else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //権限が必要な理由・メリットを説明
                builder.setMessage(R.string.alert_dialog)
                        .setPositiveButton(R.string.ok, (dialog, id) ->
                                requestPermissionLauncher.launch(
                                        Manifest.permission.ACCESS_FINE_LOCATION))
                        .setNegativeButton(R.string.no_thanks, (dialog, id) ->
                                toastMake(R.string.message1));
                builder.create();
                builder.show();

            } else {
                //システム権限を要求する
                requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION);
            }

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            //アプリのSetting画面を開く
            String uriString = "package:" + getPackageName();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse(uriString));
            startActivity(intent);
        });

        Button start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener( v -> {
            Intent intent = new Intent(home_screen.this,MainActivity.class);
            startActivity(intent);
        });

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            start_button.setEnabled(false);
            start_button.setAlpha(.5f);
        }


    }

    private void toastMake(int str) {
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        toast.show();

    }
}