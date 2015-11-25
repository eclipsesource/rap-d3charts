/*******************************************************************************
 * Copyright (c) 2013, 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.addons.d3chart;

import static org.eclipse.rap.rwt.RWT.getClient;
import static org.eclipse.rap.rwt.RWT.getResourceManager;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.client.service.JavaScriptExecutor;
import org.eclipse.rap.rwt.client.service.JavaScriptLoader;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.eclipse.rap.rwt.service.ResourceManager;


public class ChartResources {

  private Map<String, String> locations = new HashMap<>();

  void requireJs( String registerPath, String resourceName, ResourceLoader resourceLoader ) {
    String location = register( registerPath, resourceName, resourceLoader );
    JavaScriptLoader loader = getClient().getService( JavaScriptLoader.class );
    loader.require( location );
  }

  void requireCss( String registerPath, String resourceName, ResourceLoader resourceLoader ) {
    String location = register( registerPath, resourceName, resourceLoader );
    JavaScriptExecutor executor = getClient().getService( JavaScriptExecutor.class );
    executor.execute( "d3chart.loadCss('" + location + "');" );
  }

  private String register( String registerPath, String resourceName, ResourceLoader resourceLoader )
  {
    ResourceManager resourceManager = getResourceManager();
    String location = locations.get( registerPath );
    if( location == null ) {
      try( InputStream inputStream = resourceLoader.getResourceAsStream( resourceName ) ) {
        resourceManager.register( registerPath, inputStream );
      } catch( Exception exception ) {
        throw new RuntimeException( "Failed to register resource " + registerPath, exception );
      }
    }
    location = resourceManager.getLocation( registerPath );
    locations.put( registerPath, location );
    return location;
  }

}
