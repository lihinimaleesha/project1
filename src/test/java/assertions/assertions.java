package assertions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.testng.Assert.assertEquals;

public class assertions {

    public static void assertObject(Response response, String expectedName, String expectedColor) {
        assertEquals(200, response.getStatusCode()); // Assert the status code

        try {
            String responseBody = response.getBody().asString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);


            assertEquals(expectedName, jsonResponse.get("name").asText());


            assertEquals(expectedColor, jsonResponse.get("data").get("color").asText());
        } catch (Exception e) {

        }
    }


    public static void assertPostObject(Response response, String expectedName, String expectedColor) {
        assertEquals(200, response.getStatusCode()); // Assert the expected status code (e.g., 201 Created)

        try {
            String responseBody = response.getBody().asString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);




            assertEquals(expectedName, jsonResponse.get("name").asText());


            assertEquals(expectedColor, jsonResponse.get("data").get("CPU model").asText());
        } catch (Exception e) {

        }
    }


    public static void assertPutObject(Response response, String expectedName, String expectedColor) {
        assertEquals(200, response.getStatusCode()); // Assert the expected status code (e.g., 200 OK)

        try {
            String responseBody = response.getBody().asString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(responseBody);


            assertEquals(expectedName, jsonResponse.get("name").asText());

            assertEquals(expectedColor, jsonResponse.get("data").get("CPU model").asText());
        } catch (Exception e) {
        }
    }

    public static Response makeGetRequest(String endpoint) {
        return RestAssured.get(endpoint);
    }

    public static void assertDeleteObject(Response deleteResponse) {
        assertEquals(200, deleteResponse.getStatusCode());
    }

    public static void assertResourceNotExists(String resourcePath) {
        Response getObjectResponse = makeGetRequest(resourcePath);
        assertEquals(404, getObjectResponse.getStatusCode());
    }



}
