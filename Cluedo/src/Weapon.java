/**
 * Weapons are placed in estates on a RoomCell. Each estate starts with one weapon inside, however
 * when suggestions are made about the murder weapoon, that suggested weapon will move
 * to the estate of the suggester.
 */
public class Weapon implements Piece{

    public String weaponName;
    public Estate estateName;
    public int row;
    public int col;


    public Weapon(String weaponName){
        this.weaponName = weaponName;
    }

    public String getEstateName() {
        return this.estateName.getEstateName();
    }


    public Estate getEstate() { return this.estateName; }

    @Override
    public String getName() {
        return weaponName;
    }

    @Override
    public int getRow() {
        return this.row;
    }

    @Override
    public int getCol() {
        return this.col;
    }

    public String getWeaponName(){
        return this.weaponName;
    }


    public void setEstate(Estate estate) {
        this.estateName = estate;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }


    @Override
    public String toString() {
        return "Weapon{" +
                "estateName='" + estateName + '\'' +
                ", weaponName='" + weaponName + '\'' +
                '}';
    }
}