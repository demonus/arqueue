package io.github.arqueue.hibernate.beans.helpers;

import io.github.arqueue.common.Configuration;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

/**
 * Created by root on 10/26/15.
 */
@Converter
public class CryptoConverter implements AttributeConverter<String, String>
{
	private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
	private static final String KEY_STRING = Configuration.getInstance().getEncryptionKey();

	@Override
	public String convertToDatabaseColumn(String ccNumber)
	{
		try
		{
			byte[] key = KEY_STRING.getBytes("UTF-8");

			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit

			SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

			Cipher c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.ENCRYPT_MODE, secretKeySpec);

			return Base64.getEncoder().encodeToString(c.doFinal(ccNumber.getBytes()));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public String convertToEntityAttribute(String dbData)
	{
		// do some decryption
		try
		{
			byte[] key = KEY_STRING.getBytes("UTF-8");

			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16); // use only first 128 bit

			SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

			Cipher c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.DECRYPT_MODE, secretKeySpec);

			return new String(c.doFinal(Base64.getDecoder().decode(dbData)));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}