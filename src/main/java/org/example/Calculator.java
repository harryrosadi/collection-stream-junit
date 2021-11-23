package org.example;

public class Calculator {

	public Integer divide(int satu, int dua) {
		if (dua == 0){
			throw  new IllegalArgumentException("tidak bisa dibagi nol");
		}else {
			return satu/dua;
		}
	}
	public Integer add(int satu, int dua){
		return satu + dua;
	}
}
