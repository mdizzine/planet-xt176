import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
/**
 * A class that creates a location in the game map
 * 
 * @author Matt Dizzine
 * @version 5.10.15
 */ 
public class Location{
    private String description;
    private boolean hasItem;
    private boolean isSavePoint;
    private boolean isGameOver;
    private HashMap<String, Location> exits;
    private ArrayList<Item> itemList;
    private ArrayList<String> descriptionList;

    /**
     * Constructor
     */
    public Location(boolean hasItem, boolean isSavePoint, boolean isGameOver){
        this.description = description;
        this.hasItem = hasItem;
        this.isSavePoint = isSavePoint;
        this.isGameOver = isGameOver;
        exits = new HashMap<String, Location>();
        if(hasItem==true) itemList = new ArrayList<Item>();
        descriptionList = new ArrayList<String>();
    }

    /**
     * Method to alter location
     * clears the locations descriptionList
     * @param isGameOver
     */
    public void setLocation(boolean isGameOver){
        this.isGameOver = isGameOver;
        clearStrings();
    }

    /**
     * Method to add an exit
     * @param direction
     * @param adjacent
     */
    public void addExit(String direction, Location adjacent){
        exits.put(direction, adjacent);
    }

    /**
     * Method to remove an exit
     * @param direction
     */
    public void removeExit(String direction){
        exits.remove(direction);
    }
    
    /**
     * Method to clear exits
     */
    public void clearExits(){
        exits.clear();
    }
    
    /**
     * Method to add Items
     * @param item
     */
    public void addItem(Item item){
        itemList.add(item);
    }

    /**
     * Method to print item list
     */
    public void printItems(){
        if(!hasItem || itemList.size()==0) System.out.println("NO ITEMS ARE IN THIS LOCATION.");
        else {
            for(Item o : itemList){
                o.print();
            }
        }
    }

    /**
     * Method to remove item
     * @param name
     */
    public void removeItem(String name){
        int i=0;
        boolean isFound=false;
        while(i<itemList.size() && !isFound){
            if(itemList.get(i).getName().equalsIgnoreCase(name)){
                itemList.remove(i);
                isFound=true;
            }
            i++;
        }
    }
    
    /**
     * Add a description string
     * @param string
     */
    public void addString(String string){
        descriptionList.add(string);
    }
    
    /**
     * Adds exitString to descriptionList
     */
    public void addExits(){
        addString(getExitList());
    }
    
    /**
     * Emptys descriptionList
     */
    public void clearStrings(){
        descriptionList.clear();    
    }
    
    /**
     * Method to return list of exits
     * @return exitString
     */
    public String getExitList(){
        String exitString = "EXITS: ";
        Set<String> directions = exits.keySet();
        for(String exit : directions){
            exitString += exit + " ";
        }
        return exitString;
    }

    /**
     * method to return location details
     * @return info
     */
    public String getLocationInfo(){
        String info =description + ".";
        if(!isGameOver) info+= "\n" + getExitList();
        return info;
    }

    /**
     * Method to return the next location
     * @param direction
     * @return exits.get(direction)
     */
    public Location getExit(String direction){
        return exits.get(direction);
    }

    /**
     * Method to return itemList
     * @return itemList
     */
    public ArrayList<Item> getItemList(){
        return itemList;
    }

    /**
     * Method to return item name
     * @param i
     */
    public Item getItem(int i){
        return itemList.get(i);
    }

    /**
     * Method to return whether or not location is save point
     * @return isSavePoint
     */
    public boolean getSave(){
        return isSavePoint;
    }

    /**
     * Method to return whether or not items exist
     * @return hasItem
     */
    public boolean hasItem(){
        return hasItem;
    }

    /**
     * Method to return if location is a game over
     * @return isGameOver
     */
    public boolean gameOver(){
        return isGameOver;
    }

    /**
     * Method to return description
     * @return description
     */
    public String getDescription(){
        return description;
    }
    
    /**
     * Does the location have an item?
     * @return hasItem
     */
    public boolean getHasItem(){
        return hasItem;
    }
    
    /**
     * returns location's description string array
     * @return descriptionList
     */
    public ArrayList<String> getDescriptionList(){
        return descriptionList;
    }
    
    /**
     * returns that the player has not won the game
     * @return false
     */
    public boolean hasWon(){
        return false;
    }
}
