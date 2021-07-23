package com.sol.vo;

public class Tick {

	String instrument;
	double price;
	long timestamp;

	public Tick() {
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Tick(String instrument, double price, long timestamp) {
		this.instrument = instrument;
		this.price = price;
		this.timestamp = timestamp;
	}

	public String getInstrument() {
		return instrument;
	}

	public double getPrice() {
		return price;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "{instrument=" + instrument + ", price=" + price + ", timestamp=" + timestamp + "}";
	}
}