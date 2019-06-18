package FileProcessor;

import GUI.FrameDesign;
import GUI.FrameDesign.Tool;
import Shapes.PenType;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VecConverter extends JFrame {
    // properties for the converter
    private VecReader theReader;
    private VecWriter theWriter;
    private String supportFormat;
    private JFileChooser fileChooser;

    // size of the window
    private int boundaryX;
    private int boundaryY;;
    private int windowWidth;
    private int windowHeight;

    // Initialize the converter
    public VecConverter(int x, int y, int w, int h) {
        // synchronize the GUI size
        this.boundaryX = x;
        this.boundaryY = y;
        this.windowWidth = w;
        this.windowHeight = h;

        // setup properties for the converter
        supportFormat = ".vec";
        theReader = new VecReader(this);
        theWriter = new VecWriter(this);
        fileChooser = new JFileChooser(".");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Vector File", "vec"));

    }

    // Select a vec file to import
    public void Open(Color defaultPen, Color defaultFill, Tool tool, Graphics2D graphics, FrameDesign theWindow) {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            // get the selected file
            File theFile = fileChooser.getSelectedFile();
            String format = theFile.getName().substring(theFile.getName().length()-4);

            // when opening another format file
            if (!format.equals(supportFormat)){
                JOptionPane.showMessageDialog(this,"This is not a support format");
                return;
            }

            // actual open the file
            theReader.Draw(defaultPen, defaultFill, tool, graphics, ReadingTheFile(theFile));

            // change the name of the window
            theWindow.getMainFrame().setTitle(theFile.getName());

        }
    }

    // Open a vec file in a new window
    public void OpenFileInNewWindow(){
        FrameDesign newWindow = new FrameDesign("Choose your vec file");
        Graphics2D newGraphics = newWindow.getPicture();
        Tool newTool = newWindow.getTool();
        Color penColor = newWindow.getPenColor();
        Color fillColor = newWindow.getFillColor();
        Open(penColor, fillColor, newTool, newGraphics, newWindow);

    }

    // Generate information into the record
    public List<String> ReadingTheFile(File theFile){
        String line;
        List<String> list = new ArrayList<String>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(theFile));
            while ((line = reader.readLine()) != null){
                list.add(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    // Select a vec file to export
    public void Save(ArrayList<Object> record){
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
            // get the selected file
            String directory = "";
            FileWriter exporter = null;
            File theFile = fileChooser.getSelectedFile();

            // when creating a non-exist file
            directory = theFile.getPath();
            if (!theFile.exists()) {
                if (!directory.substring(directory.length()-4).equals(supportFormat)) {
                    directory += supportFormat;
                }
            }

            // when trying to save a non-vec format file
            if (!directory.substring(directory.length()-4).equals(supportFormat)) {
                JOptionPane.showMessageDialog(null, "File can only be saved in vec format");
                return;
            }

            // open the file
            try {
                exporter = new FileWriter(directory);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // actual save the file
            theWriter.Export(record, exporter);
            JOptionPane.showMessageDialog(null, "Succesfully exported!");

        }
    }

    // Conversion between text and PenType
    public PenType String2PenType(String text){
        PenType penType = PenType.None;

        switch (text){
            case "PLOT": penType = PenType.Point; break;
            case "LINE": penType = PenType.Line; break;
            case "CIRCLE": penType = PenType.Circle; break;
            case "ELLIPSE": penType = PenType.Ellipse; break;
            case "RECTANGLE": penType = PenType.Rectangle; break;
            case "POLYGON": penType = PenType.Poly; break;
        }

        return penType;
    }
    public String PenType2String(PenType whichType){
        String text = "";

        switch (whichType){
            case Point: text = "PLOT"; break;
            case Line: text = "LINE"; break;
            case Circle: text = "CIRCLE"; break;
            case Ellipse: text = "ELLIPSE"; break;
            case Rectangle: text = "RECTANGLE"; break;
            case Poly: text = "POLYGON"; break;
        }

        return text + " ";
    }

    // Conversion between text and color
    public Color String2Color(String text){
        return new Color(Integer.parseInt(text.substring(1), 16));
    }
    public String Color2String(String type, Color color){
        if (type.equals("FILL ") && color.equals(null)){
            return "FILL OFF\n";
        }else{
            return type +  "#" + Integer.toHexString(color.getRGB()).substring(2).toUpperCase() + "\n";
        }
    }

    // Conversion between text and coordinates
    public int GetXPosition(String text){
        return (int) ((Float.valueOf(text) * windowWidth + boundaryX));
    }
    public int GetYPosition(String text){
        return (int) ((Float.valueOf(text) * windowHeight + boundaryY));
    }
    public String GetXPositionStr(int x){
        return ((float)(x - boundaryX) / (float)windowWidth) + " ";
    }
    public String GetYPositionStr(int y){
        return ((float)(y - boundaryY) / (float)windowHeight) + " ";
    }



}
