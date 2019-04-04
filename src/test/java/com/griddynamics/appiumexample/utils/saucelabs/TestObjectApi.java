package com.griddynamics.appiumexample.utils.saucelabs;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.File;
import java.io.IOException;

public class TestObjectApi {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final String serverURL = "https://app.testobject.com/api/storage/upload";


    public static String uploadApp(String targetAppPath, String username, String apiKey) throws IOException {
        final HttpHeaders headers = new HttpHeaders().setBasicAuthentication(username, apiKey);

        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                request.setParser(new JsonObjectParser(JSON_FACTORY));
                request.setHeaders(headers);
            }
        });

        String filepath= targetAppPath;
        File initialFile = new File(filepath);

        HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(serverURL),
                new FileContent("application/octet-stream", initialFile));

        String response = request.execute().parseAsString();
        return response;
    }
}
