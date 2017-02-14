/**
 * Stores information for save point
 * 
 * @author Matt Dizzine
 * @version 3.19.15
 */
public class SavePoint{
    private Location location;
    private Player player;
    /**
     * Constructor
     */
    public SavePoint(){
        location=null;
        player = null;
    }
    
    /**
     * Method to set save point
     * @param location
     * @param name
     * @param score
     * @param inventory
     */
    public void setSavePoint(Location location, String name, int score, Inventory inventory){
        this.location = location;
        player = new Player(name);
        player.setScore(score);
        player.setInventory(inventory);
    }
    
    /**
     * Method to return location
     * @return location
     */
    public Location getLocation(){
        return location;
    }
    
    /**
     * Method to return player
     * @return player
     */
    public Player getPlayer(){
        return player;
    }
}