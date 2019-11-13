package io.mosip.ivv.core.structures;

import java.util.ArrayList;
import java.util.List;

import com.aventstack.extentreports.ExtentTest;
import com.google.gson.JsonObject;

import io.mosip.ivv.core.policies.AssertionPolicy;
import io.mosip.ivv.core.policies.ErrorPolicy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Scenario {
    private String id = "";
    private String name = "";
    private String description = "";
    private ArrayList<String> tags = new ArrayList<String>();
    private String personaClass, groupName,subModule,scenarioName;
    private ArrayList<Step.modules> modules = new ArrayList<Step.modules>();

    @Getter
    @Setter
    public static class Step
    {
        public enum modules {
            mi,pa
        }
        
        private String name = ""; // needs to be passed
        private String variant = "DEFAULT"; // default
        private modules module;
        private ArrayList<Assert> asserts;
        private ArrayList<Error> errors;
        private int AssertionPolicy = 0; // default
        private boolean FailExpected = false; //default
        private ArrayList<String> parameters;
        private ArrayList<Integer> index;
        private ExtentTest e;
        private String api_url;
        private JsonObject api_input;
        private String httpMethod;
        
        public static class Error{
            public ErrorPolicy type;
            public ArrayList<String> parameters = new ArrayList<>();
        }

        public static class Assert{
            public AssertionPolicy type;
            public ArrayList<String> parameters = new ArrayList<>();
        }

        public Step(){

        }

        public Step(String name, String variant, ArrayList<Assert> asserts, ArrayList<Error> errors, ArrayList<String> parameters, ArrayList<Integer> index)
        {
            this.name = name;
            this.variant = variant;
            this.asserts = asserts;
            this.errors = errors;
            this.parameters = parameters;
            this.index = index;
        }
    }
    
    
    @Getter
    @Setter
    public static class PmpData
    {    	
    	private Misp misp;
    	private MispLicense mispLicense;	
    	private Policy policy;
    	private Partner partner;
    }

    private List<Step> steps = new ArrayList<Step>();
    private PmpData pmpData = null;
    private boolean continueOnFailure = false; // default
    private boolean isFailureExpected = false; // default

}
