package io.mosip.ivv.core.structures;

import java.util.List;

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
	private String auth_policy_id;
	private String auth_policy_name;
	private String auth_policy_desc;
	private String auth_policy_file_id;
	private String auth_policy_is_active;
	private String auth_policy_cr_dates;
	private String auth_policy_cr_by;
	private String auth_policy_upd_by;
	private String auth_policy_upd_dtimes;
	private String auth_policy_is_deleted;
	private String auth_policy_del_dtimes;
	private List<AuthPolicies> authPolicies;
	private List<AllowedKycAttributes> allowedKycAttributes;
}
