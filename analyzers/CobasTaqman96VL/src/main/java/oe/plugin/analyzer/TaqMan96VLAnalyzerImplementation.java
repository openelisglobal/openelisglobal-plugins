/**
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
 * Copyright (C) CIRG, University of Washington, Seattle WA.  All Rights Reserved.
 *
 */
package oe.plugin.analyzer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.openelisglobal.analysis.service.AnalysisService;
import org.openelisglobal.analysis.valueholder.Analysis;
import org.openelisglobal.analyzer.service.AnalyzerService;
import org.openelisglobal.analyzer.valueholder.Analyzer;
import org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerLineInserter;
import org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerReaderUtil;
import org.openelisglobal.analyzerresults.valueholder.AnalyzerResults;
import org.openelisglobal.common.services.StatusService;
import org.openelisglobal.common.util.DateUtil;
import org.openelisglobal.internationalization.MessageUtil;
import org.openelisglobal.sample.service.SampleService;
import org.openelisglobal.spring.util.SpringContext;
import org.openelisglobal.test.service.TestService;
import org.openelisglobal.test.valueholder.Test;

@SuppressWarnings("unused")
public class TaqMan96VLAnalyzerImplementation extends AnalyzerLineInserter {
	private static final String UNDER_THREASHOLD = "< LL";
	private static final double THREASHOLD = 20.0;
	private int ORDER_NUMBER = 0;
	private int ORDER_DATE = 0;
	private int RESULT = 0;
	private int SAMPLE_TYPE = 0;
	private int UNIT = 0;
	// private static final String DELIMITER = "\\t";
	private static String DELIMITER = "\\t";
	private static final String DATE_PATTERN = "yyyy/MM/dd HH:mm:ss";
	static String ANALYZER_ID;
	private final String projectCode = MessageUtil.getMessage("sample.entry.project.LART");

	private AnalyzerReaderUtil readerUtil = new AnalyzerReaderUtil();
	private String error;
	Test test = SpringContext.getBean(TestService.class).getActiveTestByName("Viral Load").get(0);
	String validStatusId = StatusService.getInstance().getStatusID(StatusService.AnalysisStatus.Finalized);
	AnalysisService analysisService = SpringContext.getBean(AnalysisService.class);

	static {
		AnalyzerService analyzerService = SpringContext.getBean(AnalyzerService.class);
		Analyzer analyzer = analyzerService.getAnalyzerByName("TaqMan96VLAnalyzer");
		ANALYZER_ID = analyzer.getId();
	}

	public boolean insert(List<String> lines, String currentUserId) {
		this.error = null;

		List<AnalyzerResults> results = new ArrayList<AnalyzerResults>();

		boolean columnsFound = manageColumnsIndex(lines);

		if (!columnsFound) {
			this.error = "Cobas Taqman 96 VL analyzer: Unable to find correct columns in file";
			return false;
		}

		for (int i = getColumnsLine(lines) + 1; i < lines.size(); i++) {
			createAnalyzerResultFromLine((String) lines.get(i), results);
		}

		return persistImport(currentUserId, results);
	}

	private boolean manageColumnsIndex(List<String> lines) {
		if (getColumnsLine(lines) < 0)
			return false;
		DELIMITER = ((String) lines.get(getColumnsLine(lines))).substring(14, 15);
		String[] fields = ((String) lines.get(getColumnsLine(lines))).split(DELIMITER);

		for (int i = 0; i < fields.length; i++) {
			String header = fields[i].replace("\"", "");

			if ("Order Number".equals(header))
				ORDER_NUMBER = i;
			else if ("Order Date/Time".equals(header))
				ORDER_DATE = i;
			else if ("Result".equals(header))
				RESULT = i;
			else if ("Sample Type".equals(header)) {
				SAMPLE_TYPE = i;
			} else if ("Unit".equals(header)) {
				UNIT = i;
			}
		}

		return (ORDER_DATE != 0) && (ORDER_NUMBER != 0) && (RESULT != 0) && (SAMPLE_TYPE != 0) && (UNIT != 0);
	}

	public String getError() {
		return this.error;
	}

	private void addValueToResults(List<AnalyzerResults> resultList, AnalyzerResults result) {

		if (result.getIsControl()) {
			resultList.add(result);
			return;
		}
		SampleService sampleServ = SpringContext.getBean(SampleService.class);
		if (!result.getAccessionNumber().startsWith(projectCode) || sampleServ.getSampleByAccessionNumber(result.getAccessionNumber()) == null)
			return;

		List<Analysis> analyses = analysisService.getAnalysisByAccessionAndTestId(result.getAccessionNumber(),
				result.getTestId());
		for (Analysis analysis : analyses) {
			if (analysis.getStatusId().equals(validStatusId))
				return;

		}

		resultList.add(result);

		AnalyzerResults resultFromDB = this.readerUtil.createAnalyzerResultFromDB(result);
		if (resultFromDB != null)
			resultList.add(resultFromDB);
	}

	private void createAnalyzerResultFromLine(String line, List<AnalyzerResults> resultList) {
		String[] fields = line.split(DELIMITER);

		AnalyzerResults analyzerResults = new AnalyzerResults();

		String result = getAppropriateResults(fields[RESULT]);
		String accessionNumber = fields[this.ORDER_NUMBER].replace("\"", "").trim();
		accessionNumber = accessionNumber.replace(" ", "");
		if (accessionNumber.startsWith(projectCode) && accessionNumber.length() >= 9)
			accessionNumber = accessionNumber.substring(0, 9);

		analyzerResults.setAnalyzerId(ANALYZER_ID);
		analyzerResults.setResult(result);
		analyzerResults.setUnits(UNDER_THREASHOLD.equals(result) ? "" : fields[UNIT].replace("\"", "").trim());
		analyzerResults.setCompleteDate(DateUtil
				.convertStringDateToTimestampWithPattern(fields[ORDER_DATE].replace("\"", "").trim(), DATE_PATTERN));
		analyzerResults.setTestId(test.getId());
		analyzerResults.setIsControl(!"S".equals(fields[SAMPLE_TYPE].replace("\"", "").trim()));
		analyzerResults.setTestName(test.getName());
		analyzerResults.setResultType("A");

		if (analyzerResults.getIsControl()) {
			accessionNumber = accessionNumber + ":" + fields[this.SAMPLE_TYPE].replace("\"", "").trim();
		}

		analyzerResults.setAccessionNumber(accessionNumber);

		addValueToResults(resultList, analyzerResults);
	}

	private String getAppropriateResults(String result) {
		result = result.replace("\"", "").trim();
		if ("Target Not Detected".equalsIgnoreCase(result) || "Below range".equalsIgnoreCase(result)) {
			result = UNDER_THREASHOLD;
		} else {

			String workingResult = result.split("\\(")[0].replace("<", "").replace("E", "");
			String[] splitResult = workingResult.split("\\+");

			try {
				Double resultAsDouble = Double.parseDouble(splitResult[0])
						* Math.pow(10, Double.parseDouble(splitResult[1]));

				if (resultAsDouble <= THREASHOLD) {
					result = UNDER_THREASHOLD;
				} else {
					result = String.valueOf((int) (Math.round(resultAsDouble))) + result.substring(result.indexOf("("));
				}
			} catch (NumberFormatException e) {
				return "XXXX";
			}
		}

		return result;
	}

	public int getColumnsLine(List<String> lines) {
		for (int k = 0; k < lines.size(); k++) {
			if (lines.get(k).contains("Patient Name") && lines.get(k).contains("Patient ID")
					&& lines.get(k).contains("Order Number") && lines.get(k).contains("Sample ID")
					&& lines.get(k).contains("Test") && lines.get(k).contains("Result"))

				return k;

		}

		return -1;
	}

}