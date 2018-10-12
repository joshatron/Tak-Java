package io.joshatron.tak.server;

import io.joshatron.tak.server.utils.AccountUtils;
import io.joshatron.tak.server.utils.HttpUtils;
import io.joshatron.tak.server.utils.User;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

//Suite A
//Current final test: 012
public class AccountTest {

    private String suite;
    private HttpClient client;

    public AccountTest() {
        suite = "A";
        client = HttpUtils.createHttpClient();
    }

    //Register User
    @Test
    public void registerUser_OneUser_204() throws IOException {
        String test = "001";
        AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void registerUser_MultipleUsers_204() throws IOException {
        String test = "002";
        AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        AccountUtils.addUser(suite, test, "03", "drowssap", client, HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void registerUser_UserAlreadyCreated_403() throws IOException {
        String test = "003";
        AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_FORBIDDEN);
        AccountUtils.addUser(suite, test, "01", "drowssap", client, HttpStatus.SC_FORBIDDEN);
    }

    //Change Password
    @Test
    public void changePassword_OneUser_204() throws IOException {
        String test = "004";
        User user = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        AccountUtils.changePassword(user, "drowssap", client, HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void changePassword_WrongPassword_403() throws IOException {
        String test = "005";
        User user = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        user.setPassword("pass");
        AccountUtils.changePassword(user, "drowssap", client, HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void changePassword_InvalidUser_403() throws IOException {
        String test = "006";
        User user = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        user.setUsername(suite + test + "02");
        AccountUtils.changePassword(user, "drowssap", client, HttpStatus.SC_FORBIDDEN);
    }

    //Test 007
    @Test
    public void changePassword_BlankNewPassword_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //create user
        HttpResponse response = HttpUtils.createUser("A00701", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //change user's password with blank password
        response = HttpUtils.changePassword("A00701", "password", "", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
    }

    //Test 008
    @Test
    public void changePassword_MultipleUsers_204() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //create user
        HttpResponse response = HttpUtils.createUser("A00801", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //create a second user
        response = HttpUtils.createUser("A00802", "12345678", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //change the first user's password
        response = HttpUtils.changePassword("A00801", "password", "drowssap", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //change the second user's password
        response = HttpUtils.changePassword("A00802", "12345678", "87654321", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
    }

    //Test 009
    @Test
    public void changePassword_OtherUserPassword_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //create user
        HttpResponse response = HttpUtils.createUser("A00901", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //create a second user
        response = HttpUtils.createUser("A00902", "12345678", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //try to change the first user's password with the password of the second user
        response = HttpUtils.changePassword("A00901", "12345678", "87654321", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
    }

    //Authenticate User
    //Test 010
    @Test
    public void authenticate_Valid_204() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //create user
        HttpResponse response = HttpUtils.createUser("A01001", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //check that user is valid
        response = HttpUtils.authenticate("A01001", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
    }

    //Test 011
    @Test
    public void authenticate_WrongPassword_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //create user
        HttpResponse response = HttpUtils.createUser("A01101", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //check that user is valid with invalid password
        response = HttpUtils.authenticate("A01101", "drowssap", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
    }

    //Test 012
    @Test
    public void authenticate_InvalidUser_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //create user
        HttpResponse response = HttpUtils.createUser("A01201", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //check nonexistent user with first user's password
        response = HttpUtils.authenticate("A01202", "password", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
    }
}
