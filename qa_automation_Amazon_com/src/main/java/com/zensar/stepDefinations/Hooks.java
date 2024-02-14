package com.zensar.stepDefinations;

import com.zensar.excel.ExcelExecutionProperty;
import com.zensar.excel.OutputSheetLogger;
import com.zensar.scriptutils.EventListenerMobile;
import com.zensar.scriptutils.ScenarioState;
import com.zensar.slack.SlackClient;
import com.zensar.utilities.StringLiterals;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

public class Hooks implements StringLiterals {
	private ScenarioState scenarioState;

	public Hooks(ScenarioState scenarioState) {
		this.scenarioState = scenarioState;
		
	}

	@Before(order=10001)
	public void setupEnvironment(Scenario scenario) {
		System.out.println("SCENARIO STATE Init");
		scenarioState.init(scenario);
		scenarioState.attach(new OutputSheetLogger());
		if (ExcelExecutionProperty.getGlobalExecutionProperty("Slack Update").equalsIgnoreCase("Yes"))
			scenarioState.attach(new SlackClient());
		if(scenarioState.getDriverHandler().isBrowserStackExecution) {
			scenarioState.attach(new EventListenerMobile());
		}
		scenarioState.initComplete();
			
	}
	
	@Before("@HAR")
	public void initMobProxy(){
		System.out.println("Browser Mob Init");
		this.scenarioState.enableMobProxy();
	}
	

	@After
	public void tearDown(Scenario scenario) {
		scenarioState.destroy();
	}

}
