package com.aeroweb.runner;

import org.junit.runner.RunWith;
import org.testng.annotations.DataProvider;

import io.cucumber.junit.Cucumber;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features",
                 glue = {"classpath:com/aeroweb/stepdefination","classpath:com/aeroweb/runner"} ,
                 plugin = {"html:target/cucumber-html-report", "json:target/cucumber.json", 
                		 "pretty:target/cucumber-pretty.txt","usage:target/cucumber-usage.json" },
                 tags ={"@TC12345"} ,
                dryRun=false
)

public class runnerFile extends AbstractTestNGCucumberTests {
	@Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
          return super.scenarios();
    }
}
