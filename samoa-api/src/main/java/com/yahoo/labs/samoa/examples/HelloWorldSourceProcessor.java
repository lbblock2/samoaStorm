package com.yahoo.labs.samoa.examples;

/*
 * #%L
 * SAMOA
 * %%
 * Copyright (C) 2013 - 2015 Yahoo! Inc.
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

import java.util.Random;

import com.yahoo.labs.samoa.moa.cluster.Cluster; 

import com.yahoo.labs.samoa.core.ContentEvent;
import com.yahoo.labs.samoa.core.EntranceProcessor;
import com.yahoo.labs.samoa.core.Processor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

/* 
 * Generates a string of random integers
 */
public class HelloWorldSourceProcessor implements EntranceProcessor {

    private static final long serialVersionUID = 6212296305865604747L;
    private Random rnd;
    private long maxInst;
    private long count;
    private String filename;
    private BufferedReader br;
    //privste file? rechercher les fichiers sur Google pour Java

    public HelloWorldSourceProcessor(String filename) { //met nom de ficiher ici en place de "long maxInst"
        this.filename = filename;
        try{
            FileReader reader = new FileReader(filename);
            
            this.br = new BufferedReader(reader);
            try {
                String line = br.readLine();
                System.out.println(line);
                //long maxInst = Long.parseLong(line);
                this.maxInst = maxInst;
            }
            catch (IOException exception) {
                System.out.println("Erreur! " + exception.getMessage());
            }
        }
        catch(FileNotFoundException exception)
        {
            System.out.println("problem fichier");
        }
    }

    @Override
    public boolean process(ContentEvent event) {
        // do nothing, API will be refined further
        return false;
    }

    @Override
    public void onCreate(int id) {
        rnd = new Random(id);
    }

    @Override
    public Processor newProcessor(Processor p) {
        HelloWorldSourceProcessor hwsp = (HelloWorldSourceProcessor) p;
        //return new HelloWorldSourceProcessor(hwsp.fileName);
        return new HelloWorldSourceProcessor(hwsp.filename);
    }

    @Override
    public boolean isFinished() {
    	return count >= maxInst;
    }
    
    // continue jusqu'au méthode renvoie faux
    @Override
    public boolean hasNext() {
        return count < maxInst;
    }

    //Does not run if hasNext() returns false
    @Override
    public ContentEvent nextEvent() {
        count++;
        String line=new String();
        try {
            line= this.br.readLine();
        }
       catch (IOException exception) {
           System.out.println("Erreur! " + exception.getMessage());
       }
        
        return new HelloWorldContentEvent(line, false);
    }
}
