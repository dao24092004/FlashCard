package com.flashcard.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
public class GoogleTranslate {
	
	 

	
	
	public static List<String> getSuggestions(String word) throws Exception {
	    String apiUrl = "https://api.datamuse.com/words?ml=" + word + "&max=10";  // 'ml' tìm các từ có liên quan, tương tự
	    URL url = new URL(apiUrl);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Accept", "application/json");

	    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    StringBuilder response = new StringBuilder();
	    String output;
	    while ((output = br.readLine()) != null) {
	        response.append(output);
	    }
	    conn.disconnect();

	    JSONArray jsonArray = new JSONArray(response.toString());
	    List<String> suggestions = new ArrayList<>();
	    for (int i = 0; i < Math.min(10, jsonArray.length()); i++) {
	        JSONObject obj = jsonArray.getJSONObject(i);
	        suggestions.add(obj.getString("word"));
	    }

	    return suggestions;
	}

	
	

}



