package io.mosip.ivv.pmp.partmanager.methods;

import org.json.simple.JSONObject;

import io.mosip.ivv.core.base.ApiCaller;
import io.mosip.ivv.core.base.Step;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.structures.Partner;

public class GetAllAuthEKYCPartnersForThePolicyGroup extends Step implements StepInterface{
	
	
	private Partner partner;
	
	@SuppressWarnings("unchecked")
	@Override
	public void run(io.mosip.ivv.core.structures.Scenario.Step step) {
		
		this.partner = this.store.getScenarioPmpData().getPartner();
		
		JSONObject request_json = new JSONObject();			
		request_json.put("name", partner.getName());
        
        String url = "/pmpartners";
        ApiCaller api_caller = new ApiCaller();
        this.hasError = api_caller.callApi(step, url, request_json, "GET",this.store);
	}
}
