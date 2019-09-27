package ru.desiolab.portfolio.calculator;

import org.apache.commons.cli.*;
import ru.desiolab.portfolio.calculator.config.MainModule;

import static io.jooby.Jooby.runApp;

public class MainClass {
    public static void main(String[] args) throws ParseException {
        CommandLine cmd = parseArgs(args);
        String token = cmd.getOptionValue("iex");
        runApp(args, () -> {
            App.Config config = new App.Config()
                    .module(new MainModule(token));
            App app = new App(config);
            app.init();
            return app;
        });
    }

    private static CommandLine parseArgs(String[] args) throws ParseException {
        Options options = new Options();
        Option iexOption = Option.builder()
                .argName("IEX token")
                .longOpt("iex")
                .required(true)
                .hasArg()
                .build();
        options.addOption(iexOption);
        DefaultParser parser = new DefaultParser();
        return parser.parse(options, args);
    }
}
