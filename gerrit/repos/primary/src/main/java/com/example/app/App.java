package com.example.app;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.lang.Character;
import java.nio.charset.Charset;
import java.util.Random;

/**
 * Generate random string content from command line and return output
 * Adds a configurable delay (default 1 sec)
 */
public class App {

    public static String randomString(String chars, int length) {
        Random rand = new Random();
        StringBuilder buf = new StringBuilder();
        for (int i=0; i<length; i++) {

            buf.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return buf.toString();
    }

    public static void main( String[] args ) throws Exception {
        App handler = new App();

        Options options = new Options();
        options.addOption("help", false, "Display help prompt");
        options.addOption("l", "length", true, "Length of output string in characters");
        options.addOption("d", "delay", true, "seconds of delay to add before returning output");
//        options.addOption("c", "charset", true, "seconds of delay to add before returning output");
//        options.addOption("n", "newmode", false, "New mode not supported in legacy version");

        CommandLineParser parser = new DefaultParser();
        CommandLine inputs = parser.parse(options, args);

        // Print help options and exit
        if (args.length == 0 || inputs.hasOption("help")) {
            HelpFormatter helper = new HelpFormatter();
            helper.printHelp("primary", options);
            System.exit(0);
        }

        int delaySeconds = Integer.parseInt(inputs.getOptionValue("delay", "1"));
        Thread.sleep(delaySeconds*1000);
        int length = Integer.parseInt(inputs.getOptionValue("length", "5"));

//        String charsset = inputs.getOptionValue("charset","us-ascii");
//        boolean isNewMode = inputs.hasOption("newmode");

        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()";
        String output = randomString(characters, length);
        System.out.println(output);
    }
}
