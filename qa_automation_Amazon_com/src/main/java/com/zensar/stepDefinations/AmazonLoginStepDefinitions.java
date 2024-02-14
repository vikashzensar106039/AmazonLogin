package com.zensar.stepDefinations;

import com.zensar.pageobjects.AmazonLandingPage;
import com.zensar.scriptutils.ScenarioState;
import com.zensar.scriptutils.StepBase;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AmazonLoginStepDefinitions extends StepBase {

	AmazonLandingPage AmazonLandingPage;
	

	public AmazonLoginStepDefinitions(ScenarioState scenarioState) {
		super(scenarioState);
		AmazonLandingPage = new AmazonLandingPage(getState());
		
		
	}

	@Given("^User navigates to Amazon Page and Login$")
	public void user_navigates_to_Amazon_Page_and_Login() throws Throwable {
		AmazonLandingPage.openAmazon();
	
	}
	 
	@When("^user logs in to Amazon by Entering Username & Password$")
	public void user_logs_in_to_Amazon_by_Entering_Username_Password() throws Throwable {
		AmazonLandingPage.loginToAmazon();
	
    }
	
	@Then("^user search for Samsung mobile phone$")
	public void user_search_for_Samsung_mobile_phone() throws Throwable {
		AmazonLandingPage.SearchProduct();
	}

	@Then("^Select the Mobile phone and move to kart$")
	public void Select_the_Mobile_phone_and_move_to_kart() throws Throwable {
		AmazonLandingPage.SelectMobile();
	}
	
	@Then("^Click on proceed to checkout$")
	public void Click_on_proceed_to_checkout() throws Throwable {
		AmazonLandingPage.Checkout();
	}
	
	@Then("^Check for Payment Details$")
	public void Check_for_Payment_Details() throws Throwable {
		AmazonLandingPage.Address();
	}
	
	
	
}

