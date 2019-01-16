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
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class ArrayDeterministicGenerator {
	
	private static long lines = 0;
	
	public static long[] PDF = MathFunctions.PDF;
	
	private static BigInteger x(int a) {
		long x = PDF[a];
		String y = Long.toString(x);
		BigInteger z = new BigInteger(y);
		return z;
	}
	
	private static void looper(LinkedHashMap<Integer[],BigInteger> map, int[] statArray, int currentIndex,int lowerBound, int upperBound){
		if (currentIndex == statArray.length) {
			int[] count = new int[upperBound+1];
			for (int i = 0; i < statArray.length; i++) count[statArray[i]]++;
			BigInteger b = BigInteger.ONE;
			for (int i = 0; i < statArray.length; i++) b = b.multiply(x(statArray[i]));
			b = b.multiply(new BigInteger(Long.toString(MathFunctions.multinomial(statArray.length, count))));
			
			StringBuilder s = new StringBuilder();
			for (int i = 0; i < statArray.length; i++) s.append("%2d,");
			s.deleteCharAt(s.length()-1);
			s.append(":\t" + MathFunctions.bufferLength(statArray.length) + "\n");
			String format = s.toString();
			Object[] args = new Object[statArray.length+1];
			for (int i = 0; i < statArray.length; i++) args[i] = statArray[i];
			args[statArray.length] = b;
			//System.out.printf(format, args);
			
			Integer[] statArray2 = Arrays.stream(statArray).boxed().toArray(Integer[]::new);
			map.put(statArray2, b);
			lines++;
			ArrayChecker.linesProcessedOuter++;
			ArrayChecker.linesProcessedInner++;
			ArrayChecker.updateProgressBars();
		}
		else for (statArray[currentIndex]=lowerBound; statArray[currentIndex]<=upperBound; statArray[currentIndex]++)
			looper(map,statArray,currentIndex+1,statArray[currentIndex],upperBound);
	}
	
	/**
	 * Generates a LinkedHashMap that represents the probability (value) of getting a specific stat array (key).
	 * <p>
	 * 
	 * <p>
	 * This function runs in O((upperBound-lowerBound+1)^numberOfStats) time.
	 * <p>
	 * @param numberOfStats		The number of statistics rolled for, and thus also the length of the Integer[] key.
	 * @param lowerBound		The lower bound for each statistic, inclusive. This assumes all statistics have the same lower bound.
	 * @param upperBound		The upper bound for each statistic, inclusive. This assumes all statistics have the same upper bound.
	 * @return 
	 */
	public static LinkedHashMap<Integer[],BigInteger> arrayProbability(int numberOfStats, int lowerBound, int upperBound) {
		Instant alpha = Instant.now();
		
		int[] statArray = new int[numberOfStats];
		LinkedHashMap<Integer[],BigInteger> lhm = new LinkedHashMap<Integer[],BigInteger>(
				(int) MathFunctions.capacityCalc(numberOfStats, lowerBound, upperBound));
		looper(lhm,statArray,0,lowerBound,upperBound);
		
		Instant omega = Instant.now();
		long t = Duration.between(alpha, omega).toMillis();
		//System.out.println("Time (milliseconds): " + t);
		//System.out.println("Lines: " + lines);
		lines = 0;
		return lhm;
	}
	
	private static void pdfG_loop(long[] values, int[] ijkl, int currentIndex, int lowerBound, int upperBound, int b, int c) {
		if (currentIndex == ijkl.length) {
			Integer[] ijkl2 = Arrays.stream(ijkl).boxed().toArray(Integer[]::new);
			Arrays.sort(ijkl2);
			int n = c;
			for (int i = 0; i < b; i++) n += ijkl2[ijkl.length-1-i];
			values[n]++;
		} else for (ijkl[currentIndex]=lowerBound;ijkl[currentIndex]<=upperBound;ijkl[currentIndex]++)
			pdfG_loop(values,ijkl,currentIndex+1,lowerBound,upperBound,b,c);
	}
	
	public static void pdfGenerator(String arg) {
		//Expects AdXkB+C
		int a,x,b,c;
		String[] vals = arg.split("[dk+]");
		//System.out.println(Arrays.deepToString(vals));
		a = Integer.parseInt(vals[0]);
		x = Integer.parseInt(vals[1]);
		b = Integer.parseInt(vals[2]);
		c = Integer.parseInt(vals[3]);
		
		long[] values = new long[x*b+c+1];
		int[] ijkl = new int[a];
		pdfG_loop(values,ijkl,0,1,x,b,c);
		//System.out.println(Arrays.toString(values));
		PDF = values;
	}
	
	public static void pdfGenerator(int[] db) {
		int a,x,b,c;
		a = (db[0]);
		x = (db[1]);
		b = (db[2]);
		c = (db[3]);
		
		long[] values = new long[x*b+c+1];
		int[] ijkl = new int[a];
		pdfG_loop(values,ijkl,0,1,x,b,c);
		//System.out.println(Arrays.toString(values));
		PDF = values;
	}
	
	public static void main(String[] args) {
		pdfGenerator("4d6k3+0");
	}
	
}
