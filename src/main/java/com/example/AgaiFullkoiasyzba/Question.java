package com.example.AgaiFullkoiasyzba;

abstract class Question {
    private String description;
    private String answer;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "description='" + description + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
