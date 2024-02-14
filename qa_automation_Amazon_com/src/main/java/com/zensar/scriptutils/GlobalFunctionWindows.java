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

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;

abstract public class GlobalFunctionWindows extends GlobalFunction {

	int height,width;
	private int timeOut=20;
	
	public int getDeviceHeight(){
		return height;
	}
	public int getDeviceWidth(){
		return width;
	}
	
	public GlobalFunctionWindows(ScenarioState state) {
		super(state, DriverType.Windows);
		width=getAppiumDriver().manage().window().getSize().getWidth();
		height=getAppiumDriver().manage().window().getSize().getHeight();
	}
	
	protected AppiumDriver getAppiumDriver() {
		return (AppiumDriver) state.getWindowsDriver();
	}

	protected WindowsDriver getWindowsDriver() {
		return (WindowsDriver) state.getWindowsDriver();
	}

	private void attachScreenshot(WindowsElement element) {
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
		getWindowsDriver().switchTo().alert().accept();
	}

	public WindowsElement getEleByXpath(String xpath) { 
		WindowsElement WindowsElement= (WindowsElement) getWindowsDriver().findElement(By.xpath(xpath));
		return WindowsElement;
	}

	public WindowsElement getEleBy(By by) {
		WindowsElement WindowsElement= (WindowsElement) getWindowsDriver().findElement(by);
		return WindowsElement;
	}

	public WindowsElement getEleByXpath(String xpath, String param) {
		String finalXpath = getXpathFromParam(xpath, param);
		System.out.println("Xpath:" + finalXpath);
		WindowsElement WindowsElement = (WindowsElement) getWindowsDriver().findElement(By.xpath(finalXpath));
		return WindowsElement;
	}
	
	public List<WindowsElement> getElementsByXpath(String xpath, String param) {
		String finalXpath = getXpathFromParam(xpath, param);
		System.out.println("Xpath:" + finalXpath);
		List<WindowsElement> WindowsElement = (List<WindowsElement>) getWindowsDriver().findElements(By.xpath(finalXpath));
		return WindowsElement;
	}

	public WindowsElement waitUntilPresent(By locator) {
		System.out.println(locator);
		WindowsElement WindowsElement = (WindowsElement) new WebDriverWait(getWindowsDriver(),timeOut)
				.until(ExpectedConditions.presenceOfElementLocated(locator));
		return WindowsElement;
	}
	


	public WindowsElement waitUntilPresent(WindowsElement element) {
		FluentWait wait = new FluentWait((WindowsDriver) getWindowsDriver());
		wait.withTimeout( Duration.ofMillis(30000));
		wait.pollingEvery(Duration.ofMillis(500));
		wait.ignoring(NoSuchElementException.class);

		WindowsElement WindowsElement = (WindowsElement) wait.until(new Function<WebDriver, WindowsElement>() {
			public WindowsElement apply(WebDriver driver) {
				return (WindowsElement) element;
			}
		});
		return WindowsElement;
	}

	public boolean isPresent(WindowsElement element) {
		getWindowsDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		try {
			element.isDisplayed();
			return true;
		} catch (Exception e) {}
		finally {
			getWindowsDriver().manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
		return false;
	}

	public WindowsElement waitUntilVisible(WindowsElement me) {
		System.out.println(me);
		WindowsElement WindowsElement = (WindowsElement) new WebDriverWait(getWindowsDriver(),timeOut)
				.until(ExpectedConditions.visibilityOf(me));
		return WindowsElement;
	}

	public WindowsElement waitUntilClickable(By locator) {
		System.out.println(locator);
		WindowsElement WindowsElement = (WindowsElement) new WebDriverWait(getWindowsDriver(),timeOut)
				.until(ExpectedConditions.elementToBeClickable(locator));
		return WindowsElement;
	}

	public WindowsElement waitUntilClickable(WindowsElement me) {
		System.out.println(me);
		WindowsElement WindowsElement = (WindowsElement) new WebDriverWait(getWindowsDriver(),timeOut)
				.until(ExpectedConditions.elementToBeClickable(me));
		return WindowsElement;
	}
	
	public WindowsElement waitUntilRefreshed(WindowsElement me) {
		System.out.println(me);
		WindowsElement WindowsElement = (WindowsElement) new WebDriverWait(getWindowsDriver(),timeOut)
				.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(me)));
		return WindowsElement;
	}

	public void waitAndClick(WindowsElement me) {
		System.out.println(me);
		try {
			me.click();
		}catch (Exception e) {
			System.out.println("Error while clicking, waiting until clickable...\n"+e);
			waitUntilClickable(me).click();
		}
	}
	public void waitScrollSnapClick(WindowsElement me){
		waitUntilPresent(me);
		autoScrollToElement(me);
		waitUntilClickable(me);
		embedScreenshot(me);
		me.click();
	}
	public void waitScrollClick(WindowsElement me){
		waitUntilPresent(me);
		autoScrollToElement(me);
		waitUntilClickable(me);
		me.click();
	}
	public void waitSnapClick(WindowsElement me){
		waitUntilPresent(me);
		waitUntilClickable(me);
		embedScreenshot(me);
		me.click();
	}

	public void waitScrollSnap(WindowsElement me){
		waitUntilPresent(me);
		autoScrollToElement(me);
		embedScreenshot(me);
	}
	public WindowsElement waitScroll(WindowsElement me){
		waitUntilPresent(me);
		autoScrollToElement(me);
		return me;
	}
	public void waitSnap(WindowsElement me){
		embedScreenshot(me);
	}


	public void swipeDown(WindowsElement parent) {

		Map<String, Object> params = new HashMap<>();
		params.put("direction", "down");
		params.put("element", ((RemoteWebElement) parent).getId());
		JavascriptExecutor js = (JavascriptExecutor) getWindowsDriver();
		params = new HashMap<>();
		params.put("direction", "down");
		params.put("velocity", 250);
		params.put("element", ((RemoteWebElement) parent).getId());
		getWindowsDriver().executeScript("mobile: swipe", params);
	}
	
	public void swipeUp(WindowsElement parent) {

		Map<String, Object> params = new HashMap<>();
		params.put("direction", "up");
		params.put("element", ((RemoteWebElement) parent).getId());
		JavascriptExecutor js = (JavascriptExecutor) getWindowsDriver();

		params = new HashMap<>();
		params.put("direction", "down");
		params.put("velocity", 250);
		params.put("element", ((RemoteWebElement) parent).getId());
		getWindowsDriver().executeScript("mobile: swipe", params);
	}

	public void scrollToElement(WindowsElement parentElement, WindowsElement elementToScroll) {
		Map<String, Object> params = new HashMap<>();
		params.put("direction", "down");
		params.put("toVisible", "true");
		params.put("predicateString", "id == '" + ((RemoteWebElement) elementToScroll).getId() + "'");
		params.put("element", ((RemoteWebElement) parentElement).getId());
		JavascriptExecutor js = (JavascriptExecutor) getWindowsDriver();
		getWindowsDriver().executeScript("mobile: scroll", params);
		params = new HashMap<>();
		params.put("direction", "up");
		params.put("velocity", 100);
		params.put("element", ((RemoteWebElement) parentElement).getId());
		}

	public void swipeToElement(WindowsElement elementToScroll) {

		System.out.println("Before scroll :" + elementToScroll.getLocation());
		ArrayList<Integer> scrolledPositions=new ArrayList<Integer>();
		while (!elementToScroll.isDisplayed()) {
			int eleLocationY = elementToScroll.getLocation().getY();
			scrolledPositions.add(eleLocationY);
			String swipe = eleLocationY >= 0 ? "up" : "down";
			Map<String, Object> params = new HashMap<>();
			params.put("direction", swipe);
			params.put("velocity", 1);
			getWindowsDriver().executeScript("mobile: swipe", params);
			System.out.println(params);
			Point afterScroll=elementToScroll.getLocation();
			System.out.println("after scroll :" + afterScroll);
			if(eleLocationY==afterScroll.y || scrolledPositions.contains(afterScroll.y))
				break;
			
		}
	}
	
	public void swipeInElement(WindowsElement elementToScroll) {

		System.out.println("Before scroll :" + elementToScroll.getLocation());

			int eleLocationY = elementToScroll.getLocation().getY();

			String swipe = eleLocationY >= 0 ? "up" : "down";
			Map<String, Object> params = new HashMap<>();
			params.put("direction", swipe);
			params.put("velocity", 1);
			 params.put("element", ((RemoteWebElement) elementToScroll).getId());
			getWindowsDriver().executeScript("mobile: swipe", params);
			System.out.println(params);
			System.out.println("after scroll :" + elementToScroll.getLocation());
	}
	
	public void selectPickerWheelNextValue(WindowsElement element) {
		JavascriptExecutor js = (JavascriptExecutor) getAppiumDriver();
		Map<String, Object> params = new HashMap<>();
		params.put("order", "next");
		params.put("offset", 0.15);
		params.put("element", ((RemoteWebElement) element).getId());
		js.executeScript("mobile: selectPickerWheelValue", params);
	}

	public void autoScrollToElement(WindowsElement elementToScroll) {
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

	public void dragToElement(WindowsElement elementToScroll) {

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
			getWindowsDriver().executeScript("mobile: dragFromToForDuration", params);

			System.out.println("after scroll :" + elementToScroll.getLocation());
			
			if(eleLocationY==elementToScroll.getLocation().y) 
				break;
		}
	}

	public void dragToElement(WindowsElement elementToScroll, boolean scrollToCenter) {

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

				getWindowsDriver().executeScript("mobile: dragFromToForDuration", params);
				Point loc=elementToScroll.getLocation();				
				System.out.println("after scroll :" + loc);
				if(!(Math.abs(eleLocationY-loc.y)>30)) break; 
				if(eleLocationY==loc.y)	break;
			}
		}
	}
	
	
	
	public void dragToElementTop(WindowsElement elementToScroll){
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

			getWindowsDriver().executeScript("mobile: dragFromToForDuration", params);
			Point loc=elementToScroll.getLocation();				
			System.out.println("after scroll :" + loc);
			if(!(Math.abs(eleLocationY-loc.y)>30)) break; 
			if(eleLocationY==loc.y)	break;
		}
	}
	
	public void dragEleToCoordinates(WindowsElement me,int toX,int toY) {
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
		getWindowsDriver().executeScript("mobile: dragFromToForDuration", params);
	}
	
	
	
	public WindowsElement dragInElement(WindowsElement parentEle,WindowsElement elementToScroll){
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

			getWindowsDriver().executeScript("mobile: dragFromToForDuration", params);
			Point loc=elementToScroll.getLocation();				
			System.out.println("after scroll :" + loc);
			
			if(eleLocationY==loc.y)	
				break;
		}return elementToScroll;
	}
	
	
	
	public void scrollToElement(By parentEle, By eleToScroll) {
		WindowsElement parentElement = (WindowsElement) getWindowsDriver().findElement(parentEle);
		WindowsElement elementToScroll = (WindowsElement) getWindowsDriver().findElement(eleToScroll);
		Map<String, Object> params = new HashMap<>();
		params.put("direction", "down");
		params.put("predicateString", "id == '" + ((RemoteWebElement) elementToScroll).getId() + "'");
		params.put("element", ((RemoteWebElement) parentElement).getId());
		JavascriptExecutor js = (JavascriptExecutor) getWindowsDriver();
		getWindowsDriver().executeScript("mobile: scroll", params);
	}

	protected boolean isPresent(By by) {
		registerCall(new Object() {
		}.getClass().getEnclosingMethod().getName());
		getWindowsDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		try {
			getWindowsDriver().findElement(by).isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			getWindowsDriver().manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		}
	}

	public String getLabel(WindowsElement me){
		String label=me.getAttribute("label");
		System.out.println("Label:"+label);
		return label;
	}
	public boolean isOnScreen(WindowsElement me){
		int eleLocation=me.getLocation().getY();
		return (eleLocation>0 && eleLocation<height);
	}
	
	@Override
	public void embedScreenshot(WebElement me) {
		try {
			waitUntilVisible((WindowsElement)me);
		}catch (Exception e) {e.printStackTrace();}
		try {
			TakesScreenshot camera = (TakesScreenshot) getWindowsDriver();
			BufferedImage img = ImageIO.read(camera.getScreenshotAs(OutputType.FILE));
			int x =  me.getLocation().getX(), y =  me.getLocation().getY(), h = me.getSize().getHeight(), w = me.getSize().getWidth();
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
		TakesScreenshot camera = (TakesScreenshot) getWindowsDriver();
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
			embedScreenshot(message);
		} catch (Exception e) {
			logReportMessage("<b>[Assertion Faliure]</b>\t" + message);
			embedScreenshot(message);
			throw e;
		}
	}
	
	protected void doubleClick(WindowsElement ele) {
		actions.doubleClick(ele).build().perform();
	}
	
	protected void click(WindowsElement ele) {
		actions.click(ele).build().perform();
	}
	protected void click(WindowsElement ele,int clickCount) {
		for(int i=0;i<clickCount;i++) {
			actions.click(ele).build().perform();
		}
	}
	
}