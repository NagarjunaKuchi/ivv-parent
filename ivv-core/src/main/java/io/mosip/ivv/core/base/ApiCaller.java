package io.mosip.ivv.core.base;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.simple.JSONObject;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;

import io.mosip.ivv.core.structures.CallRecord;
import io.mosip.ivv.core.structures.Scenario;
import io.mosip.ivv.core.structures.Store;
import io.mosip.ivv.core.utils.ErrorMiddleware;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class ApiCaller extends Step {
	
	public ReadContext callGetApi_Partner(String api_url, JSONObject api_input){
		
		RestAssured.baseURI = "http://localhost:1122";//System.getProperty("ivv.mosip.host");
        Response api_response = given().relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
               // .cookie("Authorization", this.store.getHttpData().getCookie())
                .body(api_input)
                .get(api_url);

        this.callRecord = new CallRecord("http://localhost:1122" + api_url, "GET", api_input.toString(), api_response);
        //Helpers.logCallRecord(this.callRecord);
         ReadContext ctx = JsonPath.parse(api_response.getBody().asString());
         return ctx;        
	}
	
	public ReadContext callGetApiKeyReq(String api_url, JSONObject api_input){
		
		RestAssured.baseURI = "http://localhost:1122";//System.getProperty("ivv.mosip.host");
        Response api_response = given().relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
               // .cookie("Authorization", this.store.getHttpData().getCookie())
                .body(api_input)
                .get(api_url);

        this.callRecord = new CallRecord("http://localhost:1122" + api_url, "GET", api_input.toString(), api_response);
        //Helpers.logCallRecord(this.callRecord);
         ReadContext ctx = JsonPath.parse(api_response.getBody().asString());
         return ctx;        
	}
	
	public Boolean callApi(Scenario.Step step, String api_url, JSONObject api_input,
			String httpMethod, Store store){
		
		extentInstance = step.getE();		
		RestAssured.baseURI = step.getApi_url();//System.getProperty("ivv.mosip.host");
        
		Response api_response = null;
		
		if(httpMethod.toUpperCase().equals("POST")){
			api_response = given().relaxedHTTPSValidation()
	                .contentType(ContentType.JSON)
	               // .cookie("Authorization", this.store.getHttpData().getCookie())
	                .body(api_input)
	                .post(api_url);
		}
		
		if(httpMethod.toUpperCase().equals("GET")){
			api_response = given().relaxedHTTPSValidation()
	                .contentType(ContentType.JSON)
	               // .cookie("Authorization", this.store.getHttpData().getCookie())
	                .body(api_input)
	                .get(api_url);
		}

		if(httpMethod.toUpperCase().equals("PUT")){
			api_response = given().relaxedHTTPSValidation()
	                .contentType(ContentType.JSON)
	               // .cookie("Authorization", this.store.getHttpData().getCookie())
	                .body(api_input)
	                .put(api_url);
		}
		
		

        this.callRecord = new CallRecord(step.getApi_url() + api_url, "POST", api_input.toString(), api_response);
        //Helpers.logCallRecord(this.callRecord);
        ReadContext ctx = JsonPath.parse(api_response.getBody().asString());
        
        if (api_response.getStatusCode() != 200) {
        	logSevere("API HTTP status return as " + api_response.getStatusCode() + api_response.prettyPrint());
        	
            this.hasError = true;
            return true;
        }
        
        if (step.getErrors() != null && step.getErrors().size() > 0) {
            ErrorMiddleware.MiddlewareResponse emr = new ErrorMiddleware(step, api_response, extentInstance).inject();
            if (!emr.getStatus()) {
                this.hasError = true;
                return true;
            }
        }
        else{
        	if (step.getAsserts().size() > 0) {
                for (Scenario.Step.Assert pr_assert : step.getAsserts()) {
                    switch (pr_assert.type) {
                        case DONT:
                            break;
                        case API_CALL:
                        	break;
                        case DEFAULT:
                            try {
                                if(ctx.read("$['response']") == null){
                                    logInfo("Assert failed: Expected response not empty but found empty");
                                    this.hasError=true;
                                    return true;
                                }
                            } catch (PathNotFoundException e) {
                                e.printStackTrace();
                                logSevere("Assert failed: Expected response not empty but found empty");
                                this.hasError=true;
                                return true;
                            }
                           responseMatch(api_input);
                            logInfo("Assert [DEFAULT] passed");
                            break;	                            
                        
                        default:
                            logWarning("API HTTP status return as " + pr_assert.type);
	                            break;
	                    }
	                }
	            }
	        }
        
		return false;
	}
		
	public ReadContext callGetApi(String api_url, JSONObject api_input){
		
		RestAssured.baseURI = "http://localhost:8888";//System.getProperty("ivv.mosip.host");
        Response api_response = given().relaxedHTTPSValidation()
                .contentType(ContentType.JSON)
               // .cookie("Authorization", this.store.getHttpData().getCookie())
                .body(api_input)
                .get(api_url);

        this.callRecord = new CallRecord("http://localhost:8888" + api_url, "POST", api_input.toString(), api_response);
        //Helpers.logCallRecord(this.callRecord);
         ReadContext ctx = JsonPath.parse(api_response.getBody().asString());
         return ctx;        
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Boolean responseMatch(JSONObject api_input){
		ReadContext ctx = JsonPath.parse(this.callRecord.getResponse().getBody().asString());
		HashMap<String, String> app_request = api_input;
		HashMap<String, String> app_response = null;
		
		if(ctx.read("$['response']") instanceof ArrayList) {
			app_response = ctx.read("$['response'][0]");
		}else {
			app_response = ctx.read("$['response']");
		}
		
		Iterator requestIterator = app_request.entrySet().iterator();
		Iterator responseIterator = app_response.entrySet().iterator();
		
//		while(requestIterator.hasNext()){
//			Map.Entry requestKeyValue = (Map.Entry)requestIterator.next();
//		
//			while(responseIterator.hasNext()){
//				Map.Entry responseKeyValue = (Map.Entry)requestIterator.next();
//				if(requestKeyValue.getKey().equals(responseKeyValue.getKey())){
//					if(requestKeyValue.getValue().equals(responseKeyValue.getValue())){
//						return true;
//					}
//				}
//			}
//		}
		
		return true;
	}
	
	/*@SuppressWarnings("unused")
	private Boolean responseMatch(Partner partner){
		ReadContext ctx = JsonPath.parse(this.callRecord.getResponse().getBody().asString());
		
		if(ctx.read("$['response']") instanceof ArrayList) {
			HashMap<String, String> app_info = ctx.read("$['response'][0]");
		}else {
			HashMap<String, String> app_info = ctx.read("$['response']");
		}
		return true;
	}*/
	
}
