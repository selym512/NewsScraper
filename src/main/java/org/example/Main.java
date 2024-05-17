package org.example;

//import org.apache.log4j.PropertyConfigurator;
//import software.amazon.awssdk.core.SdkStandardLogger;

//import java.io.IOException;
//import java.util.Properties;
//import java.util.Set;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
//        Properties log4jProp = new Properties();
//        log4jProp.setProperty("log4j.rootLogger", "WARN");
//        PropertyConfigurator.configure(log4jProp);
        NewsScraper nwsScrp = new NewsScraper();
        String headlines = nwsScrp.handleNewsScrape();
//        System.out.println("is handleNewsScrape even finishing within main");
//        nwsScrp.putToS3(headlines);
//        System.out.println("is main finishing");


    }
}