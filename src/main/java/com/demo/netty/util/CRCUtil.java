package com.demo.netty.util;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;


public class CRCUtil {
	public static void main(String args[]) throws Exception {
		System.err.println(asiccCrcDecode("*a2|n#bhXz+;a0iam%5D0p]cv7Zu$Cnz2;+K:c"));
		System.err.println(asiccCRC("vZ:I,.1HqJu2dMS#Ki5G_l0efjv.kS"));
	}

	public static String getCRCByteStr(String reponseStr) {
		return "*" + calCrc(reponseStr) + "|";
	}

	public static String calCrc(String command) {
		int result = 0;
		for (byte value : command.getBytes()) {
			result = (result + value & 0xff) & 0xff;
		}
		return toHextString(result);
	}

	private static String toHextString(int result) {
		String rrString = Integer.toHexString(result & 0X000000FF);
		if (1 == rrString.length()) {
			rrString = "0" + rrString;
		}
		return rrString;
	}

	public static boolean isValidCRC(String text, String crc) throws Exception {
		return asiccCRC(text).equals(crc);
	}

	public static String asiccCRC(String text) throws Exception {
		if (null == text || StringUtils.isBlank(text)) {
			return "00";
		}
		byte[] bytes = text.getBytes();
		int value = 0;
		for (byte b : bytes) {
			value += b;
		}
		return getCRCByteHexValue(value);
	}

	public static String getCRCByteHexValue(int bv) {
		bv = bv & 0xff;
		if (bv >= 0x10) {
			return Integer.toHexString(bv);
		} else {
			return "0" + Integer.toHexString(bv);
		}
	}

	public static String getCRCShortHexValue(int bv) {
		bv = bv & 0xffff;
		if (bv >= 0x1000) {
			return Integer.toHexString(bv);
		} else if (bv >= 0x100) {
			return "0" + Integer.toHexString(bv);
		} else if (bv >= 0x10) {
			return "00" + Integer.toHexString(bv);
		}
		return "000" + Integer.toHexString(bv);
	}

	public static String getCRCIntHexValue(long bv) {
		bv = bv & (long) 0X00000000ffffffff;
		return Integer.toHexString((int) bv);
	}

	public static String isEncoded(String context) {
		if (StringUtils.isBlank(context) || context.startsWith("*")) {
			return null;
		} //认为是加密的
		return context.substring(1);
	}

	public static String asiccCrcEncode(String context) {
		if (StringUtils.isBlank(context)) {
			return null;
		}
		String hexEncry = context.substring(1, 3);
		String contetn = context.substring(4);
		return MessageFormat.format("*{0}|{1}", hexEncry, asiccCrcEncode(contetn, hexEncry));
	}

	public static String asiccCrcDecode(String context) {
		if (StringUtils.isBlank(context)) {
			return null;
		}
		String hexEncry = context.substring(1, 3);
		String contetn = context.substring(4);
		return MessageFormat.format("*{0}|{1}", hexEncry, asiccCrcDecode(contetn, hexEncry));
	}

	static OTUMsgEncryptor se = new OTUMsgEncryptor();

	public static String asiccCrcEncode(String context, int offset) {
		return se.asiccCrcEncode(context, offset);
	}

	public static String asiccCrcDecode(String context, int offset) {
		return se.asiccCrcDecode(context, offset);
	}

	public static String asiccCrcEncode(String context, String hex) {
		return asiccCrcEncode(context, Integer.valueOf(hex,16));
	}

	public static String asiccCrcDecode(String context, String hex) {
		return asiccCrcDecode(context, Integer.valueOf(hex,16));
	}

	public static boolean validateCRC(String in, String[] reqArr) {
		String crc = reqArr[0].substring(1);
		String body = in.split("\\" + reqArr[0] + "\\|")[1];
		byte[] b = body.getBytes();
		byte crcByte = 0;
		for (int j = 0; j < b.length; ++j) {
			crcByte += b[j];
		}
		if (!getCRCByteHexValue(crcByte).equals(crc)) {
			return false;
		}
		return true;
	}
}
