/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yahoo.labs.samoa.examples;

/**
 *
 * @author lauren
 */

import com.github.javacliparser.FileOption;
import com.github.javacliparser.StringOption;
import com.yahoo.labs.samoa.instances.Attribute;
import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import com.yahoo.labs.samoa.instances.InstancesHeader;
import com.yahoo.labs.samoa.moa.core.Example;
import com.yahoo.labs.samoa.moa.core.InstanceExample;
import com.yahoo.labs.samoa.moa.core.ObjectRepository;
import com.yahoo.labs.samoa.moa.options.AbstractOptionHandler;
import com.yahoo.labs.samoa.moa.options.ClassOption;
import com.yahoo.labs.samoa.moa.streams.InstanceStream;
import com.yahoo.labs.samoa.moa.tasks.TaskMonitor;
import java.io.*;

public class RiverDataInstanceAPI implements RiverDataInstance {

    protected Writer writer;
    protected BufferedReader reader;
    
    @Override
    public void initStream() {
        System.out.println("Flux prête...");
    }

    @Override
    public void readFile() {
        System.out.println("lisant...");
    }
    
    public Instance findIfNextInstance() {
        Instance inst = null;
        String instStr = "";
        return inst;
    }
    
    /* initReaderAndWriter()
     * ------------------------
     * Initialise "reader" et "writer" pour les fichiers. 
     */
    private void initReaderAndWriter() {
        String infile = "/Users/lauren/Documents/hiver/stage-ingenerie/fluxcode/grandfichier7avril0.csv";
        String outfile = "/Users/lauren/Documents/hiver/stage-ingenerie/fluxcode/programOutfile.csv";
        
        try {
            if (outfile != null) {
                writer = new BufferedWriter(new FileWriter(outfile));
            }
            if (infile != null) {
                reader = new BufferedReader(new FileReader(infile));
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("Ne pouvait pas écrire au fichier " + outfile, ex);
        }
    }
}
