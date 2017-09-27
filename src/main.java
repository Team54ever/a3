import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

import co.nstant.in.cbor.CborException;
import lucene.*;
import treccar.*;

public class main {

	static String TEST200_DIR = "/Users/Nithin/Desktop/test200/train.test200.cbor.paragraphs";
	static String INDEX_DIR = "/Users/Nithin/Desktop/Path";
	static String OUTLINES_DIR = "/Users/Nithin/Desktop/test200/train.test200.cbor.outlines";
	static String OUTPUT_FILE_PATH = "/Users/Nithin/Desktop/outputfile";
	static String OUTPUT_FILE_PATH_CUSTOM_SCORING_FUNCTION = "/Users/Nithin/Desktop/outputfile_custom";

	/////////////////// Variant 1 - lnc.ltn/////////////////// /////////////////////////
	public static void LNC_LTN() throws Exception {
		String LNC_INDEX_DIR = "/Users/Nithin/Desktop/Indexer/lncIndexPath";
		String outputpath =  "/Users/Nithin/Desktop/Outputs/lnc_ltn";
		SimilarityBase lnc_similarity = new SimilarityBase() {

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected float score(BasicStats stats, float freq, float docLen) {
				float l = (float) (1 + log2(stats.getTotalTermFreq()));
				float n = 1.0f;
				float c = stats.getValueForNormalization();
				float lnc = (l * n * c);

				return lnc;
			}
		};
		
		System.out.println(" \n LNC Indexing");
		Indexer lnc_indexer = new Indexer(TEST200_DIR, LNC_INDEX_DIR, lnc_similarity);
		System.out.println(" \n LNC Indexing Done");
		

		SimilarityBase ltn_similarity = new SimilarityBase() {

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected float score(BasicStats stats, float freq, float docLen) {
				// TODO Auto-generated method stub
				float l = (float) (1 + log2(stats.getTotalTermFreq()));
				float t = (float) log2((stats.getNumberOfDocuments()) / (stats.getDocFreq()));
				float n = 1.0f;
				float ltn = (l * t * n);
				return ltn;
			}
		};
		
		System.out.println("\n  Applying LTN Similarity to the searcher...");
		Search lnc_searcher = new Search(ltn_similarity, LNC_INDEX_DIR, outputpath);
		System.out.println("\n LTN Similarity done");

	}

	/////////////////// Variant 2 - BNN_BNN/////////////////// ////////////////////////////////
	public static void BNN_BNN() throws Exception {
		String BNN_INDEX_DIR = "/Users/Nithin/Desktop/Indexer/BNNIndexPath";
		String outputpath =  "/Users/Nithin/Desktop/Outputs/bnn_bnn";
		
		// bnn Similarity for Indexer
		SimilarityBase bnn_similarity = new SimilarityBase() {

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected float score(BasicStats stats, float freq, float docLen) {
				// TODO Auto-generated method stub
				float b;
				if (stats.getTotalTermFreq() > 0)
					b = 1.0f;
				else
					b = 0.0f;
				float n1 = 1.0f;
				float n2 = 1.0f;

				float bnn = (b * n1 * n2);

				return bnn;
			}
		};
		System.out.println("\n BNN Indexing");
		
		Indexer bnn_indexer = new Indexer(TEST200_DIR, BNN_INDEX_DIR, bnn_similarity);
		
		System.out.println("\n BNN Indexing done");
		
		// bnn similarity for similarity
		SimilarityBase bnn1_similarity = new SimilarityBase() {

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected float score(BasicStats stats, float freq, float docLen) {
				// TODO Auto-generated method stub
				float b;
				if (stats.getTotalTermFreq() > 0)
					b = 1.0f;
				else
					b = 0.0f;
				float n1 = 1.0f;
				float n2 = 1.0f;
				float bnn = (b * n1 * n2);
			
				return bnn;
			}
		};
		
		System.out.println("\n Applying BNN Similarity to the searcher...");
		
		Search bnn_searcher = new Search(bnn1_similarity, BNN_INDEX_DIR, outputpath);
		
		System.out.println("\n BNN Similarity done.");

	}
	
	/////////////////// Variant 3 - anc.apc /////////////////// ////////////////////////////////
	public static void ANC_APC() throws Exception
	{
		String ANC_INDEX_DIR = "/Users/Nithin/Desktop/Indexer/ANCIndexPath";
		String outputpath =  "/Users/Nithin/Desktop/Outputs/anc_apc";
		
		SimilarityBase anc_similarity = new SimilarityBase() {
			
			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected float score(BasicStats stats, float freq, float docLen) {
				// TODO Auto-generated method stub
				/////// ----- ///////
				
				//float max = Collections.max(stats.getTotalTermFreq());
				float a = 0.5f + (0.5f * stats.getTotalTermFreq());
				float n = 1.0f;
				float c = stats.getValueForNormalization();
				float anc = ( a * n * c);
				return anc;
			}
		};
		
		System.out.println("\n ANC Indexing");
		Indexer anc_indexer = new Indexer(TEST200_DIR, ANC_INDEX_DIR, anc_similarity);
		System.out.println("\n ANC Indexing done");
		
		SimilarityBase apc_similarity = new SimilarityBase() {
			
			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected float score(BasicStats stats, float freq, float docLen) {
				// TODO Auto-generated method stub
				float a = 0.5f + (0.5f * stats.getTotalTermFreq());
				float probidf = (float) log2( (stats.getNumberOfDocuments() - stats.getDocFreq() ) / (stats.getDocFreq()));
				float p = 0.0f;
				if(probidf > 0)
				{
					p = probidf;
				}
				else
				{
					p = 0.0f;
				}
				
				float c = stats.getValueForNormalization();
				
				float apc = (a * p * c);
				return apc;
			}
		};
		System.out.println("\n Applying APC Similarity to the searcher...");
		
		Search apc_search = new Search(apc_similarity, ANC_INDEX_DIR, outputpath);
		
		System.out.println("\n APC Similarity Done");
	}
	
	
	

	/** Simple command-line based search demo. */
	public static void main(String[] args) throws Exception {

		LNC_LTN();
		BNN_BNN();
		ANC_APC();
		
		

		

		System.out.println("File write finished \n");

	}

}
