import java.util.Scanner;
/**
 * Class to process user input
 * Based on World of Zuul
 * Developed by Barnes/Kolling
 * 
 * @author Matt Dizzine
 * @version 03.19.15
 */
public class Parser{
    private Scanner keybd;
    private Words words;
    /**
     * Constructor
     */
    public Parser(){
        keybd = new Scanner(System.in);
        words = new Words();
    }

    /**
     * Parses user input
     * @param string
     * @return new Input
     */
    public Input getInput(String string){
        String a = null;
        String b = null;
        String input = string.toUpperCase();
        Scanner lineIn = new Scanner(input);
        if(lineIn.hasNext()) a=lineIn.next();
        if(lineIn.hasNext()) b=lineIn.next();
        if(words.isDirection(a)){
            return new Input("GO", a);
        }
        if(words.isUnderstood(a)){
            return new Input(a, b);
        }
        else {
            return new Input(null, b);
        }

    }
}