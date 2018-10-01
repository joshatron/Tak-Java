package io.joshatron.tak.server;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

//Suite A
public class AccountTest {

    //Register User
    //Test 001
    @Test
    public void registerUser_OneUser_204() throws IOException {
        HttpResponse response = HttpUtils.createUser("A00101", "password", HttpUtils.createHttpClient());
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
    }

    //Test 002
    @Test
    public void registerUser_MultipleUsers_204() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //first user
        HttpResponse response = HttpUtils.createUser("A00201", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //second user, same password
        response = HttpUtils.createUser("A00202", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //third user, different password
        response = HttpUtils.createUser("A00203", "drowssap", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
    }

    //Test 003
    @Test
    public void registerUser_UserAlreadyCreated_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //first user
        HttpResponse response = HttpUtils.createUser("A00301", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //same user, same password
        response = HttpUtils.createUser("A00301", "password", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
        //same user, different password
        response = HttpUtils.createUser("A00301", "drowssap", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
    }

    //Change Password
    //Test 004
    @Test
    public void changePassword_OneUser_204() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //create user
        HttpResponse response = HttpUtils.createUser("A00401", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //change user password
        response = HttpUtils.changePassword("A00401", "password", "drowssap", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
    }

    //Test 005
    @Test
    public void changePassword_WrongPassword_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //create user
        HttpResponse response = HttpUtils.createUser("A00501", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //change user password with wrong password
        response = HttpUtils.changePassword("A00501", "passwor", "drowssap", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
    }

    //Test 006
    @Test
    public void changePassword_InvalidUser_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //create user
        HttpResponse response = HttpUtils.createUser("A00601", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //change nonexistent user's password
        response = HttpUtils.changePassword("A00602", "password", "drowssap", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
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

    //Test009
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
    //Test010
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

    //Test011
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

    //Test012
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
