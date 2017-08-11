import static spark.Spark.*;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import com.twilio.Twilio;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by SEAN on 8/9/17.
 */
public class TwilioBackend {
    public static final String ACCOUNT_SID = "AC50edc1ea624fdc8659c2c802b2eade7a";
    public static final String AUTH_TOKEN = "3a7c47d881f69aecbc431eb05436b54c";
    public static void main(String[] args) {
        get("/", (req, res) -> "What is up motherfuckersssss");

        TwilioRestClient client = new TwilioRestClient.Builder(System.getenv("AC50edc1ea624fdc8659c2c802b2eade7a"),
                System.getenv("3a7c47d881f69aecbc431eb05436b54c")).build();

        post("https://6989a2a9.ngrok.io", (req, res) -> {
            String body = "Testing the API again...";//req.queryParams("Body");
            String to = "+17034704997";//req.queryParams("To");
            String from = "+12408835148";

            Message message = new MessageCreator(
                    new PhoneNumber(to),
                    new PhoneNumber(from),
                    body).create(client);

            return message.getSid();
        });

//        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//
//        Message message = Message
//                .creator(new PhoneNumber("+17034704997"),  // to
//                        new PhoneNumber("+12408835148"),  // from
//                        "Testing the Twilio API messaging service.")
//                .create();
    }
}
