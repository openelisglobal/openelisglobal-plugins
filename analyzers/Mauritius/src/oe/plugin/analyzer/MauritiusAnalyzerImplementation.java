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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.openelisglobal.analyzer.service.AnalyzerService;
import org.openelisglobal.analyzer.valueholder.Analyzer;
import org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerLineInserter;
import org.openelisglobal.analyzerresults.valueholder.AnalyzerResults;
import org.openelisglobal.common.log.LogEvent;
import org.openelisglobal.spring.util.SpringContext;
import org.openelisglobal.test.service.TestService;
import org.openelisglobal.test.valueholder.Test;

public class MauritiusAnalyzerImplementation extends AnalyzerLineInserter {

//	private static String DATE_PATTERN = "yyyyMMdd";

	private static final String CONTROL_ACCESSION_PREFIX = "CNEG";
	private static final String CSV_DELIMETER = ",";
	private static final String TEST_NAME = "SARS-CoV-2 RNA";

	private List<String> columnHeaders;
	private HashMap<String, Test> testNameMap = new HashMap<>();

	private String ANALYZER_ID;
	
	public MauritiusAnalyzerImplementation() {
		testNameMap.put("SARS-CoV-2 RNA",
				SpringContext.getBean(TestService.class).getTestByLocalizedName(TEST_NAME, Locale.ENGLISH));
		Analyzer analyzer = SpringContext.getBean(AnalyzerService.class).getAnalyzerByName("MauritiusAnalyzer");
		ANALYZER_ID = analyzer.getId();
	}

	public void addResultLine(String resultLine, List<AnalyzerResults> results) {
		String[] resultData = resultLine.split(CSV_DELIMETER, -1);
		String currentAccessionNumber = resultData[getIndexOfColumn("Sample")].replace("\"", "").trim();
//		String date = resultData[4].replace("\"", "");

		AnalyzerResults analyzerResult = new AnalyzerResults();

		analyzerResult.setTestId(testNameMap.get(TEST_NAME).getId());
		analyzerResult.setTestName(testNameMap.get(TEST_NAME).getName());
		analyzerResult.setResult(resultData[getIndexOfColumn("Call")].replace("\"", "").trim());
		analyzerResult.setAnalyzerId(ANALYZER_ID);
		analyzerResult.setAccessionNumber(currentAccessionNumber);
		analyzerResult.setIsControl(isControl(currentAccessionNumber));

//		analyzerResult.setUnits(resultData[getIndexOfColumn("Cq")].replace("\"", ""));
//	 	aResult.setReadOnly(CheckReadOnly (testKey));
//		analyzerResult.setCompleteDate(getTimestampFromDate(date));

		LogEvent.logDebug(this.getClass().getName(), "addResultLine", "***" + analyzerResult.getAccessionNumber() + " "
				+ analyzerResult.getCompleteDate() + " " + analyzerResult.getResult());

		results.add(analyzerResult);

	}

	public boolean isColumnHeaderRow(String line) {
		return line.contains("Well" + CSV_DELIMETER);
	}

	public void setColumnHeaders(String columnHeaderLine) {
		columnHeaders = Arrays.asList(columnHeaderLine.split(CSV_DELIMETER, -1));
	}

	private int getIndexOfColumn(String columnHeader) {
		for (int i = 0; i < columnHeaders.size(); ++i) {
			if (columnHeaders.get(i).equals(columnHeader)) {
				return i;
			}
		}

		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerLineInserter#insert
	 * (java.util.List, java.lang.String)
	 */
	@Override
	public boolean insert(List<String> lines, String currentUserId) {
		List<AnalyzerResults> results = new ArrayList<>();

		int columnHeaderRowIndex = lines.size();

		for (int i = 0; i < lines.size(); ++i) {
			String line = lines.get(i);
			if (i > columnHeaderRowIndex) {
				addResultLine(line, results);
			} else if (isColumnHeaderRow(line)) {
				setColumnHeaders(line);
				columnHeaderRowIndex = i;
			}
		}
		return persistImport(currentUserId, results);
	}

	private boolean isControl(String accessionPrefix) {
		return accessionPrefix.startsWith(CONTROL_ACCESSION_PREFIX);
	}

	@Override
	public String getError() {
		return "Mauritius analyzer unable to write to database";
	}
}
