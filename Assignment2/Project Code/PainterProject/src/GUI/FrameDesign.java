package GUI;

import FileProcessor.VecConverter;
import Shapes.Pen;
import Shapes.PenType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FrameDesign {
    // window properties
    private int xGap = 140;
    private int yGap = 70;
    private int boundX = xGap + 6;
    private int boundY = yGap + 28;
    private int windowWidth = 650;
    private int windowHeight = 650;
    private JPanel TopPanel, CanvasPanel, SidePanel;
    private Grid theGrid;
    private JFrame theFrame;
    private static FrameDesign theWindow;

    // brush properties
    private int brushSize = 1;
    private JTextField brushSizeEnter;
    private PenType currentType = PenType.None;
    private Color InitPenColor = Color.BLACK;
    private Color InitFillColor = null;
    private Color ButtonColor = Color.decode("#87e7eb");
    private Color PressButtonColor = Color.decode("#B0C4DE");

    // button properties
    private PenType[] penTypes;
    private JButton[] shapeBtns;
    private JButton[] colorBtns;
    private ImageIcon[] gridIcon;
    private JButton UndoButton,
            RedoButton,
            ClearButton,
            GridButton,
            LineColorButton,
            FillColorButton,
            showLineColorBtn,
            showFillColorBtn,
            quickColorSelectionBtn;

    // menu items
    private JLabel toolLabel;
    private JMenuItem itOpen, itSave, itExit, itNew;

    // tool to record shapes
    private boolean isDrawing;
    private Graphics2D graphics;
    private Tool tool = new Tool();
    private VecConverter vecConverter;
    private ArrayList<Object> record = new ArrayList<>();

    // common button trigger
    private ToolBtnTrigger toolBtnTrigger = new ToolBtnTrigger();

    // When first running the program
    public static void main(String[] args){
        theWindow = new FrameDesign("CAB302");

    }

    public FrameDesign(String title) {
        theFrame = new JFrame(title);

        CreateFrame();
        CreateMenuBar();
        CreateShapeButtons();
        CreateToolButtons();
        CreateColorButtons();
        CreateColorPalette();
        CreatePensizeChooser();
        SetUpCoreComponents();

    }

    // Method for creating the window layout
    private void CreateFrame(){
        // setup windows size
        theFrame.setSize(windowWidth + xGap + 15, windowHeight + yGap + 35);

        // create a top panel
        TopPanel = new JPanel();
        TopPanel.setBounds(0, 20, windowWidth + xGap, 50);
        TopPanel.setBackground(Color.GRAY);

        // create a side panel
        SidePanel = new JPanel();
        SidePanel.setBounds(0, yGap, xGap, windowHeight - yGap);
        SidePanel.setBackground(Color.lightGray);

        // creating panels
        CanvasPanel = new JPanel();
        CanvasPanel.setBounds(xGap,yGap,windowWidth,windowHeight);

        // add panels in window
        theFrame.add(TopPanel);
        theFrame.add(CanvasPanel);
        theFrame.add(SidePanel);
        TopPanel.setLayout(null);
        CanvasPanel.setLayout(null);
        SidePanel.setLayout(null);

        // add "Choose your tool !" label
        toolLabel = new JLabel("Welcome");
        toolLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        toolLabel.setBounds(xGap / 10, 0, TopPanel.getWidth(), TopPanel.getHeight());
        TopPanel.add(toolLabel);

        // setup mouse trigger for the canvas panel
        MouseTrigger mouseTrigger = new MouseTrigger();
        CanvasPanel.addMouseListener(mouseTrigger);
        CanvasPanel.addMouseMotionListener(mouseTrigger);

    }

    // Method for creating menu bar
    public void CreateMenuBar() {
        // create a menu bar variable
        JMenuBar theBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File (F)");

        // place menu the the GUI
        theBar.add(fileMenu);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        theFrame.getContentPane().add(theBar, "North");

        // create items for menu
        itNew = new JMenuItem("New (N)", KeyEvent.VK_N);
        itOpen = new JMenuItem("Open (O)", KeyEvent.VK_O);
        itSave = new JMenuItem("Save (S)", KeyEvent.VK_S);
        itExit = new JMenuItem("Exit (X)", KeyEvent.VK_X);

        fileMenu.add(itNew);
        fileMenu.add(itOpen);
        fileMenu.add(itSave);
        fileMenu.add(itExit);

        // add trigger to items
        MenuItemTrigger menuItemTrigger = new MenuItemTrigger();
        itNew.addActionListener(menuItemTrigger);
        itOpen.addActionListener(menuItemTrigger);
        itSave.addActionListener(menuItemTrigger);
        itExit.addActionListener(menuItemTrigger);

    }

    // Method for creating selective shape shapeBtns
    private void CreateShapeButtons(){
        // common property for each shapeBtns
        int btnQuantity = 6;
        int btnWidth = 100;
        int btnHeight = 40;
        int beginX = (xGap - 100) / 2;
        int beginY = yGap;
        int marginY = 50;

        // specify property for each button
        shapeBtns = new JButton[btnQuantity];
        String[] btnNames = {
                "Point",
                "Line",
                "Circle",
                "Ellipse",
                "Rectangle",
                "Poly"};
        penTypes = new PenType[]{
                PenType.Point,
                PenType.Line,
                PenType.Circle,
                PenType.Ellipse,
                PenType.Rectangle,
                PenType.Poly};

        // initialise listener for shapeBtns
        ShapeBtnTrigger btnTrigger = new ShapeBtnTrigger();

        // actual creating the shapeBtns
        for (int i = 0; i < btnQuantity; i++) {
            // placing button to side panel
            shapeBtns[i] = new JButton(btnNames[i]);
            shapeBtns[i].setBounds(beginX,beginY + i * marginY,btnWidth,btnHeight);
            SidePanel.add(shapeBtns[i]);

            // setup button's color and trigger
            shapeBtns[i].setBackground(ButtonColor);
            shapeBtns[i].addActionListener(btnTrigger);

        }

    }

    // Method for creating tool shapeBtns
    private void CreateToolButtons(){
        // setup grid icon paths
        gridIcon = new ImageIcon[2];
        gridIcon[0] = new ImageIcon("image\\grid_on.png");
        gridIcon[1] = new ImageIcon("image\\grid_off.png");

        // shapeBtns creation
        UndoButton = new JButton (new ImageIcon("image\\undo.gif"));
        RedoButton = new JButton (new ImageIcon("image\\redo.gif"));
        ClearButton = new JButton(new ImageIcon("image\\clean.png"));
        GridButton = new JButton(gridIcon[0]);

        // set shapeBtns color
        UndoButton.setBackground(ButtonColor);
        RedoButton.setBackground(ButtonColor);
        ClearButton.setBackground(ButtonColor);
        GridButton.setBackground(ButtonColor);

        // set undo and redo button disable
        UndoButton.setEnabled(false);
        RedoButton.setEnabled(false);

        // set shapeBtns locations
        UndoButton.setBounds(windowWidth - 200, 5, 80, 40);
        RedoButton.setBounds(windowWidth - 115, 5, 80, 40);
        ClearButton.setBounds(windowWidth - 30, 5, 80, 40);
        GridButton.setBounds(windowWidth + 55, 5, 80, 40);

        // add shapeBtns in window
        TopPanel.add(UndoButton);
        TopPanel.add(RedoButton);
        TopPanel.add(ClearButton);
        TopPanel.add(GridButton);

        // add trigger to shapeBtns
        UndoButton.addActionListener(toolBtnTrigger);
        RedoButton.addActionListener(toolBtnTrigger);
        ClearButton.addActionListener(toolBtnTrigger);
        GridButton.addActionListener(toolBtnTrigger);

    }

    // Method for creating selective color shapeBtns
    private void CreateColorButtons(){
        Font colorFont = new Font("Arial", Font.ITALIC, 10);

        LineColorButton = new JButton("Line color");
        FillColorButton = new JButton("Fill color");
        showLineColorBtn = new JButton();
        showFillColorBtn = new JButton();

        LineColorButton.setFont(colorFont);
        FillColorButton.setFont(colorFont);

        LineColorButton.setBackground(ButtonColor);
        FillColorButton.setBackground(ButtonColor);
        showLineColorBtn.setBackground(InitPenColor);
        showFillColorBtn.setBackground(InitFillColor);

        LineColorButton.setBounds(30, 370, 80, 30);
        FillColorButton.setBounds(30, 410, 80, 30);
        showLineColorBtn.setBounds(20, 450, 60, 60);
        showFillColorBtn.setBounds(60, 490, 60, 60);

        SidePanel.add(LineColorButton);
        SidePanel.add(FillColorButton);
        SidePanel.add(showLineColorBtn);
        SidePanel.add(showFillColorBtn);

        LineColorButton.addActionListener(toolBtnTrigger);
        FillColorButton.addActionListener(toolBtnTrigger);
        showLineColorBtn.addActionListener(toolBtnTrigger);
        showFillColorBtn.addActionListener(toolBtnTrigger);

    }

    // Method for creating color palette
    private void CreateColorPalette(){
        // setup button quantities, sizes and positions
        int btnQuantity = 12;
        int btnWidth = 20;
        int btnHeight = 20;
        int beginX_left = (SidePanel.getWidth() - 95);
        int beginX_right = (SidePanel.getWidth() - 65 );
        int beginY_left = SidePanel.getHeight() - 20;
        int beginY_right = SidePanel.getHeight() - 140;
        int marginY = 20;

        // create shapeBtns and setup color lists
        colorBtns = new JButton[btnQuantity];
        Color[] quickColorSelection = {Color.BLACK,
                Color.GRAY,
                Color.lightGray,
                Color.WHITE,
                Color.BLUE,
                Color.CYAN,
                Color.GREEN,
                Color.MAGENTA,
                Color.ORANGE,
                Color.PINK,
                Color.RED,
                Color.YELLOW};

        // actual adding shapeBtns to the window
        for (int i = 0; i < btnQuantity; i++) {
            // create the color button
            colorBtns[i] = new JButton();

            // place it in the right position
            if (i <= 5){
                colorBtns[i].setBounds(beginX_left,beginY_left + i * marginY, btnWidth, btnHeight);
            }else{
                colorBtns[i].setBounds(beginX_right,beginY_right + i * marginY, btnWidth, btnHeight);
            }

            // adding the button to panel and trigger
            SidePanel.add(colorBtns[i]);
            colorBtns[i].setBackground(quickColorSelection[i]);
            colorBtns[i].addActionListener(toolBtnTrigger);
        }

    }

    // Method for creating pensize chooser
    private void CreatePensizeChooser(){
        // setup labels
        JLabel titleLabel = new JLabel("<html>Pen<br/>Size :<html>");
        brushSizeEnter = new JTextField(String.valueOf(brushSize), 5);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        brushSizeEnter.setHorizontalAlignment(JTextField.CENTER);

        // positioning the chooser
        titleLabel.setBounds(windowWidth - 330, 0, 100, 55);
        brushSizeEnter.setBounds(windowWidth - 255, 9, 50, 35);
        TopPanel.add(titleLabel);
        TopPanel.add(brushSizeEnter);

    }

    private void SetUpCoreComponents(){
        // setup window's properties
        theFrame.setVisible(true);
        theFrame.setResizable(false);
        theFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // setup core tools
        graphics = (Graphics2D)theFrame.getGraphics();
        graphics.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND,0));

        // record panel margin
        vecConverter = new VecConverter(boundX, boundY, windowWidth, windowHeight);

        // setup the grid
        theGrid = new Grid(10);


    }

    // Method for users to select the drawing shape
    private class ShapeBtnTrigger implements ActionListener{
        private int lastSelectedBtn = -1;

        @Override
        public void actionPerformed(ActionEvent e) {
            // get the press button
            int btnIndex = GetPressButtonIndex(e);

            // cancel drawing poly
            if (isDrawing && currentType == PenType.Poly){
                isDrawing = false;
                tool.Undo();
            }

            // check if the user is clicking on a new button
            if (lastSelectedBtn != btnIndex){
                // reset color for the preview button
                if (lastSelectedBtn != -1){
                    shapeBtns[lastSelectedBtn].setBackground(ButtonColor);
                }

                // color the button and change the penType
                lastSelectedBtn = btnIndex;
                currentType = penTypes[btnIndex];
                shapeBtns[btnIndex].setBackground(PressButtonColor);

            }else {
                // otherwise, it must be deselecting the current button
                lastSelectedBtn = -1;
                currentType = PenType.None;
                shapeBtns[btnIndex].setBackground(ButtonColor);
            }
        }

        // get the index for the pressed button
        private int GetPressButtonIndex(ActionEvent e){
            JButton src = (JButton)e.getSource();

            for (int i = 0; i < shapeBtns.length; i++) {
                if (src == shapeBtns[i]){
                    return i;
                }
            }
            return 0;
        }

    }

    // Method for users to use tool at once
    private class ToolBtnTrigger implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton)e.getSource();

            if (clickedButton.equals(ClearButton)){
                tool.Clean();

            }else if (clickedButton.equals(UndoButton)) {
                tool.Undo();

            } else if (clickedButton.equals(RedoButton)) {
                tool.Redo();

            } else if (clickedButton.equals(LineColorButton)) {
                tool.ChooseLineColor();

            } else if (clickedButton.equals(FillColorButton)) {
                tool.ChooseFillColor();

            } else if (clickedButton.equals(GridButton)){
                tool.SwitchGrid();

            }else if (clickedButton.equals(showLineColorBtn) || clickedButton.equals(showFillColorBtn)) {
                quickColorSelectionBtn = clickedButton;

            } else if (IsClickingColorPattern(clickedButton)) {
                UseQuickSelecteColor(clickedButton);

            }
        }

        // check if the user is clicking on the color palette
        private boolean IsClickingColorPattern(JButton clickedButton){
            for (int i = 0; i < colorBtns.length; i++) {
                if (clickedButton.equals(colorBtns[i])){
                    return true;
                }
            }
            return false;
        }

        // quick setup the color for pen or fill
        private void UseQuickSelecteColor(JButton clickedButton){
            if (quickColorSelectionBtn != null){
                // change the back ground of the show color btn
                Color chosenColor = clickedButton.getBackground();

                // set shown button color
                quickColorSelectionBtn.setBackground(chosenColor);

                // actual set color
                if (quickColorSelectionBtn.equals(showLineColorBtn)){
                    InitPenColor = chosenColor;

                } else if (quickColorSelectionBtn.equals(showFillColorBtn)) {
                    InitFillColor = chosenColor;

                }
            }
        }

    }

    // Method for users to use items in menu
    private class MenuItemTrigger implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem clickedItem = (JMenuItem)e.getSource();

            if (clickedItem.equals(itOpen)){
                vecConverter.Open(InitPenColor, InitFillColor, tool, graphics, theWindow);

            }else if (clickedItem.equals(itSave)){
                vecConverter.Save(record);

            }else if (clickedItem.equals(itExit)){
                System.exit(0);

            }else if (clickedItem.equals(itNew)){
                vecConverter.OpenFileInNewWindow();

            }
        }
    }

    // Method for user to draw shapes with the mouse
    private class MouseTrigger implements MouseListener, MouseMotionListener{
        private Pen pen;
        private int beginX, beginY, endX, endY;

        @Override
        public void mouseClicked(MouseEvent e) {
            switch (currentType){
                case Point: tool.AddPen(new Pen(graphics, currentType, brushSize, InitPenColor, getX(e), getY(e))); break;
                case Poly: DrawingPoly(e); break;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            StartDrawing(e);

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (isDrawing){
                switch (currentType){
                    case Line:
                        tool.AddPen(new Pen(graphics, currentType, brushSize, InitPenColor, beginX, beginY, getX(e),getY(e)));
                        break;
                    case Circle: case Ellipse: case Rectangle:
                        tool.AddPen(new Pen(graphics, currentType, brushSize, InitPenColor, InitFillColor, beginX, beginY, getX(e),getY(e)));
                        break;

                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (isDrawing && currentType!=PenType.Poly){
                CancelDrawing();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            LockBrushsize();
        }

        private int getX(MouseEvent e){
            return e.getX() + boundX;
        }

        private int getY(MouseEvent e){
            return e.getY() + boundY;
        }

        private void StartDrawing(MouseEvent e){
            if (currentType != PenType.Poly){
                isDrawing = true;
                beginX = getX(e);
                beginY = getY(e);
            }

        }

        private void CancelDrawing(){
            isDrawing = false;

            // when cancel drawing a polygon, remove the unfinished part of the polygon
            if (currentType == PenType.Poly){
                tool.Undo();
            }

        }

        private void DrawingPoly(MouseEvent e){
            // check whether or not user has started drawing poly
            if (isDrawing){
                // if he has, then add a point to the poly
                if (e.getButton() == MouseEvent.BUTTON1){
                    // for left-click function
                    pen.AddPolygonPoint(getX(e), getY(e));

                }else if (e.getButton() == MouseEvent.BUTTON3){
                    // for right-click function
                    if (pen.GetPolygonStep() >= 2){
                        isDrawing = false;
                        pen.Draw();

                    }else{
                        CancelDrawing();
                    }
                }

            }else{
                // otherwise create a polygon pen
                isDrawing = true;
                pen = new Pen(graphics,brushSize,InitPenColor,InitFillColor, getX(e), getY(e));
                tool.AddPen(pen);

            }
        }

        // Method to lock brush size
        private void LockBrushsize(){
            int min = 1;
            int max = 30;
            int size = Integer.parseInt(brushSizeEnter.getText());

            if (size > max){
                size = max;
            }else if (size < min){
                size = min;
            }

            brushSizeEnter.setText(String.valueOf(size));
            brushSize = size;
        }

    }

    // Class for all tool methods
    public class Tool{
        JColorChooser colorChooser = new JColorChooser();
        ArrayList<Object> undoHistroy = new ArrayList<>();

        // method to add pen to the record list
        // it's called when user finish drawing a shape
        public void AddPen(Pen pen){
            // add pen to record list
            record.add(pen);

            // check if has grid on
            theGrid.DrawLines();

            // cleanup undo history and set redo button disable
            undoHistroy.clear();
            RedoButton.setEnabled(false);

            // enable undo button
            UndoButton.setEnabled(true);
        }

        // method to undo the last action
        public void Undo(){
            // check to see if the record size is greater than 0
            if (record.size() > 0){
                // refresh the screen
                Refresh();

                // remove the last recorded shape
                Pen pen = (Pen)record.get(record.size()-1);
                record.remove(pen);

                // add undo request to undo history
                undoHistroy.add(pen);
                RedoButton.setEnabled(true);

                // redraw every single shape
                Redraw();

                // make undo button color gray again
                if (record.size() == 0){
                    UndoButton.setEnabled(false);
                }
            }
        }

        // method to redo the last undo
        public void Redo(){
            // check to see if the undo histroy size is greater than 0
            if (undoHistroy.size() > 0){
                // refresh the screen
                Refresh();

                // remove the last recorded shape fro undo histroy
                Pen pen = (Pen)undoHistroy.get(undoHistroy.size()-1);
                undoHistroy.remove(pen);

                // add pen to record list and enable undo button
                record.add(pen);
                UndoButton.setEnabled(true);

                // redraw every single shape
                Redraw();

                // make redo button gray again
                if (undoHistroy.size() == 0){
                    RedoButton.setEnabled(false);
                }
            }
        }

        // method to clean up the graphics
        public void Clean(){
            // refresh the screen and clear the record
            record.clear();
            Refresh();

            // disable undo and redo
            ResetUndoRedoBtns();

        }

        // disable undo and redo
        public void ResetUndoRedoBtns(){
            UndoButton.setEnabled(false);
            RedoButton.setEnabled(false);
        }

        public void ChooseLineColor(){
            InitPenColor = colorChooser.showDialog(null,"Line Color", InitPenColor);
            showLineColorBtn.setBackground(InitPenColor);
        }

        public void ChooseFillColor(){
            InitFillColor = colorChooser.showDialog(null, "Fill Color", InitFillColor);
            showFillColorBtn.setBackground(InitFillColor);
        }

        public void Update(Graphics2D newGraphics){
            graphics = newGraphics;
        }

        // Method to re paint the screen
        public void Refresh(){
            graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, 0));
            theFrame.paint(graphics);
            graphics.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, 0));
        }

        // Method to display all shapes again
        public void Redraw(){
            for (Object currentRecord : record ){
                Pen penTemp = (Pen)currentRecord;
                penTemp.Draw();
            }
        }

        // Method to turn on or off grid
        public void SwitchGrid(){
            // decide turn on or turn off
            boolean isOn;
            if (GridButton.getIcon() == gridIcon[0]){
                isOn = true;
                GridButton.setIcon(gridIcon[1]);
            }else{
                isOn = false;
                GridButton.setIcon(gridIcon[0]);
            }

            // actual turning the grid
            theGrid.EnableGrid(isOn);
        }



    }

    // Class for the grid
    private class Grid extends JFrame{
        int lines;
        boolean isOn;

        public Grid(int lines){
            // setup the base variables
            this.isOn = false;
            this.lines = lines;
            DrawLines();

        }

        // Method to enable or disable grid
        public void EnableGrid(boolean isTurningOn){
            this.isOn = isTurningOn;
            DrawLines();
        }

        public void DrawLines(){
            tool.Refresh();
            tool.Redraw();

            // check to see if the toogle is on
            if (isOn){
                // reset the stroke
                graphics.setColor(Color.black);
                graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, 0));

                // calculate the width and height
                int xMargin = windowWidth / lines;
                int yMargin = windowHeight / lines;

                for (int i = 0; i < lines; i++) {
                    // for drawing rows
                    int xPos = xMargin * i + boundX;
                    int yPos = yMargin * i + boundY;
                    graphics.drawLine(boundX,yPos,windowWidth + boundX,yPos);
                    graphics.drawLine(xPos,boundY,xPos,windowHeight + boundY);
                }

                // reset the stroke
                graphics.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, 0));
            }
        }
    }

    // Get some important private class
    public Graphics2D getPicture(){return this.graphics;}
    public Tool getTool(){return this.tool;}
    public JFrame getMainFrame(){return this.theFrame;}
    public VecConverter getVecConverter() {return vecConverter;}

    // Method for changing title
    public void ChangeTitle(String newTitle){theFrame.setTitle(newTitle);}
    public String GetTitle(){return theFrame.getTitle();}
    public void ChangeLabel(String newLabel){toolLabel.setText(newLabel);}
    public String GetLabel(){return toolLabel.getText();}

    // Method for changing or setting the pen and the fill color
    public Color getPenColor(){return this.InitPenColor;}
    public void setPenColor(Color color){InitPenColor = color; showLineColorBtn.setBackground(color);}
    public Color getFillColor(){return this.InitFillColor;}
    public void setFillColor(Color color){InitFillColor = color; showFillColorBtn.setBackground(color);}

    // Get Bounds for the canvas
    public int getBoundX(){ return boundX;}
    public int getBoundY(){ return boundY;}



}


