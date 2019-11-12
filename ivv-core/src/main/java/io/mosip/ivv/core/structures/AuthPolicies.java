package io.mosip.ivv.core.structures;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthPolicies {
	
	private String authType;
	private String authSubType;
	private String mandatory;
}
