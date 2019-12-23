/*
 * $Id: StringUtil.java 427 2009-10-24 02:38:06Z thaiha $
 *	Copyright (c) Code Block, http://codeblock.vn, 2009. All rights reserved.
 * 
 *  $Author: thaiha $
 *  $Rev: 427 $
 */
package com.tinhvan.hd.common;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Clob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Class StringUtil.
 */
public final class StringUtil {

	/** The Constant EMPTY. */
	public static final String EMPTY = "";

	/** The Constant REPLACE_CHARS. */
	private static final String REPLACE_CHARS = "aáàảãạâấầẩẫậăắằẳẵặeéèẻẽẹêế�?ểễệiíìỉĩịuúùủũụưứừửữựoóò�?õ�?ôốồổỗộơớ�?ởỡợyýỳỷỹỵ";

	/** The Constant LINK_PATTERN. */
	private static final Pattern LINK_PATTERN = Pattern
			.compile("https?://[^ <>]*");

	/** The Constant HTML_TAG_PATTERN. */
	private static final Pattern HTML_TAG_PATTERN = Pattern
			.compile("<(.|\r|\n)*?>");
	
	private static final String EMAIL_REGEX= 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	
	/** The Constant charset. */
	private static final String charset = "!0123456789abcdefghijklmnopqrstuvwxyz";

	private static final String NUMBERS = "0123456789";

	/** The Constant REF_HTML_DEV_PATTERN. */
	private static final Pattern REF_HTML_DEV_PATTERN = Pattern
			.compile("<div id=\"refHTML\">&nbsp;</div>");

	private static final String[] htmlChar = new String[] { "&", "<", ">", "'",
			"\"" };
	private static final String[] htmlNameCode = new String[] { "&amp;",
			"&lt;", "&gt;", "&apos;", "&quot;" };

	private static final Pattern SPECIAL_CHAR_PATTERN = Pattern
			.compile("[,.?;:\'\"`~!@#$%&*()^<>{}\\[\\]\\\\/ ]");

	private static final Pattern SPECIAL_CHAR_BADWORD_PATTERN = Pattern
			.compile("([,.?;:\'\"`~!@#$%&*()^<>{}\\[\\]\\\\/])");

	private static final Pattern PATTERN_NONE_WORD = Pattern
			.compile("([^a-zA-Z0-9 ])");

	/** STRING_SEARCH_LIKE_ALL. */
	public static final String STRING_SEARCH_LIKE_ALL = "%%";

	private static final String[] oracleTextKeywords = new String[] { "ACCUM",
			"ABOUT", "NOT", "OR", "AND", "BT", "BTG", "BTP", "BTI", "NT",
			"NTG", "NTP", "NTI", "PT", "RT", "RT", "SQE", "SYN", "TR", "TRSYN",
			"TT", "FUZZY", "HASPATH", "INPATH", "MINUS", "NEAR", "WITHIN",
			"84%", "8%", "MDATA" };

	/** The enum of oracle search operators */
	private static enum OrlSearchOperator {

		AND(";"), OR(",");

		private String value;

		public String getValue() {
			return value;
		}

		OrlSearchOperator(String Value) {
			this.value = Value;
		}
	}

	/**
	 * Checks if is null or empty.
	 * 
	 * @param s
	 *            the s
	 * 
	 * @return true, if is null or empty
	 */
	public static boolean isNullOrEmpty(final String s) {
		return (s == null || s.trim().length() == 0);
	}

	/**
	 * Checks if is null or empty.
	 * 
	 * @param s
	 *            the s
	 * 
	 * @return true, if is null or empty
	 */
	public static boolean isNullOrEmptyNotTrim(final String s) {
		return (s == null || s.length() == 0);
	}

	/**
	 * Gets the m d5 hash.
	 * 
	 * @param string
	 *            the string
	 * 
	 * @return the m d5 hash
	 */
	public static String getMD5Hash(String string) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(string.getBytes());
			final byte[] hash = digest.digest();
			final StringBuilder result = new StringBuilder(hash.length);
			for (int i = 0; i < hash.length; i++) {
				result.append(Integer.toString((hash[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			return result.toString();
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "error";
		}
	}

	/**
	 * Gets the action.
	 * 
	 * @param fullPath
	 *            the full path
	 * 
	 * @return the action
	 */
	public static String getAction(final String fullPath) {
		if (isNullOrEmpty(fullPath)) {
			return null;
		}

		final int index = fullPath.lastIndexOf("/");
		if (index != -1) {
			return fullPath.substring(index + 1);
		}

		return fullPath;
	}

	/**
	 * Format money.
	 * 
	 * @param i
	 *            the i
	 * 
	 * @return the string
	 */
	public static String formatMoney(final long i) {
		return formatMoney(Long.toString(i));
	}

	public static String generatePromotionCode() {
		SecureRandom sc;
		try {
			sc = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			sc = new SecureRandom();
		}
		String code = "";
		for (int i = 0; i < 13; i++) {
			code += NUMBERS.charAt(sc.nextInt(NUMBERS.length()));
		}
		return code;
	}

	/**
	 * Haformart money.
	 * 
	 * @param s
	 *            the s
	 * 
	 * @return the string
	 */
	public static String haformartMoney(String s) {
		if (isNullOrEmpty(s)) {
			return StringUtil.EMPTY;
		}
		if (s.toUpperCase().equals("LI�?N HỆ")) {
			return s;
		}
		if (s.contains(".")) {
			return s;
		}
		String p = StringUtil.EMPTY;
		if (s.contains("-")) {
			String k[] = s.split("-");
			s = k[0];
			p = k[1];
			s = formatMoney(s);
			p = formatMoney(p);
			return s + "-" + p;
		} else {
			return formatMoney(s);
		}
	}

	/**
	 * Format money.
	 * 
	 * @param s
	 *            the s
	 * 
	 * @return the string
	 */
	public static String formatMoney(String s) {
		if (isNullOrEmpty(s)) {
			return StringUtil.EMPTY;
		}

		String p = StringUtil.EMPTY;
		if (s.startsWith("-") || s.startsWith("+")) {
			p = s.substring(0, 1);
			s = s.substring(1);
		}

		String r = StringUtil.EMPTY;
		for (int k = s.length(); k >= 0; k -= 3) {
			if (k - 3 <= 0) {
				r = s.substring(0, k) + r;
			} else {
				r = "." + s.substring(k - 3, k) + r;
			}
		}

		if (!isNullOrEmpty(r) && r.charAt(0) == '.') {
			r = r.substring(1);
		}

		return p + r;
	}

	/**
	 * Format money.
	 * 
	 * @param s
	 *            the s
	 * @param shitIE
	 *            the shit ie
	 * 
	 * @return the string
	 */
	public static String formatMoney(final String s, final boolean shitIE) {
		if (shitIE && isNullOrEmpty(s)) {
			return StringUtil.EMPTY;
		}

		return formatMoney(s);
	}

	/**
	 * Simple camel case.
	 * 
	 * @param s
	 *            the s
	 * 
	 * @return the string
	 */
	public static String simpleCamelCase(final String s) {
		if (isNullOrEmpty(s)) {
			return s;
		}

		String r = s.substring(1);
		r = Character.toLowerCase(s.charAt(0)) + r;

		return r;
	}

	/**
	 * Checks if is number.
	 * 
	 * @param s
	 *            the s
	 * 
	 * @return true, if is number
	 */
	public static boolean isNumber(final String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isDigit(s.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Format text.
	 * 
	 * @param data
	 *            the data
	 * 
	 * @return the string
	 */
	public static String formatText(String data) {
		// String str =
		// "aáàảãạâấầẩẫậăắằẳẵặeéèẻẽẹêế�?ểễệiíìỉĩịuúùủũụưứừửữựoóò�?õ�?ôốồổỗộơớ�?ởỡợyýỳỷỹỵ";

		for (int i = 1; i < 6; i++) {
			data = data.replace(REPLACE_CHARS.charAt(i + 6 * 0),
					REPLACE_CHARS.charAt(0));
			data = data.replace(REPLACE_CHARS.charAt(i + 6 * 1),
					REPLACE_CHARS.charAt(6));
			data = data.replace(REPLACE_CHARS.charAt(i + 6 * 2),
					REPLACE_CHARS.charAt(12));
			data = data.replace(REPLACE_CHARS.charAt(i + 6 * 3),
					REPLACE_CHARS.charAt(18));
			data = data.replace(REPLACE_CHARS.charAt(i + 6 * 4),
					REPLACE_CHARS.charAt(24));
			data = data.replace(REPLACE_CHARS.charAt(i + 6 * 5),
					REPLACE_CHARS.charAt(30));
			data = data.replace(REPLACE_CHARS.charAt(i + 6 * 6),
					REPLACE_CHARS.charAt(36));
			data = data.replace(REPLACE_CHARS.charAt(i + 6 * 7),
					REPLACE_CHARS.charAt(42));
			data = data.replace(REPLACE_CHARS.charAt(i + 6 * 8),
					REPLACE_CHARS.charAt(48));
			data = data.replace(REPLACE_CHARS.charAt(i + 6 * 9),
					REPLACE_CHARS.charAt(54));
			data = data.replace(REPLACE_CHARS.charAt(i + 6 * 10),
					REPLACE_CHARS.charAt(60));
			data = data.replace(REPLACE_CHARS.charAt(i + 6 * 11),
					REPLACE_CHARS.charAt(66));
		}

		return data;
	}

	/**
	 * Generate key.
	 * 
	 * @param data
	 *            the data
	 * 
	 * @return the string
	 */
	public static String generateKey(String data) {
		final String strA = "aâă";
		final String strE = "eê";
		final String strU = "uư";
		final String strD = "dđ";
		final String strO = "oôơ";

		data = data.toLowerCase();
		data = data.replace('ð', 'đ');
		data = formatText(data);

		data = data.replace(strA.charAt(1), strA.charAt(0));
		data = data.replace(strA.charAt(2), strA.charAt(0));
		data = data.replace(strE.charAt(1), strE.charAt(0));
		data = data.replace(strU.charAt(1), strU.charAt(0));
		data = data.replace(strD.charAt(1), strD.charAt(0));
		data = data.replace(strO.charAt(1), strO.charAt(0));
		data = data.replace(strO.charAt(2), strO.charAt(0));

		return data.replaceAll("\\s", "_");

	}

	/**
	 * Clear html tags.
	 * 
	 * @param strHTML
	 *            the str html
	 * @param intWorkFlow
	 *            the int work flow
	 * 
	 * @return the string
	 */
	public static String ClearHTMLTags(String strHTML, int intWorkFlow) {
		if (isNullOrEmpty(strHTML)) {
			return "";
		}
		strHTML = HTML_TAG_PATTERN.matcher(strHTML).replaceAll("");
		// LuanDV - [ 02/07/2010 - replace all case breakline
		strHTML = strHTML.replaceAll("\r\n", " <br>")
				.replaceAll("\n\r", " <br>").replaceAll("\n", " <br>")
				.replaceAll("\r", " <br>");
		// ] LuanDV

		strHTML = trim(strHTML, "<br>");

		return strHTML;
	}

	public static String trimUnusedCharacter(String input) {
		input = trim(input, "\r\n");
		input = trim(input, "\n");
		return input;
	}

	/**
	 * Trim.
	 * 
	 * @param str
	 *            the str
	 * @return the string
	 */
	public static String trim(String str) {
		if (str == null || "".equals(str)) {
			return "";
		}
		return str.trim();
	}

	public static String trimAllWhiteSpace(String str) {
		if (str == null || "".equals(str)) {
			return "";
		}
		str = str.trim();
		str = str.replaceAll("\\s+", " ");
		return str;
	}

	/**
	 * Trim.
	 * 
	 * @param input
	 *            the input
	 * @param trimChar
	 *            the trim char
	 * @return the string
	 */
	public static String trim(String input, String trimChar) {
		try {
			if (StringUtil.isNullOrEmpty(input))
				return "";

			input = input.trim();

			if (!StringUtil.isNullOrEmpty(trimChar)) {
				int trimLength = trimChar.length();

				// remove begin trimChar
				while (input.indexOf(trimChar) == 0) {
					input = input.substring(trimLength).trim();
				}

				// remove last trimChar
				while (input.length() - trimLength >= 0
						&& input.lastIndexOf(trimChar) == input.length()
								- trimLength) {
					input = input.substring(0, input.length() - trimLength)
							.trim();
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return input;
	}

	/**
	 * @author LuanDV
	 * @since 18/02/2011 - Created.
	 * @param source
	 * @return
	 */
	public static String toLower(final String source) {
		if (isNullOrEmpty(source)) {
			return null;
		} else {
			return source.toLowerCase();
		}
	}



	/**
	 * Construct filter from list value.
	 * 
	 * @param filterItem
	 *            the filter item
	 * @param listString
	 *            the list string
	 * @param operator
	 *            the operator
	 * @return the string
	 */
	public static String constructFilterFromListValue(String filterItem,
			String[] listString, String operator) {
		String result = "";
		result = filterItem + "=" + listString[0] + " ";
		for (int i = 1; i < listString.length; i++) {
			result += operator + " " + filterItem + "=" + listString[i] + " ";
		}
		return result;
	}

	/**
	 * From null to space.
	 * 
	 * @param a
	 *            the a
	 * 
	 * @return the string
	 */
	public static String fromNullToSpace(String a) {
		if (a == null || "".equals(a)) {
			return " ";
		} else {
			return a;
		}
	}

	/**
	 * From null to emtpy string.
	 * 
	 * @param a
	 *            the a
	 * 
	 * @return the string
	 */
	public static String fromNullToEmtpyString(String a) {
		if (a == null) {
			return "";
		} else {
			return a;
		}
	}




	public static String clearAllHTMLTags(String strHTML) {
		if (isNullOrEmpty(strHTML)) {
			return "";
		}
		strHTML = strHTML.replaceAll("<br>", "");
		strHTML = HTML_TAG_PATTERN.matcher(strHTML).replaceAll("");
		strHTML = strHTML.replaceAll("\r", "").replaceAll("\n", "");
		return strHTML;
	}

	/**
	 * Clear html tags.
	 * 
	 * @param strHTML
	 *            the str html
	 * @return the string
	 */
	public static String ClearHTMLTags(String strHTML) {
		if (isNullOrEmpty(strHTML)) {
			return "";
		}
		strHTML = HTML_TAG_PATTERN.matcher(strHTML).replaceAll("");

		return strHTML;

	}

	/*
	 * clear html tag and co nhieu \n lien tiep thi chi giu lai 1 \n
	 */

	public static String ClearHTMLTagsAndEndLine(String strHTML) {
		if (isNullOrEmpty(strHTML)) {
			return "";
		}
		strHTML = strHTML.replaceAll("<br>", "\n");
		strHTML = strHTML.replaceAll("</li>", "\n");
		strHTML = strHTML.replaceAll("<li>", "-");
		strHTML = HTML_TAG_PATTERN.matcher(strHTML).replaceAll("");
		strHTML = strHTML.replaceAll("\\n{2,}", "\n");
		if (strHTML.startsWith("\n")) {
			strHTML = strHTML.substring(1, strHTML.length());
		}
		if (strHTML.endsWith("\n")) {
			strHTML = strHTML.substring(0, strHTML.length() - 1);
		}
		return strHTML;
	}

	public static String ClearHTMLTagsExceptEndLine(String strHTML) {
		if (isNullOrEmpty(strHTML)) {
			return "";
		}
		strHTML = strHTML.replaceAll("<br>", "\n");
		strHTML = HTML_TAG_PATTERN.matcher(strHTML).replaceAll("");

		return strHTML;

	}

	/**
	 * Check whether a Content contain html tag
	 * 
	 * @param tag
	 * @return
	 * @author khanh.hd
	 */
	public static boolean isExistHTMLTag(String tag) {
		final Matcher matcher = HTML_TAG_PATTERN.matcher(tag);
		return matcher.matches();
	}

	/**
	 * Removes the ref html dev.
	 * 
	 * @param text
	 *            the text
	 * @return the string
	 */
	public static String removeRefHtmlDev(final String text) {
		return REF_HTML_DEV_PATTERN.matcher(text).replaceAll("");
	}

	/**
	 * Clear java script tags.
	 * 
	 * @param strHTML
	 *            the str html
	 * 
	 * @return the string
	 */
	public static String ClearJavaScriptTags(String strHTML) {

		// Pattern pattern = null;
		// Matcher matcher = null;
		// String regex;
		// String strTagLess = null;
		// strTagLess = strHTML;
		//
		// if(intWorkFlow == -1)
		// {
		// regex = "<[^>]*>";
		// //removes all html tags
		// pattern = pattern.compile(regex);
		// strTagLess = pattern.matcher(strTagLess).replaceAll(" ");
		// }
		//
		// if(intWorkFlow > 0 && intWorkFlow < 3)
		// {
		//
		// regex = "[<]";
		// //matches a single <
		// pattern = pattern.compile(regex);
		// strTagLess = pattern.matcher(strTagLess).replaceAll("<");
		//
		// regex = "[>]";
		// //matches a single >
		// pattern = pattern.compile(regex);
		// strTagLess = pattern.matcher(strTagLess).replaceAll(">");
		// }
		// return strTagLess;
		// String nohtml = strHTML.replaceAll("\\<.*?/>","");
		strHTML = strHTML.replaceAll("\n", "<br/>");
		String nohtml = strHTML.replaceAll("<.*script.*>", "");
		return nohtml;
		// strHTML.rep
	}

	/**
	 * Checks if is valid email address.
	 * 
	 * @param emailAddress
	 *            the email address
	 * 
	 * @return true, if is valid email address
	 */
	/*public static boolean isValidEmailAddress(String emailAddress) {
		// a null string is invalid
		if (emailAddress == null) {
			return false;
		}
		// a string without a "@" is an invalid email address
		if (emailAddress.indexOf("@") < 0) {
			return false;
		}

		// a string without a "." is an invalid email address
		if (emailAddress.indexOf(".") < 0) {
			return false;
		}
		if (lastEmailFieldTwoCharsOrMore(emailAddress) == false) {
			return false;
		}
		try {
			// InternetAddress internetAddress = new
			// InternetAddress(emailAddress);
			return true;
		} catch (Exception ae) {
			// log exception
			return false;
		}
	}*/
	
	public static boolean isValidEmail(String emailAddress) {
		Matcher matcher = EMAIL_PATTERN.matcher(emailAddress);
		return matcher.matches();
	}

	/**
	 * Last email field two chars or more.
	 * 
	 * @param emailAddress
	 *            the email address
	 * 
	 * @return true, if successful
	 */
	/*private static boolean lastEmailFieldTwoCharsOrMore(String emailAddress) {
		if (emailAddress == null)
			return false;
		StringTokenizer st = new StringTokenizer(emailAddress, ".");
		String lastToken = null;
		while (st.hasMoreTokens()) {
			lastToken = st.nextToken();
		}
		if (lastToken.length() >= 2) {
			return true;
		} else {
			return false;
		}
	}*/

	/**
	 * Gets the random string.
	 * 
	 * @param length
	 *            the length
	 * 
	 * @return the random string
	 */
	public static String getRandomString(int length) {
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		return sb.toString();
	}

	/**
	 * Gets the short content.
	 * 
	 * @param fullContent
	 *            the full content
	 * @param numOfRows
	 *            the number of rows - 1
	 * @return the short content
	 */
	public static String getShortContent(final String fullContent,
			final int numOfRows, final String ch) {
		String shortContent = "";
		if (isNullOrEmpty(fullContent)) {
			shortContent = fullContent;
		} else {
			shortContent = shortenContent(fullContent, ch, numOfRows, 200);
		}
		return shortContent;
	}

	/**
	 * Shorten the full content to create short content, which has a special
	 * character occurs as much as maxOccurrence or has as much as masChar
	 * characters.
	 * 
	 * @param fullContent
	 *            the full content
	 * @param ch
	 *            the pattern character
	 * @param maxOccurrence
	 *            the max occurrence
	 * @param maxChar
	 *            the max char
	 * @return the shorten string
	 */
	public static String shortenContent(final String fullContent,
			final String ch, final int maxOccurrence, final int maxChar) {
		int occur = 0;
		int begin = 0;
		int end = fullContent.indexOf(ch);
		StringBuilder result;
		if (end == -1) {
			if (fullContent.length() < maxChar) {
				result = new StringBuilder(fullContent);
			} else {
				result = new StringBuilder(fullContent.substring(begin,
						maxChar - 3));
				result = result.append("...");
			}
		} else {
			result = new StringBuilder();
			while (end != -1 && occur <= maxOccurrence
					&& result.length() < (maxChar - 3)) {
				result.append(fullContent.substring(begin, end));
				begin = end;
				end = fullContent.indexOf(ch, end + 1);
				occur++;
			}
			if (result.length() - 3 >= maxChar) {
				result.substring(0, maxChar - 3);
				result = result.append("...");
			} else if (occur <= maxOccurrence) {
				result.append(fullContent.substring(begin, fullContent.length()));
			} else {
				result = result.append("...");
			}
		}
		return result.toString();
	}

	/**
	 * Remove special charactor from string
	 * 
	 * @param text
	 * @param byString
	 * @return
	 */
	public static String removeSpecialChars(String text, String byString) {
		final String[] chars = new String[] { ",", ".", "/", "!", "@", "#",
				"$", "%", "^", "&", "*", "'", "\"", ";", "-", "_", "(", ")",
				":", "|", "[", "]", "~", "+", "{", "}", "?", "\\", "<", ">" };
		if (StringUtil.isNullOrEmpty(text))
			return text;

		for (int i = 0; i < chars.length; i++) {
			if (text.indexOf(chars[i]) >= 0) {
				text = text.replace(chars[i], byString);
			}
		}
		return text;
	}

	/**
	 * @param text
	 * @param byString
	 * @return
	 */
	public static String removeNumberChars(String text, String byString) {
		final String[] chars = new String[] { "0", "1", "2", "3", "4", "5",
				"6", "7", "8", "9" };
		if (StringUtil.isNullOrEmpty(text))
			return text;

		for (int i = 0; i < chars.length; i++) {
			if (text.indexOf(chars[i]) >= 0) {
				text = text.replace(chars[i], byString);
			}
		}
		return text;
	}

	/**
	 * Convert string to searchable text with
	 * 
	 * @param searchText
	 * @return
	 */
	public static String toOracleSearchText(String searchText,
			boolean isAutocomplete) {
		String[] splitString;
		StringBuilder text = new StringBuilder();
		String OpPat = ",;&"; // search operator pattern
		String SpPat = "<>./!@#$%^*'\"-_():|[]~+{}?\\\n"; // special char
		// pattern
		char[] searchTextArr;
		boolean preCheck = true;

		// [DungNTM commented on Jan 10 - 2011: remove all special characters
		// later]
		// searchText = clearAllHTMLTags(searchText);
		// [end]

		if (!StringUtil.isNullOrEmpty(searchText)) {
			searchTextArr = searchText.toCharArray();

			// remove special char, keep operator char
			for (int i = 0; i < searchTextArr.length; i++) {
				if (SpPat.indexOf(searchTextArr[i]) >= 0) {
					searchTextArr[i] = ' ';
				} else if (OpPat.indexOf(searchTextArr[i]) >= 0) {
					if (preCheck) {
						searchTextArr[i] = ' ';
					}
					preCheck = true;
				} else
					preCheck = false;
			}

			searchText = String.valueOf(searchTextArr).trim();
			if (StringUtil.isNullOrEmpty(searchText)) {
				return STRING_SEARCH_LIKE_ALL;
			}

			if (isAutocomplete)
				// searchText = "%" + text.toString().trim() + "%";
				searchText = searchText.trim() + "%";
			else
				searchText = searchText.trim();

			splitString = searchText.split(" ");
			// if (splitString.length > 1) {
			for (int i = 0; i < splitString.length; i++) {
				if (!"".equals(splitString[i])) {

					// if (!isAutocomplete) {
					for (int j = 0; j < oracleTextKeywords.length; j++) {
						if (oracleTextKeywords[j].equals(splitString[i]
								.toUpperCase())) {
							splitString[i] = "{" + splitString[i] + "}";
							break;
						}
					}
					// }

					text.append(splitString[i] + " ");
				}
			}

			searchText = text.toString();

			// } else if (splitString.length == 1) {
			// text.append("%");
			// text.append(splitString[0].trim());
			// text.append("%");
			// }

			// remove last operator if exist
			if (OpPat.indexOf(searchText.charAt(searchText.length() - 1)) >= 0) {
				searchText = searchText.substring(0, searchText.length() - 1);
			}
		} else
			return STRING_SEARCH_LIKE_ALL;

		// System.out.println("searchString:" + searchText);
		return searchText;
	}

	/**
	 * Convert string to searchable text OR
	 * 
	 * @param searchText
	 * @return
	 */
	public static String toOracleSearchText(String searchText,
			OrlSearchOperator operator) {
		String[] splitString;
		StringBuilder text = new StringBuilder();
		String SpPat = "./!@#$%^*'\"-_():|[]~+{}?\\"; // special char pattern
		char[] searchTextArr;

		searchText = clearAllHTMLTags(searchText);

		if (!StringUtil.isNullOrEmpty(searchText)) {
			searchTextArr = searchText.toCharArray();
			for (int i = 0; i < searchTextArr.length; i++) {
				if (SpPat.indexOf(searchTextArr[i]) >= 0) {
					searchTextArr[i] = ' ';
				}
			}
			searchText = String.valueOf(searchTextArr).trim();

			if (StringUtil.isNullOrEmpty(searchText)) {
				return "%%";
			}

			splitString = searchText.split(" ");
			for (int i = 0; i < splitString.length; i++) {
				if (!"".equals(splitString[i])) {
					text.append(splitString[i].trim());
					if (i < splitString.length - 1)
						text.append(operator.getValue());
				}
			}
			searchText = text.toString().trim();
		} else
			return "%%";

		// System.out.println("searchString:" + searchText);
		return searchText;
	}

	/**
	 * Convert string to search like text.
	 * 
	 * @param searchText
	 *            the text to be searched
	 * @return String[1]: the search text String[0]: escape sql
	 */
	public static String[] toOracleSearchLike(String searchText) {
		String[] result = new String[2];
		String[] splitString;
		StringBuilder text = new StringBuilder("%");
		searchText = clearAllHTMLTags(searchText);
		String escapeChar = "/";
		boolean escape = false;

		if (!StringUtil.isNullOrEmpty(searchText)) {

			if (searchText.indexOf("%") >= 0) {
				if (searchText.indexOf(escapeChar) >= 0)
					searchText = searchText.replaceAll(escapeChar, escapeChar
							+ escapeChar);

				searchText = searchText.replaceAll("%", escapeChar + "%");
				escape = true;
			}

			splitString = searchText.split(" ");

			for (int i = 0; i < splitString.length; i++) {
				if (!"".equals(splitString[i]))
					text.append(splitString[i] + " ");
			}
			result[1] = text.toString().trim() + "%";

			if (escape)
				result[0] = " ESCAPE '" + escapeChar + "'";
			else
				result[0] = "";

			searchText = text.toString();
		} else {
			result[1] = "%%";
			result[0] = "";
		}

		return result;
	}

	/**
	 * Convert special html character to html code.
	 * 
	 * @author LuanDV
	 * @since 22/07/2010
	 * @param strSource
	 * @return
	 */
	public static String convertStringToHTMLCode(String strSource) {
		if (isNullOrEmpty(strSource)) {
			return "";
		}
		for (int i = 0; i < htmlChar.length; i++) {
			strSource = strSource.replaceAll(htmlChar[i], htmlNameCode[i]);
		}
		return strSource;
	}

	/**
	 * @author LuanDV
	 * @since 29/07/2010 - Created
	 * @param strSource
	 * @return
	 * @throws Exception
	 */
	public static String decodeMessage(String strSource) throws Exception {
		if (isNullOrEmpty(strSource)) {
			return "";
		}
		return URLDecoder.decode(strSource, "UTF-8");
	}

	/**
	 * @author LuanDV
	 * @since 29/07/2010 - Created
	 * @param strSource
	 * @return
	 * @throws Exception
	 */
	public static String encodeMessage(String strSource) throws Exception {
		if (isNullOrEmpty(strSource)) {
			return "";
		}
		return URLEncoder.encode(strSource, "UTF-8");
	}

	public static String toString(Object obj) throws Exception {
		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}


	public static String replaceSpecialChar(final String strSource) {
		if (isNullOrEmpty(strSource)) {
			return "";
		}
		return SPECIAL_CHAR_PATTERN.matcher(strSource).replaceAll("");
	}

	/**
	 * Escape string ('\n' -> '\\n').
	 * 
	 * @param source
	 *            the source
	 * @return the string
	 */
	public String escape(String source) {
		return null;
	}

	public static String escapeSpecialCharacter(String source) {
		return SPECIAL_CHAR_BADWORD_PATTERN.matcher(source)
				.replaceAll("\\\\$1");
	}

	public static String escapeNonWordCharacter(String source) {
		return PATTERN_NONE_WORD.matcher(source).replaceAll("\\\\$1");
	}

	/**
	 * Replace link.
	 * 
	 * @param text
	 *            the text
	 * @return the string
	 */
	public static String replaceLink(final String text) {
		final StringBuffer result = new StringBuffer();
		final Matcher matcher = LINK_PATTERN.matcher(text);
		String txtLink = null;
		while (matcher.find()) {
			txtLink = matcher.group(0);
			matcher.appendReplacement(result, String.format(
					"<a href='%s' target='_blank'>%s</a>", txtLink, txtLink));
		}
		matcher.appendTail(result);
		return result.toString();
	}

	public static String parseSMSMax160(String content) {
		if (content == null)
			return null;
		String[] words = content.split(" ");
		StringBuilder retContent = new StringBuilder();
		int length = 0;
		;
		int i = 0;
		do {
			retContent.append(words[i]);
			retContent.append(" ");
			length += words[i].length() + 1;
			i++;
		} while (i < words.length && (length + words[i].length() <= 160));
		return retContent.toString().trim();
	}

	public static ArrayList<String> parseListSMSMax160(String content) {
		if (content == null) {
			return null;
		} else {
			content = content.trim();
			ArrayList<String> arraySMS = new ArrayList<String>();
			String tempSMS;
			while (content.length() > 0) {
				tempSMS = parseSMSMax160(content);
				arraySMS.add(tempSMS);
				content = content.substring(tempSMS.length()).trim();
			}
			return arraySMS;
		}
	}

	/**
	 * Chuyen 84 --> 0
	 * 
	 * @param phone
	 * @return
	 */
	public static String beautifyPhone(String phone) {
		if (isNullOrEmpty(phone)) {
			return "";
		}
		if (phone.startsWith("84")) {
			return "0" + phone.substring(2);
		}
		if (phone.startsWith("+84")) {
			return "0" + phone.substring(3);
		}
		return phone;
	}

	public static String shortenContent(String s, int maxLength) {
		if (s.length() > maxLength) {
			return s.substring(0, maxLength - 3) + "...";
		} else {
			return s;
		}
	}

	public static String adjustPhone(String phone) {
		if (StringUtil.isNullOrEmpty(phone)) {
			return "";
		}
		String[] lstChar = { "-", ".", " " };
		int first = 0;
		int last = 0;
		for (String ch : lstChar) {
			first = phone.indexOf(ch);
			last = phone.lastIndexOf(ch);
			if (first > 0 && last < phone.length() - 1) {
				phone = phone.replace(ch, "");
			}

		}
		return phone;
	}

	public static String removeNonWordCharacter(String str) {
		if (isNullOrEmpty(str))
			return "";
		return PATTERN_NONE_WORD.matcher(str).replaceAll("");
	}

	public static String generateHash(String input, String salt)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// SHA or MD5
		MessageDigest md = MessageDigest.getInstance("MD5");
		String hash = "";

		if (salt == null) {
			salt = "";
		}

		input += salt;
		byte[] data = input.getBytes("US-ASCII");

		md.update(data);
		byte[] digest = md.digest();
		for (int i = 0; i < digest.length; i++) {
			String hex = Integer.toHexString(digest[i]);
			if (hex.length() == 1)
				hex = "0" + hex;
			hex = hex.substring(hex.length() - 2);
			hash += hex;
		}

		return hash;
	}

	public static String converClobToString(Object input) throws Exception {
		Clob abc = (Clob) input;

		if (null == abc) {
			return null;
		} else {
			return convertStreamToString(abc.getCharacterStream());
		}
	}

	public static String convertObjectToString(Object input) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		if (null == input) {
			return null;
		} else if (Timestamp.class.isInstance(input)) {
			return sdf.format(((Timestamp) input).getTime());
		} else if (BigDecimal.class.isInstance(input)) {
			return ((BigDecimal) input).toString();
		} else if (Integer.class.isInstance(input)) {
			return ((Integer) input).toString();
		} else if (Double.class.isInstance(input)) {
			return ((Double) input).toString();
		} else if (Date.class.isInstance(input)) {
			return sdf.format(((Date) input));
		} else if (java.sql.Date.class.isInstance(input)) {
			return sdf.format(((Date) input));
		} else if (Boolean.class.isInstance(input)) {
			// return ((Boolean) input) ? "1" : "0";
			return ((Boolean) input).toString();
		} else if (Byte.class.isInstance(input)) {
			return ((Byte) input).toString();
		} else if (Clob.class.isInstance(input)) {
			Clob abc = (Clob) input;
			return convertStreamToString(abc.getCharacterStream());
		} else {
			try {
				return input.toString();
			} catch (Exception ex) {
				throw new Exception(
						"Can't not get type of output object. Object value : "
								+ input.toString());
			}
		}
	}

	public static String convertStreamToString(Reader reader)
			throws IOException {
		if (reader != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				reader.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}
	
	public static String getMD5Hash(String input, String salt) {
		MessageDigest digest;

		try {
			if (salt != null) {
				input = salt.trim() + input;
			}
			digest = MessageDigest.getInstance("MD5");
			digest.update(input.getBytes());
			final byte[] hash = digest.digest();
			final StringBuilder result = new StringBuilder(hash.length);
			for (int i = 0; i < hash.length; i++) {
				result.append(Integer.toString((hash[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			return result.toString();
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	
	public static String convertPhone(String countryCode,String phone){
		String out= "" ;
		if (phone.startsWith("+")) {
			phone = phone.replaceAll("[^0-9+]", "");
			out=phone;
		}
		else{
			phone = phone.replaceAll("[^0-9]", "");
			if (phone.startsWith("0")) {
				out = "+" +countryCode+  phone.substring(1);
			}else{
				out = "+" +countryCode+  phone;
			}
		}
		
		return out;
		
		
	}
	public static void main(String[] arg) throws Exception {
//		System.out.println(generateHash("1234", "bcbcaa@gmail.com"));
		
		System.out.println(convertPhone("1","055555()---5555555"));
	}
}
