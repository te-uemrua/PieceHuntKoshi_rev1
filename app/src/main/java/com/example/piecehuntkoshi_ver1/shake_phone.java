package com.example.piecehuntkoshi_ver1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class shake_phone extends AppCompatActivity implements SensorEventListener {

    // (Existing variables)
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float lastX, lastY, lastZ;
    private long lastUpdate = 0;
    private static final int SHAKE_THRESHOLD = 800;
    private AppDatabase db;
    private ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();
    private boolean pieceAcquired = false;
    private TextView instructionText;
    private String landmarkId;
    private String landmarkName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_phone);

        instructionText = findViewById(R.id.instruction_text);

        landmarkId = getIntent().getStringExtra("LANDMARK_ID");
        landmarkName = getIntent().getStringExtra("LANDMARK_NAME");
        if (landmarkName != null) {
            instructionText.setText(landmarkName + "\n到着!! " + "\nスマホを振ろう!!");
        }

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
        if (pieceAcquired) return;
        pieceAcquired = true;

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

        databaseExecutor.execute(() -> {
            Puzzle currentPuzzle = db.puzzleDao().getFirstUncompletedPuzzle();

            if (currentPuzzle == null) {
                runOnUiThread(() -> Toast.makeText(this, "全てのパズルが完成しています！", Toast.LENGTH_LONG).show());
                finish();
                return;
            }

            PuzzleData pieceToUnlock = db.puzzleDao().getARandomUnlockedPiece(currentPuzzle.getId());

            if (pieceToUnlock == null) {
                 // This case should not happen anymore with the new DB logic, but as a safeguard.
                runOnUiThread(() -> Toast.makeText(this, "エラー：アンロックするピースが見つかりません。", Toast.LENGTH_LONG).show());
                finish();
                return;
            }

            db.puzzleDao().unlockPieceById(pieceToUnlock.getId());

            if (landmarkId != null && !landmarkId.isEmpty()) {
                SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
                prefs.edit().putLong(landmarkId + MainActivity.LAST_ACQUIRED_PREFIX, System.currentTimeMillis()).apply();
            }
            
            boolean justCompleted = checkPuzzleCompletion(currentPuzzle.getId());

            runOnUiThread(() -> {
                instructionText.setText("ピースをゲット！");
                Toast.makeText(this, currentPuzzle.getName() + " のピース No." + (pieceToUnlock.getPieceIndex() + 1) + " を手に入れた！", Toast.LENGTH_LONG).show();
                
                // Pass all necessary info to PieceGetActivity
                Intent intent = new Intent(shake_phone.this, PieceGetActivity.class);
                intent.putExtra("pieceNumber", pieceToUnlock.getPieceIndex());
                intent.putExtra("isPuzzleCompleted", justCompleted);
                intent.putExtra("completedPuzzleId", currentPuzzle.getId()); // Pass the ID
                intent.putExtra("completedPuzzleImage", currentPuzzle.getCompletedThumbnailResId());

                startActivity(intent);
                finish(); // Finish this activity immediately
            });
        });
    }

    private boolean checkPuzzleCompletion(int puzzleId) {
        List<PuzzleData> pieces = db.puzzleDao().getPiecesForPuzzle(puzzleId);
        boolean allUnlocked = true;
        for(PuzzleData piece : pieces) {
            if(!piece.isUnlocked()) {
                allUnlocked = false;
                break;
            }
        }
        if(allUnlocked) {
            db.puzzleDao().updatePuzzleAsCompleted(puzzleId);
            return true;
        }
        return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

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
