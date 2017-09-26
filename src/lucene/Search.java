/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lucene;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import treccar.Data;
import treccar.DeserializeData;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

public class Search {

	static final int i = 0;
	static String pageId;
	static String pageName;

	
	/*
	 * Using custom Scoring function and evaluating the results
	 */
	public static void Custom_Scoring_Function(String outline_path, String outputpath, IndexSearcher searcher) throws Exception
	{
		SimilarityBase mySimilarity = new SimilarityBase() {

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				return "lnc";
			}

			@Override
			protected float score(BasicStats arg0, float arg1, float arg2) {
				// TODO Auto-generated method stub
				float l = (float) (1 + log2(arg0.getTotalTermFreq()));
				float n = 1.0f;
				float c = arg0.getValueForNormalization();
				float lnc = ( l * n * c);
				
				
				
				
				return lnc;
			}
		};
		
		
		searcher.setSimilarity(mySimilarity);
		read_Queries_and_Write_Rankings(outline_path, outputpath, searcher);
		
	}
	/*
	 * Function reads the queries and write the results to the file
	 */
	static void read_Queries_and_Write_Rankings(String outlines, String outputfilepath, IndexSearcher searcher) throws Exception {

		final FileInputStream file = new FileInputStream(new File(outlines));
		File file_write = new File(outputfilepath);
		// creates the file
		file_write.createNewFile();
		// creates a FileWriter Object
		FileWriter writer = new FileWriter(file_write);

		for (Data.Page p : DeserializeData.iterableAnnotations(file)) {
			pageId = p.getPageId().toString();
			pageName = p.getPageName().toString();
			int rank = 0;

			

			TopDocs foundDocs1 = searchQuery(pageName, searcher);
			for (ScoreDoc sd : foundDocs1.scoreDocs) {
				Document d = searcher.doc(sd.doc);
				rank = rank + 1;
				String a = d.get("id");
				writer.write(pageId + " Q0 " + a + " " + rank + " " + sd.score + " $team5-$Lucene-runFile " + "\n");

			}
			
			
		}
		writer.flush();
		writer.close();
	}

	/*
	 * Argument 1 - Query String returns total number of results
	 */
	private static TopDocs searchQuery(String query, IndexSearcher searcher) throws Exception {
		QueryParser qp = new QueryParser("contents", new StandardAnalyzer());
		Query QueryString = qp.parse(query);
		TopDocs hits = searcher.search(QueryString, 100);
		return hits;
	}

	/*
	 * Arg 1 - relative path to the index directory
	 */
	public static IndexSearcher createSearcher(String INDEX_DIR) throws IOException {
		String index_dir = INDEX_DIR;
		Directory dir = FSDirectory.open(Paths.get(index_dir));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
}
