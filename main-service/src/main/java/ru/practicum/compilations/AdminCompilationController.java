package ru.practicum.compilations;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;


/**
 * File Name: AdminCompilationController.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   5:32 PM (UTC+3)
 * Description:
 */

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
