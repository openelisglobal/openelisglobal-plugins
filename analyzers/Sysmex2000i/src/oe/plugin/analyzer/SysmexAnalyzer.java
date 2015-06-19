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

import us.mn.state.health.lims.analyzerimport.analyzerreaders.AnalyzerLineInserter;
import us.mn.state.health.lims.common.services.PluginAnalyzerService;
import us.mn.state.health.lims.plugin.AnalyzerImporterPlugin;

import java.util.ArrayList;
import java.util.List;

import static us.mn.state.health.lims.common.services.PluginAnalyzerService.getInstance;


public class SysmexAnalyzer implements AnalyzerImporterPlugin {


    public boolean connect(){
        List<PluginAnalyzerService.TestMapping> nameMappinng = new ArrayList<PluginAnalyzerService.TestMapping>();
        getInstance().addAnalyzerDatabaseParts("SysmexAnalyzer", "Plugin for Sysmex XT 2000i analyzer",nameMappinng);
        getInstance().registerAnalyzer(this);
        return true;
    }

    @Override
    public boolean isTargetAnalyzer(List<String> lines) {
    	
    	int idInstrumentIndex = -1;
    	
    	if (lines.size() > 1 ) { 
    		
    		//String[] headers = lines.get(1).split("ret,");
                    String[] headers = lines.get(0).split("ret,");
    		for (int i = 0; i < headers.length; i++) {
    			if (headers[i].contains("ID Instrument")) {
    				idInstrumentIndex = i;
    			}
    		}
    		
    		if (idInstrumentIndex == -1) {
    			return false;
    		}
    		
    		String[] data = lines.get(1).split(",");
    		if (data[idInstrumentIndex].contains("XT-2000i")) {
    			return true;
    		}

    	} 
    	return false;
    }

    @Override
    public AnalyzerLineInserter getAnalyzerLineInserter() {
        return new SysmexAnalyzerImplementation();
    }
}
