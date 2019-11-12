package io.mosip.ivv.orchestrator;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.structures.Scenario;
import io.mosip.ivv.core.structures.Store;
import io.mosip.ivv.core.utils.Utils;
import io.mosip.ivv.dg.PmpDataGenerator;
import io.mosip.ivv.pmp.utils.ClearDataFromPmpDb;

@Test
public class Orchestrator {
    private static ExtentHtmlReporter htmlReporter;
    private static ExtentReports extent;
    private ExtentTest extentTest;
    private Properties properties;
    private HashMap<String, String> packages = new HashMap<String, String>(){/**
		 * 
		 */
		private static final long serialVersionUID = -4183385349270439348L;

	{
        put("mi", "io.mosip.ivv.pmp.misp.methods");
    }};

    @BeforeSuite
    public void beforeSuite(){
        this.properties = Utils.getProperties("config.properties");
        this.configToSystemProperties();
        Utils.setupLogger(System.getProperty("user.dir")+this.properties.getProperty("ivv.path.auditlog"));
        /* setting exentreport */
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir")+this.properties.getProperty("ivv.path.reports"));
        extent = new ExtentReports();        
        extent.attachReporter(htmlReporter);
    }

    @BeforeTest
    public static void  create_proxy_server() {

    }

    @AfterSuite
    public void afterSuite(){
        extent.flush();
    }

    @DataProvider(name="ScenarioDataProvider")
    public static Object[][] dataProvider(ITestContext context) throws SQLException {
    	//DataGenerator dg = null;    	
    	ClearDataFromPmpDb.deleteData();
        PmpDataGenerator dg = new PmpDataGenerator(System.getProperty("user.dir"), "config.properties");
        ArrayList<Scenario> scenariosToRun = dg.getScenarios();
        HashMap<String, String> configs = dg.getConfigs();
        HashMap<String, String> globals = dg.getGlobals();
        Object[][] dataArray = new Object[scenariosToRun.size()][4];
        for(int i=0; i<scenariosToRun.size();i++){
            dataArray[i][0] = i;
            dataArray[i][1] = scenariosToRun.get(i);
            dataArray[i][2] = configs;
            dataArray[i][3] = globals;
        }
        return dataArray;
    }

    @BeforeMethod
    public void beforeMethod(Method method) {

    }

    @Test(dataProvider="ScenarioDataProvider")
    private void run(int i, Scenario scenario, HashMap<String, String> configs, HashMap<String, String> globals) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String stepsAsString = mapper.writeValueAsString(scenario.getSteps());
            System.out.println(stepsAsString);
            System.out.println(mapper.writeValueAsString(scenario.getPmpData()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Utils.auditLog.info("-");
        Utils.auditLog.info("----------------- Scenario "+ scenario.getName() + ": " + scenario.getDescription()+" ----------------");
        Utils.auditLog.info("-");
        this.extentTest = extent.createTest("Scenario_" + scenario.getName() + ": " + scenario.getDescription());
        Store store = new Store();
        
        store.setConfigs(configs);
        store.setGlobals(globals);
        store.setScenarioPmpData(scenario.getPmpData());
        store.setProperties(this.properties);

        for(Scenario.Step step: scenario.getSteps()){
            Utils.auditLog.info("----");
            String identifier = "Step: "+step.getName()+", module: "+step.getModule()+", variant: "+step.getVariant();
            Utils.auditLog.info(identifier);
            try {
                this.extentTest.info(identifier+" - running");
                StepInterface st = getInstanceOf(step);
                st.setExtentInstance(this.extentTest);
                step.setE(this.extentTest);
                st.setState(store);
                st.run(step);
                //step.g
                store = st.getState();
                if(st.hasError()){
                	Assert.fail("");                	
                    this.extentTest.fail(identifier +" - failed");
                    if(System.getProperty("ivv.scenario.continueOnFailure") == null || System.getProperty("ivv.scenario.continueOnFailure").equals("N")){
                        return;
                    }
                }else{
                    this.extentTest.pass(identifier+" - passed");
                }
            } catch (ClassNotFoundException e) {
                this.extentTest.error(identifier+" - error");
                e.printStackTrace();
                return;
            } catch (IllegalAccessException e) {
                this.extentTest.error(identifier+" - error");
                e.printStackTrace();
                return;
            } catch (InstantiationException e) {
                this.extentTest.error(identifier+" - error");
                e.printStackTrace();
                return;
            } catch (RuntimeException e){
                this.extentTest.error(identifier+" - error");
                e.printStackTrace();
                return;
            }
        }
    }
    
    private String getPackage(Scenario.Step step){
        String pack = packages.get(step.getModule().toString());
        System.out.println(step.getModule());
        System.out.println(pack);
        return pack;
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {

    }

    public StepInterface getInstanceOf(Scenario.Step step) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = getPackage(step) +"."+  step.getName().substring(0, 1).toUpperCase() + step.getName().substring(1);
        System.out.println(className);
        return (StepInterface) Class.forName(className).newInstance();
    }

    private void configToSystemProperties(){
        Set<String> keys = this.properties.stringPropertyNames();
        for (String key : keys) {
            System.setProperty(key, this.properties.getProperty(key));
        }
    }

}
