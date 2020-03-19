import weka.*;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.io.IOException;

public class CSV2Arff {
  
/**
   * constructor takes 2 arguments:
   * CSV input file (string of filename)
   * ARFF output file (string of filename)
   */
	String sourceCSV;
	String finalArff;
	CSVLoader loader;
	Instances data;
	ArffSaver saver;
	 
	 public CSV2Arff(String csv, String arff) throws IOException {
		 this.sourceCSV = csv;
		 this.finalArff = arff;
		 this.loader = new CSVLoader();
		 loader.setSource(new File(sourceCSV));
		 this.data = loader.getDataSet();
		 this.saver = new ArffSaver();
		 
	 }
	 
	 
	 /*
	  * writes and saves the contents to the csv to an arff format 
	  */
	 public void saveArff() throws IOException {
		 saver.setInstances(data);
		 saver.setFile(new File(finalArff));
		 saver.writeBatch();
		 System.out.println(".arff file has been created.");
	 }
	
  public static void main(String[] args) throws Exception {
    
CSV2Arff test = new CSV2Arff("cleanedTestData.csv", "cleanedTest.arff");
test.saveArff();

  }
}