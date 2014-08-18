package jaf.agent.directory;

import jams.message.ReceiverID;

import java.util.List;

public interface AgentDirectory {

	public void registerAgent(List<Criterion> parameters,ReceiverID agent);
	public void unregisterAgent(List<Criterion> parameters,ReceiverID agent);
}
