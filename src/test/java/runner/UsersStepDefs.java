package runner;

import config.Configuration;
import factoryRequest.FactoryRequest;
import factoryRequest.RequestInfo;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UsersStepDefs {
    Response response;
    RequestInfo requestInfo = new RequestInfo();
    Map<String,String> variables = new HashMap<>();
    @Given("I created and store an user token by sending POST request to {string} with body")
    public void iCreatedAndStoreAnUserTokenBySendingPOSTRequestToWithBody(String url,String body) {
        String uniqueEmail = "testw01" + UUID.randomUUID() + "@ucb.com";
        body = body.replace("<email_placeholder>", uniqueEmail);
        requestInfo = new RequestInfo();
        requestInfo.setUrl(Configuration.host+url).setBody(body);
        response = FactoryRequest.make("post").send(requestInfo);
        System.out.println(response);
        String userId = response.then().extract().path("Id").toString();
        System.out.println("el user id es: " + userId);
        variables.put("$ID_USER_EMAIL", uniqueEmail);
        variables.put("$ID_USER", userId);
        // Save Token
        String credentials = Base64.getEncoder().encodeToString(
                (uniqueEmail + ":" + "pASswoRd").getBytes()
        );
        requestInfo.setUrl(Configuration.host + "/api/authentication/token.json")
                .setHeader("Authorization", "Basic " + credentials);
        response = FactoryRequest.make("get").send(requestInfo);
        System.out.println(response);
        String token = response.then().extract().path("TokenString").toString();
        System.out.println("Token: " + token);
        variables.put("$TOKEN", token);
    }


    @When("I get the user created")
    public void iGetTheUserCreated() {
        requestInfo = new RequestInfo();
        requestInfo.setUrl(Configuration.host + "/api/User.json");
        String credentials = Base64.getEncoder().encodeToString(
                (variables.get("$ID_USER_EMAIL") + ":" + "pASswoRd").getBytes()
        );
        requestInfo.setHeader("Authorization", "Basic " + credentials);
        response = FactoryRequest.make("get").send(requestInfo);
        System.out.println("se obtuvo el usuario:" + response);

    }


    @Then("response code should be {int}")
    public void responseCodeShouldBe(int expectedCode) {
        response.then().statusCode(expectedCode);
    }

    @When("send PUT request {string} with body")
    public void sendPUTRequestWithBody(String url, String body) {
        String uniqueEmail2 = "testwvnewemail" + UUID.randomUUID() + "@ucb.com";
        body = body.replace("<email_placeholder>", uniqueEmail2);
        requestInfo = new RequestInfo();
        String finalUrl = Configuration.host + this.replaceValues(url);
        requestInfo.setUrl(finalUrl).setBody(body);
        System.out.println(finalUrl);
        String credentials = Base64.getEncoder().encodeToString(
                (variables.get("$ID_USER_EMAIL") + ":" + "pASswoRd").getBytes()
        );
        requestInfo.setHeader("Authorization", "Basic " + credentials);
        response = FactoryRequest.make("put").send(requestInfo);
        String userId2 = response.then().extract().path("Id").toString();
        System.out.println(response);
        variables.put("$ID_USER_EMAIL", uniqueEmail2);
        variables.put("$ID_USER", userId2);
    }

    @When("send DELETE request {string} with body")
    public void sendDELETERequestWithBody(String url, String body) {
        requestInfo = new RequestInfo();
        String finalUrl = Configuration.host + this.replaceValues(url);
        requestInfo.setUrl(finalUrl).setBody(body);
        System.out.println(finalUrl);
        String credentials = Base64.getEncoder().encodeToString(
                (variables.get("$ID_USER_EMAIL") + ":" + "pASswoRd").getBytes()
        );
        requestInfo.setHeader("Authorization", "Basic " + credentials);
        response = FactoryRequest.make("delete").send(requestInfo);
        System.out.println(response);

    }

    private String replaceValues(String value){
        for (String key:variables.keySet() ) {
            value = value.replace(key,variables.get(key));
        }
        return value;
    }
}
