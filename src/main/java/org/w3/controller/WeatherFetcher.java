package org.w3.controller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;

public class WeatherFetcher {


    private static final String DB_URL = "jdbc:postgresql://localhost:5432/weatherApp";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Shuxrat@190903";

    public static JSONArray getRecordFromDB() throws SQLException {
        JSONArray recordList = new JSONArray();

        Connection connectionDB = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        String sqlQuery = "select * from weather_data";
        PreparedStatement preparedStatement = connectionDB.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            JSONObject record = new JSONObject();
            record.put("city", resultSet.getString("city"));
            record.put("temperature", resultSet.getDouble("temperature"));
            record.put("humidity", resultSet.getDouble("humidity"));
            record.put("feeling", resultSet.getDouble("temperature"));
            record.put("wind", resultSet.getDouble("wind"));
            record.put("pressure", resultSet.getDouble("pressure"));
            record.put("weather", resultSet.getString("weather"));


            recordList.put(record);
        }

        return recordList;
    }


    public static void main(String[] args) throws SQLException {
        System.out.println(getRecordFromDB().toString());
    }

}



