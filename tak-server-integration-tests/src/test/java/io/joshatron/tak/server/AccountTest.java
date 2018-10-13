package io.joshatron.tak.server;

import io.joshatron.tak.server.utils.AccountUtils;
import io.joshatron.tak.server.utils.HttpUtils;
import io.joshatron.tak.server.utils.User;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.junit.Test;

import java.io.IOException;

//Suite A
//Current final test: 012
public class AccountTest {

    private final String suite = "A";
    private HttpClient client;

    public AccountTest() {
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

    @Test
    public void changePassword_BlankNewPassword_403() throws IOException {
        String test = "007";
        User user = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        AccountUtils.changePassword(user, "", client, HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void changePassword_MultipleUsers_204() throws IOException {
        String test = "008";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "12345678", client, HttpStatus.SC_NO_CONTENT);
        AccountUtils.changePassword(user1, "drowssap", client, HttpStatus.SC_NO_CONTENT);
        AccountUtils.changePassword(user2, "87654321", client, HttpStatus.SC_NO_CONTENT);
    }

    //Test 009
    @Test
    public void changePassword_OtherUserPassword_403() throws IOException {
        String test = "009";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "12345678", client, HttpStatus.SC_NO_CONTENT);
        user1.setPassword("12345678");
        AccountUtils.changePassword(user1, "drowssap", client, HttpStatus.SC_FORBIDDEN);
    }

    //Authenticate User
    @Test
    public void authenticate_Valid_204() throws IOException {
        String test = "010";
        User user = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        AccountUtils.authenticate(user, client, HttpStatus.SC_NO_CONTENT);
    }

    @Test
    public void authenticate_WrongPassword_403() throws IOException {
        String test = "011";
        User user = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        user.setPassword("drowssap");
        AccountUtils.authenticate(user, client, HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void authenticate_InvalidUser_403() throws IOException {
        String test = "012";
        User user = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        user.setUsername(suite + test + "02");
        AccountUtils.authenticate(user, client, HttpStatus.SC_FORBIDDEN);
    }
}
