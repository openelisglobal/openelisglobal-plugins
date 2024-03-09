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

 import java.sql.Timestamp;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.List;
 
 import org.openelisglobal.analyzer.service.AnalyzerService;
 import org.openelisglobal.analyzer.valueholder.Analyzer;
 import org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerLineInserter;
 import org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerReaderUtil;
 import org.openelisglobal.analyzerimport.util.AnalyzerTestNameCache;
 import org.openelisglobal.analyzerimport.util.MappedTestName;
 import org.openelisglobal.analyzerresults.valueholder.AnalyzerResults;
 import org.openelisglobal.common.services.PluginAnalyzerService;
 import org.openelisglobal.spring.util.SpringContext;
 import org.openelisglobal.test.service.TestService;
 import org.openelisglobal.test.valueholder.Test;
 
 public class SysmexXNLAnalyzerImplementation extends AnalyzerLineInserter {
 
	 private TestService testService = SpringContext.getBean(TestService.class);
	 private AnalyzerService analyzerService = SpringContext.getBean(AnalyzerService.class);
 
	 static final String ANALYZER_TEST_WBC = "WBC";
	 static final String ANALYZER_TEST_RBC = "RBC";
	 static final String ANALYZER_TEST_HGB = "HGB";
	 static final String ANALYZER_TEST_HCT = "HCT";
	 static final String ANALYZER_TEST_MCV = "MCV";
	 static final String ANALYZER_TEST_MCH = "MCH";
	 static final String ANALYZER_TEST_MCHC = "MCHC";
	 static final String ANALYZER_TEST_PLT = "PLT";
	 static final String ANALYZER_TEST_NEUT_PERCENT = "NEUT%";
	 static final String ANALYZER_TEST_LYMPH_PERCENT = "LYMPH%";
	 static final String ANALYZER_TEST_MONO_PERCENT = "MONO%";
	 static final String ANALYZER_TEST_EO_PERCENT = "EO%";
	 static final String ANALYZER_TEST_BASO_PERCENT = "BASO%";
	 static final String ANALYZER_TEST_NEUT_COUNT = "NEUT#";
	 static final String ANALYZER_TEST_LYMPH_COUNT = "LYMPH#";
	 static final String ANALYZER_TEST_MONO_COUNT = "MONO#";
	 static final String ANALYZER_TEST_EO_COUNT = "EO#";
	 static final String ANALYZER_TEST_BASO_COUNT = "BASO#";
	 static final String ANALYZER_TEST_RDWSD = "RDW-SD";
	 static final String ANALYZER_TEST_RDWCV = "RDW-CV";
	 static final String ANALYZER_TEST_PDW = "PDW";
	 static final String ANALYZER_TEST_MPV = "MPV";
	 static final String ANALYZER_TEST_PLCR = "P-LCR";
	 static final String ANALYZER_TEST_PCT = "PCT";
	 static final String ANALYZER_TEST_RET_PERCENT = "RET%";
	 static final String ANALYZER_TEST_RET_COUNT = "RET#";
	 static final String ANALYZER_TEST_IRF = "IRF";
	 static final String ANALYZER_TEST_LFR = "LFR";
	 static final String ANALYZER_TEST_MFR = "MFR";
	 static final String ANALYZER_TEST_HFR = "HFR";
	 static final String ANALYZER_TEST_LWBC = "LWBC";
 
	 static final String LOINC_WBC = "";
	 static final String LOINC_RBC = "";
	 static final String LOINC_HGB = "";
	 static final String LOINC_HCT = "";
	 static final String LOINC_MCV = "";
	 static final String LOINC_MCH = "";
	 static final String LOINC_MCHC = "";
	 static final String LOINC_PLT = "";
	 static final String LOINC_NEUT_PERCENT = "";
	 static final String LOINC_LYMPH_PERCENT = "";
	 static final String LOINC_MONO_PERCENT = "";
	 static final String LOINC_EO_PERCENT = "";
	 static final String LOINC_BASO_PERCENT = "";
	 static final String LOINC_NEUT_COUNT = "";
	 static final String LOINC_LYMPH_COUNT = "";
	 static final String LOINC_MONO_COUNT = "";
	 static final String LOINC_EO_COUNT = "";
	 static final String LOINC_BASO_COUNT = "";
	 static final String LOINC_RDWSD = "";
	 static final String LOINC_RDWCV = "";
	 static final String LOINC_PDW = "";
	 static final String LOINC_MPV = "";
	 static final String LOINC_PLCR = "";
	 static final String LOINC_PCT = "";
	 static final String LOINC_RET_PERCENT = "";
	 static final String LOINC_RET_COUNT = "";
	 static final String LOINC_IRF = "";
	 static final String LOINC_LFR = ""; 
	 static final String LOINC_MFR = "";
	 static final String LOINC_HFR = "";
	 static final String LOINC_LWBC = "";
 
	 private final String HEADER_RECORD_IDENTIFIER = "H";
	 private final String PATIENT_RECORD_IDENTIFIER = "P";
	 private final String ORDER_RECORD_IDENTIFIER = "O";
	 private final String RESULT_RECORD_IDENTIFIER = "R";
	 private final String END_RECORD_IDENTIFIER = "L";
	 private final String DEFAULT_FIELD_DELIMITER = "\\|";
	 private final String DEFAULT_SUBFIELD_DELIMITER = "\\^";
	 private final String DEFAULT_REPEATER_DELIMITER = "\\\\";
	 private final String TEST_COMMUNICATION_IDENTIFIER = "M|1|106";
 
	 private String ANALYZER_ID;
	 private HashMap<String, List<Test>> testLoincMap = new HashMap<>();
 
	 private TestService testService = SpringContext.getBean(TestService.class);
	 private SampleService sampleService = SpringContext.getBean(SampleService.class);
	 private AnalyzerService analyzerService = SpringContext.getBean(AnalyzerService.class);
	 private AnalysisService analysisService = SpringContext.getBean(AnalysisService.class);
 
	 private String ANALYZER_ID;
	 private HashMap<String, List<Test>> testLoincMap = new HashMap<>();
 
	 private AnalyzerReaderUtil readerUtil = new AnalyzerReaderUtil();
 
	 public SysmexXNLAnalyzerAnalyzerImplementation() {
		 testLoincMap.put(ANALYZER_TEST_WBC, testService.getTestsByLoincCode(LOINC_WBC));
		 testLoincMap.put(ANALYZER_TEST_RBC, testService.getTestsByLoincCode(LOINC_RBC));
		 testLoincMap.put(ANALYZER_TEST_HGB, testService.getTestsByLoincCode(LOINC_HGB));
		 testLoincMap.put(ANALYZER_TEST_HCT, testService.getTestsByLoincCode(LOINC_HCT));
		 testLoincMap.put(ANALYZER_TEST_MCV, testService.getTestsByLoincCode(LOINC_MCV));
		 testLoincMap.put(ANALYZER_TEST_MCH, testService.getTestsByLoincCode(LOINC_MCH));
		 testLoincMap.put(ANALYZER_TEST_MCHC, testService.getTestsByLoincCode(LOINC_MCHC));
		 testLoincMap.put(ANALYZER_TEST_PLT, testService.getTestsByLoincCode(LOINC_PLT));
		 testLoincMap.put(ANALYZER_TEST_NEUT_PERCENT, testService.getTestsByLoincCode(LOINC_NEUT_PERCENT));
		 testLoincMap.put(ANALYZER_TEST_LYMPH_PERCENT, testService.getTestsByLoincCode(LOINC_LYMPH_PERCENT));
		 testLoincMap.put(ANALYZER_TEST_MONO_PERCENT, testService.getTestsByLoincCode(LOINC_MONO_PERCENT));
		 testLoincMap.put(ANALYZER_TEST_EO_PERCENT, testService.getTestsByLoincCode(LOINC_EO_PERCENT));
		 testLoincMap.put(ANALYZER_TEST_BASO_PERCENT, testService.getTestsByLoincCode(LOINC_BASO_PERCENT));
		 testLoincMap.put(ANALYZER_TEST_NEUT_COUNT, testService.getTestsByLoincCode(LOINC_NEUT_COUNT));
		 testLoincMap.put(ANALYZER_TEST_LYMPH_COUNT, testService.getTestsByLoincCode(LOINC_LYMPH_COUNT));
		 testLoincMap.put(ANALYZER_TEST_MONO_COUNT, testService.getTestsByLoincCode(LOINC_MONO_COUNT));
		 testLoincMap.put(ANALYZER_TEST_EO_COUNT, testService.getTestsByLoincCode(LOINC_EO_COUNT));
		 testLoincMap.put(ANALYZER_TEST_BASO_COUNT, testService.getTestsByLoincCode(LOINC_BASO_COUNT));
		 testLoincMap.put(ANALYZER_TEST_RDWSD, testService.getTestsByLoincCode(LOINC_RDWSD));
		 testLoincMap.put(ANALYZER_TEST_RDWCV, testService.getTestsByLoincCode(LOINC_RDWCV));
		 testLoincMap.put(ANALYZER_TEST_PDW, testService.getTestsByLoincCode(LOINC_PDW));
		 testLoincMap.put(ANALYZER_TEST_MPV, testService.getTestsByLoincCode(LOINC_MPV));
		 testLoincMap.put(ANALYZER_TEST_PLCR, testService.getTestsByLoincCode(LOINC_PLCR));
		 testLoincMap.put(ANALYZER_TEST_PCT, testService.getTestsByLoincCode(LOINC_PCT));
		 testLoincMap.put(ANALYZER_TEST_RET_PERCENT, testService.getTestsByLoincCode(LOINC_RET_PERCENT));
		 testLoincMap.put(ANALYZER_TEST_RET_COUNT, testService.getTestsByLoincCode(LOINC_RET_COUNT));
		 testLoincMap.put(ANALYZER_TEST_IRF, testService.getTestsByLoincCode(LOINC_IRF));
		 testLoincMap.put(ANALYZER_TEST_LFR, testService.getTestsByLoincCode(LOINC_LFR));
		 testLoincMap.put(ANALYZER_TEST_MFR, testService.getTestsByLoincCode(LOINC_MFR));
		 testLoincMap.put(ANALYZER_TEST_HFR, testService.getTestsByLoincCode(LOINC_HFR));
		 testLoincMap.put(ANALYZER_TEST_LWBC, testService.getTestsByLoincCode(LOINC_LWBC));
 
		 Analyzer analyzer = analyzerService.getAnalyzerByName("SysmexXNLAnalyzer");
		 ANALYZER_ID = analyzer.getId();
	 }
 
	 // example message:
	 // H|\^&|||XN-35^00-00^11001^^^^12345678||||||||E1394-97<CR>
	 // P|1|||123456|^Jim^Brown||20010820|M|||||^Dr.1||||||||||||^^^WEST<CR>
	 // O|1||^^ ABCDE1234567890^B|^^^^WBC\^^^^RBC\···\^^^^BASO#|||||||N||||||||||||||F<CR>
	 // R|1|^^^^WBC^1|7.80|10*3/uL||N||||||20011116101000<CR>
	 // R|2|^^^^RBC^1|10.00|10*6/uL||A||||||20011116101000<CR>
	 // ……
	 // R|18|^^^^PLT_C(S)?|200|||A||||||20011116101000<CR>
	 // C|1||Patient comments<CR>
	 // L|1|N<CR>
	 /*
	  * (non-Javadoc)
	  *
	  * @see
	  * org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerLineInserter#insert
	  * (java.util.List, java.lang.String)
	  */
	 @Override
	 public boolean insert(List<String> lines, String currentUserId) {
 
		 String patientRecord = null;
		 String orderRecord = null;
 
		 List<AnalyzerResults> results = new ArrayList<>();
		 for (String line : lines) {
			 if (line.startsWith(TEST_COMMUNICATION_IDENTIFIER)) {
				 LogEvent.logInfo(this.getClass().getName(), "insert", "this is a test communication record for Aquios");
			 }
			 if (line.startsWith(PATIENT_RECORD_IDENTIFIER)) {
				 if (patientRecord != null) {
					 patientRecord = null;
					 orderRecord = null;
				 }
				 patientRecord = line;
			 }
			 if (line.startsWith(ORDER_RECORD_IDENTIFIER)) {
				 orderRecord = line;
			 }
			 if (line.startsWith(RESULT_RECORD_IDENTIFIER)) {
				 LogEvent.logDebug(this.getClass().getName(), "insert", "adding result");
 
				 addRecordsToResults(patientRecord, orderRecord, line, results, currentUserId);
			 }
			 if (line.startsWith(END_RECORD_IDENTIFIER)) {
				 LogEvent.logDebug(this.getClass().getName(), "insert", "end Aquios of record");
				 break;
			 }
		 }
		 return persistImport(currentUserId, results);
	 }
 
	 @Override
	 public String getError() {
		 return "SysmexXNLAnalyzer analyzer unable to write to database";
	 }
 
	 //example patient record:
	 // P|1<CR>
	 // example order record:
	  // O|1||^^ 12345ABCDE^B|^^^^WBC\^^^^RBC\^^^^HGB\^^^^HCT\ ^^^^MCV\^^^^MCH\^^^^MCHC\^^^^PLT\^^^^W-SCR\^^^^W-MCR\^^^^W-LCR\^^^^W-SCC\^^^^W-MCC\^^^^W-LCC\^^^^RDW-SD\^^^^RDW-CV\^^^^PDW\^^^^MPV\^^^^P-LCR|||||||N||||||||||||||F<CR>
	 // example result records:
	 // R|1|^^^^WBC^26|78|10*2/uL||N||||123456789012345||20011221163530<CR>
	 private void addRecordsToResults(String patientRecord, String orderRecord, String resultRecord,
			 List<AnalyzerResults> results, String currentUserId) {
		 String[] patientRecordFields = patientRecord.split(DEFAULT_FIELD_DELIMITER);
		 String[] orderRecordFields = orderRecord.split(DEFAULT_FIELD_DELIMITER);
		 String[] orderTestIdFields = orderRecordFields[4].split(DEFAULT_REPEATER_DELIMITER);
		 String[] orderIdFields = orderRecordFields[3].split(DEFAULT_SUBFIELD_DELIMITER);
		 String[] resultRecordFields = resultRecord.split(DEFAULT_FIELD_DELIMITER);
		 String[] resultTestIdField = resultRecordFields[2].split(DEFAULT_SUBFIELD_DELIMITER);
		 List<String> orderTestIds = new ArrayList<>();
		 for (String orderIdField : orderTestIdFields) {
			 String[] orderIds = orderIdField.split(DEFAULT_SUBFIELD_DELIMITER);
			 String orderTestId = orderIds.length >= 5 ? orderIds[4] : "";
			 if (GenericValidator.isBlankOrNull(orderTestId)) {
				 LogEvent.logWarn(this.getClass().getSimpleName(), "addRecordsToResults", "order analysis parameter name is not present")
			 }
			 orderTestIds.add(orderTestId);
		 }
		 if (orderTestIds.size() <= 0) {
			 LogEvent.logWarn(this.getClass().getSimpleName(), "addRecordsToResults", "order analysis has no tests specified")
		 }
		 String resultTestId = resultTestIdField.length >= 5 ? resultTestIdField[4] : "";
 
		 String currentAccessionNumber = orderIdFields[2].trim();
		 Sample sample = sampleService.getSampleByAccessionNumber(currentAccessionNumber);
		 Test test = null;
		 if (sample != null) {
			 for (Analysis curAnalysis : analysisService.getAnalysesBySampleId(sample.getId())) {
				 List<Test> possibleTests = testLoincMap.get(resultTestId);
				 if ((possibleTests == null || possibleTests.size() == 0) && resultTestId.contains("+")) {
					 possibleTests = testLoincMap.get(resultTestId);
				 }
				 if (possibleTests != null) {
					 for (Test curTest : possibleTests) {
						 if (curTest.getLoinc() != null && curTest.getLoinc().equals(curAnalysis.getTest().getLoinc())) {
							 test = curAnalysis.getTest();
							 break;
						 }
					 }
				 }
			 }
		 }
 
		 if (test == null) {
			 LogEvent.logError(this.getClass().getName(), "addRecordsToResults",
					 "can't import a result if order does not have that test ordered");
			 return;
		 }
 
		 AnalyzerResults analyzerResults = addResult(results, null, "N", resultRecordFields[3], 
			 DateUtil.convertStringDateToTimestampWithPattern(resultRecordFields[12], "yyyyMMddHHmmss"), 
			 currentAccessionNumber, false, resultRecordFields[4], test);
		 LogEvent.logDebug(this.getClass().getName(), "addResultLine", "***" + analyzerResult.getAccessionNumber() + " "
				 + analyzerResult.getCompleteDate() + " " + analyzerResult.getResult());
	 }
 
	 public AnalyzerResults addResult(List<AnalyzerResults> resultList, List<AnalyzerResults> notMatchedResults, String resultType,
			 String resultValue, Date date, String accessionNumber, boolean isControl, String resultUnits,
			 Test test) {
		 LogEvent.logDebug(this.getClass().getName(), "addResult",
			 "adding result for lab Number: " + accessionNumber);
		 AnalyzerResults analyzerResults = createAnalyzerResult(resultType, resultValue, resultUnits, date,
				 accessionNumber, isControl, test);
		 if (analyzerResults.getTestId() != null) {
			 addValueToResults(resultList, analyzerResults);
		 } else {
			 LogEvent.logWarn(this.getClass().getName(), "addResult",
				 "no matching result for " + accessionNumber);
			 notMatchedResults.add(analyzerResults);
		 }
		 return analyzerResults;
	 }
 
	 private void addValueToResults(List<AnalyzerResults> resultList, AnalyzerResults result) {
		 resultList.add(result);
		 LogEvent.logDebug(this.getClass().getName(), "addValueToResults",
		 "searching for matching analysis for " + result.getAccessionNumber());		
		 AnalyzerResults resultFromDB = readerUtil.createAnalyzerResultFromDB(result);
		 if (resultFromDB != null) {
			 LogEvent.logWarn(this.getClass().getName(), "addValueToResults",
				 "no resultFromDB for " + result.getAccessionNumber());
			 resultList.add(resultFromDB);
		 }
 
	 }
 
	 private AnalyzerResults createAnalyzerResult(String resultType, String resultValue, String resultUnits, Date date,
			 String accessionNumber, boolean isControl, Test test) {
		 LogEvent.logDebug(this.getClass().getName(), "createAnalyzerResult",
			 "creating analyzer result for " + accessionNumber);		
				 
		 AnalyzerResults analyzerResults = new AnalyzerResults();
 
		 analyzerResults.setAnalyzerId(ANALYZER_ID);
		 analyzerResults.setResult(resultValue);
		 analyzerResults.setUnits(resultUnits);
		 if (date != null) {
			 analyzerResults.setCompleteDate(new Timestamp(date.getTime()));
		 }
		 analyzerResults.setAccessionNumber(accessionNumber);
		 analyzerResults.setTestId(test.getId());
		 analyzerResults.setIsControl(isControl);
		 analyzerResults.setTestName(test.getLocalizedTestName().getLocalizedValue());
		 return analyzerResults;
	 }
 
	 public void persistImport(List<AnalyzerResults> resultList) {
		 this.persistImport("1", resultList);
	 }
 
 }
 