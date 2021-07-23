package com.sol.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

public final class Stats {

	/*@JsonIgnore
	private double sum = 0D;*/

	private double avg;
	private double max;
	private double min;
	private long count;

	// public Stats(double sum, double avg, double max, double min, int count) {
	public Stats(double avg, double max, double min, long count) {
		// this.sum = sum;
		this.avg = avg;
		this.max = max;
		this.min = min;
		this.count = count;
	}

	public double getAvg() {
		return avg;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public long getCount() {
		return count;
	}

	/*public double getSum() {
		return sum;
	}*/

}
