package com.mycompany.basictexteditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TextEditor extends JFrame implements ActionListener {
    
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JComboBox fontSizeBox;
    private JComboBox fontStyleBox;
    private JComboBox themeBox;
    private JButton fontColorButton;
    private JLabel fileNameLabel;
    
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem exitItem;
    
    private File openedFile = null;
    
    public TextEditor() {
        
        this.setTitle("Simple Text Editor");
        this.setSize(500, 600);
        this.getContentPane().setBackground(Color.LIGHT_GRAY);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
        
        //TextArea =============================================================
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Calibri", Font.PLAIN, 20));
        
        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        //Font Label ===========================================================
        JLabel fontLabel = new JLabel("Size: ");
        
        //Font Size ============================================================
        Integer[] fontSize = new Integer[41];
        int size = 10;
        
        for (int i = 0; i < fontSize.length; i++) {
            fontSize[i] = size;
            size++;
        }
        
        fontSizeBox = new JComboBox(fontSize);
        fontSizeBox.setSelectedItem(20);
        fontSizeBox.setFocusable(false);
        fontSizeBox.addActionListener(this);
        
        //Font Color Button ====================================================
        fontColorButton = new JButton("Font Color");
        fontColorButton.setFocusable(false);
        fontColorButton.addActionListener(this);
        
        //Font Style ===========================================================
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        
        fontStyleBox = new JComboBox(fonts);
        fontStyleBox.setSelectedItem("Calibri");
        fontStyleBox.setFocusable(false);
        fontStyleBox.addActionListener(this);
        
        //File Name Label ======================================================
        fileNameLabel = new JLabel("File Name: None");
        
        //Theme Label ==========================================================
        JLabel themeLabel = new JLabel("Appearance: ");
        
        //Text Editor Theme ====================================================
        String[] theme = {"Default", "Dark"};
        
        themeBox = new JComboBox(theme);
        themeBox.setSelectedItem("Default");
        themeBox.setFocusable(false);
        themeBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
               if (e.getStateChange() == ItemEvent.SELECTED) {
                   String selectedTheme = (String) themeBox.getSelectedItem();
                   
                   if (selectedTheme.equals("Dark")) {
                       textArea.setBackground(Color.BLACK);
                       textArea.setForeground(Color.WHITE);
                       textArea.setCaretColor(Color.WHITE);
                       getContentPane().setBackground(Color.GRAY);
                   }
                   else {
                       textArea.setBackground(Color.WHITE);
                       textArea.setForeground(Color.BLACK);
                       textArea.setCaretColor(Color.BLACK);
                       getContentPane().setBackground(Color.LIGHT_GRAY);
                       
                   }
               }
            }
            
        });
        
        //Menu Bar =============================================================
        menuBar = new JMenuBar();
        menuBar.setBackground(Color.LIGHT_GRAY);
        menuBar.setOpaque(true);
        
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);
        
        openItem = new JMenuItem("Open");
        openItem.setMnemonic(KeyEvent.VK_O);
        openItem.addActionListener(this);
        fileMenu.add(openItem);
        
        saveItem = new JMenuItem("Save");
        saveItem.setMnemonic(KeyEvent.VK_S);
        saveItem.addActionListener(this);
        fileMenu.add(saveItem);
        
        exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic(KeyEvent.VK_E);
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
            
        });
        this.setJMenuBar(menuBar);
        this.add(fontLabel);
        this.add(fontSizeBox);
        this.add(fontStyleBox);
        this.add(fontColorButton);
        this.add(fileNameLabel);
        this.add(new JLabel("     "));
        this.add(themeLabel);
        this.add(themeBox);
        this.add(scrollPane);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        //Fonts ================================================================
        if (e.getSource() == fontSizeBox) {
            textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int) fontSizeBox.getSelectedItem()));
        }
        
        if (e.getSource() == fontColorButton) {
            Color color = JColorChooser.showDialog(null, "Choose a Color", Color.black);
            
            textArea.setForeground(color);
        }
        
        if (e.getSource() == fontStyleBox) {
            textArea.setFont(new Font((String) fontStyleBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
        }
        
        //Menu =================================================================
        if (e.getSource() == openItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            
            FileNameExtensionFilter txtFilter = new FileNameExtensionFilter("Text File (.txt)", "txt");
            fileChooser.setFileFilter(txtFilter);
            
            FileNameExtensionFilter htmlFilter = new FileNameExtensionFilter("HTML File (.html)", "html");
            fileChooser.setFileFilter(htmlFilter);
            
            int response = fileChooser.showOpenDialog(null);
            
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                
                try (Scanner fileScanner = new Scanner(file)) {
                    if (file.isFile()) {
                        textArea.setText("");
                        while (fileScanner.hasNextLine()) {
                            String line = fileScanner.nextLine() + "\n";
                            textArea.append(line);
                        }
                        openedFile = file;
                        fileNameLabel.setText("File Name: " + openedFile.getName());
                    }
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "An error occured while saving the file: " + ex.getMessage(), 
                            "File Creation Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        if (e.getSource() == saveItem) {
            saveFile();
        }
        
        if (e.getSource() == exitItem) {
            handleWindowClosing();
        }
    }
    
    private void handleWindowClosing() {
        if (textArea.getText().isEmpty()) {
            System.exit(0);
        }
//        else if (textArea.getText().equals(fileContents)) {
//            System.exit(0);
//        }
        else {
            int option = JOptionPane.showConfirmDialog(null, "Do you want to saved changes?", 
                "Save Changes", JOptionPane.YES_NO_OPTION);
        
            if (option == JOptionPane.YES_OPTION) {
                saveFile();
                System.exit(0);
            }
            else {
            System.exit(0);
            }
        }
    }
    
    private void saveFile() {
        if (openedFile != null) {
            File fileToSave = openedFile;
    
            if (fileToSave.exists()) {
                try (PrintWriter printWriter = new PrintWriter(fileToSave)) {
                    printWriter.println(textArea.getText());
                } 
                catch (IOException ex) {
                   JOptionPane.showMessageDialog(null, "An error occured while saving the file: " + ex.getMessage(), 
                                "File Overwriting Failed", JOptionPane.ERROR_MESSAGE);
                }
            }    
        }
        else {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
                
            int response = fileChooser.showSaveDialog(null);
                
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    
               try (PrintWriter printWriter = new PrintWriter(file)) {
                        printWriter.println(textArea.getText());
                        fileNameLabel.setText("File Name: " + file.getName());
                        
//                        openedFile = file;
                } 
                catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "An error occured while saving the file: " + ex.getMessage(), 
                            "File Creation Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
