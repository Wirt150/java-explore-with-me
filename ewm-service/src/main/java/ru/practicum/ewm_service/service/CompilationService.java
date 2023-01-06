package ru.practicum.ewm_service.service;

import ru.practicum.ewm_service.entity.Compilation;

import java.util.List;

public interface CompilationService {

    List<Compilation> getCompilations(boolean pinned, int from, int size);

    Compilation getCompilation(long compId);
}
