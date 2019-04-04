package com.griddynamics.appiumexample.utils.browserstack;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Logger;

public class BrowserstackAPI {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final Logger LOG = Logger.getLogger(BrowserstackAPI.class.getName());
    private static final String serverURL = "https://api-cloud.browserstack.com/app-automate/upload";


    public static String uploadApp(String targetAppPath, String browserstack_username, String browserstack_accesskey) throws IOException {
        final HttpHeaders headers = new HttpHeaders().setBasicAuthentication(browserstack_username, browserstack_accesskey);

        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                request.setParser(new JsonObjectParser(JSON_FACTORY));
                request.setHeaders(headers);
            }
        });

        String filepath= targetAppPath;
        File initialFile = new File(filepath);
        String fileName= initialFile.getName();
        LOG.info("Filename is: " + fileName );
        InputStream inputStream = new FileInputStream(initialFile);


        MultipartContent.Part part = new MultipartContent.Part()
                .setContent(new InputStreamContent("application/", inputStream))
                .setHeaders(new HttpHeaders().set(
                        "Content-Disposition",
                        String.format("form-data; name=\"file\"; filename=\"%s\"", fileName)
                ));
        MultipartContent content = new MultipartContent()
                .setMediaType(new HttpMediaType("multipart/form-data").setParameter("boundary", UUID.randomUUID().toString()))
                .addPart(part);

        HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(serverURL), content);

        BrowserstackAPI.Response response = request.execute().parseAs(BrowserstackAPI.Response.class);

        LOG.info("response: " + response.appUrl);

        return response.appUrl;
    }

    public static class Response {
        @Key("app_url")
        String appUrl;
    }
}
