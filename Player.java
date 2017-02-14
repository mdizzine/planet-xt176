/**
 * Class that creates a player object
 * 
 * @author Matt Dizzine
 * @version 03.19.15
 */
public class Player{
    private Inventory inventory;
    private String name;
    private int score;
    private boolean translatorOn;
    private boolean flashlightOn;
    /**
     * Constructor
     */
    public Player(String name){
        inventory = new Inventory();
        this.name = name.toUpperCase();
        score=0;
        translatorOn=false;
        flashlightOn=false;
    }
    
    /**
     * Method to increase score
     * @param increase
     */
    public void increaseScore(int increase){
        score+=increase;
    }
    
    /**
     * Method to set score
     * @param anyScore
     */
    public void setScore(int anyScore){
        score = anyScore;
    }
    
    /**
     * Method to set translator
     * @param on
     */
    public void setTranslator(boolean on){
        translatorOn=on;
    }
    
    /**
     * Method to set flashlight
     * @param on
     */
    public void setFlashlight(boolean on){
        flashlightOn=on;
    }
    
    /**
     * Method to set Inventory
     * @param inventory
     */
    public void setInventory(Inventory inventory){
        this.inventory=inventory;
    }
    
    /**
     * Method to return player's name
     * @return name
     */
    public String getName(){
        return name;
    }
    
    /**
     * Method to return inventory
     * @return inventory
     */
    public Inventory getInventory(){
        return inventory;
    }
    
    /**
     * Method to return score
     * @return score
     */
    public int getScore(){
        return score;
    }
   
    /**
     * Method to return if flashlight is on
     * @return flashlightOn
     */
    public boolean isFlashlightOn(){
        return flashlightOn;
    }
}