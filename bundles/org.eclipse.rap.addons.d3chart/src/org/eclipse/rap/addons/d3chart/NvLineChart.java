/*******************************************************************************
 * Copyright (c) 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.addons.d3chart;

import org.eclipse.swt.widgets.Composite;


public class NvLineChart extends Chart {

  public NvLineChart( Composite parent, int style ) {
    super( parent, style, "nv-line" );
    requireJs( "lib/nv.d3.js", "resources/nv.d3.js" );
    requireCss( "lib/nv.d3.css", "resources/nv.d3.css" );
    requireJs( "d3chart/nv-line.js", "chart/nv-line.js" );
  }

}
