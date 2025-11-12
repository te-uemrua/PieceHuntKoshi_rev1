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

    @Query("SELECT COUNT(*) FROM puzzles")
    int getPuzzleCount();

    @Query("UPDATE puzzles SET is_completed = 1 WHERE id = :puzzleId")
    void updatePuzzleAsCompleted(int puzzleId);

    @Query("SELECT * FROM puzzles WHERE is_completed = 0 ORDER BY id ASC LIMIT 1")
    Puzzle getFirstUncompletedPuzzle();

    @Query("DELETE FROM puzzles")
    void deleteAllPuzzles();

    // --- PuzzleData (Piece) methods ---

    @Insert
    void insertAllPieces(List<PuzzleData> pieces);

    @Query("SELECT * FROM puzzle_pieces WHERE puzzle_id = :puzzleId ORDER BY piece_index ASC")
    List<PuzzleData> getPiecesForPuzzle(int puzzleId);

    @Query("SELECT COUNT(*) FROM puzzle_pieces")
    int getPieceCount();

    @Query("UPDATE puzzle_pieces SET is_unlocked = 1 WHERE id = :pieceId")
    void unlockPieceById(int pieceId);

    @Query("UPDATE puzzle_pieces SET is_unlocked = 1 WHERE puzzle_id = :puzzleId")
    void unlockAllPiecesForPuzzle(int puzzleId);

    @Query("SELECT puzzle_id FROM puzzle_pieces WHERE id = :pieceId LIMIT 1")
    int getPuzzleIdForPiece(int pieceId);

    // Gets the next piece in sequence
    @Query("SELECT * FROM puzzle_pieces WHERE puzzle_id = :puzzleId AND is_unlocked = 0 ORDER BY piece_index ASC LIMIT 1")
    PuzzleData getNextPieceToUnlock(int puzzleId);

    // Gets a random unlocked piece from the specified puzzle
    @Query("SELECT * FROM puzzle_pieces WHERE puzzle_id = :puzzleId AND is_unlocked = 0 ORDER BY RANDOM() LIMIT 1")
    PuzzleData getARandomUnlockedPiece(int puzzleId);
    
    @Query("DELETE FROM puzzle_pieces")
    void deleteAllPieces();

}

