package org.w3;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DataToDatabase
{
    private static final String API_KEY = "102cc8c59aef1058806f15466fde4f44";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/weatherApp";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Shuxrat@190903";

    // fetching
    private static JSONObject fetchWeatherData(String city) throws IOException {
        String apiURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";
        URL url = new URL(apiURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return new JSONObject(response.toString());

    }

    // Method to insert weather data into PostgreSQL database
    private static void insertWeatherData(JSONObject weatherData) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "INSERT INTO weather_data (city, temperature, humidity, feeling, wind, pressure, weather) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, weatherData.getString("name"));
            preparedStatement.setDouble(2, weatherData.getJSONObject("main").getDouble("temp"));
            preparedStatement.setDouble(3, weatherData.getJSONObject("main").getDouble("humidity"));
            preparedStatement.setDouble(4, weatherData.getJSONObject("main").getDouble("feels_like"));
            preparedStatement.setDouble(5, weatherData.getJSONObject("wind").getDouble("speed"));
            preparedStatement.setDouble(6, weatherData.getJSONObject("main").getDouble("pressure"));
            preparedStatement.setString(7, weatherData.getJSONArray("weather").getJSONObject(0).getString("main"));
            preparedStatement.execute();

        }
    }


    public static void main( String[] args ) {
        try {
            // Fetch weather data from the API
            String city = "Plymouth"; // Change this to your desired city
            JSONObject weatherData = fetchWeatherData(city);
            // Insert weather data into the database
            insertWeatherData(weatherData);
            System.out.println("Weather data inserted successfully!");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }



    }

}
