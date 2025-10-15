package com.example.piecehuntkoshi_ver1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao // このインターフェースがDAOであることを示す
public interface PuzzleDao {

    // "puzzle_pieces"テーブルから全てのデータを取得する命令
    @Query("SELECT * FROM puzzle_pieces")
    List<PuzzleData> getAllPieces();

    // データをテーブルに挿入する命令
    // 同じIDのデータが既にあれば、新しいデータで上書きする(REPLACE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PuzzleData> pieces);

    // データを更新する命令
    @Update
    void updatePiece(PuzzleData piece);

    // テーブルの全データを削除する命令（デバッグ用など）
    @Query("DELETE FROM puzzle_pieces")
    void deleteAllPieces();

    // テーブルにデータがいくつあるか数える命令
    @Query("SELECT COUNT(*) FROM puzzle_pieces")
    int getPieceCount();
}
