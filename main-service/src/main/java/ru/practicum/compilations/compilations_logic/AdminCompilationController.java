package ru.practicum.compilations.compilations_logic;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.compilations_models.CompilationDto;
import ru.practicum.compilations.compilations_models.NewCompilationDto;
import ru.practicum.compilations.compilations_models.UpdateCompilationRequests;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final AdminCompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto add(@RequestBody @Valid NewCompilationDto newCompilationDto) {

        return compilationService.add(newCompilationDto);
    }

    @PatchMapping("/{id}")
    public CompilationDto update(@PathVariable(value = "id") Long compId,
                                 @RequestBody @Valid UpdateCompilationRequests updateRequest) {

        return compilationService.update(compId, updateRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") Long compId) {

        compilationService.delete(compId);
    }
}
