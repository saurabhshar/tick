package com.sol.vo;

public class Stats {

	private final double avg;
	private final double max;
	private final double min;
	private final long count;

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

	public double getMin() {
		return min;
	}

	public long getCount() {
		return count;
	}
}
