package com.demo.netty.util;


import org.apache.commons.lang3.StringUtils;

public class OTUMsgEncryptor {

	/*private byte[] cycleCodes = new byte[] {
			'0','1','2','3','4','5','6','7','8','9',
			'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
			' ','!','"','#','$','%','&','\'','*','+',',','-','.','/',
			':',';','<','=','>','?','@',
			'[','\\',']','^','_','`',
			'{','|','}','~'
	};*/
	private byte[] cycleCodes = new byte[] { 'G', 'H', 'I', 'J', '&', '<', '2', '8', 'd', '#', 'N', 'i', 'X', 's', '=', '5', 'R', '0', '$', 'e', '4', 'Q', '%', '[', 'j', 'p', ']', 'c', '1', '3', '7',
			'9', 'A', '6', 'n', 'z', 'B', ';', 'h', 'r', ':', 'a', '_', 'O', '{', 'D', 'E', 'm', 'W', 'Y', 'k', '}', 'x', 'Z', 'P', 'u', ',', 'F', 'M', 'g', 'C', 'K', 'f', 't', '+', '>', 'L', 'S',
			'T', 'U', 'V', 'q', '|', 'w', 'l', 'y', 'b', 'o', 'v', '.', };
	private byte[] cycleIndex = new byte[128];

	public OTUMsgEncryptor() {
		for (int index = 0; index < cycleIndex.length; index++) {
			cycleIndex[index] = -1;
		}
		for (int index = 0; index < cycleCodes.length; index++) {
			cycleIndex[cycleCodes[index]] = (byte) index;
		}
	}

	public String asiccCrcEncode(String context, int offset) {
		if (StringUtils.isBlank(context)) {
			return null;
		}

		byte[] bytes = context.getBytes();
		for (int index = 0; index < bytes.length; index++) {
			int value = bytes[index];
			if (cycleIndex[value] >= 0) {
				int nextIndex = (cycleIndex[value] + index + offset) % cycleCodes.length;
				bytes[index] = cycleCodes[nextIndex];
			}
		}

		return new String(bytes);
	}

	public String asiccCrcDecode(String context, int offset) {
		if (StringUtils.isBlank(context)) {
			return null;
		}
		byte[] bytes = context.getBytes();
		for (int index = 0; index < bytes.length; index++) {
			int value = bytes[index];

			if (cycleIndex[value] >= 0) {
				int nextIndex = (cycleIndex[value] - index - offset) % cycleCodes.length;
				nextIndex = (cycleCodes.length + nextIndex) % cycleCodes.length;
				bytes[index] = cycleCodes[nextIndex];
			}
		}

		return new String(bytes);
	}
}
