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

import static org.eclipse.rap.rwt.widgets.WidgetUtil.getId;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.remote.AbstractOperationHandler;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public abstract class Chart extends Canvas {

  private static final String REMOTE_TYPE = "d3chart.Chart";

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
          notifyListeners( SWT.Selection, event );
        }
      }
    } );
    ChartResources.ensureJavaScriptResources();
  }

  public void setChartData( JsonArray data ) {
    checkWidget();
    remoteObject.set( "items", data );
  }

  protected void setConfig( JsonObject config ) {
    checkWidget();
    remoteObject.set( "config", config );
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

  String getRemoteId() {
    return remoteObject.getId();
  }

}
