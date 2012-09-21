package controllers;

import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

  public static Result index() {
    return ok(index.render("Upload Index"));
  }
  
  public static Result Upload() throws GeneralSecurityException{
		String S3_KEY="AKIAIZCD5WN2JEXDHALQ";
		String S3_SECRET="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";
		String S3_BUCKET="/test_CORS_souhail";
		 
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
		  
		    String sig= URLEncoder.encode(new String(digest));  
		  
		    
		String url = URLEncoder.encode(S3_URL+S3_BUCKET+objectName+"?AWSAccessKeyId="+S3_KEY+"&Expires="+expires+"&Signature="+sig);
	 

		
		return ok(url);
	}
}