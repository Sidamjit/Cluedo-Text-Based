/**
 * A RoomCell represents a cell belonging to an Estate that is able to be occupied by a Piece.
 */

public class RoomCell implements Cell {

    public final int row;
    public final int col;
    public Estate estate = null;
    public boolean isOccupied = false;
    public Piece cellItem;

    public RoomCell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public void setCellPiece(Piece cellItem) {
        this.cellItem = cellItem;
    }

    @Override
    public void setIsOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }

    public void setEstate(Estate estate) {
        this.estate = estate;
    }

    @Override
    public boolean getIsOccupied() {
        return isOccupied;
    }

    @Override
    public Piece getCellPiece() {
        return this.cellItem;
    }

    public Estate getEstate() {
        return this.estate;
    }

    @Override
    public String toString() {
        if (cellItem != null) {
            if (cellItem.getName().equals("Lucilla")) {
                return "| L |";
            }
            else if (cellItem.getName().equals("Bert")) {
                return "| B |";
            }
            else if (cellItem.getName().equals("Maline")) {
                return "| M |";
            }
            else if (cellItem.getName().equals("Percy")) {
                return "| P |";
            }
            else if (cellItem.getName().equals("Broom")) {
                return "|!b!|";
            }
            else if (cellItem.getName().equals("Scissors")) {
                return "|!s!|";
            }
            else if (cellItem.getName().equals("Knife")) {
                return "|!k!|";
            }
            else if (cellItem.getName().equals("Shovel")) {
                return "|!S!|";
            }
            else if (cellItem.getName().equals("iPad")) {
                return "|!i!|";
            }
        }
        return "|***|";
    }
}