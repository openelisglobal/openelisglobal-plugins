/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is OpenELIS code.
 *
 * Copyright (C) ITECH, University of Washington, Seattle WA.  All Rights Reserved.
 */

package oe.plugin.analyzer;

import us.mn.state.health.lims.analyzer.dao.AnalyzerDAO;
import us.mn.state.health.lims.analyzer.daoimpl.AnalyzerDAOImpl;
import us.mn.state.health.lims.analyzer.valueholder.Analyzer;
import us.mn.state.health.lims.analyzerimport.analyzerreaders.AnalyzerLineInserter;
import us.mn.state.health.lims.analyzerimport.analyzerreaders.AnalyzerReaderUtil;
import us.mn.state.health.lims.analyzerimport.util.AnalyzerTestNameCache;
import us.mn.state.health.lims.analyzerimport.util.MappedTestName;
import us.mn.state.health.lims.analyzerresults.valueholder.AnalyzerResults;
import us.mn.state.health.lims.common.util.DateUtil;
import us.mn.state.health.lims.dictionary.dao.DictionaryDAO;
import us.mn.state.health.lims.dictionary.daoimpl.DictionaryDAOImpl;
import us.mn.state.health.lims.dictionary.valueholder.Dictionary;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;
import us.mn.state.health.lims.testresult.daoimpl.TestResultDAOImpl;
import us.mn.state.health.lims.testresult.valueholder.TestResult;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import us.mn.state.health.lims.common.services.PluginAnalyzerService;


public class FacsCantoIIImplementation extends AnalyzerLineInserter {
    
	static String ANALYZER_ID;
	//static String DATE_PATTERN = "dd/MM/yyyy HH:mm:ss";
        static String DATE_PATTERN = "MMM dd yyyy hh:mm:ss a";
        private static final String CONTROL_ACCESSION_PREFIX = "QA";
        private static final String CONTROL_ACCESSION_PREFIX2 = "IMM";
        private static final String[] units = new String[100];
        private static final int[] scaleIndex = new int[100];
        private static final String DELIMITER = ",";
        double result = Double.NaN;
        String line;
       	String[] data;
        
        
        
	static HashMap<String, Test> testHeaderNameMap = new HashMap<String, Test>();
        static HashMap<String, String> testUnitMap = new HashMap<String, String>();
	HashMap<String, String> indexTestMap = new HashMap<String, String>();
        List<PluginAnalyzerService.TestMapping> nameMappinng = new ArrayList<PluginAnalyzerService.TestMapping>();
    
	static{
		
	    testHeaderNameMap.put("CD3 %Parent", new TestDAOImpl().getTestByName("CD3 percentage count"));
            testHeaderNameMap.put("CD4 %Parent", new TestDAOImpl().getTestByName("CD4 percentage count"));
           // testHeaderNameMap.put("Q2 %Parent", new TestDAOImpl().getTestByName("Dénombrement des lymphocytes  CD4 (%)"));               
                
                System.out.println(testHeaderNameMap);
		AnalyzerDAO analyzerDAO = new AnalyzerDAOImpl();
		Analyzer analyzer = analyzerDAO.getAnalyzerByName("FacsCantoII");
		ANALYZER_ID = analyzer.getId();
		
		
	} 
       
        
        static{
		
                testUnitMap.put("CD3 %Parent", "/1|%");
                testUnitMap.put("CD4 %Parent", "/1|%");
		                
	System.out.println(testUnitMap);
		
	}
    
    /* (non-Javadoc)
     * @see us.mn.state.health.lims.analyzerimport.analyzerreaders.AnalyzerLineInserter#insert(java.util.List, java.lang.String)
     */
    @Override
    public boolean insert(List<String> lines, String currentUserId) {
        List<AnalyzerResults> results = new ArrayList<AnalyzerResults>();
        boolean readData = false;
        int accessionNumberIndex = -1;
        int accessionNumberIndex2 = -1;
        int hourIndex = -1;
        int dayIndex = -1;
        
       
        String[] headers = lines.get(0).split(DELIMITER);        
        for (Integer i = 0; i < headers.length; i++) {
        	if (testHeaderNameMap.containsKey(headers[i])) {
        		indexTestMap.put(i.toString(), headers[i]);
           	} else if ("SAMPLE ID".equals(headers[i])) {
        		accessionNumberIndex = i;
                        
                }  else if ("Record Date".equals(headers[i])) {
        		dayIndex = i;
        	}
        }
        
        for (Integer j = 1; j < lines.size(); j++) {        	
        	System.out.println("processing line #: "  + j);
        	line = lines.get(j);
        	data = line.split(",");
                                                
        	if (line.length() == 0 || data.length == 0) {
        		continue;
        	}
        	String dateTime = "";
                String dateTime2 = "";
        	
        	String currentAccessionNumber = "";
        	
        	for (Integer k = 0; k < data.length; k++) {
                      
                   if (indexTestMap.containsKey(k.toString())) { 
                                                       
                                String testKey = indexTestMap.get(k.toString());
                                AnalyzerResults aResult = new AnalyzerResults();
                                aResult.setTestId(testHeaderNameMap.get(testKey).getId());	        		
	        		aResult.setTestName(testHeaderNameMap.get(testKey).getName());
                                aResult.setResult(setResultByTest(k+1,testKey));
                                aResult.setAnalyzerId(ANALYZER_ID);
                                aResult.setUnits(setUnitByTestKey(testKey));
                                aResult.setAccessionNumber(currentAccessionNumber);
                                aResult.setReadOnly(CheckReadOnly (testKey));
                                aResult.setIsControl(CheckControl (currentAccessionNumber));
			        aResult.setCompleteDate(getTimestampFromDate(dateTime));
                                
	            	
                                System.out.println("***" + aResult.getAccessionNumber() + " " + aResult.getCompleteDate() + " " + aResult.getResult());
	            	
                                results.add(aResult);
                        
                        
	        		if (data[k].length() == 0) {
	        			break;
	        		} 
	        	} else if (k == accessionNumberIndex) {
                            
                            if (!(data[k+1].contains("SAMPLE"))){
                              currentAccessionNumber = data[k+1].trim();} else break;
	        		if (data[k].length() == 0) {
	        			break;
	        		} 
	        	} else if (k == dayIndex) {
                            
                        if (data[k].contains("Record")){
                            
                            break; }
                        else {                                   
                            
                        dateTime = data[k].trim();
                        dateTime2 = data[k+1].trim();
                        dateTime = dateTime + data[k+1];
                        dateTime = dateTime.replace("\"","");
                        dateTime = dateTime.trim();
                                     	} 
                        }	
        	}
             
        }
        return persistImport(currentUserId, results);
    }

   private String setUnitByTestKey(String testKey) {
       
         String unitKey = testUnitMap.get(testKey);
                int debut = unitKey.indexOf("|");
                int debutChaineUnit = 1 + debut;
                String unit = unitKey.substring(debutChaineUnit); 
        return unit;        
    } 
   
   
   private String setResultByTest(int i, String testKey) {
       
                String unitKey = testUnitMap.get(testKey);
                int debut = unitKey.indexOf("|");
                String scale = unitKey.substring(1, debut);
                String operateur = unitKey.substring(0,1);
                        if (operateur.equals("/")){
                                    
                                 try{
                                      result = Double.parseDouble(data[i].trim())/Integer.parseInt(scale);
		                     }catch( NumberFormatException nfe){
					//no-op -- defaults to NAN
                                        }
                                  } 
                                else if (operateur.equals("*")){
                                    
                                 try{
                                      result = Double.parseDouble(data[i].trim())*Integer.parseInt(scale);
		                     }catch( NumberFormatException nfe){
					//no-op -- defaults to NAN
                                        }
                                }
                                
                       String TestResult=String.valueOf(result);
              return TestResult;
     } 
    
   private boolean CheckControl (String AccessionPrefix){
     
            boolean IsControl= false;
                    
        if ((AccessionPrefix.startsWith(CONTROL_ACCESSION_PREFIX))||(AccessionPrefix.startsWith(CONTROL_ACCESSION_PREFIX2))) {
                IsControl = true;
		}
      return IsControl;
  
   }
        
private boolean CheckReadOnly (String testKey){
           
            boolean ReadOnly= false;
                    
        if (testKey.equals("Q2 #Events")) {
                ReadOnly = true;
		}
      return ReadOnly;
  
   }

    



        
        
    private Timestamp getTimestampFromDate(String dateTime) {
        //return DateUtil.convertStringDateToTimestampWithPattern(dateTime, DATE_PATTERN);
        return DateUtil.convertStringDateToTimestampWithPatternNoLocale(dateTime, DATE_PATTERN);
    }

    @Override
    public String getError() {
        return "FacsCantoII analyzer unable to write to database";
    }
}
