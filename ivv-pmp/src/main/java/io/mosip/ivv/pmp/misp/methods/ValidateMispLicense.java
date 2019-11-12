package io.mosip.ivv.pmp.misp.methods;

import org.json.simple.JSONObject;

import io.mosip.ivv.core.base.ApiCaller;
import io.mosip.ivv.core.base.Step;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.structures.Misp;
import io.mosip.ivv.core.utils.Utils;
import io.mosip.ivv.pmp.utils.MispGetter;

public class ValidateMispLicense extends Step implements StepInterface{

	private Misp misp;
	private Misp mispFromDb;
	
	@SuppressWarnings("unchecked")
	@Override
	public void run(io.mosip.ivv.core.structures.Scenario.Step step) {
		
		this.misp = this.store.getScenarioPmpData().getMisp();		
		
		MispGetter mispIdGetter = new MispGetter();
		mispFromDb = mispIdGetter.GetMisp(misp.getName());
		if(mispFromDb.getId() == null){
			logSevere("misp with name " + misp.getName() + "not found for scenario : " + 
					misp.getScenarioName());
            this.hasError = true;
            return;
		}
		
		JSONObject request_json = new JSONObject();				
		request_json.put("mispStatus",misp.getIs_active());
		request_json.put("mispLicenseKeyStatus",misp.getLicenseKey_is_active());
		request_json.put("mispLicenseKey",mispFromDb.getLicenseKey());

		JSONObject api_input = new JSONObject();
        api_input.put("id", "mosip.partnermanagement.misp.create");
        api_input.put("version", "1.0");
        api_input.put("requesttime", Utils.getCurrentDateAndTimeForAPI());
        
        api_input.put("request", request_json);
        
        String url = "/pmp/misps/" + mispFromDb.getId() + "/licenseKey";
        
        ApiCaller api_caller = new ApiCaller();
        this.hasError = api_caller.callApi(step, url, api_input, "POST",this.store);		
	}

}
