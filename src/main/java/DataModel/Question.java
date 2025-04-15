package DataModel;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int ID;
    private String type;
    private String difficulty;
    private String category;
    private String questionTitle;
    private String correctAnswer;
    private List<String> wrongAnswers = new ArrayList<>();

    public Question(int ID, String type, String difficulty, String category, String questionTitle, String correctAnswer, List<String> wrongAnswers) {
        this.ID = ID;
        this.type = type;
        this.difficulty = difficulty;
        this.category = category;
        this.questionTitle = questionTitle;
        this.correctAnswer = correctAnswer;
        this.wrongAnswers = wrongAnswers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "ID=" + ID +
                ", type='" + type + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", category='" + category + '\'' +
                ", questionTitle='" + questionTitle + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", wrongAnswers=" + wrongAnswers +
                '}';
    }

}
