/*******************************************************************************
 * Copyright (c) 2013, 2105 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.addons.d3chart;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.TestContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class BarChart_Test {

  private Display display;
  private Shell shell;
  private RemoteObject remoteObject;
  private Connection connection;
  private BarChart chart;

  @Rule
  public TestContext context = new TestContext();

  @Before
  public void setUp() {
    display = new Display();
    shell = new Shell( display );
    remoteObject = mock( RemoteObject.class );
    connection = fakeConnection( remoteObject );
    chart = new BarChart( shell, SWT.NONE );
  }

  @Test
  public void testCreate_createsRemoteObject() {
    verify( connection ).createRemoteObject( eq( "d3chart.Chart" ) );
  }

  @Test
  public void testCreate_setsRenderer() {
    verify( remoteObject ).set( "renderer", "basic-bar" );
  }

  @Test
  public void testGetBarWidth_hasDefault() {
    assertEquals( 25, chart.getBarWidth() );
  }

  @Test( expected = SWTException.class )
  public void testGetBarWidth_checksWidget() {
    chart.dispose();

    chart.getBarWidth();
  }

  @Test
  public void testSetBarWidth_changesValue() {
    chart.setBarWidth( 42 );

    assertEquals( 42, chart.getBarWidth() );
  }

  @Test( expected = SWTException.class )
  public void testSetBarWidth_checksWidget() {
    chart.dispose();

    chart.setBarWidth( 42 );
  }

  @Test
  public void testSetBarWidth_isRendered() {
    reset( remoteObject );

    chart.setBarWidth( 42 );

    verify( remoteObject ).call( "setOptions", new JsonObject().add( "barWidth", 42 ) );
  }

  @Test
  public void testSetBarWidth_notRenderedIfUnchanged() {
    chart.setBarWidth( chart.getBarWidth() );

    verify( remoteObject, times( 0 ) ).set( eq( "barWidth" ), anyInt() );
  }

  @Test
  public void testGetSpacing_hasDefault() {
    assertEquals( 2, chart.getSpacing() );
  }

  @Test( expected = SWTException.class )
  public void testGetSpacing_checksWidget() {
    chart.dispose();

    chart.getSpacing();
  }

  @Test
  public void testSetSpacing_changesValue() {
    chart.setSpacing( 23 );

    assertEquals( 23, chart.getSpacing() );
  }

  @Test( expected = SWTException.class )
  public void testSetSpacing_checksWidget() {
    chart.dispose();

    chart.setSpacing( 2 );
  }

  @Test
  public void testSetSpacing_isRendered() {
    reset( remoteObject );

    chart.setSpacing( 23 );

    verify( remoteObject ).call( "setOptions", new JsonObject().add( "spacing", 23 ) );
  }

  @Test
  public void testSetSpacing_notRenderedIfUnchanged() {
    chart.setSpacing( chart.getSpacing() );

    verify( remoteObject, times( 0 ) ).set( eq( "spacing" ), anyInt() );
  }

  private Connection fakeConnection( RemoteObject remoteObject ) {
    Connection connection = mock( Connection.class );
    when( connection.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    context.replaceConnection( connection );
    return connection;
  }

}
