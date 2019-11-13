package io.mosip.ivv.pmp.partner.methods;

import org.json.simple.JSONObject;

import io.mosip.ivv.core.base.ApiCaller;
import io.mosip.ivv.core.base.Step;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.structures.Partner;

public class GetparticularAuthEKYCPartnerDetailsForGivenPartnerId extends Step implements StepInterface{
		
	private Partner partner;
	
	@SuppressWarnings("unchecked")
	@Override
	public void run(io.mosip.ivv.core.structures.Scenario.Step step) {
		
		this.partner = this.store.getScenarioPmpData().getPartner();
		
		PartnerGetter partnerGetter = new PartnerGetter();
		this.partner.setId(partnerGetter.getPartner(partner.getName()).getId());
		if(partner.getId() == null){
			logSevere("partner with name " + partner.getName() + "not found for scenario : " + 
					partner.getScenarioName());
            this.hasError = true;
            return;
		}
		
		JSONObject request_json = new JSONObject();			
		request_json.put("name", partner.getName());
        
        String url = "/pmpartners/" + partner.getId();
        ApiCaller api_caller = new ApiCaller();
        this.hasError = api_caller.callApi_Manager(step, url, request_json, "GET",this.store);
	}
}
