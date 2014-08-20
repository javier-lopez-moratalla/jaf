package jaf.agent;

import jams.message.Message;
import jams.message.ReceiverID;

public class AgentAdapter extends AbstractAgent {

	public AgentAdapter(ReceiverID id) {
		super(id);
	}

	@Override
	public void execute() {}

	@Override
	protected Message receiveEvent(Message message) {
		
		return generateUnknownResponse(message);
	}

	@Override
	protected Message receiveInformationRequest(Message message) {
		
		return generateUnknownResponse(message);
	}

	@Override
	protected Message receiveActionRequest(Message message) {
		
		return generateUnknownResponse(message);
	}

	@Override
	protected Message receiveEventSubscription(Message message) {
		
		return generateUnknownResponse(message);
	}
	
	private Message generateUnknownResponse(Message message){
		
		Message response = getBus().createMessage(getId(), message.getHeaders().getSender());
		
		response.getHeaders().setConversationId(message.getHeaders().getConversationId());
		response.getBody().setSemantica(UNKNOWN_RESPONSE);
		
		return response;		
	}

}
