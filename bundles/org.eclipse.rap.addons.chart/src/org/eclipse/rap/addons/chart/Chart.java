/*******************************************************************************
 * Copyright (c) 2013, 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.addons.chart;

import static org.eclipse.rap.json.JsonValue.valueOf;
import static org.eclipse.rap.rwt.RWT.getClient;
import static org.eclipse.rap.rwt.SingletonUtil.getUniqueInstance;
import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.JavaScriptLoader;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.service.ResourceLoader;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public abstract class Chart extends Canvas {

  private static final String REMOTE_TYPE = "rwt.chart.Chart";
  private static final String D3_JS_URL = "https://d3js.org/d3.v3.min.js";

  private Resources resources;
  private CssLoader cssLoader;

  protected final RemoteObject remoteObject;

  public Chart( Composite parent, int style, String renderer ) {
    super( parent, style );
    remoteObject = RWT.getUISession().getConnection().createRemoteObject( REMOTE_TYPE );
    remoteObject.set( "parent", getId( this ) );
    remoteObject.set( "renderer", renderer );
    remoteObject.setHandler( new AbstractOperationHandler() {
      @Override
      public void handleNotify( String eventName, JsonObject properties ) {
        if( "Selection".equals( eventName ) ) {
          Event event = new Event();
          event.index = properties.get( "index" ).asInt();
          JsonValue detail = properties.get( "detail" );
          if( detail != null ) {
            event.detail = detail.asInt();
          }
          notifyListeners( SWT.Selection, event );
        }
      }
    } );
    resources = getUniqueInstance( Resources.class, RWT.getApplicationContext() );
    cssLoader = getUniqueInstance( CssLoader.class, RWT.getUISession() );
    RWT.getClient().getService( JavaScriptLoader.class ).require( D3_JS_URL );
    requireJs( registerResource( "chart/chart.js" ) );
  }

  public void setChartData( JsonArray data ) {
    checkWidget();
    remoteObject.set( "items", data );
  }

  protected void setOption( String name, int value ) {
    setOption( name, valueOf( value ) );
  }

  protected void setOption( String name, double value ) {
    setOption( name, valueOf( value ) );
  }

  protected void setOption( String name, boolean value ) {
    setOption( name, valueOf( value ) );
  }

  protected void setOption( String name, String value ) {
    setOption( name, valueOf( value ) );
  }

  protected void setOption( String name, JsonValue value ) {
    remoteObject.call( "setOptions", new JsonObject().add( name, value ) );
  }

  protected void requireJs( String url ) {
    getClient().getService( JavaScriptLoader.class ).require( url );
  }

  protected void requireCss( String url ) {
    cssLoader.requireCss( url );
  }

  protected String registerResource( String resourceName ) {
    return resources.register( resourceName, resourceName, getResourceLoader() );
  }

  private ResourceLoader getResourceLoader() {
    final ClassLoader classLoader = getClass().getClassLoader();
    return new ResourceLoader() {
      @Override
      public InputStream getResourceAsStream( String resourceName ) throws IOException {
        return classLoader.getResourceAsStream( resourceName );
      }
    };
  }

  @Override
  public void dispose() {
    super.dispose();
    remoteObject.destroy();
  }

  @Override
  public void addListener( int eventType, Listener listener ) {
    boolean wasListening = isListening( SWT.Selection );
    super.addListener( eventType, listener );
    if( eventType == SWT.Selection && !wasListening ) {
      remoteObject.listen( "Selection", true );
    }
  }

  @Override
  public void removeListener( int eventType, Listener listener ) {
    boolean wasListening = isListening( SWT.Selection );
    super.removeListener( eventType, listener );
    if( eventType == SWT.Selection && wasListening && !isListening( SWT.Selection ) ) {
      remoteObject.listen( "Selection", false );
    }
  }

}
