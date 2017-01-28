package com.lastminute.salestaxes;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Scanner;

public class SalesTaxes {

	public static void main(String args[]) throws Exception
	{     
		float salesTaxes = 0;
		float price;
		float total = 0;
		int quantity = 0;
		boolean isImported = false;
		boolean isExempt = false;

		Charset encoding = Charset.forName("UTF-8");
		Path path = Paths.get(ClassLoader.class.getResource("/Exempt.txt").toURI());
		String exempt = new String(Files.readAllBytes(path), encoding);

		Path input = Paths.get(ClassLoader.class.getResource("/Input.txt").toURI());
		String line="";
		DecimalFormat df = new DecimalFormat("0.00");
		Scanner fileScanner = new Scanner(input);

		while (fileScanner.hasNextLine()) {
			while (fileScanner.hasNext()) { 
				if (fileScanner.hasNextInt()) { 
					quantity = fileScanner.nextInt();
					line+= quantity;
				}
				else if(fileScanner.hasNext()){
					String wordScanned = fileScanner.next();
					if(wordScanned.matches("at")){
						line+=": ";
					}else if(wordScanned.matches("\\d+.\\d+")){
						price = Float.parseFloat(wordScanned);
						float tax = 0;
						if(isImported){
							line = line.replaceFirst(""+ quantity, quantity + " imported");
							tax += Math.round((price * 5 / 100) * 20) / 20.0;   
						}
						if(!isExempt){
							tax += Math.round((price * 10 / 100) * 20) / 20.0;  
						}
						salesTaxes+=tax;
						total+=price+tax;
						line += df.format(price+tax);
						System.out.println(line);
						isImported=false;
						isExempt = false;
						line = "";
					}
					else{
						if(wordScanned.equalsIgnoreCase("imported")){
							isImported = true;
						}else{
							if(exempt.indexOf(wordScanned) != -1){
								isExempt = true;
							}
							line += " " + wordScanned;
						}
					}
				}
			}                    
		}
		System.out.println("Sales Taxes: "+df.format(salesTaxes));
		System.out.println("Total: "+df.format(total));
		fileScanner.close();
	}
}
