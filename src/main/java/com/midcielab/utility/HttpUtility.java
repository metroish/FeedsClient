package com.midcielab.utility;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpUtility {

    private static HttpUtility instance = new HttpUtility();
    private HttpClient httpClient;
    private HttpRequest httpRequest;
    private HttpResponse<String> httpResponse;
    private static final int TIMEOUT = 60;
    private static final Logger logger = LogManager.getLogger();

    private HttpUtility() {
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(TIMEOUT)).version(Version.HTTP_2)
                .followRedirects(Redirect.NORMAL).build();
    }

    public static HttpUtility getInstance() {
        return instance;
    }

    public Optional<HttpResponse<String>> getUrlContent(String url) {
        try {
            this.httpRequest = HttpRequest.newBuilder(new URI(url)).GET().timeout(Duration.ofSeconds(TIMEOUT))
                    .header("User-Agent", ConfigUtility.getInsance().getConfig().getAgent()).build();
            this.httpResponse = this.httpClient.send(this.httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Send HTTP GET request fail on ( {} )", url, e);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Send HTTP GET request fail on ( {} )", url, e);
        }
        return Optional.ofNullable(this.httpResponse);
    }

    public Optional<HttpResponse<String>> sendLineAPI(String url, String httpBody, String token) {
        try {
            this.httpRequest = HttpRequest.newBuilder(new URI(url)).POST(HttpRequest.BodyPublishers.ofString(httpBody))
                    .headers("content-type", "application/x-www-form-urlencoded", "Authorization", "Bearer " + token)
                    .timeout(Duration.ofSeconds(TIMEOUT)).build();
            this.httpResponse = this.httpClient.send(this.httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Send Line API fail on ( {} )", url, e);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Send Line API fail on ( {} )", url, e);
        }
        return Optional.ofNullable(this.httpResponse);
    }

}
