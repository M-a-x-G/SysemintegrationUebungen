/**
 *
 */
package de.fhb.uebung1.controller.actions.get;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.fhb.uebung1.commons.HttpRequestActionBase;


/**
 * @author Max Gregor
 */
public class GetMandelbrot extends HttpRequestActionBase {

    @Override
    public void perform(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            int width = 800, height = 600, iterations = 400;
            String paramW = req.getParameter("w"), paramH = req.getParameter("h"), paramIt = req.getParameter("it");
            if (paramH != null && paramIt != null && paramW != null) {
                width = Integer.parseInt(paramW);
                height = Integer.parseInt(paramH);
                iterations = Integer.parseInt(paramIt);
            }

            if (width > 50 && width <= 1920 && height > 50 && iterations > 0 && iterations <= 1000) {
                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                int black = 0;
                int[] colors = new int[iterations];
                for (int i = 0; i < iterations; i++) {
                    colors[i] = Color.HSBtoRGB(i / 256f, 1, i / (i + 8f));
                }
                for (int row = 0; row < height; row++) {
                    for (int col = 0; col < width; col++) {
                        double c_re = (col - width / 2) * 4.0 / width;
                        double c_im = (row - height / 2) * 4.0 / width;
                        double x = 0, y = 0;
                        double r2;
                        int iteration = 0;
                        while (x * x + y * y < 4 && iteration < iterations) {
                            double x_new = x * x - y * y + c_re;
                            y = 2 * x * y + c_im;
                            x = x_new;
                            iteration++;
                        }
                        if (iteration < iterations) image.setRGB(col, row, colors[iteration]);
                        else image.setRGB(col, row, black);
                    }
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);
                baos.flush();
                resp.setContentType("image/jpeg");
                resp.getOutputStream().write(baos.toByteArray());
//                forward(req, resp, "protected/mandelbrot.jsp");
            } else {
                resp.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
        }

    }
}
