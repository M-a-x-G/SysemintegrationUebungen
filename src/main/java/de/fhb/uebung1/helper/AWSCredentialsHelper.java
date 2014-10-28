package de.fhb.uebung1.helper;

/**
 * Created by Max on 28.10.14.
 */
public class AWSCredentialsHelper {
    public static com.amazonaws.auth.AWSCredentials getAWSCredentials(){
     return new com.amazonaws.auth.AWSCredentials() {
         @Override
         public String getAWSAccessKeyId() {
             return "AWSAccessKeyId";
         }

         @Override
         public String getAWSSecretKey() {
             return "AWSSecretKey";
         }
     };
    }

}
