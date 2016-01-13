/*******************************************************************************
 * Copyright (c) 2015, 2016 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.addons.d3chart.basic;

import org.eclipse.swt.widgets.Composite;


public class NvPieChart extends NvChart {

  private boolean showLabels = true;

  public NvPieChart( Composite parent, int style ) {
    super( parent, style, "nv-pie" );
    requireJs( registerResource( "d3chart/basic/nv-pie.js" ) );
  }

  public void setShowLabels( boolean show ) {
    checkWidget();
    if( show != showLabels ) {
      showLabels = show;
      setOption( "showLabels", show );
    }
  }

  public boolean getShowLabels() {
    checkWidget();
    return showLabels;
  }

}
