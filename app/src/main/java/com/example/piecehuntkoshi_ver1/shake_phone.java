private void acquirePiece() {
    pieceAcquired = true;

    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

    databaseExecutor.execute(() -> {
        // ランダムなピース番号（0〜8）を生成
        int randomPiece = new java.util.Random().nextInt(9);

        // DBでピースをアンロック
        db.puzzleDao().unlockPieceById(randomPiece);

        // パズル完成チェック
        checkPuzzleCompletion(randomPiece);

        // UIスレッドで画面遷移・メッセージ表示
        runOnUiThread(() -> {
            instructionText.setText("ピースをゲット！");
            Toast.makeText(shake_phone.this, "ピース No." + (randomPiece + 1) + " を手に入れた！", Toast.LENGTH_LONG).show();

            // PieceGetActivity に遷移して番号を渡す
            Intent intent = new Intent(shake_phone.this, PieceGetActivity.class);
            intent.putExtra("pieceNumber", randomPiece);
            startActivity(intent);

            // 少し待って終了
            new android.os.Handler().postDelayed(this::finish, 2000);
        });
    });
}

// パズル完成チェック
private void checkPuzzleCompletion(int unlockedPieceId) {
    int puzzleId = db.puzzleDao().getPuzzleIdForPiece(unlockedPieceId);
    if (puzzleId > 0) {
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
