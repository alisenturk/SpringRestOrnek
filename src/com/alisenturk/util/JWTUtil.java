package com.alisenturk.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.google.gson.Gson;

public class JWTUtil {

	private final String JWT_ISSUER = Helper.getAppMessage("jwt.issuer");
	private final String JWT_SECRET = Helper.getAppMessage("jwt.secret");
	private final int	 JWT_IAT	= 600;
	
	public String generateTokenWithJWT(int personelNo,String kullaniciAdi){

		long iat = Helper.dateAddMinute(new Date(),JWT_IAT).getTime() / 1000l; // issued at claim 
		long exp = iat + 10L; // expires claim. In this case the token expires in 120 minutes

		JWTSigner signer = new JWTSigner(JWT_SECRET); 
		HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("iss", JWT_ISSUER);
		claims.put("exp", exp); 
		claims.put("iat", iat);
		claims.put("username",kullaniciAdi);
		claims.put("personelno",personelNo);
		claims.put("data", (int)(Math.random()*10));

		String jwt = signer.sign(claims);
		
		return jwt;
	}
	
	
	public String getDataFromKey(String key){
		try {
		    JWTVerifier verifier = new JWTVerifier(JWT_SECRET);
		    Map<String,Object> claims= verifier.verify(key);
		   	
		    return String.valueOf(claims.get("data"));		    
		} catch (JWTVerifyException | InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException | IOException e) {
			e.printStackTrace(); 
		}
		
		return null;
	}
	public boolean verifyToken(String token){
		boolean isValid = false;
		try {
		    JWTVerifier verifier = new JWTVerifier(JWT_SECRET);
		    verifier.verify(token);		    
		    isValid = true;
		} catch (JWTVerifyException | InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException | IOException e) {
			e.printStackTrace(); 
		}
		return isValid;
	}
	
	public String generateTokenForData(Object obj,boolean convertJson){
		try{
			if(obj==null){
				return "";
			}
			
			long iat = Helper.dateAddMinute(new Date(),JWT_IAT).getTime() / 1000l; // issued at claim 
			long exp = iat + 10L; // expires claim. In this case the token expires in 120 minutes
			
			String json = String.valueOf(obj);
			if(convertJson){
				Gson gson = new Gson();
				json = gson.toJson(obj);
			}
			
			JWTSigner signer = new JWTSigner(JWT_SECRET); 
			HashMap<String, Object> claims = new HashMap<String, Object>();
			claims.put("iss", JWT_ISSUER);
			claims.put("exp", exp); 
			claims.put("iat", iat);		
			claims.put("data", json);
	
			return signer.sign(claims);
		}catch(Exception e){
			e.printStackTrace();
			Helper.errorLogger(JWTUtil.class, e);
		}
		
		return "";
	}
	
}
