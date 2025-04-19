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
                preparedStatement2.setString(1, correctAnswer); // SETEZI parametrul aici
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

                preparedStatementMain.executeUpdate();


            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("err at insert");
        }
    }

    public void insertAllCategories(Connection connection, APIConnection apiConnection){
        List<Category> categories = this.getCategories(connection);
        for(Category category : categories){
            List<Question>  questions = apiConnection.getQuestions(category.getID() +8 );
            insertInDBquestions(questions,connection);
        }
    }

    public static void main(String[] argv){
        MyDataBase myDataBase = new MyDataBase();
        Connection connection = myDataBase.connectToMyDb();
        APIConnection apiConnection = new APIConnection();
        myDataBase.insertAllCategories(connection,apiConnection);
    }
}
