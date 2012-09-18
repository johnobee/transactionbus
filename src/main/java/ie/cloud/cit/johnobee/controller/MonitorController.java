package ie.cloud.cit.johnobee.controller;

import ie.cloud.cit.johnobee.domain.Event;
import ie.cloud.cit.johnobee.domain.Transaction;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/monitor")
public class MonitorController {
	

	@Autowired AmqpTemplate amqpTemplate;

	private final Queue<String> eventMessages = new LinkedBlockingQueue<String>();
	private final Queue<String> errorMessages = new LinkedBlockingQueue<String>();
	private final Queue<String> transactionMessages = new LinkedBlockingQueue<String>();

	@RequestMapping(value = "/event")
	public String eventPage() {
		return "event-monitor-page";
	}
	
	@RequestMapping(value = "/error")
	public String errorPage() {
		return "error-monitor-page";
	}
	
	

	@RequestMapping(value = "/event/log")
	@ResponseBody
	public String eventLog() {
		
	/*
	 * This is to display the new Transaction Queue 
	 */
		return StringUtils.arrayToDelimitedString(this.transactionMessages.toArray(), "<br/>");
	}

		
	@RequestMapping(value = "/error/log")
	@ResponseBody
	public String errorLog() {
		return StringUtils.arrayToDelimitedString(this.errorMessages.toArray(), "<br/>");
	}
	
	/**
	 * Handles normal event messages.
	 * This method is invoked when a RabbitMQ message is received.
	 */
	
	public void handleEvent(String message) {
		if (eventMessages.size() > 100) {
			eventMessages.remove();
		}
		eventMessages.add(message);
	}
	
	/**
	 * Handles error messages.
	 * This method is invoked when a RabbitMQ message is received.
	 */
	public void handleError(String message) {
		if (errorMessages.size() > 100) {
			errorMessages.remove();
		}
		errorMessages.add(message);
	}

	/**
	 * Handles normal event messages.
	 * This method is invoked when a RabbitMQ message is received.
	 */
	
	public void handleTransactions(String transaction) {
		if (transactionMessages.size() > 100) {
			transactionMessages.remove();
		}
		transactionMessages.add(transaction);
	}
	
	
}
