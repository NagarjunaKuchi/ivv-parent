package io.mosip.ivv.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.ivv.core.utils.Utils;
import io.mosip.ivv.parser.Utils.StepParser;
import io.mosip.ivv.core.structures.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.regex.Pattern;

public class Parser implements ParserInterface {

	
	
    private String SCENARIO_SHEET = "";
    private String CONFIGS_SHEET = "";
    private String GLOBALS_SHEET = "";
    private String PMP_SCENARIO_SHEET = "";
    private String PMP_MISP_SHEET = "";
    private String PMP_PARTNER_SHEET ="";
    
    Properties properties = null;

    public Parser(String USER_DIR, String CONFIG_FILE){
        properties = Utils.getProperties(CONFIG_FILE);
        this.SCENARIO_SHEET = USER_DIR+properties.getProperty("ivv.sheet.scenario");
        this.CONFIGS_SHEET = USER_DIR+properties.getProperty("ivv.sheet.configs");
        this.GLOBALS_SHEET = USER_DIR+properties.getProperty("ivv.sheet.globals");
        this.PMP_SCENARIO_SHEET = USER_DIR+properties.getProperty("ivv.pmp.sheet.scenario");
        this.PMP_MISP_SHEET = USER_DIR+properties.getProperty("ivv.pmp.sheet.misp");
        this.PMP_PARTNER_SHEET = USER_DIR+properties.getProperty("ivv.pmp.sheet.partner");
    }
    public ArrayList<Scenario> getScenarios(){
        ArrayList<?> data = fetchScenarios();
        ArrayList<Scenario> scenario_array = new ArrayList<Scenario>();
        ObjectMapper oMapper = new ObjectMapper();
        Iterator<?> iter = data.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            HashMap<String, String> data_map = oMapper.convertValue(obj, HashMap.class);
            Scenario scenario = new Scenario();
            scenario.setName(data_map.get("tc_no"));
            scenario.setDescription(data_map.get("description"));
            scenario.setPersonaClass(data_map.get("persona_class"));
            scenario.setGroupName(data_map.get("group_name"));
            scenario.setTags(parseTags(data_map.get("tags")));
            scenario.setSteps(formatSteps(data_map));
            for(Scenario.Step stp: scenario.getSteps()){
                if(!scenario.getModules().contains(stp.getModule())){
                    scenario.getModules().add(stp.getModule());
                }
            }
            scenario_array.add(scenario);
        }
        System.out.println("total scenarios parsed: "+scenario_array.size());
        return scenario_array;
    }
    
    public ArrayList<Scenario> getPmpScenarios(){
        ArrayList<?> data = fetchPmpScenarios();
        ArrayList<Scenario> scenario_array = new ArrayList<Scenario>();
        ObjectMapper oMapper = new ObjectMapper();
        Iterator<?> iter = data.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            HashMap<String, String> data_map = oMapper.convertValue(obj, HashMap.class);
            Scenario scenario = new Scenario();
            scenario.setName(data_map.get("tc_no"));
            scenario.setDescription(data_map.get("description"));
            scenario.setPersonaClass(data_map.get("persona_class"));
            scenario.setGroupName(data_map.get("group_name"));
            scenario.setSubModule(data_map.get("subModule"));
            scenario.setScenarioName(data_map.get("scenarioName"));
            scenario.setTags(parseTags(data_map.get("tags")));
            scenario.setSteps(formatSteps(data_map));
            for(Scenario.Step stp: scenario.getSteps()){
                if(!scenario.getModules().contains(stp.getModule())){
                    scenario.getModules().add(stp.getModule());
                }
            }
            scenario_array.add(scenario);
        }
        System.out.println("total scenarios parsed: "+scenario_array.size());
        return scenario_array;
    }

    public HashMap<String, String> getGlobals(){
        ArrayList<?> data = fetchGlobals();
        HashMap<String, String> globals_map = new HashMap<>();
        ObjectMapper oMapper = new ObjectMapper();
        Iterator<?> iter = data.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            HashMap<String, String> data_map = oMapper.convertValue(obj, HashMap.class);
            globals_map.put(data_map.get("key"), data_map.get("value"));
        }
        System.out.println("total global entries parsed: "+globals_map.size());
        return globals_map;
    }

    public HashMap<String, String> getConfigs(){
        ArrayList<?> data = fetchConfigs();
        HashMap<String, String> configs_map = new HashMap<>();
        ObjectMapper oMapper = new ObjectMapper();
        Iterator<?> iter = data.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            HashMap<String, String> data_map = oMapper.convertValue(obj, HashMap.class);
            configs_map.put(data_map.get("key"), data_map.get("value"));
        }
        System.out.println("total config entries parsed: "+configs_map.size());
        return configs_map;
    }

    private ArrayList<?> fetchScenarios(){
        return Utils.csvToList(SCENARIO_SHEET);
    }

    private ArrayList<?> fetchConfigs(){
        return Utils.csvToList(CONFIGS_SHEET);
    }

    private ArrayList<?> fetchGlobals(){
        return Utils.csvToList(GLOBALS_SHEET);
    }

    private ArrayList<?> fetchPmpScenarios(){
        return Utils.csvToList(PMP_SCENARIO_SHEET);
    }
    
    private ArrayList<?> fetchPmpMisps(){
    	return Utils.csvToList(PMP_MISP_SHEET);
    }

    private ArrayList<Scenario.Step> formatSteps(HashMap<String, String> data_map){
        ArrayList<Scenario.Step> steps = new ArrayList<Scenario.Step>();
        for (HashMap.Entry<String, String> entry : data_map.entrySet())
        {
            boolean isMatching = entry.getKey().contains("field");
            if(isMatching && entry.getValue() != null && !entry.getValue().isEmpty()){
                if(entry.getValue() != null && !entry.getValue().equals("")) {
                    steps.add(StepParser.parse(entry.getValue()));
                }
            }
        }
        return steps;
    }

    private ArrayList<String> parseTags(String tags){
        ArrayList<String> tag = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\," );
        String[] split = pattern.split(tags);
        for( int i = 0; i < split.length; i++) {
            tag.add(split[i].trim());
        }
        return tag;
    }

	@Override
	public ArrayList<Misp> getMisps() {
		ArrayList<?> data = fetchPmpMisps();
		ArrayList<Misp> misp_list = new ArrayList<Misp>();
        ObjectMapper oMapper = new ObjectMapper();
        Iterator<?> iter = data.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            HashMap<String, String> data_map = oMapper.convertValue(obj, HashMap.class);
            System.out.println("Parsing Misp: "+ data_map.get("ScenarioName"));
            Misp misp = new Misp();
            
            misp.setSubModule(data_map.get("subModule"));
            misp.setScenarioName(data_map.get("scenarioName"));
            misp.setAddress(data_map.get("address"));
            misp.setContact_no(data_map.get("contact_no"));
            misp.setEmail_id(data_map.get("email_id"));
            misp.setName(data_map.get("name"));
            misp.setIs_active(data_map.get("is_active"));
            misp.setOldName(data_map.get("old_name"));
            misp.setLicenseKey_is_active(data_map.get("license_key_status"));
            misp_list.add(misp);
        }
        
		return misp_list;
	}
	
	public ArrayList<Policy> getPolicies() {
		ArrayList<?> data = fetchPmpMisps();
		ArrayList<Policy> policy_list = new ArrayList<Policy>();
        ObjectMapper oMapper = new ObjectMapper();
        Iterator<?> iter = data.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            HashMap<String, String> data_map = oMapper.convertValue(obj, HashMap.class);
            System.out.println("Parsing policy: "+ data_map.get("scenarioName"));
            Policy policy = new Policy();
            
//            policy.setSubModule(data_map.get("subModule"));
//            policy.setScenarioName(data_map.get("scenarioName"));
//            policy.setAddress(data_map.get("address"));
//            policy.setContact_no(data_map.get("contact_no"));
//            policy.setEmail_id(data_map.get("email_id"));
//            policy.setName(data_map.get("name"));
//            policy.setIs_active(data_map.get("is_active"));
//            policy.setOldName(data_map.get("old_name"));
//            policy.setLicenseKey_is_active(data_map.get("license_key_status"));
            policy_list.add(policy);
        }
        
		return policy_list;
	}
	
	public ArrayList<Partner> getPartners(){
		ArrayList<?> data = fetchPartners();
		ArrayList<Partner> partner_list = new ArrayList<Partner>();
        ObjectMapper oMapper = new ObjectMapper();
        Iterator<?> iter = data.iterator();
        while (iter.hasNext()) {
            Object obj = iter.next();
            HashMap<String, String> data_map = oMapper.convertValue(obj, HashMap.class);
            System.out.println("Parsing Partner: "+ data_map.get("ScenarioName"));
            Partner partner = new Partner();
            
            partner.setSubModule(data_map.get("subModule"));
            partner.setScenarioName(data_map.get("scenarioName"));
            partner.setAddress(data_map.get("address"));
            partner.setContactNo(data_map.get("contact_no"));
            partner.setEmailId(data_map.get("email_id"));
            partner.setName(data_map.get("name"));
            partner.setIsActive(data_map.get("is_active"));
            partner.setStatus(data_map.get("status"));
            partner.setPolicyGroupName(data_map.get("policyGroupName"));
            partner.setNewPolicyGroupName(data_map.get("newPolicyGroupName"));
            
            partner_list.add(partner);
        }
        
		return partner_list;
	}
	private ArrayList<?> fetchPartners() {
		return Utils.csvToList(PMP_PARTNER_SHEET);
	}
}
