package com.example.piecehuntkoshi_ver1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface PuzzleDao {

    // --- Puzzle methods ---

    @Insert
    void insertPuzzle(Puzzle puzzle);

    @Query("SELECT * FROM puzzles")
    List<Puzzle> getAllPuzzles();

    @Query("SELECT * FROM puzzles WHERE id = :puzzleId")
    Puzzle getPuzzleById(int puzzleId);

    @Query("UPDATE puzzles SET is_completed = 1 WHERE id = :puzzleId")
    void updatePuzzleAsCompleted(int puzzleId);

    @Query("SELECT * FROM puzzles WHERE is_completed = 0 ORDER BY id ASC LIMIT 1")
    Puzzle getFirstUncompletedPuzzle();

    // --- PuzzleData (Piece) methods ---

    @Insert
    void insertAllPieces(List<PuzzleData> pieces);

    @Query("SELECT * FROM puzzle_pieces WHERE puzzle_id = :puzzleId ORDER BY piece_index ASC")
    List<PuzzleData> getPiecesForPuzzle(int puzzleId);

    @Query("UPDATE puzzle_pieces SET is_unlocked = 1 WHERE id = :pieceId")
    void unlockPieceById(int pieceId);

    @Query("SELECT * FROM puzzle_pieces WHERE puzzle_id = :puzzleId AND is_unlocked = 0 ORDER BY RANDOM() LIMIT 1")
    PuzzleData getARandomUnlockedPiece(int puzzleId);

}