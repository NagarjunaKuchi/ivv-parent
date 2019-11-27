package io.mosip.ivv.pmp.policy.methods;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import io.mosip.ivv.core.base.ApiCaller;
import io.mosip.ivv.core.base.Step;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.structures.AllowedKycAttributes;
import io.mosip.ivv.core.structures.AuthPolicies;
import io.mosip.ivv.core.structures.AuthPolicy;
import io.mosip.ivv.core.structures.LoadAuthPolicyData;
import io.mosip.ivv.core.structures.Policy;
import io.mosip.ivv.core.utils.Utils;
import io.mosip.ivv.pmp.utils.GetDataFromDb;

public class CreateAuthPolicies extends Step implements StepInterface {

	Policy policy;
	
	@SuppressWarnings("unchecked")
	@Override
	public void run(io.mosip.ivv.core.structures.Scenario.Step step) {
		
		this.policy = this.store.getScenarioPmpData().getPolicy();
		String policyID = GetDataFromDb.getPolicyId(policy.getPolicyName());
		AuthPolicy authPolicy = LoadAuthPolicyData.getTelecomPolicy();
		
		JSONObject request_json = new JSONObject();				
		request_json.put("name",policy.getPolicyName() + authPolicy.getAuth_policy_name());
		request_json.put("descr",policy.getPolicyDesc() + authPolicy.getAuth_policy_desc());		
		
		JSONArray authPolicies = new JSONArray();
		JSONArray allowedKycAttributes = new JSONArray();
		for (AuthPolicies authPolicyDto : authPolicy.getAuthPolicies()) {
			JSONObject authObj = new JSONObject();
			authObj.put("authType", authPolicyDto.authType);
			authObj.put("authSubType", authPolicyDto.authSubType);
			authObj.put("mandatory", authPolicyDto.mandatory);
			authPolicies.add(authObj);
			}
		for (AllowedKycAttributes allowedKycDto : authPolicy.getAllowedKycAttributes()) {
			JSONObject allowedKycObj = new JSONObject();
			allowedKycObj.put("attributeName", allowedKycDto.getAttributeName());
			allowedKycObj.put("required", allowedKycDto.getRequired());
			allowedKycAttributes.add(allowedKycObj);
		}
		request_json.put("authPolicies", authPolicies);
		request_json.put("allowedKycAttributes", allowedKycAttributes);		
		
        JSONObject api_input = new JSONObject();
        api_input.put("id", "mosip.partnermanagement.policy.create");
        api_input.put("version", "1.0");
        api_input.put("requesttime", Utils.getCurrentDateAndTimeForAPI());
        
        api_input.put("request", request_json);
        
        String url = "/pmp/policies/"+ policyID +"/authPolicies";       
        
        ApiCaller api_caller = new ApiCaller();
        this.hasError = api_caller.callApi(step, url, api_input, "POST",this.store);
		
	}

}
