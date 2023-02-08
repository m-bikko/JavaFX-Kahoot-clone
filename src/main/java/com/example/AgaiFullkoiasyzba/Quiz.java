package com.example.AgaiFullkoiasyzba;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Quiz {

    protected ArrayList<Question> questions = new ArrayList<Question>();

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public static Quiz loadFromFile(String name) throws NoSuchElementException {
        Quiz quiz = new Quiz();
        try {
            File file = new File(name);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                try {
                    String descrip = sc.nextLine();
                    String otvet = sc.nextLine();
                    String otvet1 = sc.nextLine();
                    if ("".equals(otvet1)) {
                        Fillin fillin = new Fillin();
                        fillin.setDescription(descrip.replaceAll("\\{blank}","________"));
                        fillin.setAnswer(otvet);
                        quiz.addQuestion(fillin);
                    }
                    else {
                        Test test = new Test();
                        test.setDescription(descrip);
                        String[] ma = {otvet, otvet1, sc.nextLine(), sc.nextLine()};
                        String[] shuf = quiz.shuffleArray(ma);
                        test.setAnswer(otvet);
                        test.setOptions(shuf);
                        quiz.addQuestion(test);
                        sc.nextLine();
                    }
                } catch (NoSuchElementException ignored) {
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("FILE is empty!!!");
        }
        return quiz;
    }

    public String[] shuffleArray(String[] options){
        for (int i = 0; i < options.length; i++) {
            int randomIndex = (int)(Math.random()*(options.length));
            String index = options[i];
            String rindex = options[randomIndex];
            options[i] = rindex;
            options[randomIndex] = index;
        }
        return options;
    }

    public void shuffleQuestions(){
        Collections.shuffle(questions);
    }
}
