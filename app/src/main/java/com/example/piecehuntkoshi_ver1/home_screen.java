package com.example.piecehuntkoshi_ver1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat; // Import ActivityCompat
import androidx.core.content.ContextCompat;
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

    private Button startButton;

    private final ActivityResultLauncher<String>
            requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    // Permission is granted. Update the button state.
                    updateStartButtonState();
                } else {
                    toastMake(R.string.message2);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        startButton = findViewById(R.id.start_button);

        // Set up button listeners
        startButton.setOnClickListener( v -> {
            Intent intent = new Intent(home_screen.this, MainActivity.class);
            startActivity(intent);
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            String uriString = "package:" + getPackageName();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(uriString));
            startActivity(intent);
        });

        // Initial permission check
        checkLocationPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update button state every time the screen is resumed, in case the user changes permissions in settings
        updateStartButtonState();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Already granted
            updateStartButtonState();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show rationale dialog
            new AlertDialog.Builder(this)
                    .setMessage(R.string.alert_dialog)
                    .setPositiveButton(R.string.ok, (dialog, id) -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION))
                    .setNegativeButton(R.string.no_thanks, (dialog, id) -> toastMake(R.string.message1))
                    .show();
        } else {
            // Directly request permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void updateStartButtonState() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // If permission is granted, enable the button
            startButton.setEnabled(true);
            startButton.setAlpha(1.0f);
        } else {
            // If permission is denied, disable the button
            startButton.setEnabled(false);
            startButton.setAlpha(0.5f);
        }
    }

    private void toastMake(int str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
