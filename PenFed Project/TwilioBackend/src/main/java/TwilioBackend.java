import static spark.Spark.*;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

/**
 * Created by SEAN on 8/9/17.
 */
public class TwilioBackend {
    public static void main(String[] args) {
        get("/", (req, res) -> "What is up");

        TwilioRestClient client = new TwilioRestClient.Builder(System.getenv("AC1c09fe8029a4302f6870fd0d9286e3a6"),
                System.getenv("2b6abab218e300dfa1550f89c4fdf98f")).build();

        post("/sms", (req, res) -> {
            String body = req.queryParams("Body");
            String to = req.queryParams("To");
            String from = "+12408835148";

            Message message = new MessageCreator(
                    new PhoneNumber(to),
                    new PhoneNumber(from),
                    body).create(client);

            return message.getSid();
        });
    }
}
