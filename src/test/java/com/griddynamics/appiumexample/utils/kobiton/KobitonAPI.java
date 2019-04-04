package com.griddynamics.appiumexample.utils.kobiton;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class KobitonAPI {

    private static final String ENDPOINT = "https://api.kobiton.com/v1/devices";

    private static boolean isDeviceAvailable(KobitonDevice device, String platform) {
        return device.getIsBooked().equals("false") &&
                device.getIsHidden().equals("false") &&
                device.getIsOnline().equals("true") &&
                device.getPlatformName().equals(platform);
    }

    private static HttpURLConnection getConnection() {
        HttpURLConnection con;
        try {
            URL obj = new URL(ENDPOINT);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization",
                    "Basic Y2FtZWxlb25hODoxNjdjNWQ2Zi04N2IwLTRkMjUtYWU4ZS05MTgzMmE5NTc5ODA=");
        } catch (IOException ex) {
            throw new RuntimeException("Can't create connection " + ex.toString());
        }
        return con;
    }

    private static String getAllDevices() {
        HttpURLConnection connection = getConnection();
        StringBuffer response;
        try (BufferedReader rd = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = rd.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            throw new RuntimeException("API call 'getAllDevices' failed with exception: " + e.toString());
        }
        return response.toString();
    }

    public static KobitonDevice getAvailableDevice(String platform) {
        String response = getAllDevices();
        ObjectMapper objectMapper = new ObjectMapper();
        KobitonListDevices containerList;
        try {
            containerList = objectMapper.readValue(response, KobitonListDevices.class);
        } catch (IOException e) {
            throw new RuntimeException("JSON can't be mapped to object: " + e.toString());
        }
        KobitonDevice kobitonDevice = containerList.getDevices().stream()
                .filter(device -> isDeviceAvailable(device, platform))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No available device"));
        return kobitonDevice;
    }
}
