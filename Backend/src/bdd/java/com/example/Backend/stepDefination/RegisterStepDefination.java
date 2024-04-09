package com.example.Backend.stepDefination;

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
public class RegisterStepDefination extends StartupHook {
    UserRegister tester = new UserRegister();
    TestRestTemplate restTemplate = new TestRestTemplate();
    ResponseEntity<UserResponse> response;

    @Given("User calls register endpoint")
    public void userCallsRegisterEndpoint() {
        tester.setUsername("user1");
        tester.setEmail("tester@gmail.com");
        tester.setPassword("Pass@123");
        response = restTemplate.postForEntity("http://localhost:8083/user/register", tester, UserResponse.class);
    }

    @When("System processes register request")
    public void systemProcessesRegisterRequest() {
        log.info("Processing register request");
        Serenity.setSessionVariable("response").to(response.getBody());
    }

    @Then("Validate response from the register endpoint")
    public void validateResponseFromTheRegisterEndpoint() {
        Assertions.assertEquals(UserResponse.builder().Message("User Registered Successfully!: " + tester.getUsername()).build(), Serenity.getCurrentSession().get("response"));
    }

    @Then("Validate response from the register endpoint for existing user")
    public void validateResponseFromTheRegisterEndpointForExistinguser() {
        Assertions.assertEquals(UserResponse.builder().Message("User Already Registered").build(), Serenity.getCurrentSession().get("response"));
    }
}
