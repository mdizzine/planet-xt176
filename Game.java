import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
/**
 * Main game class
 * 
 * @author Matt Dizzine
 * @version 05.10.15
 */
public class Game{
    //////////
    //FIELDS//
    //////////
    private Window window;
    private Player player;
    private Parser parser;
    private Location currentLocation;
    private SavePoint lastSave;
    private boolean hasSaved;
    private boolean gameOver;
    private int continues;
    private String initials;
    private ReadWriteScores highScore;
    private String ventString = "YOU ARE IN THE VENTILATION SHAFT.\nDOWN LEAVES THE VENTS AND ENTERS THE BUILDING";
    private boolean finished;
    /////////
    //AREAS//
    /////////
    private Location ship, lake, beast, inLake, westLake, eastLake, lakeWestSide, lakeEastSide, 
    lakeSouthWestSide, lakeSouthEastSide, lakeSouth, mud, westSwamp, darkSwamp, deadEnd, desert, 
    gromer, lost, buildingFront, eastSwamp, northEastSwamp, farEastSwamp, pit, southEastSwamp,
    outerBees, bees, mistySwamp, forest, redForestNorth, redForestSouth, redForestWest, forestHub,
    greenForestNorth, greenForestWest, greenForestSouth, yellowForestNorth, yellowForestSouth, 
    yellowForestEast, buildingEast, buildingSouthEast, buildingInterior, ventEast, ventSouthEast,
    ventSouthA, ventSouthB, ventSouthWest, ventWest, ventNorthWest, ventNorth, cliff,
    buildingNorthWest, midWestSwamp, southWestSwamp, southSwamp;
    
    private WinningLocation dockingBay;
    /**
     * Constructor
     */
    public Game(){
        window = new Window();
        hasSaved=false;
        continues = 3;
        gameOver=false;
        lastSave = new SavePoint();
        parser = new Parser();
        highScore = new ReadWriteScores();
        finished = false;
    }

    /**
     * main method
     * @param args
     */
    public static void main(String[] args){
        Game o = new Game();
        o.start();
    }

    /**
     * Main game routine
     */
    public void start(){
        intro();
        player = new Player(enterName());
        introb();
        setLocations();
        printLocation();
        while(!finished){
            while(!window.isReady()){
                wait(500);
            }
            Input input = parser.getInput(window.getPrintout());
            finished = processInput(input);
            window.setIsReady();
        }
        if(gameOver==false) {
            window.printLine("YOU HAVE ENDED THE GAME...");
        }
        boolean pass=false;
        boolean catchPass=false;
        while(!catchPass){
            highScore.readScores();
            highScore.sortScores();

            try{
                if(player.getScore()>highScore.getLowScore()){
                    while(!pass){
                        window.printLine("ENTER YOUR INITIALS:");
                        while(!window.isReady()){
                            wait(500);
                        }
                        initials = window.getPrintout();
                        if(initials.length()>3){
                            initials=initials.substring(0,3); 
                            pass=true;
                        } else if(initials.length()>0){
                            pass=true;
                        }
                    }
                    highScore.writeScores(initials, player.getScore());
                    highScore.readScores();
                    highScore.sortScores();
                } else {
                    
                    window.printLine("YOUR FINAL SCORE WAS " + player.getScore() + ".");
                    window.printLine("YOU DID NOT ACHIEVE A HIGH SCORE.");
                    window.printLine("PRESS ENTER TO CONTINUE.");
                    while(!window.isReady()){
               wait(500);
            }
                    parser.getInput("");
                }
                catchPass=true;
            } catch(IndexOutOfBoundsException iobE){
                highScore.setDefaults();
            }
        }
        highScore.printHighScores();
        ArrayList<String> printList = highScore.getPrintArray();
        for(String str : printList){
            window.printLine(str);
        }
        highScore.cleanScores();
    }

    /**
     * Method to display introductory screen
     */
    public void intro(){
        String stars1 ="*";
        String stars2 ="*    ";
        String title ="";
        for(int cx=0;cx<9;cx++){
            String next = "*   *";
            stars1+=next;
            stars2+=next;
            if(cx==0) stars1+=next;
        }
        for(int cx=0;cx<3;cx++){
            for(int c=0;c<2;c++){
                String next= "*    *";
                title+=next;
            }
            if(cx==1)title+="PLANET XT17-6  ";
        }
        for(int cx=0;cx<2;cx++){
            for(int c=0;c<4;c++){
                window.printLine(stars1);
                window.printLine(stars2);
            }
            if(cx==0) {
                window.printLine(title);
                window.printLine(stars2);
            }
        }
    }

    /**
     * Method to set player name
     */
    public String enterName(){
        String name = "";
        window.printLine("ENTER A NAME FOR YOUR STAR PILOT: ");
        while(!window.isReady()){
           wait(500);
        }
        name = window.getPrintout().toUpperCase();
        window.setIsReady();
        return name;
    }

    /**
     * Method to display introductory information
     */
    public void introb(){
        window.printLine(player.getName() + " YOU ARE A MILITARY PILOT ON A SPACE MISSION.");
        window.printLine("YOU HAVE LOST CONTROL OF YOUR SHIP.");
        window.printLine("YOU SPOT A PLANET NEARBY AND MUST MAKE A ");
        window.printLine("CRASH LANDING.");
        window.printLine("YOU HAVE SUCCESFULLY CRASH LANDED YOUR SHIP ");
        window.printLine("ON THE PLANET.");
        window.printLine("ACCORDING TO YOUR CHARTS YOU ARE ON ");
        window.printLine("AN UNEXPLORED PLANET.");
        window.printLine("AN ASTRONOMER HAS NAMED IT PLANET XT17-6.");
        window.printLine("YOU DON YOUR SPACESUIT AND EXIT YOUR SHIP " + player.getName() + ".");
        window.printLine("TYPE HELP FOR GAMEPLAY INFORMATION.");
    }
    
    /**
     * Print current location information
     */
    public void printLocation(){
        for(String str : currentLocation.getDescriptionList()){
            
            window.printLine(str);
        }
    }

    /**
     * method to process player input
     * @param input
     */
    public boolean processInput(Input input){
        boolean quit=false;
        if(input.isUnknown()){ 
            window.printLine("INVALID INPUT...");
            return false;
        }
        String inputA = input.getInputA();
        if(inputA.equalsIgnoreCase("go")) go(input);
        else if(inputA.equals("EAT")) eat(input);
        else if(inputA.equalsIgnoreCase("quit")) quit=true;
        else if(inputA.equalsIgnoreCase("inventory")) inventory();
        else if(inputA.equalsIgnoreCase("use")) use(input);
        else if(inputA.equalsIgnoreCase("look")) printLocation();
        else if(inputA.equalsIgnoreCase("drop")) drop(input);
        else if(inputA.equalsIgnoreCase("take")) take(input);
        else if(inputA.equalsIgnoreCase("search")) search();
        else if(inputA.equalsIgnoreCase("score")) score();
        else if(inputA.equalsIgnoreCase("help")) help();
        if(gameOver==true) quit=true;
        return quit;
    }

    /**
     * Method to create locations in the game 
     * Sets the start location to ship
     */
    public void setLocations(){
        //assign information to location variables
        ship = new Location(true, false, false);
        ship.addString("YOU ARE AT YOUR CRASHED SHIP " + player.getName() + ".");
        lake = new Location(false, true, false);
        lake.addString("YOU ARE AT A LAKE.");
        lake.addString("YOU CAN WALK THE PERIMETER OF THE LAKE BY ");
        lake.addString("GOING SOUTHEAST OR SOUTHWEST.");
        
        beast = new Location(false, false, true);
        beast.addString("YOU ARE IN A SWAMP " + player.getName() + ".");
        beast.addString("THERE IS A LARGE ALIEN CREATURE IN FRONT OF YOU.");
        beast.addString("THE CREATURE JUMPS OUT AND EATS YOU."); 
        
        inLake = new Location(false, false, true);
        inLake.addString("YOU BEGIN TO SWIM IN THE LAKE " + player.getName() + ".");
        inLake.addString("YOU REALIZE YOU AREN'T IN WATER WHEN THE LIQUID EATS THROUGH YOUR SPACE SUIT."); 
        
        westLake = new Location(false, false, false);
        westLake.addString("YOU ARE IN A SWAMP " + player.getName() + ".");
        
        eastLake = new Location(false, false, false);
        eastLake.addString("YOU ARE IN A SWAMP " + player.getName() + ".");
        eastLake.addString("THE TRAIL FORKS NORTHEAST AND SOUTHEAST.");
        
        lakeEastSide = new Location(false, false, false);
        lakeEastSide.addString("YOU ARE HALFWAY AROUND THE LAKE.");
        lakeEastSide.addString("THERE IS A TRAIL LEADING AWAY FROM THE LAKE TO THE EAST.");
        lakeEastSide.addString("YOU CAN CONTINUE ALONG THE PERIMETER BY GOING SOUTH.");
        
        lakeWestSide = new Location(false, false, false);
        lakeWestSide.addString("YOU ARE HALFWAY AROUND THE LAKE.");
        lakeWestSide.addString("THERE IS A TRAIL LEADING AWAY FROM THE LAKE THAT FORKS NORTHWEST AND SOUTHWEST.");
        lakeWestSide.addString("YOU CAN CONTINUE ALONG THE PERIMETER BY GOING SOUTH.");
        
        lakeSouthWestSide = new Location(false, false, false);
        lakeSouthWestSide.addString("YOU ARE ALMOST TO THE SOUTH END OF THE LAKE.");
        lakeSouthWestSide.addString("YOU CAN CONTINUE BY GOING SOUTH OR YOU CAN TAKE A TRAIL TO THE WEST.");
        
        lakeSouthEastSide = new Location(false, false, false);
        lakeSouthEastSide.addString("YOU ARE ALMOST TO THE SOUTH END OF THE LAKE.");
        lakeSouthEastSide.addString("YOU CAN CONTINUE BY GOING SOUTH OR YOU CAN TAKE A TRAIL TO THE EAST."); 
        
        lakeSouth = new Location(false, false, false);
        lakeSouth.addString("YOU HAVE REACHED THE SOUTH END OF THE LAKE.");
        lakeSouth.addString("THERE ARE TRAILS LEADING AWAY FROM THE LAKE TO THE SOUTH AND SOUTHWEST.");
        
        mud = new Location(false, false, true);
        mud.addString("YOU WALK FURTHER INTO THE SWAMP.");
        mud.addString("THE GROUND BECOMES MUDDY AND DIFFICULT TO WALK THROUGH.");
        mud.addString("YOU REALIZE YOU ARE IN QUICKSAND."); 
        
        westSwamp = new Location(false, false, false);
        westSwamp.addString("YOU ARE IN A SWAMP.");
        
        darkSwamp = new Location(false, false, false);
        darkSwamp.addString("YOU CONTINUE THROUGH THE SWAMP " + player.getName() + ".");
        darkSwamp.addString("THE TRAIL IS BEGINING TO GET DARK AND IT IS DIFFICULT FOR YOU TO SEE.");
        
        deadEnd = new Location(true, false, true);
        deadEnd.addString("THE TRAIL CONTINUES TO GET DARKER AND YOU LOSE YOUR WAY.");
        deadEnd.addString("AFTER DAYS OF TRYING TO RETRACE YOUR STEPS YOU STARVE TO DEATH.");
        
        desert = new Location(false, false, true);
        desert.addString("THE SWAMP OPENS UP AND YOU ENTER A DESERT " + player.getName() + ".");
        desert.addString("THE HEAT IS UNBEARABLE.");
        desert.addString("YOU FALL TO YOUR KNEES AND DIE OF DEHYDRATION.");
        
        gromer = new Location(false, true, false);
        gromer.addString("YOU ENCOUNTER A STRANGE CREATURE THAT IS SPEAKING A STRANGE LANGUAGE.");
        
        lost = new Location(false, false, true);
        lost.addString("YOU CONTINUE DOWN THE TRAIL.");
        lost.addString("AFTER SOME TIME YOU BECOME LOST.");
        lost.addString("FOR SEVERAL DAYS YOU TRY TO RETRACE YOUR STEPS BUT YOU CANNOT FIND YOUR WAY BACK.");
        lost.addString("YOU STARVE TO DEATH.");
        
        buildingFront = new Location(false, true, false);
        buildingFront.addString("YOU HAVE REACHED A BUILDING " + player.getName() + ".");
        buildingFront.addString("THE ENTRANCE IS LOCKED.");
        
        northEastSwamp = new Location(false, false, false);
        northEastSwamp.addString("YOU ARE STILL IN A SWAMP " + player.getName() + ".");
        
        eastSwamp = new Location(false, false, false);
        eastSwamp.addString("YOU ARE IN A SWAMP TO THE EAST OF THE LAKE."); 
        
        farEastSwamp = new Location(false, false, false);
        farEastSwamp.addString("YOU HAVE TRAVELED FARTHER AWAY FROM THE LAKE " + player.getName() + ".");
        
        pit = new Location(false, false, true);
        pit.addString("YOU TRIP OVER A ROCK AND FALL INTO A PIT " + player.getName() + ".");
        pit.addString("AFTER YOU FALL FOR WHAT SEEMS LIKE FOREVER YOU HIT THE BOTTOM OF THE PIT.");
        pit.addString("YOU DIE FROM THE IMPACT."); 
        
        southEastSwamp = new Location(false, false, false);
        southEastSwamp.addString("YOU ARE STILL IN THE SWAMP " + player.getName() + ".");
        southEastSwamp.addString("THE VEGETATION HAS BECOME MUCH MORE DENSE."); 
        
        outerBees = new Location(false, false, false);
        outerBees.addString("YOU ARE STILL IN THE SWAMP " + player.getName() + ".");
        outerBees.addString("THERE IS A LOUD BUZZING SOUND COMING FROM SOMEWHERE NEAR."); 
        
        bees = new Location(false, false, true);
        bees.addString("THE BUZZING BEGINS TO GET LOUDER AS YOU WALK THE TRAIL " + player.getName() + ".");
        bees.addString("A SWARM OF BEES ATTACKS YOU.");
        bees.addString("YOU ARE STUNG TO DEATH.");
        
        mistySwamp = new Location(true, false, false);
        mistySwamp.addString("YOU ARE IN THE SWAMP.");
        mistySwamp.addString("A STRANGE MIST IS IN THE AIR."); 
        
        forest = new Location(false, true, false);
        forest.addString("YOU ARE IN A FOREST.");
        
        redForestNorth = new Location(false, false, false);
        redForestNorth.addString("YOU ARE IN THE NORTH OF THE RED FOREST " + player.getName() + ".");
        
        redForestSouth = new Location(false, false, false);
        redForestSouth.addString("YOU ARE IN THE SOUTH RED FOREST " + player.getName() + "."); 
        
        redForestWest = new Location(false, false, false);
        redForestWest.addString("YOU ARE IN THE WEST RED FOREST " + player.getName() + ".");
        
        forestHub = new Location(true, false, false);
        forestHub.addString("THE LEAVES OF THE TREES ARE MANY DIFFERENT COLORS.");
        
        greenForestNorth = new Location(false, false, false);
        greenForestNorth.addString("YOU ARE IN THE NORTH GREEN FOREST.");
        
        greenForestWest = new Location(false, false, false);
        greenForestWest.addString("YOU ARE IN THE WEST GREEN FOREST.");
        
        greenForestSouth = new Location(false, false, false);
        greenForestSouth.addString("YOU ARE IN THE SOUTH GREEN FOREST.");
        
        yellowForestNorth = new Location(false, false, false);
        yellowForestNorth.addString("YOU ARE IN THE NORTH YELLOW FOREST " + player.getName() + ".");
        yellowForestNorth.addString("THERE IS A BUILDING TO THE WEST.");
        
        yellowForestSouth = new Location(false, false, false);
        yellowForestSouth.addString("YOU ARE IN THE SOUTH YELLOW FOREST " + player.getName() + ".");
        yellowForestSouth.addString("THERE IS A BUILDING TO THE WEST.");
        
        yellowForestEast = new Location(false, false, false);
        yellowForestEast.addString("YOU ARE IN THE EAST YELLOW FOREST."); 
        
        buildingEast = new Location(false, true, false);
        buildingEast.addString("YOU ARE AT A BUILDING " + player.getName() + ".");
        buildingEast.addString("THE ENTRANCE IS LOCKED.");
        
        buildingSouthEast = new Location(false, false, false);
        buildingSouthEast.addString("YOU ARE AT THE SOUTHEAST CORNER OF THE BUILDING " + player.getName() + ".");
        buildingSouthEast.addString("THE ENTRANCE IS LOCKED.");
        
        buildingInterior = new Location(false, false, true);
        buildingInterior.addString("YOU ENTER THE BUILDING " + player.getName() + ".");
        buildingInterior.addString("A GROUP OF ROBOTS CONVERGES ON YOU.");
        buildingInterior.addString("THEY RIP YOUR LIMBS OFF.");
        
        ventEast = new Location(false, false, false);
        ventEast.addString(ventString);
        
        ventSouthEast = new Location(false, false, false);
        ventSouthEast.addString(ventString);
        
        ventSouthA = new Location(false, false, false);
        ventSouthA.addString(ventString);
        
        ventSouthB = new Location(false, false, false);
        ventSouthB.addString(ventString);
        
        ventSouthWest = new Location(false, false, false);
        ventSouthWest.addString(ventString);
        
        ventWest = new Location(false, false, false);
        ventWest.addString(ventString);
        
        ventNorthWest = new Location(false, false, false);
        ventNorthWest.addString(ventString);
        
        ventNorth = new Location(false, false, false);
        ventNorth.addString(ventString);
        
        dockingBay = new WinningLocation(false, false, false);
        dockingBay.addString("YOU HAVE REACHED THE BUILDING'S SHIP DOCKING BAY " + player.getName() + ".");
        dockingBay.addString("YOU BOARD A SHIP.");
        dockingBay.addString("YOU ENTER THE COORDINATES FOR EARTH.");
        dockingBay.addString("YOU BEGIN TAKEOFF AND LEAVE THE PLANET'S ATMOSPHERE.");
        dockingBay.addString(dockingBay.getWin()+ player.getName() + ".");
        
        cliff = new Location(false, false, true);
        cliff.addString("");
        
        buildingNorthWest = new Location(false, false, false);
        buildingNorthWest.addString("");
        
        midWestSwamp = new Location(false, false, false);
        midWestSwamp.addString("YOU ARE IN A SWAMP TO THE WEST OF THE LAKE.");
        
        southWestSwamp = new Location(false, false, false);
        southWestSwamp.addString("YOU ARE TO THE SOUTHWEST OF THE LAKE.");
        
        southSwamp = new Location(false, false, false);
        southSwamp.addString("");
        //*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
        //////////////////////////////////////////////////////////////////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
        /// _      _____  ____  _____  _____  _  _____  __    _   ____  ////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
        // | |    |  _  ||  __||  _  ||_   _|| ||  _  ||   \ | | |  __| ////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
        // | |___ | |_| || |__ | |_| |  | |  | || |_| || |\ \| | _\  \  ////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
        // |_____||_____||____||_| |_|  |_|  |_||_____||_| \___||_____| ////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
        //                                                              ////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*        
        //////////////////////////////////////////////////////////////////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
        //*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*       
        //ship
        ship.addExit("SOUTH", lake);
        ship.addExit("WEST", beast);
        ship.addItem(new Item("FOOD", "EAT ME"));
        ship.addItem(new Item("RAFT", "TRAVERSES BODIES OF WATER"));
        ship.addItem(new Item("FLASHLIGHT", "MAKES DARK AREAS VISIBLE"));
        //beast
        beast.addExit("EAST", ship);
        beast.addExit("SOUTH", westLake);
        //gromer
        gromer.addExit("NORTH", lakeSouth);
        gromer.addExit("EAST", lost);
        //LAKE/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*            
        //lake
        lake.addExit("SWIM", inLake);
        lake.addExit("NORTH", ship);
        lake.addExit("WEST", westLake);
        lake.addExit("EAST", eastLake);
        lake.addExit("SOUTHEAST", lakeEastSide);
        lake.addExit("SOUTHWEST", lakeWestSide);
        //lakeEastSide
        lakeEastSide.addExit("NORTH", lake);
        lakeEastSide.addExit("SOUTH", lakeSouthEastSide);
        lakeEastSide.addExit("SWIM", inLake);
        lakeEastSide.addExit("EAST", eastSwamp);
        //lakeWestSide
        lakeWestSide.addExit("NORTH", lake);
        lakeWestSide.addExit("SOUTH", lakeSouthWestSide);
        lakeWestSide.addExit("SWIM", inLake);
        lakeWestSide.addExit("NORTHWEST", westSwamp);
        lakeWestSide.addExit("SOUTHWEST", midWestSwamp);
        //lakeSouthEastSide
        lakeSouthEastSide.addExit("SOUTH", lakeSouth);
        lakeSouthEastSide.addExit("NORTH", lakeEastSide);
        lakeSouthEastSide.addExit("EAST", southEastSwamp);
        lakeSouthEastSide.addExit("SWIM", inLake);
        //lakeSouthWestSide
        lakeSouthWestSide.addExit("SOUTH", lakeSouth);
        lakeSouthWestSide.addExit("NORTH", lakeWestSide);
        lakeSouthWestSide.addExit("SWIM", inLake);
        lakeSouthWestSide.addExit("WEST", southWestSwamp);
        //lakeSouth
        lakeSouth.addExit("SWIM", inLake);
        lakeSouth.addExit("NORTHWEST", lakeSouthWestSide);
        lakeSouth.addExit("NORTHEAST", lakeSouthEastSide);
        lakeSouth.addExit("SOUTH",  gromer);
        lakeSouth.addExit("SOUTHWEST", southSwamp);
        //WEST SWAMPS/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
        //westLake
        westLake.addExit("NORTH", beast);
        westLake.addExit("EAST", lake);
        westLake.addExit("WEST", mud);
        westLake.addExit("SOUTH", westSwamp);        
        //westSwamp
        westSwamp.addExit("NORTH", westLake);
        westSwamp.addExit("SOUTHEAST", lakeWestSide);
        westSwamp.addExit("NORTHWEST", mud);
        //midWestSwamp
        midWestSwamp.addExit("NORTHEAST", lakeWestSide);
        //southWestSwamp
        southWestSwamp.addExit("EAST", lakeSouthWestSide);
        //southSwamp
        southSwamp.addExit("NORTHEAST", lakeSouth);
        //EAST SWAMPS/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*         
        //eastLake
        eastLake.addExit("WEST", lake);
        eastLake.addExit("NORTHEAST", darkSwamp);
        eastLake.addExit("SOUTHEAST", northEastSwamp);
        //darkSwamp
        darkSwamp.addExit("SOUTHWEST", eastLake);
        darkSwamp.addExit("NORTHEAST", deadEnd);
        darkSwamp.addExit("SOUTHEAST", desert);
        //deadEnd
        deadEnd.addExit("SOUTHWEST", darkSwamp);
        deadEnd.addItem(new Item("SWORD", "USEFUL IN CLOSE RANGE COMBAT"));
        //northEastSwamp
        northEastSwamp.addExit("NORTHWEST", eastLake);
        northEastSwamp.addExit("SOUTH", eastSwamp);
        //eastSwamp
        eastSwamp.addExit("NORTH", northEastSwamp);
        eastSwamp.addExit("WEST", lakeEastSide);
        eastSwamp.addExit("EAST", farEastSwamp);
        eastSwamp.addExit("SOUTHEAST", pit);
        eastSwamp.addExit("SOUTH", southEastSwamp);
        //farEastSwamp
        farEastSwamp.addExit("WEST", eastSwamp);
        farEastSwamp.addExit("NORTHEAST", desert);
        farEastSwamp.addExit("SOUTH", pit);
        //southEastSwamp
        southEastSwamp.addExit("SOUTH", outerBees);
        southEastSwamp.addExit("WEST", lakeSouthEastSide); 
        southEastSwamp.addExit("NORTH", eastSwamp);
        southEastSwamp.addExit("EAST", pit);
        //outerBees
        outerBees.addExit("SOUTH", bees);
        outerBees.addExit("EAST", mistySwamp);
        outerBees.addExit("NORTH", southEastSwamp);
        outerBees.addExit("NORTHEAST", pit);
        outerBees.addExit("SOUTHWEST", lost);
        //mistySwamp
        mistySwamp.addExit("SOUTHEAST", forest);
        mistySwamp.addExit("NORTH", pit);
        mistySwamp.addExit("WEST", outerBees);
        mistySwamp.addItem(new Item("KEYS", "OPEN DOORS"));
        //FOREST/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*        
        //forest
        forest.addExit("NORTHWEST", mistySwamp);
        forest.addExit("SOUTH", redForestNorth);
        //redForestNorth
        redForestNorth.addExit("SOUTH", redForestSouth);
        redForestNorth.addExit("SOUTHEAST", lost);
        redForestNorth.addExit("SOUTHWEST", redForestWest);
        redForestNorth.addExit("NORTH", forest);
        //redForestWest
        redForestWest.addExit("NORTHEAST", redForestNorth);
        redForestWest.addExit("EAST", lost);
        redForestWest.addExit("SOUTHEAST", redForestSouth);
        redForestWest.addExit("SOUTHWEST", forestHub);
        //redForestSouth
        redForestSouth.addExit("NORTH", redForestNorth);
        redForestSouth.addExit("NORTHEAST", lost);
        redForestSouth.addExit("NORTHWEST", redForestWest);
        //forestHub
        forestHub.addExit("NORTHEAST", redForestWest);
        forestHub.addExit("NORTHWEST", lost);
        forestHub.addExit("SOUTHEAST", greenForestNorth);
        forestHub.addExit("SOUTHWEST", yellowForestNorth);
        forestHub.addItem(new Item("MUSHROOMS", "I CAN GIVE YOU HINTS"));
        //greenForestNorth
        greenForestNorth.addExit("SOUTHEAST", lost);
        greenForestNorth.addExit("SOUTHWEST", greenForestWest);
        greenForestNorth.addExit("SOUTH", greenForestSouth);
        greenForestNorth.addExit("NORTHWEST", forestHub);
        //greenForestSouth
        greenForestSouth.addExit("NORTH", greenForestNorth);
        greenForestSouth.addExit("NORTHEAST", lost);
        greenForestSouth.addExit("NORTHWEST", greenForestWest);
        //greenForestWest
        greenForestWest.addExit("NORTHEAST", greenForestNorth);
        greenForestWest.addExit("EAST", lost);
        greenForestWest.addExit("SOUTHEAST", greenForestSouth);
        //yellowForestNorth
        yellowForestNorth.addExit("NORTHEAST", forestHub);
        yellowForestNorth.addExit("SOUTHEAST", yellowForestEast);
        yellowForestNorth.addExit("SOUTH", yellowForestSouth);
        yellowForestNorth.addExit("WEST", buildingEast);
        //yellowForestEast
        yellowForestEast.addExit("NORTHWEST", yellowForestNorth);
        yellowForestEast.addExit("SOUTHWEST", yellowForestSouth);
        //yellowForestSouth
        yellowForestSouth.addExit("NORTH", yellowForestNorth);
        yellowForestSouth.addExit("NORTHEAST", yellowForestEast);
        yellowForestSouth.addExit("WEST", buildingSouthEast);
        //BUILDING/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*  
        //buildingFront
        buildingFront.addExit("NORTH", lost);
        buildingFront.addExit("WEST", buildingNorthWest);
        //buildingEast
        buildingEast.addExit("EAST", yellowForestEast);
        buildingEast.addExit("SOUTH", buildingSouthEast);
        //buildingSouthEast
        buildingSouthEast.addExit("NORTH", buildingEast);
        buildingSouthEast.addExit("EAST", yellowForestSouth);
        //buildingNorthWest
        buildingNorthWest.addExit("EAST", buildingFront);
        //VENTILATION SHAFTS/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
        //ventEast
        ventEast.addExit("DOWN", buildingInterior);
        ventEast.addExit("EAST", buildingEast);
        ventEast.addExit("SOUTH", ventSouthEast);
        //ventSoutheast
        ventSouthEast.addExit("DOWN", buildingInterior);
        ventSouthEast.addExit("NORTH", ventEast);
        ventSouthEast.addExit("EAST", buildingSouthEast);
        ventSouthEast.addExit("WEST", ventSouthA);
        //ventSouthA
        ventSouthA.addExit("DOWN", buildingInterior);
        ventSouthA.addExit("SOUTH", ventSouthB);
        ventSouthA.addExit("WEST", ventSouthWest);
        ventSouthA.addExit("EAST", ventSouthEast);
        //ventSouthB
        ventSouthB.addExit("NORTH", ventSouthA);
        ventSouthB.addExit("DOWN", dockingBay);
        //ventSouthWest
        ventSouthWest.addExit("DOWN", buildingInterior);
        ventSouthWest.addExit("EAST", ventSouthA);
        ventSouthWest.addExit("NORTH", ventWest);
        //ventWest
        ventWest.addExit("DOWN", buildingInterior);
        ventWest.addExit("NORTH", ventNorthWest);
        ventWest.addExit("SOUTH", ventSouthWest);
        ventWest.addExit("WEST", cliff);
        //ventNorthWest
        ventNorthWest.addExit("DOWN", buildingInterior);
        ventNorthWest.addExit("EAST", ventNorth);
        ventNorthWest.addExit("SOUTH", ventWest);
        ventNorthWest.addExit("SOUTHWEST", cliff);
        ventNorthWest.addExit("NORTH", buildingNorthWest);
        //ventNorth
        ventNorth.addExit("DOWN", buildingInterior);
        ventNorth.addExit("NORTH", buildingFront);
        ventNorth.addExit("WEST", ventNorthWest);
        ////////////////////////////////////////////////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
        ///*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*        
        ///*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* 
        ///___    _    _    _   ________   /////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
        //|  _|  | |  | |  | | |___  ___|  /////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*      
        //| |_    \ \/ /   | |     | |     /////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
        //| |_    / / \ \  | |     | |     /////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
        //|___|  |_|   |_| |_|     |_|     /////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
        ///*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/* 
        ship.addExits();
        lake.addExits();
        westLake.addExits();
        eastLake.addExits();
        lakeEastSide.addExits();
        lakeWestSide.addExits();
        lakeSouthWestSide.addExits();
        lakeSouthEastSide.addExits();
        lakeSouth.addExits();
        westSwamp.addExits();
        darkSwamp.addExits();
        gromer.addExits();
        buildingFront.addExits();
        northEastSwamp.addExits();
        eastSwamp.addExits();
        farEastSwamp.addExits();
        southEastSwamp.addExits();
        outerBees.addExits();
        mistySwamp.addExits();
        forest.addExits();
        redForestNorth.addExits();
        redForestSouth.addExits();
        redForestWest.addExits();
        forestHub.addExits();
        greenForestNorth.addExits();
        greenForestWest.addExits();
        greenForestSouth.addExits();
        yellowForestNorth.addExits();
        yellowForestSouth.addExits();
        yellowForestEast.addExits();
        buildingEast.addExits();
        buildingSouthEast.addExits();
        ventEast.addExits();
        ventSouthEast.addExits();
        ventSouthA.addExits();
        ventSouthB.addExits();
        ventSouthWest.addExits();
        ventWest.addExits();
        ventNorthWest.addExits();
        ventNorth.addExits();
        buildingNorthWest.addExits();
        midWestSwamp.addExits();
        southWestSwamp.addExits();
        southSwamp.addExits();
        
        //set starting location
        currentLocation = ship;
    }

    /**
     * Method for navigation
     * @param input
     */
    public void go(Input input){
        if(!input.hasSecond()) window.printLine("GO WHERE?");
        else {
            String direction = input.getInputB();
            Location next = currentLocation.getExit(direction);
            if(next==null)window.printLine("YOU CAN'T GO THAT DIRECTION");
            else{
                currentLocation = next;
                if(currentLocation.getSave()==true){
                    lastSave.setSavePoint(currentLocation, player.getName(), player.getScore(), player.getInventory());
                    hasSaved=true;
                }
                printLocation();
                if(currentLocation.gameOver()==true) gameOver();
               if(currentLocation==darkSwamp){
                    player.setFlashlight(false);
                    deadEnd.setLocation(true);
                    deadEnd.addString("THE TRAIL CONTINUES TO GET DARKER AND YOU LOSE YOUR WAY.");
                    deadEnd.addString("AFTER DAYS OF TRYING TO RETRACE YOUR STEPS YOU STARVE TO DEATH.");
                } 
               if(currentLocation.hasWon()==true) {
                   finished=true; 
                   gameOver=true;
                }
            }
        }
    }

    /**
     * Method to eat an item
     * @param input
     */
    public void eat(Input input){
        if(!input.hasSecond()) window.printLine("EAT WHAT?");
        else {
            if(input.getInputB().equals("FOOD")){
                boolean isFood=false;
                int i=0;
                while(!isFood && i<player.getInventory().getSize()){
                    if(player.getInventory().getItem(i).getName().equals("FOOD")) isFood=true;
                    i++;
                }
                if(isFood==true) use(new Input("USE", "FOOD"));
                else window.printLine("YOU DON'T HAVE ANY FOOD " + player.getName() + ".");
            } else if(input.getInputB().equals("MUSHROOMS")){
                boolean isMushrooms=false;
                int i=0;
                while(!isMushrooms && i<player.getInventory().getSize()){
                    if(player.getInventory().getItem(i).getName().equals("MUSHROOMS")) isMushrooms=true;
                    i++;
                }
                if(isMushrooms==true) use(new Input("USE", "MUSHROOMS"));
                else window.printLine("YOU DON'T HAVE ANY MUSHROOMS " + player.getName() + ".");
            } else {
                window.printLine("YOU CAN'T EAT THAT!");
            }
        }
    }
    //*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
    ////////////////////////////////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
    /// _   _   ____  _____    /////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
    // | | | | |  __|| ____|   /////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
    // | |_| | _\  \ | __|     /////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
    // |_____||_____||_____|   /////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
    //                         /////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*        
    ////////////////////////////////*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
    //*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*      
    /**
     * Method to use items
     * @param input
     */
    public void use(Input input){
        if(!input.hasSecond()) window.printLine("USE WHAT?");
        else {
            String item = input.getInputB();
            Item active = null;
            boolean isFound =false;
            int i =0;
            while(i<player.getInventory().getSize() && !isFound){
                if(player.getInventory().getItem(i).getName().equals(item)) active=player.getInventory().getItem(i);
                i++;
            }
            if(active==null)window.printLine("ITEM NOT IN INVENTORY");
            else {
                if(item.equals("GUN")){
                    //GUN/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
                    window.printLine("YOU FIRE YOUR GUN " + player.getName() + ".");
                    window.printLine("IT STARTS A CHAIN REACTION OF EXPLOSIONS AND PLANET XT17-6 IS DESTROYED.");
                    player.increaseScore(5);
                    gameOver();
                } else if(item.equals("TRANSLATOR")){
                    //TRANSLATOR/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
                    if(currentLocation==gromer){
                        window.printLine("TRANSLATOR ACTIVATED");
                        player.increaseScore(500);
                        gromer.setLocation(false);
                        gromer.addString("YOU CAN NOW UNDERSTAND THE CREATURE.");
                        gromer.addString("HE TELLS YOU THAT HIS NAME IS GROMER AND THAT HE IS A NATIVE OF THE PLANET.");
                        gromer.addString("YOU TELL HIM YOUR STORY AND HE TELLS YOU HE CAN TAKE YOU TO A SHIP.");
                        gromer.addString("TYPE FOLLOW IF YOU WANT GROMER'S HELP.");
                        gromer.addExit("FOLLOW", buildingFront);
                        gromer.removeExit("NORTH");
                        gromer.addExit("NORTH", lost);
                        gromer.addExit("SOUTH", lost);
                        gromer.addExit("SOUTHWEST", lost);
                        gromer.addExits();
                        printLocation();
                    } else window.printLine("YOU HAVE NO USE FOR THE TRANSLATOR RIGHT NOW " + player.getName() + ".");
                } else if(item.equals("FOOD")) {
                    //FOOD/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
                    window.printLine("YUM YUM.");
                    window.printLine("THAT WAS GOOD.");
                    player.getInventory().removeItem(item);
                    player.increaseScore(10);
                } else if(item.equals("RAFT")) {
                    //RAFT/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
                    if(currentLocation==lake || currentLocation==lakeEastSide || currentLocation==lakeWestSide || currentLocation==lakeSouthWestSide || currentLocation==lakeSouthEastSide){
                        window.printLine("YOU TAKE YOUR RAFT TO THE SOUTH END OF THE LAKE.");
                        player.getInventory().removeItem(item);
                        currentLocation = lakeSouth;
                        printLocation();
                    } else if(currentLocation==lakeSouth){
                        window.printLine("YOU TAKE YOUR RAFT TO THE NORTH END OF THE LAKE.");
                        player.getInventory().removeItem(item);
                        currentLocation = lake;
                        printLocation();
                    }else window.printLine("YOU AREN'T NEAR ANY BODIES OF WATER " + player.getName() + ".");
                } else if(item.equals("FLASHLIGHT")) {
                    //FLASHLIGHT/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
                    if(currentLocation==darkSwamp && player.isFlashlightOn()==false){
                        player.setFlashlight(true);
                        deadEnd.clearStrings();
                        deadEnd.setLocation(false);
                        deadEnd.addString("YOU HAVE REACHED A DEAD END AND MUST TURN AROUND " + player.getName() + ".");
                        deadEnd.addExits();
                        window.printLine("THE FLASHLIGHT HELPS YOU SEE YOUR WAY " + player.getName() + ".");
                        window.printLine("YOU CAN TAKE THE PATH TO THE NORTHEAST OR THERE IS A PATH TO THE SOUTHEAST.");
                    } else if(currentLocation==darkSwamp && player.isFlashlightOn()==true){
                        window.printLine("YOUR FLASHLIGHT IS ALREADY ON.");
                    } else{
                        window.printLine("YOU HAVE NO NEED FOR YOUR FLASHLIGHT HERE.");
                    }
                } else if(item.equals("SWORD")){
                    //SWORD/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
                    if(currentLocation==beast){
                        player.increaseScore(2500);
                        window.printLine("YOU HAVE KILLED THE LARGE ALIEN CREATURE " + player.getName() + "!\nYOU HAVE RECEIVED 2,500 POINTS!");
                        beast.setLocation(false);
                        beast.addString("YOU ARE IN A SWAMP.");
                        beast.addString("THE REMAINS OF THE DEAD ALIEN CREATURE ARE STREWN ABOUT.");
                        beast.addExits();
                        printLocation();
                        lastSave.setSavePoint(currentLocation, player.getName(), player.getScore(), player.getInventory());
                    } else window.printLine("YOU HAVE NO USE FOR THE SWORD " + player.getName() + ".");
                } else if(item.equals("KEYS")){
                    //KEYS/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
                    if(currentLocation==buildingFront || currentLocation==buildingEast || currentLocation==buildingSouthEast){
                        player.increaseScore(1500);
                        player.getInventory().removeItem(item);
                        window.printLine("YOU HAVE UNLOCKED THE BUILDING.");
                        buildingFront.setLocation(false);
                        buildingFront.addString("YOU ARE AT THE FRONT OF THE BUILDING " + player.getName() + ".");
                        buildingFront.addString("SOUTH GOES IN THE DOOR, UP GOES INTO THE VENTILATION SHAFT.");
                        buildingFront.addExit("SOUTH", buildingInterior);
                        buildingFront.addExit("UP", ventNorth);
                        buildingFront.addExits();
                        buildingEast.setLocation(false);
                        buildingEast.addString("YOU ARE AT THE EAST ENTRANCE OF THE BUILDING " + player.getName() + ".");
                        buildingEast.addString("WEST GOES IN THE DOOR, UP GOES INTO THE VENTILATION SHAFT.");
                        buildingEast.addExit("WEST", buildingInterior);
                        buildingEast.addExit("UP", ventEast);
                        buildingEast.addExits();
                        buildingSouthEast.setLocation(false);
                        buildingSouthEast.addString("YOU ARE AT THE SOUTHEAST CORNER OF THE BUILDING " + player.getName() + ".");
                        buildingSouthEast.addString("NORTHWEST GOES IN THE DOOR, UP GOES INTO THE VENTILATION SHAFT.");
                        buildingSouthEast.addExit("NORTHWEST", buildingInterior);
                        buildingSouthEast.addExit("UP", ventSouthEast);
                        buildingSouthEast.addExits();
                        buildingNorthWest.setLocation(false);
                        buildingNorthWest.addString("YOU ARE AT THE NORTHWEST CORNER OF THE BUILDING " + player.getName() + ".");
                        buildingNorthWest.addString("SOUTHEAST GOES IN THE DOOR, UP GOES INTO THE VENTILATION SHAFT."); 
                        buildingNorthWest.addExit("UP", ventNorthWest);
                        buildingNorthWest.addExits();
                        lastSave.setSavePoint(currentLocation, player.getName(), player.getScore(), player.getInventory());
                        printLocation();
                    } else window.printLine("THERE IS NOTHING TO UNLOCK HERE.");
                } else if(item.equals("MUSHROOMS")){
                    //MUSHROOMS/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*
                    player.increaseScore(100);
                    player.getInventory().removeItem(item);
                    forestHub.addItem(new Item("MUSHROOMS", "I CAN GIVE YOU HINTS"));
                    Random rand = new Random();
                    int hint = rand.nextInt(5);
                    switch(hint){
                        case 0 :
                        window.printLine("THERE IS A PLACE IN THE SWAMP TO THE EAST OF THE LAKE.");
                        window.printLine("WHERE THE WEATHER IS STRANGE.");
                        window.printLine("SEARCH HIGH AND LOW.");
                        window.printLine("THERE WILL BE SOMETHING YOU NEED.");
                        break;
                        case 1 :
                        window.printLine("WHEN THE PATH GETS DARK.");
                        window.printLine("TURN ON YOUR FLASHLIGHT TO FIND A WAY.");
                        window.printLine("WHEN YOU HIT A DEAD END.");
                        window.printLine("YOU MAY FIND THE POWER TO SLAY THE BEAST.");
                        break;
                        case 2 :
                        window.printLine("TO THE SOUTH OF THE LAKE.");
                        window.printLine("THERE IS SOMEONE WHO CAN HELP.");
                        window.printLine("BUT MAKE SURE THAT YOU CAN UNDERSTAND.");
                        break;
                        case 3 :
                        window.printLine("WHEN YOU ARE INSIDE THE VENTS.");
                        window.printLine("DO NOT GO DOWN.");
                        window.printLine("UNLESS YOU ARE SURE.");
                        window.printLine("YOU ARE FARTHEST SOUTH.");
                        break;
                        case 4 :
                        window.printLine("YOU MIGHT JUST HEAR.");
                        window.printLine("THAT GUNS ARE BAD.");
                        window.printLine("BUT IF YOU USE YOURS HERE.");
                        window.printLine("YOU WILL BE REALLY MAD.");
                        break;
                    }
                }
            }
        }
    }

    /**
     * Method to discard an item from inventory
     * @param input
     */
    public void drop(Input input){
        if(!input.hasSecond()) window.printLine("DROP WHAT?");
        else {
            String item = input.getInputB();
            boolean isFound=false;
            int i=0;
            while(i<player.getInventory().getSize() && !isFound){
                if(player.getInventory().getItem(i).getName().equalsIgnoreCase(item)){
                    player.getInventory().removeItem(item.toUpperCase());
                    isFound=true;
                    window.printLine("YOU HAVE DISCARDEDED " + item.toUpperCase() + ".");
                    if(item.equalsIgnoreCase("gun")) player.increaseScore(25);
                    if(item.equalsIgnoreCase("food")) player.increaseScore(-10);
                    if(item.equals("SWORD")){
                        player.increaseScore(-200);
                        beast.setLocation(true);
                        beast.addString("YOU ARE IN A SWAMP " + player.getName() + ".");
                        beast.addString("THERE IS A LARGE ALIEN CREATURE IN FRONT OF YOU.");
                        beast.addString("THE CREATURE JUMPS OUT AND EATS YOU.");
                    }
                }
                i++;
            }
            if(!isFound) window.printLine("ITEM NOT IN INVENTORY");
        }
    }

    /**
     * Method to pick up an item
     * @param input
     */
    public void take(Input input){
        if(!input.hasSecond()) window.printLine("TAKE WHAT?");
        else {
            if(!currentLocation.hasItem()) window.printLine("THERE ARE NO ITEMS TO TAKE...");
            else{
                String item = input.getInputB().toUpperCase();
                boolean isFound=false;
                int i=0;
                while(i<currentLocation.getItemList().size() && !isFound){
                    if(currentLocation.getItem(i).getName().equals(item)){
                        window.printLine(player.getInventory().addItem(currentLocation.getItem(i)));
                        player.increaseScore(5);
                        currentLocation.removeItem(item);
                        isFound=true;
                        if(item.equals("SWORD")){
                            player.increaseScore(100);
                            beast.setLocation(false);
                            beast.addString("YOU ARE IN A SWAMP " + player.getName() + ".");
                            beast.addString("THERE IS A LARGE ALIEN CREATURE IN FRONT OF YOU.");
                            beast.addExits();
                        }
                        if(item.equals("KEYS")){
                            player.increaseScore(100);
                        }
                    }
                    i++;
                }
                if(!isFound) window.printLine("ITEM IS NOT IN CURRENT LOCATION...");
            }
        }
    }

    /**
     * Method to search location for items
     */
    public void search(){
        if(!currentLocation.getHasItem() || currentLocation.getItemList().size()==0){
            window.printLine("NO ITEMS ARE IN THIS LOCATION");
        } else {
            for(Item o : currentLocation.getItemList()){
                window.printLine(o.print());
            }
        }
    }

    /**
     * Method to display player score
     */
    public void score(){
        window.printLine("YOUR CURRENT SCORE IS " + player.getScore() + ".");
    }

    /**
     * Method to display help information
     */
    public void help(){
        window.printLine("YOU ARE A STARPILOT THAT HAS CRASHED ON AN UNEXPLORED PLANET.");
        window.printLine("YOU MUST FIND A WAY OFF OF THE PLANET.");
        window.printLine("TYPE GO AND A DIRECTION TO NAVIGATE.");
        window.printLine("INVENTORY WILL DISPLAY ITEMS YOU ARE CARRYING.");
        window.printLine("LOOK WILL GIVE INFORMATION ABOUT YOUR LOCATION.");
        window.printLine("SEARCH WILL LIST ANY ITEMS IN YOUR LOCATION.");
        window.printLine("TAKE AND ITEM NAME WILL PICK UP AN ITEM.");
        window.printLine("DROP AND ITEM WILL REMOVE AN ITEM FROM YOUR INVENTORY.");
        window.printLine("USE AND ITEM WILL USE AN ITEM IN YOUR INVENTORY.");
        window.printLine("SCORE DISPLAYS YOUR CURRENT SCORE.");
        window.printLine("QUIT ENDS THE GAME.");
        window.printLine("HELP DISPLAYS THIS INFORMATION.");
    }

    /**
     * Method for a game over
     */
    public void gameOver(){
        window.setIsReady();
        window.printLine("GAME OVER " + player.getName() + "......");
        window.printLine("FINAL SCORE " + player.getScore());
        if(continues>0 && hasSaved==true){
            window.printLine("WOULD YOU LIKE TO CONTINUE?");
            while(!window.isReady()){
                wait(500);
            }
            window.setIsReady();
            String input = window.getPrintout();
            if(input.toLowerCase().startsWith("y")){
                loadSavePoint();
            } else if(input.toLowerCase().startsWith("n")){
                gameOver=true;
            }  
        }
        else{
            gameOver=true;
        }
    }

    /**
     * Method to load previous save point
     */    
    public void loadSavePoint(){
        continues--;
        currentLocation= lastSave.getLocation();
        player = lastSave.getPlayer();
        printLocation();
    }
    
    /**
     * Prints inventory to gui
     */
    public void inventory(){
        if(player.getInventory().getSize()>0){
            window.printLine("INVENTORY:");
            for(Item item : player.getInventory().print()){
                window.printLine(item.print());
            }
        }
        else window.printLine("YOUR INVENTORY IS EMPTY.");
    }
    
    /**
     * Pauses routine to wait for user input
     * @param time
     */
    public void wait(int time){
        try{
                    Thread.sleep(time);
                } catch(InterruptedException ie){}
    }
}