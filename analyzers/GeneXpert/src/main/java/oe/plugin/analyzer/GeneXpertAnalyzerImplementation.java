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
import java.util.List;

import org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerLineInserter;
import org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerReaderUtil;
import org.openelisglobal.analyzerimport.util.AnalyzerTestNameCache;
import org.openelisglobal.analyzerimport.util.MappedTestName;
import org.openelisglobal.analyzerresults.valueholder.AnalyzerResults;

public class GeneXpertAnalyzerImplementation extends AnalyzerLineInserter {

	static final String HBV = "Xpert HBV Viral Load";
	static final String HCV = "Xpert HCV Viral Load";
	static final String HIV_QUAL = "Xpert HIV-1 Qual";
	static final String HIV_VIRAL = "Xpert HIV-1 viral Load";
	static final String COV_2 = "Xpert Xpress SARS-CoV-2 assay";

	private AnalyzerReaderUtil readerUtil = new AnalyzerReaderUtil();

	public GeneXpertAnalyzerImplementation() {
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
		return false;
	}

	@Override
	public String getError() {
		return "GeneXpert analyzer unable to write to database";
	}

	public void addResult(List<AnalyzerResults> resultList, List<AnalyzerResults> notMatchedResults, String resultType,
			String resultValue, Date date, String accessionNumber, boolean isControl, String resultUnits,
			String analyzerTestId) {
		AnalyzerResults analyzerResults = createAnalyzerResult(resultType, resultValue, resultUnits, date,
				accessionNumber, isControl, analyzerTestId);
		if (analyzerResults.getTestId() != null) {
			addValueToResults(resultList, analyzerResults);
		} else {
			notMatchedResults.add(analyzerResults);
		}
	}

	private void addValueToResults(List<AnalyzerResults> resultList, AnalyzerResults result) {
		resultList.add(result);

		AnalyzerResults resultFromDB = readerUtil.createAnalyzerResultFromDB(result);
		if (resultFromDB != null) {
			resultList.add(resultFromDB);
		}

	}

	private AnalyzerResults createAnalyzerResult(String resultType, String resultValue, String resultUnits, Date date,
			String accessionNumber, boolean isControl, String analyzerTestId) {
		AnalyzerResults analyzerResults = new AnalyzerResults();
		MappedTestName mappedName = AnalyzerTestNameCache.getInstance().getMappedTest("GeneXpert", analyzerTestId);

		analyzerResults.setAnalyzerId(mappedName.getAnalyzerId());
		analyzerResults.setResult(resultValue);
		analyzerResults.setUnits(resultUnits);
		if (date != null) {
			analyzerResults.setCompleteDate(new Timestamp(date.getTime()));
		}
		analyzerResults.setAccessionNumber(accessionNumber);
		analyzerResults.setTestId(mappedName.getTestId());
		analyzerResults.setIsControl(isControl);
		analyzerResults.setTestName(mappedName.getOpenElisTestName());
		return analyzerResults;
	}

	public void persistImport(List<AnalyzerResults> resultList) {
		this.persistImport("1", resultList);
	}

}
