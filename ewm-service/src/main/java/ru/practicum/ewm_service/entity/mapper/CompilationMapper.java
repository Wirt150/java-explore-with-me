package ru.practicum.ewm_service.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.ewm_service.entity.Compilation;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.entity.model.compilations.request.CompilationRequestDto;
import ru.practicum.ewm_service.entity.model.compilations.response.CompilationResponseDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface CompilationMapper {
    @Mapping(target = "events", source = "events", qualifiedByName = "mapEvent")
    Compilation toCompilation(CompilationRequestDto compilationRequestDto);

    CompilationResponseDto toCompilationResponseDto(Compilation compilation);

    List<CompilationResponseDto> toCompilationResponseDtos(List<Compilation> compilations);

    @Named("mapEvent")
    default List<Event> mapEvent(Set<Long> eventId) {
        return eventId.stream().map(i -> Event.builder().id(i).build()).collect(Collectors.toList());
    }
}
