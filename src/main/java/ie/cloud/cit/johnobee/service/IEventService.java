package ie.cloud.cit.johnobee.service;

import ie.cloud.cit.johnobee.domain.Event;

import java.util.List;


/**
 * Service interface for {@link Event}
 * 
 * @author krams at {@link http://krams915@blogspot.com}
 */
public interface IEventService {

	/* CRUD  
	  
	 */
	
	public Event create(Event event);

	public Event read(Long id);

	public Event update(Event event);

	public Event delete(Long id);

	
	public List<Event> readAll();
	

}