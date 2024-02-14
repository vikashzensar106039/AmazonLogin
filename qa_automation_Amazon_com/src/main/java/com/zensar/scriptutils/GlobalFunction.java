package com.zensar.scriptutils;

import static org.testng.Assert.assertTrue;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import com.zensar.excel.ExcelExecutionProperty;
import com.zensar.excel.ExcelInput;
import com.zensar.excel.ExcelMulti;
import com.zensar.excel.ExcelOutput;
import com.zensar.utilities.StringLiterals;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.windows.WindowsDriver;

abstract public class GlobalFunction implements StringLiterals {

	protected EventFiringWebDriver remoteDriver;
	protected WebDriverWait wait;
	protected Actions actions;
	protected JavascriptExecutor jsExecutor;
	protected ScenarioState state;
	private String saleSheetName = "AllSalesSheet";
	private int timeOut=10;
	private boolean isMobile=false;
	public enum DriverType{
		Mobile,Windows
	}

	protected void registerCall(String functionName) {

		System.out.println(functionName);
		state.registerGlobalFunctionCall(this.getClass().getSimpleName(), functionName);
	}

	protected void registerCall(String functionName, String data) {
		state.registerGlobalFunctionCall(this.getClass().getSimpleName(), functionName, data);
	}

	protected String getCurrentPageTitle() {
		return remoteDriver.getTitle();
	}

	public GlobalFunction(ScenarioState state) {
		this.state = state;
		this.remoteDriver = state.getEventFiringDriver();
		PageFactory.initElements(remoteDriver, this);
		wait = new WebDriverWait(remoteDriver, 15);
		actions = new Actions(remoteDriver);
		jsExecutor = (JavascriptExecutor) remoteDriver;
	}
	protected boolean jsClearAndEnterText(WebElement element, String text) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), text);
		try {
			while (element.getAttribute("value").length() > 0) {
				element.sendKeys(Keys.BACK_SPACE);
			}
			jsExecutor.executeScript("arguments[0].value='" + text + "';", element);
		} catch (Exception e) {
			logError("Error while JS click", e);
			e.printStackTrace();
			throw e;
		}
		return true;
	}
	@SuppressWarnings("deprecation")
	protected void clickAndEnterTextPerformance(WebElement element, String text) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), text);
		remoteDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
			
			Wait<WebDriver> wait = new FluentWait<WebDriver>(remoteDriver)							
					.withTimeout(30, TimeUnit.SECONDS) 			
					.pollingEvery(1, TimeUnit.SECONDS) 			
					.ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class);

				  wait.until(ExpectedConditions.visibilityOf(element));
				  element.clear();
				  element.click();
				  pause(1000);
			element.sendKeys(text);
			
		} catch (Exception e) {
			e.printStackTrace();
			logError("Error while click clear and EnterText", e);
		}
	}
	protected String getCurrentDateWithAddDate2() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		String time = dtf.format(now.plusDays(1));
		return time;
	}
	protected String getCurrentDateWithFullMnothName() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM yyyy");
		LocalDateTime now = LocalDateTime.now();
		String time = dtf.format(now);
		return time;
	}
	protected String getCurrentDateWithAddDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
		LocalDateTime now = LocalDateTime.now();
		String time = dtf.format(now.plusDays(1)).toLowerCase();
		return time;
	}
	protected boolean refreshPage() {

		try {
			remoteDriver.navigate().refresh();
			return true;
		} catch (Exception e) {
			return false;
		}

	}
	protected void clearAndEnterTextPerformance(WebElement element, String text) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), text);
		remoteDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
			
			Wait<WebDriver> wait = new FluentWait<WebDriver>(remoteDriver)							
					.withTimeout(30, TimeUnit.SECONDS) 			
					.pollingEvery(1, TimeUnit.SECONDS) 			
					.ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class);

				  wait.until(ExpectedConditions.visibilityOf(element));
				  element.clear();
				  element.sendKeys(text);
			
		} catch (Exception e) {
			e.printStackTrace();
			logError("Error while click clear and EnterText", e);
		}
	}

	protected boolean setTestDataValueOnline(String key, String value) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + value + ")");
		String DataSetName = state.getDataSetName();
		String[] DataSetValue = DataSetName.split("_");
		if(DataSetValue.length == 1)
		{
		System.out.println("Data set Name :"+state.getDataSetName());
		return ExcelInput.updateData("MasterCalendar", "sale_Title", key, value);
		}
		else
		{
			System.out.println("Data set Name :"+DataSetValue[1]);
			return ExcelInput.updateData("MasterCalendar", "sale_Title", key, value);
		}
	}

	
	protected boolean setTestDataValueMasterCalendar(String DataSetName, String key, String value) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + value + ")");
		
		String[] DataSetValue = DataSetName.split("_");
		if(DataSetValue.length == 1)
		{
		System.out.println("Data set Name :"+state.getDataSetName());
		return ExcelInput.updateData("MasterCalendar", DataSetName, key, value);
		}
		else
		{
			System.out.println("Data set Name :"+DataSetValue[1]);
			return ExcelInput.updateData("MasterCalendar", DataSetValue[1], key, value);
		}
	}
	
	protected String getCurrentTimeStamp() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMMyyyy_hhmm");
		LocalDateTime now = LocalDateTime.now();
		String time = dtf.format(now).toLowerCase();
		return time;
	}
	protected String getCurrentTimeStampwithSec() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMMyyyy_hhmmss");
		LocalDateTime now = LocalDateTime.now();
		String time = dtf.format(now).toLowerCase();
		return time;
	}
	protected ArrayList<Map<String, String>> getMultiExcelData(String dataName) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), dataName);
		return ExcelMulti.getData(dataName);
	}
	protected String getCurrentDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		String time = dtf.format(now).toLowerCase();
		return time;
	}
	protected String getCurrentDateInMonth() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		String time = dtf.format(now).toLowerCase();
		return time;
	}protected String getCurrentDateInMonthWithSpace() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
		LocalDateTime now = LocalDateTime.now();
		String time = dtf.format(now).toLowerCase();
		return time;
	}
	protected String getInputText(WebElement element) throws Throwable {
		String text = null;
		try {
			text = element.getAttribute("value");
		} catch (Exception e) {
			logReportMessage(e.getMessage());
			e.printStackTrace();
		}
		return text;
	}
	public GlobalFunction(ScenarioState state, boolean isMobile) {
			this.state = state;
			if (this.isMobile=isMobile) {
				IOSDriver driver = (IOSDriver) state.getMobileDriver();
				PageFactory.initElements(new AppiumFieldDecorator(driver), this);
				wait = new WebDriverWait(state.getMobileDriver(), 20);
				actions = new Actions((IOSDriver) state.getMobileDriver());
				jsExecutor = (JavascriptExecutor) state.getMobileDriver();
			} else {

				this.remoteDriver = state.getEventFiringDriver();
				PageFactory.initElements(remoteDriver, this);
				wait = new WebDriverWait(remoteDriver, 20);
				actions = new Actions(remoteDriver);
				jsExecutor = (JavascriptExecutor) state.getDriver();
			}
	}
	
	public GlobalFunction(ScenarioState state, DriverType type) {
		this.state = state;
		switch (type) {
		case Mobile:
			IOSDriver driver = (IOSDriver) state.getMobileDriver();
			PageFactory.initElements(new AppiumFieldDecorator(driver), this);
			wait = new WebDriverWait(state.getMobileDriver(), 20);
			actions = new Actions((IOSDriver) state.getMobileDriver());
			jsExecutor = (JavascriptExecutor) state.getMobileDriver();
			break;
		case Windows:
			WindowsDriver driverWin = (WindowsDriver) state.getWindowsDriver();
			PageFactory.initElements(new AppiumFieldDecorator(driverWin), this);
			wait = new WebDriverWait(state.getWindowsDriver(), 20);
			actions = new Actions((WindowsDriver) state.getWindowsDriver());
			jsExecutor = (JavascriptExecutor) state.getWindowsDriver();
			break;
		default:
			this.remoteDriver = state.getEventFiringDriver();
			PageFactory.initElements(remoteDriver, this);
			wait = new WebDriverWait(remoteDriver, 20);
			actions = new Actions(remoteDriver);
			jsExecutor = (JavascriptExecutor) state.getDriver();
			break;
		}
}

	public RemoteWebDriver getRemoteWebDriver() {
		if (isMobile)
			return state.getMobileDriver(); //Return mobile driver if mobile execution
		else
			return state.getDriver();	//Return WebDriver for web execution
	}

	
	protected ScenarioState getState() {
		return state;
	}

	protected EventFiringWebDriver getDriver() {
		return this.remoteDriver;
	}

	
	protected void clickPseudoElement(WebElement element) {
		getDriver().switchTo().defaultContent();
		Actions build = new Actions(getDriver());
		build.moveToElement(element).contextClick().build().perform();

	}
	

	protected String getPageLoadStatus() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			jsExecutor.executeScript("return document.readyState");
			jsExecutor.toString();
			return (String) jsExecutor.executeScript("return document.readyState");
		} catch (Exception e) {
			logError("Error while JS click", e);
			e.printStackTrace();
			throw e;
		}
	}

	
	protected void scrollIntoView(WebElement element) {
		jsExecutor.executeScript("arguments[0].scrollIntoView();", element);
	}

	

	protected void fileUpload(WebElement element, String fileName) {
		String filePath = UPLOAD_FILES_PATH + "/" + fileName;
		System.out.println(filePath);

		enterText(element, fileName);

		
	}
	
	protected void pressKey(int keyEvent) {
		try {
			Robot r=new Robot();
			r.keyPress(keyEvent);
			r.keyRelease(keyEvent);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	protected boolean openURL(String url) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			remoteDriver.get(url);
			return true;
		} catch (WebDriverException e) {
			e.printStackTrace();
			logError("Exception while opening the page", e);
			remoteDriver.get(url);
			return false;
		} finally {
			if (!isMobileBrowser())
				remoteDriver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);
		}

	}

	
	protected String getCurrentUrl() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		return remoteDriver.getCurrentUrl();
	}

	

	protected boolean click(WebElement element) throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			if (isMobileBrowser()) {
				boolean result = jsClick(element);
				return result;
			} else {
				// scrollToElement(element);
				actions.click(element).build().perform();
				return true;
			}
		} catch (NoSuchElementException e) {
			logError("Element Not found exception while ckicking on the elemnt", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption while ckicking on the element", e);
			throw e;
		}

	}

	
	protected boolean mouseHover(WebElement element) throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			actions.moveToElement(element).build().perform();
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception while hovering on the elemnt", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption while hovering on the element", e);
			throw e;
		}

	}

	protected boolean navigateBack() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			getDriver().navigate().back();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	

	protected WebElement getElementByXpath(String xpath) throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		WebElement element = null;
		try {
			// element = remoteDriver.findElementByXPath(xpath);
			element = remoteDriver.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
		return element;
	}

	protected List<WebElement> getElementsByXpath(String xpath)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		List<WebElement> element = null;
		try {
			// element = remoteDriver.findElementByXPath(xpath);
			element = remoteDriver.findElements(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
		return element;
	}

	protected WebElement getElementById(String idValue) throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		WebElement element = null;
		try {
			// element = remoteDriver.findElementByXPath(xpath);
			element = getDriver().findElement(By.id(idValue));
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
		return element;
	}

	protected boolean moveAndClick(WebElement element) throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			// scrollToElement(element);
			actions.moveToElement(element).click().build().perform();
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
	}

	protected boolean enterText(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), text);
		try {
			element.sendKeys(text);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when entering text in the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when entering text in finding the element", e);
			throw e;
		}
	}

	protected boolean pressKey(WebElement element, Keys key)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			element.sendKeys(key);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when entering text in the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when entering text in finding the element", e);
			throw e;
		}
	}

	protected String getText(WebElement element) throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		String text = null;
		try {
			text = element.getText();
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when getting text from the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when getting text from the element", e);
			throw e;
		}
		return text;
	}

	protected String getJSInputText(WebElement element){
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		String text = null;
		try {
			text = (String) jsExecutor.executeScript("return arguments[0].value", element);
		} catch (Exception e) {
			logReportMessage(e.getMessage());
			e.printStackTrace();
		}
		return text;
	}

	public String getInputTextOrText(WebElement element) throws Throwable {
		String text = "";
		text = getText(element);
		if (text == null || text.trim().equalsIgnoreCase("")) {
			text = getJSInputText(element);
		}
		System.out.println("getInputTextOrText : " + text);
		return text;
	}

	protected boolean moveToElement(WebElement element) throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			actions.moveToElement(element).build().perform();
			return true;
		} catch (NoSuchElementException e) {

			logError("Element Not found exception when moving to the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when moving to the element", e);
			throw e;
		}
	}
	protected boolean clearText(WebElement element) throws Throwable {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			isElementInteractable(element);
			actions.click(element).build().perform();
			isElementInteractable(element);
			element.clear();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	protected boolean clearTextByKeyEvents(WebElement element) throws Throwable {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			isElementInteractable(element);
			actions.click(element).build().perform();
			element.sendKeys(Keys.chord(Keys.CONTROL,"A"));
			pause(1000);
			element.sendKeys(Keys.chord(Keys.BACK_SPACE));
			pause(1000);
			element.sendKeys(Keys.chord(Keys.BACK_SPACE));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	protected boolean clickAndEnterText(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			if (isMobileBrowser()) {
				click(element);
				enterText(element, text);
				return true;
			} else {
				actions.click(element).sendKeys(text).build().perform();
				return true;
			}
		} catch (NoSuchElementException e) {
			logError("Element Not found exception ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption", e);
			throw e;
		}

	}

	protected boolean clickAndEnterTextAndEnter(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			if (isMobileBrowser()) {
				click(element);
				enterText(element, text);
				return true;
			} else {
				actions.click(element).sendKeys(text).sendKeys(Keys.ENTER).build().perform();
				return true;
			}
		} catch (NoSuchElementException e) {
			logError("Element Not found exception ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption", e);
			throw e;
		}

	}

	protected boolean moveClickAndEnterText(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			actions.moveToElement(element).click().build().perform();
			enterText(element, text);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption", e);
			throw e;
		}

	}

	protected boolean moveClickClearAndEnterText(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			actions.moveToElement(element).click().build().perform();
			element.clear();
			element.sendKeys(text);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption", e);
			throw e;
		}
	}

	protected boolean clearAndEnterText(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			element.clear();
			element.sendKeys(text);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption", e);
			throw e;
		}
	}

	protected boolean moveAndClearText(WebElement element)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			actions.moveToElement(element).click().build().perform();
			element.clear();
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption", e);
			throw e;
		}
	}

	protected boolean switchToiFrame(WebElement element) throws Exception {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			remoteDriver.switchTo().frame(element);
			return true;
		} catch (Exception e) {
			logError("Exception while switching to Iframe", e);
			throw e;
		}
	}

	public int pause(int millis) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), millis + "");
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return millis;
	}

	protected boolean doesParamElementExist(String xpath, String parameter) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			WebElement element = null;
			xpath = xpath.replaceFirst(VAR, parameter);
			element = remoteDriver.findElement(By.xpath(xpath));
			if (element.isDisplayed()) {
				return true;
			} else {
				return false;
			}
		} catch (NoSuchElementException e) {
			return false;
		}

	}

	protected boolean doesParamElementExist(String xpath, String parameter1, String parameter2) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			WebElement element = null;
			xpath = xpath.replaceFirst(VAR, parameter1);
			xpath = xpath.replaceFirst(VAR, parameter2);

			element = remoteDriver.findElement(By.xpath(xpath));
			if (element.isDisplayed()) {
				return true;
			} else {
				return false;
			}
		} catch (NoSuchElementException e) {
			return false;
		}

	}

	protected WebElement getElemtFromParamXpath(String xpath, String parameter)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		WebElement element = null;
		try {
			xpath = xpath.replaceFirst(VAR, parameter.trim());
			System.out.println("xpath" + xpath);
			element = remoteDriver.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			embedScreenshot();
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
		return element;
	}

	protected String getXpathFromParam(String xpath, String parameter) {
		return xpath.replaceFirst(VAR, parameter.trim());
	}
	protected List<WebElement> getElemtsFromParamXpath(String xpath, String parameter)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		List<WebElement> element = null;
		try {
			xpath = xpath.replaceFirst(VAR, parameter.trim());
			System.out.println("xpath" + xpath);
			element = remoteDriver.findElements(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
		return element;
	}

	protected WebElement getElemtFromParamXpath(String xpath, String parameter1, String parameter2)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		WebElement element = null;
		try {
			xpath = xpath.replaceFirst(VAR, parameter1);
			xpath = xpath.replaceFirst(VAR, parameter2);
			element = remoteDriver.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
		return element;
	}

	protected WebElement getElemtFromParamXpath(String xpath, String parameter1, String parameter2, String parameter3)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		WebElement element = null;
		try {
			xpath = xpath.replaceFirst(VAR, parameter1);
			xpath = xpath.replaceFirst(VAR, parameter2);
			xpath = xpath.replaceFirst(VAR, parameter3);
			element = remoteDriver.findElement(By.xpath(xpath));
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when finding the element", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when finding the element", e);
			throw e;
		}
		return element;
	}

	protected boolean selectBoxByVisibleText(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			Select select = new Select(element);
			select.selectByVisibleText(text);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when selecting option by visible text", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when selecting option by visible text", e);
			throw e;
		}
	}
	
	protected boolean selectBoxByVisibleTextContains(WebElement element, String containingText)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			Select select = new Select(element);
			for(WebElement ele : select.getOptions()){
				String optionVal=ele.getText();
				if(optionVal.contains(containingText)) {
					select.selectByVisibleText(optionVal);
					break;
				}
			};
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when selecting option by visible text", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when selecting option by visible text", e);
			throw e;
		}
	}


	protected boolean selectBoxByValue(WebElement element, String text)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			Select select = new Select(element);
			select.selectByValue(text);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when selecting option by value", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when selecting option by value", e);
			throw e;
		}
	}

	protected boolean selectBoxByIndex(WebElement element, int index)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			Select select = new Select(element);
			select.selectByIndex(index);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when selecting option by value", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when selecting option by value", e);
			throw e;
		}
	}

	protected boolean scrollToElement(WebElement element)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when scrolling to element (JavaScript)", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when scrolling to element (JavaScript)", e);
			throw e;
		}
	}
	

	protected boolean scrollToBottom() throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			jsExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when scrolling to element (JavaScript)", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when scrolling to element (JavaScript)", e);
			throw e;
		}
	}

	protected boolean safeJavaScriptClick(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			if (element.isEnabled() && element.isDisplayed()) {
				((JavascriptExecutor) remoteDriver).executeScript("arguments[0].click();", element);
			} else {
				scrollToElement(element);
				click(element);
			}
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when scrolling to element (JavaScript)", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when scrolling to element (JavaScript)", e);
			throw e;
		}
	}

	protected void waitForInvisibility(WebElement webElement, int maxSeconds) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		Long startTime = System.currentTimeMillis();

		while (System.currentTimeMillis() - startTime < maxSeconds * 1000 && webElement.isDisplayed()) {
		}

	}

	public void logReportMessage(String message) {
		getState().getScenario().write("[INFO]\t" + message);
		
	}
	
	public void logReportRaw(String message) {
		getState().getScenario().write(message);
		
	}

	protected void logError(String message, Exception e) {
		getState().getScenario().write("[Error]\t" + message);
		
	}
	

	protected void logError(String message, Throwable e) {
		getState().getScenario().write("[Error]\t" + message);
		
	}

	protected boolean switchTab() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		Set<String> handles = remoteDriver.getWindowHandles();
		try {
			for (String h : handles) {
				if (!h.equals(remoteDriver.getWindowHandle())) {
					WebDriver wd = remoteDriver.switchTo().window(h);
					System.out.println("CURRENT WINDOW TITLE : " + wd.getTitle());
					break;
				}
			}
			return true;
		} catch (Exception e) {
			logError("Error while switching tab. (No other tab found)", e);
			throw e;
		}
	}

	protected boolean doesElementExist(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			if (element.isDisplayed()) {
				return true;
			} else {
				return false;
			}
		} catch (NoSuchElementException e) {
			return false;
		}

	}

	protected boolean jsClick(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			jsExecutor.executeScript("arguments[0].click();", element);
			return true;
		} catch (Exception e) {
			logError("Error while JS click", e);
			e.printStackTrace();
			throw e;
		}
	}

	protected void zAssertTrueAndTakeScreenshot(boolean condition, String successMessage, String failMessage) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			if (!condition) {
				embedScreenshot(failMessage);
				logReportMessage("[Assertion Faliure]\t" + failMessage);
			}
			assertTrue(condition);
			logReportMessage("[Assertion Pass]\t" + successMessage);
			embedScreenshot(successMessage);
		} catch (Exception e) {
			logReportMessage("[Assertion Faliure]\t" + failMessage);
			embedScreenshot(failMessage);
			throw e;
		}
	}
	
	protected void zAssertTrueAndTakeScreenshot(boolean condition, String message) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			if (!condition) {
				embedScreenshot(message);
				logReportMessage("[Assertion Faliure]\t" + message);
			}
			assertTrue(condition);
			logReportMessage("[Assertion Pass]\t" + message);
			embedScreenshot(message);
		} catch (Exception e) {
			logReportMessage("[Assertion Faliure]\t" + message);
			embedScreenshot(message);
			throw e;
		}
	}


	protected void zAssertTrue(boolean condition, String successMessage, String failMessage) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			if (!condition) {
				embedScreenshot(failMessage);
				logReportMessage("[Assertion Faliure]</b>\t" + failMessage);
			}

			assertTrue(condition);
			logReportMessage("[Assertion Pass]\t" + successMessage);
		} catch (Exception e) {
			logReportMessage("[Assertion Faliure]\t" + failMessage);
			throw e;
		}
	}
	
	protected void zAssertTrue(boolean condition, String message) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			if (!condition) {
				embedScreenshot(message);
				logReportMessage("[Assertion Faliure]\t" + message);
			}

			assertTrue(condition);
			logReportMessage("[Assertion Pass]\t" + message);
		} catch (Exception e) {
			logReportMessage("[Assertion Faliure]\t" + message);
			throw e;
		}
	}
	protected void zAssertEquals(String actual,String expected, String message) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		String result="\nActual:"+actual+"\nExpected"+expected;
		try {
			boolean condition=actual.equals(expected);
			if (!condition) {
				embedScreenshot(message);
				logReportMessage("[Assertion Faliure]\t" + message + result);
			}

			assertTrue(condition);
			logReportMessage("[Assertion Pass]\t" + message + result);
		} catch (Exception e) {
			logReportMessage("[Assertion Faliure]\t" + message + result);
			throw e;
		}
	}

	protected boolean acceptAlert() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		for (int i = 0; i < 10; i++) {
			try {
				Alert newAlert = getDriver().switchTo().alert();
				pause(1000);
				System.out.println("Alert Message : " + newAlert.getText());
				pause(1000);
				newAlert.accept();
				return true;
			} catch (Exception e) {
				System.out.println("Waiting for alert");
				pause(1000);
			}
		}

		try {
			getDriver().switchTo().alert().accept();
			return true;
		} catch (Exception e) {
			System.out.println("No alert found after waiting for 10 Seconds");
			logError("No alert found exception", e);
			throw e;
		}
	}
	
	protected String acceptAlertGetAlertMsg() {
		return acceptAlertGetAlertMsg(10);
	}

	protected String acceptAlertGetAlertMsg(int timeOut) {
		String msg;
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		for (int i = 0; i < timeOut; i++) {
			try {
				msg = remoteDriver.switchTo().alert().getText();
				remoteDriver.switchTo().alert().accept();
				return msg;
			} catch (Exception e) {
				System.out.println("Waiting for alert");
				pause(1000);
			}
		}

		try {
			msg = remoteDriver.switchTo().alert().getText();
			remoteDriver.switchTo().alert().accept();
			return msg;

		} catch (Exception e) {
			System.out.println("No alert found after waiting for 10 Seconds");
			logError("No alert found exception", e);
			throw e;
		}
	}

	Queue<String> stylesQueue = new LinkedList<>();

	public void embedScreenshot(List<WebElement> list) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		try {
			for (WebElement ele : list) {
				highlightElement(ele);
			}
			
			attachScreenshot();
			for (WebElement ele : list) {
				unHighlightElement(ele);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logError("Error while taking the screenshot", e);
		}
	}

	public void embedScreenshot(List<WebElement> list, String reportLog) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		try {
			for (WebElement ele : list) {
				highlightElement(ele);
			}
			
			attachScreenshot();
			for (WebElement ele : list) {
				unHighlightElement(ele);
			}
			logReportMessage(reportLog);
		} catch (Exception e) {
			e.printStackTrace();
			logError("Error while taking the screenshot", e);
		}
	}

	public void embedScreenshot() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		
		try {
			attachScreenshot();
		} catch (Exception e) {
			logError("Error while taking screenshot", e);
			e.printStackTrace();
		}
	}

	public void embedScreenshot(String title) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		
		try {
			attachScreenshot();
		} catch (Exception e) {
			logError("Error while taking screenshot", e);
			e.printStackTrace();
		}
	}

	public void embedScreenshot(WebElement webElement) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		try {
			highlightElement(webElement);
			attachScreenshot();
			unHighlightElement(webElement);
		} catch (Exception e) {
			e.printStackTrace();
			logError("Error while taking the screenshot", e);
		}

	}
	
	protected boolean scrollElementToCenter(WebElement element)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			jsExecutor.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", element);
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception when scrolling to element (JavaScript)", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption when scrolling to element (JavaScript)", e);
			throw e;
		}
	}
	
	
	
	protected boolean forceClick(WebElement ele) {
		return forceClick(ele,timeOut);
	}

	protected boolean forceClick(WebElement ele, int maxTimeOutInSeconds) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		long startTime=System.currentTimeMillis();
		boolean isSuccess = false;

		Exception exception = null;
		while (!isSuccess) {
			long timeNow=System.currentTimeMillis()-startTime;

			if (timeNow >= (maxTimeOutInSeconds * 100))
				return isSuccess;
			pause(500);
			try {
				ele.click();
				isSuccess = true;
				return isSuccess;

			} catch (Exception e) {
				exception = e;
			}
		}
		if (exception != null)
			throw new RuntimeException(exception);
		else
			return isSuccess;

	}

	
	protected boolean forceScrollToElement(WebElement ele, int maxTimeOutInSeconds) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		boolean success = false;
		int interval = 0;
		Exception exception = null;
		while (!success) {

			if (interval >= (maxTimeOutInSeconds * 100))
				break;
			pause(interval += 500);
			try {
				jsExecutor.executeScript("arguments[0].scrollIntoView(true);", ele);
				return success = true;
			} catch (NoSuchElementException e) {
				logError("Element Not found exception when scrolling to element (JavaScript)", e);
				exception = e;
			} catch (StaleElementReferenceException e) {
				logError("Stale element exeption when scrolling to element (JavaScript)", e);
				exception = e;

			}
		}
		if (exception != null) {
			exception.printStackTrace();
			throw new RuntimeException(exception.getMessage());
			// throw exception;
		} else
			return success;

	}
	
	protected boolean forceScrollElementToCenter(WebElement ele, int maxTimeOutInSeconds) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		boolean success = false;
		int interval = 0;
		Exception exception = null;
		while (!success) {

			if (interval >= (maxTimeOutInSeconds * 100))
				break;
			pause(interval += 500);
			try {
				jsExecutor.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'})", ele);
				return success = true;
			} catch (NoSuchElementException e) {
				logError("Element Not found exception when scrolling to element (JavaScript)", e);
				exception = e;
			} catch (StaleElementReferenceException e) {
				logError("Stale element exeption when scrolling to element (JavaScript)", e);
				exception = e;

			}
		}
		if (exception != null) {
			exception.printStackTrace();
			throw new RuntimeException(exception.getMessage());
			} else
			return success;

	}

	public void waitForceScrollScreenshotForceClick(WebElement element, int maxTimeOutInSeconds) throws Exception {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		waitForElement(element);
		forceScrollToElement(element, maxTimeOutInSeconds);
		pause(1500);
		embedScreenshot(element);
		forceClick(element, maxTimeOutInSeconds);
	}

	public void waitForceScrollForceClick(WebElement element, int maxTimeOutInSeconds) throws Exception {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		waitForElement(element, maxTimeOutInSeconds);
		forceScrollToElement(element, maxTimeOutInSeconds);
		forceClick(element, maxTimeOutInSeconds);
	}

	public void forceScrollClick(WebElement element, int maxTimeOutInSeconds) throws Exception {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		forceScrollToElement(element, maxTimeOutInSeconds);
		forceClick(element, maxTimeOutInSeconds);
	}

	public void waitForceScrollScreenshot(WebElement element, int maxTimeOutInSeconds) throws Exception {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		waitForElement(element);
		forceScrollToElement(element, maxTimeOutInSeconds);
		pause(1000);
		embedScreenshot(element);
	}
	
	public void waitForceScrollClick(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		waitForElement(element, timeOut);
		forceScrollToElement(element, timeOut);
		forceClick(element, timeOut);
	}
	public void waitForceScroll(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		waitForElement(element, timeOut);
		forceScrollToElement(element, timeOut);
	}
	public void waitForceScrollScreenshotClick(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		waitForElement(element, timeOut);
		forceScrollToElement(element, timeOut);
		embedScreenshot(element);
		forceClick(element, timeOut);
	}
	public void waitForceClick(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		waitForElement(element, timeOut);
		forceClick(element, timeOut);
	}
	public void waitScreenshotForceClick(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		waitForElement(element, timeOut);
		embedScreenshot(element);
		forceClick(element, timeOut);
	}
	public void waitForceScrollCenterClick(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		waitForElement(element, timeOut);
		forceScrollElementToCenter(element, timeOut);
		forceClick(element, timeOut);
	}

	

	private void attachScreenshot() {
		TakesScreenshot camera = (TakesScreenshot) getRemoteWebDriver();
		byte[] screenshot = camera.getScreenshotAs(OutputType.BYTES);
		getState().getScenario().embed(screenshot, "image/png");

	}

	
	protected void highlightElement(WebElement webElement) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			String style = (String) jsExecutor.executeScript("arguments[0].getAttribute('style')", webElement);
			jsExecutor.executeScript("arguments[0].setAttribute('style', 'border:4px dashed #F00')", webElement);
			stylesQueue.add(style);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	protected void unHighlightElement(WebElement webElement) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			String style = stylesQueue.poll();
			jsExecutor.executeScript("arguments[0].setAttribute('style', '" + style + "')", webElement);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void switchToMainWindow() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			remoteDriver.switchTo().defaultContent();
		} catch (Exception e) {
			logReportMessage("Already On Main Window");
		}
	}

	public boolean switchToTabWithTitle(String title) {
		String baseHandel = null;
		Set<String> handles = getDriver().getWindowHandles();
		for (String handle : handles) {
			getDriver().switchTo().window(handle);
			System.out.println(getDriver().getTitle().trim().concat("-------------"));
			if (getDriver().getTitle().trim().contains(title))
				baseHandel = getDriver().getWindowHandle();
		}
		getDriver().switchTo().window(baseHandel);
		return false;
	}

	public boolean switchTabIfNewTabOpens() {
		Set<String> handles = getDriver().getWindowHandles();
		if (handles.size() > 1) {
			switchTab();
		}
		return true;
	}

	protected void closeCurrentTab() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		remoteDriver.close();
	}

	protected boolean isPresent(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		remoteDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			element.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			remoteDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
	}

	protected Boolean isVisibleInViewport(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		return (Boolean) ((JavascriptExecutor) remoteDriver).executeScript(
				"var elem = arguments[0],                 " + "  box = elem.getBoundingClientRect(),    "
						+ "  cx = box.left + box.width / 2,         " + "  cy = box.top + box.height / 2,         "
						+ "  e = document.elementFromPoint(cx, cy); " + "for (; e; e = e.parentElement) {         "
						+ "  if (e === elem)                        " + "    return true;                         "
						+ "}                                        " + "return false;                            ",
				element);
	}

	protected boolean waitForElement(WebElement element, int timeout) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			element = new WebDriverWait(getDriver(), 20).until(ExpectedConditions.visibilityOf(element));
		} catch (StaleElementReferenceException e) {
			element = new WebDriverWait(getDriver(), 10).until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			embedScreenshot();
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		return true;
	}

	protected boolean waitForElementToLocate(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		for (int i = 0; i < 40; i++) {
			pause(1000);
			if (isPresent(element) && element.isDisplayed()) {
				break;
			} else {
				System.out.println("Element Not Present : " + i);
			}
		}

		return true;

	}

	protected boolean waitForElement(WebElement element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		int tryCount = PAGE_IMPLICIT_WAIT / 2;
		int currentCount = 0;
		int time = 0;
		while (!isPresent(element) && currentCount++ < tryCount) {
			pause(2000);
			time = (time + 1);
			System.out.println(time);
		}
		if (isPresent(element))
			return true;
		else {
			remoteDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			try {
				element.isDisplayed();
				return true;
			} catch (NoSuchElementException e) {
				embedScreenshot();
				remoteDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
				throw e;
			} catch (Exception e) {
				remoteDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
				throw e;
			}

		}
	}

	protected boolean waitForElement(String element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		int tryCount = PAGE_IMPLICIT_WAIT / 2;
		int currentCount = 0;
		int time = 0;
		while (!isPresent(element) && currentCount++ < tryCount) {
			pause(2000);
			time = (time + 1);
			System.out.println(time);
		}
		if (isPresent(element))
			return true;
		else {
			remoteDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			try {
				getElementByXpath(element).isDisplayed();
				return true;
			} catch (Exception e) {
				remoteDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
				throw e;
			}

		}
	}

	protected boolean waitForElementVisibility(WebElement ele)
			throws NoSuchElementException, StaleElementReferenceException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			remoteDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			wait.until(ExpectedConditions.visibilityOf(ele));
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption", e);
			throw e;
		} finally {
			remoteDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
	}

	protected boolean waitForElementClickabality(WebElement ele)
			throws NoSuchElementException, StaleElementReferenceException, TimeoutException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			remoteDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			wait.until(ExpectedConditions.elementToBeClickable(ele));
			return true;
		} catch (NoSuchElementException e) {
			logError("Element Not found exception while waiting for clickability of element. ", e);
			throw e;
		} catch (StaleElementReferenceException e) {
			logError("Stale element exeption while waiting for clickability of element.", e);
			throw e;
		} catch (TimeoutException e) {
			logError("Timeout exeption while waiting for clickability of element.", e);
			throw e;
		} finally {
			remoteDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
	}

	protected boolean isPresent(String paramXpath, String value) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		remoteDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			remoteDriver.findElement(By.xpath(paramXpath.replaceAll(VAR, value)));
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			remoteDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
	}

	protected String base64Decode(String input) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		byte[] outputA = Base64.getDecoder().decode(input);
		String output = "";
		for (byte b : outputA) {
			output = output + (char) b;
		}
		return output;
	}

	protected String getEnvProperty(String key) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), key);
		return ExcelInput.fetchProperty("Environment_Data", key);
	}
	protected String getEnvironmentType(String key) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), key);
		return ExcelExecutionProperty.getRunnerEnvironment(key);
	}
	protected String getTestDataValue(String key) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), key);
		return ExcelInput.fetchData(state.getFeatureName(), state.getDataSetName(), key);
	}

	protected boolean setTestDataValue(String key, String value) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + value + ")");
		return ExcelInput.updateData(state.getFeatureName(), state.getDataSetName(), key, value);
	}

	protected boolean setMultiDataValue(String key, String value, String whereColName, String whereColValue) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + value + ")");
		return ExcelMulti.updateData(state.getFeatureName(), whereColName, whereColValue, key, value);
	}

	protected boolean setOutputDataValue(String key, String value) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + value + ")");
		return ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, key, value, state.getId());
	}

	protected boolean setOutputDataValue(String key, String value, int id) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + value + ")");
		return ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, key, value, id);
	}

	protected boolean insertOutputDataValue(String key, String value) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + value + ")");
		return ExcelOutput.insertOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, state.getId(), value);
	}

	protected List<String> getSaleDataValues(String columnName) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + columnName + ")");
		return ExcelInput.getColumnsAsList(saleSheetName, columnName);
	}

	protected boolean setSaleDataValues(String key, String value) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + value + ")");
		return ExcelInput.updateData(saleSheetName, state.getDataSetName(), key, value);
	}

	protected boolean clearSaleDataValues() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		return ExcelInput.clearSheet(saleSheetName);
	}

	protected void setRuntimeData(String key, String data) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + data + ")");
		System.out.println("setRuntimeData(" + key + "," + data + ")");
		getState().getScenarioMap().put(key, data);
	}
	
	protected void setRuntimeData(String key, Object data) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + data + ")");
		System.out.println("setRuntimeData(" + key + "," + data + ")");
		getState().getScenarioMap().put(key, data);
	}

	protected void setRuntimeData(String key, int data) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + data + ")");
		getState().getScenarioMap().put(key, data);
	}

	protected String getRuntimeData(String key) throws NullPointerException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + ")");
		String value;
		value = (String) getState().getScenarioMap().get(key);
		if (value == null)
			throw new NullPointerException();
		System.out.println("getRuntimeData(" + key + ")\t"+value);
		return value;
	}
	
	protected Object getRuntimeDataObj(String key) throws NullPointerException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + ")");
		Object value;
		value = getState().getScenarioMap().get(key);
		if (value == null)
			throw new NullPointerException();
		System.out.println("getRuntimeData(" + key + ")\t"+value);
		return value;
	}

	protected int getRuntimeDataInt(String key) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + ")");
		Integer value;
		value = (Integer) getState().getScenarioMap().get(key);
		if (value == null)
			throw new NullPointerException();
		return value.intValue();
	}

	protected void setRuntimeDataList(String key, List<String> data) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + "," + data.toString() + ")");
		getState().getScenarioMap().put(key, data);
	}

	protected List<String> getRuntimeDataList(String key) throws NullPointerException {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), "(" + key + ")");
		List<String> value;
		value = (List<String>) getState().getScenarioMap().get(key);
		if (value == null)
			throw new NullPointerException();
		return value;
	}

	protected boolean refresh() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			remoteDriver.navigate().refresh();
			return true;
		} catch (Exception e) {
			logError("Exception while refreshing the page", e);
			throw e;
		}

	}

	protected boolean openNewTab(String baseUrl) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName(), baseUrl);
		Robot rb;
		try {
			rb = new Robot();
			rb.keyPress(KeyEvent.VK_CONTROL);
			rb.keyPress(KeyEvent.VK_T);
			rb.keyRelease(KeyEvent.VK_CONTROL);
			rb.keyRelease(KeyEvent.VK_T);
			pause(1000);
			ArrayList<String> tabs = new ArrayList<String>(remoteDriver.getWindowHandles());
			remoteDriver.switchTo().window(tabs.get(1));
			remoteDriver.get(baseUrl);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return true;
	}

	protected void openSingleNewTabJS() throws AWTException {
		pause(2000);
		((JavascriptExecutor) remoteDriver).executeScript("window.open()");
		System.out.println("Opened new tab Java Script Executor");

	}

	protected boolean startHarPage(String pageName) {
		getState().getDriverHandler().startNewPage(pageName);
		return false;
	}

	protected String getEnvironment() {
		return getEnvProperty("Environment");
	}

	protected String getValue(WebElement element) {
		String value = element.getAttribute("value");
		return value;
		}

	protected boolean isPresent(String element) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		getRemoteWebDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {

			return getRemoteWebDriver().findElements(By.xpath(element)).size() > 0;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		} finally {
			getRemoteWebDriver().manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
	}

	
	protected boolean back() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			remoteDriver.navigate().back();
			return true;
		} catch (Exception e) {
			logError("Exception while redirecting to previous page", e);
			throw e;
		}

	}

	// splitting String from selected delimiter
	public String getTextBetweenDelmiter(String text, String delimiter1, String delimiter2) {
		String[] textAfterDelimiter = text.split("\\" + delimiter1);
		String[] textBetweenDelimiter = textAfterDelimiter[1].split("\\" + delimiter2);
		return textBetweenDelimiter[0];
	}

	// For getting the String or Text without any special character

	public String getTextWithoutFirstSpecialChar(WebElement element) {

		String returnText = "";
		String webElementText = getText(element);
		char[] character = webElementText.toCharArray();
		for (int i = 0; i < character.length; i++) {
			int asciiCode = (int) character[i];
			if (asciiCode >= 97 && asciiCode <= 122) {
				returnText = returnText + character[i];

			} else if (asciiCode >= 65 && asciiCode <= 90) {
				returnText = returnText + character[i];
			}

			else if (asciiCode >= 48 && asciiCode <= 57) {
				returnText = returnText + character[i];
			}

			else if (asciiCode == 32) {
				returnText = returnText + character[i];
			} else
				break;

		}

		return returnText;
	}

	// for checking string contains in array
	public boolean checkStringContainsForSearch(String expected, String[] actual) {
		boolean trueFlag = false;
		for (int i = 0; i < actual.length; i++) {
			if (expected.contains(actual[i])) {
				trueFlag = true;
				break;
			} else
				trueFlag = false;
		}
		return trueFlag;
	}
	protected String getRexExMatch(String str,String regExPattern) {
		System.out.println("RegEx Input String:"+str+"\nRegExPattern:"+regExPattern);
		Matcher m=Pattern.compile(regExPattern).matcher(str);
		while(m.find()) {
			System.out.println("Found Match:"+m.group());
			return m.group();
		}
		throw new RuntimeException("RegEx Match not found please check input string and regex pattern \nString:"+str+"\t RexEx Pattern:"+regExPattern);
	}

	protected boolean pressTab() {
		Robot rb;
		try {
			rb = new Robot();
			rb.keyPress(KeyEvent.VK_TAB);
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return true;
	}

	protected boolean isMobileBrowser() {
		return getState().getDriverHandler().isMobileBrowser();
	}

	protected String getFilePath() {
		String path = "";
		if (!System.getProperty("os.name").equalsIgnoreCase("linux")) {
			path = System.getProperty("user.dir") + "\\support_files\\upload_files\\";
		} else {
			path = System.getProperty("user.dir") + "/support_files/upload_files/";
		}
		return path;

	}
	
	
	public Boolean dateformatcheck(String date) throws ParseException {
	String sDate1=date;
	SimpleDateFormat format = new SimpleDateFormat("DD MMM YYYY");
	Date date1=format.parse(sDate1);
	if(sDate1.equalsIgnoreCase(date))
	{
	System.out.println("Format is correct");
	}
	return true;
	}
	
	public Boolean dateformatchecksimplifiedchinese(String dateChinese) throws ParseException {
	String sDate1=dateChinese;
	SimpleDateFormat format = new SimpleDateFormat("DD MMM YYYY");
	Date date1=format.parse(sDate1);
	if(sDate1.equalsIgnoreCase(dateChinese))
	{
	System.out.println("Format is correct");
	}
	return true;
	}
	
	
	
	
	
	public class ZSoftAssert extends SoftAssert {

		boolean takeScreenShot = false;
		boolean logReportMessage = true;

		public ZSoftAssert() {
		}
		public ZSoftAssert(boolean takeScreenshot) {
			this.takeScreenShot = takeScreenshot;
		}

		public ZSoftAssert setLogReportMessage(boolean enable) {
			this.logReportMessage = enable;
			return this;
		}

		public void setScreenShotEnabled(boolean isEnabled) {
			takeScreenShot = isEnabled;
		}

		public void assertAll(boolean takeScreenshot) {
			takeScreenShot = takeScreenshot;
			assertAll();
		}

		@Override
		public void onAfterAssert(IAssert<?> arg0) {
			if (takeScreenShot)
				embedScreenshot();
			super.onAfterAssert(arg0);
		}

		@Override
		public void onAssertSuccess(IAssert<?> arg0) {
			if (logReportMessage)
				logReportMessage("[Assertion Pass]\t" + arg0.getMessage());
			super.onAssertSuccess(arg0);
		}

		@Override
		public void onAssertFailure(IAssert<?> arg0, AssertionError arg1) {
			if (logReportMessage)
				logError(arg0.getMessage(), arg1.fillInStackTrace());
			super.onAssertFailure(arg0, arg1);
		}
		
		
		public void zAssertContains(String string, String subString) {
			assertTrue(string.contains(subString), "\nActual :" + string + "\nExpected :" + subString);
		}

		public void zAssertContains(String string, String subString, String msg) {
			assertTrue(string.contains(subString), msg + "\nActual :" + string + "\nExpected :" + subString);
		}
		public void zAssertContainsIgnoreOrder(String string, String subString, String msg) {
			assertTrue(string.contains(subString) || subString.contains(string), msg + "\nActual :" + string + "\nExpected :" + subString);
		}
		public void zAssertContainsAny(String msg, String string,String subString1,String... subString) {
			if(string.contains(subString1))
				assertTrue(string.contains(subString1), msg + "\nActual:" + string + "\nExpected Any:"+ subString1+","+ subString+"\nMatched:"+subString1);
			else
			for (int i = 0; i < subString.length; i++) {
				if(string.contains(subString[i])) {
					assertTrue(string.contains(subString[i]), msg + "\nActual:" + string + "\nExpected Any:" + subString+"\nMatched:"+subString[i]);
					break;
				}
			}
		}
		public void zAssertContainsIgnoreCase(String string, String subString, String msg) {
			assertTrue(string.toLowerCase().contains(subString.toLowerCase()), msg + "\nActual :" + string + "\nExpected :" + subString);
		}
		public void zAssertContainsAnyIgnoreCase(String string, String subString, String msg) {
			assertTrue(string.toLowerCase().contains(subString.toLowerCase()) || subString.toLowerCase().contains(string.toLowerCase()), msg + "\nActual :" + string + "\nExpected :" + subString);
		}

		public void zAssertEquals(String actual, String expected) {
			assertTrue(actual.equals(expected), "\nActual :" + actual + "\nExpected :" + expected);
		}

		public void zAssertEquals(String actual, String expected, String msg) {
			assertTrue(actual.equals(expected), msg + "\nActual :" + actual + "\nExpected :" + expected);
		}
		
		public void zAssertEquals(int actual, int expected, String msg) {
			assertTrue(actual==expected, msg + "\nActual :" + actual + "\nExpected :" + expected);
		}
		
		public void zAssertNotEquals(String actual, String expected, String msg) {
			assertTrue(!actual.equals(expected), msg + "\nActual :" + actual + "\nExpected :" + expected);
		}

		public void zAssertEqualIgnoreCase(String actual, String expected) {
			assertTrue(actual.equalsIgnoreCase(expected), "\nActual :" + actual + "\nExpected :" + expected);
		}

		public void zAssertEqualIgnoreCase(String actual, String expected, String msg) {
			assertTrue(actual.equalsIgnoreCase(expected), msg + "\nActual :" + actual + "\nExpected :" + expected);
		}
	}
	
	protected String getAttributes(WebElement ele, String attribute) {
        try {
            String value = ele.getAttribute(attribute);
            System.out.println("<b>Attribute: </b>" + value);
            return value;
        } catch (NoSuchElementException e) {
            System.out.println("Unable to find the element -- " + ele
                    + ". <br><b> The exception message is :</b> " + e.getMessage());
            Assert.fail("No such element exception: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("<p>Operation Failed<br><b>Exception:</b>" + e);
            Assert.fail("Exception: " + e.getMessage());
            return null;
        }
    }
	
	public void waitUntilElementVisible(WebElement WebElement, long timeOutInSeconds) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                new WebDriverWait(getDriver(), timeOutInSeconds).until(ExpectedConditions.visibilityOf(WebElement));
                break;
            } catch (StaleElementReferenceException e) {
            attempts++;
            }
        }
    }
	
public WebElement waitforElementPresent( WebElement locator, int timeout)
    
    {
        WebDriverWait wait= new  WebDriverWait(getDriver(),timeout);
        wait.until(ExpectedConditions.visibilityOf(locator));
        return locator;
        
        
    }
	
	
	protected void clickClearAndEnterText(WebElement element, String text) {
        registerCall(new Object() {
        }.getClass().getEnclosingMethod().getName(), text);
        try {
            isElementInteractable(element);
            actions.click(element).build().perform();
            isElementInteractable(element);
            element.clear();
            isElementInteractable(element);
            char[] txt = text.toCharArray();
            for (char c : txt) {
                if (c == '/') {
                    jsEnterChar(element);
                } else {
                    element.sendKeys(String.valueOf(c));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logError("Error while click clear and EnterText", e);
        }
    }
	
	protected boolean jsEnterChar(WebElement element) {
        registerCall(new Object() {
        }.getClass().getEnclosingMethod().getName());
        try {
            jsExecutor.executeScript("arguments[0].value=arguments[0].value+String.fromCharCode(0x2f);", element);
        } catch (Exception e) {
            logError("Error while JS click", e);
            e.printStackTrace();
            throw e;
        }
        return true;
    }
	
	protected String getCurrentDateWithSpecificFormat(String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); 
		String output = sdf.format(c.getTime());
		return output;
	}
	
	public boolean isElementInteractable(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            System.out.println("Waited for Element Visibility But Element Not Visible");
        }
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            System.out.println("Waited for Element Clickibility But Element Not Clickable");
        }
        return true;
    }
	
	protected String decodeBase64String(String encodedText) {
		byte[] decodedPwdBytes = org.apache.commons.codec.binary.Base64.decodeBase64(encodedText);
		String decodedPwd = new String(decodedPwdBytes);
		return decodedPwd;
	}
	
	
	public void  zoomIn() throws AWTException {
		
		for(int i=0; i<8; i++){
	        Robot robot = new Robot();
	        robot.keyPress(KeyEvent.VK_CONTROL);
	        robot.keyPress(KeyEvent.VK_MINUS);
	        robot.keyRelease(KeyEvent.VK_CONTROL);
	        robot.keyRelease(KeyEvent.VK_MINUS);
	        }
	}
	
}