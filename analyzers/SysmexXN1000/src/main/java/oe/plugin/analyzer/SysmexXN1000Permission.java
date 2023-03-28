// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SysmexXN1000Permission.java

package oe.plugin.analyzer;

import org.openelisglobal.common.services.PluginPermissionService;
import org.openelisglobal.plugin.PermissionPlugin;
import org.openelisglobal.role.valueholder.Role;
import org.openelisglobal.systemmodule.valueholder.SystemModuleUrl;

public class SysmexXN1000Permission extends PermissionPlugin {

	public SysmexXN1000Permission() {
	}

	protected boolean insertPermission() {
		PluginPermissionService service = new PluginPermissionService();
		org.openelisglobal.systemmodule.valueholder.SystemModule module = service.getOrCreateSystemModule(
				"AnalyzerResults", "SysmexXN1000Analyzer", "Results->Analyzer->SysmexXN1000Analyzer");
		SystemModuleUrl moduleUrl = service.getOrCreateSystemModuleUrl(module, "/AnalyzerResults");
		Role role = service.getSystemRole("Results");
		return service.bindRoleToModule(role, module, moduleUrl);
	}
}
