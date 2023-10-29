package ru.practicum.compilations.compilations_logic;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.compilations_models.CompilationDto;

import javax.transaction.Transactional;
import java.util.List;

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
