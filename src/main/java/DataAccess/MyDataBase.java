package DataAccess;

import DataModel.Category;
import DataModel.Question;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MyDataBase {

    public Connection connectToMyDb(){
        String url = "jdbc:mysql://localhost:3306/quizdatabase";
        String userName = "root";
        String password = "root";
        try {
            Connection connection = DriverManager.getConnection(url,userName,password);

            return connection;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection err");
        }
        return null;
    }

    public List<Category> getCategories(Connection connection){
        List<Category> categories = new ArrayList<>();
        String sql = "select CategoryName,CategoryID from category";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                int categoryID = rs.getInt("CategoryID");
                String categoryName = rs.getString("CategoryName");
                categories.add(new Category(categoryID,categoryName));
            }
        }catch (Exception e ){
            e.printStackTrace();
            System.out.println("err at categories");
        }
        return categories;
    }

    private void insertInDBquestions(List<Question> questions,Connection connection){
        String insertCorrect = "INSERT INTO CorrectAnswers (CorrectAnswerName) VALUES (?)";
        String checkSql = "SELECT CorrectAnswerID FROM CorrectAnswers WHERE CorrectAnswerName = ?";
        String sql = "INSERT INTO Question(TypeID, DifficultyID, CategoryID, QuestionTitle , CorrectAnswerID ) VALUES(" +
                "(SELECT TypeID FROM Type WHERE TypeName = ?), (SELECT DifficultyID FROM Difficulty where DifficultyName = ?), (SELECT CategoryID FROM Category WHERE CategoryName = ?)," +
                " ? , (SELECT CorrectAnswerID FROM CorrectAnswers WHERE CorrectAnswerName = ?))";

        try{

            PreparedStatement preparedStatement = connection.prepareStatement(insertCorrect);
            PreparedStatement preparedStatementMain = connection.prepareStatement(sql);
            for(Question question : questions){
                String correctAnswer = question.getCorrectAnswer();
                PreparedStatement preparedStatement2 = connection.prepareStatement(checkSql);
                preparedStatement2.setString(1, correctAnswer);

                insertQuestion(connection,checkSql,correctAnswer,preparedStatement,preparedStatementMain,question,preparedStatement2);

                preparedStatementMain.executeUpdate();
                String getId= "SELECT QuestionID FROM Question where QuestionTitle = ?";
                PreparedStatement preparedStatementId = connection.prepareStatement(getId);
                preparedStatementId.setString(1,question.getQuestionTitle());
                ResultSet rsGetId = preparedStatementId.executeQuery();
                int id = 0;
                if(rsGetId.next()){
                     id = Integer.parseInt(rsGetId.getString("QuestionID"));
                }
                Question questionAux = new Question(id,question.getType(),question.getDifficulty(),question.getCategory(),question.getQuestionTitle(),question.getCorrectAnswer(),question.getWrongAnswers());
                String wrongInsert = "INSERT INTO WrongAnswers (WrongAnswerName,QuestionID) VALUES (?, ?) ";
                insertWrongAnswers(connection,questionAux, wrongInsert);


            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("err at insert");
        }
    }

    private void insertQuestion(Connection connection,String checkSql, String correctAnswer,PreparedStatement preparedStatement,PreparedStatement preparedStatementMain,Question question,PreparedStatement preparedStatement2) throws Exception{

        ResultSet rs = preparedStatement2.executeQuery();
        if (!rs.next()) {
            preparedStatement.setString(1, correctAnswer);
            preparedStatement.executeUpdate();
        }

        preparedStatementMain.setString(1,question.getType());
        preparedStatementMain.setString(2,question.getDifficulty());
        preparedStatementMain.setString(3,question.getCategory());
        preparedStatementMain.setString(4,question.getQuestionTitle());
        preparedStatementMain.setString(5,question.getCorrectAnswer());
    }

    public void insertAllCategories(Connection connection, APIConnection apiConnection){
        try {
            List<Category> categories = this.getCategories(connection);
            for (Category category : categories) {
                List<Question> questions = apiConnection.getQuestions(category.getID() + 8);
                insertInDBquestions(questions, connection);
                Thread.sleep(5000);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("err sleep");
        }

    }

    private void insertWrongAnswers(Connection connection,Question question,String sql) throws Exception{
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (String wrongAnswer : question.getWrongAnswers()) {
            preparedStatement.setInt(2, question.getID());
            preparedStatement.setString(1, wrongAnswer);
            preparedStatement.executeUpdate();
        }
    }

    public List<Question> getRandomQuestions(int count) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT q.QuestionID, q.QuestionTitle, q.TypeID, q.DifficultyID, q.CategoryID, ca.CorrectAnswerName, " +
                "t.TypeName, d.DifficultyName, c.CategoryName " +
                "FROM Question q " +
                "JOIN Type t ON q.TypeID = t.TypeID " +
                "JOIN Difficulty d ON q.DifficultyID = d.DifficultyID " +
                "JOIN Category c ON q.CategoryID = c.CategoryID " +
                "JOIN CorrectAnswers ca ON q.CorrectAnswerID = ca.CorrectAnswerID " +
                "ORDER BY RAND() LIMIT ?";
        try (Connection conn = connectToMyDb();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, count);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int questionId = rs.getInt("QuestionID");
                String questionTitle = rs.getString("QuestionTitle");
                String correctAnswer = rs.getString("CorrectAnswerName");
                String type = rs.getString("TypeName");
                String difficulty = rs.getString("DifficultyName");
                String category = rs.getString("CategoryName");

                // get wrong answers
                List<String> wrongAnswers = new ArrayList<>();
                String wrongSql = "SELECT WrongAnswerName FROM WrongAnswers WHERE QuestionID = ?";
                try (PreparedStatement wrongStmt = conn.prepareStatement(wrongSql)) {
                    wrongStmt.setInt(1, questionId);
                    ResultSet wrongRs = wrongStmt.executeQuery();
                    while (wrongRs.next()) {
                        wrongAnswers.add(wrongRs.getString("WrongAnswerName"));
                    }
                }

                Question question = new Question(questionId, type, difficulty, category, questionTitle, correctAnswer, wrongAnswers);
                questions.add(question);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching random questions");
        }

        return questions;
    }

}
