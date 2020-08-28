package com.mygroup.springstore.manager;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.json.Json;
import javax.json.JsonObject;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mygroup.springstore.dao.UserDAO;
import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.model.UserModel;

@Component
public class TokenManager {

    private static final Logger logger = LoggerFactory.getLogger(TokenManager.class);

    @Autowired
    private  UserDAO userDAO;

    private final SecretKey KEY;

    public TokenManager() throws NoSuchAlgorithmException {
        KEY = KeyGenerator.getInstance("DES").generateKey();
    }

    public String generateToken(int userId) throws DataAccessException {
        UUID uuid = UUID.randomUUID();
        String UUIDString = uuid.toString();

        
        LocalDateTime expDate = LocalDateTime.now().plusHours(1);
        JsonObject JSONToken = Json
                .createObjectBuilder() 
                .add("userId", userId)
                .add("uuid", UUIDString)
                .add("expDate", expDate.toString())
                .build();

        String token = JSONToken.toString();

        UserModel um = userDAO.getById(userId);

        um.setToken(token);

        userDAO.update(um);

        token = encryptToken(token);

        return token;
    }

    public boolean verifyToken(String token, int id) throws DataAccessException {
        
        token = decryptToken(token);

        JSONObject jsonToken = new JSONObject(token);

        int userId = jsonToken.getInt("userId");
        String uuid = jsonToken.getString("uuid");
        LocalDateTime expDate = LocalDateTime.parse(
                jsonToken.getString("expDate")
        );

        UserModel um = userDAO.getById(userId); 
        if (um.getToken() != null) {
            JSONObject jsonTokenStored = new JSONObject(um.getToken());
            if (uuid.equals(jsonTokenStored.getString("uuid")) && expDate.isAfter(LocalDateTime.now())) {
                return true;
            }
        }
        return false;
    }

    private String encryptToken(String str) {

        try {
			// Encode the string into a sequence of bytes using the named charset
			// storing the result into a new byte array
			byte[] utf8 = str.getBytes("UTF8");
			
			// Encrypt with DES cypher
			Cipher ecipher = Cipher.getInstance("DES");
			ecipher.init(Cipher.ENCRYPT_MODE, KEY);
			byte[] bytes = ecipher.doFinal(utf8);
			
			// Encode to base64
			
			String s = Base64.getEncoder().encodeToString(bytes);
			//Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(bytes));
            
			// return bis.toString();
			return s;
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public String decryptToken(String str) {
        try {
            // Decode in base64 to get an array of bytes
        	// Base64OutputStream bos = new Base64OutputStream(new ByteArrayOutputStream());
        	// bos.write(str.getBytes());
            //byte[] bytes = bos.toString().getBytes();
            
            byte[] bytes = Base64.getDecoder().decode(str);
            // Decrypt with DES cypher
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, KEY);
            byte[] utf8 = cipher.doFinal(bytes);

            // Create new string based on the specified charset
            return new String(utf8, "UTF8");
        }
        catch(NoSuchAlgorithmException e)
        {
          throw new InternalError();
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException();
        }

    }  
}
