package io.mosip.ivv.pmp.partmanager.methods;

import org.json.simple.JSONObject;

import io.mosip.ivv.core.base.ApiCaller;
import io.mosip.ivv.core.base.Step;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.structures.Partner;
import io.mosip.ivv.pmp.partner.methods.PartnerAPIKeyReqIDGetter;
import io.mosip.ivv.pmp.partner.methods.PartnerGetter;

public class GetTheRequestForPartnerAPIKeyToPolicyMapping extends Step implements StepInterface{
	
	
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
		
		
		PartnerAPIKeyReqIDGetter partnerAPIKeyReqIDGetter = new PartnerAPIKeyReqIDGetter();
		this.partner.setAPIKeyReqID(partnerAPIKeyReqIDGetter.getPartnerAPIKeyReqIDGetter(partner.getId()));
		if(partner.getId() == null){
			logSevere("partner with name " + partner.getAPIKeyReqID() + "not found for scenario : " + 
					partner.getScenarioName());
            this.hasError = true;
            return;
		}
		
		JSONObject request_json = new JSONObject();				
		request_json.put("name",partner.getName());
        
        String url = "/pmpartners" + "/PartnerAPIKeyRequests/" + partner.getAPIKeyReqID();
        ApiCaller api_caller = new ApiCaller();
        this.hasError = api_caller.callApi(step, url, request_json, "GET",this.store);
	}
}
