package de.fhb.uebung1.helper;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

/**
 * Created by Max on 19.11.14.
 */
public class PollJob implements Job {

    public static Session mailSession = getMailSession("mail.gmx.net", 465, true, CredentialHelper.EMAIL_NAME, CredentialHelper.EMAIL_PW, false);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        Logger.getAnonymousLogger().log(Level.ALL, "Poll Queue");
        String email;
        int height;
        int width;
        int iterations;

        AWSCredentials credentials = CredentialHelper.AWS_CREDENTIALS;
        AmazonSQSClient sqs = new AmazonSQSClient(credentials);

        try {
            sqs.getQueueUrl(CredentialHelper.QUEUE_NAME);
        } catch (QueueDoesNotExistException e) {
            CreateQueueRequest createQueueRequest = new CreateQueueRequest().withQueueName(CredentialHelper.QUEUE_NAME);
            sqs.createQueue(createQueueRequest);
        }
//        ReceiveMessageResult result = sqs.receiveMessage(new ReceiveMessageRequest(sqs.getQueueUrl(AWSHelper.QUEUE_NAME).getQueueUrl()));
        ReceiveMessageRequest request = new ReceiveMessageRequest(sqs.getQueueUrl(CredentialHelper.QUEUE_NAME).getQueueUrl());
        request.setMaxNumberOfMessages(10);
        ReceiveMessageResult result = sqs.receiveMessage(request);
        List<Message> messages = result.getMessages();

        if (messages.isEmpty()) {
//            Logger.getAnonymousLogger().log(Level.ALL, "Queue was empty");
            System.out.println("Queue was empty");
            return;
//            System.out.println("Queue was empty");
        }


        for (com.amazonaws.services.sqs.model.Message message : messages) {
            String[] array = message.getBody().split(",");
            if (array.length == 4) {
                width = Integer.parseInt(array[0]);
                height = Integer.parseInt(array[1]);
                iterations = Integer.parseInt(array[2]);
                email = array[3];

                try {
                    String link = s3Mandelbrot(height, width, iterations);
                    sendMail(email, link);
                    System.out.println("email: " + email + " link: " + link);
                    sqs.deleteMessage(new DeleteMessageRequest(sqs.getQueueUrl(CredentialHelper.QUEUE_NAME).getQueueUrl(), message.getReceiptHandle()));
                } catch (MessagingException e) {
//                    Logger.getAnonymousLogger().log(Level.ALL, "Messeging exception "+e.getMessage());
                    System.out.println("Messagingexception: " + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    System.out.println("IOException: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
//                Logger.getAnonymousLogger().log(Level.ALL, "Message was malformed");
                System.out.println("Message was malformed!");
            }
        }

    }

    private String s3Mandelbrot(int height, int width, int iterations) {
        try {

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

                String imageName = "image-" + UUID.randomUUID() + ".png";
                File imageFile = new File(imageName);
                ImageIO.write(image, "png", imageFile);

                AWSCredentials credentials = CredentialHelper.AWS_CREDENTIALS;


                AmazonS3 s3 = new AmazonS3Client(credentials);
                Region region = Region.getRegion(Regions.EU_WEST_1);

                s3.setRegion(region);
                try {
                    s3.createBucket(CredentialHelper.BUCKED_NAME);
                } catch (AmazonS3Exception e) {
                    System.out.println("use existing bucked");
                }
                s3.putObject(new PutObjectRequest(CredentialHelper.BUCKED_NAME, imageName, imageFile));

                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(CredentialHelper.BUCKED_NAME, imageName);
                generatePresignedUrlRequest.setMethod(HttpMethod.GET);
                URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
                return url.toString();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*

     */


    protected void sendMail(String email, String link)
            throws MessagingException, IOException {
        StringBuilder sb = new StringBuilder();
        javax.mail.Message message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress("SuperShooterAktivierung@gmx.de"));
        message.setRecipients(javax.mail.Message.RecipientType.TO,
                InternetAddress.parse(email));
        message.setSubject("Mandelbrot");
        // message.setText(activationLink);

        sb.append("<HTML>\n");
        sb.append("<HEAD>\n");
        sb.append("<TITLE>\n");
        sb.append("</TITLE>\n");
        sb.append("</HEAD>\n");

        sb.append("<BODY>\n");
        sb.append("<h3>Mandelbrot Link</h3><br>Dear user,<br> your awesome mandelbrot was created. Click ");
        sb.append("<a href=\"").append(link).append("\">here</a>");
        sb.append(" to see it.");
        sb.append("</BODY>\n");
        sb.append("</HTML>\n");


        message.setDataHandler(new DataHandler(new ByteArrayDataSource(
                sb.toString(), "text/html")));

        message.saveChanges();
        Transport.send(message);

    }

    private static Session getMailSession(String host, int port, boolean sslAuthenticationRequired, final String userName, final String password, boolean debug) {
        Properties properties = new Properties();
        String mailSmtp = "mail.smtp";

        if (sslAuthenticationRequired) {
//            mailSmtp += "s";

            properties.put(mailSmtp + ".socketFactory.port", port + "");
            properties.put(mailSmtp + ".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put(mailSmtp + ".socketFactory.fallback", "false");
        }

        properties.put("mail.debug", debug ? "true" : "false");
//        properties.put("mail.transport.protocol", "smtps");
        properties.put(mailSmtp + ".host", host);
        properties.put(mailSmtp + ".user", userName);
        properties.put(mailSmtp + ".password", password);
        properties.put(mailSmtp + ".port", Integer.toString(port));
        properties.put(mailSmtp + ".starttls.enable", "true");
        properties.put(mailSmtp + ".auth", "true");
        //fh vertrauen
        // properties.setProperty("mail.smtp.ssl.trust", host);
        return Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password);
                    }
                });
    }
}
