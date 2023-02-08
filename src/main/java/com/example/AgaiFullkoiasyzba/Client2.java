package com.example.AgaiFullkoiasyzba;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client2 extends Application {

    private static Socket clientSocket;
    private static BufferedReader input;
    private static BufferedWriter output;

    private  static int JOIN_GAME_CODE = 1;
    private  static int SUCCESS_CODE = 2;
    private  static int START_GAME_CODE = 3;
    private  static int DENY_CODE = 4;
    private  static int ANSWER_CODE = 5;
    private  static int FILLIN_CODE = 6;
    private  static int TEST_CODE = 7;

    VBox vBox;
    FlowPane flowPane;

    int millis = 0;
    int questionCount = 0;

    Scene sceneFlow;
    Scene fillinScene;

    Stage window;

    @Override
    public void start(Stage stage){
        window = stage;
        TextField name = new TextField();
        TextField code = new TextField();
        VBox.setMargin(name,new Insets(25,100,0,100));
        VBox.setMargin(code,new Insets(25,100,0,100));
        Button joinGameButton = new Button();
        joinGameButton.setText("join");
        vBox = new VBox(20);
        vBox.getChildren().addAll(name,code,joinGameButton);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-background-color: #4e0380");
        Scene scene = new Scene(vBox,500,500);
        stage.setScene(scene);
        stage.show();

        Button A = new Button();
        A.setText("A");
        Button B = new Button();
        B.setText("B");
        Button C = new Button();
        C.setText("C");
        Button D = new Button();
        D.setText("D");
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(A);
        buttons.add(B);
        buttons.add(C);
        buttons.add(D);
        flowPane = new FlowPane();
        for(Button b : buttons){
            b.setPrefSize(250,250);
            flowPane.getChildren().add(b);
        }

        sceneFlow = new Scene(flowPane,500,500);

        TextField textField = new TextField();
        Button sendFillin = new Button();
        sendFillin.setText("Send Answer");
        VBox vBox = new VBox(100);
        vBox.getChildren().add(textField);
        vBox.getChildren().add(sendFillin);
        Insets sets = new Insets(60,60,60,60);
        VBox.setMargin(textField, sets);
        vBox.setAlignment(Pos.TOP_CENTER);
        fillinScene = new Scene(vBox,500,500);

        try {
                clientSocket = new Socket("localhost", 8081);
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        }
        catch (IOException e) {
            System.err.println(e);
        }

        new Thread(()->{
            try {
                while (true) {
                    int code1 = input.read();
                    if(code1 == START_GAME_CODE) startGame();
                    else if(code1 == SUCCESS_CODE) successPin();
                    else if(code1 == DENY_CODE) denyPin();
                }
            } catch (IOException e) {
            }
        }).start();

        joinGameButton.setOnAction(event -> {
            try {
                output.write(JOIN_GAME_CODE);
                output.flush();
                output.write(name.getText()+"\n");
                output.flush();
                output.write(code.getText()+"\n");
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        int[] butin = {1};
        for(Button but : buttons){
            but.setOnAction(event -> {
                waitNextQuestion();
                try {
                    output.write(ANSWER_CODE);
                    output.write(TEST_CODE);
                    output.write(butin[0]);
                    output.write(millis);
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                butin[0] += 1;
            });
        }
        sendFillin.setOnAction(event -> {
            waitNextQuestion();
            try {
                output.write(ANSWER_CODE);
                output.write(FILLIN_CODE);
                output.write(textField.getText()+"\n");
                output.write(millis);
                output.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void successPin(){
        Platform.runLater(() -> {
            window.setScene(new Scene(new StackPane(new Button("Waiting for the game start")), 500, 500));
        });
    }
    public void denyPin(){
        if(vBox != null) {
            Platform.runLater(()->{
                vBox.getChildren().add(new Button("Incorrect PIN"));
            });
        }
    }
    public void startGame() throws IOException {
        if(flowPane != null) {
            if(input.read() == TEST_CODE) {
                millis = 10000;
                Platform.runLater(() -> {
                    window.setScene(sceneFlow);
                });
                KeyFrame keyFrame = new KeyFrame(Duration.millis(10), event -> {
                    millis--;
                });
                Timeline timeline = new Timeline(keyFrame);
                questionCount++;
                timeline.setCycleCount(millis);
                timeline.play();
            }
            else{
                millis = 10000;
                Platform.runLater(() -> {
                    window.setScene(fillinScene);
                });
                KeyFrame keyFrame = new KeyFrame(Duration.millis(10), event -> {
                    millis--;
                });
                Timeline timeline = new Timeline(keyFrame);
                questionCount++;
                timeline.setCycleCount(millis);
                timeline.play();
            }
        }
        System.out.println("start");
    }
    public void waitNextQuestion(){
        Platform.runLater(() -> {
            window.setScene(new Scene(new StackPane(new Text("Waiting next game")), 500, 500));
        });
    }
}