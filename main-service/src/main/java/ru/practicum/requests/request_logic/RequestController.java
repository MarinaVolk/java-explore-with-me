package ru.practicum.requests.request_logic;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.request_models.RequestDto;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto add(@PathVariable(value = "userId") Long requesterId,
                              @RequestParam(value = "eventId") Long eventId) {

        return requestService.add(requesterId, eventId);
    }

    @GetMapping
    public List<RequestDto> getByRequesterId(@PathVariable(value = "userId") Long requesterId) {

        return requestService.getByRequesterId(requesterId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelPartRequest(@PathVariable(value = "userId") Long requesterId,
                                            @PathVariable(value = "requestId") Long requestId) {

        return requestService.cancelPartRequest(requesterId, requestId);
    }
}
