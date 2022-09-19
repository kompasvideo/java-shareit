package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Slf4j
@Service
public class BookingClient extends BaseClient {

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/bookings"))
                        .build()
        );
    }

    public Object save(long userId, BookingDto bookingDto) {
        return post("", userId, bookingDto);
    }

    public Object update(long bookingId, long userId, Boolean isApproved) {
        return patch("/" + bookingId + "?approved=" + isApproved, userId);
    }

    public Object get(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    public Object getBookings(String path, long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get(path + "?state={state}&from={from}&size={size}", userId, parameters);
    }


}
