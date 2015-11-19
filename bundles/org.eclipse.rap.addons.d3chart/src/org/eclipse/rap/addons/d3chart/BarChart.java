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

import org.eclipse.rap.json.JsonObject;
import org.eclipse.swt.widgets.Composite;


public class BarChart extends Chart {

  private int barWidth = 25;
  private int spacing = 2;

  public BarChart( Composite parent, int style ) {
    super( parent, style, "barChart" );
    updateConfig();
  }

  public int getBarWidth() {
    checkWidget();
    return barWidth;
  }

  public void setBarWidth( int width ) {
    checkWidget();
    if( width != barWidth ) {
      barWidth = width;
      updateConfig();
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
      updateConfig();
    }
  }

  private void updateConfig() {
    setConfig( new JsonObject()
      .add( "barWidth", barWidth )
      .add( "spacing", spacing ) );
  }

}
