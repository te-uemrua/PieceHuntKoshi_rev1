package com.example.piecehuntkoshi_ver1;

public class Piece {
    //メンバ変数
    private int pieceResourceId;
    private boolean isAcquired;

    //コンストラクタ
    public Piece(int pieceResourceId, boolean isAcquired){
        this.pieceResourceId = pieceResourceId;
        this.isAcquired = isAcquired;
    }

    //ゲッター
    public int getPieceResourceId(){
        return this.pieceResourceId;
    }

    public boolean isAcquired(){
        return this.isAcquired;
    }

    //セッター
    public void setPieceResourceId(int pieceResourceId){
        this.pieceResourceId = pieceResourceId;
    }

    public void setAcquired(boolean isAcquired){
        this.isAcquired = isAcquired;
    }


}
