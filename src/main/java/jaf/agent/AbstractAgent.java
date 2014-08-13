package jaf.agent;

import jams.message.Message;
import jams.message.ReceiverID;

public abstract class AbstractAgent implements Agent{

	public static final String ACTION_REQUEST = "actionRequest";
	public static final String INFORMATION_REQUEST = "informationRequest";
	public static final String EVENT_NOTIFICATION = "eventNotification";
	public static final String EVENT_SUBSCRIPTION = "eventSubscription";

	private ReceiverID id;	
	private LifeCycle lifeCycle;
	
	public AbstractAgent(ReceiverID id) {
		super();
		this.id = id;
	}

	@Override
	public ReceiverID getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public LifeCycle getLifeCycle() {
		return lifeCycle;
	}

	public void setLifeCycle(LifeCycle lifeCycle) {
		this.lifeCycle = lifeCycle;
	}

	@Override
	public void receiveMesssage(Message message) {
			
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
	}
	
	protected abstract void receiveEvent(Message message);
	protected abstract void receiveInformationRequest(Message message);
	protected abstract void receiveActionRequest(Message message);
	protected abstract void receiveEventSubscription(Message message);
}
