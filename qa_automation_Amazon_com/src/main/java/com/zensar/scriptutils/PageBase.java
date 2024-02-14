package com.zensar.scriptutils;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.openqa.selenium.WebElement;

public abstract class PageBase extends GlobalFunction{
	private Robot robot;

	public PageBase(ScenarioState state) {
		super(state);
	}
	
	public PageBase(ScenarioState state, boolean isMobile) {
		super(state,isMobile);
	}
	
	public boolean pressCTRL_ALT_Plus(Character key) throws Throwable {
		try {
			robot.keyPress(KeyEvent.VK_ALT);
			robot.keyPress(KeyEvent.VK_CONTROL);
			key = ("" + key).toUpperCase().charAt(0);
			robot.keyPress((int) key);
			System.out.println("Send Keys : CTRL + ALT + " + key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logReportMessage(e.getMessage());
			return false;
		} finally {
			robot.keyRelease(KeyEvent.VK_ALT);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			key = ("" + key).toUpperCase().charAt(0);
			robot.keyRelease((int) key);
			System.out.println("Release Keys : CTRL + ALT + " + key);
		}
	}

	
	

	public ArrayList<Map<String, String>> getMultiExcelData() {
		return getMultiExcelData("MultiLotData");
	}
	protected boolean checkBoxSelectIfNotSelected(WebElement element) throws Throwable {
		try {
			if (!element.isSelected()) {
				element.click();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logReportMessage(e.getMessage());
			return false;
		}
	}
	
	protected String setDate() throws Throwable {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(new Date());
		} catch (Exception e) {
			logReportMessage(e.getMessage());
			e.printStackTrace();
		}
		return sdf.format(c.getTime());
	}
	

	protected String setDate(int days) throws Throwable {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(new Date());
			c.add(Calendar.DATE, days);
		} catch (Exception e) {
			logReportMessage(e.getMessage());
			e.printStackTrace();
		}
		return sdf.format(c.getTime());
	}

	

	public boolean cancelPrintOption() throws AWTException {
		robot = new Robot();
		pause(5000);
		embedScreenshot();
		robot.mouseMove(280, 180);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		return true;
	}

	public static void main(String args[]) throws AWTException, InterruptedException {
		Robot robot;
		robot = new Robot();
		Thread.sleep(5000);
		robot.mouseMove(250, 190);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

	}

	}
