package tests;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;

import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

    public class beforeRequest {
        public String newID;

        private static Properties properties;

        static {
            properties = loadProperties();
        }

        private static Properties loadProperties() {
            Properties prop = new Properties();
            try (InputStream input = beforeRequest.class.getClassLoader().getResourceAsStream("api.properties")) {
                if (input == null) {
                    System.out.println("Sorry, unable to find api.properties");
                    return null;
                }
                prop.load(input);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return prop;
        }

        @Test
        public void getRequest() {
            RestAssured.config = RestAssured.config().sslConfig(new SSLConfig().allowAllHostnames().relaxedHTTPSValidation());

            RestAssured.baseURI = properties.getProperty("baseURI");
            System.out.println("baseurl"+baseURI);

            Response response = RestAssured.get(properties.getProperty("getObjectPath"));

            String responseBody = response.getBody().asString();
            System.out.println("Response Body: \n" + responseBody);

            int statusCode = response.getStatusCode();
            System.out.println("Status Code: " + statusCode);
            String newID = response.jsonPath().getString("id");
            System.out.println("NewID: " + newID);
        }
        @Test
        public void postRequest() {
            RestAssured.config = RestAssured.config().sslConfig(new SSLConfig().allowAllHostnames().relaxedHTTPSValidation());
            String requestBody = "{" +
                    "\"name\": \"Apple MacBook Pro 16\"," +
                    "\"data\": {" +
                    "\"year\": 2019," +
                    "\"price\": 1849.99," +
                    "\"CPU model\": \"Intel Core i9\"," +
                    "\"Hard disk size\": \"1 TB\"" +
                    "}" +
                    "}";
            Response response = RestAssured.given().baseUri(properties.getProperty("baseURI"))
                    .contentType("application/json")
                    .body(requestBody)
                    .post(properties.getProperty("postObjectPath"));
            response.then().statusCode(200);
            System.out.println("responseBody:\n" + response.asString());
            System.out.println("Status Code: " + response.getStatusCode());
        }

        @Test
        public void putRequest() {
            RestAssured.config = RestAssured.config().sslConfig(new SSLConfig().allowAllHostnames().relaxedHTTPSValidation());
            String requestBody = "{" +
                    "\"id\":\"ff8081818cb48d30018cce04ab861992\"," +
                    "\"name\": \"Apple MacBook Pro 16\"," +
                    "\"updatedAt\": \"2024-01-03T06:31:06.095+00:00\"," +
                    "\"data\": {" +
                    "\"year\": 2023," +
                    "\"price\": 2049.99," +
                    "\"CPU model\": \"Intel Core i9\"," +
                    "\"Hard disk size\": \"1 TB\"," +
                    "\"color\": \"silver\"" +
                    "}" +
                    " }";
            String url = properties.getProperty("putObjectPath");
            Response response = given()
                    .contentType("application/json")
                    .body(requestBody)
                    .put(url);


            response.then()

                    .body("id", equalTo("ff8081818cb48d30018cce04ab861992"))
                    .body("name", equalTo("Apple MacBook Pro 16"))
                    .body("data.year", equalTo(2023))
                    .body("data.price", equalTo(2049.99f))
                    .body("data.'CPU model'", equalTo("Intel Core i9"))
                    .body("data.'Hard disk size'", equalTo("1 TB"))
                    .body("data.color", equalTo("silver"))
                    .body("updatedAt", equalTo("2024-01-03T06:31:06.095+00:00"));
        }

        @Test
        public void deleteRequest() {
            RestAssured.config = RestAssured.config().sslConfig(new SSLConfig().allowAllHostnames().relaxedHTTPSValidation());
            given()
                    .when()
                    .delete(properties.getProperty("deleteObjectPath"))
                    .then()
                    .statusCode(200)
                    .log().all();
        }
    }


