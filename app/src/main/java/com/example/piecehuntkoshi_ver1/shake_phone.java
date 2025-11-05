package com.example.piecehuntkoshi_ver1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences; // ★★★ SharedPreferences をインポート ★★★
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

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float lastX, lastY, lastZ;
    private long lastUpdate = 0;
    private static final int SHAKE_THRESHOLD = 800;

    private AppDatabase db;
    private ExecutorService databaseExecutor = Executors.newSingleThreadExecutor();

    private boolean pieceAcquired = false;
    private TextView instructionText;

    // ★★★ 取得対象のIDと名前を保持 ★★★
    private String landmarkId;
    private String landmarkName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_phone);

        instructionText = findViewById(R.id.instruction_text);
        instructionText.setTextSize(23);
        // ★★★ MainActivityからIDと名前を受け取る ★★★
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
        // ★★★ IDがなければ何もせず終了 ★★★
        if (landmarkId == null || landmarkId.isEmpty()) {
            Toast.makeText(this, "エラー: 取得対象のランドマークIDがありません", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pieceAcquired = true;

        // バイブレーション
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

        databaseExecutor.execute(() -> {
            try {
                // ランダムなピースID（0〜8）
                int randomPiece = new java.util.Random().nextInt(9);

                // DB更新
                db.puzzleDao().unlockPieceById(randomPiece);

                // ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
                // ★★★ SharedPreferences に取得時刻を保存 ★★★
                // ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
                SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
                prefs.edit().putLong(landmarkId + MainActivity.LAST_ACQUIRED_PREFIX, System.currentTimeMillis()).apply();
                // ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★

                // パズル完成チェック
                checkPuzzleCompletion(randomPiece);

                // UIスレッドで表示
                runOnUiThread(() -> {
                    instructionText.setText("ピースをゲット！");
                    Toast.makeText(shake_phone.this,
                            "ピース No." + (randomPiece + 1) + " を手に入れた！",
                            Toast.LENGTH_LONG).show();

                    // 遷移
                    Intent intent = new Intent(shake_phone.this, PieceGetActivity.class);
                    intent.putExtra("pieceNumber", randomPiece);
                    startActivity(intent);

                    // 2秒後に終了
                    new android.os.Handler().postDelayed(this::finish, 2000);
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(shake_phone.this,
                                "ピース取得中にエラーが発生しました",
                                Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void checkPuzzleCompletion(int unlockedPieceId) {
        try {
            int puzzleId = db.puzzleDao().getPuzzleIdForPiece(unlockedPieceId);
            if (puzzleId > 0) {
                List<PuzzleData> allPieces = db.puzzleDao().getPiecesForPuzzle(puzzleId);
                boolean allUnlocked = true;
                for (PuzzleData piece : allPieces) {
                    if (!piece.isUnlocked()) {
                        allUnlocked = false;
                        break;
                    }
                }
                if (allUnlocked) {
                    db.puzzleDao().updatePuzzleAsCompleted(puzzleId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 何もしない
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