package ru.practicum.ewm_service.service.admin;

import ru.practicum.ewm_service.entity.Compilation;
import ru.practicum.ewm_service.service.CompilationService;

public interface AdminCompilationService extends CompilationService {
    Compilation createCompilation(Compilation compilation);

    void deleteCompilation(long compilationId);

    void deleteEventCompilation(long compId, long eventId);

    void editEventCompilation(long compId, long eventId);

    void deletePinCompilation(long compId);

    void editPinCompilation(long compId);
}
