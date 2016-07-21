
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

import us.mn.state.health.lims.analyzerimport.analyzerreaders.AnalyzerLineInserter;
import us.mn.state.health.lims.analyzerresults.valueholder.AnalyzerResults;
import us.mn.state.health.lims.common.util.DateUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import us.mn.state.health.lims.analyzer.dao.AnalyzerDAO;
import us.mn.state.health.lims.analyzer.daoimpl.AnalyzerDAOImpl;
import us.mn.state.health.lims.analyzer.valueholder.Analyzer;
import us.mn.state.health.lims.test.daoimpl.TestDAOImpl;
import us.mn.state.health.lims.test.valueholder.Test;

public class CobasIntegra400Implementation extends AnalyzerLineInserter {
   static String ANALYZER_ID;
	
        static String DATE_PATTERN = "yyyy-MM-dd";
        
        private static final String CONTROL_ACCESSION_PREFIX = "PCC";
        private static final String[] units = new String[100];
        private static final int[] scaleIndex = new int[100];
        private static final String DELIMITER = "\\s";
        double result = Double.NaN;
        String line;
       	String[] data;
        
        
        
        
	static HashMap<String, Test> testNameMap = new HashMap<String, Test>();
        static HashMap<String, String> testUnitMap = new HashMap<String, String>();
        static HashMap<String, Integer> testPositionMap = new HashMap<String, Integer>();
	    
	static{
            
            testNameMap.put("ALTL", new TestDAOImpl().getTestByName("Transaminases ALTL"));
            testNameMap.put("ASTL", new TestDAOImpl().getTestByName("Transaminases ASTL"));
            testNameMap.put("CREJ2", new TestDAOImpl().getTestByName("Créatininémie"));
            testNameMap.put("GLU3", new TestDAOImpl().getTestByName("Glycémie"));
            testNameMap.put("GLU2", new TestDAOImpl().getTestByName("Glycémie"));
            
                
            /*
            testNameMap.put("ALTL", new TestDAOImpl().getTestByName("Transaminases GPT (37°C)"));
            testNameMap.put("ASTL", new TestDAOImpl().getTestByName("Transaminases"));
            testNameMap.put("CREJ2", new TestDAOImpl().getTestByName("Créatinine"));
            testNameMap.put("GLU2", new TestDAOImpl().getTestByName("Glucose"));
             */
            
                //System.out.println(testNameMap);
                
            
		AnalyzerDAO analyzerDAO = new AnalyzerDAOImpl();
		Analyzer analyzer = analyzerDAO.getAnalyzerByName("CobasIntegra400");
		ANALYZER_ID = analyzer.getId();
		
		
	} 
        
        
        static{
            
            
            testPositionMap.put("ALTL", 0);
            testPositionMap.put("ASTL", 1);
            testPositionMap.put("CREJ2", 2);   
            testPositionMap.put("GLU2", 3); 
            testPositionMap.put("GLU3", 4);
            
                //System.out.println(testPositionMap);
		
            
        }
        
        
        
        
       
        
        static{
		
                
                testUnitMap.put("ALTL", "/1|g/L");
                testUnitMap.put("ASTL", "/1|mg/L");
                testUnitMap.put("CREJ2", "/1|U/L");
                testUnitMap.put("GLU3", "/1|U/L");
                testUnitMap.put("GLU2", "/1|U/L");
		                
		System.out.println(testUnitMap);
		
		
		
	}
    
    /* (non-Javadoc)
     * @see us.mn.state.health.lims.analyzerimport.analyzerreaders.AnalyzerLineInserter#insert(java.util.List, java.lang.String)
     */
    @Override
    public boolean insert(List<String> lines, String currentUserId) {
        List<AnalyzerResults> results = new ArrayList<AnalyzerResults>();
        Integer Position = 0;
        
        for (Position = 0; Position < 5; Position++) {
        
            
                                for (Integer j = 1; j < lines.size(); j++) {        	
                                        System.out.println("processing line #: "  + j);
                                        line = lines.get(j).replace("\\s", "").trim();
                                        System.out.println(line);
                                        data = line.split("\\s");
                                        Integer Real_Position;

                                        AnalyzerResults aResult = new AnalyzerResults();
                                    String currentAccessionNumber = data [5].trim();
                                    String Result = data [13].trim();
                                    String testKey = data [3].trim();
                                    String date = data [1];  

                                      aResult.setTestId(testNameMap.get(testKey).getId());
                                      aResult.setTestName(testNameMap.get(testKey).getName());
                                      Real_Position = testPositionMap.get(testKey);
                                      aResult.setAnalyzerId(ANALYZER_ID);
                                      aResult.setAccessionNumber(data [5].trim());
                                      aResult.setIsControl(CheckControl(currentAccessionNumber));
                                      aResult.setCompleteDate(getTimestampFromDate(date));           
                                      aResult.setUnits(data [8].trim()); 
                                      aResult.setResult(Result.trim());

                                    if ((Result.isEmpty())) {
                                                         Result = data [12].trim();
                                                         aResult.setResult(Result.trim());
                                                 }   


                                    if (currentAccessionNumber.startsWith(CONTROL_ACCESSION_PREFIX)){
                                                         Result = data [14].trim();
                                                         aResult.setResult(Result.trim());
                                                        aResult.setUnits(data [9].trim());


                                                } 

                                     //System.out.println("***" + aResult.getAccessionNumber() + " " + aResult.getCompleteDate() + "***" + aResult.getResult()+ "***");
                                        
                                        if (Real_Position.equals(Position)) {
                                        results.add(aResult);
                                                                           }
                                     
                                            

                                }
       
        }
     
       return persistImport(currentUserId, results);  
  
    }

   
   private boolean CheckControl (String AccessionPrefix){
           
            boolean IsControl= false;
                    
        if (AccessionPrefix.startsWith(CONTROL_ACCESSION_PREFIX)) {
                IsControl = true;
		}
      return IsControl;
  
   }
   
  
    private Timestamp getTimestampFromDate(String dateTime) {
        return DateUtil.convertStringDateToTimestampWithPattern(dateTime, DATE_PATTERN);
    }

    @Override
    public String getError() {
        return "Cobas Integra 400 unable to write to database";
    }
}
