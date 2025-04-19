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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public List<String> getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(List<String> wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }
}
