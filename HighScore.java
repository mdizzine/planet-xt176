/**
 * Class to hold a high score
 * 
 * @author Matt Dizzine
 * @version 3.24.15
 */
public class HighScore{
    private String initials;
    private int score;
    /**
     * Constructor
     * @anyInitials
     * @anyScore
     */
    public HighScore(String anyInitials, int anyScore){
        initials = anyInitials;
        score = anyScore;
    }
    
    /**
     * retrieve the score
     * @return score
     */
    public int getScore(){
        return score;
    }
    
    /**
     * retrieve player's initials
     * @return initials
     */
    public String getInitials(){
        return initials;
    }
}