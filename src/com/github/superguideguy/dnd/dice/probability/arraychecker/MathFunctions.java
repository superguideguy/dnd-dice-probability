/*********************************************************************
* Copyright (c) 2019 superguideguy
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package com.github.superguideguy.dnd.dice.probability.arraychecker;

import java.math.BigInteger;

public class MathFunctions {

	public static final long[] PDF = {
			0,
			0,
			0,
			1,
			4,
			10,
			21,
			38,
			62,
			91,
			122,
			148,
			167,
			172,
			160,
			131,
			94,
			54,
			21
	};
	
	public static final long[] CDF = {
			0,
			0,
			0,
			1,
			5,
			15,
			36,
			74,
			136,
			227,
			349,
			497,
			664,
			836,
			996,
			1127,
			1221,
			1275,
			1296	
	};
	
	private static long factorial(int n) {
		if (n == 0) return 1;
		if (n == 1) return 1;
		if (n == 2) return 2;
		if (n == 3) return 6;
		if (n == 4) return 24;
		if (n == 5) return 120;
		if (n == 6) return 720;
		else return n*factorial(n-1);
	}
	
	public static long multinomial(int num, int[] divCount) {
		long multiNum = factorial(num);
		long multiDiv = 1;
		for (Integer i : divCount) multiDiv *= factorial(i);
		return (multiNum/multiDiv);
	}
	
	public static String bufferLength(int n) {
		double alpha = Math.pow(172, n);
		double beta = (double) factorial(n);
		double logAlpha = Math.log10(alpha);
		double logBeta = Math.log10(beta);
		double log = logAlpha + logBeta;
		byte ret = (byte) (Math.floor(log)+1);
		String rets = "%" + ret + "d";
		return rets;
	}
	
	public static long capacityCalc(int r, int nl, int nu) {
		int n = nu - nl + 1;
		long ret = 1;
		for (int s = 0; s < r; s++) ret = Math.multiplyExact(ret, n+s);
		long div = factorial(r);
		return ret/div;
	}
	
	private static final char[] subtractLookUpTable = {
			'k',
			'l','m','n','o','p',
			'q','r','s','t','u',
			'v','w','x','y','z',
								'0',
									'1','2','3','4','5',
									'6','7','8','9','A',
									'B','C','D','E','F',
	};
	
	private static char subtractLookUp(char alpha, char beta) {
		String table = new String(subtractLookUpTable);
		int zeroIndex = table.indexOf('0');
		int a = table.indexOf(alpha);
		int b = table.indexOf(beta);
		if ((a < zeroIndex) || (b < zeroIndex)) return '?';
		return (subtractLookUpTable[a-b+zeroIndex]);
	}
	
	private static String leftPad(String toPad, int numPad) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < numPad; i++) s.append('0');
		s.append(toPad);
		String ret = new String(s);
		return ret;
	}
	
	/**
	 * NOTE: The subtrahend MUST be greater than the minuend. Do NOT use this method if this cannot be guaranteed.
	 * The minuend is allowed to be equal to the subtrahend.
	 * @param subtrahend 'a' in "a-b"
	 * @param minuend 'b' in "a-b"
	 * @return a-b
	 */
	public static BigInteger subtract(BigInteger subtrahend, BigInteger minuend) {
		String alpha = subtrahend.toString(16).toUpperCase();
		String beta = minuend.toString(16).toUpperCase();
		/*try {
            Thread.sleep(100);
        } catch (InterruptedException ignore) {}*/
		//System.out.println(alpha);
		int leftPad = alpha.length() - beta.length();
		beta = leftPad(beta,leftPad);
		//System.out.println(beta);
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < alpha.length(); i++) s.append(subtractLookUp(alpha.charAt(i),beta.charAt(i)));
		//String z = new String(s);
		//System.err.println(z);
		for (int i = s.length()-1; i >= 1; i--) if (Character.isLowerCase(s.charAt(i))){
			String table = new String(subtractLookUpTable);
			int a0 = table.indexOf(s.charAt(i-1));
			int a1 = table.indexOf(s.charAt(i));
			String str = "" + subtractLookUpTable[a0-1] + "" + subtractLookUpTable[a1+16];
			s.replace(i-1, i+1, str);
		}
		String ret = new String(s);
		//System.out.println(ret);
		/*try {
            Thread.sleep(100);
        } catch (InterruptedException ignore) {}*/
		return new BigInteger(ret,16);
	}
	
	public static final int[] costTable5e = {
			0, 0, 0, 0, 0, 0, 0, 0,
			0, 1, 2, 3, 4, 5, 7, 9
	};
	
	public static final int[] costTablePF = {
			-4, -4, -4, -4, -4, -4, -4,
			 	-4, -2, -1,  0,  1,  2,
			 	 3,  5,  7, 10, 13, 17
	};
	
}
