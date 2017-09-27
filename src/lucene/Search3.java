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
package lucene1;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import treccar1.Data;
import treccar1.Data.PageSkeleton;
import treccar1.Data.Section;
import treccar1.DeserializeData;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

public class Search {

	static String OUTLINES_DIR = "C:\\Users\\Ajesh\\Desktop\\UNH\\Courses\\CS853\\Programming Assignment 1 Lucene Index for TREC Complex Answer Retrieval\\test200_all\\train.test200.cbor.outlines";;
	static final int i = 0;
	static String pageId;
	static String pageName;
	static List<PageSkeleton> skeletons;
	static ArrayList<Section> childsections;
	static String headingId;
	static String heading;
	static String heading2;
	
	public Search(SimilarityBase similarity, String INDEX_DIR, String outputpath) throws Exception
	{
		IndexSearcher searcher = createSearcher(INDEX_DIR);
		searcher.setSimilarity(similarity);
		read_Queries_and_Write_Rankings(OUTLINES_DIR, outputpath, searcher);
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
			skeletons = p.getSkeleton();
			childsections =p.getChildSections();
			int rank = 0;
			//System.out.println(childsections);
			//System.out.println(pageName);
		
			for (Data.Section p1 : childsections) {				
				headingId = p1.getHeadingId().toString();
				heading = p1.getHeading().toString();
				heading2 = heading.replaceAll("\\W"," ");
				System.out.println(heading2);
				
			TopDocs foundDocs1 = searchQuery(heading2, searcher);
			
			for (ScoreDoc sd : foundDocs1.scoreDocs) {
				Document d = searcher.doc(sd.doc);
				rank = rank + 1;
				String a = d.get("id");
				writer.write(pageName+"/"+headingId + " Q0 " + a + " " + rank + " " + sd.score + " $team5-$Lucene-runFile " + "\n");
			}
		  }
		}
		writer.flush();
		writer.close();
		System.out.println(" results written to path - "+outputfilepath );
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
