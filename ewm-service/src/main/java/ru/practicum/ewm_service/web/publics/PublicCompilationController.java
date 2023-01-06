package ru.practicum.ewm_service.web.publics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.entity.Compilation;
import ru.practicum.ewm_service.entity.mapper.CompilationMapper;
import ru.practicum.ewm_service.entity.model.compilations.response.CompilationResponseDto;
import ru.practicum.ewm_service.service.CompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
public class PublicCompilationController {

    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @Autowired
    public PublicCompilationController(CompilationService compilationService, CompilationMapper compilationMapper) {
        this.compilationService = compilationService;
        this.compilationMapper = compilationMapper;
    }


    @GetMapping("/{compId}")
    public ResponseEntity<CompilationResponseDto> getCompilation(
            @PathVariable(value = "compId") final long compId
    ) {
        Compilation compilation = compilationService.getCompilation(compId);
        return new ResponseEntity<>(compilationMapper.toCompilationResponseDto(compilation), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CompilationResponseDto>> getCompilations(
            @RequestParam(defaultValue = "0") final int from,
            @RequestParam(defaultValue = "10") final int size,
            @RequestParam(defaultValue = "false") final boolean pinned
    ) {
        List<Compilation> compilations = compilationService.getCompilations(pinned, from, size);
        return new ResponseEntity<>(compilationMapper.toCompilationResponseDtos(compilations), HttpStatus.OK);
    }
}
