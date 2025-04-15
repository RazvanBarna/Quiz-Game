package DataAccess;

import com.sun.security.auth.UnixNumericGroupPrincipal;

import java.sql.Connection;
import java.sql.DriverManager;

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

    public static void main(String[] argv){
        MyDataBase myDataBase = new MyDataBase();
        myDataBase.connectToMyDb();
    }
}
