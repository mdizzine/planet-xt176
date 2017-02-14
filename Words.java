/**
 * Class of recognizable words
 * Based on World of Zuul
 * Developed Barne/Kolling
 * 
 * @author Matt Dizzine
 * @version 03.19.15
 */
public class Words{
        private String[] wordArray = {"go", "help", "inventory", "quit", "use", "take", "drop", "look", "search", "eat",
        "score"};
        private String[] exitArray = {"north", "south", "west", "east", "northwest", "northeast", "southwest", "southeast",
            "swim", "follow", "up", "down"};
    /**
     * Constructor
     */
        public Words(){
    }
    
    /**
     * Is the commmand recognized by system?
     * @param input
     * @return isFound
     */
    public boolean isUnderstood(String input){
        boolean isFound = false;
        int c=0;
        while(!isFound && c<wordArray.length){
            if(wordArray[c].equalsIgnoreCase(input))isFound=true;
            c++;
        }
    
        return isFound;
    }
    
    /**
     * Is the command word a direction
     * @param input
     * @return isDirection
     */
    public boolean isDirection(String input){
        boolean isDirection = false;
        int c=0;
        while(!isDirection && c<exitArray.length){
            if(exitArray[c].equalsIgnoreCase(input))isDirection=true;
            c++;
        }
        return isDirection;
    }
}