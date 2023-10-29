package ru.practicum.requests.request_models;/* # parse("File Header.java")*/

import java.util.ArrayList;
import java.util.List;

public class RequestMapper {
    public static Request partRequestDtoToModel(RequestDto partRequestDto) {

        Request participationRequest = new Request();

        participationRequest.setCreated(partRequestDto.getCreated());
        participationRequest.setEvent(partRequestDto.getEvent());
        participationRequest.setRequester(partRequestDto.getRequester());
        participationRequest.setStatus(partRequestDto.getStatus().toString());

        return participationRequest;
    }

    public static RequestDto partRequestToDto(Request request) {

        return new RequestDto(
                request.getId(),
                request.getCreated(),
                request.getEvent(),
                request.getRequester(),
                RequestState.valueOf(request.getStatus()));
    }

    public static List<RequestDto> partRequestToDto(List<Request> requests) {

        List<RequestDto> listToReturn = new ArrayList<>();

        for (Request request : requests) {
            listToReturn.add(partRequestToDto(request));
        }
        return listToReturn;
    }
}
