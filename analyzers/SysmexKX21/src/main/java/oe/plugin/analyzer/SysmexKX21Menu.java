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

import java.util.Locale;

import org.openelisglobal.common.services.PluginMenuService;
import org.openelisglobal.common.services.PluginMenuService.KnownMenu;
import org.openelisglobal.menu.valueholder.Menu;
import org.openelisglobal.plugin.MenuPlugin;

public class SysmexKX21Menu extends MenuPlugin {

	@Override
	protected void insertMenu() {
		PluginMenuService service = PluginMenuService.getInstance();
		Menu menu = new Menu();

		menu.setParent(PluginMenuService.getInstance().getKnownMenu(KnownMenu.ANALYZER, "menu_results"));
		// The order this analyzer will show on the menu relative to other analyzers
		menu.setPresentationOrder(50);
		// The id needs to be unique in the system
		menu.setElementId("sysmex_kx21_analyzer_plugin");
		// This will always be "/AnalyzerResults.do?type=<The name of the analyzer in
		// the database as specified in then Analyzer class call to
		// addAnalyzerDatabaseParts(....)
		menu.setActionURL("/AnalyzerResults?type=SysmexKX21Analyzer");
		// The key used for the name of the analyzer on the menu. Should not already
		// exist in MessageResource.properties.
		menu.setDisplayKey("banner.menu.results.sysmexkx21analyzer");
		menu.setOpenInNewWindow(false);

		service.addMenu(menu);
		// Analyzer name in English
		service.insertLanguageKeyValue("banner.menu.results.sysmexkx21analyzer",
				"Hematology: Sysmex KX21 analyzer importer", Locale.ENGLISH.toLanguageTag());
		// Analyzer name in French
		service.insertLanguageKeyValue("banner.menu.results.sysmexkx21analyzer", "Hematologie: Sysmex KX21",
				Locale.FRENCH.toLanguageTag());
	}

}
