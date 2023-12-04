package processor;

import java.util.*;
import java.io.*;
import java.text.*;

public class OrderRunnable implements Runnable{
	private String fileName;
	private TreeMap<Integer, TreeMap<String, Integer>> orderData;
	private TreeMap<String, Integer> totalItemsSold;
	private int clientId;
    
    public OrderRunnable(String file, TreeMap<Integer, TreeMap<String, Integer>> orderData, TreeMap<String, Integer> totalItemsSold) {
    	this.fileName = file;
    	this.orderData = orderData;
    	this.totalItemsSold = totalItemsSold;
    }
    
    @Override
    public void run() {
    	try {
            Scanner fileScan = new Scanner(new BufferedReader(new FileReader(fileName)));
            fileScan.next();
            this.clientId = fileScan.nextInt();
            synchronized(orderData) {
            	orderData.put(clientId, new TreeMap<String, Integer>());
            }
            while (fileScan.hasNextLine()) {
            	String item = fileScan.next();
            	fileScan.next();
                synchronized(orderData) {
                	if(orderData.get(clientId).containsKey(item)) {
                    	orderData.get(clientId).put(item, orderData.get(clientId).get(item)+1);
                    } else {
                    	orderData.get(clientId).put(item, 1);
                    }               	
                    if (totalItemsSold.containsKey(item)) {
                        totalItemsSold.put(item, totalItemsSold.get(item) + 1);
                    } else {
                        totalItemsSold.put(item, 1);
                    }
                }
            }
		} catch(IOException e) {
			e.getMessage();
		}
    }
}
