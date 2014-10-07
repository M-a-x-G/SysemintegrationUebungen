
package de.mass.uebung1.helper;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * H�llt den SessionUser konsistent
 * @author Max Gregor
 */
public class SessionListener implements HttpSessionListener{

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http
     * .HttpSessionEvent)
     */
    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet
     * .http.HttpSessionEvent)
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
//	HttpSession session = hse.getSession();
//	SessionUser sessionUser = (SessionUser) session
//		.getAttribute("sessionUser");
//	PlayerBO spielerBO = (PlayerBO) session.getAttribute("spielerBO");
//	
//	boolean sessionTimedOut = ((getTime() - session.getLastAccessedTime()) / 60) >= session
//		.getMaxInactiveInterval();
//	if (sessionTimedOut && sessionUser != null && spielerBO != null) {
//	    
//	    sessionUser = spielerBO.getSessionUser(sessionUser.getPlayerID());
//	   
//	    if (session.getAttribute("game") != null) {
//		sessionUser.setJsessionGame(null);
//	    } else {
//		sessionUser.setJsessionHP(null);
//	    }
//	    sessionUser.setLastUpdate();
//	    spielerBO.updateSessionUser(sessionUser.getPlayerID(), sessionUser);
//	}
    }

    protected long getTime() {
	return System.currentTimeMillis();
    }
}
