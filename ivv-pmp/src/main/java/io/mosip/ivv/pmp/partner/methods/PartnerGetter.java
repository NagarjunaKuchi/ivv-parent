package io.mosip.ivv.pmp.partner.methods;

import java.util.HashMap;

import org.json.simple.JSONObject;

import com.jayway.jsonpath.ReadContext;

import io.mosip.ivv.core.base.ApiCaller;
import io.mosip.ivv.core.structures.Partner;
import io.mosip.ivv.core.utils.Utils;

public class PartnerGetter {
	
	@SuppressWarnings("unchecked")
	public Partner getPartner(String name) {
		
		JSONObject request_json = new JSONObject();				
		request_json.put("name",name);
		
		JSONObject api_input = new JSONObject();
		api_input.put("id", "mosip.partnermanagement.partners.retrieve");
		api_input.put("version", "1.0");
		api_input.put("requesttime", Utils.getCurrentDateAndTimeForAPI());
        
        api_input.put("request", request_json);
        
        String url = "/partners/findbyname/" + name;
        
        ApiCaller api_caller = new ApiCaller();
        ReadContext ctx = api_caller.callGetApi_Partner(url, api_input);

        Partner partner = new Partner(); 
        String is_active;
        HashMap<String, String> app_info = ctx.read("$['response']");
        
        boolean isActive = app_info.get("isActive") != null;
        if(isActive){
        	is_active = "Active";
        }else{
        	is_active = "De-Active";
        }
        
        partner.setId(app_info.get("id"));
        partner.setAddress(app_info.get("address"));
        partner.setContactNo(app_info.get("contactNo"));
        partner.setCrBy(app_info.get("crBy"));
        partner.setCrDtimes(app_info.get("crDtimes"));
        partner.setDelDtimes(app_info.get("delDtimes"));
        partner.setEmailId(app_info.get("emailId"));
        partner.setIsActive(is_active);
        partner.setName(app_info.get("name"));
        partner.setPolicyGroupName(app_info.get("policyGroupName"));
        partner.setUpdBy(app_info.get("updBy"));
        partner.setUpdDtimes(app_info.get("updDtimes"));
        partner.setUserId(app_info.get("userId"));
        
		return partner;
	}

}
