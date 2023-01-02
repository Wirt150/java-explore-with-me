package ru.practicum.ewm_service.repository.custom;

import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.model.event.request.PublicEventSearchRequest;

import java.util.List;

public interface EventCustomRepository {

    List<Event> eventPublicSearch(PublicEventSearchRequest publicEventSearchRequest);

}
