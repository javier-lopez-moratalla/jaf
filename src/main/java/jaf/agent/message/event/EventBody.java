package jaf.agent.message.event;

import java.util.Map;

public class EventBody {

	private String id;
	private Map<String,Object> parameters;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Map<String, Object> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}		
}
