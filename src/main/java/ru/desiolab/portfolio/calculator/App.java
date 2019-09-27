package ru.desiolab.portfolio.calculator;

import com.google.inject.Module;
import io.jooby.Jooby;
import io.jooby.MediaType;
import io.jooby.di.GuiceModule;
import io.jooby.json.JacksonModule;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.desiolab.portfolio.calculator.controller.PortfolioCostController;
import ru.desiolab.portfolio.calculator.handler.CalculatePortfolioHandler;

public class App extends Jooby {

    private final Config config;

    public App(Config config) {
        this.config = config;
    }

    public void init() {
        initModules();
        initHandlers();
    }

    private void initModules() {
        install(new GuiceModule(config.module));
        install(new JacksonModule());
    }

    private void initHandlers() {
        post("/portfolio/cost",
                ctx -> new CalculatePortfolioHandler(require(PortfolioCostController.class)).apply(ctx)
        ).produces(MediaType.json).consumes(MediaType.json);
    }

    @Setter
    @Accessors(fluent = true)
    public static class Config {
        private String token;
        private Module module;
    }
}
