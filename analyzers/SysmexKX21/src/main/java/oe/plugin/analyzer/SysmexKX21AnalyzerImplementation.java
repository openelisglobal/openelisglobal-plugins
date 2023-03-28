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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openelisglobal.analysis.dao.AnalysisDAO;
import org.openelisglobal.analysis.daoimpl.AnalysisDAOImpl;
import org.openelisglobal.analysis.valueholder.Analysis;
import org.openelisglobal.analyzer.service.AnalyzerService;
import org.openelisglobal.analyzer.valueholder.Analyzer;
import org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerLineInserter;
import org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerReaderUtil;
import org.openelisglobal.analyzerresults.valueholder.AnalyzerResults;
import org.openelisglobal.common.services.StatusService;
import org.openelisglobal.common.util.ConfigurationProperties;
import org.openelisglobal.common.util.ConfigurationProperties.Property;
import org.openelisglobal.common.util.DateUtil;
import org.openelisglobal.sample.service.SampleService;
import org.openelisglobal.spring.util.SpringContext;
import org.openelisglobal.test.service.TestService;
import org.openelisglobal.test.valueholder.Test;

public class SysmexKX21AnalyzerImplementation extends AnalyzerLineInserter {
	private int ORDER_NUMBER_INDEX = -1;
	private int ORDER_DAY_INDEX = -1;
	private int ORDER_HOUR_INDEX = -1;

	private static String DELIMITER = ";";
	static String ANALYZER_ID;
	private static final String CONTROL_ACCESSION_PREFIX = "QC-";
	static String DATE_PATTERN = "dd/MM/yyyy hh:mm";
	static HashMap<String, Test> testHeaderNameMap = new HashMap<String, Test>();
	HashMap<String, String> indexTestMap = new HashMap<String, String>();
	static HashMap<String, String> scaleIndexMap = new HashMap<String, String>();

	private AnalyzerReaderUtil readerUtil = new AnalyzerReaderUtil();
	// private final String projectCode =
	// StringUtil.getMessageForKey("sample.entry.project.LART");
	private final String projectCode = ConfigurationProperties.getInstance()
			.getPropertyValue(Property.ACCESSION_NUMBER_PREFIX);

	String validStatusId = StatusService.getInstance().getStatusID(StatusService.AnalysisStatus.Finalized);
	AnalysisDAO analysisDao = new AnalysisDAOImpl();

	static {

		testHeaderNameMap.put("GB",
				SpringContext.getBean(TestService.class).getTestByName("Numération des globules blancs"));// .getTestByGUID("0e240569-c095-41c7-bfd2-049527452f16"));
		testHeaderNameMap.put("GR",
				SpringContext.getBean(TestService.class).getTestByName("Numération des globules rouges"));// .getTestByGUID("fe6405c8-f96b-491b-95c9-b1f635339d6a"));
		testHeaderNameMap.put("HB", SpringContext.getBean(TestService.class).getTestByName("Hémoglobine"));// .getTestByGUID("cecea358-1fa0-44b2-8185-d8c010315f78"));
		testHeaderNameMap.put("Hct", SpringContext.getBean(TestService.class).getTestByName("Hématocrite"));// .getTestByGUID("6792a51e-050b-4493-88ca-6f490c20cc5c"));
		testHeaderNameMap.put("VGM", SpringContext.getBean(TestService.class).getTestByName("Volume Globulaire Moyen"));// .getTestByGUID("ddce6c12-e319-455f-9f48-2f6ff363a246"));
		testHeaderNameMap.put("TCMH",
				SpringContext.getBean(TestService.class).getTestByName("Teneur Corpusculaire Moyenne en Hémoglobine"));// .getTestByGUID("bf497153-ba88-4fe8-83ee-c144229d7628"));
		testHeaderNameMap.put("CCMH", SpringContext.getBean(TestService.class)
				.getTestByName("Concentration Corpusculaire Moyenne en Hémoglobine"));// .getTestByGUID("8ab87a81-6b6b-4d4b-b53b-fac57109e393"));
		testHeaderNameMap.put("PLT", SpringContext.getBean(TestService.class).getTestByName("Plaquette"));// .getTestByGUID("88b7d8d3-e82b-441f-aff3-1410ba2850a5"));
		testHeaderNameMap.put("GRAN%",
				SpringContext.getBean(TestService.class).getTestByName("Polynucléaires Neutrophiles (%)"));// .getTestByGUID("0c25692f-a321-4e9c-9722-ca73f6625cb9"));
		testHeaderNameMap.put("LYM%", SpringContext.getBean(TestService.class).getTestByName("Lymphocytes (%)"));// .getTestByGUID("eede92e7-d141-4c76-ab6e-b24ccfc84215"));
		testHeaderNameMap.put("MONO%", SpringContext.getBean(TestService.class).getTestByName("Monocytes (%)"));// .getTestByGUID("9eece97f-04f3-4381-b378-2a9ac08a535a"));
		testHeaderNameMap.put("GRAN#",
				SpringContext.getBean(TestService.class).getTestByName("Polynucléaires Neutrophiles (Abs)"));// .getTestByGUID("9eece97f-04f3-4381-b378-2a9ac08a535a"));
		testHeaderNameMap.put("LYM#", SpringContext.getBean(TestService.class).getTestByName("Lymphocytes (Abs)"));// .getTestByGUID("eede92e7-d141-4c76-ab6e-b24ccfc84215"));
		testHeaderNameMap.put("MONO#", SpringContext.getBean(TestService.class).getTestByName("Monocytes (Abs)"));// .getTestByGUID("9eece97f-04f3-4381-b378-2a9ac08a535a"));

		// testHeaderNameMap.put("EO%(10^(-1)%)",
		// SpringContext.getBean(TestService.class).getTestByName("Eo
		// %"));//.getTestByGUID("50b568e8-e9da-428d-9697-8080bca7377b"));
		// testHeaderNameMap.put("BASO%(10^(-1)%)",
		// SpringContext.getBean(TestService.class).getTestByName("Baso
		// %"));//.getTestByGUID("a41fcfb4-e3ba-4add-ac5d-56fae322cb9e"));

		// System.out.println(testHeaderNameMap);

		scaleIndexMap.put("GB", "1,10^3uL");
		scaleIndexMap.put("GR", "1,10^6uL");
		scaleIndexMap.put("HB", "1,g/dL");
		scaleIndexMap.put("Hct", "1,%");
		scaleIndexMap.put("VGM", "1,fL");
		scaleIndexMap.put("TCMH", "1,pg");
		scaleIndexMap.put("CCMH", "1,g/dL");
		scaleIndexMap.put("PLT", "1,10^3/uL");
		scaleIndexMap.put("GRAN%", "1,%");
		scaleIndexMap.put("LYM%", "1,%");
		scaleIndexMap.put("MONO%", "1,%");
		scaleIndexMap.put("GRAN#", "1000,/mm3");
		scaleIndexMap.put("LYM#", "1000,/mm3");
		scaleIndexMap.put("MONO#", "1000,/mm3");

		AnalyzerService analyzerService = SpringContext.getBean(AnalyzerService.class);
		Analyzer analyzer = analyzerService.getAnalyzerByName("SysmexKX21Analyzer");
		ANALYZER_ID = analyzer.getId();

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

		List<AnalyzerResults> results = new ArrayList<AnalyzerResults>();

		boolean columnsFound = manageColumnsIndex(lines);

		if (!columnsFound) {
			System.out.println("Sysmex KX21 analyzer: Unable to find correct columns in file");
			return false;
		}

		for (int i = getColumnsLine(lines) + 1; i < lines.size(); ++i) {
			createAnalyzerResultFromLine(lines.get(i), results);
		}

		return persistImport(currentUserId, results);

	}

	private Timestamp getTimestampFromDate(String dateTime) {
		return DateUtil.convertStringDateToTimestampWithPattern(dateTime, DATE_PATTERN);
	}

	@Override
	public String getError() {
		return "KX21 analyzer unable to write to database";
	}

	private String[] getAppropriateResults(String result, String testKey) {
		result = result.trim().replace(",", ".");
		String scale = scaleIndexMap.get(testKey);
		String[] results = scale.split(",");
		double d = Double.NaN;
		if (!result.equals("")) {
			int dem = Integer.parseInt(results[0]);

			// System.out.println(dem);

			try {
				d = Double.parseDouble(result) * Double.valueOf(dem);
			} catch (NumberFormatException nfe) {
				// no-op -- defaults to NAN
			}
			if (dem == 1)
				results[0] = result;
			else
				results[0] = String.valueOf(d);
			// System.out.println("result="+d);
		}

		else {
			results[0] = result;
		}

		return results;
	}

	private boolean manageColumnsIndex(List<String> lines) {
		if (getColumnsLine(lines) < 0)
			return false;
		// DELIMITER=((String)lines.get(getColumnsLine(lines))).substring(5, 6);
		String[] headers = lines.get(getColumnsLine(lines)).split(DELIMITER);

		for (Integer i = 0; i < headers.length; i++) {
			String header = headers[i].trim();
			if (testHeaderNameMap.containsKey(header)) {
				indexTestMap.put(i.toString(), header);
			} else if (header.contains("KX21-NERG")) {
				ORDER_NUMBER_INDEX = i;
			} else if (header.contains("DATE")) {
				ORDER_DAY_INDEX = i;
			} else if (header.contains("HEURE")) {
				ORDER_HOUR_INDEX = i;
			}
		}

		return ORDER_NUMBER_INDEX != -1 && ORDER_DAY_INDEX != -1 && ORDER_HOUR_INDEX != -1;
	}

	public int getColumnsLine(List<String> lines) {
		for (int k = 0; k < lines.size(); k++) {
			if (lines.get(k).contains("KX21") && lines.get(k).contains("LYM%") && lines.get(k).contains("GRAN%"))

				return k;

		}

		return -1;
	}

	private void createAnalyzerResultFromLine(String line, List<AnalyzerResults> resultList) {
		String[] fields = line.split(DELIMITER);

		for (Integer k = 0; k < fields.length; k++) {

			if (indexTestMap.containsKey(k.toString())) {
				String testKey = indexTestMap.get(k.toString());
				AnalyzerResults aResult = new AnalyzerResults();
				aResult.setTestId(testHeaderNameMap.get(testKey).getId());
				aResult.setTestName(testHeaderNameMap.get(testKey).getName());

				String[] result = getAppropriateResults(fields[k], testKey);
				aResult.setResult(result[0]);
				aResult.setUnits(result[1]);
				aResult.setAnalyzerId(ANALYZER_ID);
				aResult.setAccessionNumber(fields[ORDER_NUMBER_INDEX].trim());
				aResult.setResultType("N");

				String dateTime = fields[ORDER_DAY_INDEX].trim();
				dateTime = dateTime + " " + fields[ORDER_HOUR_INDEX].trim();
				aResult.setCompleteDate(getTimestampFromDate(dateTime));

				// System.out.println("***" + aResult.getAccessionNumber() + " " +
				// aResult.getCompleteDate() + " " + aResult.getResult());

				if (aResult.getAccessionNumber() != null) {
					aResult.setIsControl(aResult.getAccessionNumber().startsWith(CONTROL_ACCESSION_PREFIX));
				} else {
					aResult.setIsControl(false);
				}

				addValueToResults(resultList, aResult);
			}

		}
	}

	private void addValueToResults(List<AnalyzerResults> resultList, AnalyzerResults result) {

		if (result.getIsControl()) {
			resultList.add(result);
			return;
		}
		SampleService sampleServ = SpringContext.getBean(SampleService.class);
		if (!result.getAccessionNumber().startsWith(projectCode)
				|| sampleServ.getSampleByAccessionNumber(result.getAccessionNumber()) == null)
			return;

		List<Analysis> analyses = analysisDao.getAnalysisByAccessionAndTestId(result.getAccessionNumber(),
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

}
