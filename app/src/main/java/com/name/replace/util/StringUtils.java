package com.name.replace.util;


public class StringUtils {

	/**
	 * <p>
	 * Compares two CharSequences, returning {@code true} if they are equal.
	 * </p>
	 * StringUtils.equals(null, null)   = true
	 * StringUtils.equals("abc", "abc") = true
	 * @param cs1
	 * @param cs2
	 * @return
	 */
	public static boolean equals(CharSequence cs1, CharSequence cs2) {
		return cs1 == null ? cs2 == null : cs1.equals(cs2);
	}

	/**
	 *<p> Checks if a CharSequence is empty ("") or null.</p>
	 * StringUtils.isEmpty(null) = true
	 * StringUtils.isEmpty("") = true
	 * StringUtils.isEmpty(" ") = false
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isEmpty(CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * <p>
	 * Checks if a CharSequence is not empty ("") and not null.
	 * </p>
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isNotEmpty(CharSequence cs) {
		return !StringUtils.isEmpty(cs);
	}

	/**
	 * <p>
	 * Checks if a CharSequence is whitespace, empty ("") or null.
	 * </p>
	 * 
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	
	/**
	 * 
	 * @param value
	 *            double
	 * @return 保留2位小数点的string
	 */
	public static String getFormatValue(String value) {
		if (isNotEmpty(value)) {
			return String.format("%.2f", Double.valueOf(value));
		} else {
			return "";
		}
	}
	
}
