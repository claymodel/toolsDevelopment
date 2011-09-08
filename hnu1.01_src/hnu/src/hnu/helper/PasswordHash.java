package hnu.helper;

import java.security.MessageDigest;

import sun.misc.BASE64Encoder;

/*
 * Copyright original (C) 2002-2003 by Martin Maier <martin.maier@fh-joanneum.at>
 * modifications (C) 2002-2003 by Thomas Maschutznig <thomas.maschutznig@fh-joanneum.at>
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */ 

/**
 * @author Martin Maier, Thomas Maschutznig
 */

public class PasswordHash {
	/**
	 * Creates a new PasswordHash object.
	 */
	public PasswordHash() {
	}

	/**
	 * Returns a SHA-Hash.
	 * 
	 * @param plain Plain String
	 * @return String containing an SHA-Hash
	 * @see MessageDigest
	 */
	public static String getSHAString(String plain) {
		byte[] hash = null;

		try {
			MessageDigest ms = MessageDigest.getInstance("SHA-256");

			ms.reset();
			ms.update(plain.getBytes());
			hash = ms.digest();

		} catch (Exception ex) {
			return null;
		}

		BASE64Encoder baseEnc = new sun.misc.BASE64Encoder();

		return baseEnc.encode(hash);
	}

	/**
	 * Compares the SHA-Hash of a plain string with an MD5-Hash.
	 * 
	 * @param plain
	 *            Plain String
	 * @param crypt
	 *            SHA-Hash
	 * @return true if equal, otherwise false
	 * @see MessageDigest
	 */
	public static boolean isEqual(String plain, String crypt) {
		return getSHAString(plain).equals(crypt);
	}
}
