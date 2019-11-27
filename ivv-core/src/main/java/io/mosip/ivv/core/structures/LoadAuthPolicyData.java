package io.mosip.ivv.core.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadAuthPolicyData {	
	
	public static HashMap<String,AuthPolicy> getPolicies(){		
		HashMap<String, AuthPolicy> domainPolicies = new HashMap<String, AuthPolicy>();
		domainPolicies.put("Telecom", getTelecomPolicy());
		return null;
		
	}
	
	
	public static AuthPolicy getTelecomPolicy(){
		AuthPolicy authpolicy= new AuthPolicy();
		authpolicy.setAuth_policy_name("Telecom");
		authpolicy.setAuth_policy_desc("Telecom Policy");
		authpolicy.setAllowedKycAttributes(getTelecomAllowedKycAttributes());
		authpolicy.setAuthPolicies(getTelecomAuthPolicy());
		return authpolicy;
	}
	
	private static AuthPolicy getInsurancePolicy(){
		AuthPolicy authpolicy= new AuthPolicy();
		authpolicy.setAuth_policy_name("Insurance");
		authpolicy.setAuth_policy_desc("Insurance Policy");
		authpolicy.setAllowedKycAttributes(getInsuranceAllowedKycAttributes());
		authpolicy.setAuthPolicies(getInsuranceAuthPolicy());
		return authpolicy;
	}
	
	private static AuthPolicy getBankingPolicy(){
		AuthPolicy authpolicy= new AuthPolicy();
		authpolicy.setAuth_policy_name("Banking");
		authpolicy.setAuth_policy_desc("Banking Policy");
		authpolicy.setAllowedKycAttributes(getBankingAllowedKycAttributes());
		authpolicy.setAuthPolicies(getBankingAuthPolicy());
		return authpolicy;
	}

	//AuthPolicies
	private static List<AuthPolicies> getTelecomAuthPolicy(){
		List<AuthPolicies> authPolicy = new ArrayList<>();
		authPolicy.add(new AuthPolicies("otp","null","true"));
		authPolicy.add(new AuthPolicies("demo","null","false"));
		authPolicy.add(new AuthPolicies("bio","FINGER","true"));
		authPolicy.add(new AuthPolicies("bio","FACE","false"));
		authPolicy.add(new AuthPolicies("bio","IRIS","false"));
		authPolicy.add(new AuthPolicies("kyc","null","true"));
		return authPolicy;
	}
	
	private static List<AllowedKycAttributes> getTelecomAllowedKycAttributes(){
		List<AllowedKycAttributes> kycAttributes = new ArrayList<AllowedKycAttributes>();
		kycAttributes.add(new AllowedKycAttributes("fullName", "true"));
		kycAttributes.add(new AllowedKycAttributes("dateOfBirth", "true"));
		kycAttributes.add(new AllowedKycAttributes("gender", "true"));
		kycAttributes.add(new AllowedKycAttributes("phone", "true"));
		kycAttributes.add(new AllowedKycAttributes("email", "true"));
		kycAttributes.add(new AllowedKycAttributes("addressLine1", "true"));
		kycAttributes.add(new AllowedKycAttributes("addressLine2", "true"));
		kycAttributes.add(new AllowedKycAttributes("location1", "true"));
		kycAttributes.add(new AllowedKycAttributes("postalCode", "true"));
		kycAttributes.add(new AllowedKycAttributes("photo", "true"));
		return kycAttributes;
	}	
	
	private static List<AuthPolicies> getInsuranceAuthPolicy(){
		List<AuthPolicies> authPolicy = new ArrayList<>();
		return authPolicy;
	}
	
	private static List<AllowedKycAttributes> getInsuranceAllowedKycAttributes(){
		List<AllowedKycAttributes> kycAttributes = new ArrayList<AllowedKycAttributes>();
		return kycAttributes;
	}
	
	private static List<AuthPolicies> getBankingAuthPolicy(){
		List<AuthPolicies> authPolicy = new ArrayList<>();
		return authPolicy;
	}
	
	private static List<AllowedKycAttributes> getBankingAllowedKycAttributes(){
		List<AllowedKycAttributes> kycAttributes = new ArrayList<AllowedKycAttributes>();
		return kycAttributes;
	}
}
