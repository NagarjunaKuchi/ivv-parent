package io.mosip.ivv.parser;

import java.util.ArrayList;
import java.util.HashMap;

import io.mosip.ivv.core.structures.Misp;
import io.mosip.ivv.core.structures.Scenario;

public interface ParserInterface {    
    
	ArrayList<Misp> getMisps();
    
    ArrayList<Scenario> getScenarios();
    
    HashMap<String, String> getGlobals();

}
