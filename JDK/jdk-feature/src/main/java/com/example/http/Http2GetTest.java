package com.example.http;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Http2GetTest {

    public static void main(String[] args) {

        try {

            HttpClient client = HttpClient.newHttpClient();

            // GET
            HttpResponse<String> response = client.send(
                    HttpRequest
                            .newBuilder(new URI("http://www.foo.com/"))
                            .headers("MethodHandleTest", "foovalue", "Bar", "barvalue")
                            .GET()
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );
            int statusCode = response.statusCode();
            String body = response.body();
            System.out.println(statusCode+"+body:"+body);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
