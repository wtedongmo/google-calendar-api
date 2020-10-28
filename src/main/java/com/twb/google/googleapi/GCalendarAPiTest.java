package com.twb.google.googleapi;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

public class GCalendarAPiTest {

    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    //    private static final String APPLICATION_NAME = "blaxand-anglo";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/blaxand-anglo-7101de473f73.json";

    public static void main(String[] args) {

        try {

            TimeZone timeZoneDef = TimeZone.getDefault();
            TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
            System.out.println("\nTime zone default: "+timeZone.getID());
            System.out.println("\nTime zone default name : "+timeZone.getDisplayName());
            System.out.println("\nTime zone default 2 : "+timeZone.getDSTSavings());
            System.out.println("\nTime zone default 3 : "+((timeZone.getRawOffset()/3600000) + (timeZoneDef.getRawOffset()/3600000)));
            // Load client secrets.
//            InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
//            if (in == null) {
//                throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
//            }
//            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

//            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//
//            Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY,
//                    new BasicAuthentication("wilfried.tedongmo@nanobnk.com", "WTedongm00"))
//                    .setApplicationName(APPLICATION_NAME)
//                    .build();
//
//            DateTime now = new DateTime(System.currentTimeMillis());
//            Events events = service.events().list("primary")
//                    .setMaxResults(10)
//                    .setTimeMin(now)
//                    .setOrderBy("startTime")
//                    .setSingleEvents(true)
//                    .setKey("AIzaSyD_lPGLmrLIuqLPFASQg0bLB2HIyFtAMXc")
//                    .execute();
//            List<Event> items = events.getItems();
//            if (items.isEmpty()) {
//                System.out.println("No upcoming events found.");
//            } else {
//                System.out.println("Upcoming events");
//                for (Event event : items) {
//                    DateTime start = event.getStart().getDateTime();
//                    if (start == null) {
//                        start = event.getStart().getDate();
//                    }
//                    System.out.printf("%s (%s)\n", event.getSummary(), start);
//                }
//            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
