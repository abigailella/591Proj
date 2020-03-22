package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DTMNormalizer {

	String filename;
	String[] headers;
	
	public DTMNormalizer(String filename){
		this.filename = filename;
		
	}
	
	
	
	/*
	 * read the file into an arrayList, 
	 */
	public ArrayList<String[]> fileReader(String filename){
		int count = 0;
		File f = new File(filename);
		ArrayList<String[]> lines = new ArrayList<String[]>();
		
		try {
			Scanner fileParser = new Scanner(f);
			headers = fileParser.nextLine().split(",");
			//fileParser.useDelimiter("(Z1Q)");
			

			while (fileParser.hasNextLine()) {

			String[] thisLine = fileParser.nextLine().split(",");
			lines.add(thisLine);
				
				}
			
			
			}
		
	 catch (FileNotFoundException e) {
		System.out.println("ERROR - file nnot found. Please try again.");
		
		e.printStackTrace();
	}
		
		return lines;
		
	}
	
	public ArrayList<String[]> normalizeArticles(ArrayList<String[]> lines){
		//looping through each line and creating a wordcount for the article, 
		// and then normalizing each cell based off of that 
		for (String[] line : lines) {
			int wordCount = 0;
			for (int i = 1; i < line.length -1; i++) {
				wordCount += Integer.valueOf(line[i]);
			}
			//System.out.println("wordcount is: " + wordCount);
			for (int i = 1; i < line.length -1; i++) {
				double newValue = (Double.valueOf(line[i]) / (double) wordCount); 
				//System.out.println("new value is: " + newValue);
				line[i] = String.valueOf(newValue);
			}	
			
		}
		
		return lines;
	}
	


	public void fileWriter(ArrayList<String[]> lines) {
		
		String outFileName = "normalizedDTM.csv";
		try {
			FileWriter fw = new FileWriter(outFileName);
			BufferedWriter bw = new BufferedWriter(fw);
			String headerString = Arrays.toString(headers);
			headerString = headerString.replace("[", "");
			headerString = headerString.replace("]", "");
			bw.write(headerString);
			bw.newLine();
			
			for (String[] line : lines) {
				String lineString = Arrays.toString(line);
				lineString = lineString.replace("[", "");
				lineString = lineString.replace("]", "");
				bw.write(lineString);
				bw.newLine();
			}
			
		bw.close();	
		
		}
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void normalizeDTM() {
		
		ArrayList <String[]> lines = fileReader(filename);
		ArrayList <String[]> normalizedArticles = normalizeArticles(lines);
		fileWriter(normalizedArticles);
		
	}
	
	
	
	
	//main method
	
	public static void main(String[] args) {
		DTMNormalizer dtm = new DTMNormalizer("newsSourcesOut.csv");
		dtm.normalizeDTM();
	}
	
	
}
	
	
	
	
	
	
	



		
