package ru.practicum.ewm_service.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.Request;
import ru.practicum.ewm_service.entity.User;
import ru.practicum.ewm_service.entity.model.request.RequestDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mappings({
            @Mapping(target = "event", source = "event", qualifiedByName = "mapEvent"),
            @Mapping(target = "requester", source = "requester", qualifiedByName = "mapUser")
    })
    RequestDto toRequestDto(Request request);

    List<RequestDto> toRequestDtos(List<Request> requests);

    @Named("mapEvent")
    default Long mapEvent(Event event) {
        return event.getId();
    }

    @Named("mapUser")
    default Long mapUser(User user) {
        return user.getId();
    }
}
