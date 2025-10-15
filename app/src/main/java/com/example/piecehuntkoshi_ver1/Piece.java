package com.example.piecehuntkoshi_ver1;

public class Piece {
    //  すべてのフィールドをfinalにして、変更不可能なクラスにする
    private final int id;
    private final int pieceResourceId;
    private final boolean isAcquired;

    public Piece(int id, int pieceResourceId, boolean isAcquired) {
        this.id = id;
        this.pieceResourceId = pieceResourceId;
        this.isAcquired = isAcquired;
    }

    // getId() は将来データベースの主キーとして使うため、警告が出ていても残しておきます
    public int getId() {
        return id;
    }

    public int getPieceResourceId() {
        return pieceResourceId;
    }

    public boolean isAcquired() {
        return isAcquired;
    }


}

