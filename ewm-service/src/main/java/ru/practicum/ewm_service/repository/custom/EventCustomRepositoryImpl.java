package ru.practicum.ewm_service.repository.custom;

import lombok.AllArgsConstructor;
import ru.practicum.ewm_service.entity.Category;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.constant.EventState;
import ru.practicum.ewm_service.entity.constant.SortState;
import ru.practicum.ewm_service.entity.model.event.request.PublicEventSearchRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@AllArgsConstructor
public class EventCustomRepositoryImpl implements EventCustomRepository {

    private final EntityManager entityManager;

    @Override
    public List<Event> eventPublicSearch(PublicEventSearchRequest eventSearchRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> eventQuery = cb.createQuery(Event.class);
        Root<Event> event = eventQuery.from(Event.class);
        List<Predicate> predicatesOr = new ArrayList<>();
        List<Predicate> predicatesAnd = new ArrayList<>();

        if (!eventSearchRequest.getText().isEmpty()) {
            predicatesOr.add(cb.like(cb.lower(event.get("annotation")), "%" + eventSearchRequest.getText().toLowerCase() + "%"));
            predicatesOr.add(cb.like(cb.lower(event.get("description")), "%" + eventSearchRequest.getText().toLowerCase() + "%"));
        }
        predicatesAnd.add(datePredicate(cb, event, eventSearchRequest.getRangeStart(), eventSearchRequest.getRangeEnd()));
        predicatesAnd.add(cb.equal(event.get("state"), EventState.PUBLISHED));
        Optional.ofNullable(eventSearchRequest.getCategories()).ifPresent(c ->
                predicatesAnd.add(cb.equal(event.get("category"), c.stream()
                        .map(i -> Category.builder().id(i).build()).collect(Collectors.toList()))));
        predicatesAnd.add(cb.equal(event.get("paid"), eventSearchRequest.isPaid()));
        if (eventSearchRequest.isOnlyAvailable()) {
            predicatesAnd.add(cb.lt(event.get("confirmedRequests"), event.get("participantLimit")));
        }
        eventQuery.orderBy(sortRequest(cb, event, eventSearchRequest.getSort()));
        eventQuery.select(event).where(
                cb.and(predicatesAnd.toArray(new Predicate[]{})),
                cb.or(predicatesOr.toArray(new Predicate[]{}))
        );
        return pagination(eventQuery, eventSearchRequest.getFromPage(), eventSearchRequest.getSizePage()).getResultList();
    }

    private Predicate datePredicate(CriteriaBuilder cb, Root<Event> event, Timestamp start, Timestamp end) {
        if (start == null || end == null) {
            return cb.greaterThanOrEqualTo(
                    event.get("eventDate"), Timestamp.from(Instant.now()));
        } else {
            return cb.between(
                    event.get("eventDate"), start, end);
        }
    }

    private Order sortRequest(CriteriaBuilder cb, Root<Event> event, SortState sortState) {
        SortState state = SortState.UNSUPPORTED_STATUS;
        if (sortState != null && Arrays.stream(SortState.values()).anyMatch(ss -> ss.name().equals(sortState.name()))) {
            state = SortState.valueOf(sortState.name());
        }
        switch (state) {
            case VIEWS:
                return cb.asc(event.get("views"));
            case EVENT_DATE:
                return cb.asc(event.get("eventDate"));
            default:
                return cb.asc(event.get("id"));
        }
    }

    private TypedQuery<Event> pagination(CriteriaQuery<Event> criteriaQuery, int from, int size) {
        TypedQuery<Event> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(from);
        typedQuery.setMaxResults(size);
        return typedQuery;
    }
}