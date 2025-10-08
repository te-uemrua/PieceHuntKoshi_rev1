package com.example.piecehuntkoshi_ver1;

import android.app.Activity;    // 画面機能(Activity)のインポート
import android.os.Bundle;    // 画面状態管理機能(Bundle)のインポート

public class shake_phone extends Activity {    // Activityの継承
    @Override
    protected void onCreate(Bundle savedInstanceState) {    // 最初に呼ばれる
        super.onCreate(savedInstanceState);    // 親の初期化
        setContentView(R.layout.shake_phone);    // XML画面の表示
    }
}
