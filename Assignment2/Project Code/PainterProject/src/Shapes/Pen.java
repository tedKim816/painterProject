package Shapes;

import java.awt.*;
import java.util.ArrayList;

public class Pen {
    // common shape properties
    private Color color;
    private Color fill;
    private PenType type;
    private Graphics2D graphics;
    private int penSize;
    private int beginX, beginY, endX, endY;

    // properties for polygon only
    private int step;
    private ArrayList<Integer> xLocs;
    private ArrayList<Integer> yLocs;


    // method for creating dot pen
    public Pen(Graphics2D whichGraphics, PenType whichType, int Size, Color color, int x, int y){
        this.beginX = x;
        this.beginY = y;
        this.endX = x;
        this.endY = y;
        this.color = color;
        this.penSize = Size;
        this.type = whichType;
        this.graphics = whichGraphics;
        Draw();
    }

    // method for creating line pen
    public Pen(Graphics2D whichGraphics, PenType whichType, int Size, Color color, int beginX, int beginY, int endX, int endY){
        this.beginX = beginX;
        this.beginY = beginY;
        this.endX = endX;
        this.endY = endY;
        this.color = color;
        this.penSize = Size;
        this.type = whichType;
        this.graphics = whichGraphics;
        Draw();
    }

    // method for creating circle, ellipse, rectangle pen
    public Pen(Graphics2D whichGraphics, PenType whichType, int Size, Color color, Color fillColor, int beginX, int beginY, int endX, int endY){
        this.beginX = beginX;
        this.beginY = beginY;
        this.endX = endX;
        this.endY = endY;
        this.color = color;
        this.fill = fillColor;
        this.penSize = Size;
        this.type = whichType;
        this.graphics = whichGraphics;
        Draw();
    }

    // method for creating polygon pen
    public Pen(Graphics2D whichGraphics, int Size, Color color, Color fileColor, int x, int y){
        // setup common properties
        this.color = color;
        this.fill = fileColor;
        this.penSize = Size;
        this.graphics = whichGraphics;
        this.type = PenType.Poly;

        // setup polygon properties
        this.step = 0;
        this.xLocs = new ArrayList<Integer>();
        this.yLocs = new ArrayList<Integer>();
        this.xLocs.add(step, x);
        this.yLocs.add(step, y);
        graphics.setColor(color);

    }

    // Method for drawing a single shape, shape type based on what the current pen type is
    public void Draw(){
        graphics.setStroke(new BasicStroke(penSize, BasicStroke.CAP_ROUND, 0));
        switch (this.type){
            case Point: DrawPoint(); break;
            case Line: DrawLine(); break;
            case Circle: DrawCircle(); break;
            case Ellipse: DrawEllipse(); break;
            case Rectangle: DrawRectangle(); break;
            case Poly: DrawPolygon(); break;
        }
    }

    // Method for drawing a point
    private void DrawPoint(){
        graphics.setColor(color);
        graphics.drawOval(beginX, beginY,penSize,penSize);
        graphics.fillOval(beginX,beginY,penSize,penSize);
    }

    // Method for drawing a line
    private void DrawLine(){
        graphics.setColor(color);
        graphics.drawLine(beginX, beginY, endX, endY);

    }

    // Method for drawing a circle
    private void DrawCircle(){
        int startX = Math.min(beginX, endX);
        int startY = Math.min(beginY, endY);
        int radius = Math.abs(beginX - endX);

        graphics.setColor(color);
        graphics.drawOval(startX, startY, radius, radius);
        if (fill != null){
            graphics.setColor(fill);
            graphics.fillOval(startX, startY, radius, radius);
        }
    }

    // Method for drawing an ellipse
    private void DrawEllipse(){
        int startX = Math.min(beginX, endX);
        int startY = Math.min(beginY, endY);
        int width = Math.abs(beginX - endX);
        int height = Math.abs(beginY - endY);

        graphics.setColor(color);
        graphics.drawOval(startX, startY, width, height);
        if (fill != null){
            graphics.setColor(fill);
            graphics.fillOval(startX, startY, width, height);
        }

    }

    // Method for drawing a rectangle
    private void DrawRectangle(){
        int startX = Math.min(beginX, endX);
        int startY = Math.min(beginY, endY);
        int width = Math.abs(beginX - endX);
        int height = Math.abs(beginY - endY);

        graphics.setColor(color);
        graphics.drawRect(startX, startY, width, height);
        if (fill != null){
            graphics.setColor(fill);
            graphics.fillRect(startX, startY, width, height);
        }

    }

    // Method for drawing a polygon
    public void DrawPolygon(){
        // draw a line between new point and preview point
        AddPolygonPoint(xLocs.get(step), yLocs.get(step));

        // convert points from list to array
        int[] xs = buildInArray(xLocs);
        int[] ys = buildInArray(yLocs);

        graphics.setColor(color);
        graphics.drawPolygon(xs, ys, step);

        if (fill != null){
            graphics.setColor(fill);
            graphics.fillPolygon(xs, ys, step);
        }
    }

    // Method for adding polygon point
    public void AddPolygonPoint(int x, int y){
        // draw a line between new point and preview point
        graphics.drawLine(xLocs.get(step),yLocs.get(step),x,y);

        // update vertices
        step += 1;
        xLocs.add(step, x);
        yLocs.add(step, y);

    }

    // Get how many angle the polygon currently has
    public int GetPolygonStep(){
        return step;
    }

    // Convert an array list to a int array
    private int[] buildInArray(ArrayList<Integer> points){
        int[] ints = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            ints[i] = points.get(i);
        }
        return ints;
    }

    public PenType getType(){
        return type;
    }

    public int getBeginX(){
        return beginX;
    }

    public int getBeginY(){
        return beginY;
    }

    public int getEndX(){
        return endX;
    }

    public int getEndY(){
        return endY;
    }

    public Color getPenColor(){
        return color;
    }

    public Color getFillColor(){
        return fill;
    }

    public int getSize(){ return penSize;}

    public ArrayList<ArrayList<Integer>> getPolyCoordinates(){
        ArrayList<ArrayList<Integer>> cors = new ArrayList<>();

        cors.add(xLocs);
        cors.add(yLocs);

        return cors;
    }

}
