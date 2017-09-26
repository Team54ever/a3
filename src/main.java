import org.apache.lucene.search.IndexSearcher;

import lucene.*;
import treccar.*;


public class main {
	
	
	static String TEST200_DIR = "/Users/Nithin/Desktop/test200/train.test200.cbor.paragraphs";
	static String INDEX_DIR = "/Users/Nithin/Desktop/Path";
	static String OUTLINES_DIR = "/Users/Nithin/Desktop/test200/train.test200.cbor.outlines";
	static String OUTPUT_FILE_PATH = "/Users/Nithin/Desktop/outputfile";
	static String OUTPUT_FILE_PATH_CUSTOM_SCORING_FUNCTION = "/Users/Nithin/Desktop/outputfile_custom";
	
	
	/** Simple command-line based search demo. */
	public static void main(String[] args) throws Exception {

		
		
		Indexer indexer = new Indexer(TEST200_DIR, INDEX_DIR);
		IndexSearcher searcher = Search.createSearcher(INDEX_DIR);

		System.out.println("extracting Page Id and pageName \n");
		
		//read_Queries_and_Write_Rankings(OUTLINES_DIR, OUTPUT_FILE_PATH,  searcher);
		Search.Custom_Scoring_Function(OUTLINES_DIR, OUTPUT_FILE_PATH_CUSTOM_SCORING_FUNCTION, searcher);
		System.out.println("File write finished \n");



	}

}
