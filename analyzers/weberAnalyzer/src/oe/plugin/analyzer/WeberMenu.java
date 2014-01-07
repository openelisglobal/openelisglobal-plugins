package oe.plugin.analyzer;

import us.mn.state.health.lims.common.services.PluginMenuService;
import us.mn.state.health.lims.common.services.PluginMenuService.KnownMenu;
import us.mn.state.health.lims.common.util.ConfigurationProperties;
import us.mn.state.health.lims.menu.util.MenuUtil;
import us.mn.state.health.lims.menu.valueholder.Menu;
import us.mn.state.health.lims.plugin.MenuPlugin;

public class WeberMenu extends MenuPlugin {

	@Override
	protected void insertMenu() {
        PluginMenuService service = PluginMenuService.getInstance();
        Menu menu = new Menu();
		
		menu.setParent(PluginMenuService.getInstance().getKnownMenu(KnownMenu.ANALYZER, "menu_results"));
		menu.setPresentationOrder(5);
		menu.setElementId("weber_analyzer_plugin");
		menu.setActionURL("/AnalyzerResults.do?type=WeberAnalyzer");
		menu.setDisplayKey("banner.menu.results.weber");
		menu.setOpenInNewWindow(false);

        service.addMenu(menu);
        service.insertLanguageKeyValue("banner.menu.results.weber","Weber 4000", ConfigurationProperties.LOCALE.ENGLISH.getRepresentation());
        service.insertLanguageKeyValue("banner.menu.results.weber","La Weber 4000", ConfigurationProperties.LOCALE.FRENCH.getRepresentation());
	}
	
}
