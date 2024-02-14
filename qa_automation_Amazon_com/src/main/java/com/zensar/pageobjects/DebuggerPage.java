package com.zensar.pageobjects;

import com.zensar.scriptutils.PageBase;
import com.zensar.scriptutils.ScenarioState;


//Page object specificaly created to develop and test the framework features

public class DebuggerPage extends PageBase {

	public DebuggerPage(ScenarioState state) {
		super(state);
	}
	
	
	public void testFunctions(){
		startHarPage("Landing Page");
		openURL("https://ngsqa.isg-one.com/home");
		pause(100);
		
	}
	

}
