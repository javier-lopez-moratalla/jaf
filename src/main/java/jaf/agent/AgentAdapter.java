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
	protected void receiveEvent(Message message) {}

	@Override
	protected void receiveInformationRequest(Message message) {}

	@Override
	protected void receiveActionRequest(Message message) {}

	@Override
	protected void receiveEventSubscription(Message message) {}

}
