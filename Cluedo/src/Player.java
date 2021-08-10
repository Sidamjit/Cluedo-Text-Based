
import java.util.*;
/**
 * The player is someone who controls a PlayerPiece in the game. The player is asked prompts
 * such as if they want to move or accuse.
 */
public class Player{

  private final String name;
  private List<Card> hand = new ArrayList<>();
  private PlayerPiece playerPiece;
  private boolean lost = false;

  public Player(String name) {
    this.name = name;
  }


  public String getName() {
    return name;
  }

  public List<Card> getHand() {
    return hand;
  }

  public PlayerPiece getPlayerPiece(){
    return playerPiece;
  }


  public void addToHand(Card card) {
    hand.add(card);
  }

  public void addPlayerPiece(PlayerPiece playerPiece){
    this.playerPiece = playerPiece;
  }

  public void setHand(List<Card> hand) {
    this.hand = hand;
  }

  @Override
  public String toString() {
    return "Player{" +
            "name='" + name + '\'' +
            '}';
  }

    public void setLost(boolean b) {
      this.lost = true;
    }

  public boolean getLost() {
    return lost;
  }
}