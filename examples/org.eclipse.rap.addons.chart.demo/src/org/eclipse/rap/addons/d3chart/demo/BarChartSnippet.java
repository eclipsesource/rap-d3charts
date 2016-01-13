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

import static org.eclipse.rap.addons.d3chart.demo.internal.data.Colors.toCss;

import org.eclipse.rap.addons.d3chart.basic.BarChart;
import org.eclipse.rap.addons.d3chart.demo.internal.data.Colors;
import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonObject;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;


public class BarChartSnippet extends AbstractEntryPoint {

  @Override
  protected void createContents( Composite parent ) {
    BarChart chart = new BarChart( parent, SWT.NONE );
    chart.setLayoutData( new GridData( 300, 300 ) );
    chart.setBarWidth( 25 );
    chart.setChartData( createData() );
  }

  private static JsonArray createData() {
    Colors colors = Colors.cat10Colors();
    return new JsonArray()
      .add( createItem( "Chrome", toCss( colors.next() ), 0.4f ) )
      .add( createItem( "Firefox", toCss( colors.next() ), 0.2f ) )
      .add( createItem( "IE", toCss( colors.next() ), 0.3f ) );
  }

  private static JsonObject createItem( String text, String color, float value ) {
    return new JsonObject()
      .add( "text", text )
      .add( "color", color )
      .add( "value", value );
  }

}
