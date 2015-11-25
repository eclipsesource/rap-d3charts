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
package org.eclipse.rap.addons.d3chart.demo;

import org.eclipse.rap.addons.d3chart.NvLineChart;
import org.eclipse.rap.addons.d3chart.NvPieChart;
import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class NvChartSnippet extends AbstractEntryPoint {

  @Override
  protected void createContents( Composite parent ) {
    parent.setLayout( new GridLayout() );
    final NvPieChart pieChart = new NvPieChart( parent, SWT.NONE );
    pieChart.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    pieChart.setChartData( createPieData() );
    final NvLineChart lineChart = new NvLineChart( parent, SWT.NONE );
    lineChart.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    lineChart.setChartData( createLineData() );
    Button button = new Button( parent, SWT.PUSH );
    button.setText( "Change data" );
    button.addListener( SWT.Selection, new Listener() {
      @Override
      public void handleEvent( Event event ) {
        pieChart.setChartData( createPieData() );
        lineChart.setChartData( createLineData() );
      }
    } );
  }

  private static JsonArray createPieData() {
    return new JsonArray()
      .add( createPieItem( "Item 1", "#ff0000", Math.random() * 100 ) )
      .add( createPieItem( "Item 2", "#00ff00", Math.random() * 100 ) )
      .add( createPieItem( "Item 3", "#0000ff", Math.random() * 100 ) );
  }

  private static JsonObject createPieItem( String text, String color, double value ) {
    return new JsonObject()
      .add( "label", text )
      .add( "color", color )
      .add( "value", value );
  }

  private static JsonArray createLineData() {
    return new JsonArray()
      .add( createLineItem( "Series 1", "#ff0000", createRandomSeries() ) )
      .add( createLineItem( "Series 2", "#0000ff", createRandomSeries() ) );
  }

  private static JsonObject createLineItem( String text, String color, JsonArray values ) {
    return new JsonObject()
      .add( "key", text )
      .add( "color", color )
      .add( "values", values );
  }

  private static JsonArray createRandomSeries() {
    JsonArray data = new JsonArray();
    for( int i = 0; i < 100; i++ ) {
      data.add( new JsonObject().add( "x", i ).add( "y", Math.random() * 100 ) );
    }
    return data;
  }

}
