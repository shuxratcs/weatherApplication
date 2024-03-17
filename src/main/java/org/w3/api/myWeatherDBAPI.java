package org.w3.api;

import org.json.JSONArray;
import spark.Request;
import spark.Response;
import static spark.Spark.*;
import org.json.JSONArray;
import org.w3.controller.WeatherFetcher;

import java.sql.SQLException;

public class myWeatherDBAPI {

    private static String getWeatherDataAsJson(Request req, Response res) {
        JSONArray jsonArray = new JSONArray();

        try {
            jsonArray = WeatherFetcher.getRecordFromDB();
        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
            return "Internal Server Error";
        }
        res.type("application/json");
        System.out.println(jsonArray.toString());
        return jsonArray.toString();
    }

    private static String getJSONdata(Request request, Response response) throws SQLException {
        JSONArray recordList = new JSONArray();
        recordList = WeatherFetcher.getRecordFromDB();
        response.type("application/json");
        return recordList.toString();



    }

    public static void main(String[] args) {
        port(8080);

        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept");
            res.header("Access-Control-Allow-Credentials", "true");
        });

        get("/weather", (req, res) -> getWeatherDataAsJson(req, res));

    }



}
