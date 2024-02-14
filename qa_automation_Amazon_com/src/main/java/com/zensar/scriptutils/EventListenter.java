package com.zensar.scriptutils;


import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class EventListenter implements ScenarioEventListener {

	

	@Override
	public void beforeAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
	}

//	@Override
//	public void beforeSwitchToWindow(String windowName, WebDriver driver) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void afterSwitchToWindow(String windowName, WebDriver driver) {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		System.out.println("Exception occured");
		
	}

//	@Override
//	public <X> void beforeGetScreenshotAs(OutputType<X> target) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void beforeGetText(WebElement element, WebDriver driver) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void afterGetText(WebElement element, WebDriver driver, String text) {
//		// TODO Auto-generated method stub	
//	}

	@Override
	public void onScenarioStart(ScenarioState scenarioState) {
		System.out.println("Scenario Start");
		
	}

	@Override
	public void onScenarioEnd(ScenarioState scenarioState) {
		System.out.println("Scenario end");
		
	}

	@Override
	public void onScenarioPass(ScenarioState scenarioState2) {
		System.out.println("Scenario pass");
		
	}

	@Override
	public void onScenarioFailed(ScenarioState scenarioState) {
		System.out.println("Scenario failed");
	}

	@Override
	public void onPageObjectChange(ScenarioState scenarioState, String previous, String current) {
		System.out.println(previous + " > " + current);
		
	}

	@Override
	public void onGlobalFunctionCalled(ScenarioState scenarioState, String pageObjectName, String functionInfo) {
		System.out.println(functionInfo);
		
	}

	@Override
	public void beforeSwitchToWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSwitchToWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <X> void beforeGetScreenshotAs(OutputType<X> target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeGetText(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterGetText(WebElement element, WebDriver driver, String text) {
		// TODO Auto-generated method stub
		
	}

}
