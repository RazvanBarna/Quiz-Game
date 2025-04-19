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

    public  List<Question> getQuestions(int categoryID) {
        try {
            StringBuilder stringBuilder = readLinesFromAPi(categoryID);
            JsonParser parser = new JsonParser();
            Object readL = parser.parse(stringBuilder.toString());
            JsonObject jsonObject = new JsonObject();
            if(readL instanceof JsonObject){
                jsonObject = (JsonObject) readL;
                int code = Integer.parseInt(jsonObject.get("response_code").toString()); // dont need this
                JsonArray questionsArray = (JsonArray) jsonObject.get("results");
                List<Question> questions = parseObjectsFromArray(questionsArray);
                return questions;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("API Problem");
        }
        return null;
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

    private List<Question> parseObjectsFromArray(JsonArray array) {
        List<Question> questions = new ArrayList<>();
        for (JsonElement element : array) {
            if (element instanceof JsonObject) {
                JsonObject obj = (JsonObject) element;

                String type = obj.get("type").getAsString();
                String difficulty = obj.get("difficulty").getAsString();
                String category = obj.get("category").getAsString();
                String questionText = obj.get("question").getAsString();
                String correctAnswer = obj.get("correct_answer").getAsString();

                List<String> wrongAnswers = new ArrayList<>();
                JsonArray arrayOfWrong = obj.get("incorrect_answers").getAsJsonArray();
                for (JsonElement wrongEl : arrayOfWrong) {
                    wrongAnswers.add(wrongEl.getAsString());
                }

                Question q = new Question(0, type, difficulty, category, questionText, correctAnswer, wrongAnswers);
                questions.add(q);
            }
        }
        return questions;
    }
}
