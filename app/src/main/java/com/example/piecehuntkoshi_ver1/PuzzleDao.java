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

    @Query("SELECT COUNT(*) FROM puzzles")
    int getPuzzleCount();

    @Query("UPDATE puzzles SET is_completed = 1 WHERE id = :puzzleId")
    void updatePuzzleAsCompleted(int puzzleId);

    // --- PuzzleData (Piece) methods ---

    @Insert
    void insertAllPieces(List<PuzzleData> pieces);

    // Get pieces for a specific puzzle, sorted by their index
    @Query("SELECT * FROM puzzle_pieces WHERE puzzle_id = :puzzleId ORDER BY piece_index ASC")
    List<PuzzleData> getPiecesForPuzzle(int puzzleId);

    @Query("SELECT COUNT(*) FROM puzzle_pieces")
    int getPieceCount();

    @Query("UPDATE puzzle_pieces SET is_unlocked = 1 WHERE id = :pieceId")
    void unlockPieceById(int pieceId);

    @Query("SELECT puzzle_id FROM puzzle_pieces WHERE id = :pieceId LIMIT 1")
    int getPuzzleIdForPiece(int pieceId);
}
