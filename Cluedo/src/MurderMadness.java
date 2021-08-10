
import java.security.spec.RSAOtherPrimeInfo;
import java.util.*;
import java.util.List;

public class
MurderMadness
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  private Board board;
  private Player player;
  private List<Card> cards = new ArrayList<>();
  private final ArrayList<Card> cardsCopy = new ArrayList<>();
  private List<Player> players = new ArrayList<>();
  private List<Estate> estates = new ArrayList<>();

  private List<Weapon> weapons = new ArrayList<>();
  private List<String> characterNames = new ArrayList<>();
  private List<Card> estateCards = new ArrayList<>();

  private List<Card> weaponCards = new ArrayList<>();
  private List<Card> characterCards = new ArrayList<>();
  private List<Suggestion> suggestions = new ArrayList<>();
  private Map<String, Integer> playerPieceXs = new HashMap<>();
  private Map<String, Integer> playerPieceYs = new HashMap<>();
  private Card solutionCharacter;
  private Card solutionWeapon;
  private Card solutionEstate;
  public boolean won = false;
  private boolean lost = false;
  public boolean isInRoom = false;
  public int newRow;
  public int newCol;
  private int moves = 0;

  /**
   * The MurderMadness method sets up and plays the game. This involves creating the Board,
   * initialising the deck of cards, seeing how many people are playing, shuffling and
   * handing out cards to the players, creating the murder solution and giving the player
   * pieces their initial location on the Board.
   */
  public MurderMadness(){
    makeEstates();
    setWeapons(); // abbie
    addCharacterNames();
    addInitialLocation();
    boolean validPlayersCount = false;
    int playerCount = 0;
    Scanner sc= new Scanner(System.in);
    while(!validPlayersCount) {
      System.out.print("How many players are playing? [Entre a number between 2-4]\n>");
      while (!sc.hasNextInt()) {
        sc.next();
        System.out.print("How many players are playing? [Entre a number between 2-4]\n>");
      }
      playerCount = sc.nextInt();
      if (playerCount>1 && playerCount<=4){
        validPlayersCount = true;
      }
    }
    int counter = 1;
    for (int i = 0; i < playerCount; i++) {
      String playerName = characterNames.get(i);
      Player player = new Player(playerName);
      players.add(player);
      System.out.println("Player " + counter + " is " + playerName);
      player.addPlayerPiece(new PlayerPiece(player.getName(), playerPieceXs.get(playerName), playerPieceYs.get(playerName)));
      counter++;
    }
    makeCards();
    this.cardsCopy.addAll(this.cards);
    createBoard();
    play();
  }


  /**
   * The play method continuously loops until someone wins the game OR everyone loses. It is constantly
   * going around the circle of players and letting them take their turns accordingly.
   */
  public void play(){
    while(!won && !lost){
      for (Player player : this.players) {
        moves = 0;
        if (player.getLost()){
          if (checkAllPlayerLost()){
            System.out.println("Game over!");
            System.out.println("The murder solution was that " + solutionCharacter.getName() + " committed murder using the " + solutionWeapon.getName() + " weapon in " + solutionEstate.getName() + ".");
            this.lost = true;
            return;
          }
          continue;
        }
        System.out.println(player.getName() + "'s turn");
        boolean validAnswer = false;
        while(!validAnswer) {
          Scanner scanner = new Scanner(System.in);
          System.out.print("Would you like to move, suggest, accuse or end?\n>");
          String answer = scanner.nextLine();
          if (answer.equals("move")) {
            boolean moveMore = false;
            while (!moveMore) {
              validAnswer = true;
              move(player);
              if (player.getPlayerPiece().getEstate()==null){
                isInRoom = false;
              }
              if (isInRoom) {
                boolean correctAnswer = false;
                while (!correctAnswer) {
                    System.out.print("Would you like to move, suggest, accuse or end?\n>");
                    answer = scanner.nextLine();
                    correctAnswer = checkAnswer(answer, player);
                  if (moves==0 && correctAnswer && answer.equals("move")){
                    correctAnswer = true;
                    moveMore = true;
                    System.out.println("You ran out of moves.");
                  }
                    if (!answer.equals("move") && correctAnswer) {
                      moveMore = true;
                    }
                }
              } else {
                System.out.println("Your ran out of moves.");
                moveMore = true;
              }
            }
          }
          if (answer.equals("suggest")) {
            if (player.getPlayerPiece().getEstate() instanceof Estate){
              validAnswer = true;
              suggest(player);
            } else {
              System.out.println("You must be in an estate to make a suggestion.");
            }
            continue;
          }
          if (answer.equals("accuse")) {
            if (player.getPlayerPiece().getEstate() instanceof Estate){
              validAnswer = true;
              accuse(player);
              if (won) {
                return;
              }
            } else {
              System.out.println("You must be in an estate to make an accusation.");
            }
            continue;
          }
          if (answer.equals("end")) {
            validAnswer = true;
            continue;
          }
          if (!validAnswer) {
            System.out.print("Not a valid response. Please type either 'move', 'suggest' or 'accuse'!\n");
          }
        }
      }
    }
  }

  /**
   * Checks if all the players in the game have been eliminated as a result
   * of accusing incorrectly. If this is the case, return true.
   * @return
   */
  private boolean checkAllPlayerLost() {
    boolean allPlayersLost = false;
    int counter = 0;
    for (Player player : this.players){
      if (player.getLost()){
        counter++;
      }
    }
    if (counter==players.size()){
      allPlayersLost = true;
    }
    return allPlayersLost;
  }

  private boolean checkAnswer(String answer, Player player) {
    if(answer.equals("move") || answer.equals("end") || answer.equals("accuse") || answer.equals("suggest")){
      return true;
    }
    else {
      System.out.println("Please choose from 'move', 'suggest', 'accuse' or 'end'.");
      return false;
    }
  }

  /**
   * The move method enables the players to change their x/y location on the board when
   * they play the game. This is done by rolling a dice, calculating their total moves
   * allowed and changing their row and col fields.
   * @param player
   *        the player whose turn it currently is.
   */
  private void move(Player player) {
    if (moves==0) {
      this.moves = rollDice();
      System.out.println("We automatically rolled the dice for you!");
      System.out.println("Total moves allowed: " + moves);
    }
    Scanner scanner = new Scanner(System.in);
    String move = "";

    PlayerPiece playerPiece = player.getPlayerPiece();
    int oldRow = playerPiece.getRow();
    int oldCol = playerPiece.getCol();
    System.out.println("Current location of your player is:\nrow: " + oldRow + "\ncol: " + oldCol);
    while (moves > 0 ) {
      boolean validEntry = false;
      boolean validMove = false;
      while (!validEntry) {
        System.out.print("Move using 'w', 'a', 's', 'd' keys.\n>");
        move = scanner.nextLine();
        oldRow = playerPiece.getRow();
        oldCol = playerPiece.getCol();
        validEntry = checkValidEntry(playerPiece.getRow(), playerPiece.getCol(), move);
        if(validEntry) {
          validEntry = checkValid(oldRow, oldCol);
        }
      }
      if (player.getPlayerPiece().getEstate()==null) {
        moves = moves - 1;
      }
      board.movePlayerPiece(player.getPlayerPiece(), newRow, newCol);
      player.getPlayerPiece().move(newRow, newCol);
      System.out.println("Remaining moves left: " + moves);
      Cell newCell = board.getCell(newRow, newCol);
      if (newCell instanceof RoomCell) {
        player.getPlayerPiece().setEstate(((RoomCell) newCell).estate);
        System.out.println("You are now in the " + player.getPlayerPiece().getEstateName() + " estate.");
        isInRoom = true;
        System.out.println("Current location of your player is:\nrow: " + newRow + "\ncol: " + newCol);
        return;
      }
      System.out.println("Your current location on the board is:\nrow: " + newRow + "\ncol: " + newCol);
    }
  }

  /**
   * Checks to see if the player's piece is using the doors to enter/exit estates,
   * not going out of bounds or occupying grey cells. Much of this is done by comparing
   * their old x/y location to their new one, to see if they took a "correct path"
   * or an "incorrect shortcut".
   * @param oldRow
   *        the player piece's x location on the board
   * @param oldCol
   *        the player piece's y location on the board
   * @return
   */
  private boolean checkValid(int oldRow, int oldCol) {
    if (newRow>23 || newRow<0 || newCol<0 || newCol>23){
      System.out.println("You cannot move out of bounds.");
      return false;
    }
    Cell oldCell = board.getCell(oldRow, oldCol);
    Cell newCell = board.getCell(newRow, newCol);

    if (newCell instanceof RoomCell && oldCell instanceof GroundCell){
      System.out.println("You must go through a door to enter an estate.");
      return false;
    }
    if (newCell.getCellPiece() instanceof PlayerPiece || newCell.getCellPiece() instanceof Weapon){
      System.out.println("The cell you are trying to move to is occupied.");
      return false;
    }
    if (newCell instanceof GroundCell && oldCell instanceof RoomCell){
      System.out.println("You must go through a door to exit an estate.");
      return false;
    }
    if (newCell instanceof GreyCell){
      System.out.println("You are not allowed to occupy grey cells.");
      return false;
    }

    return true;
  }

  /**
   * For a player piece to move around the Board, the corresponding Player must enter
   * the correct inputs. checkValidEntry is responsible for checking if any
   * user's move inputs are correct.
   * @param oldRow
   *        the player piece's x location on the board
   * @param oldCol
   *        the player piece's y location on the board
   * @param move
   *        the direction the player wants to move
   * @return
   */
  private boolean checkValidEntry(int oldRow, int oldCol, String move) {
    newRow = oldRow;
    newCol = oldCol;
      if (move.equals("w")) {
        newRow = newRow - 1;
        return true;
      }
      else if (move.equals("s")) {
        newRow = newRow + 1;
        return true;
      }
      else if (move.equals("a")) {
        newCol = newCol - 1;
       return true;
      }
      else if (move.equals("d")) {
        newCol = newCol + 1;
        return true;
      }
      System.out.println("Not a valid Move.");
      return false;
  }

  /**
   * This enables the player to choose a character and weapon card (the estate card
   * is the estate they're in). Then these three cards are passed around the group so
   * each player can refute accordingly.
   * @param suggester
   *        The player who is making the suggestion
   */
  private void suggest(Player suggester) {
    boolean cardsChosen = false;
    Card estateCard = null;
    Card characterCard = null;
    Card weaponCard = null;
    while(!cardsChosen) {
      Scanner scanner = new Scanner(System.in);
      System.out.print("Which person would you like to suggest?\nYour choices are 'Lucilla', 'Bert', 'Maline', 'Percy'.\n>");
      String characterName = scanner.nextLine();
      System.out.print("Which weapon would you like to suggest?\nYour choices are 'Broom', 'Scissors', 'Knife', 'Shovel', 'iPad'.\n>");
      String weaponName = scanner.nextLine();
      for (Card card : this.cardsCopy) {
        if (card.getName().equals(suggester.getPlayerPiece().getEstateName())) {
          estateCard = card;
        }
        if (card.getName().equals(characterName)) {
          characterCard = card;
        }
        if (card.getName().equals(weaponName)) {
          weaponCard = card;
        }
      }
      if (weaponCard == null) {
        System.out.println("The weapon you chose doesn't exist.\nYour choices are 'Broom', 'Scissors', 'Knife', 'Shovel', 'iPad'.");
      }
      if (characterCard == null) {
        System.out.println("The person you chose doesn't exist.\nYour choices are 'Lucilla', 'Bert', 'Maline', 'Percy'.");
      }
      if (weaponCard != null && characterCard != null){
        cardsChosen = true;
      }
    }
    System.out.println("Cards chosen are: " + characterCard.getName() + ", " + weaponCard.getName() + ", " + estateCard.getName() + ".");
    Suggestion suggestion = new Suggestion(suggester, weaponCard, characterCard, estateCard);
    suggestions.add(suggestion);
    passTheSuggestionAround(suggestion);
  }

  /**
   * The suggestion is automatically compared with each player's deck of cards - a
   * refutation is made if need be. If there is more than one card that a player can refute,
   * the method gives them the option to choose which card.
   * @param suggestion
   *        The three cards (character, weapon and estate) that
   *        make up the suggestion.
   */
  private void passTheSuggestionAround(Suggestion suggestion) {
    Player suggester = suggestion.getSuggester();
    Card characterCard = suggestion.getCharacterCard();
    Card weaponCard = suggestion.getWeaponCard();
    Card estateCard = suggestion.getEstateCard();

    for(Weapon weapon : weapons) {
      if (weapon.getWeaponName().equals(weaponCard.getName()) && !weapon.getEstateName().equals(estateCard.getName())) {
        weapon.setEstate(suggester.getPlayerPiece().getEstate());

        board.moveWeapons(weapon, suggester.getPlayerPiece().getEstate().roomCells.get(2).row, suggester.getPlayerPiece().getEstate().roomCells.get(2).col);
        System.out.println("The " + weapon.getWeaponName() + " weapon was moved to " + weapon.getEstateName());
      }
    }

    for (Player player : this.players){
      if (player.getName().equals(suggester.getName())){
        continue;
      }
      if (player.getName().equals(characterCard.getName())) {
        player.getPlayerPiece().setEstate(suggester.getPlayerPiece().getEstate());
        // set the coordinates of the suggested murderer
        board.movePlayerPiece(player.getPlayerPiece(), suggester.getPlayerPiece().getEstate().roomCells.get(1).row, suggester.getPlayerPiece().getEstate().roomCells.get(1).col);
        System.out.println(player.getName() + " was moved to " + player.getPlayerPiece().getEstateName());
      }

      List<Card> hand = player.getHand();
      List<Card> similarities = new ArrayList<>();
      Scanner scanner = new Scanner(System.in);
      for(Card card : hand){
        if (card.getName().equals(characterCard.getName()) || card.getName().equals(weaponCard.getName()) || card.getName().equals(estateCard.getName())){
          similarities.add(card);
        }
      }
      if (similarities.size()>1){
        Card answerCard = null;
        boolean validAnswer = false;
        while(!validAnswer) {
          System.out.println(player.getName() + "'s turn to refute.");
          System.out.println("Your cards are:\n" + similarities);
          System.out.print("Which card would you like to suggest?\n>");
          String answer = scanner.nextLine();
          for (Card card : similarities){
            if (card.getName().equals(answer)){
              answerCard = card;
            }
          }
          if (answerCard == null){
            System.out.println("You do not own that card.");
          }
          else {
            validAnswer = true;
          }
        }
        System.out.println(player.getName() + " has refuted your suggestion as they have the " + answerCard.getName() + " card.");
      }
      else{
        if (similarities.size()==1){
          System.out.println(player.getName() + " has refuted your suggestion as they have the " + similarities.get(0).getName() + " card.");
        }
        else{
          System.out.println(player.getName() + " did not refute.");
        }
      }

    }
  }

  /**
   * A player can make an accusation and if it's correct then they win the game. This
   * ends the while loop in play() and ends the whole game. If all the players in the game
   * accuse incorrectly, this is counted as 'game over' and ends the while loop in play()
   * as well.
   * The accuser selects three cards that make up the murder scenario which is then
   * compared with the solution cards.
   * @param accuser
   *        The person who is completing the accusation.
   */
  private void accuse(Player accuser) {
    boolean cardsChosen = false;
    Card estateCard = null;
    Card characterCard = null;
    Card weaponCard = null;
    while(!cardsChosen) {
      Scanner scanner = new Scanner(System.in);
      System.out.print("Which person would you like to accuse?\nYour choices are 'Lucilla', 'Bert', 'Maline', 'Percy'.\n>");
      String characterName = scanner.nextLine();
      System.out.print("Which weapon do you think they used?\nYour choices are 'Broom', 'Scissors', 'Knife', 'Shovel', 'iPad'.\n>");
      String weaponName = scanner.nextLine();
      for (Card card : this.cardsCopy) {
        if (card.getName().equals(accuser.getPlayerPiece().getEstateName())) {
          estateCard = card;
        }
        if (card.getName().equals(characterName)) {
          characterCard = card;
        }
        if (card.getName().equals(weaponName)) {
          weaponCard = card;
        }
      }
      if (weaponCard == null) {
        System.out.println("The weapon you chose doesn't exist.\nYour choices are 'Broom', 'Scissors', 'Knife', 'Shovel', 'iPad'.");
      }
      if (characterCard == null) {
        System.out.println("The estate you chose doesn't exist.\nYour choices are 'Lucilla', 'Bert', 'Maline', 'Percy'.");
      }
      if (weaponCard != null && characterCard != null){
        cardsChosen = true;
      }
    }
    System.out.println("Your accusation is that " + characterCard.getName() + " committed murder using the " + weaponCard.getName() + " weapon in " + estateCard.getName() + ".");
    if(solutionWeapon.getName().equals(weaponCard.getName()) && solutionEstate.getName().equals(estateCard.getName()) && solutionCharacter.getName().equals(characterCard.getName())) {
          won = true;
          System.out.println("You have figured out the murder case! You win!");
    }
    else {
      System.out.println("Uh oh! Your accusation was wrong and now you may only refute.");
      accuser.setLost(true);
    }
  }


  /**
   * Creates the deck of cards for the game.
   */
  private void makeCards() {
    //Making Estates cards
    estateCards.add(new Card("Haunted House", "Estate"));
    estateCards.add(new Card("Manic Manor", "Estate"));
    estateCards.add(new Card("Villa Celia", "Estate"));
    estateCards.add(new Card("Calamity Castle", "Estate"));
    estateCards.add(new Card("Peril Palace", "Estate"));

    //Making Weapons cards
    weaponCards.add(new Card("Broom", "Weapon"));
    weaponCards.add(new Card("Scissors", "Weapon"));
    weaponCards.add(new Card("Knife", "Weapon"));
    weaponCards.add(new Card("Shovel", "Weapon"));
    weaponCards.add(new Card("iPad", "Weapon"));

    //Making Characters cards
    characterCards.add(new Card("Lucilla", "Character"));
    characterCards.add(new Card("Bert", "Character"));
    characterCards.add(new Card("Maline", "Character"));
    characterCards.add(new Card("Percy", "Character"));

    createSolution();
  }

  /**
   * Randomises the murder scenario and removes these cards
   * from the general deck used to hand out to the players.
   */
  private void createSolution() {
    Random random = new Random();
    int guess = random.nextInt(4);
    solutionCharacter = characterCards.get(guess);
    this.cardsCopy.add(solutionCharacter);
    characterCards.remove(guess);
    guess = random.nextInt(5);
    solutionWeapon = weaponCards.get(guess);
    this.cardsCopy.add(solutionWeapon);
    weaponCards.remove(guess);
    solutionEstate = estateCards.get(guess);
    this.cardsCopy.add(solutionEstate);
    estateCards.remove(guess);
    assignCards();
  }

  /**
   * Retrieves the deck of cards and assigns them
   * to each player in the game.
   */
  private void assignCards() {
    List<Card> assigningList = new ArrayList<>();
    assigningList.addAll(characterCards);
    assigningList.addAll(weaponCards);
    assigningList.addAll(estateCards);
    cards.addAll(assigningList);

    Random random = new Random();
    while (assigningList.size() > 1) {
      int guess = random.nextInt(assigningList.size());
      for (Player player : players) {
        player.addToHand(assigningList.get(guess));
        assigningList.remove(guess);
        if (assigningList.size() == 0) {
          break;
        }
        guess = random.nextInt(assigningList.size());
      }
    }
  }

  /**
   * Creatres and adds to the list of the estates on the Board
   */
  private void makeEstates() {
    estates.add(new Estate("Haunted House"));
    estates.add(new Estate("Manic Manor"));
    estates.add(new Estate("Villa Celia"));
    estates.add(new Estate("Calamity Castle"));
    estates.add(new Estate("Peril Palace"));
  }



  /**
   * Creates the weapon objects that are on the Board and randomly
   * assigns each one in an estate.
   */
  public void setWeapons() {
    // Create weapon objects and add to list of weapons in the game
    weapons.add(new Weapon("Broom"));
    weapons.add(new Weapon("Scissors"));
    weapons.add(new Weapon("Knife"));
    weapons.add(new Weapon("Shovel"));
    weapons.add(new Weapon("iPad"));

    List<Estate> assigningEstates = new ArrayList<>(); // gather list of Weapons
    assigningEstates.addAll(estates);

    // the total number of Weapons and the total number of estates, respectively.
    // they will always be matching as there is one weapon per estate
    int estatesAndWeaponsTotal = 5;
    Random random = new Random();
    // keep looping until there are no more estates left to assign to a respective weapon
    while (assigningEstates.size() != 0) {
      // random number between 1-5
      int randomNum = random.nextInt(estatesAndWeaponsTotal);
      // for as many Weapons there are,
      for (int i = 0; i < weapons.size(); i++) {
        // set the current Weapon's Estate field to to a randomly chosen Estate from the Estate list
        weapons.get(i).setEstate(assigningEstates.get(randomNum));
        // remove the Estate once assigned,
        assigningEstates.remove(randomNum);
        // if there are no more Estates left, end everything
        if (assigningEstates.size() == 0) {
          break;
        }
        // make the new random number range as the new size of the Estate list
        randomNum = random.nextInt(assigningEstates.size());
      }
    }
  }

  /**
   * Sets the names of the players and the player pieces.
   */
  private void addCharacterNames() {
    characterNames.add("Lucilla");
    characterNames.add("Bert");
    characterNames.add("Maline");
    characterNames.add("Percy");
  }

  /**
   * Sets the initial location of each player piece on the Board.
   */
  private void addInitialLocation(){
    playerPieceXs.put("Lucilla", 11);
    playerPieceYs.put("Lucilla", 1);
    playerPieceXs.put("Bert", 1);
    playerPieceYs.put("Bert", 9);
    playerPieceXs.put("Maline", 9);
    playerPieceYs.put("Maline", 22);
    playerPieceXs.put("Percy", 22);
    playerPieceYs.put("Percy", 14);
  }

  /**
   * Method for virtual dice which tells the player
   * how many moves they get.
   * @return
   */
  private int rollDice() {
    Random dice = new Random();
    int sum = (dice.nextInt(5) + 1) + (dice.nextInt(5) + 1);
    return sum;
  }

  private void createBoard() {
    this.board = new Board(estates, players, weapons);
  }


  public static void main(String args []){

    MurderMadness game = new MurderMadness();

  }
}