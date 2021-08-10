/**
 * In the game, a GreyCell cannot be occupied by a player's piece
 * or a weapon.
 */
public class GreyCell implements Cell {

    boolean isOccupied = true;
    public int row;
    public int col;
    public Piece cellItem;

    public GreyCell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "|&&&|";
    }


    @Override
    public void setIsOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    @Override
    public void setCellPiece(Piece cellItem) {
        this.cellItem = cellItem;
    }


    @Override
    public boolean getIsOccupied() {
        return isOccupied;
    }

    @Override
    public Piece getCellPiece() {
        return this.cellItem;
    }
}