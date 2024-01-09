package base;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeTest;
import java.io.File;
import java.io.IOException;

public class base  {
        protected String baseURI;
        protected String newResourceId;

        @BeforeTest
        public void setup() {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode configNode = objectMapper.readTree(new File("src/test/resources/config.json"));

                if (configNode.has("baseURI")) {
                    baseURI = configNode.get("baseURI").asText();
                    RestAssured.baseURI = baseURI;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


