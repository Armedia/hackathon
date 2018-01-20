
import com.google.gson.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class phase2 {

    //Goal is to display some information to a UI.
    //TODO: Parse JSON to make sure the deadline is not a week away.
    //TODO: Implement if a case is getting too close to its deadline to email the superior(s) with high priority
    //TODO: Implement the UI


    /*
     * Given the time constraint also since I did not have a skeleton web-app to play with, I decided the quickest manner in which
     * I could have a 'ui' would be to send an email if a constraint has been met. For example, I intend to to send a superior and emial
     * if a case is within a week of its due date.
     */

    /**
     * Returns the Authentication token
     * @param authTokenUrl is the specific url to get the auth token
     * @param username email address
     * @param password
     * @return (String) token
     */
    private static String getAuthToken(String authTokenUrl, String username, String password) {
        try {


            String authString = username + ":" + password;
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            String authStringEnc = new String(authEncBytes);
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(authTokenUrl);

            request.addHeader("User-Agent", USER_AGENT);
            request.addHeader("Accept", "text/plain");
            request.addHeader("Authorization", "Basic " + authStringEnc);
            request.addHeader("Content-Type", "application/json");
            request.addHeader("Keep-Alive", "True");
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Goes to the desired URL
     * @param url to fetch; this is a GET request
     * @return HTTPEntity for the retrieved data
     */
    public static HttpEntity HttpExecute(String url) {
        HttpEntity entity = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            return response.getEntity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * Returns a String of information in JSON format
     * @param queryType CASE_FILE, TASK, FILE
     * @return String of JSON with the desired query type
     */
    private static String getAdvancedSearch(String queryType) {
        try {
            Gson g = new Gson();
        String authToken = getAuthToken(
                "https://cloud.arkcase.com/arkcase/api/latest/authenticationtoken",
                "ann-acm@armedia.com",
                "AcMd3v$");
        String url = "https://cloud.arkcase.com/arkcase/api/latest/plugin/search/advancedSearch?" +
                "q=object_type_s:" + queryType +
                "&acm_ticket=" + authToken;
        HttpEntity entity = HttpExecute(url);

        //return json using google's gson
            return EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getCaseFileDate(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String casefile = getAdvancedSearch("CASE_FILE");
        JsonElement jelem = gson.fromJson(casefile, JsonElement.class);
        JsonObject jobj = jelem.getAsJsonObject();
        System.out.println(jobj.getAsJsonObject("response"));

        int numDocs = jobj.getAsJsonObject("response").getAsJsonArray("docs").size();
        System.out.println(jobj.getAsJsonObject("response").getAsJsonArray("docs").getAsJsonObject());
        for (int i = 0; i < numDocs; i++) {
            //for each case doc, check the date
//            jobj.getAsJsonObject("response").getAsJsonArray("docs").get
            //TODO: Stopped here; needed to continue parsing the JSON to get the date.
            //TODO:      I was then going to check if the date for the given doc is a week away,
            //TODO:      and finally if so, I would send out the email to the superior with the most
            //TODO:      important parts of the case file so the proper course of action may be taken.
        }
        return "";
    }

    private static void sendEmail(String mainContentToEmail) {

        try {
            // Step1
            System.out.println("\n 1st ===> setup Mail Server Properties..");
            Properties mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.smtp.port", "587");
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.starttls.enable", "true");
            System.out.println("Mail Server Properties have been setup successfully..");

            // Step2
            System.out.println("\n\n 2nd ===> get Mail Session..");
            Session getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            MimeMessage generateMailMessage = new MimeMessage(getMailSession);
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("no-reply@armedia.com"));
            generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("bossman@armedia.com"));
            generateMailMessage.setSubject("[URGENT ACTION REQUIRED] Deadline Approaching");
            String emailBody = " " + "<br><br> Regards, <br>Jeet";
            generateMailMessage.setContent(emailBody, "text/html");
            System.out.println("Mail Session has been created successfully..");

            // Step3
            System.out.println("\n\n 3rd ===> Get Session and Send mail");
            Transport transport = getMailSession.getTransport("smtp");

            // Enter your correct gmail UserID and Password
            // if you have 2FA enabled then provide App Specific Password
            transport.connect("smtp.gmail.com", " GMAIL ID", "Your GMAIL PASSWORD");
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getCurrentDate() {
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }

    public static void main(String[] args) {
        getCaseFileDate();
//        System.out.println(getAdvancedSearch("CASE_FILE"));
//        System.out.println(getAdvancedSearch("TASK"));
//        System.out.println(getAdvancedSearch("FILE"));

    }
}
