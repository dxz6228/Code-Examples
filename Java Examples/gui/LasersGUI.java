package gui;

import backtracking.Backtracker;
import backtracking.Configuration;
import backtracking.SafeConfig;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import model.*;

/**
 * The main class that implements the JavaFX UI.   This class represents
 * the view/controller portion of the UI.  It is connected to the model
 * and receives updates from it.
 *
 * @author Sean Strout @ RIT CS
 * @author Denis Zhenilov
 * @author Austin Richards
 */
public class LasersGUI extends Application implements Observer {
    /** The UI's connection to the model */
    private LasersModel model;

    private Label msg;
    private VBox safeChart;
    private String safeFile;
    private List<Configuration> solution;
    private BorderPane window;

    /** this can be removed - it is used to demonstrates the button toggle */
    private static boolean status = true;

    @Override
    public void init() throws Exception {
        // the init method is run before start.  the file name is extracted
        // here and then the model is created.
        try {
            Parameters params = getParameters();
            String filename = params.getRaw().get(0);
            this.model = new LasersModel(filename);
            this.msg=new Label(filename+" loaded");
            this.safeFile=filename;
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
    }

    /**
     * A private utility function for setting the background of a button to
     * an image in the resources subdirectory.
     *
     * @param button the button control
     * @param bgImgName the name of the image file
     */
    private void setButtonBackground(Button button, String bgImgName) {
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image( getClass().getResource("resources/" + bgImgName).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        button.setBackground(background);
    }

    /**
     * This is a private demo method that shows how to create a button
     * and attach a foreground image with a background image that
     * toggles from yellow to red each time it is pressed.
     *
     * @param stage the stage to add components into
     */
    private void buttonDemo(Stage stage) {
        // this demonstrates how to create a button and attach a foreground and
        // background image to it.
        Button button = new Button();
        Image laserImg = new Image(getClass().getResourceAsStream("resources/laser.png"));
        ImageView laserIcon = new ImageView(laserImg);
        button.setGraphic(laserIcon);
        setButtonBackground(button, "yellow.png");
        button.setOnAction(e -> {
            // toggles background between yellow and red
            if (!status) {
                setButtonBackground(button, "yellow.png");
            } else {
                setButtonBackground(button, "red.png");
            }
            status = !status;
        });

        Scene scene = new Scene(button);
        stage.setScene(scene);
    }

    private void handleCell(int y, int x){
        if(this.model.getSafe()[y][x].equals("L")){
            this.model.remove(y,x);
        }
        else {
            this.model.add(y,x);
        }
    }

    /**
     * The
     * @param stage the stage to add UI components into
     */
    private void init(Stage stage) {
        this.window=new BorderPane();
        window.setTop(this.msg);
        this.safeChart=new VBox();
        window.setCenter(this.safeChart);
        this.msg.setAlignment(Pos.CENTER);
        this.safeChart.setAlignment(Pos.CENTER);
        int dimY=this.model.getDimY();
        int dimX=this.model.getDimX();
        int xCounter=0;
        int yCounter=0;
        while (dimY>0){
            HBox row=new HBox();
            while(dimX>0){
                Button cell=new Button();
                cell.setMaxWidth(30.0);
                cell.setMinWidth(30.0);
                cell.setMaxHeight(30.0);
                cell.setMinHeight(30.0);
                int xCoord=xCounter;
                int yCoord=yCounter;
                cell.setOnAction((ActionEvent event) -> handleCell(yCoord,xCoord));
                xCounter++;
                String cellText=this.model.getSafe()[yCoord][xCoord];
                switch(cellText){
                    case "X":
                        Image pillarXImg = new Image(getClass().getResourceAsStream("resources/pillarX.png"));
                        ImageView pillarXIcon = new ImageView(pillarXImg);
                        cell.setGraphic(pillarXIcon);
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"white.png");
                        }
                        break;
                    case "0":
                        Image pillar0Img = new Image(getClass().getResourceAsStream("resources/pillar0.png"));
                        ImageView pillar0Icon = new ImageView(pillar0Img);
                        cell.setGraphic(pillar0Icon);
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"white.png");
                        }
                        break;
                    case "1":
                        Image pillar1Img = new Image(getClass().getResourceAsStream("resources/pillar1.png"));
                        ImageView pillar1Icon = new ImageView(pillar1Img);
                        cell.setGraphic(pillar1Icon);
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"white.png");
                        }
                        break;
                    case "2":
                        Image pillar2Img = new Image(getClass().getResourceAsStream("resources/pillar2.png"));
                        ImageView pillar2Icon = new ImageView(pillar2Img);
                        cell.setGraphic(pillar2Icon);
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"white.png");
                        }
                        break;
                    case "3":
                        Image pillar3Img = new Image(getClass().getResourceAsStream("resources/pillar3.png"));
                        ImageView pillar3Icon = new ImageView(pillar3Img);
                        cell.setGraphic(pillar3Icon);
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"white.png");
                        }
                        break;
                    case "4":
                        Image pillar4Img = new Image(getClass().getResourceAsStream("resources/pillar4.png"));
                        ImageView pillar4Icon = new ImageView(pillar4Img);
                        cell.setGraphic(pillar4Icon);
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"white.png");
                        }
                        break;
                    case "L":
                        Image laserImg = new Image(getClass().getResourceAsStream("resources/laser.png"));
                        ImageView laserIcon = new ImageView(laserImg);
                        cell.setGraphic(laserIcon);
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"yellow.png");
                        }
                        break;
                    case "*":
                        Image beamImg = new Image(getClass().getResourceAsStream("resources/beam.png"));
                        ImageView beamIcon=new ImageView(beamImg);
                        cell.setGraphic(beamIcon);
                        break;
                    default:
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"white.png");
                        }
                        break;
                }
                row.getChildren().add(cell);
                row.setMargin(cell,new Insets(0.0,3.0,3.0,0.0));
                dimX--;
            }
            dimX=this.model.getDimX();
            this.safeChart.getChildren().add(row);
            xCounter=0;
            yCounter++;
            dimY--;
        }

        HBox buttons=new HBox();
        Button checkBtn=new Button("Check");
        Button hintBtn = new Button("Hint");
        Button solveBtn=new Button("Solve");
        solveBtn.setOnAction((ActionEvent e) -> {
            Optional<Configuration> sol= new Backtracker(false).solve(new SafeConfig(this.model));
            if(sol.isPresent()){
                SafeConfig fin = (SafeConfig) sol.get();
                this.model.copy(fin.accessModel());
                this.msg.setText(this.safeFile+" solved!");
            }
            else {
                this.msg.setText(this.safeFile+" has no solution!");
            }
        });
        Button restartBtn=new Button("Restart");
        restartBtn.setOnAction((ActionEvent event) -> {
            try{
                LasersModel resetModel=new LasersModel(this.safeFile);
                this.model.errorHandled();
                this.model.copy(resetModel);
                this.msg.setText(this.safeFile+" reloaded.");
            }
            catch (FileNotFoundException e){
                System.out.println("Could not find the starting file! SOMETHING IS HORRIBLY WRONG.");
            }
        });
        checkBtn.setOnAction((ActionEvent event) -> this.model.verify());
        hintBtn.setOnAction((ActionEvent event) -> {
            this.solution=new Backtracker(false).solveWithPath(new SafeConfig(this.model));
            if(this.solution!=null){
                SafeConfig step=(SafeConfig) this.solution.get(0);
                LasersModel n=new LasersModel(step.accessModel());
                LasersModel oldModel=new LasersModel(this.model);
                this.model.errorHandled();
                this.model.copy(n);
                this.solution.remove(0);
                this.msg.setText("Hint: "+oldModel.findDiff(this.model));
            }
            else {
                this.msg.setText("Hint: no next step!");
            }
        });
        Button loadBtn=new Button("Load");
        loadBtn.setOnAction((ActionEvent e) -> {
            FileChooser loader = new FileChooser();
            loader.setTitle("Select safe configuration file.");
            File file=loader.showOpenDialog(stage);
            try {
                if(file!=null){
                    LasersModel n=new LasersModel(file.getPath());
                    this.model.errorHandled();
                    this.model.copy(n);
                    stage.setMinWidth( 36*this.model.getDimX()*1.7 );
                    stage.setMinHeight( 36*this.model.getDimY()*1.7 );
                    stage.setMaxWidth( 36*this.model.getDimX()*1.7);
                    stage.setMaxHeight( 36*this.model.getDimY()*1.7 );
                    this.safeFile=file.getPath();
                    this.msg.setText(file.getName()+" loaded.");
                }
            }
            catch (FileNotFoundException f){
                this.msg.setText("Could not open the file.");
                System.out.println(f);
            }
        });
        buttons.getChildren().addAll(checkBtn,hintBtn,solveBtn,restartBtn,loadBtn);
        window.setBottom(buttons);
        buttons.setAlignment(Pos.BOTTOM_CENTER);

        safeChart.setPadding(new Insets(0.0,0.0,5.0,0.0));

        Scene scene=new Scene(window);
        stage.setMinWidth( 36*this.model.getDimX()*1.7 );
        stage.setMinHeight( 36*this.model.getDimY()*1.7 );
        stage.setMaxWidth( 36*this.model.getDimX()*1.7 );
        stage.setMaxHeight( 36*this.model.getDimY()*1.7 );
        stage.setScene(scene);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init(primaryStage);  // do all your UI initialization here

        primaryStage.setTitle("Lasers");
        primaryStage.show();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg!=null){
            this.msg.setText((String) arg);
        }
        int yCounter=0;
        int xCounter=0;
        this.safeChart.getChildren().clear();
        while(yCounter<this.model.getDimY()){
            HBox row=new HBox();
            xCounter=0;
            while (xCounter<this.model.getDimX()){
                Button cell = new Button();
                cell.setMaxWidth(30.0);
                cell.setMinWidth(30.0);
                cell.setMaxHeight(30.0);
                cell.setMinHeight(30.0);
                int xCoord=xCounter;
                int yCoord=yCounter;
                cell.setOnAction((ActionEvent event) -> handleCell(yCoord,xCoord));
                String cellText=this.model.getSafe()[yCoord][xCoord];
                switch(cellText){
                    case "X":
                        Image pillarXImg = new Image(getClass().getResourceAsStream("resources/pillarX.png"));
                        ImageView pillarXIcon = new ImageView(pillarXImg);
                        cell.setGraphic(pillarXIcon);
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"white.png");
                        }
                        break;
                    case "0":
                        Image pillar0Img = new Image(getClass().getResourceAsStream("resources/pillar0.png"));
                        ImageView pillar0Icon = new ImageView(pillar0Img);
                        cell.setGraphic(pillar0Icon);
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"white.png");
                        }
                        break;
                    case "1":
                        Image pillar1Img = new Image(getClass().getResourceAsStream("resources/pillar1.png"));
                        ImageView pillar1Icon = new ImageView(pillar1Img);
                        cell.setGraphic(pillar1Icon);
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"white.png");
                        }
                        break;
                    case "2":
                        Image pillar2Img = new Image(getClass().getResourceAsStream("resources/pillar2.png"));
                        ImageView pillar2Icon = new ImageView(pillar2Img);
                        cell.setGraphic(pillar2Icon);
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"white.png");
                        }
                        break;
                    case "3":
                        Image pillar3Img = new Image(getClass().getResourceAsStream("resources/pillar3.png"));
                        ImageView pillar3Icon = new ImageView(pillar3Img);
                        cell.setGraphic(pillar3Icon);
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"white.png");
                        }
                        break;
                    case "4":
                        Image pillar4Img = new Image(getClass().getResourceAsStream("resources/pillar4.png"));
                        ImageView pillar4Icon = new ImageView(pillar4Img);
                        cell.setGraphic(pillar4Icon);
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"white.png");
                        }
                        break;
                    case "L":
                        Image laserImg = new Image(getClass().getResourceAsStream("resources/laser.png"));
                        ImageView laserIcon = new ImageView(laserImg);
                        cell.setGraphic(laserIcon);
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"yellow.png");
                        }
                        break;
                    case "*":
                        Image beamImg = new Image(getClass().getResourceAsStream("resources/beam.png"));
                        ImageView beamIcon=new ImageView(beamImg);
                        cell.setGraphic(beamIcon);
                        break;
                    default:
                        if(this.model.getErrorState() && yCounter==this.model.getProblematicCoordY() && xCounter==this.model.getProblematicCoordX()){
                            setButtonBackground(cell,"red.png");
                            this.model.errorHandled();
                        }
                        else {
                            setButtonBackground(cell,"white.png");
                        }
                        break;
                }
                row.getChildren().add(cell);
                row.setMargin(cell,new Insets(0.0,3.0,3.0,0.0));
                xCounter++;
            }
            this.safeChart.getChildren().add(row);
            yCounter++;
        }
        safeChart.setPadding(new Insets(0.0,0.0,5.0,0.0));
        this.msg.setAlignment(Pos.CENTER);
        this.safeChart.setAlignment(Pos.CENTER);
    }
}
