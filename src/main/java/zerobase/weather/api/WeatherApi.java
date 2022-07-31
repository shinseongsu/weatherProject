package zerobase.weather.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zerobase.weather.dto.WeatherDto;

@Slf4j
@Service
public class WeatherApi {

    @Value("${openweathermap.key}")
    private String apiKey;

    @Value("${openweathermap.url}")
    private String url;

    public WeatherDto getWeatherFromApi() throws Exception{
        String weatherString = getWeatherString();

        Map<String, Object> parseWeather = parseWeather(weatherString);

        return WeatherDto.of( parseWeather.get("main").toString(),
            parseWeather.get("icon").toString(),
            (Double) parseWeather.get("temp")
        );
    }

    private String getWeatherString() throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.addParameter("q", "seoul");
        uriBuilder.addParameter("appid", apiKey);

        try {
            URL url = uriBuilder.build().toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.getResponseCode();
            return readData(connection);
        } catch (Exception e) {
            return "failed to get response";
        }
    }

    private String readData(HttpURLConnection connection) {
        StringBuilder response = new StringBuilder();
        String inputLine;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            while((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (Exception e) {
            log.error("결과값을 읽는 도중 에러가 발생하였습니다.");
        }

        return response.toString();
    }

    private Map<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        Map<String, Object> resultMap = new HashMap<>();

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
            JSONObject mainData = (JSONObject) jsonObject.get("main");
            resultMap.put("temp", mainData.get("temp"));

            JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
            JSONObject weatherData = (JSONObject) weatherArray.get(0);
            resultMap.put("main", weatherData.get("main"));
            resultMap.put("icon", weatherData.get("icon"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return resultMap;
    }

}
