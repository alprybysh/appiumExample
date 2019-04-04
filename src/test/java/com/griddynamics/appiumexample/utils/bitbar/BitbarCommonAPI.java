package com.griddynamics.appiumexample.utils.bitbar;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BitbarCommonAPI {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String API_BITBAR_URL = "https://cloud.bitbar.com";

    public static String createProject(String testdroid_apikey, String platformName) {
        String projectName;

        try {
            final HttpHeaders headers = new HttpHeaders().setBasicAuthentication(testdroid_apikey, "");
            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory((request) -> {
                request.setHeaders(headers);
                request.setParser(new JsonObjectParser(JSON_FACTORY));
            });

            Map<String, String> body = new HashMap<>();
            body.put("type", platformName);
            HttpContent content = new UrlEncodedContent(body);

            HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(API_BITBAR_URL + "/api/v2/me/projects"), content);
            projectName = request.execute().parseAs(BitbarProject.class).name;
        } catch (IOException ex) {
            throw new RuntimeException("New project can't be created");
        }
        return projectName;
    }

    public static class BitbarProject {
        @Key("name")
        String name;
    }
}
