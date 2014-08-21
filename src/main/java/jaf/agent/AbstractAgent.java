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
	public static final String INFORMATION_RESPONSE = "informationRequest";
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

		Message response = null;
		
		String semantica = message.getBody().getSemantica();
		
		if(semantica.equals(ACTION_REQUEST)){
			
			response = receiveActionRequest(message);
		}
		else if(semantica.equals(INFORMATION_REQUEST)){
			
			response = receiveInformationRequest(message);
		}
		else if(semantica.equals(EVENT_NOTIFICATION)){
			
			response = receiveEvent(message);
		}
		else if(semantica.equals(EVENT_SUBSCRIPTION)){
			
			response = receiveEventSubscription(message);
		}
		else{
						
			response = bus.createMessage(id, message.getHeaders().getSender());
			response.getHeaders().setConversationId(message.getHeaders().getConversationId());
			response.getBody().setSemantica(UNKNOWN_RESPONSE);			
		}
		
		if(response!=null){
			
			bus.sendMessage(response);
		}
	}
	
	protected abstract Message receiveEvent(Message message);
	protected abstract Message receiveInformationRequest(Message message);
	protected abstract Message receiveActionRequest(Message message);
	protected abstract Message receiveEventSubscription(Message message);
}
