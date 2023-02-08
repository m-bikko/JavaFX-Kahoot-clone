package com.example.AgaiFullkoiasyzba;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.*;
import java.net.Socket;

import static com.example.AgaiFullkoiasyzba.Server.vBox;

public class ServerClassofjOHN extends Thread {

    Button but[] = new Button[4];
    int JOIN_GAME_CODE = 1;
    int SUCCESS_CODE = 2;
    int START_GAME_CODE = 3;
    int DENY_CODE = 4;
    int ANSWER_CODE = 5;
    int FILLIN_CODE = 6;
    int TEST_CODE = 7;

    public int[] answers;
    public boolean otvet;

    Socket socket;
    BufferedReader input;
    BufferedWriter output;

    private int pinCode;
    String nameUser;


    public ServerClassofjOHN(Socket socket, int pinCode) throws IOException {
        this.socket = socket;
        this.pinCode = pinCode;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                int code1 = input.read();
                System.out.println(code1);
                if(code1 == JOIN_GAME_CODE) {
                    joinGame();

                }
                else if(code1 == ANSWER_CODE) {
                    checkAnswer();
                }
            }
        } catch (IOException e) {
        }
    }

    private void joinGame() throws IOException {
        nameUser = input.readLine();
        int outPinCode = Integer.parseInt(input.readLine());
        if(outPinCode == pinCode) {
            output.write(SUCCESS_CODE);
            output.flush();
            Platform.runLater(() -> {
                Text text = new Text();
                text.setText(nameUser);
                vBox.getChildren().add(text);
            });
        }
        else {
            output.write(DENY_CODE);
            output.flush();
        }
    }

    private void send(int msg){
        try {
            output.write(msg);
            output.flush();
        } catch (IOException ignored) {
        }
    }

    public void startGame() throws IOException {
        if(answers == null){
            answers = new int[Server.quiz.questions.size()];
        }
        send(START_GAME_CODE);
        if(Server.quiz.questions.get(Server.Suraqindexy) instanceof Test){
            send(TEST_CODE);
        }else if(Server.quiz.questions.get(Server.Suraqindexy) instanceof Fillin){
            send(FILLIN_CODE);
        }
    }

    public void checkAnswer() throws IOException {
        int test1 = input.read();
        if(test1 == TEST_CODE) {
            int index = input.read();
            int millis = input.read();
            otvet = true;
            but[0] = Server.b1;
            but[1] = Server.b2;
            but[2] = Server.b3;
            but[3] = Server.b4;

            if (Server.quiz.questions.get(Server.Suraqindexy - 1) instanceof Test test) {
                for (int i = 1; i < 5; i++) {
                    if(index == i-1 && but[i-1].getText().equals(test.getAnswer())){
                        answers[Server.Suraqindexy - 1] = millis / 10;
                    }
                }
            }
        }else{
            //propuskaet liniu
            String propusklinii = input.readLine();
            int millis = input.read();
            if (Server.quiz.questions.get(Server.Suraqindexy - 1) instanceof Fillin fillin) {
                if (Server.quiz.questions.get(Server.Suraqindexy - 1).equals(fillin.getAnswer())) {
                    answers[Server.Suraqindexy - 1] = millis / 10;
                }
            }
            otvet = true;
        }
    }

    public int getBall(){
        int ball = 0;
        for (int i = 0; i < answers.length; i++) {
            if(answers[i] != 0){
                ball += answers[i];
            }
        }
        System.out.println("fori");
        return ball;
    }
}