package com.example.piecehuntkoshi_ver1;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

// 加速度センサーを使ってシェイク検知
public class shake_phone extends Activity implements SensorEventListener {
    // センサー管理用の変数
    private SensorManager sensorManager;
    private Sensor accelerometer;

    // シェイク判定の閾値
    private static final float SHAKE_THRESHOLD = 15.0f;

    // 連続検知を防ぐタイマー
    private long lastShakeTime = 0;

    // Activityが生成されたときに呼ばれる
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    // 親クラスの初期化
        setContentView(R.layout.shake_phone);    // 振ってください画面の表示

        // SensorManagerの取得
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 加速度センサーの取得
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    // 画面が表示されたときにセンサーリスナー登録
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    // 画面が非表示になったときにセンサーリスナー解除（バッテリー節約）
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    // センサーの値が変化したときに呼ばれる
    @Override
    public void onSensorChanged(SensorEvent event) {
        // x, y, z軸の加速度を取得
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // 合成加速度を計算
        float acceleration = (float) Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;

        // 閾値を超えたら振ったと判定
        if (acceleration > SHAKE_THRESHOLD) {
            long now = System.currentTimeMillis();

            // 0.5秒以上感覚が空いていればシェイク処理を実行
            if (now - lastShakeTime > 500) {
                lastShakeTime = now;

                // 🔽 ピース獲得画面へ遷移
                Intent intent = new Intent(this, PieceGetActivity.class);
                startActivity(intent);
            }
        }
    }

    // センサーの精度が変化したときに呼ばれる（今回は使わないけど書かないとエラーになる）
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // 今回は未使用
    }

}
