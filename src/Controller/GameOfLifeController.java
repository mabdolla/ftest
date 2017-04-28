package Controller;

import FileHandler.FileReader;
import FileHandler.FileReaderRLE;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Board.DynamicBoard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Optional;

/**
 * The Game Of Life application created for HIOA
 * The Controller class is the fx for fxml, all the features in fxml are assigned in this class.
 * The class is also implementing Initializable interface.
 *
 * @author Fredrik, Hans-Jacob, Mohammad
 *         Studentnr : S309293,
 */
public class GameOfLifeController implements Initializable {

    @FXML
    private ColorPicker colorpickercell;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Button StartStopBtn;
    @FXML
    private Slider celleSlider;
    @FXML
    private Slider sliderSpeed;
    @FXML
    public Canvas canvas;

    public Timeline timeline = new Timeline();
    public GraphicsContext gc;
    DynamicBoard dynamicBoard;


    /**
     * Constructs and initializes the canvas and application features.
     *
     * @param location  .
     * @param resources .
     */

    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

        gc = canvas.getGraphicsContext2D();
        dynamicBoard = new DynamicBoard(90, 80, gc, canvas);
        //MoveBoardkey();

        colorpickercell.setValue(Color.BLACK);
        colorPicker.setValue(Color.AQUA);
        draw();

        celleSlider.setValue(dynamicBoard.getCellSize());
        celleSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            dynamicBoard.setCellSize((int)celleSlider.getValue());
            if ((double)newValue < (double)oldValue) {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }
            draw();
        }));

        KeyFrame frame = new KeyFrame(Duration.millis(500), event -> {
            dynamicBoard.nextGeneration();
            celleSlider.getValue();
            draw();
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * This method allows the user to upload RLE file containing board pattern from computer disk.
     */
    @FXML
    public void RLEopen() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("RLE", "*.rle"));

        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            FileReaderRLE filereaderRle = new FileReaderRLE(file);
            dynamicBoard.setBrett(filereaderRle.brett);
            dynamicBoard.setRules(filereaderRle.rules);

            draw();

        } else {
            alertBox();
        }
    }

    /**
     * This method allows the user to upload RLE board pattern from URL link.
     */
    @FXML
    public void URLopen() throws IOException {

        TextInputDialog dialog = new TextInputDialog("//");
        dialog.setTitle("URL FileReader");
        dialog.setHeaderText("Copy and paste url adress here:");
        dialog.setContentText("URL:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            System.out.println("URL-adress: " + result.get());
            //lage til File og sende til FileReaderRLE
            URL website = new URL(result.get());
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());

            FileOutputStream fos = new FileOutputStream("tempPattern.rle");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            File file = new File("tempPattern.rle");

            FileReaderRLE filereaderRle = new FileReaderRLE(file);
            dynamicBoard.setBrett(filereaderRle.brett);//eventuelt, sette disse tre linjene inne i FileReaderRle
            dynamicBoard.setRules(filereaderRle.rules);
            draw();

        } else {
            alertBox();
        }
    }

    //////////////////////////////////Moving array with eventhandler////////////////////////
    public void MoveBoardkey() {
        canvas.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(javafx.scene.input.KeyEvent event) {
                moveBoard(event);
            }
        });
    }

    public void moveBoard(KeyEvent event) {
        {
            switch (event.getCode()) {
                case UP:
                    //moveCanvas(event);
                    break;
                case DOWN:
                    //moveCanvas(event);
                    break;
                case A:

                    dynamicBoard.moveCellsLeft(event);
                    break;
                case RIGHT:

                    //moveCanvas(event);
                    break;
            }
            draw();
        }
    }

    /**
     * This method allows the user to upload txt file containing board pattern.
     */
    @FXML
    public void openFile(Event e) {

        int[][] nyBrett = FileReader.openTXTfile();

        /*for (int x = 0; x < nyBrett.length; x++) {
            for (int y = 0; y < nyBrett[0].length; y++) {
                //dynamicBoard.getBrett()[x][y] = nyBrett[x][y];
            }
        }*/

        dynamicBoard.setBrett(nyBrett);
        draw();
    }


    /**
     * This method allows user to change the color for dead cells.
     */
    @FXML
    public void changecolor(ActionEvent e) {
        dynamicBoard.setBackgroundColor(colorPicker.getValue());
        draw();
    }

    /**
     * This method allows userinput to change the background color with input from colorpicker.
     *
     * @param c is choosing color
     */
    @FXML
    public void changeColorCell(ActionEvent c) {
        dynamicBoard.setCellColor(colorpickercell.getValue());
        draw();
    }

    /**
     * This method allows user to clear canvas and creates a new empty board.
     */
    public void resetBoard() {
        gc = canvas.getGraphicsContext2D();
        dynamicBoard = new DynamicBoard(dynamicBoard.getRows(), dynamicBoard.getColumns(), gc, canvas);
        draw();
    }

    /**
     * This method allows user to click start/stop button in the application.
     */
    @FXML
    public void startSimulation() {
        if (timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.stop();
            StartStopBtn.setText("Start");
        } else {
            timeline.play();
            StartStopBtn.setText("Stop");
        }
    }

    @FXML
    public void AboutGameofLife() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Game of Life");
        alert.setContentText("About Game of Life" +
                "+dccdc" +
                "+cdcdc" +
                "+dcdcd" +
                "+dcdcd" +
                "+dcdcdc" +
                "+dcdccd" +
                "+cdcdcdcd");

        alert.showAndWait();

    }

    /**
     * This method allows user to change the gamespeed.
     */
    @FXML
    public void AdjustSpeed() {

        timeline.setRate(sliderSpeed.getValue());
    }

    /**
     * This method draws the background to the canvas.
     */
    public void background() {
        gc.setFill(Color.WHITE);

        gc.fillRect(0, 0, dynamicBoard.getRows() * dynamicBoard.getCellSize() - 1, dynamicBoard.getColumns() * dynamicBoard.getCellSize() - 1);
    }

    /**
     * This method draws the grid/cells on canvas.
     */
    public void draw() throws ArrayIndexOutOfBoundsException {

        try {
            background();
            for (int j = 0; j < dynamicBoard.getRows() && j < canvas.getHeight(); j++) {
                for (int i = 0; i < dynamicBoard.getColumns() && i < canvas.getWidth(); i++) {
                    if (dynamicBoard.getValue(j, i) == 1) {
                        gc.setFill(colorpickercell.getValue());

                    } else {
                        gc.setFill(colorPicker.getValue());
                    }
                    int CellSize = dynamicBoard.getCellSize();
                    gc.fillRect(j * CellSize, i * CellSize, CellSize - 1, CellSize - 1);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {


        }
    }

    int oldChangeX;
    int getOldChangeY;

    /**
     * This method allows the user to fill rectangles on canvas with alive or deadcells.
     */
    @FXML
    public void userDrawCell(MouseEvent e) throws Exception {

        try {
            int x = (int) (e.getX() / dynamicBoard.getCellSize());
            int y = (int) (e.getY() / dynamicBoard.getCellSize());
            if (x == oldChangeX && y == getOldChangeY)
                return;

                if (x < dynamicBoard.getRows() && y < dynamicBoard.getColumns()) {
                    boolean value = dynamicBoard.getValue(x, y) == 0;
                    dynamicBoard.setValue(x, y, value ? 1 : 0); //ternary operator
                    oldChangeX = x;
                    getOldChangeY = y;
                    draw();
                }
        } catch (Exception f) {
            alertBox();
        }
    }

    /**
     * This method allows the user to fill rectangles on canvas with alive or deadcells.
     */
    @FXML
    public void userDrawCellClicked(MouseEvent e) throws Exception {
        if (e.isDragDetect())
            return;
        try {

            int x = (int) (e.getX() / dynamicBoard.getCellSize());
            int y = (int) (e.getY() / dynamicBoard.getCellSize());

            if (x < dynamicBoard.getRows() && y < dynamicBoard.getColumns()) {
                if (dynamicBoard.getValue(x, y) == 1) {
                    dynamicBoard.setValue(x, y, 1);
                    draw();
                }

            } else if (x < dynamicBoard.getRows() && y < dynamicBoard.getColumns()) {
                if (dynamicBoard.getValue(x, y) == 0) {
                    dynamicBoard.setValue(x, y, 0);
                    draw();
                }
            }
        } catch (Exception err) {
            System.out.print("Out of bounds");
        }
    }

    /**
     * This method creates a alertbox.
     */
    public void alertBox() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Game of Life");
        alert.setContentText("Something went wrong, please try again!");

        alert.showAndWait();
    }

}

