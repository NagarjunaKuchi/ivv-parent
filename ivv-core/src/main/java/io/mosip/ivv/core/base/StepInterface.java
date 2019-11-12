package io.mosip.ivv.core.base;


import com.aventstack.extentreports.ExtentTest;

import io.mosip.ivv.core.structures.CallRecord;
import io.mosip.ivv.core.structures.Scenario;
import io.mosip.ivv.core.structures.Store;

public interface StepInterface {
    Boolean hasError();

    void setState(Store s);

    Store getState();

    void setExtentInstance(ExtentTest e);

    CallRecord getCallRecord();
    
    void run(Scenario.Step step);
}
