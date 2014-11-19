package de.fhb.uebung1.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.fhb.uebung1.commons.HttpRequestActionBase;
import de.fhb.uebung1.commons.HttpServerletControllerBase;


/**
 * Servlet implementation
 */
@WebServlet("/")
public class ServletController extends HttpServerletControllerBase {

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletController() {
        super();
    }


    protected HttpSession sessionHandling(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return session;

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String operation = getOperation(request);
        sessionHandling(request);
//	System.out.println("Angefragt(get): "+ operation);
        boolean containsKey = getActions.containsKey(operation);
        HttpRequestActionBase action;
        if (operation != null && containsKey) {
            action = getActions.get(operation);
            action.perform(request, response);
        } else if (operation != null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
//            action = getActions.get("getmandelbrot");
//            action.perform(request, response);
            response.getOutputStream().print("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head lang=\"en\">\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>MandelbrotQueue</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<form name=\"myWebForm\" action=\"?action=sendtoqueue\" method=\"post\">\n" +
                    "    Email <input type=\"email\" name=\"email\" placeholder=\"email\"/><br/>\n" +
                    "    Breite <input type=\"text\" name=\"width\" placeholder=\"Breite\"/> <br/>\n" +
                    "    H&ouml;he <input type=\"text\" name=\"height\" placeholder=\"H&ouml;he\"/> <br/>\n" +
                    "    Iterationen <input type=\"text\" name=\"iteration\" placeholder=\"Iterationen\"/><br/>\n" +
                    "    <input type=\"submit\" value=\"SUBMIT\"/>\n" +
                    "</form>\n" +
                    "</body>\n" +
                    "</html>");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        String operation = getOperation(request);
        sessionHandling(request);
        boolean containsKey = postActions.containsKey(operation);
        HttpRequestActionBase action;
        postActions.get("sendtoqueue").perform(request,response);
//        if (operation != null && containsKey) {
//            action = postActions.get(operation);
//            action.perform(request, response);
//
//        }
//        else if (operation != null && operation.equalsIgnoreCase("login")) {
//            postActions.get(operation).perform(request, response);
//        } else if (operation != null && !containsKey) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//        } else {
//            response.sendError(HttpServletResponse.SC_FORBIDDEN);
//        }
    }

    @Override
    protected String getOperation(HttpServletRequest req) {
        String action = req.getParameter("action");
        if (action != null) {
            action = action.toLowerCase();
        }
        return action;
    }

}
