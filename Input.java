/**
 * Class that takes in user input
 * Based on World of Zuul
 * Developed by Barnes/Kolling
 * 
 * @author Matt Dizzine
 * @version 03.19.15
 */
public class Input{
    private String inputa;
    private String inputb;
    /**
     * Constructor
     * @param word1
     * @param word2
     */
    public Input(String word1, String word2){
        inputa = word1;
        inputb = word2;
    }
    
    /**
     * retrieves inputa
     * @return inputa
     */
    public String getInputA(){
        return inputa;
    }
    
    /**
     * retrieves inputb
     * @return inputb
     */
    public String getInputB(){
        return inputb;
    }
    
    /**
     * returns whether inputa is null
     * @return (inputa==null)
     */
    public boolean isUnknown(){
        return (inputa == null);
    }
    
    /**
     * returns wheter or not there is an inputb
     * @return (inputb!=null)
     */
    public boolean hasSecond(){
        return (inputb != null);
    }
}