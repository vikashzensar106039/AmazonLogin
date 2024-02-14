package com.zensar.runner;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.zensar.excel.ExcelExecutionProperty;
import com.zensar.slack.SlackClient;
import com.zensar.utilities.ExecutionEnvironment;
import com.zensar.utilities.StringLiterals;

@Test
public class InitializeExecution {
	@BeforeClass(alwaysRun = true)
	public static void initializeExecutionModel() {
		File file;
		if (ExecutionEnvironment.isJenkinsBuild()) {
			System.out.println(System.getProperty("os.name"));
			if (System.getProperty("os.name").equalsIgnoreCase("linux")) {
				file = new File(StringLiterals.getReportsPath());
				StringLiterals.deleteDirectory(file);
				if (file.exists()) {
					System.out.println("Not Deleted");
				}
			}
		} else if (System.getProperty("os.name").equalsIgnoreCase("windows")) {
		}
		
		System.out.println("=============================Execution Start====================================");
		if(ExcelExecutionProperty.getGlobalExecutionProperty("Slack Update").equals("Yes")) SlackClient.initSlack();
		
//		SaleDataHandler salesData = new SaleDataHandler();
	/*	try {
//			System.out.println(salesData);
			salesData.compareSalesData();
		} catch (URISyntaxException | IOException e) {
			System.out.println("[CRITICAL ERROR]There was error while fetcging sales from Calender API and update the excelSheet");
			e.printStackTrace();
		}*/
//		Sale currentSiteSale = null;
//		Sale currentOnlineSale = null;
		
		/*ArrayList<Sale> sales = salesData.getSalesFromExcel();
		for (Sale sale : sales) {
			boolean testable = sale.getTest_status().trim().equalsIgnoreCase("TEST");
			if(!testable) continue;
			boolean isOnline = sale.getOnline().trim().equalsIgnoreCase("TRUE");
			if(isOnline){
				if(currentOnlineSale == null) currentOnlineSale = sale;
				int currentOnlineSaleRetestCount = Integer.parseInt(currentOnlineSale.getRetestCount());
				int onlineRetestCount = Integer.parseInt(sale.getRetestCount());
				if(onlineRetestCount < currentOnlineSaleRetestCount) currentOnlineSale = sale;
			}else{
				if(currentSiteSale == null) currentSiteSale = sale;
				int currentSiteSaleRetestCount = Integer.parseInt(currentSiteSale.getRetestCount());
				int siteRetestCount = Integer.parseInt(sale.getRetestCount());
				if(siteRetestCount < currentSiteSaleRetestCount) currentSiteSale = sale;
			}
		}*/
//		currentSiteSale.setSale_number("17026");
//		ExecutionState.onlineSale = currentOnlineSale;
//		ExecutionState.siteSale = currentSiteSale;
//		System.out.println(ExecutionState.onlineSale + "Manveer   \n" + ExecutionState.siteSale);
//		ExecutionState.onlineSale = new Sale();
//		ExecutionState.onlineSale.setSale_number("17495");
//		ExecutionState.siteSale = new Sale();
//		ExecutionState.siteSale.setSale_number("16544"); 
		System.setOut(new java.io.PrintStream(System.out) {
	        private StackTraceElement getCallSite() {

	            for (StackTraceElement e : Thread.currentThread()

	                    .getStackTrace())

	                if (!e.getMethodName().equals("getStackTrace")

	                        && !e.getClassName().equals(getClass().getName()))

	                    return e;

	            return null;

	        }



	        @Override

	        public void println(String s) {

	            println((Object) s);

	        }


	        private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:s");
	        @Override
	        public void println(Object o) {
	            StackTraceElement e = getCallSite();
	            String callSite = e == null ? "??" :
	            String.format("(%s:%d)",
	                    e.getFileName(),
	                    e.getLineNumber());
	            Timestamp time=new Timestamp(System.currentTimeMillis());
	            super.println(o + "\t >>>>>> " + callSite +"\t"+sdf.format(time));
	        }

	    });
		
	}
	
	
	@Test
	public void stub() {
		//Placeholder function
	}
	
	
	
	public static void main(String args[]){
		
		initializeExecutionModel();
		
		
	}
	
	
	
	
}
