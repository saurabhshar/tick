package com.sol.work;

import java.util.concurrent.ConcurrentHashMap;

public class Main {

	public static void main(String[] args) {
		ConcurrentHashMap<String, Integer>
        map = new ConcurrentHashMap<>();
    map.put("Book1", 10);
    map.put("Book2", 500);
    map.put("Book3", 400);

    // print map details
    System.out.println("ConcurrentHashMap: "  + map.toString());

    // remap the values of ConcurrentHashMap
    // using compute method
    map.compute("Book4", (key, val) -> val==null?10:20);
    System.out.println(map);
	}
}
