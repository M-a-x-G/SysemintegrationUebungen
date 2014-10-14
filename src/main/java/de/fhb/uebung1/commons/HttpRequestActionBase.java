/**
 * 
 */
package de.fhb.uebung1.commons;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Max
 * 
 */
public abstract class HttpRequestActionBase {

    public abstract void perform(HttpServletRequest req,
	    HttpServletResponse resp) throws ServletException, IOException;

    protected void forward(HttpServletRequest req, HttpServletResponse resp,
	    String forwardName) throws ServletException, IOException {
		RequestDispatcher reqDis = req.getRequestDispatcher(forwardName);
		reqDis.forward(req, resp);
    }
    
}
