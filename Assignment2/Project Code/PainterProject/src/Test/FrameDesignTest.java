package Test;

import FileProcessor.VecConverter;
import GUI.FrameDesign;
import Shapes.Pen;
import Shapes.PenType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FrameDesignTest {
    private static FrameDesign theProject;
    private static Graphics2D theGraphics;
    private static VecConverter theConverter;

    @BeforeAll
    public static void Start(){
        theProject = new FrameDesign("CAB302");
        theGraphics = theProject.getPicture();
        theConverter = theProject.getVecConverter();
    }

    @Test
    public void TestTitleAndLabel(){
        String expect = "This is just a test";
        theProject.ChangeTitle(expect);
        assertEquals(expect, theProject.GetTitle(), "The title does not match.");

    }

    @Test
    public void TestLabel(){
        String expect = "What're you doing??";
        theProject.ChangeLabel(expect);
        assertEquals(expect, theProject.GetLabel(), "The label text does not match.");
    }

    @Test
    public void TestPenColor(){
        Color expect = Color.black;
        theProject.setPenColor(expect);
        assertEquals(expect, theProject.getPenColor(), "The pen color does not match.");

    }

    @Test
    public void TestFillColor(){
        Color expect = Color.black;
        theProject.setFillColor(expect);
        assertEquals(expect, theProject.getFillColor(), "The fill color does not match.");

    }

    @Test
    public void TestDrawPoint(){
        // setup property for the shape
        int sizeExpect = 5;
        float xExpect = 0.324f;
        float yExpect = 0.424f;
        Color penColor = Color.BLACK;
        int xPos = theConverter.GetXPosition(String.valueOf(xExpect));
        int yPos = theConverter.GetYPosition(String.valueOf(yExpect));

        // actual draw the shape
        Pen pen = new Pen(theGraphics, PenType.Point, sizeExpect, penColor, xPos, yPos);

        // test the property
        int sizeActual = pen.getSize();
        float xActual = Float.valueOf(theConverter.GetXPositionStr(pen.getBeginX()));
        float yAcutal = Float.valueOf(theConverter.GetYPositionStr(pen.getBeginY()));

        assertEquals(sizeExpect, sizeActual, "Size does not match!");
        assertEquals(xExpect, xActual, 0.01f,"X positions do not match!");
        assertEquals(yExpect, yAcutal, 0.01f,"Y positions do not match!");
    }

    @Test
    public void TestLine(){
        // setup property for the shape
        int sizeExpect = 19;
        Color penColor = Color.BLACK;
        float beginXExpect = 0f;
        float beginYExpect = 0f;
        float endXExpect = 1f;
        float endYExpect = 1f;
        int beginX = theConverter.GetXPosition(String.valueOf(beginXExpect));
        int beginY = theConverter.GetYPosition(String.valueOf(beginYExpect));
        int endX = theConverter.GetXPosition(String.valueOf(endXExpect));
        int endY = theConverter.GetYPosition(String.valueOf(endYExpect));

        // actual draw the shape
        Pen pen = new Pen(theGraphics, PenType.Line, sizeExpect, penColor, beginX, beginY, endX, endY);

        // test the property
        int sizeActual = pen.getSize();
        float beginXActual = Float.valueOf(theConverter.GetXPositionStr(pen.getBeginX()));
        float beginYActual = Float.valueOf(theConverter.GetYPositionStr(pen.getBeginY()));
        float endXActual = Float.valueOf(theConverter.GetXPositionStr(pen.getEndX()));
        float endYActual = Float.valueOf(theConverter.GetYPositionStr(pen.getEndY()));

        assertEquals(sizeExpect, sizeActual, "Size does not match!");
        assertEquals(beginXExpect, beginXActual, 0.01f,"beginX positions do not match!");
        assertEquals(beginYExpect, beginYActual, 0.01f,"beginY positions do not match!");
        assertEquals(endXExpect, endXActual, 0.01f,"endX positions do not match!");
        assertEquals(endYExpect, endYActual, 0.01f,"endY positions do not match!");
    }

    @Test
    public void TestRectangle(){
        // setup property for the shape
        int sizeExpect = 30;
        Color penColor = Color.BLACK;
        Color fillColor = Color.RED;
        float beginXExpect = 0f;
        float beginYExpect = 0f;
        float endXExpect = 1f;
        float endYExpect = 1f;
        int beginX = theConverter.GetXPosition(String.valueOf(beginXExpect));
        int beginY = theConverter.GetYPosition(String.valueOf(beginYExpect));
        int endX = theConverter.GetXPosition(String.valueOf(endXExpect));
        int endY = theConverter.GetYPosition(String.valueOf(endYExpect));

        // actual draw the shape
        Pen pen = new Pen(theGraphics, PenType.Rectangle, sizeExpect, penColor, fillColor, beginX, beginY, endX, endY);

        // test the property
        int sizeActual = pen.getSize();
        float beginXActual = Float.valueOf(theConverter.GetXPositionStr(pen.getBeginX()));
        float beginYActual = Float.valueOf(theConverter.GetYPositionStr(pen.getBeginY()));
        float endXActual = Float.valueOf(theConverter.GetXPositionStr(pen.getEndX()));
        float endYActual = Float.valueOf(theConverter.GetYPositionStr(pen.getEndY()));

        assertEquals(sizeExpect, sizeActual, "Size does not match!");
        assertEquals(beginXExpect, beginXActual, 0.01f,"beginX positions do not match!");
        assertEquals(beginYExpect, beginYActual, 0.01f,"beginY positions do not match!");
        assertEquals(endXExpect, endXActual, 0.01f,"endX positions do not match!");
        assertEquals(endYExpect, endYActual, 0.01f,"endY positions do not match!");
    }

    @AfterAll
    public static void HoldTheWindow(){
        System.out.println("Test Finished! No error well done!");
    }




}