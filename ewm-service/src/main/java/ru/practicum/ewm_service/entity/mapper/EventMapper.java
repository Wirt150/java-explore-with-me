package ru.practicum.ewm_service.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.ewm_service.entity.Category;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.model.event.request.EventNewDto;
import ru.practicum.ewm_service.entity.model.event.response.EventFullDto;
import ru.practicum.ewm_service.entity.model.event.response.EventShortDto;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface EventMapper {
    @Mapping(target = "category", source = "category", qualifiedByName = "mapCategoryByEvent")
    Event toEvent(EventNewDto eventNewDto);

    EventFullDto toEventFullDto(Event event);

    List<EventShortDto> toEventShortDtos(List<Event> events);

    List<EventFullDto> toEventFullDtos(List<Event> events);

    @Named("mapCategoryByEvent")
    default Category mapCategoryByEvent(Long categoryId) {
        return Category.builder().id(categoryId).build();
    }

}
