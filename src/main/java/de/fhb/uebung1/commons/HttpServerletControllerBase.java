package de.fhb.uebung1.commons;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import de.fhb.uebung1.controller.actions.get.ActivateQueue;
import de.fhb.uebung1.controller.actions.get.DeleteMandelbrot;
import de.fhb.uebung1.controller.actions.get.GetMandelbrot;
import de.fhb.uebung1.controller.actions.get.GetStoreMandelbrot;
import de.fhb.uebung1.controller.actions.post.SendToQueue;


/**
 * @author Max
 */
public abstract class HttpServerletControllerBase extends HttpServlet {

    protected Map<String, HttpRequestActionBase> postActions;
    protected Map<String, HttpRequestActionBase> getActions;

    @Override
    public void init(ServletConfig conf) throws ServletException {

        HttpRequestActionBase action;
        postActions = new HashMap<>();

        postActions.put("sendtoqueue", new SendToQueue());

        getActions = new HashMap<>();
        getActions.put("pollqueue", new ActivateQueue());
        action = new GetMandelbrot();
        getActions.put("getmandelbrot", action);
        action = new GetStoreMandelbrot();
        getActions.put("generatetos3", action);
        action = new DeleteMandelbrot();
        getActions.put("deletefroms3", action);

    }

    protected abstract String getOperation(HttpServletRequest req);
}
