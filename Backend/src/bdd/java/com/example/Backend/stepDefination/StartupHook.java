package com.example.Backend.stepDefination;

import com.example.Backend.BackendApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = BackendApplication.class,webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = "bdd")
public class StartupHook {
}
