package FileProcessor;

import Shapes.Pen;
import Shapes.PenType;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VecWriter {
    List<String> list;
    private VecConverter theConverter;

    public VecWriter(VecConverter theConverter) {
        this.theConverter = theConverter;
    }

    public void Export(ArrayList<Object> record, FileWriter theExporter){
        // create a list variable
        list = new ArrayList<>();

        // scanning for objs
        ScanForObjs(record);

        // write the file
        WriteInInFile(theExporter);
    }

    // look for every single pen in this graphics
    private void ScanForObjs(ArrayList<Object> record){
        for (Object obj : record){
            Pen pen = (Pen)obj;
            InsertObjInfoIntoList(pen);
        }
    }

    // when pen is found, get it's pen color, fill color, locations and save it as a string
    // and then add the string to the list ()
    private void InsertObjInfoIntoList(Pen pen){
        // create a string to record info
        String order = "";

        // saving pensize
        order += "PENSIZE " + pen.getSize() + "\n";

        // saving pencolor
        order += theConverter.Color2String("PEN ", pen.getPenColor());

        // saving fill color
        Color fillColor = pen.getFillColor();
        if (fillColor == null){
            order += "FILL OFF\n";
        }else{
            order += theConverter.Color2String("FILL ",fillColor);
        }

        // saving shape and its coordinates
        PenType type = pen.getType();
        switch (type){
            case Point:
                order += theConverter.PenType2String(pen.getType());
                order += theConverter.GetXPositionStr(pen.getBeginX());
                order += theConverter.GetYPositionStr(pen.getBeginY());
                break;
            case Poly:
                order += theConverter.PenType2String(pen.getType());
                ArrayList<ArrayList<Integer>> cors = pen.getPolyCoordinates();
                ArrayList<Integer> xCos = cors.get(0);
                ArrayList<Integer> yCos = cors.get(1);

                for (int i = 0; i < xCos.size(); i++) {
                    order += theConverter.GetXPositionStr(xCos.get(i));
                    order += theConverter.GetYPositionStr(yCos.get(i));
                }

                break;
            default: // assume that default is circle, ellipse, rectangle and line
                order += theConverter.PenType2String(pen.getType());
                order += theConverter.GetXPositionStr(pen.getBeginX());
                order += theConverter.GetYPositionStr(pen.getBeginY());
                order += theConverter.GetXPositionStr(pen.getEndX());
                order += theConverter.GetYPositionStr(pen.getEndY());
                break;
        }

        // change line
        order += "\n";

        // actual add cotent to the list
        list.add(order);
    }

    // read every line from the list, and write it to the file writer
    private void WriteInInFile(FileWriter theExporter){
        for (int i = 0; i < list.size(); i++) {
            try {
                theExporter.write(list.get(i));
                if (i == list.size() - 1){
                    theExporter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }







}
