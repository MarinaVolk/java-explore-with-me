package ru.practicum.compilations;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

/**
 * File Name: PublicCompilationController.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   5:33 PM (UTC+3)
 * Description:
 */

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class PublicCompilationController {
    private final PublicCompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getByParameters(@RequestParam(value = "pinned", required = false) Boolean pinned,
                                                @RequestParam(value = "from", defaultValue = "0") int from,
                                                @RequestParam(value = "size", defaultValue = "10") int size) {

        return compilationService.getByParameters(pinned, from, size);
    }

    @GetMapping("/{id}")
    public CompilationDto getById(@PathVariable(value = "id") Long compId) {

        return compilationService.getById(compId);
    }
}
