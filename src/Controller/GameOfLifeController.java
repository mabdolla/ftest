package Controller;

import FileHandler.FileReader;
import FileHandler.FileReaderRLE;
import FileHandler.FileReaderURL;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.junit.Test;
import sample.Board.Brett;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;


public class GameOfLifeController implements Initializable {

    @FXML public Canvas canvas;
    @FXML private Slider celleSlider;
    @FXML private Slider sliderSpeed;
    @FXML private Button StartStopBtn;
    @FXML private ColorPicker colorPicker;
    @FXML private ColorPicker colorpickercell;
    public GraphicsContext gc;
    Brett brett;
    public Timeline timeline = new Timeline();
    FileReaderRLE f2 = new FileReaderRLE();
    FileReaderURL f3 = new FileReaderURL();




    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {

        gc = canvas.getGraphicsContext2D();


        brett = new Brett(900, 400, gc, canvas);


        brett.setBackgroundColor(Color.AQUA);
        brett.setCellColor(Color.BLACK);

        brett.draw();


        celleSlider.setValue(canvas.getWidth()/brett.getCelleSTR()/canvas.getHeight()/brett.getCelleSTR());
        sliderSpeed.setValue(10);

        celleSlider.valueProperty().addListener(((observable, oldValue, newValue) -> {
            brett.setCelleSTR((int) celleSlider.getValue());
//            gc.clearRect(0,0,canvas.getWidth(),canvas.getWidth());
            brett.draw();
        }));

        KeyFrame frame = new KeyFrame(Duration.millis(500), event -> {
            brett.nextGeneration();
            brett.draw();
        });
        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);
    }



    @FXML
    public void RLEopen() {
        f2.readBoard();
        brett.setBrett(f2.brett);
        brett.setRules(f2.rules);
        brett.draw();
    }

    @FXML
    public void URLopen(){
        f3.readBoardURL();
        brett.setBrett(f3.brett);
        brett.setRules(f3.rules);
        brett.draw();

    }

    @FXML
    public void openFile(Event e) {

        int[][] nyBrett = FileReader.openTXTfile();

        for (int x = 0; x < nyBrett.length; x++) {
            for (int y = 0; y < nyBrett[0].length; y++) {
                brett.getBrett()[x][y] = nyBrett[x][y];
            }
        }
        brett.draw();
    }

    @FXML
    public void changecolor (ActionEvent e){
        brett.setBackgroundColor(colorPicker.getValue());
        brett.draw();


    }

    @FXML
    public void changeColorCell (ActionEvent c){
        brett.setCellColor(colorpickercell.getValue());
        brett.draw();
    }

    public void clearBoard() {
        brett.setBrett(new int[brett.getRad()][brett.getKolonne()]);
        brett.draw();
    }

    //Next generation button show only next generation at a time
    @FXML
    public void startAnimation() {
        brett.nextGeneration();
        brett.draw();
    }

    //Start & Stop button
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
    public void AdjustSpeed() {
        timeline.setRate(sliderSpeed.getValue());
    }

    @FXML
    public void userDrawCell() {
        canvas.setOnMouseDragged(e -> {
            int x = (int) (e.getX() / brett.getCelleSTR());
            int y = (int) (e.getY() / brett.getCelleSTR());

            if (x < brett.getBrett().length && y < brett.getBrett()[0].length) {
                if (brett.getBrett()[x][y] == 1) {
                    brett.getBrett()[x][y] = 1;
                    brett.draw();

                } else {
                    brett.getBrett()[x][y] = 1;
                    brett.draw();
                }
            }
        });
    }


    @FXML
    public void userDrawCellClicked() {
        canvas.setOnMouseClicked(e -> {
            int x = (int) (e.getX() / brett.getCelleSTR());
            int y = (int) (e.getY() / brett.getCelleSTR());

            if (brett.getBrett()[x][y] == 1) {
                brett.getBrett()[x][y] = 0;
                brett.draw();

            } else {
                brett.getBrett()[x][y] = 1;
                brett.draw();
            }
        });
    }
}

