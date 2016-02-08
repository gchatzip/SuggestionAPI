package com.gchatzip.JsonSuggestionAPI;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Main {
    //hold the suggestions
    private static List<Suggestion> suggestionList;
    //csv file helpers
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "_id,Name,Type,Latitude,Longitude";



    private static void sendGet(String parameter,String fpath) throws Exception {

        String url_base = "http://api.goeuro.com/api/v2/position/suggest/en/";
        String url = url_base + parameter;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);

        if (responseCode==200) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            StringBuffer response = new StringBuffer();
            //handling the JSON response
            JSONParser parser = new JSONParser();
            Object object = parser.parse(in.readLine());
            JSONArray jsonObject = (JSONArray) object;
            in.close();
            //get the elements that we desire from the response
            Iterator it = jsonObject.iterator();
            while (it.hasNext()) { //iterator breaks every distinct element of the response to pieces.
                JSONObject parsedObj = (JSONObject) it.next();
                Long locId = (Long) parsedObj.get("_id");
                String locName = (String) parsedObj.get("name");
                String locType = (String) parsedObj.get("type");
                JSONObject geoPos = (JSONObject) parsedObj.get("geo_position");
                Double locLat = (Double) geoPos.get("latitude");
                Double locLong = (Double) geoPos.get("longitude");
                suggestionList.add(new Suggestion(locId,locName,locType,locLat,locLong));
            }
            //now generate the CSV
            generateCsvFile(fpath);
        }
        else {
            System.out.println("Something went wrong with the API call , please try again. Response code :"+responseCode);
            System.exit(2);
        }


    }

    private static void generateCsvFile(String sFileName){
        try {
            FileWriter writer = new FileWriter(sFileName);
            writer.append(FILE_HEADER.toString());
            writer.append(NEW_LINE_SEPARATOR);

            for (Suggestion suggestion :suggestionList){
                writer.append(String.valueOf(suggestion.getId()));
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(suggestion.getName()));
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(suggestion.getType()));
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(suggestion.getLatitude()));
                writer.append(COMMA_DELIMITER);
                writer.append(String.valueOf(suggestion.getLongitude()));
                writer.append(NEW_LINE_SEPARATOR);
            }
            writer.flush();
            writer.close();
            System.out.println("File generated,please find it in path:"+sFileName);

        }
        catch (IOException e){
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws Exception {

        suggestionList = new ArrayList<>();
        Path path1 = FileSystems.getDefault().getPath(System.getProperty("user.home"));
        System.out.println(path1);
        String extendedpath = path1.toString() + "\\suggestion.csv";
        System.out.println(extendedpath);
        sendGet(args[0],extendedpath);



    }
}
