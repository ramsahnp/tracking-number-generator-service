package com.code.assessment.tracking.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.*;

public class TestDuplicate {

    private static final String API_URL ="http://localhost:8080/next-tracking-number?origin_country_id=IN&destination_country_id=US&weight=1.2344545&created_at=2025-05-22T14:30:00Z&customer_id=123e4567-e89b-12d3-a456-426614174000&customer_name=John%20Doe&customer_slug=john-doe";
           // "http://34.45.155.115:8080/next-tracking-number?origin_country_id=IN&destination_country_id=US&weight=1.2344545&created_at=2025-05-22T14:30:00Z&customer_id=123e4567-e89b-12d3-a456-426614174000&customer_name=John%20Doe&customer_slug=john-doe";

    public static void main(String[] args) throws InterruptedException {
        int numThreads = 1500;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        Set<String> trackingNumbers = ConcurrentHashMap.newKeySet();

        CountDownLatch latch = new CountDownLatch(numThreads);

        Runnable task = () -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                int status = con.getResponseCode();
                if (status == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Extract tracking number from JSON response
                    String json = response.toString();
                    String trackingNumber = json.replaceAll(".*\"trackingNumber\"\\s*:\\s*\"(.*?)\".*", "$1");

                    boolean added = trackingNumbers.add(trackingNumber);
                    if (!added) {
                        System.out.println("❌ Duplicate found: " + trackingNumber);
                    } else {
                        System.out.println("✅ " + trackingNumber);
                    }
                } else {
                    System.out.println("❗ Error: HTTP " + status);
                }

            } catch (Exception e) {
                System.err.println("⚠ Error: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        };

        // Launch all threads
        for (int i = 0; i < numThreads; i++) {
            executor.submit(task);
        }

        latch.await();
        executor.shutdown();

        System.out.println("\n🔍 Total unique tracking numbers received: " + trackingNumbers.size());
    }
}
