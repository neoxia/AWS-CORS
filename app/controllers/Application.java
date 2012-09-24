package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.typesafe.config.ConfigFactory;

import play.mvc.*;

import views.html.*;

public class Application extends Controller {

  public static Result index() {
    return ok(index.render("Upload Index"));
  }
  
  public static Result Upload() throws GeneralSecurityException{
	  String S3_KEY=ConfigFactory.load("AWS.properties").getString("S3_KEY");
		String S3_SECRET=ConfigFactory.load("AWS.properties").getString("S3_SECRET");
		String S3_BUCKET=ConfigFactory.load("AWS.properties").getString("S3_BUCKET");
		 
		long EXPIRE_TIME=(60 * 5); // 5 minutes
		String S3_URL="http://s3.amazonaws.com";
		String objectName="/" + request().queryString().get("name")[0];
	 
		String mimeType=request().queryString().get("type")[0];
		long expires = (System.currentTimeMillis()/1000) + EXPIRE_TIME;
		String amzHeaders= "x-amz-acl:public-read";
		String stringToSign = "PUT\n\n"+mimeType+"\n"+expires+"\n"+amzHeaders+"\n"+S3_BUCKET+objectName;
		

		    Mac mac = Mac.getInstance("HmacSHA1");
		    SecretKeySpec secret = new SecretKeySpec(S3_SECRET.getBytes(),"HmacSHA1");
		    mac.init(secret);
		    byte[] digest = mac.doFinal(stringToSign.getBytes());
		    digest= Base64.encodeBase64(digest);  
		  
		    String sig = null;
			try {
				sig = URLEncoder.encode(new String(digest),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		  
		    
		String url = null;
		try {
			url = URLEncoder.encode(S3_URL+S3_BUCKET+objectName+"?AWSAccessKeyId="+S3_KEY+"&Expires="+expires+"&Signature="+sig,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 

		
		return ok(url);
	}
}