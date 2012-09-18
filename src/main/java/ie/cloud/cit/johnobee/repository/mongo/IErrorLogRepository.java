package ie.cloud.cit.johnobee.repository.mongo;

import ie.cloud.cit.johnobee.domain.ErrorLog;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author krams at {@link http://krams915@blogspot.com}
 */
public interface IErrorLogRepository extends MongoRepository<ErrorLog, String> {
}
