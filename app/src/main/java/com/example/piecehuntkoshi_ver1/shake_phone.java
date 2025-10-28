package com.example.piecehuntkoshi_ver1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List; // Import List
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class shake_phone extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float lastX, lastY, lastZ;
    private long lastUpdate = 0;
    private static final int SHAKE_THRESHOLD = 800;

    private AppDatabase db;
    private ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    private boolean pieceAcquired = false;

    private TextView instructionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_phone); 

        instructionText = findViewById(R.id.instruction_text);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        db = AppDatabase.getDatabase(getApplicationContext());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!pieceAcquired) {
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    acquirePiece();
                }

                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    private void acquirePiece() {
        pieceAcquired = true;

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

        databaseExecutor.execute(() -> {
            int pieceIdToUnlock = 3; 

            db.puzzleDao().unlockPieceById(pieceIdToUnlock);

            // Check if the puzzle is completed
            checkPuzzleCompletion(pieceIdToUnlock);

            runOnUiThread(() -> {
                instructionText.setText("ピースをゲット！");
                Toast.makeText(shake_phone.this, "パズルのピースNo.4 を手に入れた！", Toast.LENGTH_LONG).show();

                new android.os.Handler().postDelayed(
                        this::finish,
                        2000);
            });
        });
    }

    // New method to check for puzzle completion
    private void checkPuzzleCompletion(int unlockedPieceId) {
        // This still runs on the background thread
        int puzzleId = db.puzzleDao().getPuzzleIdForPiece(unlockedPieceId);
        if (puzzleId > 0) { // If a valid puzzle was found
            List<PuzzleData> allPiecesForPuzzle = db.puzzleDao().getPiecesForPuzzle(puzzleId);
            
            boolean allUnlocked = true;
            for (PuzzleData piece : allPiecesForPuzzle) {
                if (!piece.isUnlocked()) {
                    allUnlocked = false;
                    break;
                }
            }

            if (allUnlocked) {
                db.puzzleDao().updatePuzzleAsCompleted(puzzleId);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do nothing
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
