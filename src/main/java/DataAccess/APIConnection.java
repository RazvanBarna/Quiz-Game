package DataAccess;

import DataModel.Question;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.print.attribute.standard.OutputBin;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class APIConnection {

    public  void getQuestions(int categoryID) {
        try {
            StringBuilder stringBuilder = readLinesFromAPi(categoryID);
            JsonParser parser = new JsonParser();
            Object readL = parser.parse(stringBuilder.toString());
            JsonObject jsonObject = new JsonObject();
            if(readL instanceof JsonObject){
                jsonObject = (JsonObject) readL;
                int code = Integer.parseInt(jsonObject.get("response_code").toString());
                System.out.println(code);
                JsonArray questionsArray = (JsonArray) jsonObject.get("results");
                parseObjectsFromArray(questionsArray);
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("API Problem");
        }
    }

    private  StringBuilder readLinesFromAPi(int categoryID) throws Exception{
        URL url = new URL("https://opentdb.com/api.php?amount=50&category=" + categoryID);
        HttpURLConnection  urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String line;
        StringBuilder stringBuilder =new StringBuilder();
        while((line=bufferedReader.readLine()) != null){
            stringBuilder.append(line);
        }

        return stringBuilder;
    }

    private  List<Question> parseObjectsFromArray(JsonArray array){
        List<Question> questions = new ArrayList<>();
        for(JsonElement element : array){
            if(element instanceof JsonObject){
                String type = String.valueOf(((JsonObject) element).get("type"));
                String difficulty = String.valueOf(((JsonObject) element).get("difficulty"));
                String category = String.valueOf(((JsonObject) element).get("category"));
                String question = String.valueOf(((JsonObject) element).get("question"));
                String correctAnswer = String.valueOf(((JsonObject) element).get("correct_answer"));
                List<String> wrongAnswers = new ArrayList<>();
                JsonArray arrayOfWrong = (JsonArray) ((JsonObject) element).get("incorrect_answers");
                for (JsonElement jsonElement : arrayOfWrong) {
                    String wrong = jsonElement.getAsString();
                    wrongAnswers.add(wrong);
                }
            }
        }
    }
}
