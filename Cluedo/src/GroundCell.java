/**
 * A GroundCell is any cell that a player's piece can occupy that's not
 * part of an Estate.
 */
public class GroundCell implements Cell {

    public int row;
    public int col;
    public boolean isOccupied = false;
    public Piece cellItem;

    public GroundCell(int row, int col) {
        this.row = row;
        this.col = col;
        this.cellItem = null;
    }

    @Override
    public void setCellPiece(Piece cellItem) {
        this.cellItem = cellItem;
    }

    @Override
    public void setIsOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }

    @Override
    public Piece getCellPiece() {
        return this.cellItem;
    }

    @Override
    public boolean getIsOccupied() {
        return isOccupied;
    }

    @Override
    public String toString() {
        if (cellItem != null ) {
            if (cellItem.getName().equals("Lucilla")) {
                return "| L |";
            }
            else if (cellItem.getName().equals("Bert")) {
                return "| B |";
            }
            else if(cellItem.getName().equals("Maline")) {
                return "| M |";
            }
            else if (cellItem.getName().equals("Percy")) {
                return "| P |";
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
        return "|___|";
    }
}