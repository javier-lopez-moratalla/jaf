package jaf.agent;

import jams.message.Receiver;
import jams.message.ReceiverID;
import jams.message.bus.Bus;

public interface Agent extends Receiver{

	public void setDirectoryAgent(ReceiverID agent);
	public void setBus(Bus bus);
	
	public LifeCycle getLifeCycle();
	
	public void execute(); 
}
