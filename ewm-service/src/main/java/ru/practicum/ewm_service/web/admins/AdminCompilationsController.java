package ru.practicum.ewm_service.web.admins;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_service.entity.Compilation;
import ru.practicum.ewm_service.entity.mapper.CompilationMapper;
import ru.practicum.ewm_service.entity.model.compilations.request.CompilationRequestDto;
import ru.practicum.ewm_service.entity.model.compilations.response.CompilationResponseDto;
import ru.practicum.ewm_service.entity.model.user.UserDto;
import ru.practicum.ewm_service.service.admin.AdminCompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {

    private final AdminCompilationService adminCompilationService;
    private final CompilationMapper compilationMapper;

    @Autowired
    public AdminCompilationsController(AdminCompilationService adminCompilationService, CompilationMapper compilationMapper) {
        this.adminCompilationService = adminCompilationService;
        this.compilationMapper = compilationMapper;
    }

    @PostMapping()
    public ResponseEntity<CompilationResponseDto> createCompilation(
            @Valid @RequestBody final CompilationRequestDto dto
    ) {
        Compilation compilation = adminCompilationService.createCompilation(compilationMapper.toCompilation(dto));
        return new ResponseEntity<>(compilationMapper.toCompilationResponseDto(compilation), HttpStatus.OK);
    }

    @DeleteMapping("{compId}")
    public ResponseEntity<UserDto> deleteCompilation(
            @PathVariable("compId") final long compId
    ) {
        adminCompilationService.deleteCompilation(compId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{compId}/events/{eventId}")
    public ResponseEntity deleteEventCompilation(
            @PathVariable("compId") final long compId,
            @PathVariable("eventId") final long eventId
    ) {
        adminCompilationService.deleteEventCompilation(compId, eventId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{compId}/events/{eventId}")
    public ResponseEntity editEventCompilation(
            @PathVariable("compId") final long compId,
            @PathVariable("eventId") final long eventId
    ) {
        adminCompilationService.editEventCompilation(compId, eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{compId}/pin")
    public ResponseEntity deletePinCompilation(
            @PathVariable("compId") final long compId
    ) {
        adminCompilationService.deletePinCompilation(compId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{compId}/pin")
    public ResponseEntity editPinCompilation(
            @PathVariable("compId") final long compId
    ) {
        adminCompilationService.editPinCompilation(compId);
        return ResponseEntity.ok().build();
    }
}
