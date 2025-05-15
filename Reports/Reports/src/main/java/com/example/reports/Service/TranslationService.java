/*package com.example.reports.Service;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class LibreTranslateService {

    private final String apiUrl = "https://libretranslate.de/translate"; // Public instance

    public String translateText(String text, String targetLanguage) throws Exception {
        if (text == null || text.isEmpty()) return text;

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(apiUrl);

            JSONObject json = new JSONObject();
            json.put("q", text);
            json.put("source", "en");
            json.put("target", targetLanguage);

            post.setEntity(new StringEntity(json.toString()));
            post.setHeader("Content-Type", "application/json");

            String response = client.execute(post, httpResponse ->
                    EntityUtils.toString(httpResponse.getEntity()));

            JSONObject responseJson = new JSONObject(response);
            return responseJson.getString("translatedText");
        }
    }
}*/