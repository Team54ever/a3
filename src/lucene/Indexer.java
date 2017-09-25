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

import treccar.Data;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Paths;

import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import co.nstant.in.cbor.CborException;

import treccar.DeserializeData;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;


/**
 * Index all text files under a directory.
 */
public class Indexer {

	// Global name given to paraId and paraText
	static String paraId;
	static String paraText;
	


	/**
	 * Index all text files under a directory.
	 * 
	 * @throws FileNotFoundException
	 * @throws CborException
	 */
	public Indexer(String TEST200_DIR, String INDEX_DIR) throws CborException, FileNotFoundException {

		System.setProperty("file.encoding", "UTF-8");
		String test200path = TEST200_DIR;
		String indexPath = INDEX_DIR;


		Date start = new Date();
		try {
			
			
			System.out.println("Indexing to directory '" + indexPath + "'...");

			Directory dir = FSDirectory.open(Paths.get(indexPath));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

			IndexWriter writer = new IndexWriter(dir, iwc);

			// Function call
			
			indexParagraphs(writer, test200path);
			

			writer.close();

			// Time in Milliseconds
			Date end = new Date();
			System.out.println(".................Indexing done");
			System.out.println(end.getTime() - start.getTime() + " total milliseconds");

		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}

	}
	
	
	

	/*
	 * indexParagraphs - Reads the data from the test200 path Deserialize the
	 * paragraph file and stored as paraId and paraText ParaId and ParaText is
	 * feeded to createIndex
	 */
	public String indexParagraphs(IndexWriter writer, String test200Path) throws CborException, IOException {
		String paragraphs = test200Path;
		final FileInputStream fileInputStream2 = new FileInputStream(new File(paragraphs));
		for (Data.Paragraph p : DeserializeData.iterableParagraphs(fileInputStream2)) {

			paraId = p.getParaId().toString();
			paraText = p.getTextOnly().toString();

			createIndex(writer, paraId, paraText);

		}
		return paraId;

	}

	/*
	 * createIndex - Responsible for creating Index file Two Fields are added to
	 * the index document Field 1 - id, Field 2 - contents
	 */
	static void createIndex(IndexWriter writer, String id, String text) throws IOException {
		Document doc = new Document();
		doc.add(new StringField("id", id, Field.Store.YES));
		doc.add(new TextField("contents", text, Field.Store.YES));

		writer.addDocument(doc);

	}
}
