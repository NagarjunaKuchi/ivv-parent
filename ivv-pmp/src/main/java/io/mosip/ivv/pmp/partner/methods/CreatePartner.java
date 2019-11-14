package io.mosip.ivv.pmp.partner.methods;


import org.json.simple.JSONObject;

import io.mosip.ivv.core.base.ApiCaller;
import io.mosip.ivv.core.base.Step;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.structures.Partner;
import io.mosip.ivv.core.structures.Scenario;
import io.mosip.ivv.core.utils.Utils;

public class CreatePartner extends Step implements StepInterface {

	private Partner partner;

	@SuppressWarnings("unchecked")
	@Override
	public void run(Scenario.Step step) {

		
		this.partner = this.store.getScenarioPmpData().getPartner();

		JSONObject request_json = new JSONObject();
		request_json.put("organizationName", partner.getName());
		request_json.put("contactNumber", partner.getContactNo());
		request_json.put("emailId", partner.getEmailId());
		request_json.put("address", partner.getAddress());
		request_json.put("policyGroup", partner.getPolicyGroupName());
		

		JSONObject api_input = new JSONObject();
		api_input.put("id", "mosip.partnermanagement.partners.create");
		api_input.put("version", "1.0");
		api_input.put("requesttime", Utils.getCurrentDateAndTimeForAPI());

		api_input.put("request", request_json);

		String url = "/partners/partnerReg";

		ApiCaller api_caller = new ApiCaller();
		this.hasError = api_caller.callApi_PartnerService(step, url, api_input, "Post", this.store);
	}
}
