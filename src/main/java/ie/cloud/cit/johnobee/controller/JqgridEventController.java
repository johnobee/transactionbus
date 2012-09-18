package ie.cloud.cit.johnobee.controller;

import ie.cloud.cit.johnobee.domain.*;
import ie.cloud.cit.johnobee.dto.JqgridTableDto;
import ie.cloud.cit.johnobee.dto.ResponseDto;
import ie.cloud.cit.johnobee.service.IEventService;

import java.util.Date;
import java.util.List;

import javax.transaction.Transaction;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.amqp.core.AmqpTemplate;
/**
 * Controlling for handling event requests
 * 
 * @author krams at {@link http://krams915@blogspot.com}
 */
@Controller
@RequestMapping("/jqgrid/event")
public class JqgridEventController {
	
	@Autowired
	private volatile IEventService service;
	
	@Autowired AmqpTemplate amqpTemplate;
	public static final String RABBIT_EXCHANGE = "eventExchange";
	public static final String GENERAL_EVENT_ROUTE_KEY = "event.general.*";
	public static final String ERROR_EVENT_ROUTE_KEY = "event.error.*";
	public static final String TRANSACTION_ROUTE_KEY = "transaction.general.*";
	


	
	
	@RequestMapping
	public String getEventPage() {
		return "jqgrid/event-page";
	}

	@CacheEvict(value = "records", allEntries=true)
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody ResponseDto<Event> add(Event event) {
		
		
		
		
		this.amqpTemplate.convertAndSend(RABBIT_EXCHANGE, TRANSACTION_ROUTE_KEY, "This addition has hit the Transaciton Queue @ " + new Date().getTime());

		if (service.create(event) != null) {
			return new ResponseDto<Event>(true);
		}
		
		return new ResponseDto<Event>(false);
	}
	
	@CacheEvict(value = "records", allEntries=true)
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public @ResponseBody ResponseDto<Event> edit(Event event) {
		
		if (service.update(event) != null) {
			return new ResponseDto<Event>(true);
		}

		return new ResponseDto<Event>(false);
	}

	@CacheEvict(value = "records", allEntries=true)
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody ResponseDto<Event> delete(Long id) {
		
		if (service.delete(id) == null) {
			return new ResponseDto<Event>(true);
		}
	
		return new ResponseDto<Event>(false);
	}
	
	@Cacheable(value = "records")
	@RequestMapping(value = "/getall", method = RequestMethod.POST)
	public @ResponseBody JqgridTableDto<Event> getall() {

		List<Event> events = service.readAll();
		
		if (events != null) {
			JqgridTableDto<Event> response = new JqgridTableDto<Event>();
			response.setRows(events);
			response.setRecords(new Integer(events.size()).toString());
			response.setTotal(new String("1"));
			response.setPage(new String("1"));
			
			return response;
		}
		
		return new JqgridTableDto<Event>();
	}
}
