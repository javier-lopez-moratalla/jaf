package jaf.agent.directory;

import jaf.agent.AgentAdapter;
import jaf.agent.message.event.EventBody;
import jaf.agent.message.event.EventSubscriptionBody;
import jams.message.Message;
import jams.message.ReceiverID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleAgentDirectory extends AgentAdapter implements AgentDirectory {
 
	private Map<ReceiverID,List<ReceiverID>> listenerMap;
	private Map<String,Map<String,Set<ReceiverID>>> directory;
	
	public SimpleAgentDirectory(ReceiverID id) {
		super(id);
		
		this.directory = new HashMap<String,Map<String,Set<ReceiverID>>>();
		this.listenerMap = new HashMap<ReceiverID,List<ReceiverID>>();
	}
	
	@Override
	protected void receiveActionRequest(Message message) {
	
		RegisterAgentRequest request = (RegisterAgentRequest)message.getBody().getContent();
		
		if(request.isRegister()){
			
			registerAgent(request.getCriteria(), message.getHeaders().getSender());
		}
		else{
			unregisterAgent(request.getCriteria(), message.getHeaders().getSender());
		}
	}
	
	private void notifyEvent(boolean register,ReceiverID agent,List<Criterion> criteria){
		
		List<ReceiverID> listeners = listenerMap.get(agent);
		List<ReceiverID> receivers = new ArrayList<ReceiverID>(listeners);
		
		Message message = getBus().createMessage(getId(), receivers);
		message.getBody().setSemantica(EVENT_NOTIFICATION);
		
		EventBody body = new EventBody();
		body.setId(register?REGISTER_EVENT_ID:UNREGISTER_EVENT_ID);
		
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put(CRITERIA_PARAM,criteria);
		parameters.put(AGENT_PARAM, agent);
		
		body.setParameters(parameters);
		message.getBody().setContent(body);
		
		getBus().sendMessage(message);
	}

	@Override
	protected void receiveInformationRequest(Message message) {
	
		SearchCriteriaRequest criteria = (SearchCriteriaRequest)message.getBody().getContent();
	
		List<ReceiverID> searchResult = searchAgents(criteria.getCriteria());
		Message response = buildResponse(message, searchResult);
		
		getBus().sendMessage(response);
	}
	
	@Override
	protected void receiveEventSubscription(Message message) {
	
		EventSubscriptionBody body = (EventSubscriptionBody)message.getBody().getContent();
		
		String event = body.getEventId();
		if(event.equals(REGISTER_EVENT_ID)||event.equals(UNREGISTER_EVENT_ID)){
			
			ReceiverID agente = (ReceiverID)body.getParameters().get(AGENT_PARAM);
			List<ReceiverID> listeners = listenerMap.get(agente);
			if(body.isSubscription()){
			
				listeners.add(message.getHeaders().getSender());
			}
			else{
				
				listeners.remove(message.getHeaders().getSender());
			}
			
			Message response = getBus().createMessage(getId(), message.getHeaders().getSender());
			response.getBody().setSemantica(OK_RESPONSE);
			response.getHeaders().setConversationId(message.getHeaders().getConversationId());
			
			getBus().sendMessage(response);
		}
		else{
			
			Message response = getBus().createMessage(getId(), message.getHeaders().getSender());
			response.getBody().setSemantica(UNKNOWN_RESPONSE);
			response.getHeaders().setConversationId(message.getHeaders().getConversationId());
			
			getBus().sendMessage(response);
		}
	}
	
	private List<ReceiverID> searchAgents(List<Criterion> criteria){
		
		List<ReceiverID> result = new LinkedList<ReceiverID>();
		
		for(Criterion criterion:criteria){
			
			Map<String,Set<ReceiverID>> categoryMap = directory.get(criterion.getCategory());			
			if(categoryMap != null){
							
				Set<ReceiverID> subcategory = categoryMap.get(criterion.getSubcategory());
				if(subcategory != null){
										
					result.addAll(subcategory);
				}
			}
		}
		
		return result;
	}
	
	private Message buildResponse(Message request,List<ReceiverID> searchResult){
		
		List<ReceiverID> receivers = new LinkedList<ReceiverID>();
		receivers.add(request.getHeaders().getSender());
		
		Message message = getBus().createMessage(getId(),receivers);
		message.getBody().setContent(searchResult);
		message.getHeaders().setConversationId(request.getHeaders().getConversationId());
				
		return message;
	}
	
	@Override
	public void registerAgent(List<Criterion> parameters, ReceiverID agent) {
	
		for(Criterion criterion:parameters){
			
			Map<String,Set<ReceiverID>> categoryMap = directory.get(criterion.getCategory());			
			if(categoryMap == null){
				
				categoryMap = new HashMap<String, Set<ReceiverID>>();
				directory.put(criterion.getCategory(), categoryMap);
			}
			
			Set<ReceiverID> subcategory = categoryMap.get(criterion.getSubcategory());
			if(subcategory == null){
				
				subcategory = new HashSet<ReceiverID>();
				categoryMap.put(criterion.getSubcategory(),subcategory);
			}
			
			subcategory.add(agent);
		}
		
		notifyEvent(true, agent, parameters);
	}
	
	@Override
	public void unregisterAgent(List<Criterion> parameters, ReceiverID agent) {
	
		for(Criterion criterion:parameters){
			
			Map<String,Set<ReceiverID>> categoryMap = directory.get(criterion.getCategory());			
			if(categoryMap != null){
				
				Set<ReceiverID> subcategory = categoryMap.get(criterion.getSubcategory());			
				if(subcategory != null){
					
					subcategory.remove(agent);
				}
			}
		}
		
		notifyEvent(false, agent, parameters);
	}
}
