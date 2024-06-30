
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.Border;

public class GUI {
    private static final String DEFAULT_LABEL_COLUMN = "right";
  
    private JFrame mainFrame;
    private JPanel controlPanelUP;
    private JPanel controlPanelDOWN;
    private JPanel controlPanelUP1;
    private JPanel controlPanelUP2;
    private JPanel controlPanelUP3;
    private JPanel controlPanelUP4;
    private JPanel controlPanelUP5;
    private JPanel controlPanelUP6;
    private JPanel controlPanelUP7;
    private JPanel controlPanelButtons;
    
    private JFileChooser fc;
    private JTextField model1;
    private JTextField model2;
    private JLabel openLabel;
    private JLabel model1Label;
    private JLabel model2Label;
    private JLabel delimiterLabel;
    private JTextArea log;
    
    private String delimiter;
    private File file;
    private String labelColumn;
    private String modelName;
    
    public GUI() {
        prepareGUI();
    }
    
    public static void main(String[] args){
        GUI swingControlDemo = new GUI();  
        swingControlDemo.showCalc();       
    }
    
    private void prepareGUI() {      
        mainFrame = new JFrame("A' calculator");
        mainFrame.setSize(500,500);
        mainFrame.setLayout(new GridLayout(2, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }        
        });  
        
        controlPanelUP = new JPanel();
        controlPanelUP.setLayout(new GridLayout(7, 1));
        controlPanelDOWN = new JPanel();
        controlPanelDOWN.setLayout(new BorderLayout());
        
        Border paddingUP = BorderFactory.createEmptyBorder(20, 25, 5, 25);
        Border paddingDOWN = BorderFactory.createEmptyBorder(10, 10, 10, 10);
       
        controlPanelUP.setBorder(paddingUP);
        controlPanelDOWN.setBorder(paddingDOWN);
      
        controlPanelUP1 = new JPanel();
        controlPanelUP1.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlPanelUP2 = new JPanel();
        controlPanelUP2.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlPanelUP3 = new JPanel();
        controlPanelUP3.setLayout(new GridLayout(1, 2));
        controlPanelUP4 = new JPanel();
        controlPanelUP4.setLayout(new GridLayout(1, 2));
        controlPanelUP5 = new JPanel();
        controlPanelUP5.setLayout(new GridLayout(1, 2));
        controlPanelUP6 = new JPanel();
        controlPanelUP6.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlPanelUP7 = new JPanel();
        controlPanelUP7.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        controlPanelButtons = new JPanel();
        controlPanelButtons.setLayout(new FlowLayout());

        controlPanelUP.add(controlPanelUP1);
        controlPanelUP.add(controlPanelUP2);
        controlPanelUP.add(controlPanelUP3);
        controlPanelUP.add(controlPanelUP4);
        controlPanelUP.add(controlPanelUP5);
        controlPanelUP.add(controlPanelUP6);
        controlPanelUP.add(controlPanelUP7);
      
        mainFrame.add(controlPanelUP);
        mainFrame.add(controlPanelDOWN);
    
        mainFrame.setVisible(true);
    }
    
    private void showCalc() {
        file = null;
        fc = new JFileChooser();
        model1 = new JTextField(10);
        model2 = new JTextField(20);
        openLabel = new JLabel();
        model1Label = new JLabel("Model1");
        model2Label = new JLabel("Model2 (optional)");
        delimiterLabel = new JLabel("Delimiter (optional)");
        log = new JTextArea();
        log.setMargin(new Insets(2,2,2,2));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        
        JButton openButton = new JButton("Open file");
        JButton simpleButton = new JButton("Run Simple");
        JButton perStudentButton = new JButton("Run Per Student");
        
        openButton.setActionCommand("Open");
        simpleButton.setActionCommand("Simple");
        perStudentButton.setActionCommand("perStudent");
        openButton.addActionListener(new OpenButtonClickListener());
        simpleButton.addActionListener(new RunButtonClickListener());
        perStudentButton.addActionListener(new RunButtonClickListener()); 

        JRadioButton tabButton = new JRadioButton("Tab");
        JRadioButton spaceButton = new JRadioButton("Space");
        JRadioButton commaButton = new JRadioButton("Comma");
        
        ButtonGroup group = new ButtonGroup();
        group.add(tabButton);
        group.add(spaceButton);
        group.add(commaButton);
        tabButton.addActionListener(new RadioButtonClickListener());
        spaceButton.addActionListener(new RadioButtonClickListener());
        commaButton.addActionListener(new RadioButtonClickListener());
        tabButton.setActionCommand("\t");
        spaceButton.setActionCommand(" ");
        commaButton.setActionCommand(",");
       
        controlPanelUP1.add(openButton);
        controlPanelUP1.add(openLabel);  
        controlPanelUP3.add(model1Label);
        controlPanelUP3.add(model1);
        controlPanelUP4.add(model2Label);
        controlPanelUP4.add(model2);
        controlPanelUP5.add(delimiterLabel);
        controlPanelUP5.add(controlPanelButtons);      
        controlPanelUP7.add(simpleButton);
        controlPanelUP7.add(perStudentButton);
        controlPanelDOWN.add(logScrollPane, BorderLayout.CENTER);
        controlPanelButtons.add(tabButton);
        controlPanelButtons.add(spaceButton);
        controlPanelButtons.add(commaButton);
        
        mainFrame.setVisible(true);  
    }
    
    private class RunButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();  
            
            // Substitute StdErr and StdOut to capture the output
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            PrintStream oldOut = System.out;
            PrintStream oldErr = System.err;
            System.setOut(ps);
            System.setErr(ps); 
            
            if(command.equals( "Simple" ))  {
                if(file == null) {
                    log.setText("No file selected.");
                    return;
                }
                if(delimiter == null) {
                    log.setText("You need to choose the delimiter in order to use Simple A\'.");
                    return;
                }
                if(model1.getText().equals("") && model2.getText().equals("")) {
                    log.setText("You need to specify at least 1 model name to use Simple A\'.");
                    return;
                }
                
                try {
                    SimpleAPrime computer = new SimpleAPrime();
                    Map<String,Double> results;
                    
                    labelColumn = DEFAULT_LABEL_COLUMN;
                    modelName = model1.getText();
                    results = computer.computeAPrimeFromFile(file.getAbsolutePath(), delimiter, labelColumn, modelName);
                    printSimpleResults(results);
                    if(!model2.getText().equals("")) {
                        modelName = model2.getText();
                        results = computer.computeAPrimeFromFile(file.getAbsolutePath(), delimiter, labelColumn, modelName);
                        printSimpleResults(results);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }                
            } else if(command.equals("perStudent"))  {
                if(file == null) {
                    log.setText("No file selected.");
                    return;
                }
                if(model1.getText().equals("") && model2.getText().equals("")) {
                    log.setText("You need to specify both model names to use A\' per srudent.");
                    return;
                }
                
                try {
                    AprimeperstudentD computer = new AprimeperstudentD();
                    computer.a_prime_general(file.getAbsolutePath(), model1.getText(), model2.getText());

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
             }
            
            System.out.flush();
            // Important! Set output streams to old values
            System.setOut(oldOut);
            System.setErr(oldErr);
            
            log.setText(baos.toString());
        }     
    }

    private class OpenButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int returnVal = fc.showOpenDialog(mainFrame);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                openLabel.setText(file.getName());
            }
        }
    }
    
    private class RadioButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            delimiter = e.getActionCommand(); 
        }
    }
    
    private void printSimpleResults(Map<String,Double> results) {
        System.out.println("Model: " + modelName);
        System.out.println();
        System.out.println("A' = " + results.get(SimpleAPrime.APRIME));
        System.out.println("True Positives = " + results.get(SimpleAPrime.TRUE_POSITIVES));
        System.out.println("False Positives = " + results.get(SimpleAPrime.FALSE_POSITIVES));
        System.out.println();
    }
}