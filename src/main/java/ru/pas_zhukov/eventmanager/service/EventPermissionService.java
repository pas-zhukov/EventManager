package ru.pas_zhukov.eventmanager.service;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.pas_zhukov.eventmanager.model.Event;
import ru.pas_zhukov.eventmanager.model.User;
import ru.pas_zhukov.eventmanager.model.UserRole;
import ru.pas_zhukov.eventmanager.security.AuthenticationService;

@Service
public class EventPermissionService {

    private final EventService eventService;
    private final AuthenticationService authenticationService;

    public EventPermissionService(EventService eventService, AuthenticationService authenticationService) {
        this.eventService = eventService;
        this.authenticationService = authenticationService;
    }

    public void throwOnCurrentlyAuthenticatedUserCantModifyEvent(Long eventId) {
        Event event = eventService.getEventByIdOrThrow(eventId);
        User user = authenticationService.getCurrentAuthenticatedUserOrThrow();
                if (user.getRole() != UserRole.ADMIN || !event.getOwner().equals(user)) {
            throw new AccessDeniedException("Only admins or owners can delete events");
        }
    }
}
