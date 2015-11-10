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
package org.eclipse.rap.addons.d3chart.demo.internal;

import static org.eclipse.rap.addons.d3chart.demo.internal.data.Colors.toCss;

import java.text.DecimalFormat;

import org.eclipse.rap.addons.d3chart.BarChart;
import org.eclipse.rap.addons.d3chart.PieChart;
import org.eclipse.rap.addons.d3chart.demo.internal.data.Colors;
import org.eclipse.rap.examples.ExampleUtil;
import org.eclipse.rap.examples.IExamplePage;
import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class BarChartExample implements IExamplePage {

  private Colors colors;
  private BarChart barChart;
  private PieChart pieChart;
  private JsonArray items = new JsonArray();

  @Override
  public void createControl( Composite parent ) {
    colors = Colors.cat10Colors();
    parent.setLayout( ExampleUtil.createMainLayout( 2 ) );
    createChartPart( parent );
    createControlPart( parent );
  }

  private void createChartPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayoutData( ExampleUtil.createFillData() );
    composite.setLayout( ExampleUtil.createGridLayout( 1, false, true, true ) );
    pieChart = new PieChart( composite, SWT.BORDER );
    pieChart.setLayoutData( new GridData( 300, 200 ) );
    barChart = new BarChart( composite, SWT.BORDER );
    barChart.setLayoutData( new GridData( 300, 300 ) );
  }

  private void createControlPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayout( ExampleUtil.createGridLayout( 2, true, true, false ) );
    composite.setLayoutData( ExampleUtil.createFillData() );
    createButton( composite, "Add item", new Listener() {
      @Override
      public void handleEvent( Event event ) {
        float value = ( float )( Math.random() * 0.8 );
        addItem( value, toCss( colors.next() ) );
        pieChart.setChartData( items );
        barChart.setChartData( items );
      }
    } ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    createButton( composite, "Remove item", new Listener() {
      @Override
      public void handleEvent( Event event ) {
        removeItem();
        pieChart.setChartData( items );
        barChart.setChartData( items );
      }
    } ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    createButton( composite, "small bars", new Listener() {
      @Override
      public void handleEvent( Event event ) {
        barChart.setBarWidth( 20 );
      }
    } ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    createButton( composite, "large bars", new Listener() {
      @Override
      public void handleEvent( Event event ) {
        barChart.setBarWidth( 40 );
      }
    } ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    createButton( composite, "spacing", new Listener() {
      @Override
      public void handleEvent( Event event ) {
        barChart.setSpacing( 2 );
      }
    } ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    createButton( composite, "no spacing", new Listener() {
      @Override
      public void handleEvent( Event event ) {
        barChart.setSpacing( 0 );
      }
    } ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    createButton( composite, "Resize charts", new Listener() {
      @Override
      public void handleEvent( Event event ) {
        if( pieChart.getSize().x > 350 ) {
          pieChart.setLayoutData( new GridData( 300, 200 ) );
          barChart.setLayoutData( new GridData( 300, 300 ) );
        } else {
          pieChart.setLayoutData( new GridData( 400, 300 ) );
          barChart.setLayoutData( new GridData( 400, 400 ) );
        }
        barChart.getParent().layout();
      }
    } );
  }

  private static Button createButton( Composite parent, String text, Listener listener ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( text );
    button.addListener( SWT.Selection, listener );
    return button;
  }

  private void addItem( float value, String color ) {
    DecimalFormat format = new DecimalFormat( "#.#" );
    items.add( new JsonObject()
      .add( "value", value )
      .add( "text", format.format( value * 100 ) + "%" )
      .add( "color", color ) );
  }

  private void removeItem() {
    // TODO Use JsonArray.remove() when JSON API is updated in RAP
    JsonArray newItems = new JsonArray();
    for( int i = 0; i < items.size(); i++ ) {
      if (i != 0) {
        newItems.add( items.get( i ) );
      }
    }
    items = newItems;
  }

}
