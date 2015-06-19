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


public class SysmexAnalyzerImplementation extends AnalyzerLineInserter {
    
	static String ANALYZER_ID;
	static String DATE_PATTERN = "dd/MM/yyyy HH:mm:ss";
        
        
        private static final String CONTROL_ACCESSION_PREFIX = "QC-";
        private static final String[] units = new String[100];
        private static final int[] scaleIndex = new int[100];
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
		testHeaderNameMap.put("HCT(10^(-1)%)", new TestDAOImpl().getTestByGUID("dacf059a-bf70-48a6-849a-28d462087f6e"));
		testHeaderNameMap.put("VGM(10^(-1)fL)", new TestDAOImpl().getTestByGUID("29736377-dd22-42bb-b3ba-2d1e5db8549f"));
		testHeaderNameMap.put("TCMH(10^(-1)pg)", new TestDAOImpl().getTestByGUID("092a766e-6549-4838-a96f-7bfbc547b06d"));
		testHeaderNameMap.put("CCMH(g/L)", new TestDAOImpl().getTestByGUID("42d607b3-8283-41ba-9d33-927e31a9f2a4"));
		testHeaderNameMap.put("PLQ(10^3/uL)", new TestDAOImpl().getTestByGUID("a5ac2b5d-d958-419e-ab48-69328adc74a3"));
		testHeaderNameMap.put("NEUT#(10/uL)", new TestDAOImpl().getTestByGUID("82172296-a86f-4d90-b2d8-cbd1fe089df7"));
                testHeaderNameMap.put("NEUT%(10^(-1)%)", new TestDAOImpl().getTestByGUID("37f96e86-6036-4710-83f6-325f0b815d24"));
                testHeaderNameMap.put("LYMPH#(10/uL)", new TestDAOImpl().getTestByGUID("d5325b4c-6e38-4214-84cc-6eb3026f653a"));
		testHeaderNameMap.put("LYMPH%(10^(-1)%)", new TestDAOImpl().getTestByGUID("03074da7-5f1b-41ed-ba29-f4c22276f3cc"));
		testHeaderNameMap.put("MONO#(10/uL)", new TestDAOImpl().getTestByGUID("d6ddad61-a22e-471f-af05-a0b3e367c776"));
		testHeaderNameMap.put("MONO%(10^(-1)%)", new TestDAOImpl().getTestByGUID("751a0897-d8a5-4c89-910c-12244193599f"));
		testHeaderNameMap.put("EO#(10/uL)", new TestDAOImpl().getTestByGUID("30d037e5-fa9e-4bb3-ae42-8c098e1536b4"));
		testHeaderNameMap.put("EO%(10^(-1)%)", new TestDAOImpl().getTestByGUID("77d0d185-9e43-4f6a-bd11-3c34e4419a26"));
		testHeaderNameMap.put("BASO#(10/uL)", new TestDAOImpl().getTestByGUID("e21958b9-17b6-41c6-bfb9-e1017cad627b"));
		testHeaderNameMap.put("BASO%(10^(-1)%)", new TestDAOImpl().getTestByGUID("7864e487-94d4-4f2a-9a14-87fdf4814f26"));

		System.out.println(testHeaderNameMap);
		
		AnalyzerDAO analyzerDAO = new AnalyzerDAOImpl();
		Analyzer analyzer = analyzerDAO.getAnalyzerByName("SysmexAnalyzer");
		ANALYZER_ID = analyzer.getId();
		
		
	}
        static{
		
                testUnitMap.put("GB(10/uL)", "/100|10^3/uL");
		testUnitMap.put("GR(10^4/uL)", "/100|10^6/uL");
                testUnitMap.put("HBG(g/L)", "/10|g/dL");
                testUnitMap.put("HCT(10^(-1)%)", "/10|%");
		testUnitMap.put("VGM(10^(-1)fL)", "/10|fl");
                testUnitMap.put("TCMH(10^(-1)pg)", "/10|pg");
                testUnitMap.put("CCMH(g/L)", "/10|10^3/uL");
		testUnitMap.put("PLQ(10^3/uL)", "/1|g/dL");
                testUnitMap.put("NEUT#(10/uL)", "/100|10^3/uL");
		testUnitMap.put("NEUT%(10^(-1)%)", "/10|%");
                testUnitMap.put("LYMPH#(10/uL)", "/100|10^3/uL");
		testUnitMap.put("LYMPH%(10^(-1)%)", "/10|%");
                testUnitMap.put("MONO#(10/uL)", "/100|10^3/uL");
		testUnitMap.put("MONO%(10^(-1)%)", "/10|%");
                testUnitMap.put("EO#(10/uL)", "/100|10^3/uL");
		testUnitMap.put("EO%(10^(-1)%)", "/10|%");
                testUnitMap.put("BASO#(10/uL)", "/10|10^3/uL");
		testUnitMap.put("BASO%(10^(-1)%)", "/10|%");
                

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
        
       
        String[] headers = lines.get(0).split(DELIMITER);        
        for (Integer i = 0; i < headers.length; i++) {
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
        
        for (Integer j = 1; j < lines.size(); j++) {        	
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
        return "Sysmex 2000i analyzer unable to write to database";
    }
}
