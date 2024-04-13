package com.example.Backend.stepDefination;

import com.example.Backend.model.UserContact;
import com.example.Backend.model.UserRegister;
import com.example.Backend.model.UserResponse;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.Serenity;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
public class ContactStepDefination extends StartupHook {
    UserRegister tester = new UserRegister();
    UserContact testContact = new UserContact();
    TestRestTemplate restTemplate = new TestRestTemplate();
    ResponseEntity<UserResponse> responseRegister, responseContact;
    ResponseEntity<List<UserContact>> result;

    @Given("User calls add contact endpoint for valid username")
    public void userCallsAddContactEndpointForValidUsername() {
        tester.setUsername("newuser1");
        tester.setEmail("newtester@gmail.com");
        tester.setPassword("Pass@123");
        responseRegister = restTemplate.postForEntity("http://localhost:8083/user/register", tester, UserResponse.class);
        testContact.setUsername(tester.getUsername());
        testContact.setPhoneNumber("1234567890");
    }

    @When("System processes add contact request for valid username")
    public void systemProcessesAddContactRequestForValidUsername() {
        log.info("Processing add contact request for valid username");
        responseContact = restTemplate.postForEntity("http://localhost:8083/user/newuser1/add/contact", testContact, UserResponse.class);
        Serenity.setSessionVariable("responseContact").to(responseContact.getBody());
    }

    @Then("Validate response from the add contact endpoint for valid username")
    public void validateResponseFromTheAddContactEndpointForValidUsername() {
        Assertions.assertEquals(UserResponse.builder().Message("User Contact Details Added!: " + tester.getUsername()).build(), Serenity.getCurrentSession().get("responseContact"));
    }

    @Given("User calls display contact endpoint for valid username")
    public void userCallsDisplayContactEndpointForValidUsername() {
        testContact.setUsername("newuser1");
    }

    @When("System processes display contact request for valid username")
    public void systemProcessesDisplayContactRequestForValidUsername() {
        log.info("Processing display contact request for valid username");
        result = restTemplate.exchange(
                "http://localhost:8083/user/newuser1/contact",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserContact>>() {}
        );
        Serenity.setSessionVariable("responseContactDisplay").to(result.getBody());
    }

    @Then("Validate response from the display contact endpoint for valid username")
    public void validateResponseFromTheDisplayContactEndpointForValidUsername() {
        Assertions.assertEquals(List.of(UserContact.builder().username("newuser1").phoneNumber("1234567890").build()), Serenity.getCurrentSession().get("responseContactDisplay"));
    }

}
