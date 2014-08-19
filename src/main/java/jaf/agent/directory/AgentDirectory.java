package jaf.agent.directory;

import jams.message.ReceiverID;

import java.util.List;

public interface AgentDirectory {

	public static final String REGISTER_EVENT_ID = "agentRegistered";
	public static final String UNREGISTER_EVENT_ID = "agentUnregistered";
	
	public static final String CRITERIA_PARAM = "criteria";	
	public static final String AGENT_PARAM = "agent";
	
	public void registerAgent(List<Criterion> parameters,ReceiverID agent);
	public void unregisterAgent(List<Criterion> parameters,ReceiverID agent);
}
