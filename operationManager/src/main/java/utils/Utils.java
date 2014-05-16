package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

public class Utils {
	public static String computeHash(String password) throws NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance("SHA-256");
		sha.update(password.getBytes());
		byte[] digest = sha.digest();

		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			String hex = Integer.toHexString(0xff & digest[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}

		return hexString.toString();
	}
	
	/**
	 * This is used to convert a HH:mm date and a base date to
	 * base + HH:mm
	 * 
	 * @param base
	 * @param time
	 * @return
	 */
	public static Date adjustDayTime(Date base, Date time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		int hours = cal.get(Calendar.HOUR_OF_DAY);
		int minutes = cal.get(Calendar.MINUTE);
		
		cal.setTime(base);
		cal.add(Calendar.HOUR_OF_DAY, hours);
		cal.add(Calendar.MINUTE, minutes);
		
		return cal.getTime();
	}
	
}
