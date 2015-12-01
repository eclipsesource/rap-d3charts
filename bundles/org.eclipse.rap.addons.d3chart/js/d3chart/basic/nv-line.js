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

d3chart.register("nv-line", function(widget) {
  var chart = nv.models.lineChart();
  chart.yAxis.tickFormat(d3.format("d"));
  chart.yAxis.tickFormat(d3.format("d"));
  chart.lines.dispatch.on("elementClick", function(item) {
    widget.notifySelection(item.seriesIndex, item.pointIndex);
  });
  chart.xAxisLabel = chart.xAxis.axisLabel.bind(chart);
  chart.yAxisLabel = chart.yAxis.axisLabel.bind(chart);
  chart.xAxisFormat = function(value) {
    return chart.xAxis.tickFormat(d3.format(value));
  };
  chart.yAxisFormat = function(value) {
    return chart.yAxis.tickFormat(d3.format(value));
  };
  return chart;
});
