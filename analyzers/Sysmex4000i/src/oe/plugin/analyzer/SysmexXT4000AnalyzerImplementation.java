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


public class SysmexXT4000AnalyzerImplementation extends AnalyzerLineInserter {
    
	static String ANALYZER_ID;
	static String DATE_PATTERN = "dd/MM/yyyy HH:mm:ss";
        
        
        private static final String CONTROL_ACCESSION_PREFIX = "QC-";
        private static final String[] units = new String[100];
        private static int[] scaleIndex = new int[100];
        private static final String DELIMITER = ",";
        double result = Double.NaN;
        String line;
       	String[] data;
        
        
        
	static HashMap<String, Test> testHeaderNameMap = new HashMap<String, Test>();
        static HashMap<String, String> testUnitMap = new HashMap<String, String>();
	HashMap<String, String> indexTestMap = new HashMap<String, String>();
    
	static{
		
		testHeaderNameMap.put("GB(10/uL)", new TestDAOImpl().getTestByGUID("d3b227d6-53ce-4402-bb0b-8f271e3b27e6"));
		testHeaderNameMap.put("GR(10^4/uL)", new TestDAOImpl().getTestByGUID("fdae85ea-3034-43af-9e11-55d5032256b5"));
		testHeaderNameMap.put("HBG(g/L)", new TestDAOImpl().getTestByGUID("781848ae-ada5-465c-9a19-d941dd424962"));
		/*testHeaderNameMap.put("HCT(10^(-1)%)", new TestDAOImpl().getTestByGUID("6792a51e-050b-4493-88ca-6f490c20cc5c"));
		testHeaderNameMap.put("VGM(10^(-1)fL)", new TestDAOImpl().getTestByGUID("ddce6c12-e319-455f-9f48-2f6ff363a246"));
		testHeaderNameMap.put("TCMH(10^(-1)pg)", new TestDAOImpl().getTestByGUID("bf497153-ba88-4fe8-83ee-c144229d7628"));
		testHeaderNameMap.put("CCMH(g/L)", new TestDAOImpl().getTestByGUID("8ab87a81-6b6b-4d4b-b53b-fac57109e393"));
		testHeaderNameMap.put("PLQ(10^3/uL)", new TestDAOImpl().getTestByGUID("88b7d8d3-e82b-441f-aff3-1410ba2850a5"));
		testHeaderNameMap.put("NEUT%(10^(-1)%)", new TestDAOImpl().getTestByGUID("0c25692f-a321-4e9c-9722-ca73f6625cb9"));
		testHeaderNameMap.put("LYMPH%(10^(-1)%)", new TestDAOImpl().getTestByGUID("eede92e7-d141-4c76-ab6e-b24ccfc84215"));
		testHeaderNameMap.put("MONO%(10^(-1)%)", new TestDAOImpl().getTestByGUID("9eece97f-04f3-4381-b378-2a9ac08a535a"));
		testHeaderNameMap.put("EO%(10^(-1)%)", new TestDAOImpl().getTestByGUID("50b568e8-e9da-428d-9697-8080bca7377b"));
		testHeaderNameMap.put("BASO%(10^(-1)%)", new TestDAOImpl().getTestByGUID("a41fcfb4-e3ba-4add-ac5d-56fae322cb9e"));*/

		System.out.println(testHeaderNameMap);
		
		AnalyzerDAO analyzerDAO = new AnalyzerDAOImpl();
		Analyzer analyzer = analyzerDAO.getAnalyzerByName("SysmexXT4000Analyzer");
		ANALYZER_ID = analyzer.getId();
		
		
	}
        static{
		
                testUnitMap.put("GB(10/uL)", "/100|10^3/uL");
		testUnitMap.put("GR(10^4/uL)", "/100|10^6/uL");
                testUnitMap.put("HBG(g/L)", "*10|10^3/uL");

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
        int hourIndex = -1;
        int dayIndex = -1;
        
       
        String[] headers = lines.get(1).split(DELIMITER);        
        for (Integer i = 1; i < headers.length; i++) {
        	if (testHeaderNameMap.containsKey(headers[i])) {
        		indexTestMap.put(i.toString(), headers[i]);
           	} else if ("N' Echantillon".equals(headers[i])) {
        		accessionNumberIndex = i;
        	} else if ("Ana. Heure".equals(headers[i])) {
        		hourIndex = i;
        	} else if ("Ana. Jour".equals(headers[i])) {
        		dayIndex = i;
        	}
        }
        
        for (Integer j = 2; j < lines.size(); j++) {        	
        	System.out.println("processing line #: "  + j);
        	//String line = lines.get(j);
        	//String[] data = line.split(",");
                
                line = lines.get(j);
        	data = line.split(",");
                
        	if (line.length() == 0 || data.length == 0) {
        		continue;
        	}
        	String dateTime = "";
        	
        	String currentAccessionNumber = "";
        	
        	for (Integer k = 0; k < data.length; k++) {
                   if (indexTestMap.containsKey(k.toString())) { 
                            
                            	String testKey = indexTestMap.get(k.toString());
                                AnalyzerResults aResult = new AnalyzerResults();
                                aResult.setTestId(testHeaderNameMap.get(testKey).getId());	        		
	        		aResult.setTestName(testHeaderNameMap.get(testKey).getName());
                                aResult.setResult(setResultByTest(k,testKey));
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
	        		currentAccessionNumber = data[k].trim();
	        		if (data[k].length() == 0) {
	        			break;
	        		} 
	        	} else if (k == dayIndex) {
	        		dateTime = data[k].trim();
	        	} else if (k == hourIndex) {
	        		dateTime = dateTime + " " + data[k].trim();
	        		
	        		if (data[k].length() == 0) {
	        			break;
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
                    
        if (AccessionPrefix.startsWith(CONTROL_ACCESSION_PREFIX)) {
                IsControl = true;
		}
      return IsControl;
  
   }

   private boolean CheckReadOnly (String testKey){
           
            boolean ReadOnly= false;
                    
        if (testKey.equals("NEUT#(10/uL)")||testKey.equals("MONO#(10/uL)")||testKey.equals("EO#(10/uL)")||testKey.equals("BASO#(10/uL)")||testKey.equals("LYMPH#(10/uL)")) {
                ReadOnly = true;
		}
      return ReadOnly;
  
   }
    
    private Timestamp getTimestampFromDate(String dateTime) {
        return DateUtil.convertStringDateToTimestampWithPattern(dateTime, DATE_PATTERN);
    }

    @Override
    public String getError() {
        return "Sysmex 400i analyzer unable to write to database";
    }
}
