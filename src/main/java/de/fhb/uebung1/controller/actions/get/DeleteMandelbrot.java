/**
 *
 */
package de.fhb.uebung1.controller.actions.get;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.fhb.uebung1.commons.HttpRequestActionBase;
import de.fhb.uebung1.helper.CredentialHelper;


/**
 * @author Max Gregor
 */
public class DeleteMandelbrot extends HttpRequestActionBase {

    @Override
    public void perform(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        AWSCredentials credentials = CredentialHelper.AWS_CREDENTIALS;

        String bucketName = req.getParameter("bucketname");
        if (bucketName == null || bucketName.isEmpty()){
            resp.getOutputStream().print("No Bucket specified (parameter bucketname)");
        }
        AmazonS3 s3 = new AmazonS3Client(credentials);
        Region region = Region.getRegion(Regions.EU_WEST_1);
        s3.setRegion(region);
        List<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();
        for (S3ObjectSummary s3ObjectSummary : s3.listObjects(bucketName).getObjectSummaries()) {
            keys.add(new DeleteObjectsRequest.KeyVersion(s3ObjectSummary.getKey()));
        }
        s3.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));
        resp.getOutputStream().print("All images are deleted");
    }
}
