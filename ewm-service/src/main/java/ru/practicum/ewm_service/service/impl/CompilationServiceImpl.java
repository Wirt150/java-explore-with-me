package ru.practicum.ewm_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_service.entity.Compilation;
import ru.practicum.ewm_service.entity.Event;
import ru.practicum.ewm_service.error.compilation.CompilationNotFoundException;
import ru.practicum.ewm_service.repository.CompilationsRepository;
import ru.practicum.ewm_service.service.CompilationService;
import ru.practicum.ewm_service.service.EventService;
import ru.practicum.ewm_service.service.admin.AdminCompilationService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService, AdminCompilationService {

    private final CompilationsRepository compilationsRepository;
    private final EventService eventService;

    @Override
    public Compilation createCompilation(final Compilation compilation) {
        compilation.setEvents(eventService.findEvents(compilation.getEvents().stream()
                .map(Event::getId)
                .collect(Collectors.toList())));
        return compilationsRepository.save(compilation);
    }

    @Override
    public void deleteCompilation(final long compilationId) {
        compilationsRepository.deleteById(compilationId);
    }

    @Override
    public void deleteEventCompilation(final long compId, final long eventId) {
        compilationsRepository.deleteEventsCompilations(compId, eventId);
    }

    @Override
    public void editEventCompilation(final long compId, final long eventId) {
        compilationsRepository.editEventsCompilations(compId, eventId);
    }

    @Override
    public void deletePinCompilation(long compId) {
        compilationsRepository.deletePinCompilations(compId);
    }

    @Override
    public void editPinCompilation(long compId) {
        compilationsRepository.editPinCompilations(compId);
    }

    @Override
    public List<Compilation> getCompilations(boolean pinned, int from, int size) {
        return compilationsRepository.findAllByPinned(pinned, PageRequest.of(from, size));
    }

    @Override
    public Compilation getCompilation(long compId) {
        return compilationsRepository.findById(compId).orElseThrow(() -> new CompilationNotFoundException(compId));
    }
}
