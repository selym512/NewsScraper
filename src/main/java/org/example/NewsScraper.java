package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.json.simple.JSONObject;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
//import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewsScraper {


    public String handleNewsScrape() {
//        Thread t1 = new Thread();
//        t1.start();
//        apDoc = Jsoup.connect("https://apnews.com/").get();
//        bbcDoc = Jsoup.connect("https://www.bbc.com/").get();

        scrapeThread apThread = new scrapeThread("https://apnews.com/");
//        scrapeThread apThread = new scrapeThread("https://www.cnn.com/");
        scrapeThread bbcThread = new scrapeThread("https://www.bbc.com/");
        Thread thread1 = new Thread(apThread);
        Thread thread2 = new Thread(bbcThread);
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch(InterruptedException e){
            System.err.println(e);
        }
        List<Element> apPhrases = apThread.getScrapeList();
        List<Element> bbcPhrases = bbcThread.getScrapeList();

        Set<String> cleanedHeadlines = new HashSet<>();
        int strLength;
        for (Element element : apPhrases) {
            strLength = element.text().split(" ").length;
            if (strLength < 4 || strLength > 14){
                continue;
            }
            cleanedHeadlines.add(element.text() + "\n");

        }
        for (Element element : bbcPhrases) {
            strLength = element.text().split(" ").length;
            if (strLength < 4 || strLength > 14){
                continue;
            }
            cleanedHeadlines.add(element.text() + "\n");
        }
        StringBuilder headlineString = new StringBuilder("");
        for (String str : cleanedHeadlines ){
            headlineString.append(str);
        }
        for (String str : cleanedHeadlines){
            System.out.println("\"" + JSONObject.escape(str) + "\",");
        }
        for (String str : cleanedHeadlines){
            System.out.print(str);
        }
//        System.out.println((headlineString.toString()));


        String bucketName = System.getenv("bucketName");
        String objectKey = System.getenv("objectKey");
        try {
        Region region = Region.US_EAST_2;
        S3Client s3 = S3Client.builder()
                .region(region)
                .build();

//            Map<String, String> metadata = new HashMap<>();
//            metadata.put("x-amz-meta-myVal", "test");
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
//                    .metadata()
                    .build();

            s3.putObject(putOb, RequestBody.fromString(headlineString.toString()));
            System.out.println("Successfully placed " + objectKey + " into bucket " + bucketName);

        s3.close();
        } catch (S3Exception e) {
            System.out.println("S3 upload failed: " + e.getMessage());
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return headlineString.toString();

    }
    public void putToS3(String headlines){
        System.out.println("is putToS3 even running");
        String bucketName = System.getenv("bucketName");
        String objectKey = System.getenv("objectKey");
        Region region = Region.US_EAST_2;
        S3Client s3 = S3Client.builder()
                .region(region)
                .build();

        try {
//            Map<String, String> metadata = new HashMap<>();
//            metadata.put("x-amz-meta-myVal", "test");
            PutObjectRequest putOb = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
//                    .metadata()
                    .build();

            s3.putObject(putOb, RequestBody.fromString(headlines));
            System.out.println("Successfully placed " + objectKey + " into bucket " + bucketName);

        } catch (S3Exception e) {
            System.out.println(e.getMessage());
            System.err.println(e.getMessage());
            System.exit(1);
        }
        s3.close();
    }

}
