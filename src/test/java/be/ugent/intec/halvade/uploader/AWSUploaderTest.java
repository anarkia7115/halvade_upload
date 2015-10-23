package be.ugent.intec.halvade.uploader;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;
import com.amazonaws.services.s3.transfer.Upload;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author ddecap
 */
public class AWSUploaderTest
	extends TestCase{
    
    private String existingBucketName;
    private TransferManager tm;

    private void setS3Region(AWSCredentials credentials) {

        AmazonS3 s3 = new AmazonS3Client(credentials);
        Region cnNorth1 = Region.getRegion(Regions.CN_NORTH_1);
        s3.setRegion(cnNorth1);
        
        this.tm = new TransferManager(s3);   
    }
    
    public AWSUploaderTest(String existingBucketName) throws IOException {
    	super( existingBucketName );
        this.existingBucketName = existingBucketName;
        AWSCredentials c;
        
        try{
            DefaultAWSCredentialsProviderChain prov = new DefaultAWSCredentialsProviderChain();
            c= prov.getCredentials();
        } catch(AmazonClientException ex) {
            // read from ~/.aws/credentials
            String access = null;
            String secret = null;
            try (BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.home") + "/.aws/credentials"))) {
                String line;
                while ((line = br.readLine()) != null && !line.contains("[default]")) {}
                line = br.readLine();
                if(line != null)
                    access = line.split(" = ")[1];
                line = br.readLine();
                if(line != null)
                    secret = line.split(" = ")[1];
            }
            c = new BasicAWSCredentials(access, secret);
        }
        //this.tm = new TransferManager(c);
        setS3Region(c);
    }
    
    public void shutDownNow() {
        tm.shutdownNow();
    }
    
    public void Upload(String key, InputStream input, long size) throws InterruptedException {
        ObjectMetadata meta = new ObjectMetadata();
        //meta.setServerSideEncryption(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);   
        meta.setContentLength(size);
        System.out.println("Bucket: " + existingBucketName + "\tKey: " + key + "\tInput: " + input);
        Upload upload = tm.upload(existingBucketName, key, input, meta);
        //Upload upload = tm.upload(existingBucketName, key, input, input);      
        try {
        	// Or you can block and wait for the upload to finish
        	upload.waitForCompletion();
                //Logger.DEBUG("Upload complete.");
        } catch (AmazonClientException amazonClientException) {
        	//Logger.DEBUG("Unable to upload file, upload was aborted.");
        	//Logger.EXCEPTION(amazonClientException);
        }
    }

    public static Test suite()
    {
        return new TestSuite( AWSUploaderTest.class );
    }
    
    public void testApp() throws IOException, InterruptedException {
    	AWSUploader a = new AWSUploader("gcbibucket");
    	
    	String str = "This is a String ~ GoGoGo";

    	// convert String into InputStream
    	InputStream input = new ByteArrayInputStream(str.getBytes());
    	long size = str.getBytes().length;
    	a.Upload("halvade/testUpload", input, size);
    	
    	assertTrue(true);
    }
}
