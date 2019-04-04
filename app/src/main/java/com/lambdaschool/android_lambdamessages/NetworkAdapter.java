package com.lambdaschool.android_lambdamessages;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class NetworkAdapter {

    public static final String REQUEST_GET = "GET";
    public static final String REQUEST_POST = "POST";
    public static final String REQUEST_PUT = "PUT";
    public static final String REQUEST_DELETE = "DELETE";
    private static final int READ_TIMEOUT = 3000;
    private static final int CONNECT_TIMEOUT = 3000;

    public static String httpRequest(String url) {
        return httpRequest(url, REQUEST_GET, null, null);
    }

    public static String httpRequest(String url, String requestMethod) {
        return httpRequest(url, requestMethod, null, null);
    }

    public static String httpRequest(String url, String requestMethod, JSONObject requestBody, Map<String, String> headerProperties) {
        String httpResult = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL urlObject = new URL(url);
            httpURLConnection = (HttpURLConnection) urlObject.openConnection();
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpURLConnection.setRequestMethod(requestMethod);

            if (headerProperties != null) {
                for (Map.Entry<String, String> property : headerProperties.entrySet()) {
                    httpURLConnection.setRequestProperty(property.getKey(), property.getValue());
                }
            }

            switch (requestMethod) {
                case REQUEST_POST:
                case REQUEST_PUT:
                    if (requestBody != null) {
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        outputStream.write(requestBody.toString().getBytes());
                        outputStream.close();
                        break;
                    }
                default:
                    httpURLConnection.connect();
                    break;
            }

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                if (inputStream != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String singleLine;
                    while ((singleLine = bufferedReader.readLine()) != null) {
                        stringBuilder.append(singleLine);
                    }
                    httpResult = stringBuilder.toString();
                }
            } else {
                throw new IOException("Connection failed (" + responseCode + ")");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return httpResult;
    }
}
