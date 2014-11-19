package de.fhb.uebung1.helper;

/**
 * Created by Max on 28.10.14.
 */
public class CredentialHelper {

    public static final String QUEUE_NAME = "GregorQueue";
    public static final String BUCKED_NAME = "gregorue5";
    public static final String EMAIL_NAME = "emailname";
    public static final String EMAIL_PW = "password";
    public static final com.amazonaws.auth.AWSCredentials AWS_CREDENTIALS = new com.amazonaws.auth.AWSCredentials() {
        @Override
        public String getAWSAccessKeyId() {
            return "keyid";
        }

        @Override
        public String getAWSSecretKey() {
            return "secretkey";
        }
    };

}
