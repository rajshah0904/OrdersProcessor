package processor;
import java.util.*;
import java.io.*;
import java.text.*;

public class OrdersProcessor {
	public static void main(final String[] args) {
		Scanner in = new Scanner(System.in);
		boolean threads = false;
		TreeMap<Integer, TreeMap<String, Integer>> orderData= new TreeMap<Integer, TreeMap<String, Integer>>();
		TreeMap<String, Double> itemPrice = new TreeMap<String, Double>();
		Thread thread = null;
		System.out.print("Enter item's data file name: ");
		String itemPriceFile = in.next();
		System.out.print("Enter \'y\' for multiple threads, any other character otherwise:");
		String multiT = in.next();
		if(multiT.equals("y")) {
			threads = true;
		}
		System.out.print("Enter number of orders to process: ");
        int numOrders = in.nextInt();
        in.nextLine();
        System.out.print("Enter order's base filename: ");
        String orderFileName = in.next();
        System.out.print("Enter result's filename: ");
        String resultsFileName = in.next();
        long startTime = System.currentTimeMillis();
        try {
            FileReader dataFile = new FileReader(itemPriceFile);
            BufferedReader buffData = new BufferedReader(dataFile);
            String line;
            while (buffData.ready()) {
                String[] split = buffData.readLine().split(" ");
                itemPrice.put(split[0], Double.valueOf(split[1]));
            }
        } catch (IOException e) {
            e.getMessage();
        }
        StringBuilder orderFileBuilder = new StringBuilder(orderFileName);
        TreeMap<String, Integer> totalItemsSold = new TreeMap<String, Integer>();
        orderFileBuilder.append("0.txt");
        if (!threads) {
        	for(int i = 1; i <= numOrders; i++) {
        		try {
        			orderFileBuilder.replace(orderFileName.length(), orderFileBuilder.length(), String.valueOf(i) + ".txt");
        			Scanner fileScan = new Scanner(new BufferedReader(new FileReader(orderFileBuilder.toString())));
                    fileScan.next();
                    int clientId = fileScan.nextInt();
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
        } else {
        	ArrayList<Thread> threadList = new ArrayList<Thread>();
        	for(int i = 1; i <= numOrders; i++) {
        		orderFileBuilder.replace(orderFileName.length(), orderFileBuilder.length(), String.valueOf(i) + ".txt");
        		OrderRunnable order = new OrderRunnable(orderFileBuilder.toString(), orderData, totalItemsSold);
        		Thread orderThread = new Thread(order);
        		threadList.add(orderThread);
        	}
        	try {
        		for(Thread t : threadList) {
    				t.start();
    			}
    			for(Thread t : threadList) {
    				t.join();
    			}
    		} catch(InterruptedException e) {
    			e.getMessage();
    		}
        }
        StringBuilder results = new StringBuilder();
        Set<Integer> idSet = orderData.keySet();
        System.out.print(itemPrice.keySet());
        System.out.print(idSet);
        for (Integer id : idSet) {
            Set<String> items = orderData.get(id).keySet();
            results.append("----- Order details for client with Id: ").append(id).append(" -----\n");
            double clientCost = 0.0;
            for (String item : items) {
                String pricePerItem = NumberFormat.getCurrencyInstance().format(itemPrice.get(item));
                int quantity = orderData.get(id).get(item);
                clientCost += quantity * itemPrice.get(item);
                String itemTotalCost = NumberFormat.getCurrencyInstance().format(itemPrice.get(item) * quantity);
                results.append("Item's name: ");
                results.append(item);
                results.append(", Cost per item: ");
                results.append(pricePerItem);
                results.append(", Quantity: ");
                results.append(orderData.get(id).get(item));
                results.append(", Cost: ");
                results.append(itemTotalCost + "\n");
            }
            results.append("Order Total: ");
            results.append(NumberFormat.getCurrencyInstance().format(clientCost));
            results.append("\n");
        }
        System.out.print(results.toString());
        StringBuilder finalString = new StringBuilder();
        double grandTotal = 0.0;
        finalString.append("***** Summary of all orders *****\n");
        for (String item : totalItemsSold.keySet()) {
            double total = totalItemsSold.get(item) * itemPrice.get(item);
            grandTotal += total;
            finalString.append("Summary - Item's name: " + item);
            finalString.append(", Cost per item: " + NumberFormat.getCurrencyInstance().format(itemPrice.get(item)));
            finalString.append(", Number sold: "+ totalItemsSold.get(item));
            finalString.append(", Item's Total: " + NumberFormat.getCurrencyInstance().format(total) + "\n");
        }
        finalString.append("Summary Grand Total: " + NumberFormat.getCurrencyInstance().format(grandTotal)+"\n");
        try {
            FileWriter write = new FileWriter(resultsFileName, false);
            BufferedWriter output = new BufferedWriter(write);
            PrintWriter output1 = new PrintWriter(output);
            output1.println(results.toString());
            output1.println(finalString.toString());
            output1.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Processing time (msec): " + (endTime - startTime));
        System.out.println("Results can be found in the file: " + resultsFileName);
	}
}
