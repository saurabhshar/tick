package com.sol.vo;

public class Stats {

	private double avg;
	private double max;
	private double min;
	private long count;

	public Stats() {
	}

	public Stats(double avg, double max, double min, long count) {
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
}
