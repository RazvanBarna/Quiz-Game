package DataAccess;

import com.sun.security.auth.UnixNumericGroupPrincipal;

import javax.print.attribute.SetOfIntegerSyntax;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
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

    public List<String> getCategories(Connection connection){
        List<String> categories = new ArrayList<>();
        String sql = "select CategoryName from category";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                String category = rs.getString("CategoryName");
                categories.add(category);
            }
        }catch (Exception e ){
            e.printStackTrace();
            System.out.println("err at categories");
        }
        return categories;
    }

    public static void main(String[] argv){
        MyDataBase myDataBase = new MyDataBase();
        Connection connection = myDataBase.connectToMyDb();
        myDataBase.getCategories(connection);
        APIConnection.getQuestions(9);
    }
}
