package main;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import corenlp.FileIO;

/*
 * This class reads in the DocumentTermMatrix and and normalizes
 * wordcounts on an article by article basis. 
 */
public class DTMNormalizer {

	String filename;
	String[] headers;
	FileIO io = new FileIO(filename);
	
	/*
	 * constructor for the class 
	 */
	public DTMNormalizer(String filename){
		this.filename = filename;
		
	}
	
	
	/*
	 * Normalizes the DTM
	 */
	public ArrayList<String[]> normalizeArticles(ArrayList<String[]> lines){
		//looping through each line and creating a word count for the article, 
		// and then normalizing each cell based off of that 
		for (String[] line : lines) {
			int wordCount = 0;
			for (int i = 4; i < line.length -1; i++) {
				wordCount += Integer.valueOf(line[i]);
			}
			for (int i = 4; i < line.length -1; i++) {
				double newValue = (Double.valueOf(line[i]) / (double) wordCount);
				line[i] = String.valueOf(newValue);
			}	
			
		}
		
		return lines;
	}
	

	/*
	 * writes the normalized DTM to a csv file 
	 */
	public void NormalizedDTMFileWriter(ArrayList<String[]> lines) {
		
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
		
		ArrayList <String[]> lines = io.DTMfileReader();
		ArrayList <String[]> normalizedArticles = normalizeArticles(lines);
		NormalizedDTMFileWriter(normalizedArticles);
		
	}
	
	
	
	
	//main method
	
	public static void main(String[] args) {
		DTMNormalizer dtm = new DTMNormalizer("newsSourcesOut.csv");
		dtm.normalizeDTM();
	}
	
	
}
	
	
	
	
	
	
	



		
