/**
 * Class of an inventory item
 * 
 * @author Matt Dizzine
 * @version 05.09.15
 */
public class Item{
    private String name;
    private String description;
    /**
     * Constructor
     * @param anyName
     * @param anyDescription
     */
    public Item(String anyName, String anyDescription){
        name = anyName;
        description = anyDescription;
    }
    
    /**
     * Method to return a string of item details
     * @return print
     */
    public String print(){
        String print = name + " - " + description;
        return print;
    }
    
    /**
     * Get name
     * @return name
     */
    public String getName(){
        return name;
    }
    
    /**
     * Get Description
     * @return description
     */
    public String getDescription(){
        return description;
    }
    
    /**
     * Overrides toString method
     * @return obj
     */
    public String toString(){
        String obj = name + " - " + description;
        return obj;
    }
}