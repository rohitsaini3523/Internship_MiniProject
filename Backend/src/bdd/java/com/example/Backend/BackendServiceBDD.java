package com.example.Backend;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.*;

@Suite
@SuiteDisplayName("Backend service BDD tests")
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key= Constants.GLUE_PROPERTY_NAME, value="com.example.Backend.stepDefination")
public class BackendServiceBDD {

}
