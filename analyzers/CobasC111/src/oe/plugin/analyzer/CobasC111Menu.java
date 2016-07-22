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

import us.mn.state.health.lims.common.services.PluginMenuService;
import us.mn.state.health.lims.common.services.PluginMenuService.KnownMenu;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.menu.util.MenuUtil;
import us.mn.state.health.lims.menu.valueholder.Menu;
import us.mn.state.health.lims.plugin.MenuPlugin;

public class CobasC111Menu extends MenuPlugin {

	@Override
	protected void insertMenu() {
        PluginMenuService service = PluginMenuService.getInstance();
        Menu menu = new Menu();
		
		menu.setParent(PluginMenuService.getInstance().getKnownMenu(KnownMenu.ANALYZER, "menu_results"));
		//The order this analyzer will show on the menu relative to other analyzers
		menu.setPresentationOrder(9);
		//The id needs to be unique in the system
		menu.setElementId("cobasc111_analyzer_plugin");
		//This will always be "/AnalyzerResults.do?type=<The name of the analyzer in the database as specified in then Analyzer class call to addAnalyzerDatabaseParts(....) 
		menu.setActionURL("/AnalyzerResults.do?type=CobasC111Analyzer");
		//The key used for the name of the analyzer on the menu.  Should not already exist in MessageResource.properties.
		menu.setDisplayKey("banner.menu.results.cobasc111analyzer");
		menu.setOpenInNewWindow(false);

        service.addMenu(menu);
		//Analyzer name in English
        service.insertLanguageKeyValue("banner.menu.results.cobasc111analyzer","Biochemistry: Cobas C111", ConfigurationProperties.LOCALE.ENGLISH.getRepresentation());
		//Analyzer name in French
        service.insertLanguageKeyValue("banner.menu.results.cobasc111analyzer","Biochimie: Cobas C111", ConfigurationProperties.LOCALE.FRENCH.getRepresentation());
	}
	
}
