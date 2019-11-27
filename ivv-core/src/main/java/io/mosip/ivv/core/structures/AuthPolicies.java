package io.mosip.ivv.core.structures;

public class AuthPolicies {
	
	public String authType;
	public String authSubType;
	public String mandatory;
	
	public AuthPolicies(String authType,String authSubType,String mandatory){
	 
	 this.authSubType = authSubType;
	 this.authType = authType;
	 this.mandatory = mandatory;
	}
}
