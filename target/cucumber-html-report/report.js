$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("classpath:features/Logistics.feature");
formatter.feature({
  "name": "Flink Feature File",
  "description": "",
  "keyword": "Feature"
});
formatter.scenario({
  "name": "01 -TC12345- Verification of file upload and download under",
  "description": "",
  "keyword": "Scenario",
  "tags": [
    {
      "name": "@TC12345"
    },
    {
      "name": "@Regression"
    }
  ]
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "User navigated to URL",
  "keyword": "Given "
});
formatter.match({
  "location": "com.aeroweb.stepdefination.LogisticsStepDefs.user_navigated_to_login_page()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "add least expensive \"Moisturizers\" to cart if weather is \"below 19\"",
  "keyword": "And "
});
formatter.match({
  "location": "com.aeroweb.stepdefination.LogisticsStepDefs.navigate_to_page_if_weather_condition(java.lang.String,java.lang.String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "add least expensive \"Sunscreens\" to cart if weather is \"above 34\"",
  "keyword": "And "
});
formatter.match({
  "location": "com.aeroweb.stepdefination.LogisticsStepDefs.navigate_to_page_if_weather_condition(java.lang.String,java.lang.String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "click on \"Cart\" button",
  "keyword": "And "
});
formatter.match({
  "location": "com.aeroweb.stepdefination.LogisticsStepDefs.click_on_something_button(java.lang.String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "click on \"Pay with Card\" button",
  "keyword": "And "
});
formatter.match({
  "location": "com.aeroweb.stepdefination.LogisticsStepDefs.click_on_something_button(java.lang.String)"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "verify execution completed successfully",
  "keyword": "And "
});
formatter.match({
  "location": "com.aeroweb.stepdefination.LogisticsStepDefs.verify_execution_completed_successfully()"
});
formatter.result({
  "status": "passed"
});
formatter.after({
  "status": "passed"
});
});