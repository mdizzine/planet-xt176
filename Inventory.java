import java.util.ArrayList;
/**
 * Class that creates an inventory for a player
 * 
 * @author Matt Dizzine
 * @version 5.10.15
 */
public class Inventory{
    private ArrayList<Item> inventoryList;
    /**
     * Constructor
     */
    public Inventory(){
        inventoryList = new ArrayList<Item>();
        setInitial();
    }

    /**
     * Method to set starting inventory
     */
    public void setInitial(){
        inventoryList.add(new Item("GUN", "LASER BLASTER"));
        inventoryList.add(new Item("TRANSLATOR", "TRANSLATES UP TO 900 ALIEN LANGUAGES"));
    }

    /**
     * Retrieves inventoryList to send to GUI
     * @return inventoryList;
     */
    public ArrayList<Item> print(){
        return inventoryList;
    }

    /**
     * Method to add an item to list
     * @param item
     * @return String
     */
    public String addItem(Item item){
        inventoryList.add(item);
        return "YOU HAVE ADDED " + item.getName() + " TO YOUR INVENTORY.";
    }

    /**
     * Method to drop an item from the list
     * @param name
     * @param use 
     */
    public void removeItem(String name){
        int i=0;
        boolean isFound=false;
        while(i<inventoryList.size() && !isFound){
            if(inventoryList.get(i).getName().equalsIgnoreCase(name)){
                inventoryList.remove(i);
                isFound=true;
            }
            i++;
        }
        if(!isFound) System.out.println("ITEM IS NOT IN YOUR INVENTORY.");
    }

    /**
     * Method to return size
     * @return inventoryList.size()
     */
    public int getSize(){
        return inventoryList.size();
    }

    /**
     * Method to return Item by index
     * @param i index
     * @return inventoryList.get(i)
     */
    public Item getItem(int i){
        return inventoryList.get(i);
    }
}