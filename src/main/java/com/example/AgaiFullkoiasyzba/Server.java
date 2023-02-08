package com.example.AgaiFullkoiasyzba;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.*;
import java.net.Socket;

import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.E;

public class Server extends Application {
    public static ArrayList<ServerClassofjOHN> serverList = new ArrayList<ServerClassofjOHN>();
    private int pass;
    static int vremia;
    static Button b1, b2, b3, b4;
    static ArrayList<ToggleButton> toggleButtons;
    static Scene sceneTest;
    static Text suraq;
    static GridPane gridPane;
    static File quizFile;
    static Timeline timeline;

    static VBox vBox;
    static int Suraqindexy = 0;
    int a = 1;
    static Button nextQuestion;

    static Quiz quiz;


    public static void main(String[] args) {
        launch();

    }

    @Override
    public void start(Stage stage) throws IOException {
        FileChooser fileChooser = new FileChooser();
        Button button = new Button("Select File");
        BorderPane BorderPane1 = new BorderPane();
        BorderPane1.setCenter(button);
        BorderPane1.setStyle("-fx-background-color: #4e0380");
        Scene scenee = new Scene(BorderPane1,150,150);
        stage.setScene(scenee);
        stage.show();

        Random random = new Random();
        pass = random.nextInt(999999);
        vBox = new VBox(50);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().add(new Text("Your PIN is: " + pass));
        vBox.setStyle("-fx-background-color: #4e0380");
        Button startGame = new Button("Start Game!");
        vBox.getChildren().add(startGame);
        Scene scene = new Scene(vBox,500,500);
        ServerSocket server = new ServerSocket(8081);

        button.setOnAction(event -> {
            quizFile = fileChooser.showOpenDialog(stage);
            stage.setScene(scene);
        });

        new Thread(()-> {
            try {
                while (true) {
                    Socket socket = server.accept();
                    try {
                        serverList.add(new ServerClassofjOHN(socket, pass));
                    } catch (IOException e) {
                        socket.close();
                    }
                }
            }
            catch (Exception e) {}
            finally {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        startGame.setOnAction(event -> {
            quiz = Quiz.loadFromFile(quizFile.getPath());
            for (ServerClassofjOHN client : Server.serverList) {
                try {
                    client.startGame();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            stage.setScene(quizBorderPane(quiz.questions.get(Suraqindexy)));
            Suraqindexy++;
        });
        nextQuestion = new Button("Next");
        nextQuestion.setOnAction(event -> {
            if(Suraqindexy != quiz.questions.size()) {
                if(a == 0) {
                    for (int i = 0; i < serverList.size();i++) {
                        try {
                            serverList.get(i).startGame();
                            serverList.get(i).otvet = false;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    stage.setScene(quizBorderPane(quiz.questions.get(Suraqindexy)));
                    Suraqindexy++;;
                    a = 1;
                }
                else if(a == 1){
                    VBox vBox2 = new VBox(30);
                    vBox2.setAlignment(Pos.TOP_CENTER);
                    nextQuestion.setAlignment(Pos.BASELINE_RIGHT);
                    vBox2.getChildren().add(nextQuestion);
                    for (int i = 0; i< serverList.size();i++) {
                        Rectangle rectangle = new Rectangle(serverList.get(i).getBall()/11,50, Color.BLUEVIOLET);
                        StackPane stackPane = new StackPane(rectangle,new Text(serverList.get(i).nameUser+"\n"+serverList.get(i).getBall()));
                        vBox2.getChildren().add(stackPane);
                    }
                    stage.setScene(new Scene(vBox2,800,800));
                    a = 0;
                }
            }
            else{
                Text top3 = new Text("Top 3 joq bez obid)))");
                VBox h = new VBox(100);
                h.getChildren().add(top3);
                for (int i = 0; i< serverList.size();i++) {
                    Rectangle rectangle = new Rectangle(80,serverList.get(i).getBall()/10, Color.GREEN);
                    StackPane stackPane = new StackPane(rectangle, new Text(serverList.get(i).nameUser+"\n"+serverList.get(i).getBall()));
                    h.getChildren().add(stackPane);
                }
                stage.setScene(new Scene(h, 1000, 1000));
            }
        });
    }

    public static Scene quizBorderPane(Question question){
        BorderPane borderPane = new BorderPane();
        suraq = new Text();
        suraq.setStyle("-fx-font-size:15");
        BorderPane.setAlignment(suraq, Pos.TOP_CENTER);
        borderPane.setTop(suraq);
        File imageFile = new File("src/main/resources/logo.png");
        Image image = new Image(imageFile.toURI().toString());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.fitHeightProperty().bind(borderPane.heightProperty().divide(1.5));

        VBox ImageBox = new VBox(25,imageView);
        ImageBox.setAlignment(Pos.TOP_CENTER);
        borderPane.setCenter(ImageBox);
        BorderPane.setAlignment(nextQuestion,Pos.TOP_CENTER);
        borderPane.setRight(nextQuestion);

        Text vrremiatext = new Text(vremia +   "");
        ArrayList<Button> bb = new ArrayList<>();
        b1 = new Button();
        b2 = new Button();
        b3 = new Button();
        b4 = new Button();
        bb.add(b1);
        bb.add(b2);
        bb.add(b3);
        bb.add(b4);
        b1.setStyle("-fx-base: red;-fx-font-size: 16;-fx-font-weight: bold");
        b2.setStyle("-fx-base: blue;-fx-font-size: 16;-fx-font-weight: bold");
        b3.setStyle("-fx-base: yellow;-fx-font-size: 16;-fx-font-weight: bold");
        b4.setStyle("-fx-base: green;-fx-font-size: 16;-fx-font-weight: bold");

        gridPane = new GridPane();

        b1.prefWidthProperty().bind(gridPane.widthProperty().divide(2));
        b1.prefHeightProperty().bind(gridPane.widthProperty().divide(12));
        b1.setWrapText(true);
        b2.prefWidthProperty().bind(gridPane.widthProperty().divide(2));
        b2.prefHeightProperty().bind(gridPane.widthProperty().divide(12));
        b2.setWrapText(true);
        b3.prefWidthProperty().bind(gridPane.widthProperty().divide(2));
        b3.prefHeightProperty().bind(gridPane.widthProperty().divide(12));
        b3.setWrapText(true);
        b4.prefWidthProperty().bind(gridPane.widthProperty().divide(2));
        b4.prefHeightProperty().bind(gridPane.widthProperty().divide(12));
        b4.setWrapText(true);


        gridPane.add(b1,0,0);
        gridPane.add(b2,1,0);
        gridPane.add(b3,0,1);
        gridPane.add(b4,1,1);
        gridPane.setPadding(new Insets(5));
        if(question instanceof Test test) {
            suraq.setText(test.getDescription());
            b1.setText(test.getOptionAt(0));
            b2.setText(test.getOptionAt(1));
            b3.setText(test.getOptionAt(2));
            b4.setText(test.getOptionAt(3));

        }else if (question instanceof Fillin fillin){
            suraq.setText(fillin.getDescription());
            b1.setVisible(false);
            b2.setVisible(false);
            b3.setVisible(false);
            b4.setVisible(false);
        }
        borderPane.setBottom(gridPane);

        vremia = 60000;
        timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            vremia -= 100;
            int result = (int)Math.floor(vremia /1000);
            vrremiatext.setText(String.valueOf(result));
            if(vremia == 0){
                if(question instanceof Test) {
                    for (int i = 0; i < 4; i++) {
                        if (bb.get(i).getText().equals(question.getAnswer())) {
                            bb.get(i).setVisible(true);
                            bb.get(i).setStyle("-fx-base: green");
                        } else {
                            bb.get(i).setVisible(false);
                        }
                    }
                    timeline.stop();
                }
                else{
                    Text texte = new Text();
                    texte.setStyle("-fx-font-size: 28");
                    borderPane.setBottom(new StackPane(texte));
                    texte.setText("Answer " +question.getAnswer());
                    timeline.stop();
                }
            }
            boolean otvet = true;
            for (int i = 0; i< serverList.size();i++) {
                if(!serverList.get(i).otvet) otvet = false;
            }
            if(otvet){
                if(question instanceof Test) {
                    for (int i = 0; i < 4; i++) {
                        if (bb.get(i).getText().equals(question.getAnswer())) {
                            bb.get(i).setVisible(true);
                            bb.get(i).setStyle("-fx-base: green");
                        } else {
                            bb.get(i).setVisible(false);
                        }
                    }
                    timeline.stop();
                }
                else{
                    Text text = new Text();
                    text.setStyle("-fx-font-size: 28");
                    borderPane.setBottom(new StackPane(text));
                    text.setText("Correct answer is: " +question.getAnswer());
                    timeline.stop();
                }
            }
        }));
        borderPane.setLeft(vrremiatext);
        BorderPane.setAlignment(vrremiatext, Pos.TOP_CENTER);
        vrremiatext.setStyle("-fx-font-size: 20");
        timeline.setCycleCount(vremia /100);
        timeline.play();
        sceneTest = new Scene(borderPane,800,600);
        return sceneTest;
    }


}