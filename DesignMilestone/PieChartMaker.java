package corenlp;
import java.util.ArrayList;
import java.util.HashMap;
import org.knowm.xchart.*;
import org.knowm.xchart.SwingWrapper;

/*
 * This is a class that creates a pie chart. 
 * We will have several graphic classes with similar functions. 
 * We plan to change the methods to take inputs (ie:
 * allow us to specify if we want to make a chart for positive, 
 * negative, or neutral sentiment) 
 */
public class PieChartMaker {

	String filename;
	FileIO io;

	/*
	 * Constructs a PieChartMaker object
	 */
	public PieChartMaker(String file) {
		this.filename = file;
		this.io = new FileIO(filename);

	}

	/*
	 * Simplifies the large csv file into an array with just the necessary info. 
	 * This array can be used to investigate sentiment by source. 
	 */
	public HashMap<String, Integer[]> makeSentSeriesArray(ArrayList<String[]> lines) {
		HashMap<String, Integer[]> series = new HashMap<>();
		for (String[] line : lines) {
			String source = line[0];
			int neg = Integer.valueOf(line[1].trim());
			int neut = Integer.valueOf(line[2].trim());
			int pos = Integer.valueOf(line[3].trim());
			// add each individual source into the hashmap, storing the pos, neg, and
			// neutral counts
			if (!series.containsKey(source)) {
				Integer[] posNegNeut = { neg, neut, pos };
				series.put(source, posNegNeut);
				System.out.println("added " + source + " to series hashmap");

			}

		}
		return series;
	}
	
	
	/*
	 * makes a chart showing the percent of positive sentiment that each source
	 * contributes to the corpus. 
	 */
	public void makeChart(HashMap<String, Integer[]> series) {
		PieChart chart = new PieChartBuilder().width(800).height(600).title("simple chart").build();
		System.out.println("built chart object");

		for (String source : series.keySet()) {
			Integer[] sents = series.get(source);
			int pos = sents[2];
			chart.addSeries(source, pos);

		}

		new SwingWrapper<PieChart>(chart).displayChart();

	}

	public static void main(String[] args) {
		PieChartMaker gm = new PieChartMaker("normalizedDTM.csv");
		ArrayList<String[]> lines = gm.io.DTMfileReader();
		HashMap<String, Integer[]> series = gm.makeSentSeriesArray(lines);
		gm.makeChart(series);

	}

}
