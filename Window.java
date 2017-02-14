//import java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
/**
 * Class to work with a gui
 * 
 * @author Matt Dizzine
 * @version 5.10.15
 */
public class Window extends JFrame
{
    private boolean enter;
    private JLabel output;
    private String printOut;
    private JTextField typeArea;
    private ArrayList<String> outputList;
    private Box center;
    private static final Dimension RESOLUTION = new Dimension(550,300);
    private Font font;
    private JLabel a = new JLabel();
    private JLabel b = new JLabel();
    private JLabel c = new JLabel();
    private JLabel d = new JLabel();
    private JLabel e = new JLabel();
    private JLabel f = new JLabel();
    private JLabel g = new JLabel();
    private JLabel h = new JLabel();
    private JLabel i = new JLabel();
    private JLabel j = new JLabel();
    private JLabel k = new JLabel();
    private JLabel l = new JLabel();
    private JLabel m = new JLabel();
    private JLabel n = new JLabel();
    private JLabel o = new JLabel();
    private ArrayList<JLabel> labels = new ArrayList<JLabel>();
    private boolean twice;
    /**
     * Constructor
     */
    public Window(){
        super("Planet XT17-6");
        enter = false;
        twice = false;
        font = new Font("Monospaced", Font.PLAIN, 16);
        outputList = new ArrayList<String>();
        setLabels();
        makeFrame();
    }
    
    /**
     * Sets all of the JLabels to defaults
     */
    public void setLabels(){
        labels.add(a);
        labels.add(b);
        labels.add(c);
        labels.add(d);
        labels.add(e);
        labels.add(f);
        labels.add(g);
        labels.add(h);
        labels.add(i);
        labels.add(j);
        labels.add(k);
        labels.add(l);
        labels.add(m);
        labels.add(n);
        labels.add(o);
        for(int index=0;index<15;index++){
            outputList.add("");
        }
    }
    
    /**
     * Builds frame and adds components
     */
    public void makeFrame(){
        //Window settings
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(RESOLUTION);
        setMinimumSize(RESOLUTION);
        setBackground(Color.BLACK);
        center = new Box(BoxLayout.PAGE_AXIS);
        center.setOpaque(true);
        center.setBackground(Color.BLACK);
        center.setForeground(Color.GREEN);
        add(center, BorderLayout.CENTER);

        //JPanel bottom = new JPanel();
        Box bottom = new Box(BoxLayout.LINE_AXIS);
        bottom.setOpaque(true);
        bottom.setBackground(Color.BLACK);
        add(bottom, BorderLayout.SOUTH);
        JLabel caret = new JLabel(">");
        caret.setForeground(Color.GREEN);
        caret.setOpaque(true);
        caret.setBackground(Color.BLACK);
        caret.setFont(font);
        bottom.add(caret);
        //typeArea JTextField
        typeArea = new JTextField(10);
        typeArea.setForeground(Color.GREEN);
        typeArea.setOpaque(true);
        typeArea.setBackground(Color.BLACK);
        typeArea.setBorder(null);
        typeArea.setCaretColor(Color.GREEN);
        typeArea.putClientProperty("caretWidth", 8);
        typeArea.setFont(font);
        typeArea.addActionListener(new Listener());
        bottom.add(typeArea);
        typeArea.setText("  ");
        //Build the window and make it visible
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Action Listener class
     */
    private class Listener implements ActionListener{
        /**
         * Occurs when users presses Enter key
         * @param e
         */
        public void actionPerformed(ActionEvent e){
            printOut=typeArea.getText().trim().toUpperCase();
            enter=true;
            if(printOut!=null){
                outputList.add("> " + printOut);
                typeArea.setText(" ");
                multiLine();
            }
        }
    }

    /**
     * Rewrites the all JLabels when ActionEvent occurs
     */
    public void multiLine(){
        if(outputList.size()>15) {
            outputList.remove(0);
        }
        if(twice==true){
            outputList.remove(0);
            twice=false;
        }
        
        center.removeAll();
        validate();
        repaint();
        for(int index=0; index<15; index++){
            labels.get(index).setForeground(Color.GREEN);
            labels.get(index).setFont(font);
            center.add(labels.get(index));
            labels.get(index).setText(outputList.get(index));
        }
    }

    /**
     * Adds user input to outputList
     * @param string
     */
    public void printLine(String string){
        if(string.length()>55){
            String shortString = string.substring(0,55);
            String a = string.substring(0, shortString.lastIndexOf(' '));
            String b = string.substring(shortString.lastIndexOf(' ')+1);
            outputList.add(a);
            outputList.add(b);
            twice=true;
        } else {
        outputList.add(string);
       }
        multiLine();
    }
    
    /**
     * Sets enter parameter to false
     */
    public void setIsReady(){
        enter=false;
    }
    
    /**
     * retrieves user input
     * @return printOut
     */
    public String getPrintout(){
        return printOut;
    }

    /**
     * Has the user input data?
     * @return enter;
     */
    public boolean isReady(){
        return enter;
    }
    
    /**
     * retrieves the outputList
     * @return outputList
     */
    public ArrayList<String> getList(){
        return outputList;
    }
}