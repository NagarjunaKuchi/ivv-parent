package io.mosip.ivv.pmp.partner.methods;


import org.json.simple.JSONObject;

import io.mosip.ivv.core.base.ApiCaller;
import io.mosip.ivv.core.base.Step;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.structures.Partner;
import io.mosip.ivv.core.utils.Utils;

public class PartnerApiKeyToPolicyMappings extends Step implements StepInterface{

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
		
		DownloadPartnerAPIKey downloadPartnerAPIKey = new DownloadPartnerAPIKey();
		this.partner.setPartnerAPIKey(downloadPartnerAPIKey.getPartnerAPIKey(partner.getId(), partner.getAPIKeyReqID()));
		if(partner.getId() == null){
			logSevere("partner with name " + partner.getName() + "not found for scenario : " + 
					partner.getScenarioName());
            this.hasError = true;
            return;
		}
		
		PolicyIdGetter policyIdGetter = new PolicyIdGetter();
		String oldPolicyId = policyIdGetter.getPolicyId(partner.getPolicyGroupName());
		String newPolicyId = policyIdGetter.getPolicyId(partner.getNewPolicyGroupName());
		
		JSONObject request_json = new JSONObject();			
		request_json.put("oldPolicyID", oldPolicyId);
		request_json.put("newPolicyID", newPolicyId);
		
		
		JSONObject api_input = new JSONObject();
		api_input.put("id", "mosip.partnermanagement.partners.policy.mapping");
		api_input.put("version", "1.0");
		api_input.put("requesttime", Utils.getCurrentDateAndTimeForAPI());
        
        api_input.put("request", request_json);
        
        String url = "/pmpartners/" + partner.getId() + "/" + partner.getPartnerAPIKey();
        ApiCaller api_caller = new ApiCaller();
        this.hasError = api_caller.callApi_Manager(step, url, api_input, "POST",this.store);
		
	}

}
