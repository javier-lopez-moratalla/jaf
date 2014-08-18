package jaf.agent.directory;

import java.util.List;

public interface RegisterAgentRequest {

	public boolean isRegister();	
	public List<Criterion> getCriteria();
}
