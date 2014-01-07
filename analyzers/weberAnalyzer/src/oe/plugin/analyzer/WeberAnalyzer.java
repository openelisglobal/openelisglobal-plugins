package oe.plugin.analyzer;

import us.mn.state.health.lims.analyzerimport.analyzerreaders.AnalyzerLineInserter;
import us.mn.state.health.lims.common.services.PluginAnalyzerService;
import us.mn.state.health.lims.plugin.AnalyzerImporterPlugin;

import java.util.ArrayList;
import java.util.List;

import static us.mn.state.health.lims.common.services.PluginAnalyzerService.getInstance;


public class WeberAnalyzer implements AnalyzerImporterPlugin {


    public boolean connect(){
        List<PluginAnalyzerService.TestMapping> nameMappinng = new ArrayList<PluginAnalyzerService.TestMapping>();
        nameMappinng.add(new PluginAnalyzerService.TestMapping("CD4_PER", "CD4 Compte en %"));
        nameMappinng.add(new PluginAnalyzerService.TestMapping("CD3_PER", "CD4 Compte Absolu"));
        getInstance().addAnalyzerDatabaseParts("WeberAnalyzer", "Plugin for weber analyzer",nameMappinng);
        getInstance().registerAnalyzer(this);
        return true;
    }

    @Override
    public boolean isTargetAnalyzer(List<String> lines) {
        return lines.get(1) != null && lines.get(1).contains("MugelSET");
    }

    @Override
    public AnalyzerLineInserter getAnalyzerLineInserter() {
        return new WeberAnalyzerImplementation();
    }
}
