package ie.cloud.cit.johnobee.service;

import ie.cloud.cit.johnobee.domain.Event;
import ie.cloud.cit.johnobee.repository.jpa.IEventRepository;

import java.util.Date;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for {@link Event}
 * 
 * @author krams at {@link http://krams915@blogspot.com}
 */
@Service
@Transactional
public class EventService implements IEventService {
	
	@Autowired
	private IEventRepository repository;
	
		
	public Event create(Event event) {
		return repository.save(event);
	}
	
	public Event read(Long id) {
		return repository.findOne(id);
	}
	
	public List<Event> readAll() {
		return repository.findAll();
	}
	

	
	public Event update(Event event) {
		Event existingEvent = repository.findOne(event.getId());
		
		// Assign new values
		existingEvent.setName(event.getName());
		existingEvent.setParticipants(event.getParticipants());
		existingEvent.setDescription(event.getDescription());
		existingEvent.setDate(event.getDate());
		
		return repository.save(existingEvent);
	}
	
	public Event delete(Long id) {
		repository.delete(id);
		
		return repository.findOne(id);
	}
}

