/*******************************************************************************
 * Copyright (c) 2016 EclipseSource and others.
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


public abstract class NvChart extends Chart {

  private static final String NVD3_CSS_URL
    = "https://cdnjs.cloudflare.com/ajax/libs/nvd3/1.8.1/nv.d3.min.css";
  private static final String NVD3_JS_URL
    = "https://cdnjs.cloudflare.com/ajax/libs/nvd3/1.8.1/nv.d3.min.js";

  public NvChart( Composite parent, int style, String renderer ) {
    super( parent, style, renderer );
    requireJs( NVD3_JS_URL );
    requireCss( NVD3_CSS_URL );
  }

}
