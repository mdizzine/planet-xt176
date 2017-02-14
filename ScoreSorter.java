import java.util.Comparator;
/**
 * Class to sort scores
 * 
 * @author Matt Dizzine
 * @version 3.24.15
 */
public class ScoreSorter implements Comparator<HighScore>{
    /**
     * method that compares HighScore score fields
     * @return returnVal
     */
    public int compare(HighScore o1, HighScore o2){
        int returnVal = 0;
        if(o1.getScore()<o2.getScore()) returnVal=1;
        else if(o1.getScore()==o2.getScore()) returnVal=0;
        else if(o1.getScore()>o2.getScore()) returnVal=-1;
        return returnVal;
     }
}