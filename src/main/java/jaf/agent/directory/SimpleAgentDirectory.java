package jaf.agent.directory;

import jaf.agent.AgentAdapter;
import jams.message.Message;
import jams.message.ReceiverID;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleAgentDirectory extends AgentAdapter implements AgentDirectory {

	private Map<String,Map<String,Set<ReceiverID>>> directory;
	
	public SimpleAgentDirectory(ReceiverID id) {
		super(id);
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

	@Override
	protected void receiveInformationRequest(Message message) {
	
		SearchCriteriaRequest criteria = (SearchCriteriaRequest)message.getBody().getContent();
	
		List<ReceiverID> searchResult = searchAgents(criteria.getCriteria());
		Message response = buildResponse(message, searchResult);
		
		getBus().sendMessage(response);
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
	}
}
