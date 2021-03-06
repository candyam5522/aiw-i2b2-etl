/*
 * #%L
 * AIW i2b2 ETL
 * %%
 * Copyright (C) 2012 - 2013 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.emory.cci.aiw.i2b2etl.dest.config.xml;

import edu.emory.cci.aiw.i2b2etl.dest.config.ConfigurationInitException;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Andrew Post
 */
final class ConfigurationReader {
    private final DictionarySection dictionary;
    private final DatabaseSection database;
    private final ConceptsSection concepts;
    private final DataSection data;
    private final File conf;
    
    ConfigurationReader(File confFile) {
        this.conf = confFile;
        this.dictionary = new DictionarySection();
        this.database = new DatabaseSection();
        this.concepts = new ConceptsSection();
        this.data = new DataSection();
    }
    
    void read() throws ConfigurationInitException {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(conf);
            Element eRoot = doc.getDocumentElement();
            NodeList nL = eRoot.getChildNodes();
            for (int i = 0; i < nL.getLength(); i++) {

                Node node = nL.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (node.getNodeName().equals("dictionary")) {
                        dictionary.load((Element) node);
                    } else if (node.getNodeName().equals("database")) {
                        database.load((Element) node);
                    } else if (node.getNodeName().equals("concepts")) {
                        concepts.load((Element) node);
                    } else if (node.getNodeName().equals("data")) {
                        data.load((Element) node);
                    }
                }
            }
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            throw new ConfigurationInitException("Could not read configuration file " + this.conf.getAbsolutePath(), ex);
        }
	}
    
    DictionarySection getDictionarySection() {
        return this.dictionary;
    }
    
    DatabaseSection getDatabaseSection() {
        return this.database;
    }
    
    ConceptsSection getConceptsSection() {
        return this.concepts;
    }
    
    DataSection getDataSection() {
        return this.data;
    }
    
    
}
