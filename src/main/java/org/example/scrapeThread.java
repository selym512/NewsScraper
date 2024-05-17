package org.example;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;


public class scrapeThread implements Runnable{

    private Document doc;
    String url;
    List<Element> list;

    public scrapeThread(String url){
        this.url = url;
    }

    public List<Element> getScrapeList(){
        return list;
    }

    @Override
    public void run() {
        try {
            doc = Jsoup.connect(url).get();
            list = doc.select("h1,h2,h3,h4,h5,h6,span,div");
        }
        catch (IOException e){
            System.out.println("IOException");
            System.err.println(e.getMessage());
        }
    }

    public void start() {
        this.run();
    }
}
