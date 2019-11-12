package io.mosip.ivv.core.structures;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MispLicense {
	
	private String mispId;
	private String licensekey;
	private String validFromDate;
	private String validToDate;
	private String is_active;
	private String cr_by;
	private String upd_by;
	private LocalDateTime cr_dtimes;
	private LocalDateTime upd_dtimes;
	private String is_deleted;
	private LocalDateTime del_dtimes;
}
