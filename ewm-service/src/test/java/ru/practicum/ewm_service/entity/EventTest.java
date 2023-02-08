package ru.practicum.ewm_service.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.ewm_service.entity.constant.EventState;
import ru.practicum.ewm_service.entity.constant.SortState;
import ru.practicum.ewm_service.entity.model.event.request.*;
import ru.practicum.ewm_service.entity.model.event.response.EventFullDto;
import ru.practicum.ewm_service.entity.model.event.response.EventShortDto;
import ru.practicum.ewm_service.entity.model.user.UserShortDto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class EventTest {

    @Autowired
    private JacksonTester<Event> json;
    @Autowired
    private JacksonTester<EventNewDto> jsonNewDto;
    @Autowired
    private JacksonTester<EventShortDto> jsonShortDto;
    @Autowired
    private JacksonTester<EventFullDto> jsonFullDto;
    @Autowired
    private JacksonTester<AdminEventSearchRequest> adminEventSearchRequestJacksonTester;
    @Autowired
    private JacksonTester<AdminUpdateEventRequest> adminUpdateEventRequestJacksonTester;
    @Autowired
    private JacksonTester<EventUpdateRequest> eventUpdateRequestJacksonTester;
    @Autowired
    private JacksonTester<PublicEventSearchRequest> publicEventSearchRequestJacksonTester;


    @Test
    @DisplayName("Проверяем правильность сериализации Event.")
    void whenCreateEventAndSerializableHim() throws Exception {

        final Event event = Event.builder()
                .id(1L)
                .annotation("test")
                .category(Category.builder().build())
                .confirmedRequests(1)
                .createdOn(Timestamp.from(Instant.now()))
                .description("test")
                .eventDate(Timestamp.from(Instant.now()))
                .initiator(User.builder().build())
                .location(Location.builder().build())
                .paid(true)
                .participantLimit(1)
                .publishedOn(Timestamp.from(Instant.now()))
                .requestModeration(true)
                .state(EventState.PENDING)
                .title("test")
                .views(1)
                .build();

        JsonContent<Event> result = json.write(event);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.annotation").isEqualTo("test");
        assertThat(result).extractingJsonPathValue("$.category").isNotNull();
        assertThat(result).extractingJsonPathNumberValue("$.confirmedRequests").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.createdOn").isNotBlank();
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.eventDate").isNotNull();
        assertThat(result).extractingJsonPathValue("$.initiator").isNotNull();
        assertThat(result).extractingJsonPathValue("$.location").isNotNull();
        assertThat(result).extractingJsonPathBooleanValue("$.paid").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.participantLimit").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.publishedOn").isNotBlank();
        assertThat(result).extractingJsonPathBooleanValue("$.requestModeration").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.state").isEqualTo(EventState.PENDING.toString());
        assertThat(result).extractingJsonPathStringValue("$.title").isEqualTo("test");
        assertThat(result).extractingJsonPathNumberValue("$.views").isEqualTo(1);
    }

    @Test
    @DisplayName("Проверяем правильность сериализации EventNewDto.")
    void whenCreateEventNewDtoAndSerializableHim() throws Exception {

        final EventNewDto eventNewDto = EventNewDto.builder()
                .annotation("test")
                .category(1L)
                .description("test")
                .eventDate(Timestamp.from(Instant.now()))
                .location(Location.builder().build())
                .paid(true)
                .participantLimit(1)
                .requestModeration(true)
                .title("test")
                .build();

        JsonContent<EventNewDto> result = jsonNewDto.write(eventNewDto);

        //test
        assertThat(result).extractingJsonPathStringValue("$.annotation").isEqualTo("test");
        assertThat(result).extractingJsonPathNumberValue("$.category").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.eventDate").isNotNull();
        assertThat(result).extractingJsonPathValue("$.location").isNotNull();
        assertThat(result).extractingJsonPathBooleanValue("$.paid").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.participantLimit").isEqualTo(1);
        assertThat(result).extractingJsonPathBooleanValue("$.requestModeration").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.title").isEqualTo("test");
    }

    @Test
    @DisplayName("Проверяем правильность сериализации EventShortDto.")
    void whenCreateEventShortDtoAndSerializableHim() throws Exception {

        final EventShortDto eventShortDto = EventShortDto.builder()
                .id(1L)
                .annotation("test")
                .category(Category.builder().build())
                .confirmedRequests(1)
                .eventDate(Timestamp.from(Instant.now()))
                .initiator(UserShortDto.builder().build())
                .paid(true)
                .title("test")
                .views(1)
                .build();

        JsonContent<EventShortDto> result = jsonShortDto.write(eventShortDto);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.annotation").isEqualTo("test");
        assertThat(result).extractingJsonPathValue("$.category").isNotNull();
        assertThat(result).extractingJsonPathNumberValue("$.confirmedRequests").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.eventDate").isNotNull();
        assertThat(result).extractingJsonPathValue("$.initiator").isNotNull();
        assertThat(result).extractingJsonPathBooleanValue("$.paid").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.title").isEqualTo("test");
        assertThat(result).extractingJsonPathNumberValue("$.views").isEqualTo(1);
    }

    @Test
    @DisplayName("Проверяем правильность сериализации EventFullDto.")
    void whenCreateEventFullDtoAndSerializableHim() throws Exception {

        final EventFullDto eventFullDto = EventFullDto.builder()
                .id(1L)
                .annotation("test")
                .category(Category.builder().build())
                .confirmedRequests(1)
                .createdOn(Timestamp.from(Instant.now()))
                .description("test")
                .eventDate(Timestamp.from(Instant.now()))
                .initiator(UserShortDto.builder().build())
                .location(Location.builder().build())
                .paid(true)
                .participantLimit(1)
                .publishedOn(Timestamp.from(Instant.now()))
                .requestModeration(true)
                .state(EventState.PENDING)
                .title("test")
                .views(1)
                .build();

        JsonContent<EventFullDto> result = jsonFullDto.write(eventFullDto);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.annotation").isEqualTo("test");
        assertThat(result).extractingJsonPathValue("$.category").isNotNull();
        assertThat(result).extractingJsonPathNumberValue("$.confirmedRequests").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.createdOn").isNotBlank();
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.eventDate").isNotNull();
        assertThat(result).extractingJsonPathValue("$.initiator").isNotNull();
        assertThat(result).extractingJsonPathValue("$.location").isNotNull();
        assertThat(result).extractingJsonPathBooleanValue("$.paid").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.participantLimit").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.publishedOn").isNotBlank();
        assertThat(result).extractingJsonPathBooleanValue("$.requestModeration").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.state").isEqualTo(EventState.PENDING.toString());
        assertThat(result).extractingJsonPathStringValue("$.title").isEqualTo("test");
        assertThat(result).extractingJsonPathNumberValue("$.views").isEqualTo(1);
    }

    @Test
    @DisplayName("Проверяем правильность сериализации AdminEventSearchRequest.")
    void whenCreateAdminEventSearchRequestAndSerializableHim() throws Exception {

        final AdminEventSearchRequest adminEventSearchRequest = AdminEventSearchRequest.builder()
                .fromPage(1)
                .sizePage(1)
                .users(Set.of(1L))
                .eventState(EventState.PENDING)
                .categories(Set.of(1L))
                .rangeStart(Timestamp.from(Instant.now()))
                .rangeEnd(Timestamp.from(Instant.now()))
                .build();

        JsonContent<AdminEventSearchRequest> result = adminEventSearchRequestJacksonTester.write(adminEventSearchRequest);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.fromPage").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.sizePage").isEqualTo(1);
        assertThat(result).extractingJsonPathArrayValue("$.users").size().isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.eventState").isEqualTo(EventState.PENDING.toString());
        assertThat(result).extractingJsonPathArrayValue("$.categories").size().isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.rangeStart").isNotNull();
        assertThat(result).extractingJsonPathStringValue("$.rangeEnd").isNotNull();
    }

    @Test
    @DisplayName("Проверяем правильность сериализации AdminUpdateEventRequest.")
    void whenCreateAdminUpdateEventRequestAndSerializableHim() throws Exception {

        final AdminUpdateEventRequest adminEventSearchRequest = AdminUpdateEventRequest.builder()
                .annotation("test")
                .category(1L)
                .description("test")
                .eventDate(Timestamp.from(Instant.now()))
                .location(Location.builder().build())
                .paid(true)
                .participantLimit(1)
                .requestModeration(true)
                .title("test")
                .build();

        JsonContent<AdminUpdateEventRequest> result = adminUpdateEventRequestJacksonTester.write(adminEventSearchRequest);

        //test
        assertThat(result).extractingJsonPathStringValue("$.annotation").isEqualTo("test");
        assertThat(result).extractingJsonPathValue("$.category").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.eventDate").isNotNull();
        assertThat(result).extractingJsonPathValue("$.location").isNotNull();
        assertThat(result).extractingJsonPathBooleanValue("$.paid").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.participantLimit").isEqualTo(1);
        assertThat(result).extractingJsonPathBooleanValue("$.requestModeration").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.title").isEqualTo("test");
    }

    @Test
    @DisplayName("Проверяем правильность сериализации EventUpdateRequest.")
    void whenCreateEventUpdateRequestAndSerializableHim() throws Exception {

        final EventUpdateRequest eventUpdateRequest = EventUpdateRequest.builder()
                .eventId(1L)
                .annotation("test")
                .category(1L)
                .description("test")
                .eventDate(Timestamp.from(Instant.now()))
                .paid(true)
                .participantLimit(1)
                .title("test")
                .build();

        JsonContent<EventUpdateRequest> result = eventUpdateRequestJacksonTester.write(eventUpdateRequest);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.eventId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.annotation").isEqualTo("test");
        assertThat(result).extractingJsonPathValue("$.category").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.eventDate").isNotNull();
        assertThat(result).extractingJsonPathBooleanValue("$.paid").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.participantLimit").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.title").isEqualTo("test");
    }

    @Test
    @DisplayName("Проверяем правильность сериализации PublicEventSearchRequest.")
    void whenCreatePublicEventSearchRequestAndSerializableHim() throws Exception {

        final PublicEventSearchRequest publicEventSearchRequest = PublicEventSearchRequest.builder()
                .fromPage(1)
                .sizePage(1)
                .text("test")
                .categories(Set.of(1L))
                .paid(true)
                .onlyAvailable(true)
                .sort(SortState.UNSUPPORTED_STATUS)
                .rangeStart(Timestamp.from(Instant.now()))
                .rangeEnd(Timestamp.from(Instant.now()))
                .build();

        JsonContent<PublicEventSearchRequest> result = publicEventSearchRequestJacksonTester.write(publicEventSearchRequest);

        //test
        assertThat(result).extractingJsonPathNumberValue("$.fromPage").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.sizePage").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("test");
        assertThat(result).extractingJsonPathArrayValue("$.categories").size().isEqualTo(1);
        assertThat(result).extractingJsonPathBooleanValue("$.paid").isTrue();
        assertThat(result).extractingJsonPathBooleanValue("$.onlyAvailable").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.sort").isEqualTo(SortState.UNSUPPORTED_STATUS.toString());
        assertThat(result).extractingJsonPathStringValue("$.rangeStart").isNotNull();
        assertThat(result).extractingJsonPathStringValue("$.rangeEnd").isNotNull();
    }
}
