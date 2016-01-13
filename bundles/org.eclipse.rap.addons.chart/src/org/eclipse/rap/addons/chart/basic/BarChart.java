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
package org.eclipse.rap.addons.chart.basic;

import org.eclipse.rap.addons.chart.Chart;
import org.eclipse.swt.widgets.Composite;


public class BarChart extends Chart {

  private int barWidth = 25;
  private int spacing = 2;

  public BarChart( Composite parent, int style ) {
    super( parent, style, "basic-bar" );
    requireJs( registerResource( "chart/basic/bar.js" ) );
  }

  public int getBarWidth() {
    checkWidget();
    return barWidth;
  }

  public void setBarWidth( int width ) {
    checkWidget();
    if( width != barWidth ) {
      barWidth = width;
      setOption( "barWidth", width );
    }
  }

  public int getSpacing() {
    checkWidget();
    return spacing;
  }

  public void setSpacing( int width ) {
    checkWidget();
    if( width != spacing ) {
      spacing = width;
      setOption( "spacing", width );
    }
  }

}
