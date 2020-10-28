package com.twb.google.googleapi;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class CalendarQuickstart {
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
//    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        Event eventCreated = createEvent(service);
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("Id: %s \tSummary %s \tStart Time:(%s)\n", event.getId(), event.getSummary(), start);
            }
        }

        Event eventUpd  = updateEvent(service, eventCreated.getId());
//        Event eventUpd  = updateEvent(service, items.get(items.size()-1).getId());
        System.out.printf("\nEvent updated Id: %s \tSummary %s \tStart Time:(%s)\n", eventUpd.getId(), eventUpd.getSummary(),
                eventUpd.getStart().getDateTime());
    }

    private static Event createEvent(Calendar service){
        // Refer to the Java quickstart on how to setup the environment:
// https://developers.google.com/calendar/quickstart/java
// Change the scope to CalendarScopes.CALENDAR and delete any stored
// credentials.

        Event event = new Event()
                .setSummary("Test Create event with Google meet link")
                .setLocation("Mauritius - Ebene")
//                .setConferenceData(new ConferenceData().setCreateRequest(new CreateConferenceRequest()))
                .setDescription("Second meeting and A chance to hear more about Google's developer products.");

//        DateTime startDateTime = new DateTime("2015-05-28T09:00:00-07:00");
        TimeZone timeZone = TimeZone.getDefault();
        int addHour = (timeZone.getRawOffset()/3600000);
        String addZoneTime = addHour>=0 ? (addHour<10 ? "+0"+addHour+":00" : "+"+addHour+":00") :
                (addHour<-10 ? "-0"+Math.abs(addHour)+":00" : "-"+Math.abs(addHour)+":00");

        DateTime startDateTime = new DateTime("2020-10-29T09:00:00"+addZoneTime);
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(timeZone.getID());
//                .setTimeZone("America/Los_Angeles");
        event.setStart(start);

        System.out.println("\nTime zone default: "+timeZone.getID());
        DateTime endDateTime = new DateTime("2020-10-29T13:00:00"+addZoneTime);
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone(timeZone.getID());
        event.setEnd(end);

//        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
//        event.setRecurrence(Arrays.asList(recurrence));

        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail("attendee-email1@mail.com"),
                new EventAttendee().setEmail("attendee-email1@mail.com"),
        };
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);


        ConferenceSolutionKey conferenceSKey = new ConferenceSolutionKey();
        conferenceSKey.setType("hangoutsMeet"); // Non-G suite user
        CreateConferenceRequest createConferenceReq = new CreateConferenceRequest();

        String valueGen = RandomStringUtils.randomAlphanumeric(10);
        createConferenceReq.setRequestId(valueGen); // ID generated by you
//        createConferenceReq.setRequestId("3whatisup3545"); // ID generated by you
        createConferenceReq.setConferenceSolutionKey(conferenceSKey);
        ConferenceData conferenceData = new ConferenceData();
        conferenceData.setCreateRequest(createConferenceReq);
        event.setConferenceData(conferenceData);


        try {
            String calendarId = "primary";

            event = service.events().insert(calendarId, event)
                    .setKey("Your-Google-API-Key")
                    .setSendNotifications(true)
                    .setConferenceDataVersion(1)
                    .execute();
            System.out.printf("Event created: Id: %s, \tLink: %s \tGoogle Meet: %s, \tGen value: %s\n",
                    event.getId(), event.getHtmlLink(), event.getHangoutLink(), valueGen);
            System.out.println("\nFull event: "+event.toString());
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return event;
    }

    private static Event updateEvent(Calendar service, String eventId){
        Event event  = null;
        try{
            // Retrieve the event from the API
            event = service.events().get("primary", eventId).execute();

            System.out.println("\nEvent retrieved: "+event.getId());

    // Make a change
            event.setSummary("Test update event2");
            List atts = event.getAttendees();
            atts.add(new EventAttendee().setEmail("attendee-added-email@mail.com"));
            event.setAttendees(atts);

    // Update the event
            event = service.events().update("primary", event.getId(), event)
                    .setKey("Your-Google-API-Key")
                    .setSendNotifications(true)
                    .execute();

            System.out.println("\nEvent updated: "+event.getUpdated());
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return event;
    }
}