package com.example.piecehuntkoshi_ver1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;


public class PieceGetActivity extends AppCompatActivity {

    private MediaPlayer soundEffectPlayer;
    private LottieAnimationView lottieView;

    private final int[] pieceImages = {
            R.drawable.piece_1, R.drawable.piece_2, R.drawable.piece_3,
            R.drawable.piece_4, R.drawable.piece_5, R.drawable.piece_6,
            R.drawable.piece_7, R.drawable.piece_8, R.drawable.piece_9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_piece_get);

        lottieView = findViewById(R.id.lottie_animation_view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int pieceNumber = getIntent().getIntExtra("pieceNumber", 0);

        ImageView pieceImageView = findViewById(R.id.piece_image_view);
        if (pieceNumber >= 0 && pieceNumber < pieceImages.length) {
            pieceImageView.setImageResource(pieceImages[pieceNumber]);
        }

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.piece_get_animation);
        pieceImageView.startAnimation(anim);

        playSoundEffect(R.raw.get_fanfare);

        if (lottieView != null) {
            lottieView.playAnimation();
        }

        Button backButton = findViewById(R.id.backmainbutton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PieceGetActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }



    private void playSoundEffect(int soundResourceId) {
        if (soundEffectPlayer != null) {
            soundEffectPlayer.release();
            soundEffectPlayer = null;
        }
        soundEffectPlayer = MediaPlayer.create(this, soundResourceId);
        if (soundEffectPlayer != null) {
            soundEffectPlayer.setOnCompletionListener(mp -> {
                mp.release();
                soundEffectPlayer = null;
            });
            soundEffectPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundEffectPlayer != null) {
            soundEffectPlayer.release();
            soundEffectPlayer = null;
        }
    }
}