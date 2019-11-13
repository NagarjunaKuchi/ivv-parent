package io.mosip.ivv.pmp.partner.methods;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.ReadContext;

import io.mosip.ivv.core.base.ApiCaller;
import io.mosip.ivv.core.utils.Utils;



public class DownloadPartnerAPIKey{
	
	@SuppressWarnings("unchecked")
	public String getPartnerAPIKey(String PartnerId , String PartnerApiKeyReqId){
		
		JSONObject request_json = new JSONObject();				
		request_json.put("id",PartnerId);
		
		JSONObject api_input = new JSONObject();
		api_input.put("id", "mosip.partnermanagement.partnerAPIKey.download");
		api_input.put("version", "1.0");
		api_input.put("requesttime", Utils.getCurrentDateAndTimeForAPI());
        
        api_input.put("request", request_json);
        
        String url = "/partners/" + PartnerId + "/partnerAPIKeyRequests/" + PartnerApiKeyReqId;
        
        ApiCaller api_caller = new ApiCaller();
        ReadContext ctx = api_caller.callGetApiKeyReq(url, api_input);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);   
        String Partner_API_Key = ctx.read("$['response']['partnerApiKey']").toString();
       
		return Partner_API_Key;
	}
	
}
