/**
 * A Cell represents a square on the Board that can be occupied by a Piece.
 */

public interface Cell {
    void setIsOccupied(boolean occupied);
    boolean getIsOccupied();
    void setCellPiece(Piece cellItem);
    Piece getCellPiece();
}

