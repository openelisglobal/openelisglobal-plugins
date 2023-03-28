// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SysmexXN1000Analyzer.java

package oe.plugin.analyzer;

import java.util.ArrayList;
import java.util.List;
import org.openelisglobal.analyzerimport.analyzerreaders.AnalyzerLineInserter;
import org.openelisglobal.common.services.PluginAnalyzerService;
import org.openelisglobal.plugin.AnalyzerImporterPlugin;

// Referenced classes of package oe.plugin.analyzer:
//            SysmexXN1000AnalyzerImplementation

public class SysmexXN1000Analyzer implements AnalyzerImporterPlugin {

	public SysmexXN1000Analyzer() {
		InstrumentIndex = -1;
	}

	public boolean connect() {
		List nameMappinng = new ArrayList();
		nameMappinng
				.add(new org.openelisglobal.common.services.PluginAnalyzerService.TestMapping("WBC(10^3/uL)", "GB"));
		nameMappinng
				.add(new org.openelisglobal.common.services.PluginAnalyzerService.TestMapping("RBC(10^6/uL)", "GR"));
		nameMappinng.add(new org.openelisglobal.common.services.PluginAnalyzerService.TestMapping("HGB(g/dL)", "Hb"));
		nameMappinng.add(new org.openelisglobal.common.services.PluginAnalyzerService.TestMapping("HCT(%)", "HCT"));
		nameMappinng.add(new org.openelisglobal.common.services.PluginAnalyzerService.TestMapping("MCV(fL)", "VGM"));
		nameMappinng.add(new org.openelisglobal.common.services.PluginAnalyzerService.TestMapping("MCH(pg)", "TCMH"));
		nameMappinng
				.add(new org.openelisglobal.common.services.PluginAnalyzerService.TestMapping("MCHC(g/dL)", "CCMH"));
		nameMappinng
				.add(new org.openelisglobal.common.services.PluginAnalyzerService.TestMapping("PLT(10^3/uL)", "PLQ"));
		nameMappinng
				.add(new org.openelisglobal.common.services.PluginAnalyzerService.TestMapping("NEUT%(%)", "Neut %"));
		nameMappinng
				.add(new org.openelisglobal.common.services.PluginAnalyzerService.TestMapping("LYMPH%(%)", "Lymph %"));
		nameMappinng
				.add(new org.openelisglobal.common.services.PluginAnalyzerService.TestMapping("MONO%(%)", "Mono %"));
		nameMappinng.add(new org.openelisglobal.common.services.PluginAnalyzerService.TestMapping("EO%(%)", "Eo %"));
		nameMappinng
				.add(new org.openelisglobal.common.services.PluginAnalyzerService.TestMapping("BASO%(%)", "Baso %"));
		PluginAnalyzerService.getInstance().addAnalyzerDatabaseParts("SysmexXN1000Analyzer",
				"Plugin for Sysmex XN1000 analyzer", nameMappinng);
		PluginAnalyzerService.getInstance().registerAnalyzer(this);
		return true;
	}

	public boolean isTargetAnalyzer(List lines) {
		for (int j = 0; j < lines.size(); j++)
			if (((String) lines.get(j)).contains(SYSMEX_XN1000_INDICATOR))
				return true;

		return false;
	}

	public AnalyzerLineInserter getAnalyzerLineInserter() {
		return new SysmexXN1000AnalyzerImplementation();
	}

	public int getColumnsLine(List lines) {
		for (int k = 0; k < lines.size(); k++)
			if (((String) lines.get(k)).contains("ID Instrument") && ((String) lines.get(k)).contains("N' Echantillon")
					&& ((String) lines.get(k)).contains("Ana. Jour") && ((String) lines.get(k)).contains("Ana. Heure")
					&& ((String) lines.get(k)).contains("N' Rack") && ((String) lines.get(k)).contains("Pos. Tube"))
				return k;

		return -1;
	}

	private static final String DELIMITER = ",";
	private static final CharSequence SYSMEX_XN1000_INDICATOR = "XN-10^23865";
	int InstrumentIndex;

}
