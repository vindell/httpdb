package org.httpdb.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;


public class StringUtils {

	/**
	 * Any number of these characters are considered delimiters between
	 * multiple context config paths in a single String value.
	 */
	public static String CONFIG_LOCATION_DELIMITERS = ",; \t\n";
	
	 private static final byte[] HEXBYTES = {
	        (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5',
	        (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
	        (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
	    };
	 
	 private static int getNibble(int value) {

        if (value >= '0' && value <= '9') {
            return value - '0';
        }

        if (value >= 'a' && value <= 'f') {
            return 10 + value - 'a';
        }

        if (value >= 'A' && value <= 'F') {
            return 10 + value - 'A';
        }

        return -1;
    }
	 

    // Empty checks
    //-----------------------------------------------------------------------
    /**
     * <p>Checks if a CharSequence is empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the CharSequence.
     * That functionality is available in isBlank().</p>
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     * @since 3.0 Changed signature from isEmpty(String) to isEmpty(CharSequence)
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * <p>Checks if a CharSequence is not empty ("") and not null.</p>
     *
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null
     * @since 3.0 Changed signature from isNotEmpty(String) to isNotEmpty(CharSequence)
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }
       
    /**
     * <p>Checks if any one of the CharSequences are empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isAnyEmpty(null)             = true
     * StringUtils.isAnyEmpty(null, "foo")      = true
     * StringUtils.isAnyEmpty("", "bar")        = true
     * StringUtils.isAnyEmpty("bob", "")        = true
     * StringUtils.isAnyEmpty("  bob  ", null)  = true
     * StringUtils.isAnyEmpty(" ", "bar")       = false
     * StringUtils.isAnyEmpty("foo", "bar")     = false
     * </pre>
     *
     * @param css  the CharSequences to check, may be null or empty
     * @return {@code true} if any of the CharSequences are empty or null
     * @since 3.2
     */
    public static boolean isAnyEmpty(final CharSequence... css) {
      if (ArrayUtils.isEmpty(css)) {
        return true;
      }
      for (final CharSequence cs : css){
        if (isEmpty(cs)) {
          return true;
        }
      }
      return false;
    }
    
    /**
     * <p>Checks if none of the CharSequences are empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isNoneEmpty(null)             = false
     * StringUtils.isNoneEmpty(null, "foo")      = false
     * StringUtils.isNoneEmpty("", "bar")        = false
     * StringUtils.isNoneEmpty("bob", "")        = false
     * StringUtils.isNoneEmpty("  bob  ", null)  = false
     * StringUtils.isNoneEmpty(" ", "bar")       = true
     * StringUtils.isNoneEmpty("foo", "bar")     = true
     * </pre>
     *
     * @param css  the CharSequences to check, may be null or empty
     * @return {@code true} if none of the CharSequences are empty or null
     * @since 3.2
     */
    public static boolean isNoneEmpty(final CharSequence... css) {
      return !isAnyEmpty(css);
    }    
	
    
    /**
	 * 
	 * @description	：获得以 ",; \t\n"分割的字符数组
	 * @author 		： <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
	 * @date 		：Dec 17, 2015 9:07:47 PM
	 * @param str
	 * @return
	 */
	public static String[] tokenizeToStringArray(String str) {
		return tokenizeToStringArray(str, CONFIG_LOCATION_DELIMITERS, true, true);
	}
	
	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * Trims tokens and omits empty tokens.
	 * <p>The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using {@code delimitedListToStringArray}
	 * @param str the String to tokenize
	 * @param delimiters the delimiter characters, assembled as String
	 * (each of those characters is individually considered as delimiter).
	 * @return an array of the tokens
	 * @see java.util.StringTokenizer
	 * @see String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * <p>The given delimiters string is supposed to consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using {@code delimitedListToStringArray}
	 * @param str the String to tokenize
	 * @param delimiters the delimiter characters, assembled as String
	 * (each of those characters is individually considered as delimiter)
	 * @param trimTokens trim the tokens via String's {@code trim}
	 * @param ignoreEmptyTokens omit empty tokens from the result array
	 * (only applies to tokens that are empty after trimming; StringTokenizer
	 * will not consider subsequent delimiters as token in the first place).
	 * @return an array of the tokens ({@code null} if the input String
	 * was {@code null})
	 * @see java.util.StringTokenizer
	 * @see String#trim()
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(
			String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

		if (str == null) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(str, delimiters);
		List<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}
	
	/**
	 * Copy the given Collection into a String array.
	 * The Collection must contain String elements only.
	 * @param collection the Collection to copy
	 * @return the String array ({@code null} if the passed-in
	 * Collection was {@code null})
	 */
	public static String[] toStringArray(Collection<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}

	
	/**
     * If necessary, adds zeros to the beginning of a value so that the total
     * length matches the given precision, otherwise trims the right digits.
     * Then if maxSize is smaller than precision, trims the right digits to
     * maxSize. Negative values are treated as positive
     */
    public static String toZeroPaddedString(long value, int precision,
            int maxSize) {

        StringBuffer sb = new StringBuffer();

        if (value < 0) {
            value = -value;
        }

        String s = Long.toString(value);

        if (s.length() > precision) {
            s = s.substring(precision);
        }

        for (int i = s.length(); i < precision; i++) {
            sb.append('0');
        }

        sb.append(s);

        if (maxSize < precision) {
            sb.setLength(maxSize);
        }

        return sb.toString();
    }
    
    /**
     * Converts a hexadecimal string into a byte array
     *
     *
     * @param s hexadecimal string
     *
     * @return byte array for the hex string
     * @throws IOException
     */
    public static byte[] hexStringToByteArray(String s) throws IOException {

        int     l    = s.length();
        byte[]  data = new byte[l / 2 + (l % 2)];
        int     n,
                b    = 0;
        boolean high = true;
        int     i    = 0;

        for (int j = 0; j < l; j++) {
            char c = s.charAt(j);

            if (c == ' ') {
                continue;
            }

            n = getNibble(c);

            if (n == -1) {
                throw new IOException( "hexadecimal string contains non hex character");    //NOI18N
            }

            if (high) {
                b    = (n & 0xf) << 4;
                high = false;
            } else {
                b         += (n & 0xf);
                high      = true;
                data[i++] = (byte) b;
            }
        }

        if (!high) {
            throw new IOException( "hexadecimal string with odd number of characters");    //NOI18N
        }

        if (i < data.length) {
            data = (byte[]) ArrayUtils.resizeArray(data, i);
        }

        return data;
    }
    
    /**
     * Converts a byte array into a hexadecimal string
     *
     *
     * @param b byte array
     *
     * @return hex string
     */
    public static String byteArrayToHexString(byte[] b) {

        int    len = b.length;
        char[] s   = new char[len * 2];

        for (int i = 0, j = 0; i < len; i++) {
            int c = ((int) b[i]) & 0xff;

            s[j++] = (char) HEXBYTES[c >> 4 & 0xf];
            s[j++] = (char) HEXBYTES[c & 0xf];
        }

        return new String(s);
    }

    /**
     * Converts a byte array into an SQL hexadecimal string
     *
     *
     * @param b byte array
     *
     * @return hex string
     */
    public static String byteArrayToSQLHexString(byte[] b) {

        int    len = b.length;
        char[] s   = new char[len * 2 + 3];

        s[0] = 'X';
        s[1] = '\'';

        int j = 2;

        for (int i = 0; i < len; i++) {
            int c = ((int) b[i]) & 0xff;

            s[j++] = (char) HEXBYTES[c >> 4 & 0xf];
            s[j++] = (char) HEXBYTES[c & 0xf];
        }

        s[j] = '\'';

        return new String(s);
    }
}
