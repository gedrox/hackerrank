package hackerrank;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;
//import jdk.incubator.http.internal.websocket.WebSocketRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class BlueOrange {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        Random r = new Random();

        String pId = UUID.randomUUID().toString();
        String rId = UUID.randomUUID().toString();

        URI uri = new URI("https://gate.blueorangebank.com/open-api/accounts/1237486/transactions/?date_from=2017-01-01&date_to=2017-12-31");
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(uri)
                .header("Process-ID", pId)
                .header("Request-ID", rId)
                .header("Date", "Sat, 15 Nov 1994 08:12:31 GMT")
                .GET()
                .build();

        HttpResponse<String> send = httpClient.send(req, HttpResponse.BodyHandler.asString());
        System.out.println(send.statusCode());
        System.out.println(send.body());

        System.out.println(send.headers().map());

//        URL url = new URL("https://gate.blueorangebank.com/open-api/accounts/1237486/transactions/?date_from=2017-01-01&date_to=2017-12-31");
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestProperty("Process-ID", pId);
//        con.setRequestProperty("Request-ID", rId);
//        con.setRequestProperty("date", "2017-12-09");
//        con.setRequestMethod("GET");
//
//        int rc = con.getResponseCode();
//        System.out.println(rc);
//
//        InputStream inputStream = con.getInputStream();
//        Scanner sc = new Scanner(inputStream);
//        String line = sc.nextLine();
//        System.out.println(line);
    }
}
