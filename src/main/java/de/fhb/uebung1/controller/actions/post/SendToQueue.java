package de.fhb.uebung1.controller.actions.post;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import de.fhb.uebung1.commons.HttpRequestActionBase;
import de.fhb.uebung1.helper.CredentialHelper;

/**
 * Created by Max on 18.11.14.
 */
public class SendToQueue extends HttpRequestActionBase {
    @Override
    public void perform(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        int width = -1;
        int heigth = -1;
        int iterations = -1;

        try{
            width = Integer.parseInt(req.getParameter("width"));
            heigth = Integer.parseInt(req.getParameter("height"));
            iterations = Integer.parseInt(req.getParameter("iteration"));
        }catch (NumberFormatException e){
            resp.getOutputStream().print("In die Felder width, height, iterations müssen Zahlen eingetragen werden.");
        }

        AWSCredentials credentials = CredentialHelper.AWS_CREDENTIALS;
        AmazonSQSClient sqs = new AmazonSQSClient(credentials);

        try{
            sqs.getQueueUrl(CredentialHelper.QUEUE_NAME);
        }catch(QueueDoesNotExistException e){
            CreateQueueRequest createQueueRequest = new CreateQueueRequest().withQueueName(CredentialHelper.QUEUE_NAME);
            sqs.createQueue(createQueueRequest);
        }

        sqs.sendMessage(new SendMessageRequest(sqs.getQueueUrl(CredentialHelper.QUEUE_NAME).getQueueUrl(),
                width + "," + heigth + "," + iterations + "," + email));

        resp.getOutputStream().print("successfully send to queue");
    }
}
