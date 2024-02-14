package com.zensar.stepDefinations;

import com.zensar.pageobjects.DebuggerPage;
import com.zensar.scriptutils.ScenarioState;
import com.zensar.scriptutils.StepBase;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;



//Page Definition specifically created to develop and test the framework features.

public class DebugingSteps extends StepBase {
	private DebuggerPage debuggerPage; 
	public DebugingSteps(ScenarioState scenarioState) {
		super(scenarioState);
		debuggerPage = new  DebuggerPage(getState());
	}

	@Given("^step ONE$")
	public void step_ONE() throws Throwable {
		debuggerPage.testFunctions();
	}

	@When("^step TWO$")
	public void step_TWO() throws Throwable {
	}

	@When("^step THREE$")
	public void step_THREE() throws Throwable {
	}

	@When("^step FOUR$")
	public void step_FOUR() throws Throwable {
	}

	@Then("^step FIVE$")
	public void step_FIVE() throws Throwable {
	}

}
