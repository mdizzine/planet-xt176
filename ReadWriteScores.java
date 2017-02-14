import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/**
 * Class to write/read to/from a high scores text file
 * 
 * @author Matt Dizzine
 * @version 5/10/15
 */
public class ReadWriteScores{
    private FileWriter writer;
    private FileReader reader;
    private String fileName;
    private ArrayList<HighScore> scoreList;
    private ArrayList<String> printArray;
    private final int WIDTH=53;
    /**
     * Constructor
     */
    public ReadWriteScores(){
        fileName="highscores.txt";
        scoreList = new ArrayList<HighScore>();
        printArray = new ArrayList<String>();
    }

    /**
     * Method to write to file
     */
    public void writeScores(String initials, int score){
        try{
            writer = new FileWriter(fileName, true);
            writer.write(initials + ":" + score + ";");
            writer.close();
        } catch(IOException e){
        }
    }

    /**
     * Method to read from file
     */
    public void readScores(){
        try{
            scoreList.clear();
            reader = new FileReader(fileName);
            Scanner fileIn = new Scanner(reader);
            fileIn.useDelimiter(";");
            while(fileIn.hasNext()){
                String temp = fileIn.next();
                String[] splitter = temp.split(":");
                scoreList.add(new HighScore(splitter[0], Integer.parseInt(splitter[1])));
            }        
        } catch(IOException e){
        }
    }

    /**
     * Method to sort scores from highest to lowest
     */
    public void sortScores(){
        Collections.sort(scoreList, new ScoreSorter());
    }

    /**
     * Method to populate arrayList of Strings called printArray
     * printArray stores information that will be sent to the gui
     */
    public void printHighScores(){
        if(scoreList.size()>=10){
            
            printArray.add(printBreak('='));
            String deadSpace="";
            for(int cx=0;cx<21;cx++){
                deadSpace+="_";
            }
            printArray.add("|" + deadSpace + "High_Scores" + deadSpace + "|");
            for(int cx=0;cx<20;cx++){
                deadSpace+="_";
            }
            deadSpace+="|";
            printArray.add(printBreak('='));
            for(int cx=0;cx<10;cx++){
                String score = "";
                if(cx<9) score+="|";
                score+="|" + (cx+1) + ".";
                score+=scoreList.get(cx).getInitials();
                if(scoreList.get(cx).getInitials().length()==1)score+="__";
                else if(scoreList.get(cx).getInitials().length()==2) score+="_";
                score+=deadSpace;
                if(scoreList.get(cx).getScore()<10) score+="0000";
                else if(scoreList.get(cx).getScore()<100) score+="000";
                else if(scoreList.get(cx).getScore()<1000) score+="00";
                else if(scoreList.get(cx).getScore()<10000) score+="0";
                score+=scoreList.get(cx).getScore();
                score+="|";
                printArray.add(score);
            }
            printArray.add(printBreak('='));
            printArray.add(printBreak('='));
        } else {
            setDefaults();
            readScores();
            sortScores();
            printHighScores();
        }
    }

    /**
     * Method to remove unused scores
     */
    public void cleanScores(){
        if(scoreList.size()>10){
            try{
                writer = new FileWriter(fileName);
                int cx=0;
                while(cx<10){
                    writer.write(scoreList.get(cx).getInitials() + ":" + scoreList.get(cx).getScore() + ";");
                    cx++;
                }
                writer.close();
            } catch(IOException e){

            }
        }
    }

    /**
     * Starter method
     */
    public void starter(String initials, int score){
        writeScores(initials, score);
        readScores();
        sortScores();
    }

    /**
     * Retrieve horizontal rule string
     * @return line
     */
    public String printBreak(char anyChar){
        String line="|";
        for(int cx=0;cx<WIDTH;cx++){
            line+=anyChar;
        }
        line+="|";
        return line;
    }
    
    /**
     * Method to set default scores
     */
    public void setDefaults(){
        writeScores("MRD", 50000);
        writeScores("ALC", 4500);
        writeScores("AB", 4000);
        writeScores("JMB", 3000);
        writeScores("LOL", 2500);
        writeScores("SRD", 1750);
        writeScores("NYR", 1500);
        writeScores("ZZZ", 1000);
        writeScores("DLR", 500);
        writeScores("XYZ", 25);
    }
    
    /**
     * Method to return lowest score
     * @return scoreList.get(9).getScore();
     */
    public int getLowScore(){
        return scoreList.get(9).getScore();
    }
    
    /**
     * Method to return printArray
     * @return printArray
     */
    public ArrayList<String> getPrintArray(){
        return printArray;
    }
}