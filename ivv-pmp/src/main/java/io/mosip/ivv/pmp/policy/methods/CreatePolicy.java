package io.mosip.ivv.pmp.policy.methods;

import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.structures.Policy;
import io.mosip.ivv.core.utils.Utils;

import org.json.simple.JSONObject;

import io.mosip.ivv.core.base.ApiCaller;
import io.mosip.ivv.core.base.Step;

public class CreatePolicy extends Step implements StepInterface {

	Policy policy;
	@SuppressWarnings("unchecked")
	@Override
	public void run(io.mosip.ivv.core.structures.Scenario.Step step) {
		
		this.policy = this.store.getScenarioPmpData().getPolicy();
		
		JSONObject request_json = new JSONObject();				
		request_json.put("name",policy.getPolicyName());
		request_json.put("desc",policy.getPolicyDesc());
		
        JSONObject api_input = new JSONObject();
        api_input.put("id", "mosip.partnermanagement.policy.create");
        api_input.put("version", "1.0");
        api_input.put("requesttime", Utils.getCurrentDateAndTimeForAPI());
        
        api_input.put("request", request_json);
        
        String url = "/pmp/policies/";       
        
        ApiCaller api_caller = new ApiCaller();
        this.hasError = api_caller.callApi(step, url, api_input, "POST",this.store);
		
	}

}
