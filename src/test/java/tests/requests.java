package tests;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static io.restassured.RestAssured.given;
import assertions.assertions;
import base.base;

public class requests extends base {

    @Test
    public void getObjectByIdTest() {
        RestAssured.config = RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation());

        try {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode expectedValues = objectMapper.readTree(Files.newBufferedReader(Paths.get("src/test/resources/GET/expectedvalues.json")));
            String endpointPath = expectedValues.get("endpointPath").asText();
            String expectedName = expectedValues.get("expectedName").asText();
            String expectedColor = expectedValues.get("expectedColor").asText();
            Response response = makeGetRequest(endpointPath);
            assertions.assertObject(response, expectedName, expectedColor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void postObjectTest() {
        RestAssured.config = RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation());

        String requestBody = readJsonFromFile("src/test/resources/POST/postobject.json");

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode expectedValues = objectMapper.readTree(Files.newBufferedReader(Paths.get("src/test/resources/POST/expectedvalues.json")));

            String endpointPath = expectedValues.get("endpointPath").asText();
            String expectedName = expectedValues.get("expectedName").asText();
            String expectedDescription = expectedValues.get("expectedDescription").asText();


            Response response = makePostRequest(endpointPath, requestBody);

         assertions.assertPostObject(response, expectedName, expectedDescription);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void putObjectTest() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode expectedValues = objectMapper.readTree(Files.newBufferedReader(Paths.get("src/test/resources/PUT/expectedvalues.json")));

            String postEndpointPath = expectedValues.get("postEndpointPath").asText();
            String putEndpointPath = expectedValues.get("putEndpointPath").asText();
            String expectedName = expectedValues.get("expectedName").asText();
            String expectedDescription = expectedValues.get("expectedDescription").asText();

            String postRequestBody = readJsonFromFile("src/test/resources/POST/postobject.json");
            Response postResponse = makePostRequest(postEndpointPath, postRequestBody);

            String newResourceId = postResponse.jsonPath().getString("id");
            System.out.println("New Resource ID (POST): " + newResourceId);

            // Make the PUT request using the put endpoint path
            String putRequestBody = readJsonFromFile("src/test/resources/PUT/putobject.json");
            Response putResponse = makePutRequest(putEndpointPath + "/" + newResourceId, putRequestBody);

            // Use the assertion class to assert the response
           assertions.assertPutObject(putResponse, expectedName, expectedDescription);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteObjectTest() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode endpointArray = objectMapper.readTree(Files.newBufferedReader(Paths.get("src/test/resources/DELETE/expectedvalues.json")));

            for (JsonNode endpointPair : endpointArray) {
                String postEndpoint = endpointPair.get("postEndpoint").asText();
                String deleteEndpoint = endpointPair.get("deleteEndpoint").asText();

                // POST request to create an object
                String requestBody = readJsonFromFile("src/test/resources/POST/postobject.json");
                Response postResponse = makePostRequest(postEndpoint, requestBody);
                newResourceId = postResponse.jsonPath().getString("id");

                //  DELETE request for the created object
                Response deleteResponse = makeDeleteRequest(deleteEndpoint + "/" + newResourceId);

                //   assertion class to assert the deleteResponse
                assertions.assertDeleteObject(deleteResponse);

                // Check resource no longer exists
              assertions.assertResourceNotExists(deleteEndpoint + "/" + newResourceId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response makeGetRequest(String endpoint) {
        return given().when().get(endpoint);
    }


    private Response makePostRequest(String endpoint, String requestBody) {
        return given().contentType("application/json").body(requestBody).post(endpoint);
    }

    private Response makePutRequest(String endpoint, String requestBody) {
        return given().contentType("application/json").body(requestBody).put(endpoint);
    }

    private Response makeDeleteRequest(String endpoint) {
        return given().delete(endpoint);
    }

    private String readJsonFromFile(String filePath) {
        // Read JSON data from a file and return it as a string
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
