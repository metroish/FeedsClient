package com.midcielab.utility;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

public class HttpUtility {

    private static HttpUtility instance = new HttpUtility();
    private HttpClient httpClient;
    private HttpRequest httpRequest;
    private HttpResponse<String> httpResponse;

    private final int connectTimeout = 60;
    private final Version HTTP2 = HttpClient.Version.HTTP_2;
    
    private HttpUtility() {
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(connectTimeout)).version(HTTP2)
        .build();
    }    

    public static HttpUtility getInstance() {
        return instance;
    }

    public Optional<HttpResponse<String>> sendRequest(String url, String httpMethod, String httpBody) {
        try {
            this.httpRequest = HttpRequest.newBuilder(new URI(url))
                    .method(httpMethod, HttpRequest.BodyPublishers.ofString(httpBody))
                    .timeout(Duration.ofSeconds(connectTimeout)).build();
            this.httpResponse = this.httpClient.send(this.httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(this.httpResponse);
    }
}
