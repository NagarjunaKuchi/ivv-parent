package io.mosip.ivv.core.structures;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Policy {
	private String subModule;
	private String scenarioName;
	private String policyId;
	private String policyName;
	private String policyDesc;
	private String user_id;
	private String policy_is_active;
	private String policy_cr_dates;
	private String policy_cr_by;
	private String policy_upd_by;
	private String policy_upd_dtimes;
	private String policy_is_deleted;
	private String policy_del_dtimes;
	private AuthPolicy auth_policy;
	private String result;
}
