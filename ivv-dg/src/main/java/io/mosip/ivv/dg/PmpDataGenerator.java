package io.mosip.ivv.dg;

import java.util.ArrayList;
import java.util.HashMap;

import io.mosip.ivv.core.structures.Misp;
import io.mosip.ivv.core.structures.Scenario;
import io.mosip.ivv.dg.exceptions.MispNotFoundException;
import io.mosip.ivv.parser.Parser;

public class PmpDataGenerator implements DataGeneratorInterface {

	private String user_dir = "";
    private String config_file = "";
    private ArrayList<Scenario> generatedScenarios= null;
    private HashMap<String, String> globals = null;
    private HashMap<String, String> configs = null;
    private ArrayList<Misp> misps = null;
    
    public PmpDataGenerator(String USER_DIR, String CONFIG_FILE) {
        this.user_dir = USER_DIR;
        this.config_file = CONFIG_FILE;
        generate();
    }
	
	@Override
	public void saveScenariosToDb() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Scenario> getScenarios() {		
		return this.generatedScenarios;
	}

	@Override
	public HashMap<String, String> getConfigs() {
		return this.configs;
	}

	@Override
	public HashMap<String, String> getGlobals() {
		return this.globals;
	}
	
	private void generate(){
        Parser parser = new Parser(this.user_dir, this.config_file);
        this.globals = parser.getGlobals();
        this.configs = parser.getConfigs();
        ArrayList<Scenario> scenarios = parser.getPmpScenarios();
        this.misps = parser.getMisps();
        this.generatedScenarios = new ArrayList<Scenario>();
        for (Scenario scenario : scenarios){
        	scenario.setPmpData(new Scenario.PmpData());
        	scenario.getPmpData().setMisp(addMispData(scenario,this.misps));
        	this.generatedScenarios.add(scenario);
        }

	}

	private Misp addMispData(Scenario scenario, ArrayList<Misp> misps) {		
		for(Misp mi : misps){
			if(scenario.getSubModule() != null && !scenario.getSubModule().isEmpty()
					&& scenario.getScenarioName() != null && !scenario.getScenarioName().isEmpty()
					&& mi.getScenarioName() != null && mi.getScenarioName().equals(scenario.getScenarioName())
					&& mi.getSubModule() !=null && mi.getSubModule().equals(scenario.getSubModule())){
				return mi;
			}
		}
		
		throw new MispNotFoundException("misp not found");
	}

}
