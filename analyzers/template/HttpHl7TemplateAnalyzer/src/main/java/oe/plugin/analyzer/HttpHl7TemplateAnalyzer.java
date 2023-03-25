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

import static org.openelisglobal.common.services.PluginAnalyzerService.getInstance;

import java.util.ArrayList;
import java.util.List;

import org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerLineInserter;
import org.openelisglobal.common.services.PluginAnalyzerService;
import org.openelisglobal.plugin.AnalyzerImporterPlugin;

public class HttpHl7TemplateAnalyzer implements AnalyzerImporterPlugin {

	@Override
	public boolean connect() {
		List<PluginAnalyzerService.TestMapping> nameMapping = new ArrayList<>();
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(HttpHl7TemplateAnalyzerAnalyzerImplementation.HBV, "HEPATITIS B VIRAL LOAD",
						HttpHl7TemplateAnalyzerAnalyzerImplementation.HBV_LOINC));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(HttpHl7TemplateAnalyzerAnalyzerImplementation.HCV, "HEPATITIS C VIRAL LOAD",
						HttpHl7TemplateAnalyzerAnalyzerImplementation.HCV_LOINC));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(HttpHl7TemplateAnalyzerAnalyzerImplementation.HIV_QUAL, "​Xpert HIV-1 Qual",
				HttpHl7TemplateAnalyzerAnalyzerImplementation.HIV_QUAL_LOINC));
		nameMapping
				.add(new PluginAnalyzerService.TestMapping(HttpHl7TemplateAnalyzerAnalyzerImplementation.HIV_VIRAL, "HIV VIRAL LOAD",
						HttpHl7TemplateAnalyzerAnalyzerImplementation.HIV_VIRAL_LOINC));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(HttpHl7TemplateAnalyzerAnalyzerImplementation.COV_2, "COVID-19 PCR",
						HttpHl7TemplateAnalyzerAnalyzerImplementation.COV_2_LOINC));
		getInstance().addAnalyzerDatabaseParts("HttpHl7TemplateAnalyzer", "HttpHl7TemplateAnalyzer", nameMapping, true);
		getInstance().registerAnalyzer(this);
		return true;
	}

	@Override
	// this plugin does not work for flat files, so we disable it in that workflow
	public boolean isTargetAnalyzer(List<String> lines) {
		return false;
	}

	@Override
	public AnalyzerLineInserter getAnalyzerLineInserter() {
		return new HttpHl7TemplateAnalyzerImplementation();
	}

}
