package jaf.agent;

import jams.message.Receiver;

public interface Agent extends Receiver{

	public LifeCycle getLifeCycle();
	
	public void execute(); 
}
