package com.zensar.scriptutils;

import com.zensar.utilities.StringLiterals;

public class StepBase implements StringLiterals{

	private ScenarioState scenarioState;	
	
	public StepBase(ScenarioState  scenarioState) {
		this.scenarioState = scenarioState;
	}
	
	public ScenarioState getState() {
		return this.scenarioState;
	}
	
	
}
