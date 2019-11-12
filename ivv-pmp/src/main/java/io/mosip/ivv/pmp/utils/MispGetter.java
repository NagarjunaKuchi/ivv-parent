package io.mosip.ivv.pmp.utils;

import org.json.simple.JSONObject;

import com.jayway.jsonpath.ReadContext;

import io.mosip.ivv.core.base.ApiCaller;
import io.mosip.ivv.core.structures.Misp;
import io.mosip.ivv.core.utils.Utils;

public class MispGetter {
	
	@SuppressWarnings("unchecked")
	public Misp GetMisp(String name) {
		
		JSONObject request_json = new JSONObject();				
		request_json.put("name",name);
		
        JSONObject api_input = new JSONObject();
        api_input.put("id", "mosip.partnermanagement.misp.get");
        api_input.put("version", "1.0");
        api_input.put("requesttime", Utils.getCurrentDateAndTimeForAPI());
        
        api_input.put("request", request_json);
        
        String url = "/pmp/misps/misp/" + name;
        ApiCaller api_caller = new ApiCaller();
        ReadContext ctx = api_caller.callGetApi(url, api_input);

        Misp misp = new Misp();
        String is_misp_active;
        String is_misp_license_active;
        
        if((boolean)ctx.read("$[0]['mispLicenses']['isActive']")){
        	is_misp_active = "Active";
        }else{
        	is_misp_active = "De-Active";
        }
        
        if((boolean)ctx.read("$[0]['mispLicenses']['isActive']")){
        	is_misp_license_active = "Active";
        }else{
        	is_misp_license_active = "De-Active";
        }

        misp.setId(ctx.read("$[0]['id']"));
        misp.setName(ctx.read("$[0]['name']"));
        misp.setAddress(ctx.read("$[0]['address']"));
        misp.setContact_no(ctx.read("$[0]['contactNumber']"));
        misp.setCr_by(ctx.read("$[0]['createdBy']"));
        misp.setCr_dtimes(ctx.read("$[0]['createdDateTime']"));
        misp.setDel_dtimes(ctx.read("$[0]['deletedDateTime']"));
        misp.setEmail_id(ctx.read("$[0]['emailId']"));
        misp.setIs_active(is_misp_active);
        misp.setLicenseKey(ctx.read("$[0]['mispLicenses']['license_key']"));
        misp.setLicenseKey_del_dtimes(ctx.read("$[0]['mispLicenses']['deletedDateTime']"));
        misp.setLicenseKey_is_active(is_misp_license_active);
        misp.setLicenseKey_is_deleted(ctx.read("$[0]['mispLicenses']['isDeleted']"));
        
		return misp;
	}

}
