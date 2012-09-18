package ie.cloud.cit.johnobee.repository.jpa;

import ie.cloud.cit.johnobee.domain.Event;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * A repository for {@link Event}
 * 
 * @author krams at {@link http://krams915@blogspot.com}
 */
public interface IEventRepository extends JpaRepository<Event, Long> {
}
