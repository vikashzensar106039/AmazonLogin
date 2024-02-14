package com.zensar.scriptutils;

import static org.testng.Assert.assertTrue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.zensar.utilities.StringLiterals;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
abstract public class GlobalFunctionMobile extends GlobalFunction implements StringLiterals {

	int height,width;
	private int timeOut=20;
	
	public int getDeviceHeight(){
		return height;
	}
	public int getDeviceWidth(){
		return width;
	}
	
	public GlobalFunctionMobile(ScenarioState state) {
		super(state, true);

		System.out.println("DETAILS--");
		String iosVersion=((Map)(getAppiumDriver().getCapabilities().getCapability("mobile"))).get("version").toString();
		System.out.println("version-"+iosVersion);
		System.out.println(getAppiumDriver().getSessionDetails());
		width=getAppiumDriver().manage().window().getSize().getWidth();
		height=getAppiumDriver().manage().window().getSize().getHeight();
	}
	
	private void addUpdateUrl() {
		String errorMessage = "<strong>Scenario session url:</strong><br/>"; 
		String executionLink="https://app-automate.browserstack.com/builds/";
		errorMessage = errorMessage.concat("<a href=\""+executionLink+"\" target=\"_blank\">"+executionLink+"</a>"); 
		getState().getScenario().embed(errorMessage.getBytes(), "text/html");
	}
	
	protected AppiumDriver<MobileElement> getAppiumDriver() {
		return (AppiumDriver<MobileElement>) super.getRemoteWebDriver();
	}

	protected IOSDriver getIOSDriver() {
		return (IOSDriver) super.getRemoteWebDriver();
	}

	private void attachScreenshot(MobileElement element) {
		TakesScreenshot camera = (TakesScreenshot) getState().getDriverHandler().getDriver();
		try {
			BufferedImage img = ImageIO.read(camera.getScreenshotAs(OutputType.FILE));
			Graphics2D g2d = img.createGraphics();
			g2d.setColor(Color.RED);

			System.out.println(element.getLocation().x + " " + element.getLocation().y);
			
			System.out.println("SCreenDots:: " + element.getRect().getX() + " " + element.getRect().getY());
			g2d.drawRect(element.getRect().getX(), element.getRect().getY(), element.getRect().width,
					element.getRect().height);
			g2d.dispose();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "png", baos);
			byte[] screenshot = baos.toByteArray();
			getState().getScenario().embed(screenshot, "image/png");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void mobileAcceptAlert() {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());

		wait.until(ExpectedConditions.alertIsPresent());
		getRemoteWebDriver().switchTo().alert().accept();
	}

	public IOSElement getEleByXpath(String xpath) {
		IOSElement iosElement= (IOSElement) getIOSDriver().findElement(MobileBy.xpath(xpath));
		return iosElement;
	}

	public IOSElement getEleBy(By by) {
		IOSElement iosElement= (IOSElement) getIOSDriver().findElement(by);
		return iosElement;
	}

	public IOSElement getEleByXpath(String xpath, String param) {
		String finalXpath = getXpathFromParam(xpath, param);
		System.out.println("Xpath:" + finalXpath);
		IOSElement iosElement = (IOSElement) getIOSDriver().findElement(MobileBy.xpath(finalXpath));
		return iosElement;
	}
	
	public List<IOSElement> getElementsByXpath(String xpath, String param) {
		String finalXpath = getXpathFromParam(xpath, param);
		System.out.println("Xpath:" + finalXpath);
		List<IOSElement> iosElement = (List<IOSElement>) getIOSDriver().findElements(MobileBy.xpath(finalXpath));
		return iosElement;
	}
	
	public List<IOSElement> getElementsByXpath(IOSElement element, String xpath) {
		return (List<IOSElement>) element.findElementByXPath(xpath);

	}

	public IOSElement waitUntilPresent(By locator) {
		System.out.println(locator);
		IOSElement iosElement = (IOSElement) new WebDriverWait(getRemoteWebDriver(), timeOut)
				.until(ExpectedConditions.presenceOfElementLocated(locator));
		return iosElement;
	}
	


	public IOSElement waitUntilPresent(MobileElement element) {
		
		FluentWait wait = new FluentWait((IOSDriver) getRemoteWebDriver());
		wait.withTimeout(30000, TimeUnit.MILLISECONDS);
		wait.pollingEvery(500, TimeUnit.MILLISECONDS);
		wait.ignoring(NoSuchElementException.class);

		IOSElement iosElement = (IOSElement) wait.until(new Function<WebDriver, IOSElement>() {
			public IOSElement apply(WebDriver driver) {
				return (IOSElement) element;
			}
		});
		return iosElement;
	}

	public boolean isPresent(MobileElement element) {
		getRemoteWebDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		try {
			element.isDisplayed();
			return true;
		} catch (Exception e) {}
		finally {
			getRemoteWebDriver().manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
		return false;
	}

	public IOSElement waitUntilVisible(MobileElement me) {
		System.out.println(me);
		IOSElement iosElement = (IOSElement) new WebDriverWait(getRemoteWebDriver(), timeOut)
				.until(ExpectedConditions.visibilityOf(me));
		return iosElement;
	}

	public IOSElement waitUntilClickable(By locator) {
		System.out.println(locator);
		IOSElement iosElement = (IOSElement) new WebDriverWait(getRemoteWebDriver(), timeOut)
				.until(ExpectedConditions.elementToBeClickable(locator));
		return iosElement;
	}

	public IOSElement waitUntilClickable(MobileElement me) {
		System.out.println(me);
		IOSElement iosElement = (IOSElement) new WebDriverWait(getRemoteWebDriver(), timeOut)
				.until(ExpectedConditions.elementToBeClickable(me));
		return iosElement;
	}
	
	
	public IOSElement waitUntilRefreshed(MobileElement me) {
		System.out.println(me);
		IOSElement iosElement = (IOSElement) new WebDriverWait(getRemoteWebDriver(), timeOut)
				.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(me)));
		return iosElement;
	}

	public void waitAndClick(MobileElement me) {
		System.out.println(me);
		try {
			me.click();
		}catch (Exception e) {
			System.out.println("Error while clicking, waiting until clickable...\n"+e);
			waitUntilClickable(me).click();
		}
	}
	public void waitScrollSnapClick(MobileElement me){
		waitUntilPresent(me);
		autoScrollToElement(me);
		waitUntilClickable(me);
		embedScreenshot(me);
		me.click();
	}
	public void waitScrollClick(MobileElement me){
		waitUntilPresent(me);
		autoScrollToElement(me);
		waitUntilClickable(me);
		me.click();
	}
	public void waitSnapClick(MobileElement me){
		waitUntilPresent(me);
		waitUntilClickable(me);
		embedScreenshot(me);
		me.click();
	}

	public void waitScrollSnap(MobileElement me){
		waitUntilPresent(me);
		autoScrollToElement(me);
		embedScreenshot(me);
	}
	public MobileElement waitScroll(MobileElement me){
		waitUntilPresent(me);
		autoScrollToElement(me);
		return me;
	}
	public void waitSnap(MobileElement me){
		embedScreenshot(me);
	}

	public void scrollToElement(MobileElement elementToScroll) {
		Map<String, Object> params = new HashMap<>();
		params.put("direction", "down");
		params.put("element", ((RemoteWebElement) elementToScroll).getId());
		getRemoteWebDriver().executeScript("mobile: scroll", params);
	}
	
	public void scrollToElement(String name) {
		Map<String, Object> params = new HashMap<>();
		params.put("direction", "down");
		params.put("label", name);
		getRemoteWebDriver().executeScript("mobile: scroll", params);
	}
	
	public void scrollToMobileElement(String elementName, String direction) {

		final int maximumScrolls = 5;

		for (int i = 0; i < maximumScrolls; i++) {
			try {
				if (findElementsByPredicateString("label CONTAINS \"" + elementName + "\"").size() > 0)
					break;
			} catch (Exception e) {
				e.printStackTrace();
			}
			scroll(direction);
		}
	}

	private void scroll(String direction) {
		final HashMap<String, String> scrollObject = new HashMap<String, String>();
		scrollObject.put("direction", direction);
		getRemoteWebDriver().executeScript("mobile:scroll", scrollObject);
	}

	public List<WebElement> findElementsByPredicateString(String predicateString) {
		return getRemoteWebDriver().findElements(MobileBy.iOSNsPredicateString(predicateString));
	}

	@Deprecated
	public void scrollToElementSwipe(MobileElement elementToScroll) {
		RemoteWebElement parent = (RemoteWebElement) getRemoteWebDriver().findElement(
				MobileBy.iOSNsPredicateString("type==\"XCUIElementTypeOther\" AND (label CONTAINS \"tab panel\")"));

		Map<String, Object> params = new HashMap<>();
		params.put("direction", "down");
		params.put("predicateString", "id == '" + ((RemoteWebElement) elementToScroll).getId() + "'");
		params.put("element", ((RemoteWebElement) parent).getId());
		JavascriptExecutor js = (JavascriptExecutor) getRemoteWebDriver();
		getRemoteWebDriver().executeScript("mobile: scroll", params);
		params = new HashMap<>();
		params.put("direction", "up");
		params.put("velocity", 100);
		params.put("element", ((RemoteWebElement) parent).getId());
		}

	public void swipeDown(MobileElement parent) {

		Map<String, Object> params = new HashMap<>();
		params.put("direction", "down");
		params.put("element", ((RemoteWebElement) parent).getId());
		JavascriptExecutor js = (JavascriptExecutor) getRemoteWebDriver();
		params = new HashMap<>();
		params.put("direction", "down");
		params.put("velocity", 250);
		params.put("element", ((RemoteWebElement) parent).getId());
		getRemoteWebDriver().executeScript("mobile: swipe", params);
	}
	
	public void swipeUp(MobileElement parent) {

		Map<String, Object> params = new HashMap<>();
		params.put("direction", "up");
		params.put("element", ((RemoteWebElement) parent).getId());
		JavascriptExecutor js = (JavascriptExecutor) getRemoteWebDriver();

		params = new HashMap<>();
		params.put("direction", "down");
		params.put("velocity", 250);
		params.put("element", ((RemoteWebElement) parent).getId());
		getRemoteWebDriver().executeScript("mobile: swipe", params);
	}

	public void scrollToElement(MobileElement parentElement, MobileElement elementToScroll) {
		Map<String, Object> params = new HashMap<>();
		params.put("direction", "down");
		params.put("toVisible", "true");
		params.put("predicateString", "id == '" + ((RemoteWebElement) elementToScroll).getId() + "'");
		params.put("element", ((RemoteWebElement) parentElement).getId());
		JavascriptExecutor js = (JavascriptExecutor) getRemoteWebDriver();
		getRemoteWebDriver().executeScript("mobile: scroll", params);
		params = new HashMap<>();
		params.put("direction", "up");
		params.put("velocity", 100);
		params.put("element", ((RemoteWebElement) parentElement).getId());
		}

	public void swipeToElement(MobileElement elementToScroll) {

		System.out.println("Before scroll :" + elementToScroll.getLocation());
		ArrayList<Integer> scrolledPositions=new ArrayList<Integer>();
		while (!elementToScroll.isDisplayed()) {
			int eleLocationY = elementToScroll.getLocation().getY();
			scrolledPositions.add(eleLocationY);
			String swipe = eleLocationY >= 0 ? "up" : "down";

			Map<String, Object> params = new HashMap<>();
			params.put("direction", swipe);
			params.put("velocity", 1);
			getIOSDriver().executeScript("mobile: swipe", params);
			System.out.println(params);
			Point afterScroll=elementToScroll.getLocation();
			System.out.println("after scroll :" + afterScroll);
			if(eleLocationY==afterScroll.y || scrolledPositions.contains(afterScroll.y)) //Stop scrolling if element not changing its location or scrolling to same location again
				break;
			
		}
	}
	
	public void swipeInElement(MobileElement elementToScroll) {

		System.out.println("Before scroll :" + elementToScroll.getLocation());

			int eleLocationY = elementToScroll.getLocation().getY();
			String swipe = eleLocationY >= 0 ? "up" : "down";

			Map<String, Object> params = new HashMap<>();
			params.put("direction", swipe);
			params.put("velocity", 1);
			 params.put("element", ((RemoteWebElement) elementToScroll).getId());
			getIOSDriver().executeScript("mobile: swipe", params);
			System.out.println(params);
			System.out.println("after scroll :" + elementToScroll.getLocation());
	}
	
	public void selectPickerWheelNextValue(MobileElement element) {
		JavascriptExecutor js = (JavascriptExecutor) getAppiumDriver();
		Map<String, Object> params = new HashMap<>();
		params.put("order", "next");
		params.put("offset", 0.15);
		params.put("element", ((RemoteWebElement) element).getId());
		js.executeScript("mobile: selectPickerWheelValue", params);
	}

	public void autoScrollToElement(MobileElement elementToScroll) {
		int eleLocation = elementToScroll.getLocation().getY();
		boolean isFar = eleLocation > (height * 2) || eleLocation < (-(height * 2)) ? true : false;

			dragToElement(elementToScroll, true);

		eleLocation = elementToScroll.getLocation().getY();

		boolean isNearTop = (height * 0.25) > eleLocation ? true : false;
		boolean isNearDown = (height * 0.75) < eleLocation ? true : false;
		boolean isNearCenter = isNearTop == false && isNearDown == false ? true : false;

		if (!isNearCenter)
			dragToElement(elementToScroll, true);

	}

	public void dragToElement(MobileElement elementToScroll) {

		System.out.println("Before scroll :" + elementToScroll.getLocation());

		int fromX = (int) (getAppiumDriver().manage().window().getSize().getWidth() * 0.5);
		int fromY = (int) (getAppiumDriver().manage().window().getSize().getHeight() * 0.6);
		int toX = (int) (getAppiumDriver().manage().window().getSize().getWidth() * 0.5);
		int toY = (int) (getAppiumDriver().manage().window().getSize().getHeight() * 0.3);

		while (!elementToScroll.isDisplayed()) {
			int eleLocationY = elementToScroll.getLocation().getY();
			Map<String, Object> params = new HashMap<>();

			params.put("duration", 0.1);
			params.put("fromX", fromX);
			params.put("fromY", fromY);
			params.put("toX", toX);
			params.put("toY", toY);
			
			System.out.println(params);
			getIOSDriver().executeScript("mobile: dragFromToForDuration", params);

			System.out.println("after scroll :" + elementToScroll.getLocation());
			
			if(eleLocationY==elementToScroll.getLocation().y) 
				break;
		}
	}

	public void dragToElement(MobileElement elementToScroll, boolean scrollToCenter) {

		if (!scrollToCenter)
			dragToElement(elementToScroll);
		else {
			boolean isNearCenter = false;

			System.out.println("Before scroll :" + elementToScroll.getLocation());

			int fromX = (int) (width * 0.5);
			int fromY = (int) (height * 0.6);
			int toX = (int) (width * 0.5);
			int toY = (int) (height * 0.3);

			Map<String, Object> params = new HashMap<>();
			params.put("duration", 0.1);
			params.put("fromX", fromX);
			params.put("fromY", fromY);
			params.put("toX", toX);
			params.put("toY", toY);
			
			while (!isNearCenter) {

				System.out.println(params);
				int eleLocationY = elementToScroll.getLocation().getY();
				boolean isNearTop = (height * 0.25) > eleLocationY ? true : false;
				boolean isNearDown = (height * 0.75) < eleLocationY ? true : false;
				isNearCenter = isNearTop == false && isNearDown == false ? true : false;
				if (!isNearCenter) {
					params = new HashMap<>();
					if (isNearDown) {
						fromY = (int) (height * 0.55);
						toY = (int) (height * 0.35);
					} else if (isNearTop) {
						fromY = (int) (height * 0.35);
						toY = (int) (height * 0.55);
					}

					params.put("duration", 0.1);
					params.put("fromX", fromX);
					params.put("fromY", fromY);
					params.put("toX", toX);
					params.put("toY", toY);
				}
				if(isNearCenter)break;

				getIOSDriver().executeScript("mobile: dragFromToForDuration", params);
				Point loc=elementToScroll.getLocation();				
				System.out.println("after scroll :" + loc);
				if(!(Math.abs(eleLocationY-loc.y)>30)) break; 
				if(eleLocationY==loc.y)	break;
			}
		}
	}

	
	
	
	
	public void dragToElementTop(MobileElement elementToScroll){
		boolean isNearCenter = false;
		System.out.println("Before scroll :" + elementToScroll.getLocation());

		int fromX = (int) (width * 0.5);
		int fromY = (int) (height * 0.6);
		int toX = (int) (width * 0.5);
		int toY = (int) (height * 0.3);

		Map<String, Object> params = new HashMap<>();
		params.put("duration", 0.1);
		params.put("fromX", fromX);
		params.put("fromY", fromY);
		params.put("toX", toX);
		params.put("toY", toY);
		
		while (!isNearCenter) {

			System.out.println(params);
			int eleLocationY = elementToScroll.getLocation().getY();
			boolean isNearTop = (height * 0.22) > eleLocationY ? true : false;
			boolean isNearDown = (height * 0.55) < eleLocationY ? true : false;
			isNearCenter = isNearTop == false && isNearDown == false ? true : false;
			if (!isNearCenter) {
				params = new HashMap<>();
				if (isNearDown) {
					if(eleLocationY<height){
						fromY = (int) (height * 0.48);
						toY = (int) (height * 0.42);
					}else{
					fromY = (int) (height * 0.55);
					toY = (int) (height * 0.35);
					}
				} else if (isNearTop) {
					if(eleLocationY>0){
						fromY = (int) (height * 0.42);
						toY = (int) (height * 0.48);
					}else{
					fromY = (int) (height * 0.35);
					toY = (int) (height * 0.55);
					}
				}

				params.put("duration", 0.1);
				params.put("fromX", fromX);
				params.put("fromY", fromY);
				params.put("toX", toX);
				params.put("toY", toY);
			}
			if(isNearCenter)break;

			getIOSDriver().executeScript("mobile: dragFromToForDuration", params);
			Point loc=elementToScroll.getLocation();				
			System.out.println("after scroll :" + loc);
			if(!(Math.abs(eleLocationY-loc.y)>30)) break; 
			if(eleLocationY==loc.y)	break;
		}
	}
	
	public void dragEleToCoordinates(MobileElement me,int toX,int toY) {
		int centerX=me.getRect().getX() + (me.getRect().getWidth()/2);
		int centerY=me.getRect().getY() + (me.getRect().getHeight()/2);
		dragFromToCoordinates(centerX,centerY,toX,toY);
	}
	
	public void dragFromToCoordinates(int fromX,int fromY,int toX,int toY) {
		Map<String, Object> params = new HashMap<>();
		params.put("duration", 0.1);
		params.put("fromX", fromX);
		params.put("fromY", fromY);
		params.put("toX", toX);
		params.put("toY", toY);
		System.out.println(params);
		getIOSDriver().executeScript("mobile: dragFromToForDuration", params);
	}
	
	
	
	public MobileElement dragInElement(MobileElement parentEle,MobileElement elementToScroll){
		boolean isNearCenter = false;
		int width=parentEle.getRect().width;
		int height=parentEle.getRect().height;
		System.out.println("Before scroll :" + elementToScroll.getLocation());
		int pX=parentEle.getLocation().getX(),pY=parentEle.getLocation().getY();
		int fromX = (int) (width * 0.5)+pX;
		int fromY=(int) (((height * 0.6)+pY)<this.height?((height * 0.6)+pY): this.height*0.85);

		int toX = (int) (width * 0.5)+pX;
		int toY = (int) (height * 0.3)+pY;

		Map<String, Object> params = new HashMap<>();
		params.put("duration", 0.1);
		params.put("fromX", fromX);
		params.put("fromY", fromY);
		params.put("toX", toX);
		params.put("toY", toY);
		
		while (!isNearCenter) {

			System.out.println(params);
			int eleLocationY = elementToScroll.getLocation().getY();
			boolean isNearTop = pY > eleLocationY ? true : false; 
			boolean isNearDown = (height * 0.95)+pY < eleLocationY ? true : false;
			isNearCenter = isNearTop == false && isNearDown == false ? true : false;
			System.out.println("Width:"+width);
			System.out.println("Height:"+height);
			if (!isNearCenter) {
				params = new HashMap<>();
				if (isNearDown) {
					fromY = (int) ((height * 0.55)+pY<this.height?((height * 0.6)+pY): this.height*0.85);
					toY = (int) (height * 0.35)+pY;
				} else if (isNearTop) {
					fromY = (int) (height * 0.35)+pY;
					toY = (int) (height * 0.55);
				}

				params.put("duration", 0.1);
				params.put("fromX", fromX);
				params.put("fromY", fromY);
				params.put("toX", toX);
				params.put("toY", toY);
			}
			if(isNearCenter)break;

			getIOSDriver().executeScript("mobile: dragFromToForDuration", params);
			Point loc=elementToScroll.getLocation();				
			System.out.println("after scroll :" + loc);
			
			if(eleLocationY==loc.y)	
				break;
		}return elementToScroll;
	}
	
	
	
	
	public void scrollToElement(By parentEle, By eleToScroll) {
		IOSElement parentElement = (IOSElement) getIOSDriver().findElement(parentEle);
		IOSElement elementToScroll = (IOSElement) getIOSDriver().findElement(eleToScroll);
		Map<String, Object> params = new HashMap<>();
		params.put("direction", "down");
		params.put("predicateString", "id == '" + ((RemoteWebElement) elementToScroll).getId() + "'");
		params.put("element", ((RemoteWebElement) parentElement).getId());
		JavascriptExecutor js = (JavascriptExecutor) getRemoteWebDriver();
		getRemoteWebDriver().executeScript("mobile: scroll", params);
	}

	protected boolean isPresent(By by) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		getRemoteWebDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		try {
			getRemoteWebDriver().findElement(by).isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			getRemoteWebDriver().manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
	}

	public void tapOnCoordinates(int x, int y) {
		PointOption s=new PointOption();
		s.withCoordinates(x, y).build();
		new TouchAction((PerformsTouchActions) getRemoteWebDriver()).tap(s).perform();
	}
	
	public String getLabel(MobileElement me){
		String label=me.getAttribute("label");
		System.out.println("Label:"+label);
		return label;
	}
	
	//Check if element is on screen even if its not displayed
	public boolean isOnScreen(MobileElement me){
		int eleLocation=me.getLocation().getY();
		return (eleLocation>0 && eleLocation<height);
	}
	
	public void tapOnEleByCoordinates(MobileElement me){
		int centerX=me.getRect().getX() + (me.getRect().getWidth()/2);
		int centerY=me.getRect().getY() + (me.getRect().getHeight()/2);
		tapOnCoordinates(centerX, centerY);
	}
	public void tapOnEleByCoordinatesOffset(MobileElement me,float xOffsetInPercent,float yOffsetInPercent){
		int centerX=(int) (me.getRect().getX() + ((me.getRect().getWidth()/2) * xOffsetInPercent));
		int centerY=(int) (me.getRect().getY() + ((me.getRect().getHeight()/2) * yOffsetInPercent));
		tapOnCoordinates(centerX, centerY);
	}
	
	public void tapOnEleInElement(MobileElement parentEle,MobileElement ele) {
		int parentHeight = parentEle.getRect().y + parentEle.getRect().height;
		int centerY=(int) (ele.getRect().getY() + (ele.getRect().getHeight()/2) );
	}
	
	
	@Override
	public void embedScreenshot(WebElement me) {
		try {
			waitUntilVisible((MobileElement)me);
		}catch (Exception e) {e.printStackTrace();}
		try {
			TakesScreenshot camera = (TakesScreenshot) getRemoteWebDriver();
			BufferedImage img = ImageIO.read(camera.getScreenshotAs(OutputType.FILE));
			int x =  me.getRect().getX(), y =  me.getRect().getY(), h = me.getRect().getHeight(), w = me.getRect().getWidth();
			int relativeX=(x*img.getWidth()/width);
			int relativeY=(y*img.getHeight()/height);
			int relativeHeight=(h*img.getHeight()/height);
			int relativeWidth=(w*img.getWidth()/width);
			
			BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = resized.createGraphics();
			 g2d.drawImage(img, 0, 0, width, height, null);
			 g2d.setColor(Color.RED);
			 g2d.setStroke(new BasicStroke(2));
			g2d.drawRect(x, y, w, h);
			g2d.dispose();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(resized, "png", baos);
			 getState().getScenario().embed(baos.toByteArray(), "image/png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void embedScreenshot() {
		TakesScreenshot camera = (TakesScreenshot) getRemoteWebDriver();
		try {
			BufferedImage img = ImageIO.read(camera.getScreenshotAs(OutputType.FILE));
			BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = resized.createGraphics();
			 g2d.drawImage(img, 0, 0, width, height, null);
			g2d.dispose();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(resized, "png", baos);
			 getState().getScenario().embed(baos.toByteArray(), "image/png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void zAssertTrueAndTakeScreenshot(boolean condition, String message) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		try {
			if (!condition) {
				this.embedScreenshot(message);
				logReportMessage("<b>[Assertion Faliure]</b>\t" + message);
			}
			assertTrue(condition);
			logReportMessage("<b>[Assertion Pass]</b>\t" + message);
			embedScreenshot();
		} catch (Exception e) {
			logReportMessage("<b>[Assertion Faliure]</b>\t" + message);
			embedScreenshot();
			throw e;
		}
	}
	
	public void clickClearAndEnterTextAndDone(MobileElement me,String text) {
		me.click();
		me.clear();
		me.sendKeys(text);
		getEleBy(MobileBy.iOSNsPredicateString("type=='XCUIElementTypeButton' AND name == 'Done'")).click();
	}
	public void clearAndEnterTextAndDone(MobileElement me,String text) {
		me.clear();
		me.sendKeys(text);
		getEleBy(MobileBy.iOSNsPredicateString("type=='XCUIElementTypeButton' AND name == 'Done'")).click();
	}
	public void clickAndEnterTextAndDone(MobileElement me,String text) {
		me.click();
		me.sendKeys(text);
		getEleBy(MobileBy.iOSNsPredicateString("type=='XCUIElementTypeButton' AND name == 'Done'")).click();
	}
	public void enterTextAndDone(MobileElement me,String text) {
		me.sendKeys(text);
		getEleBy(MobileBy.iOSNsPredicateString("type=='XCUIElementTypeButton' AND name == 'Done'")).click();
	}
	
	protected void doubleClick(MobileElement me) {
		actions.doubleClick(me).build().perform();;
	}
	protected void doubleTap(MobileElement me) {
		int centerX=me.getRect().getX() + (me.getRect().getWidth()/2);
		int centerY=me.getRect().getY() + (me.getRect().getHeight()/2);
		
		PointOption s=new PointOption();
		s.withCoordinates(centerX,centerY).build();
		new TouchAction((PerformsTouchActions) getRemoteWebDriver())
		.tap(s)
		.waitAction(WaitOptions.waitOptions(Duration.ofMillis(100)) )
		.tap(s)
		.perform();
	}
}