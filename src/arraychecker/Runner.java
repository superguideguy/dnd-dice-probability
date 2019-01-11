/*********************************************************************
* Copyright (c) 2019 superguideguy
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package arraychecker;

import java.math.BigInteger;

public class Runner {

	public static void main(String[] args) {
		BigInteger alpha = new BigInteger("12B98D11",16);
		BigInteger beta  = new BigInteger("0EFC03F2",16);
		MathFunctions.subtract(alpha, beta);
		ArrayChecker a = new ArrayChecker();
		Thread t = new Thread(a);
		t.start();
	}
	
}

enum RadioButtonMode {
	DETERMINISTIC,
	RANDOM
	;
}

class ArrayCheckerState {
	
	RadioButtonMode r;
	String[] db;
	String n;
	String[] list;
	
	ArrayCheckerState(RadioButtonMode r, String[] db, String n, String[] list) {
		this.r = r;
		this.db = db;
		this.n = n;
		this.list = list;
	}
	
	RadioButtonMode getR() {
		return r;
	}
	int[] getDB() {
		int[] ret = new int[db.length];
		for (int i = 0; i < db.length; i++) ret[i] = Integer.parseInt(db[i]);
		return ret;
	}
	int getN() {
		return Integer.parseInt(n);
	}
	int[] getList() {
		int[] ret = new int[list.length];
		for (int i = 0; i < list.length; i++) ret[i] = Integer.parseInt(list[i]);
		return ret;
	}
	
	boolean isVaild() {
		if (db.length != 4) return false;
		if (list[0].length() == 0) return false;
		String[] test = new String[db.length+list.length+1];
		for (int i = 0; i < db.length; i++) test[i] = db[i];
		for (int i = 0; i < list.length; i++) test[i+db.length] = list[i];
		test[db.length+list.length] = n;
		String pattern = ".*[^0-9].*";
		for (int i = 0; i < test.length; i++) if (test[i].matches(pattern)) return false;
		if (Integer.parseInt(db[0]) < Integer.parseInt(db[2])) return false;
		return true;
	}
	
	boolean isListValid() {
		int[] listI = this.getList();
		if (listI.length != this.getN()) return false;
		int lb = this.getDB()[2]					+ this.getDB()[3];
		int ub = this.getDB()[2] * this.getDB()[1]  + this.getDB()[3];
		for (int i = 0; i < listI.length; i++)
			if ( (listI[i] < lb) || (listI[i] > ub) ) return false;
		return true;
	}
	
	Boolean isValidAndEqual(ArrayCheckerState other) {
		if (!this.isVaild()) return null;
		if (!(other instanceof ArrayCheckerState)) return false;
		if ( (this.r.equals(other.r)) && (this.n.equals(other.n)) ) {
			if (this.db.length != other.db.length) return false;
			for (int i = 0; i < this.db.length; i++) if (!this.db[i].equals(other.db[i])) return false;
			return true;
		}
		return false;
	}
	
}

