package codesquad.security;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

@Slf4j
public class EventSessionAttributeListener implements HttpSessionAttributeListener {


    public void attributeAdded(HttpSessionBindingEvent sessionBindingEvent) {

        // 세션을 얻습니다.
        HttpSession session = sessionBindingEvent.getSession();

        // 몇몇 정보의 로그를 남깁니다.
        log.debug("attributeAdded");
       log.debug(" Attribute added, session {}, {}, {} ", session,sessionBindingEvent.getName(), sessionBindingEvent.getValue());
    }

    //todo > attributeRemoved, EmptyCart아니고 loginUser없는 Cart 정보 삭제시 history로 이동
    public void attributeRemoved(HttpSessionBindingEvent sessionBindingEvent) {

        // 세션을 얻습니다.
        HttpSession session = sessionBindingEvent.getSession();
        log.debug("attributeRemoved");
        log.debug(" Attribute added, session {}, {}, {} ", session,sessionBindingEvent.getName(), sessionBindingEvent.getValue());    }

    public void attributeReplaced(HttpSessionBindingEvent sessionBindingEvent) {

        // 세션을 얻습니다.
        HttpSession session = sessionBindingEvent.getSession();
        log.debug("attributeReplaced");
        log.debug(" Attribute added, session {}, {}, {} ", session,sessionBindingEvent.getName(), sessionBindingEvent.getValue());  }

}
