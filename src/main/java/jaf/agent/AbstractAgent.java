package jaf.agent;

import jams.message.Message;
import jams.message.ReceiverID;
import jams.message.bus.Bus;

public abstract class AbstractAgent implements Agent{

	public static final String ACTION_REQUEST = "actionRequest";
	public static final String INFORMATION_REQUEST = "informationRequest";
	public static final String EVENT_NOTIFICATION = "eventNotification";
	public static final String EVENT_SUBSCRIPTION = "eventSubscription";

	public static final String OK_RESPONSE = "okResponse";
	public static final String ERROR_RESPONSE = "errorResponse";
	public static final String UNKNOWN_RESPONSE = "unknownResponse";
	
	private ReceiverID directoryAgent;
	
	private Bus bus;
	private ReceiverID id;	
	private LifeCycle lifeCycle;
	
	public AbstractAgent(ReceiverID id) {
		super();
		this.id = id;
	}

	@Override
	public ReceiverID getId() {
		
		return id;
	}
		
	public ReceiverID getDirectoryAgent() {
		return directoryAgent;
	}

	public void setDirectoryAgent(ReceiverID directoryAgent) {
		this.directoryAgent = directoryAgent;
	}

	public LifeCycle getLifeCycle() {
		return lifeCycle;
	}

	public void setLifeCycle(LifeCycle lifeCycle) {
		this.lifeCycle = lifeCycle;
	}

	public Bus getBus() {
		return bus;
	}
	
	public void setBus(Bus bus) {
		this.bus = bus;
	}
	
	@Override
	public void receiveMessage(Message message) {
			
		String semantica = message.getBody().getSemantica();
		
		if(semantica.equals(ACTION_REQUEST)){
			
			receiveActionRequest(message);
		}
		else if(semantica.equals(INFORMATION_REQUEST)){
			
			receiveInformationRequest(message);
		}
		else if(semantica.equals(EVENT_NOTIFICATION)){
			
			receiveEvent(message);
		}
		else if(semantica.equals(EVENT_SUBSCRIPTION)){
			
			receiveEventSubscription(message);
		}
		else{
			
			Message response = bus.createMessage(id, message.getHeaders().getSender());
			response.getBody().setSemantica(UNKNOWN_RESPONSE);
			
			bus.sendMessage(response);
		}
	}
	
	protected abstract void receiveEvent(Message message);
	protected abstract void receiveInformationRequest(Message message);
	protected abstract void receiveActionRequest(Message message);
	protected abstract void receiveEventSubscription(Message message);
}
