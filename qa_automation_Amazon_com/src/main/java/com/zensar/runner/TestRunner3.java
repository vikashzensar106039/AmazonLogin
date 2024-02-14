package com.zensar.runner;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.zensar.datatypes.ExecutionProperty;
import com.zensar.scriptutils.PickleWrapper;
import com.zensar.scriptutils.RunnerBase;
import com.zensar.utilities.RuntimeAnnotations;
import com.zensar.utilities.StringLiterals;
import cucumber.api.CucumberOptions;
import cucumber.api.testng.PickleEventWrapper;
import cucumber.api.testng.TestNGCucumberRunner;

@CucumberOptions()
public class TestRunner3 extends RunnerBase implements StringLiterals {
	private TestNGCucumberRunner testNGCucumberRunner;

	@Parameters({ "tags"})
	@BeforeClass(alwaysRun = true)
	public void setUpClass(String tags) throws Exception {
		RuntimeAnnotations.putAnnotation(this.getClass(), CucumberOptions.class, getCucumberOptions(tags));
		testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
	}

	@Test(groups = "cucumber", description = "Cucmber Scenarios", dataProvider = "scenarios")
	public void feature(PickleWrapper pickleWrapper) {
		int retyrCount = pickleWrapper.executionProperty.getRetryCount() + 1;
		retyrCount = retyrCount < 1 ? 1 : retyrCount;
		for (int i = 0; i < retyrCount; i++) {
				try {
					System.out.println("[INFO]\t Attempt " + (i + 1) + " of " + retyrCount + " to execute ");
					printScenarioInfo(pickleWrapper.executionProperty);
					testNGCucumberRunner.runScenario(pickleWrapper.pickleEventWrapper.getPickleEvent());
					System.out.println(
							"[INFO]\t Attempt " + (i + 1) + " of " + retyrCount + " to execute " + " [PASSED]");
					printScenarioInfo(pickleWrapper.executionProperty);
					break;
				} catch (Throwable e) {
					e.printStackTrace();
					System.out.println(
							"\n\n[ERROR]\t Attempt " + (i + 1) + " of " + retyrCount + " to execute " + " [FAILED]");
					printScenarioInfo(pickleWrapper.executionProperty);
					if (retyrCount > 1 && i != retyrCount - 1) {
						System.out.println("[INFO]\t Retrying....");
					} else {
						System.out.println("[INFO]\t Not going to attempt again.");
					}

				}
			
		}

	}

	private void printScenarioInfo(ExecutionProperty property) {
		System.out.println("Feature :\t" + property.getFeatureName());
		System.out.println("Scenario :\t" + property.getScenarioName());
		System.out.println("DataSetName :\t" + property.getDataSetName() + "\n");
	}

	@DataProvider
	public PickleWrapper[] scenarios() {
		return getScenarios(testNGCucumberRunner.provideScenarios());
	}

	@AfterClass(alwaysRun = true)
	public void tearDownClass() throws Exception {
		testNGCucumberRunner.finish();
	}

	@AfterClass
	public void teardown() {
		System.out.println("Suite Complete - " + this.getClass().getSimpleName());
	}


}