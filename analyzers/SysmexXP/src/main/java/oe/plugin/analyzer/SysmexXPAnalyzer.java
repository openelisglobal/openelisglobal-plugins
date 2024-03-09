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

public class SysmexXPAnalyzer implements AnalyzerImporterPlugin {

	@Override
	public boolean connect() {
		List<PluginAnalyzerService.TestMapping> nameMapping = new ArrayList<>();
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_WBC, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_WBC));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_RBC, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_RBC));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_HGB, "​",
				SysmexXPAnalyzerAnalyzerImplementation.LOINC_HGB));
		nameMapping
				.add(new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_PLCR, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_PLCR));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_HCT, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_HCT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_MCV, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_MCV));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_MCH, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_MCH));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_MCHC, "​",
				SysmexXPAnalyzerAnalyzerImplementation.LOINC_MCHC));
		nameMapping
				.add(new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_PLT, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_PLT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_WSCR, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_WSCR));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_WMCR, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_WMCR));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_WLCR, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_WLCR));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_WSCC, "​",
				SysmexXPAnalyzerAnalyzerImplementation.LOINC_WSCC));
		nameMapping.add(
			new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_WMCC, "",
								SysmexXPAnalyzerAnalyzerImplementation.LOINC_WMCC));
		nameMapping.add(
					new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_WLCC, "",
								SysmexXPAnalyzerAnalyzerImplementation.LOINC_WLCC));
		nameMapping
				.add(new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_RDWSD, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_RDWSD));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_RDWCV, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_RDWCV));
		nameMapping
				.add(new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_PDW, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_PDW));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_MPV, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_MPV));
		nameMapping
				.add(new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_PCT, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_PCT));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_WSMV, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_WSMV));
		nameMapping
				.add(new PluginAnalyzerService.TestMapping(SysmexXPAnalyzerAnalyzerImplementation.ANALYZER_TEST_WLMV, "",
						SysmexXPAnalyzerAnalyzerImplementation.LOINC_WLMV));
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
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is not SysmexXP: header record not long enough")
					return false;
				}
				String[] senderName = headerRecord[4].split(SysmexXPAnalyzerImplementation.DEFAULT_SUBFIELD_DELIMITER);
				if (senderName.length < 1) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is not SysmexXP: sender name field not long enough")
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
