/**
 * WinningLocation is a child of the location class
 * 
 * @author Matt Dizzine
 * @version 5.10.15
 */
public class WinningLocation extends Location{

/**
 * Constructor
 */
public WinningLocation(boolean hasItem, boolean isSavePoint, boolean isGameOver){
    super(hasItem, isSavePoint, isGameOver);
}

/**
 * retrieve string used when player wins the game
 * @return win
 */
public String getWin(){
    String win = "YOU HAVE SURVIVED PLANET XT17-6, ";
    return win;
}

/**
 * retrieve whether or not player has won the game
 * @return true
 */
public boolean hasWon(){
    return true;
}
}