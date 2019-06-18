package FileProcessor;

import GUI.FrameDesign.Tool;
import Shapes.Pen;
import Shapes.PenType;

import java.awt.*;
import java.util.List;

public class VecReader {
    Graphics2D graphics;
    private int penSize;
    private Color penColor;
    private Color fillColor;
    private VecConverter theConverter;

    public VecReader(VecConverter theConverter){
        this.theConverter = theConverter;

    }

    public void Draw(Color defaultPen, Color defaultFill, Tool tool, Graphics2D graphics, List<String> list){
        // record graphic and default colors
        this.graphics = graphics;
        this.penColor = defaultPen;
        this.fillColor = defaultFill;

        // clean up the screen first
        tool.Clean();

        // start to draw
        DrawEverySingleShape(list, tool);

        // save it in graphic and record
        tool.Update(graphics);

    }

    private void DrawEverySingleShape(List<String> list, Tool tool){
        // get everyline from the list
        for (String shape : list){
            // get detail of the line
            String[] detail = shape.split(" ");

            // check what type of command it is
            if (detail[0].equals("PENSIZE")){
                penSize = Integer.valueOf(detail[1]);

            }else if (detail[0].equals("PEN")){
                penColor = theConverter.String2Color(detail[1]);

            }else if (detail[0].equals("FILL")){
                if (detail[1].equals("OFF")){
                    fillColor = null;
                }else{
                    fillColor = theConverter.String2Color(detail[1]);
                }

            }else {
                // otherwise draw shapes
                int beginX, beginY, endX, endY;
                PenType penType = theConverter.String2PenType(detail[0]);

                switch (penType){
                    case Point:
                        beginX = theConverter.GetXPosition(detail[1]);
                        beginY = theConverter.GetYPosition(detail[2]);
                        tool.AddPen(new Pen(graphics, penType, penSize, penColor, fillColor, beginX, beginY, 1, 1));
                        break;

                    case Poly:
                        beginX = theConverter.GetXPosition(detail[1]);
                        beginY = theConverter.GetYPosition(detail[2]);
                        Pen pen = new Pen(graphics, penSize, penColor, fillColor, beginX, beginY);

                        for (int i = 3; i < detail.length; i+=2) {
                            endX = theConverter.GetXPosition(detail[i]);
                            endY = theConverter.GetYPosition(detail[i+1]);
                            pen.AddPolygonPoint(endX, endY);
                        }

                        pen.DrawPolygon();

                        tool.AddPen(pen);

                        break;
                    default: // assume that default is circle, ellipse, rectangle and line
                        beginX = theConverter.GetXPosition(detail[1]);
                        beginY = theConverter.GetYPosition(detail[2]);
                        endX = theConverter.GetXPosition(detail[3]);
                        endY = theConverter.GetYPosition(detail[4]);

                        tool.AddPen(new Pen(graphics, penType, penSize, penColor, fillColor, beginX, beginY, endX, endY));
                        break;
                }
            }
        }

    }
























}
