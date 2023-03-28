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
import org.openelisglobal.menu.valueholder.Menu;
import org.openelisglobal.plugin.MenuPlugin;

public class Cobas4800Menu extends MenuPlugin
{
  protected void insertMenu()
  {
    PluginMenuService service = PluginMenuService.getInstance();
    
    Menu menu = new Menu();
    menu.setParent(PluginMenuService.getInstance().getKnownMenu(PluginMenuService.KnownMenu.ANALYZER, "menu_results"));
    menu.setPresentationOrder(132);
    menu.setElementId("taqman4800_vl_analyzer_plugin");
    menu.setActionURL("/AnalyzerResults?type=Cobas4800VLAnalyzer");
    menu.setDisplayKey("banner.menu.results.cobas4800vlanalyzer");
    menu.setOpenInNewWindow(false);
    
    service.addMenu(menu);
    
    menu = new Menu();
    menu.setParent(PluginMenuService.getInstance().getKnownMenu(PluginMenuService.KnownMenu.ANALYZER, "menu_results"));
    menu.setPresentationOrder(133);
    menu.setElementId("taqman4800_eid_analyzer_plugin");
    menu.setActionURL("/AnalyzerResults?type=Cobas4800EIDAnalyzer");
    menu.setDisplayKey("banner.menu.results.cobas4800eidanalyzer");
    menu.setOpenInNewWindow(false);
   
    service.addMenu(menu);

    service.insertLanguageKeyValue("banner.menu.results.cobas4800vlanalyzer", "Virology: Cobas4800: Viral Load", Locale.ENGLISH.toLanguageTag());
    service.insertLanguageKeyValue("banner.menu.results.cobas4800vlanalyzer", "Virologie: Cobas4800: Charge Virale", Locale.FRENCH.toLanguageTag());
  
    service.insertLanguageKeyValue("banner.menu.results.cobas4800eidanalyzer", "Virology: Cobas4800: EID", Locale.ENGLISH.toLanguageTag());
    service.insertLanguageKeyValue("banner.menu.results.cobas4800eidanalyzer", "Virologie: Cobas4800: EID", Locale.FRENCH.toLanguageTag());

  
  }
}