package de.mass.uebung1.commons;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import de.mass.uebung1.controller.actions.get.GetMandelbrot;

/**
 * @author Max
 */
public abstract class HttpServerletControllerBase extends HttpServlet {

    protected Map<String, HttpRequestActionBase> postActions;
    protected Map<String, HttpRequestActionBase> getActions;

    @Override
    public void init(ServletConfig conf) throws ServletException {

        HttpRequestActionBase action = null;
        postActions = new HashMap<String, HttpRequestActionBase>();


        getActions = new HashMap<>();
        action = new GetMandelbrot();
        getActions.put("getmandelbrot", action);

    }

    protected abstract String getOperation(HttpServletRequest req);
}
