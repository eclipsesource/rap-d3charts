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

d3chart.pieChart = function() {

  var arc = d3.svg.arc();
  var layout = d3.layout.pie().sort( null )
    .value( function( item ) { return item.value || 0; } );

  function render( chart, data ) {
    var centerX = chart._width / 2;
    var centerY = chart._height / 2;
    var maxRadius = Math.min( centerX, centerY ) - chart._padding;
    layout
      .startAngle( chart._config.startAngle )
      .endAngle( chart._config.endAngle );
    arc
      .outerRadius( chart._config.outerRadius * maxRadius )
      .innerRadius( chart._config.innerRadius * maxRadius );
    var layer = chart.getLayer( "layer" );
    layer.attr( "transform", "translate(" + centerX + "," + centerY + ")" );
    var selection = layer.selectAll( "g.segment" )
      .data( layout( data ) );
    show( selection );
    updateSegments( selection );
    createSegments( selection.enter(), chart );
    removeSegments( selection.exit() );
  }

  return render;

  function createSegments( selection, chart ) {
    var newGroups = selection.append( "svg:g" )
      .attr( "class", "segment" )
      .attr( "opacity", 0.0 );
    createPaths( newGroups );
    createTexts( newGroups );
    newGroups.on( "click", function( datum, index ) { chart.notifySelection( index ); } );
    show( newGroups );
  }

  function createPaths( selection ) {
    selection.append( "svg:path" )
      .attr( "fill", function( item ) { return item.data.color || "#000"; } )
      .attr( "d", arc )
      .each( function( datum ) {
        this._buffer = { startAngle: datum.startAngle, endAngle: datum.endAngle };
      } );
  }

  function createTexts( selection ) {
    selection.append( "svg:text" )
      .attr( "opacity", 1.0 )
      .style( "font-family", "sans-serif" )
      .style( "font-size", "11px" )
      .style( "fill", "white" )
      .attr( "transform", function( datum ) { return "translate(" + arc.centroid( datum ).join( "," ) + ")"; } )
      .attr( "dy", ".35em" )
      .attr( "text-anchor", "middle" )
      .text( function( item ) { return item.data.text || ""; } );
  }

  function updateSegments( selection ) {
    updatePaths( selection.select( "path" ) );
    updateTexts( selection.select( "text" ) );
  }

  function updatePaths( selection ) {
    selection
      .transition()
      .duration( 1000 )
      .attr( "fill", function( item ) { return item.data.color || "#000"; } )
      .attrTween( "d", function( datum ) {
        var previous = this._buffer;
        var interpolate = d3.interpolate( previous, datum );
        this._buffer = { startAngle: datum.startAngle, endAngle: datum.endAngle };
        return function( t ) {
          return arc( interpolate( t ) );
        };
      } );
  }

  function updateTexts( selection ) {
    selection
      .transition()
      .duration( 500 )
      .attr( "opacity", 0.0 )
      .transition()
      .duration( 100 )
      .attr( "transform", function( datum ) { return "translate(" + arc.centroid( datum ).join( "," ) + ")"; })
      .attr( "dy", ".35em" )
      .attr( "text-anchor", "middle" )
      .text( function( item ) { return item.data.text || ""; } )
      .transition()
      .duration( 500 )
      .attr( "opacity", 1.0 );
  }

  function removeSegments( selection ) {
    selection
      .transition()
      .duration( 500 )
      .attr( "opacity", 0.0 )
      .remove();
  }

  function show( selection ) {
    selection.transition().duration( 1000 ).attr( "opacity", 1.0 );
  }

};
