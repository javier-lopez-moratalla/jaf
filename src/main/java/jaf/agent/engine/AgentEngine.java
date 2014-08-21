package jaf.agent.engine;

import jaf.agent.Agent;
import jaf.agent.task.AgentExecutionTask;
import jams.message.ReceiverID;
import jams.message.bus.Bus;
import jasa.schedule.Schedule;

import java.util.HashMap;
import java.util.Map;

public class AgentEngine {

	private Map<ReceiverID,AgentExecutionTask> agents;
	
	private Agent directoryAgent;
	private Bus messageBus;
	private Schedule schedule;
	
	public Bus getMessageBus() {
		return messageBus;
	}
	public void setMessageBus(Bus messageBus) {
		this.messageBus = messageBus;
	}
	public Schedule getSchedule() {
		return schedule;
	}
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	
	public Agent getDirectoryAgent() {
		return directoryAgent;
	}
	public void setDirectoryAgent(Agent directoryAgent) {
		this.directoryAgent = directoryAgent;
	}
	
	public AgentEngine() {
	
		this.agents = new HashMap<ReceiverID, AgentExecutionTask>();
	}
	
	public void init(){
	
		addAgent(directoryAgent);
		
		Thread thread = new Thread(new ScheduleRunnable());
		thread.setDaemon(false);		
		thread.start();		
	}
	
	public void addAgent(Agent agent){
		
		agent.setDirectoryAgent(directoryAgent.getId());
		
		messageBus.addReceiver(agent.getId(), agent);
		agent.setBus(messageBus);
		
		AgentExecutionTask task = new AgentExecutionTask(agent);
		agents.put(agent.getId(), task);
		
		schedule.addTask(task, agent.getLifeCycle().getFirstExecution());
	}
	
	public void removeAgent(Agent agent){
		
		messageBus.removeReceiver(agent.getId());
		AgentExecutionTask task = agents.get(agent.getId());
		
		if(task != null){
		
			agents.remove(task);
			schedule.removeTask(task, task.getNextExecution());
		}
	}
	
	private class ScheduleRunnable implements Runnable{
		
		@Override
		public void run() {
		
			schedule.start();
		}
	}
}
