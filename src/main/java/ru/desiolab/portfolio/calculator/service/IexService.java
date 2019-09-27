package ru.desiolab.portfolio.calculator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.desiolab.portfolio.calculator.config.IexToken;
import ru.desiolab.portfolio.calculator.dto.CompanyInfo;
import ru.desiolab.portfolio.calculator.dto.StockQuote;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class IexService {
    private static final String BASE_URI = "https://cloud.iexapis.com/v1/";

    private final HttpClient httpClient;
    private final String token;
    private final ObjectMapper objectMapper;

    @Inject
    public IexService(@IexToken String token,
                      HttpClient httpClient,
                      ObjectMapper objectMapper) {
        this.token = token;
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
    }

    public CompanyInfo requestInfo(String symbol) {
        try {
            URI uri = URI.create(BASE_URI + "/stock/" + symbol + "/company?token=" + token);
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                throw new RuntimeException("Something wrong with iex.cloud");
            }
            return objectMapper.readValue(response.body(), CompanyInfo.class);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Double requestPrice(String symbol) {
        try {
            URI uri = URI.create(BASE_URI + "/stock/" + symbol + "/quote/price?token=" + token);
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                throw new RuntimeException("Something wrong with iex.cloud");
            }
            return objectMapper.readValue(response.body(), StockQuote.class).price();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
