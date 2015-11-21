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
  var config = {
    width: 0,
    height: 0,
    margin: 0,
    startAngle: 0,
    endAngle: 2 * Math.PI,
    outerRadius: 1,
    innerRadius: 0
  };

  function render( selection, chart ) {
    var centerX = config.width / 2;
    var centerY = config.height / 2;
    var maxRadius = Math.min( centerX, centerY ) - config.margin;
    layout
      .startAngle( config.startAngle )
      .endAngle( config.endAngle );
    arc
      .outerRadius( config.outerRadius * maxRadius )
      .innerRadius( config.innerRadius * maxRadius );
    // TODO improve
    var root = selection.select( "g.root" );
    if( root.empty() ) {
      root = selection.append( "svg:g" ).attr( "class", "root" );
    }
    root.attr( "transform", "translate(" + centerX + "," + centerY + ")" );
    var segments = root.selectAll( "g.segment" ).data( layout( selection.datum() ) );
    show( selection );
    updateSegments( segments );
    createSegments( segments.enter(), chart );
    removeSegments( segments.exit() );
  }

  d3chart.addConfigOptions( render, config );

  return render;

  function createSegments( selection, chart ) {
    var newGroups = selection.append( "svg:g" )
      .attr( "class", "segment" )
      .attr( "opacity", 0.0 );
    newGroups.on( "click", function( datum, index ) { chart.notifySelection( index ); } );
    // createPaths
    newGroups.append( "svg:path" )
      .attr( "fill", function( item ) { return item.data.color || "#000"; } )
      .attr( "d", arc )
      .each( function( datum ) {
        this._buffer = { startAngle: datum.startAngle, endAngle: datum.endAngle };
      } );
    // createTexts
    newGroups.append( "svg:text" )
      .attr( "opacity", 1.0 )
      .style( "font-family", "sans-serif" )
      .style( "font-size", "11px" )
      .style( "fill", "white" )
      .attr( "transform", function( datum ) { return "translate(" + arc.centroid( datum ).join( "," ) + ")"; } )
      .attr( "dy", ".35em" )
      .attr( "text-anchor", "middle" )
      .text( function( item ) { return item.data.text || ""; } );
    show( newGroups );
  }

  function updateSegments( selection ) {
    // updatePaths
    selection.select( "path" )
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
    // updateTexts
    selection.select( "text" )
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
