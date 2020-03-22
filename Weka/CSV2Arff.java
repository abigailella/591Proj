import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.File;

public class CSV2Arff {
  /**
   * takes 2 arguments:
   * - CSV input file
   * - ARFF output file
   */
  public static void main(String[] args) throws Exception {
    // load CSV
    CSVLoader loader = new CSVLoader();
    loader.setSource(new File("normalizedDTM.csv"));
    Instances data = loader.getDataSet();

    // save ARFF
    ArffSaver saver = new ArffSaver();
    saver.setInstances(data);
    saver.setFile(new File("normalizedDTM.arff"));
    saver.setDestination(new File("normalizedDTM.arff"));
    saver.writeBatch();
    System.out.println("arff file has been created.");
  }
}