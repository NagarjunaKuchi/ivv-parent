package io.mosip.ivv.core.structures;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllowedKycAttributes {
	
	private String attributeName;
	private String required;
	
	public AllowedKycAttributes(String attributeName,String required){
		this.attributeName = attributeName;
		this.required = required;
	}
}
