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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.TestContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class PieChart_Test {

  private Display display;
  private Shell shell;
  private RemoteObject remoteObject;
  private Connection connection;
  private PieChart chart;

  @Rule
  public TestContext context = new TestContext();

  @Before
  public void setUp() {
    display = new Display();
    shell = new Shell( display );
    remoteObject = mock( RemoteObject.class );
    connection = fakeConnection( remoteObject );
    chart = new PieChart( shell, SWT.NONE );
  }

  @Test
  public void testCreate_createsRemoteObject() {
    verify( connection ).createRemoteObject( eq( "d3chart.Chart" ) );
  }

  @Test
  public void testCreate_setsRenderer() {
    verify( remoteObject ).set( "renderer", "basic-pie" );
  }

  @Test
  public void testInnerRadius_defaultValue() {
    assertEquals( 0, chart.getInnerRadius(), 0 );
  }

  @Test
  public void testInnerRadius_changeValue() {
    chart.setInnerRadius( 0.5f );

    assertEquals( 0.5, chart.getInnerRadius(), 0 );
  }

  @Test
  public void testInnerRadius_isRendered() {
    reset( remoteObject );

    chart.setInnerRadius( 0.5f );

    verify( remoteObject ).call( "setOptions", new JsonObject().add( "innerRadius", 0.5 ) );
  }

  @Test
  public void testStartAngle_defaultValue() {
    assertEquals( 0, chart.getStartAngle(), 0 );
  }

  @Test
  public void testStartAngle_changeValue() {
    chart.setStartAngle( 0.5f );

    assertEquals( 0.5, chart.getStartAngle(), 0 );
  }

  @Test
  public void testStartAngle_isRendered() {
    reset( remoteObject );

    chart.setStartAngle( -0.5f );

    verify( remoteObject ).call( "setOptions", new JsonObject().add( "startAngle", -Math.PI ) );
  }

  @Test
  public void testEndAngle_defaultValue() {
    assertEquals( 1, chart.getEndAngle(), 0 );
  }

  @Test
  public void testEndAngle_changeValue() {
    chart.setEndAngle( 0.5f );

    assertEquals( 0.5, chart.getEndAngle(), 0 );
  }

  @Test
  public void testEndAngle_isRendered() {
    reset( remoteObject );

    chart.setEndAngle( 0.5f );

    verify( remoteObject ).call( "setOptions", new JsonObject().add( "endAngle", Math.PI ) );
  }

  private Connection fakeConnection( RemoteObject remoteObject ) {
    Connection connection = mock( Connection.class );
    when( connection.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    context.replaceConnection( connection );
    return connection;
  }

}
