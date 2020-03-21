import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Properties;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

//test comment
public class DocumentTermMatrix {
    File fileToOpen = new File("newsSources.csv");
    File fileToWrite = new File ("newsSourcesOut.csv");
    PrintWriter pw = new PrintWriter(fileToWrite);
    int idxTitle = 1, idxArticle = 2, idxCategory = 0;

    ArrayList<String[]> fileArray = new ArrayList<>();
    ArrayList<String> titleArray = new ArrayList<>();
    ArrayList<String> categoryArray = new ArrayList<>();
    TreeMap<String, Integer> templateMap = new TreeMap<>();
    ArrayList<TreeMap> printArray = new ArrayList<>();
    Properties prop = new Properties();
    StanfordCoreNLP pipeline = new StanfordCoreNLP(property());

    int loopCounter = 0;


    public DocumentTermMatrix() throws FileNotFoundException {
    }

    /**
     * Set property for NLP pipeline
     * @return
     */
    private Properties property(){
        prop.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
        return prop;
    }


    /**
     * Construct a TreeMap for each article (Sting: Word -> Integer: Frequency Count)
     */
    public void populateEmbeddings() {
        constructFileArray(fileToOpen);
        System.out.println("file opened successfully");
        makeTemplateMap();
        System.out.println("template file created successfully");
        printHeader(templateMap);
        System.out.println("header printed");
        String category;

//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 3; j++) {
//                System.out.print(i + " : " + j + ":::");
//                System.out.println(fileArray.get(i)[j]);
//
//            }
//
//        }


        for (String[] article : fileArray) {
            //Store FAKE or REAL label
            try {
                categoryArray.add(article[idxCategory].trim());
                category = article[idxCategory].trim();
                System.out.println("category array created successfully");
            } catch (Exception e) {
                continue;
            }

            System.out.println("Title: " + article[idxTitle].trim());
            System.out.println("Source: " + category);
            System.out.println("Article: " + article[idxArticle]);
            System.out.println();
            //Just to see progress while the loop is running
            loopCounter++;
//            System.out.println(loopCounter);
            if (loopCounter == 2) break;

            //Store embeddings in a treemap
            TreeMap<String, Integer> rowMap = new TreeMap<>(templateMap);
            CoreDocument document = new CoreDocument(article[idxArticle]);
            pipeline.annotate(document);
            for (int i = 0; i < document.tokens().size(); i++) {
                try {
                    String word = document.tokens().get(i).lemma().toLowerCase();
                    if (word.matches("\\w+")) rowMap.put(word, rowMap.get(word) + 1);
                } catch (Exception e) {
                    continue;
                }
            }

            //Store sentiment count
            TreeMap<String, Integer> sentMap = new TreeMap<>();
            sentMap.put("Positive", 0);
            sentMap.put("Neutral", 0);
            sentMap.put("Negative", 0);

            for (int i = 0; i < document.sentences().size(); i++) {
                try {
                    String sentiment = document.sentences().get(i).sentiment();
//                    System.out.println(sentiment);
                    sentMap.put(sentiment, sentMap.get(sentiment) + 1);
                } catch (Exception e) {
                    continue;
                }
            }
//            System.out.println(sentMap.get("Positive"));


            //Output a new row in file
            printRow(rowMap, category);

        }
        pw.close();

    }

    /**
     * Read contents of file into an array
     * @param file
     */
    private void constructFileArray(File file) {
        try {
            Scanner fileInput = new Scanner(file);
            fileInput.useDelimiter("(Z1Q\")|(Z1Q)");

            //File to Array
            int loopCounter = 0;
            while (fileInput.hasNext()) {
                String[] rawText = fileInput.next().split("(,Q1Z)|(,\"Q1Z)");
                fileArray.add(rawText);
                loopCounter++;
                if (loopCounter == 3) break;



            }
        } catch (FileNotFoundException e) {
            System.out.println("document not found");
        }

    }

    /**
     * Create a map with all distinct words in all document
     */
    private void makeTemplateMap() {
        //Get all distinct words
        for (String[] row : fileArray) {
            CoreDocument document = null;
            try {
                document = new CoreDocument(row[idxArticle]);
            } catch (Exception e) {
                continue;
            }
            pipeline.annotate(document);

            for (int i = 0; i < document.tokens().size(); i++) {
                try {
                    String word = document.tokens().get(i).lemma().toLowerCase();
                    if (word.matches("\\b[a-zA-Z]+")) templateMap.put(word, 0);
                } catch (Exception e) {
                    continue;
                }
            }

        }
    }

    /**
     * Print each row of word embeddings from treemap created for each article.
     * @param tMap Treemap of embeddings
     * @param cat Fake or Real
     */
    private void printRow(TreeMap tMap, String cat){
        pw.print(cat);
        tMap.values().forEach(value -> pw.print("," + value));
        pw.println();
        pw.flush();

    }

    /**
     * Print Header from template map
     * @param tMap
     */
    private void printHeader(TreeMap tMap){
        pw.print("Category");
        tMap.keySet().forEach(key -> pw.print("," + key) );
        pw.println();
        pw.flush();

    }



    public static void main(String[] args) throws FileNotFoundException {
        DocumentTermMatrix dtm = new DocumentTermMatrix();

        dtm.populateEmbeddings();
    }

}
