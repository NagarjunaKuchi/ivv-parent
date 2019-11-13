package io.mosip.ivv.pmp.partner.methods;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.ReadContext;

import io.mosip.ivv.core.base.ApiCaller;

public class PolicyIdGetter {
		
		@SuppressWarnings("unchecked")
		public String getPolicyId(String PolicyName) {
	        
			JSONObject request_json = new JSONObject();				
			request_json.put("policyName",PolicyName);
			
	        String url = "/partners/" + "findbypolicyname/" + PolicyName;
	        
	        ApiCaller api_caller = new ApiCaller();
	        ReadContext ctx = api_caller.callGetApiKeyReq(url, request_json);
	        
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	        String app_info = ctx.read("$['response']['policyId']").toString();
	       
			return app_info;
			
		}
}
