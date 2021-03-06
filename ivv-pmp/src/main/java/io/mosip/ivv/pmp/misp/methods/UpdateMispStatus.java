package io.mosip.ivv.pmp.misp.methods;

import org.json.simple.JSONObject;

import io.mosip.ivv.core.base.ApiCaller;
import io.mosip.ivv.core.base.Step;
import io.mosip.ivv.core.base.StepInterface;
import io.mosip.ivv.core.structures.Misp;
import io.mosip.ivv.core.utils.Utils;
import io.mosip.ivv.pmp.utils.MispGetter;

public class UpdateMispStatus extends Step implements StepInterface{

	private Misp misp;
	
	@SuppressWarnings("unchecked")
	@Override
	public void run(io.mosip.ivv.core.structures.Scenario.Step step) {
		
		this.misp = this.store.getScenarioPmpData().getMisp();			
		
		
		MispGetter mispIdGetter = new MispGetter();
		this.misp.setId(mispIdGetter.GetMisp(misp.getName()).getId());
		if(misp.getId() == null){
			logSevere("misp with name " + misp.getOldName() + "not found for scenario : " + 
					misp.getScenarioName());
            this.hasError = true;
            return;
		}
		
		JSONObject request_json = new JSONObject();				
		request_json.put("mispId",misp.getId());
		request_json.put("mispStatus",misp.getIs_active());
		
		
        JSONObject api_input = new JSONObject();
        api_input.put("id", "mosip.partnermanagement.misp.update");
        api_input.put("version", "1.0");
        api_input.put("requesttime", Utils.getCurrentDateAndTimeForAPI());
        
        api_input.put("request", request_json);
        
        String url = "/pmp/misps/" + misp.getId();
        
        ApiCaller api_caller = new ApiCaller();
        this.hasError = api_caller.callApi(step, url, api_input, "PUT",this.store);
		
	}

}
