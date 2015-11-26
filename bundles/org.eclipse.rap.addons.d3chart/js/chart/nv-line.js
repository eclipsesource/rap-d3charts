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
/* global nv: false */

d3chart['nv-line'] = function(widget) {
  var chart = nv.models.lineChart();
  chart.xAxis
    .axisLabel("X-axis Label");
  chart.yAxis
    .axisLabel("Y-axis Label")
    .tickFormat(d3.format("d"));
  chart.lines.dispatch.on("elementClick", function(item) {
    widget.notifySelection(item.seriesIndex, item.pointIndex);
  });
  return chart;
};
