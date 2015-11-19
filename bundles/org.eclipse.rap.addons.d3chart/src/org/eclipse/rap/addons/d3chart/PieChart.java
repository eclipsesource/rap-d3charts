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


public class PieChart extends Chart {

  private float startAngle = 0;
  private float endAngle = 1;
  private float innerRadius = 0;

  public PieChart( Composite parent, int style ) {
    super( parent, style, "pieChart" );
    updateConfig();
  }

  public float getStartAngle() {
    checkWidget();
    return startAngle;
  }

  public void setStartAngle( float angle ) {
    checkWidget();
    if( angle != startAngle ) {
      startAngle = angle;
      updateConfig();
    }
  }

  public float getEndAngle() {
    checkWidget();
    return endAngle;
  }

  public void setEndAngle( float angle ) {
    checkWidget();
    if( angle != endAngle ) {
      endAngle = angle;
      updateConfig();
    }
  }

  public float getInnerRadius() {
    checkWidget();
    return innerRadius;
  }

  public void setInnerRadius( float radius ) {
    checkWidget();
    if( radius != innerRadius ) {
      innerRadius = radius;
      updateConfig();
    }
  }

  private void updateConfig() {
    setConfig( new JsonObject()
      .add( "startAngle", startAngle * Math.PI * 2 )
      .add( "endAngle", endAngle * Math.PI * 2 )
      .add( "innerRadius", innerRadius )
      .add( "outerRadius", 1 ) );
  }

}
