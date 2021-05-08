import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    //c8d6ec6c602f7c6fcfcd99c67d71d1ac
    public static String getWeather(String message, Model model) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=c8d6ec6c602f7c6fcfcd99c67d71d1ac");
        Scanner scanner = new Scanner((InputStream) url.getContent());
        String result = "";
        while (scanner.hasNext()) {
            result += scanner.nextLine();
        }
        System.out.println(result);
        JSONObject jsonObject = new JSONObject(result);
        model.setName(jsonObject.getString("name"));
        JSONObject main = jsonObject.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));
        JSONArray jsonArray = jsonObject.getJSONArray("weather");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            model.setIcon((String) obj.get("icon"));
            model.setMain((String) obj.get("main"));
        }

        return "City " + model.getName() + "\nTemp " + model.getTemp() + "^c" + " Humidity" + model.getHumidity() + "%" + "\n"
                + "http://openweathermap.org/img/wn/" + model.getIcon() + "@2x.png";
    }
}
