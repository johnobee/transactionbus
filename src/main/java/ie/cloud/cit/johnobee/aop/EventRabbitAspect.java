package ie.cloud.cit.johnobee.aop;

import java.util.Date;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Interceptor for publishing messages to RabbitMQ 
 * 
 * @author krams at {@link http://krams915@blogspot.com}
 */
@Aspect
@Order(1)
@Component
public class EventRabbitAspect {

	protected Logger logger = Logger.getLogger("aop");

	@Autowired
	private volatile AmqpTemplate amqpTemplate;
	
	public static final String RABBIT_EXCHANGE = "eventExchange";
	public static final String GENERAL_EVENT_ROUTE_KEY = "event.general.*";
	public static final String ERROR_EVENT_ROUTE_KEY = "event.error.*";
	public static final String TRANSACTION_EVENT_ROUTE_KEY = "transaction.general.*";
	
	@Around("execution(* ie.cloud.cit.johnobee.service.EventService.*(..))")
	public Object interceptService(ProceedingJoinPoint pjp) throws Throwable {
		
		try {
			
			logger.debug("Publishing event to RabbitMQ");
			this.amqpTemplate.convertAndSend(RABBIT_EXCHANGE, GENERAL_EVENT_ROUTE_KEY, new Date() + ": " + pjp.toShortString());

			
			/*
			 This is to hit the new transaction queue and will have need to build the detail as per the 
			 use case and data model
			 
			 */
			 
			
			
			this.amqpTemplate.convertAndSend(RABBIT_EXCHANGE, TRANSACTION_EVENT_ROUTE_KEY,  ": This is being triggered by the AOP and hit the TransactionQueue @ " + new Date().getTime());

			/*
			int i = 0;
		    do {
		    	this.amqpTemplate.convertAndSend(RABBIT_EXCHANGE, TRANSACTION_EVENT_ROUTE_KEY,  ": This hit the TransactionQueue @ " + new Date().getTime());
		    	i++;
		    } while (i < 100);
			
			*/
			
			return pjp.proceed();
			
		} catch (Exception e) {
			
			logger.debug("Publishing event to RabbitMQ");
			this.amqpTemplate.convertAndSend(RABBIT_EXCHANGE, ERROR_EVENT_ROUTE_KEY, new Date() + ": " + pjp.getSignature().toLongString() + " - " + e.toString());
			
			return pjp.proceed();
		}
	}
}
