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
package org.eclipse.rap.addons.chart.demo.internal;

import static org.eclipse.rap.addons.chart.demo.internal.data.Colors.toCss;

import java.text.DecimalFormat;
import java.util.List;

import org.eclipse.rap.addons.chart.basic.PieChart;
import org.eclipse.rap.addons.chart.demo.internal.data.Colors;
import org.eclipse.rap.addons.chart.demo.internal.data.DataSet;
import org.eclipse.rap.addons.chart.demo.internal.data.ExampleData;
import org.eclipse.rap.addons.chart.demo.internal.data.DataSet.DataItem;
import org.eclipse.rap.examples.ExampleUtil;
import org.eclipse.rap.examples.IExamplePage;
import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;


public class PieChartExample implements IExamplePage {

  private final Colors colors = Colors.cat10Colors();
  private final DataSet dataSet = ExampleData.BROWSER_YEARLY;
  private final JsonArray items = createItems();
  private int cursor;
  private PieChart pieChart;
  private Table table;
  private Label yearLabel;

  @Override
  public void createControl( Composite parent ) {
    parent.setLayout( ExampleUtil.createMainLayout( 2 ) );
    createChartPart( parent );
    createControlPart( parent );
    updateItems();
  }

  private JsonArray createItems() {
    JsonArray items = new JsonArray();
    int count = dataSet.getColumnCount();
    for( int index = 0; index < count; index++ ) {
      items.add( new JsonObject()
        .add( "value", 0 )
        .add( "color", toCss( colors.get( index ) ) ) );
    }
    return items;
  }

  private void createChartPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayoutData( ExampleUtil.createFillData() );
    composite.setLayout( ExampleUtil.createGridLayout( 2, true, true, true ) );
    pieChart = new PieChart( composite, SWT.NONE );
    pieChart.setInnerRadius( 0.6f );
    GridData layoutData = new GridData( 400, 350 );
    layoutData.horizontalAlignment = SWT.FILL;
    layoutData.horizontalSpan = 2;
    pieChart.setLayoutData( layoutData );
    pieChart.addListener( SWT.Selection, new Listener() {
      @Override
      public void handleEvent( Event event ) {
        table.select( event.index );
        table.showSelection();
      }
    } );
    createButton( composite, "180°", new Listener() {
      @Override
      public void handleEvent( Event event ) {
        pieChart.setStartAngle( -0.25f );
        pieChart.setEndAngle( 0.25f );
      }
    } ).setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, true, false ) );
    createButton( composite, "360°", new Listener() {
      @Override
      public void handleEvent( Event event ) {
        pieChart.setStartAngle( 0f );
        pieChart.setEndAngle( 1f );
      }
    } ).setLayoutData( new GridData( SWT.RIGHT, SWT.CENTER, true, false ) );
    createButton( composite, "donut", new Listener() {
      @Override
      public void handleEvent( Event event ) {
        pieChart.setInnerRadius( 0.6f );
      }
    } ).setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, true, false ) );
    createButton( composite, "pie", new Listener() {
      @Override
      public void handleEvent( Event event ) {
        pieChart.setInnerRadius( 0f );
      }
    } ).setLayoutData( new GridData( SWT.RIGHT, SWT.CENTER, true, false ) );
  }

  private void createControlPart( Composite parent ) {
    Composite composite = new Composite( parent, SWT.NONE );
    composite.setLayout( ExampleUtil.createGridLayout( 3, true, true, false ) );
    composite.setLayoutData( ExampleUtil.createFillData() );
    createButton( composite, "Prev", new Listener() {
      @Override
      public void handleEvent( Event event ) {
        showPrevious();
      }
    } ).setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, true, false ) );
    yearLabel = new Label( composite, SWT.NONE );
    yearLabel.setFont( new Font( parent.getDisplay(), "sans-serif", 24, SWT.NORMAL ) );
    yearLabel.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, true, false ) );
    createButton( composite, "Next", new Listener() {
      @Override
      public void handleEvent( Event event ) {
        showNext();
      }
    } ).setLayoutData( new GridData( SWT.RIGHT, SWT.CENTER, true, false ) );
    createTable( composite );
  }

  private void createTable( Composite parent ) {
    table = new Table( parent, SWT.SINGLE );
    GridData tableData = new GridData( SWT.FILL, SWT.FILL, true, true );
    tableData.verticalIndent = 20;
    tableData.horizontalSpan = 3;
    table.setLayoutData( tableData );
    TableColumn column0 = new TableColumn( table, SWT.NONE );
    column0.setText( " " );
    column0.setWidth( 10 );
    TableColumn column1 = new TableColumn( table, SWT.NONE );
    column1.setText( "Browser" );
    column1.setWidth( 250 );
    TableColumn column2 = new TableColumn( table, SWT.NONE );
    column2.setText( "Market share" );
    column2.setWidth( 120 );
    createTableItems();
  }

  private void createTableItems() {
    List<String> columns = dataSet.getColumns();
    int index = 0;
    for( String column : columns ) {
      TableItem tableItem = new TableItem( table, SWT.NONE );
      tableItem.setText( 1, column );
      tableItem.setBackground( 0, new Color( table.getDisplay(), colors.get( index++ ) ) );
    }
  }

  private void showPrevious() {
    if( cursor > 0 ) {
      cursor--;
      updateItems();
    }
  }

  private void showNext() {
    if( cursor < dataSet.getRowCount() - 1 ) {
      cursor++;
      updateItems();
    }
  }

  private void updateItems() {
    DataItem row = dataSet.getRow( cursor );
    yearLabel.setText( row.getText() );
    float[] values = row.getValues();
    TableItem[] tableItems = table.getItems();
    DecimalFormat decimalFormat = new DecimalFormat( "#.#" );
    for( int i = 0; i < values.length; i++ ) {
      float value = values[i];
      String text = decimalFormat.format( value ) + "%";
      tableItems[i].setText( 2, text );
      items.get( i ).asObject().set( "value", value ).set( "text", value > 5 ? text : "" );
    }
    pieChart.setChartData( items );
  }

  private static Button createButton( Composite parent, String text, Listener listener ) {
    Button button = new Button( parent, SWT.PUSH );
    button.setText( text );
    button.addListener( SWT.Selection, listener );
    return button;
  }

}
