package com.example.Backend.stepDefination;

import com.example.Backend.model.UserLogin;
import com.example.Backend.model.UserRegister;
import com.example.Backend.model.UserResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.Serenity;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@Slf4j
public class LoginStepDefination extends StartupHook{
    UserRegister tester = new UserRegister();
    UserLogin testLogin = new UserLogin();
    TestRestTemplate restTemplate = new TestRestTemplate();
    ResponseEntity<UserResponse> responseRegister, responseLogin;
    ResponseEntity<UserLogin> result;

    @Given("User calls login endpoint for valid user")
    public void userCallsLoginEndpointForValidUser() {
        tester.setUsername("user2");
        tester.setEmail("tester2@gmail.com");
        tester.setPassword("Pass@123");
        responseRegister = restTemplate.postForEntity("http://localhost:8083/user/register", tester, UserResponse.class);
        testLogin.setUsername(tester.getUsername());
        testLogin.setPassword(tester.getPassword());
    }

    @When("System processes login request for valid user")
    public void systemProcessesLoginRequestForValidUser() {
        log.info("Processing login request");
        responseLogin = restTemplate.postForEntity("http://localhost:8083/user/login", testLogin, UserResponse.class);
        Serenity.setSessionVariable("response").to(responseLogin.getBody());
    }

    @Then("Validate response from the login endpoint for valid user")
    public void validateResponseFromTheLoginEndpointForValidUser() {
        Assertions.assertEquals(UserResponse.builder().Message("User Logged in: " + tester.getUsername()).build(), Serenity.getCurrentSession().get("response"));
    }

    @Given("User calls login endpoint for invalid user")
    public void userCallsLoginEndpointForInvalidUser() {
        testLogin.setUsername("nouser");
        testLogin.setPassword("Password");
    }

    @When("System processes login request for invalid user")
    public void systemProcessesLoginRequestForInvalidUser() {
        log.info("Processing login request");
        responseLogin = restTemplate.postForEntity("http://localhost:8083/user/login", testLogin, UserResponse.class);
        Serenity.setSessionVariable("responseInvalid").to(responseLogin.getBody());
    }

    @Then("Validate response from the login endpoint for invalid user")
    public void validateResponseFromTheLoginEndpointForInvalidUser() {
        Assertions.assertEquals(UserResponse.builder().Message("User doesn't exists").build(), Serenity.getCurrentSession().get("responseInvalid"));
    }

    @Given("User calls display user endpoint for valid username")
    public void userCallsDisplayUserEndpointForValidUsername() {
        log.info("User calls display user endpoint");
    }

    @When("System processes display user request for valid username")
    public void systemProcessesDisplayUserRequestForValidUsername() {
        log.info("Processing display user request");
        result = restTemplate.getForEntity("http://localhost:8083/user/user2", UserLogin.class);
        Serenity.setSessionVariable("responseDisplayUser").to(result.getBody());
    }

    @Then("Validate response from the display user endpoint for valid username")
    public void validateResponseFromTheDisplayUserEndpointForValidUsername() {
        Assertions.assertEquals(UserLogin.builder().username("user2").password("Pass@123").build(), Serenity.getCurrentSession().get("responseDisplayUser"));
    }
}
