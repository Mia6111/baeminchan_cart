package codesquad.security;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class EventSessionListener implements HttpSessionListener {

    private HttpSession session = null;


    public void sessionCreated(HttpSessionEvent event)
    {
        // no need to do anything here as connection may not have been established yet
        session  = event.getSession();
        log.info("Session created for id " + session.getId());
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        session = event.getSession();
        log.info("Session destroyed for id " + session.getId());

    }



}