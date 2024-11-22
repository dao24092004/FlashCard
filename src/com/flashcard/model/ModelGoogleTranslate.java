package com.flashcard.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ModelGoogleTranslate {

    public static String translate(String text, String sourceLang, String targetLang) throws Exception {
        System.out.println("Start translate with text: " + text + ", sourceLang: " + sourceLang + ", targetLang: " + targetLang);
//
//        String url = "https://translate.google.com/m?hl=" + sourceLang + "&sl=" + sourceLang + "&tl=" + targetLang
//                + "&ie=UTF-8&q=" + URLEncoder.encode(text, StandardCharsets.UTF_8);

        String url = "https://translate.google.com/m?hl=" + sourceLang + "&sl=" + sourceLang + "&tl=" + targetLang + "&ie=UTF-8&q=" + text;

        System.out.println("Translate URL: " + url);

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3")
                    .timeout(60000) // Timeout 15 giây
                    .get();

            System.out.println("Connected to Translate URL successfully!");

            Elements elements = doc.select("div.result-container");

            if (elements.isEmpty()) {
                System.err.println("No result found in HTML response!");
                System.out.println("HTML response for debugging: \n" + doc.html());
                throw new Exception("Không tìm thấy phần tử kết quả dịch trong HTML.");
            }

            String result = elements.first().text();
            System.out.println("Translation result: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("Error during translation:");
            e.printStackTrace(); // In lỗi chi tiết
            throw e;
        }
    }

    public static List<String> getSuggestions(String word) throws Exception {
        System.out.println("Start getSuggestions with word: " + word);

        String encodedWord = URLEncoder.encode(word, StandardCharsets.UTF_8.toString());
        String apiUrl = "https://api.datamuse.com/words?sl=" + encodedWord + "&max=10";

        System.out.println("API URL: " + apiUrl);

        HttpURLConnection conn = null;
        try {
            URL url = new URL(apiUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }

            JSONArray jsonArray = new JSONArray(response.toString());
            List<String> suggestions = new ArrayList<>();
            for (int i = 0; i < Math.min(10, jsonArray.length()); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                suggestions.add(obj.getString("word"));
            }
            System.out.println("Parsed suggestions: " + suggestions);
            return suggestions;
        } catch (Exception e) {
            System.err.println("Error while connecting to API or parsing response:");
            e.printStackTrace();
            throw e;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static List<String> getSuggestions(String word, String sourceLang, String targetLang) throws Exception {
        System.out.println("Start getSuggestions with translation, word: " + word);

        List<String> suggestions = getSuggestions(word);
        List<String> exampleSentences = new ArrayList<>();

        for (String suggestion : suggestions) {
            try {
                String translatedWord = translate(suggestion, sourceLang, targetLang);
                String example = generateExample(suggestion, translatedWord, sourceLang);
                exampleSentences.add(example);
            } catch (Exception e) {
                System.err.println("Error translating or generating example for: " + suggestion);
            }
        }

        return exampleSentences;
    }

    private static String generateExample(String word, String translatedWord, String sourceLang) {
        Random random = new Random();

        if (word.endsWith("ing") || word.endsWith("ed")) {
            String[] verbExamples = {
                    "I am " + word + " (" + translatedWord + ") every day to improve myself.",
                    "He has been " + word + " (" + translatedWord + ") for hours without rest.",
                    "They enjoy " + word + " (" + translatedWord + ") in their free time."
            };
            return verbExamples[random.nextInt(verbExamples.length)];
        }

        if (word.endsWith("ly")) {
            String[] adverbExamples = {
                    "She speaks very " + word + " (" + translatedWord + ") during the presentation.",
                    "The car moves " + word + " (" + translatedWord + ") on the slippery road.",
                    "He answered the question " + word + " (" + translatedWord + "), surprising everyone."
            };
            return adverbExamples[random.nextInt(adverbExamples.length)];
        }

        if (word.endsWith("ion") || word.endsWith("ment") || word.endsWith("ness")) {
            String[] nounExamples = {
                    "The " + word + " (" + translatedWord + ") is crucial for success.",
                    "His " + word + " (" + translatedWord + ") impressed the entire team.",
                    "They discussed the " + word + " (" + translatedWord + ") during the meeting."
            };
            return nounExamples[random.nextInt(nounExamples.length)];
        }

        if (word.endsWith("ful") || word.endsWith("ous") || word.endsWith("ive")) {
            String[] adjectiveExamples = {
                    "This place is very " + word + " (" + translatedWord + ") in spring.",
                    "She gave a " + word + " (" + translatedWord + ") performance last night.",
                    "The solution was simple yet " + word + " (" + translatedWord + ")."
            };
            return adjectiveExamples[random.nextInt(adjectiveExamples.length)];
        }

        String[] defaultExamples = {
                "I find the word '" + word + "' (" + translatedWord + ") quite interesting.",
                "The meaning of '" + word + "' (" + translatedWord + ") is worth exploring.",
                "Learning the word '" + word + "' (" + translatedWord + ") was fun!"
        };
        return defaultExamples[random.nextInt(defaultExamples.length)];
    }
}
