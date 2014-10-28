/**
 *
 */
package de.fhb.uebung1.controller.actions.get;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.fhb.uebung1.commons.HttpRequestActionBase;
import de.fhb.uebung1.helper.AWSCredentialsHelper;


/**
 * @author Max Gregor
 */
public class GetStoreMandelbrot extends HttpRequestActionBase {

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
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                ImageIO.write(image, "png", baos);
//                baos.flush();
//                resp.setContentType("image/jpeg");
//                resp.getOutputStream().write(baos.toByteArray());
//                forward(req, resp, "protected/mandelbrot.jsp");

                String imageName = "image-" + UUID.randomUUID() + ".png";
                File imageFile = new File(imageName);
                ImageIO.write(image, "png", imageFile);
                AWSCredentials credentials = AWSCredentialsHelper.getAWSCredentials();

                String bucketName = req.getParameter("bucketname");
                if (bucketName == null || bucketName.isEmpty()) {
                    resp.getOutputStream().print("No Bucket specified (parameter bucketname)");
                } else {

                    AmazonS3 s3 = new AmazonS3Client(credentials);
                    Region region = Region.getRegion(Regions.EU_WEST_1);
                    s3.setRegion(region);
                    try {
                        s3.createBucket(bucketName);
                    } catch (AmazonS3Exception e) {
                        System.out.println("Error on creating bucket (already exist?)");
                    }
                    s3.putObject(new PutObjectRequest(bucketName, imageName, imageFile));

                    GeneratePresignedUrlRequest generatePresignedUrlRequest =
                            new GeneratePresignedUrlRequest(bucketName, imageName);
                    generatePresignedUrlRequest.setMethod(HttpMethod.GET);
                    URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
                    resp.sendRedirect(url.toString());
                }
            } else {
                resp.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
        }

    }
}
