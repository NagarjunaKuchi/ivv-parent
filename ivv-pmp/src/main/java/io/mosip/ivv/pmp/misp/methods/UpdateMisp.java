package io.mosip.ivv.pmp.misp.methods;

import org.json.simple.JSONObject;

import io.mosip.ivv.core.base.ApiCaller;
import io.mosip.ivv.core.base.Step;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.structures.Misp;
import io.mosip.ivv.core.utils.Utils;
import io.mosip.ivv.pmp.utils.MispGetter;

public class UpdateMisp extends Step implements StepInterface {
	
	private Misp misp;
	
	@SuppressWarnings("unchecked")
	@Override
	public void run(io.mosip.ivv.core.structures.Scenario.Step step) {		

		this.misp = this.store.getScenarioPmpData().getMisp();	

		MispGetter mispIdGetter = new MispGetter();
		this.misp.setId(mispIdGetter.GetMisp(misp.getOldName()).getId());
		if(misp.getId() == null){
			logSevere("misp with name " + misp.getOldName() + "not found for scenario : " + 
					misp.getScenarioName());
            this.hasError = true;
            return;
		}
		
		JSONObject request_json = new JSONObject();				
		request_json.put("name",misp.getName());
		request_json.put("contactNumber",misp.getContact_no());
		request_json.put("emailId",misp.getEmail_id());
		request_json.put("address",misp.getAddress());
		
        JSONObject api_input = new JSONObject();
        api_input.put("id", "mosip.partnermanagement.misp.create");
        api_input.put("version", "1.0");
        api_input.put("requesttime", Utils.getCurrentDateAndTimeForAPI());
        
        api_input.put("request", request_json);
        
        String url = "/pmp/misps/" + misp.getId();

        ApiCaller api_caller = new ApiCaller();
        this.hasError = api_caller.callApi(step, url, api_input,"POST",this.store);
	}
}
