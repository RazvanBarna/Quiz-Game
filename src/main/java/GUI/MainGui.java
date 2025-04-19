package GUI;

import DataModel.Question;
import DataAccess.MyDataBase;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class MainGui extends Application {

    private int correctAnswersCount = 0;
    private int currentQuestionIndex = 0;
    private List<Question> questions;
    private VBox root;
    private Label feedbackLabel;
    private Label scoreLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MyDataBase db = new MyDataBase();
        questions = db.getRandomQuestions(50);

        root = new VBox(20);
        root.setStyle("-fx-padding: 20px;");

        feedbackLabel = new Label();
        feedbackLabel.getStyleClass().add("feedback-label");
        scoreLabel = new Label("Correct Answers: 0");
        scoreLabel.getStyleClass().add("score-label");

        root.getChildren().addAll(scoreLabel, feedbackLabel);

        Scene scene = new Scene(root, 1000, 1000);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setTitle("Quiz Application");
        primaryStage.setScene(scene);
        primaryStage.show();

        loadQuestion();
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            Label questionLabel = new Label(question.getQuestionTitle());
            questionLabel.getStyleClass().add("label-question");

            ToggleGroup group = new ToggleGroup();
            RadioButton[] answerButtons = new RadioButton[question.getWrongAnswers().size() + 1];
            answerButtons[0] = new RadioButton(question.getCorrectAnswer());
            answerButtons[0].getStyleClass().add("radio-button");
            answerButtons[0].setToggleGroup(group);

            for (int i = 0; i < question.getWrongAnswers().size(); i++) {
                answerButtons[i + 1] = new RadioButton(question.getWrongAnswers().get(i));
                answerButtons[i + 1].getStyleClass().add("radio-button");
                answerButtons[i + 1].setToggleGroup(group);
            }

            Button submitButton = new Button("Answer:");
            submitButton.getStyleClass().add("button");
            submitButton.setOnAction(e -> {
                RadioButton selectedAnswer = (RadioButton) group.getSelectedToggle();
                if (selectedAnswer != null) {
                    checkAnswer(selectedAnswer.getText(), question.getCorrectAnswer());
                }
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.size()) {
                    loadQuestion();
                } else {
                    showFinalScore();
                }
            });

            VBox questionBox = new VBox(10);
            questionBox.getChildren().add(questionLabel);
            questionBox.getChildren().addAll(answerButtons);
            questionBox.getChildren().add(submitButton);

            root.getChildren().clear();
            root.getChildren().addAll(scoreLabel, feedbackLabel, questionBox);
        }
    }

    private void checkAnswer(String userAnswer, String correctAnswer) {
        if (userAnswer.equals(correctAnswer)) {
            correctAnswersCount++;
            showCorrectFeedback();
        } else {
            showIncorrectFeedback();
        }

        scoreLabel.setText("Correct Answers: " + correctAnswersCount);
    }

    private void showCorrectFeedback() {
        feedbackLabel.setText("Correct!");
        feedbackLabel.setStyle("-fx-text-fill: green; -fx-font-size: 20px;");

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), new KeyValue(feedbackLabel.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(1), new KeyValue(feedbackLabel.opacityProperty(), 0))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void showIncorrectFeedback() {
        feedbackLabel.setText("Wrong!");
        feedbackLabel.setStyle("-fx-text-fill: red; -fx-font-size: 20px;");

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), new KeyValue(feedbackLabel.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(1), new KeyValue(feedbackLabel.opacityProperty(), 0))
        );
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void showFinalScore() {
        Label finalScoreLabel = new Label("You answered correctly to " + correctAnswersCount + " questions!");
        finalScoreLabel.getStyleClass().add("final-score-label");

        root.getChildren().clear();
        root.getChildren().add(finalScoreLabel);
    }
}
