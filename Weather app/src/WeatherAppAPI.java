import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;



public class WeatherAppAPI {
    //grabbing weather data for give location
    public static JSONObject getWeatherData(String locationName){
        JSONArray locationData = getLocationData(locationName);


        System.out.println("Data: " + locationData);

        //Casting JSONArray element at index 0 to JSON object to grab coordinates of city
        JSONObject location = (JSONObject) locationData.get(0);

        double latitude = (double)location.get("latitude");
        double longitude = (double)location.get("longitude");

        String weatherApiUrl = "https://api.open-meteo.com/v1/forecast?latitude=" +
                latitude + "&longitude=" + longitude + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&temperature_unit=fahrenheit&wind_speed_unit=mph&precipitation_unit=inch&timezone=America%2FChicago";


        try{
            // calling the API and getting response.
            HttpURLConnection conn = getApiResponse(weatherApiUrl);

            if (conn.getResponseCode() == 500 || conn.getResponseCode() == 400) {
                if (conn.getResponseCode() == 500) {
                    System.out.println("Error Code 500");
                    return null;
                } else {
                    System.out.println("Error Code 400");
                    return null;
                }

            } else {
                StringBuilder resultJson = new StringBuilder();
                Scanner read = new Scanner(conn.getInputStream());

                //reading JSON results and appending to a string
                while (read.hasNext()) {
                    resultJson.append(read.nextLine());
                }

                //closing scanner
                read.close();

                //disconnecting URL connection
                conn.disconnect();

                //converting string data into JSONObject
                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                //Grabbing hourly data from API
                JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

                //Each time correlate with temp, humidity, wind-speed, and weather code.
                JSONArray time = (JSONArray) hourly.get("time");
                int index = findIndexTime(time);

                JSONArray temp = (JSONArray) hourly.get("temperature_2m");
                double currTemp = (double) temp.get(index);

                JSONArray humidity = (JSONArray) hourly.get("relative_humidity_2m");
                long humidityPercent = (long) humidity.get(index);

                JSONArray windSpeed = (JSONArray) hourly.get("wind_speed_10m");
                double windSpeedMph = (double) windSpeed.get(index);

                JSONArray weather = (JSONArray) hourly.get("weather_code");
                String theWeather = weatherCode((long) weather.get(index));


                //Creating new JSONObject of our weather data that we've collected to be returned to the frontend
                JSONObject weatherData = new JSONObject();
                weatherData.put("temperature",currTemp);
                weatherData.put("weather_condition", theWeather);
                weatherData.put("humidity", humidityPercent);
                weatherData.put("wind-speed", windSpeedMph);

                return weatherData;
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        return null;
    }

    //this whole method is for grabbing the longitude and latitude of the city user picked using a Geolocation API
    public static JSONArray getLocationData(String locationName){
        //replace spaces with plus sign to fit API format
        locationName = locationName.replaceAll(" ", "+");


        String apiURL = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                locationName +
                "&count=10&language=en&format=json";

        try {
            //calling API and getting a response
            HttpURLConnection conn = getApiResponse(apiURL);

            if (conn.getResponseCode() == 500 || conn.getResponseCode() == 400) {
                if (conn.getResponseCode() == 500) {
                    System.out.println("Error Code 500");
                } else {
                    System.out.println("Error Code 400");
                }
            } else {
                StringBuilder resultJson = new StringBuilder();
                Scanner read = new Scanner(conn.getInputStream());

                //reading JSON results and appending to a string
                while (read.hasNext()) {
                    resultJson.append(read.nextLine());
                }

                //closing scanner and disconnecting URLConnection

                read.close();
                conn.disconnect();

                //turning string to an Json object using JSONParser
                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObject = (JSONObject) parser.parse(String.valueOf(resultJson));

                JSONArray locationData = (JSONArray) resultsJsonObject.get("results");
                //System.out.println(locationData);
                return locationData;

            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private static HttpURLConnection getApiResponse(String apiURL){
        try{
            //attempt making connection to API
            URL url = new URL(apiURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            conn.connect();
            return conn;
        }catch(IOException e){
            e.printStackTrace();
        }

        //no connection made.
        return null;
    }

    private static int findIndexTime(JSONArray timeList){
        String curr = getCurrentTime();

        for (int i = 0; i < timeList.size(); i++) {
            //cast timeList at index to string
            String time = (String) timeList.get(i);

            if(time.equalsIgnoreCase(curr)){
                return i;
            }
        }

        return 0;
    }


    /**
     * Gets the Current Date and formatted it into the format of the API
     * @return formatted date
     */
    private static String getCurrentTime(){
        LocalDateTime curr = LocalDateTime.now();

        //formatting date to fit API
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String formatted = curr.format(format);
        return formatted;
    }

    private static String weatherCode(long weatherCode){
        String weatherCon = "";

        if(weatherCode == 0L){
            weatherCon = "Clear";
        }else if(weatherCode > 0L && weatherCode <= 3L){
            weatherCon = "Cloudy";
        }else if((weatherCode >= 51L && weatherCode <= 67L) || (weatherCode >= 80L && weatherCode <= 77L)){
            weatherCon = "Rain";
        }else if(weatherCode >= 71L && weatherCode <= 77L){
            weatherCon = "Snow";
        }
        return weatherCon;
    }



}
