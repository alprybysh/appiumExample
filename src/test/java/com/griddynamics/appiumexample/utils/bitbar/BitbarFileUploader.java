package com.griddynamics.appiumexample.utils.bitbar;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;
import com.testdroid.api.http.MultipartFormDataContent;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class BitbarFileUploader {
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final Logger LOG = Logger.getLogger(BitbarFileUploader.class.getName());

    public static String uploadFile(String targetAppPath, String serverURL, String testdroid_apikey)
            throws IOException {

        final HttpHeaders headers = new HttpHeaders().setBasicAuthentication(testdroid_apikey, "");
        HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory((request) -> {
            request.setParser(new JsonObjectParser(JSON_FACTORY));
            request.setHeaders(headers);

        });
        MultipartFormDataContent multipartContent = new MultipartFormDataContent();
        FileContent fileContent = new FileContent("application/octet-stream", new File(targetAppPath));

        MultipartFormDataContent.Part filePart = new MultipartFormDataContent.Part("file", fileContent);
        multipartContent.addPart(filePart);

        HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(serverURL + "/upload"), multipartContent);
        AppiumResponse appiumResponse = request.execute().parseAs(AppiumResponse.class);

        LOG.info("response: " + appiumResponse.uploadStatus.message);
        LOG.info("File id: " + appiumResponse.uploadStatus.fileInfo.file);
        return appiumResponse.uploadStatus.fileInfo.file;
    }

    public static class AppiumResponse {
        Integer status;
        @Key("sessionId")
        String sessionId;

        @Key("value")
        UploadStatus uploadStatus;
    }

    public static class UploadedFile {
        @Key("file")
        String file;
    }

    public static class UploadStatus {
        @Key("message")
        String message;

        @Key("uploadCount")
        Integer uploadCount;

        @Key("expiresIn")
        Integer expiresIn;

        @Key("uploads")
        UploadedFile fileInfo;
    }
}
