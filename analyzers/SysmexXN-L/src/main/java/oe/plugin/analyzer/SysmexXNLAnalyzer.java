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

public class SysmexXNLAnalyzer implements AnalyzerImporterPlugin {

	@Override
	public boolean connect() {
		List<PluginAnalyzerService.TestMapping> nameMapping = new ArrayList<>();
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXNLAnalyzerAnalyzerImplementation.HBV, "HEPATITIS B VIRAL LOAD",
						SysmexXNLAnalyzerAnalyzerImplementation.HBV_LOINC));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXNLAnalyzerAnalyzerImplementation.HCV, "HEPATITIS C VIRAL LOAD",
						SysmexXNLAnalyzerAnalyzerImplementation.HCV_LOINC));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXNLAnalyzerAnalyzerImplementation.HIV_QUAL, "​Xpert HIV-1 Qual",
				SysmexXNLAnalyzerAnalyzerImplementation.HIV_QUAL_LOINC));
		nameMapping
				.add(new PluginAnalyzerService.TestMapping(SysmexXNLAnalyzerAnalyzerImplementation.HIV_VIRAL, "HIV VIRAL LOAD",
						SysmexXNLAnalyzerAnalyzerImplementation.HIV_VIRAL_LOINC));
		nameMapping.add(
				new PluginAnalyzerService.TestMapping(SysmexXNLAnalyzerAnalyzerImplementation.COV_2, "COVID-19 PCR",
						SysmexXNLAnalyzerAnalyzerImplementation.COV_2_LOINC));
		getInstance().addAnalyzerDatabaseParts("SysmexXNLAnalyzer", "SysmexXNLAnalyzer", nameMapping, true);
		getInstance().registerAnalyzer(this);
		return true;
	}

	@Override
	public boolean isTargetAnalyzer(List<String> lines) {
		for (String line : lines) {
			if (line.startsWith(SysmexXNLAnalyzerImplementation.HEADER_RECORD_IDENTIFIER)) {
				String[] headerRecord = line.split(SysmexXNLAnalyzerImplementation.DEFAULT_FIELD_DELIMITER);
				if (headerRecord.length < 5) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is not SysmexXN: header record not long enough")
					return false;
				}
				String[] senderName = headerRecord[4].split(SysmexXNLAnalyzerImplementation.DEFAULT_SUBFIELD_DELIMITER);
				if (senderName.length < 1) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is not SysmexXN: sender name field not long enough")
					return false;
				}
				LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message analyzer name is " + senderName[0]);
				if (senderName[0].equalsIgnoreCase("XN-550")) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is SysmexXN (XN-350)");
					return true;
				} else if (senderName[0].equalsIgnoreCase("XN-530")) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is SysmexXN (XN-350)");
					return true;
				} else if (senderName[0].equalsIgnoreCase("XN-450")) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is SysmexXN (XN-350)");
					return true;
				} else if (senderName[0].equalsIgnoreCase("XN-430")) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is SysmexXN (XN-350)");
					return true;
				} else if (senderName[0].equalsIgnoreCase("XN-350")) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is SysmexXN (XN-350)");
					return true;
				} else if (senderName[0].equalsIgnoreCase("XN-330")) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is SysmexXN (XN-350)");
					return true;
				} else if (senderName[0].equalsIgnoreCase("XN-150")) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is SysmexXN (XN-350)");
					return true;
				} else if (senderName[0].equalsIgnoreCase("XN-110")) {
					LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is SysmexXN (XN-350)");
					return true;
				} 
				LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is not SysmexXN: sender name doesn't match");
				return false;
			}
		}
		LogEvent.logTrace(this.getClass().getSimpleName(), "isTargetAnalyzer", "incoming message is not SysmexXN: no header line");
		return false;
	}

	@Override
	public AnalyzerLineInserter getAnalyzerLineInserter() {
		return new SysmexXNLAnalyzerImplementation();
	}

}
