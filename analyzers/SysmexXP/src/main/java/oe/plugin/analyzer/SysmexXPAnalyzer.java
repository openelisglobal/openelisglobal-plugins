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
import org.openelisglobal.common.log.LogEvent;

public class SysmexXPAnalyzer implements AnalyzerImporterPlugin {

	@Override
	public boolean connect() {
		List<PluginAnalyzerService.TestMapping> nameMapping = new ArrayList<>();
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_WBC, "White Blood Cells Count",
						SysmexXPAnalyzerImplementation.LOINC_WBC));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_RBC, "Red Blood Cells Count",
						SysmexXPAnalyzerImplementation.LOINC_RBC));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_HGB, "​Hemoglobin",
				SysmexXPAnalyzerImplementation.LOINC_HGB));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_HCT, "Hematocrit",
						SysmexXPAnalyzerImplementation.LOINC_HCT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_MCV, "Medium corpuscular volum",
						SysmexXPAnalyzerImplementation.LOINC_MCV));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_MCH, "",
						SysmexXPAnalyzerImplementation.LOINC_MCH));
		nameMapping
				.add(new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_MCHC, "",
						SysmexXPAnalyzerImplementation.LOINC_MCHC));
		nameMapping
				.add(new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_RDWSD, "",
						SysmexXPAnalyzerImplementation.LOINC_RDWSD));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_RDWCV, "",
						SysmexXPAnalyzerImplementation.LOINC_RDWCV));
		nameMapping
				.add(new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_PLT, "Platelets",
						SysmexXPAnalyzerImplementation.LOINC_PLT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_MPV, "",
						SysmexXPAnalyzerImplementation.LOINC_MPV));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_NEUT_COUNT, "Neutrophiles​",
				SysmexXPAnalyzerImplementation.LOINC_NEUT_COUNT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_NEUT_PERCENT, "Neutrophiles (%)",
						SysmexXPAnalyzerImplementation.LOINC_NEUT_PERCENT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_LYMPH_COUNT, "Lymphocytes (Abs)",
						SysmexXPAnalyzerImplementation.LOINC_LYMPH_COUNT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_LYMPH_PERCENT, "Lymphocytes (%)",
						SysmexXPAnalyzerImplementation.LOINC_LYMPH_PERCENT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_MONO_COUNT, "Monocytes (Abs)",
						SysmexXPAnalyzerImplementation.LOINC_MONO_COUNT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_MONO_PERCENT, "Monocytes (%)",
						SysmexXPAnalyzerImplementation.LOINC_MONO_PERCENT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_EO_COUNT, "Eosinophiles",
						SysmexXPAnalyzerImplementation.LOINC_EO_COUNT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_EO_PERCENT, "Eosinophiles (%)",
						SysmexXPAnalyzerImplementation.LOINC_EO_PERCENT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_BASO_COUNT, "Basophiles",
						SysmexXPAnalyzerImplementation.LOINC_BASO_COUNT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_BASO_PERCENT, "Basophiles (%)",
						SysmexXPAnalyzerImplementation.LOINC_BASO_PERCENT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_IG_COUNT, "",
						SysmexXPAnalyzerImplementation.LOINC_IG_COUNT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_IG_PERCENT, "",
						SysmexXPAnalyzerImplementation.LOINC_IG_PERCENT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_MXD_COUNT, "",
						SysmexXPAnalyzerImplementation.LOINC_MXD_COUNT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerImplementation.ANALYZER_TEST_MXD_PERCENT, "",
						SysmexXPAnalyzerImplementation.LOINC_MXD_PERCENT));
		getInstance().addAnalyzerDatabaseParts("SysmexXPAnalyzer", "SysmexXPAnalyzer", nameMapping, true);
		getInstance().registerAnalyzer(this);
		return true;
	}

	@Override
	public boolean isTargetAnalyzer(List<String> lines) {
		for (String line : lines) {
			if (line.startsWith(SysmexXPAnalyzerImplementation.HEADER_RECORD_IDENTIFIER)) {
				String[] headerRecord = line.split(SysmexXPAnalyzerImplementation.DEFAULT_FIELD_DELIMITER);
				if (headerRecord.length < 5) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is not SysmexXP: header record not long enough");
					return false;
				}
				String[] senderName = headerRecord[4].split(SysmexXPAnalyzerImplementation.DEFAULT_SUBFIELD_DELIMITER);
				if (senderName.length < 1) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is not SysmexXP: sender name field not long enough");
					return false;
				}
				LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message analyzer name is " + senderName[0]);
				if (senderName[0].equalsIgnoreCase("XP-100")) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is SysmexXP (XP-100)");
					return true;
				} else if (senderName[0].equalsIgnoreCase("XP-300")) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is SysmexXP (XP-300)");
					return true;
				}
				LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is not SysmexXP: sender name doesn't match");
				return false;
			}
		}
		LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is not SysmexXP: no header line");
		return false;
	}

	@Override
	public AnalyzerLineInserter getAnalyzerLineInserter() {
		return new SysmexXPAnalyzerImplementation();
	}

}
