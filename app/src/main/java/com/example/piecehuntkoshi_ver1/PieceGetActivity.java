package com.example.piecehuntkoshi_ver1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class PieceGetActivity extends AppCompatActivity {

    private MediaPlayer soundEffectPlayer;
    private LottieAnimationView lottieView;
    private TextView pieceNumberText;

    // The fixed array of piece images is no longer needed.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece_get);

        lottieView = findViewById(R.id.lottie_animation_view);
        ImageView pieceImageView = findViewById(R.id.piece_image_view);
        pieceNumberText = findViewById(R.id.piece_number_text);
        Button backButton = findViewById(R.id.backmainbutton);

        Intent intent = getIntent();
        
        // This is now the primary way to get piece info
        int pieceImageResId = intent.getIntExtra("pieceImageResId", 0);
        int pieceNumberForDisplay = intent.getIntExtra("pieceNumberForDisplay", 0);

        // This is for the puzzle completion flow
        int pieceNumber = intent.getIntExtra("pieceNumber", -2); 
        boolean isPuzzleCompleted = intent.getBooleanExtra("isPuzzleCompleted", false);


        // --- Flow for Puzzle Completion --- 
        if (pieceNumber == -1) {
            int completedImageRes = intent.getIntExtra("completedPuzzleImage", 0);
            if (completedImageRes != 0) {
                pieceImageView.setImageResource(completedImageRes);
            }
            pieceNumberText.setText("パズル完成！");
            playSoundEffect(R.raw.get_fanfare);
            backButton.setText("マップに戻る");
            backButton.setOnClickListener(v -> {
                Intent mapIntent = new Intent(PieceGetActivity.this, MainActivity.class);
                mapIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(mapIntent);
                finish();
            });

        // --- Flow for REGULAR Piece Get ---
        } else if (pieceImageResId != 0) { 
            pieceImageView.setImageResource(pieceImageResId);
            pieceNumberText.setText("ピースNo." + pieceNumberForDisplay);
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.piece_get_animation);
            pieceImageView.startAnimation(anim);
            playSoundEffect(R.raw.get_fanfare);

            if (isPuzzleCompleted) {
                int puzzleImage = intent.getIntExtra("completedPuzzleImage", 0);

                backButton.setText("完成！！");
                backButton.setOnClickListener(v -> {
                    Intent completionIntent = new Intent(PieceGetActivity.this, PieceGetActivity.class);
                    completionIntent.putExtra("pieceNumber", -1);
                    completionIntent.putExtra("completedPuzzleImage", puzzleImage);
                    startActivity(completionIntent);
                    finish();
                });
            } else {
                backButton.setText("マップに戻る");
                backButton.setOnClickListener(v -> finish());
            }
        }

        if (lottieView != null) {
            lottieView.playAnimation();
        }
    }

    private void playSoundEffect(int soundResourceId) {
        if (soundEffectPlayer != null) {
            soundEffectPlayer.release();
        }
        soundEffectPlayer = MediaPlayer.create(this, soundResourceId);
        if (soundEffectPlayer != null) {
            soundEffectPlayer.setOnCompletionListener(mp -> mp.release());
            soundEffectPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundEffectPlayer != null) {
            soundEffectPlayer.release();
        }
    }
}
