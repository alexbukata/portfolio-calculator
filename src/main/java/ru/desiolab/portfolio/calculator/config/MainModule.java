package ru.desiolab.portfolio.calculator.config;

import com.google.inject.AbstractModule;

import java.net.http.HttpClient;

public class MainModule extends AbstractModule {

    private final String iexToken;

    public MainModule(String iexToken) {
        this.iexToken = iexToken;
    }

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(IexToken.class).toInstance(iexToken);
        bind(HttpClient.class).toInstance(HttpClient.newBuilder().build());
    }
}
