import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void disableValidation() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext context = SSLContext.getInstance("TLSv1.3");
        TrustManager[] trustManager = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(X509Certificate[] certificate, String str) {
                    }

                    public void checkServerTrusted(X509Certificate[] certificate, String str) {
                    }
                }
        };
        context.init(null, trustManager, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    public static String getSite() throws IOException, URISyntaxException {
        // Make a URL to the web page
        URI url = new URI("https://sjsuparkingstatus.sjsu.edu/");

        // Get the input stream through URL Connection
        URLConnection con = url.toURL().openConnection();
        InputStream is = con.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        //System.out.println(br.lines().collect(Collectors.joining()));
        return br.lines().collect(Collectors.joining());
    }

    public static void main(String[] args) throws URISyntaxException, IOException, NoSuchAlgorithmException, KeyManagementException {

        disableValidation();

        File out = new File("percentages.csv");
        boolean exists = out.exists();


        try (FileWriter outWriter = new FileWriter(out, true)) {
            if (!exists) {
                System.out.println("Creating " + out.getAbsolutePath());
                outWriter.write("TIMESTAMP,SOUTH,WEST,NORTH,SOUTH CAMPUS\n");
                outWriter.flush();
            }

            int delay;
            if (args.length == 0){
                delay = 10;
            }
            else{
                delay = Integer.parseInt(args[0]);
            }

            int fucked = 0;
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    //use when trying not to rate limite yourself in testing
                    //String line = "<!DOCTYPE html><html><head><script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src='https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);})(window,document,'script','dataLayer','GTM-PMNG6XV');</script><meta charset=\"UTF-8\"><meta name=\"description\" content=\"Check availability for SJSU parking garages\"><meta name=\"keyword\" content=\"SJSU parking, SJSU parking garage\"><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><link rel=\"stylesheet\" type=\"text/css\" href=\"/css/parking.css\"><link href=\"https://fonts.googleapis.com/css?family=Nunito+Sans:400,600,800\" rel=\"stylesheet\"><header class=\"sjsu-header u-bg--dark\" role=\"banner\"><div class=\"wrap\"><a class=\"sjsu-title\" target=\"_blank\" href=\"https://www.sjsu.edu/\">SJSU</a></div></header><span class=\"sjsu-gradientbar\"></span><main class=\"sjsu-main\"></main><div class=\"wrap\"><h1 class=\"parking-title\">Parking Garage Fullness</h1><h2 class=\"parking-services\"><a target=\"_blank\" href=\"https://www.sjsu.edu/parking/index.php\">Parking Services</a></h2><p class=\"timestamp\">Last updated 2025-3-4 3:37:00 PM<a class=\"btn btn-primary\" style=\"width: 100%;\" href=\"/GarageStatusPlain\"> Refresh</a></p><div class=\"garage\"><p></p><h2 class=\"garage__name\">South Garage </h2><p class=\"garage__text\"><a class=\"garage__address\" target=\"_blank\" href=\"https://www.google.com/maps/place/377 S. 7th St., San Jose, CA 95112\">377 S. 7th St., San Jose, CA 95112</a><span class=\"garage__fullness\"> 95 %   </span></p><p></p><h2 class=\"garage__name\">West Garage </h2><p class=\"garage__text\"><a class=\"garage__address\" target=\"_blank\" href=\"https://www.google.com/maps/place/350 S. 4th St., San Jose, CA 95112\">350 S. 4th St., San Jose, CA 95112</a><span class=\"garage__fullness\"> 84 %   </span></p><p></p><h2 class=\"garage__name\">North Garage </h2><p class=\"garage__text\"><a class=\"garage__address\" target=\"_blank\" href=\"https://www.google.com/maps/place/65 S. 10th St., San Jose, CA 95112\">65 S. 10th St., San Jose, CA 95112</a><span class=\"garage__fullness\"> 87 %   </span></p><p></p><h2 class=\"garage__name\">South Campus Garage </h2><p class=\"garage__text\"><a class=\"garage__address\" target=\"_blank\" href=\"https://www.google.com/maps/place/1278 S. 10th Street, San Jose, CA 95112\">1278 S. 10th Street, San Jose, CA 95112</a><span class=\"garage__fullness\"> 27 %   </span></p><p></p></div><h2>Parking Shuttles</h2><p class=\"u-lift\">During Fall and Spring semesters, parking shuttles run regularly from parking lots to campus.</p><p><a href=\"https://sjsu.transloc.com/routes\" target=\"_blank\">Realtime Shuttle Location</a></p><p><a href=\"https://www.sjsu.edu/parking/help/shuttle.php\" target=\"_blank\">SJSU Shuttle Information</a></p></div><link rel=\"stylesheet\" type=\"text/css\" href=\"/css/parking.css\"><link href=\"https://fonts.googleapis.com/css?family=Nunito+Sans:400,600,800\" rel=\"stylesheet\"><br><div class=\"row\"><div class=\"form-group\"></div></div><script src=\"http://code.jquery.com/jquery-1.11.3.min.js\"></script><body><noscript><iframe src=\"https://www.googletagmanager.com/ns.html?id=GTM-PMNG6XV\" height=\"0\" width=\"0\" style=\"display:none;visibility:hidden\"></iframe></noscript><div class=\"container\"></div><div class=\"navbar navbar-default\"><div class=\"container\"><div class=\"navbar-header\"><a class=\"navbar-brand\" href=\"/\"></a></div></div></div><div class=\"alert alert-dismissable\"></div></body></head></html>"; System.out.println("got fake site");

                    String line = getSite();

                    @SuppressWarnings("RegExpRepeatedSpace")
                    Pattern pattern = Pattern.compile("<h2 class=\"garage__name\">([A-Za-z ]+) </h2><p class=\"garage__text\"><a class=\"garage__address\" target=\"_blank\" href=\".+?\">.+?</a><span class=\"garage__fullness\"> (\\d+) %   </span></p>");
                    Matcher matcher = pattern.matcher(line);
                    HashMap<String, Integer> map = new HashMap<>();
                    while (matcher.find()) {
                        map.put(matcher.group(1), Integer.parseInt(matcher.group(2)));
                    }
                    String template = "%d,%d,%d,%d,%d\n";
                    outWriter.write(String.format(template, (System.currentTimeMillis() / 1000L), map.get("South Garage"), map.get("West Garage"), map.get("North Garage"), map.get("South Campus Garage")));
                    outWriter.flush();
                    //System.out.printf((template), (System.currentTimeMillis() / 1000L), map.get("South Garage"), map.get("West Garage"), map.get("North Garage"), map.get("South Campus Garage"));
                } catch (RuntimeException e) {
                    fucked++;
                    System.out.println("fuck" + fucked);
                }
                Thread.sleep(Duration.ofMinutes(delay));
            }
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
        }
    }
}