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

import us.mn.state.health.lims.common.services.PluginPermissionService;
import us.mn.state.health.lims.plugin.PermissionPlugin;
import us.mn.state.health.lims.role.valueholder.Role;
import us.mn.state.health.lims.systemmodule.valueholder.SystemModule;

/**
 */
public class FullyPermission extends PermissionPlugin{
    @Override
    protected boolean insertPermission(){
        PluginPermissionService service = new PluginPermissionService();
        SystemModule module = service.getOrCreateSystemModule( "AnalyzerResults", "FullyAnalyzer", "Results->Analyzer->FullyAnalyzer" );
        Role role = service.getSystemRole( "Results entry" );
        return service.bindRoleToModule( role, module );
    }
}
