/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yahoo.labs.samoa.riverdata;

/* Credits to Rahnama Amir
 * 
 */

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


import com.yahoo.labs.samoa.instances.Attribute;
import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import com.yahoo.labs.samoa.instances.InstancesHeader;
import com.yahoo.labs.samoa.instances.SparseInstance;
import com.yahoo.labs.samoa.moa.core.Example;
import com.yahoo.labs.samoa.moa.core.InstanceExample;
import com.yahoo.labs.samoa.moa.core.ObjectRepository;
import com.yahoo.labs.samoa.moa.options.AbstractOptionHandler;
import com.yahoo.labs.samoa.moa.options.ClassOption;
import com.yahoo.labs.samoa.moa.streams.InstanceStream;
import com.yahoo.labs.samoa.moa.tasks.TaskMonitor;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Credits to Rahnama Amir
 * @author lauren
 */
public class RiverDataInstanceStream extends AbstractOptionHandler implements
    InstanceStream {

    private static final long serialVersionUID = 1L;
    
    private boolean hasNext = true;
    protected Writer writer;
    protected BufferedReader reader;
    private boolean isReading;
    
    protected String lastLineRead;
    private String currLine;
    private InstanceExample lastInstance;
    
    private int nInstances = 0;
    private int nInstancesRead = 0;
    public double[] attVals = {};
    public int[] indices = {};
    
    public Instance inst = null;
    protected Instances instances;
    
    
    
    /*
     * Pas necessaire de faire rien
     */
    @Override
    protected void prepareForUseImpl(TaskMonitor monitor, ObjectRepository repository) {
        
    }

    /* getDescription
     * pas necessaire
     */
    @Override
    public void getDescription(StringBuilder sb, int indent) {
    }

    /*
     * Crée une description de la longeur et les attributs du flux
     */
    private void setStreamHeader() {
        setAttributes();
        this.instances = new Instances(getCLICreationString(InstanceStream.class), null, 0);
        this.instances.setClassIndex(0);
    }
    
    /*
     * Crée les attributs spécifique. Ce serait une bonne idée d'ajouter un
     * méthode qui fait ça automatiquement.     
     */
    private void setAttributes() {
        List<String> allLocData = new ArrayList<String>();
        allLocData.add("Débits Tuby");
        allLocData.add("Débits Gambetta");
        allLocData.add("Débits Ile de Lerins");
        allLocData.add("Débits Distribution Cannes Sup");
        allLocData.add("Débits Distribution Perier");
        allLocData.add("Débits Refoulement");
        allLocData.add("Chlore REU la Baume");
        allLocData.add("Chlore REU Mouré Rouge");
        allLocData.add("Chlore REU Palm Beach");
        allLocData.add("Residuel Chlore Cannes superieur");
        allLocData.add("Residuel Chlore Perrier");
        allLocData.add("Niveau reservoir Cannes Sup");
        allLocData.add("Débit distribution Perier");
        allLocData.add("Niveau Perier");
        Attribute locData = new Attribute("Location and Data", allLocData);
        Attribute val = new Attribute("Value");
    }
    
    /*
     * Renvoie la description
     */
    @Override
    public InstancesHeader getHeader() {
        return new InstancesHeader(this.instances);
    }

    /*
     * Renvoie 0; n'estime pas
     */
    @Override
    public long estimatedRemainingInstances() {
        return 0;
    }

    /*
     * Renvoie hasNext, qui décrit si il reste des Instances
     */
    @Override
    public boolean hasMoreInstances() {
        return this.hasNext;
    }

    /*
     * Renvoie le prochain Instance si possible
     */
    @Override
    public Example<Instance> nextInstance() {
       // Si on n'a pas encore l'instance, trouvez ça
        if (this.lastInstance == null) {
            getNextInstance();
        }
        InstanceExample prevInstance = this.lastInstance;
        return prevInstance;
    }

    /*
     * Trouve le prochain Instance et l'analyse
     */
    private boolean getNextInstance() {
        // Initialiser tous les variables
        Instance inst = null;
        boolean result = false;
        while (result == false)
        {
            // Trouve s'il reste toujours un Instance
            inst = checkIfMoreInstances();
            if (inst == null)
            {
                try
                {
                    Thread.sleep(500);
                    System.out.println("on attend...");
                }
                catch (InterruptedException x)
                {
                }
            }
            else
            {
                result = true;
            }
        }
        // Transforme l'Instance et trouve l'information
        this.instances.add(inst);
        this.lastInstance = new InstanceExample(this.instances.instance(0));
        this.instances.delete(); // keep instances clean
        this.nInstancesRead++;
        return result;
    }
    
    /* 
     * On ne peut pas recommencer
     */
    @Override
    public boolean isRestartable() {
        return false;
    }

    /*
     * Pas necessaire parce qu'on ne peut pas recommencer
     */
    @Override
    public void restart() {
        
    }

    
    protected boolean readNextLine() {
        try
        {
            if (lastLineRead != null)
            {
                lastLineRead = reader.readLine();

                if (lastLineRead != null)
                {
                    currLine = lastLineRead;
                    return true;
                }
            }
            if (lastLineRead == null)
            {
                reader.close();
                reader = null;
            }

            return false;
        }
        catch (IOException ioe)
        {
            throw new RuntimeException(
                "Erreur: Impossible de lire l'instance.", ioe);
        }
    }

    private void initReaderAndWriter() {
        String infile = "/Users/lauren/Documents/hiver/stage-ingenerie/fluxcode/grandfichier7avril0.csv";
        String outfile = "/Users/lauren/Documents/hiver/stage-ingenerie/fluxcode/programOutfile.csv";
        
        try {
            if (outfile != null) {
                writer = new BufferedWriter(new FileWriter(outfile));
            }
            if (infile != null) {
                reader = new BufferedReader(new FileReader(infile));
                lastLineRead = "";
                isReading = true;
            }
            else {
                isReading = false;
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("Impossible d'écrire au fichier " + outfile, ex);
        }
    }
    
    public Instance checkIfMoreInstances()
    {
        Instance inst = null;
        String tempStr = "";
        nInstances++;

        if (this.isReading)
        {
            tempStr = this.lastLineRead;
            this.hasNext = readNextLine();
            if (tempStr != null && !tempStr.equals(""))
            {
                //inst = tempStr;
            }
        }
//        else if (this.twitterStreamReader.size() > 0)
//        {
//            m = this.twitterStreamReader.getAndRemove(0);
//            inst = this.filterTfIdf.filter(m, this.getHeader());
//            if (this.writer != null)
//            {
//                try
//                {
//                    writer.write(m);
//                    writer.write("\n");
//                }
//                catch (Exception ex)
//                {
//                    throw new RuntimeException(
//                        "Failed writing to file ", ex);
//                }
//            }
//        }
        if (inst != null)
        {
            //System.out.println("CHECK " + m + " ");
        }
        return inst;
    }
    
    private Instance getInstance(InstancesHeader header, String type, String[] messageTokens) {
        Instance inst = new SparseInstance(1.0, attVals, indices, header.numAttributes());
        inst.setDataset(header);
        if (type.equals("S") || type.equals("H"))
        {
            //inst.setClassValue(type.equals("S") ? "S" : "H");
            inst.setClassValue(type.equals("S") ? 0 : 1);
        }
        else
        {
            //inst.setClassMissing();
            inst.setClassValue(2);

        }
        return inst;
    }
    
    private void translateLine() {
            try {
                String line = reader.readLine();
                //System.out.println(line);
                //long maxInst = Long.parseLong(line);
                //this.maxInst = maxInst;
                if (line == null) {
                    this.hasNext = false;
                }
                //parse line
                // send info re. value and type of info
            }
            catch (IOException exception) {
                System.out.println("Erreur! " + exception.getMessage());
            }
    }
    
}
