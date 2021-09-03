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

public class GeneXpertAnalyzer implements AnalyzerImporterPlugin {

	@Override
	public boolean connect() {
		List<PluginAnalyzerService.TestMapping> nameMapping = new ArrayList<>();
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(GeneXpertAnalyzerImplementation.HBV, "29615-2", "29615-2"));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(GeneXpertAnalyzerImplementation.HBV, "11011-4", "11011-4"));
		nameMapping
				.add(new PluginAnalyzerService.TestMapping(GeneXpertAnalyzerImplementation.HIV_VIRAL, "10351-5",
						"10351-5"));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(GeneXpertAnalyzerImplementation.COV_2, "94500-6", "94500-6"));
		getInstance().addAnalyzerDatabaseParts("GeneXpert", "GeneXpert", nameMapping, true);
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
		return new GeneXpertAnalyzerImplementation();
	}

}
