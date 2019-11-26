package io.mosip.ivv.pmp.partner.methods;

import org.json.simple.JSONObject;

import io.mosip.ivv.core.base.ApiCaller;
import io.mosip.ivv.core.base.Step;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.structures.Partner;
import io.mosip.ivv.core.structures.Scenario;
import io.mosip.ivv.core.utils.Utils;

public class UpdatePartner extends Step implements StepInterface{

	
	private Partner partner;

	@SuppressWarnings("unchecked")
	@Override
	public void run(Scenario.Step step) {

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
		request_json.put("organizationName", partner.getName());
		request_json.put("contactNumber", partner.getContactNo());
		request_json.put("emailId", partner.getEmailId());
		request_json.put("address", partner.getAddress());
		

		JSONObject api_input = new JSONObject();
		api_input.put("id", "mosip.partnermanagement.partners.update");
		api_input.put("version", "1.0");
		api_input.put("requesttime", Utils.getCurrentDateAndTimeForAPI());
		
		api_input.put("request", request_json);

		String url = "/partners/" + partner.getId();

		ApiCaller api_caller = new ApiCaller();
		this.hasError = api_caller.callApi(step, url, api_input, "PUT", this.store);
	}
}
