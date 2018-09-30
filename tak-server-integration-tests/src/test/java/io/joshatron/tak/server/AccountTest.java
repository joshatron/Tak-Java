package io.joshatron.tak.server;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

//Suite A
public class AccountTest {

    private String baseUrl = "https://localhost:8080";

    //Test 001
    @Test
    public void accountCreation_OneUser_204() throws IOException {
        String payload = "{" +
                "    \"username\": \"A00101\"," +
                "    \"password\": \"password\"" +
                "}";

        HttpClient httpClient = HttpUtils.createHttpClient();

        HttpPost request = new HttpPost(baseUrl + "/account/register");
        StringEntity entity = new StringEntity(payload);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(entity);

        HttpResponse response = httpClient.execute(request);

        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
    }

    //Test 002
    @Test
    public void accountCreation_MultipleUsers_204() {

    }

    //Test 003
    @Test
    public void accountCreation_UserAlreadyCreated_403() {

    }

    //Test 004
    @Test
    public void changePassword_OneUser_204() {

    }

    //Test 005
    @Test
    public void changePassword_WrongPassword_403() {

    }

    //Test 006
    @Test
    public void changePassword_InvalidUser_403() {

    }

    //Test 007
    @Test
    public void changePassword_BlankNewPassword_403() {

    }

    //Test 008
    @Test
    public void changePassword_MultipleUsers_204() {

    }

    //Test009
    @Test
    public void changePassword_OtherUserPassword_403() {

    }
}
