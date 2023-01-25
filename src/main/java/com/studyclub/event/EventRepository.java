package com.studyclub.event;

import com.studyclub.domain.Event;
import com.studyclub.domain.Study;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface EventRepository  extends JpaRepository<Event, Long> {
//    List<Event> findByStudyOrderByStartDateTime(Study study);
    @EntityGraph(value = "Event.withEnrollments", type = EntityGraph.EntityGraphType.LOAD)
    List<Event> findByStudyOrderByStartDateTime(Study study);
}
