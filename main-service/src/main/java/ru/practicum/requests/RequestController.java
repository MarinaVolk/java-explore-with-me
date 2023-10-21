package ru.practicum.requests;/* # parse("File Header.java")*/

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

/**
 * File Name: RequestController.java
 * Author: Marina Volkova
 * Date: 2023-10-20,   7:41 PM (UTC+3)
 * Description:
 */

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
