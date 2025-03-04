import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    public static void disableValidation() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLSv1.3");
        TrustManager[] trustManager = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(X509Certificate[] certificate, String str) {}
                    public void checkServerTrusted(X509Certificate[] certificate, String str) {}
                }
        };
        context.init(null, trustManager, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, KeyManagementException {

        disableValidation();
        // Make a URL to the web page
        URL url = new URL("https://sjsuparkingstatus.sjsu.edu/");

        // Get the input stream through URL Connection
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();

        File out = new File("percentages.csv");
        boolean exists = out.exists();
        FileWriter outWriter = new FileWriter(out);
        if (!exists){outWriter.write("TIMESTAMP,SOUTH,WEST,NORTH,SOUTH CAMPUS");}




        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line = br.lines().collect(Collectors.joining());

            System.out.println(line);
            Pattern pattern = Pattern.compile("<h2 class=\"garage__name\">([A-Za-z ]+)<\\/h2><p class=\"garage__text\"><a class=\"garage__address\" target=\"_blank\" href=\".+?\">.+?<\\/a><span class=\"garage__fullness\"> (\\d+) %   <\\/span><\\/p>");
            Matcher matcher = pattern.matcher(line);
            HashMap<String, Integer> map = new HashMap<>();
            while (matcher.find()) {
                System.out.print(System.currentTimeMillis()/1000L);
                map.put(matcher.group(1), Integer.parseInt(matcher.group(2)));
            }
            String template = ""
            System.out.println(String.format(template, map.get("South Garage"), map.get("West Garage"), map.get("North Garage"), map.get("South Campus Garage"), map.get()));
        }
    }
}