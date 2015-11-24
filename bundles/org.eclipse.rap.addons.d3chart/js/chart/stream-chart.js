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

d3chart.streamChart = function() {

  var stack = d3.layout.stack()
    .offset( "wiggle" )
    .values( function( d ) { return d.values; } );
  var area = d3.svg.area();
  var config = {
    width: 0,
    height: 0,
    margin: 0
  };

  function render( chart, data ) {
    var width = config.width - config.margin * 2;
    var height = config.height - config.margin * 2;
    var xScale = d3.scale.linear()
      .domain( [ 0, 17 ] )
      .range( [ config.margin, width ] );
    var yScale = d3.scale.linear()
      .domain( [ 0, 160 ] )
      .range( [ height, config.margin ] );
    area
      .x( function( d ) { return xScale( d.x ); } )
      .y0( function( d ) { return yScale( d.y0 ); } )
      .y1( function( d ) { return yScale( d.y0 + d.y ); } );
    var items = data.map( function( item ) {
      return {
        item: item,
        values: ( item.values || []).map( function( value, index ) {
          return { x: index, y: value };
        } )
      };
    } );
    var selection = chart.getLayer( "layer" ).selectAll( "g.item" ).data( stack( items ) );
    createElements( selection.enter(), chart );
    updateElements( selection );
    removeElements( selection.exit() );
  }

  d3chart.addConfigOptions( render, config );

  return render;

  function createElements( selection, chart ) {
    var items = selection.append( "svg:g" )
      .attr( "class", "item" )
      .attr( "opacity", 1.0 );
    items.on( "click", function( datum, index ) { chart.notifySelect( index ); } );
    // createStreams
    items.append( "svg:path" )
      .attr( "d", function( d ) { return area( d.values ); } )
      .style( "fill", function( d ) { return d.item.color || "#000"; } )
      .append( "svg:title" )
        .text( function( d ) { return d.item.text || ""; } );
    // createTexts
    items.append( "svg:text" )
      .attr( "text", function( d ) { return d.item.text || ""; } );
  }

  function updateElements( selection ) {
    // updateStreams
    selection.select( "path" )
      .transition()
      .duration( 1000 )
      .attr( "d", function( d ) { return area( d.values ); } );
  }

  function removeElements( selection ) {
    selection
      .transition()
      .duration( 400 )
      .attr( "opacity", 0.0 )
      .remove();
  }

};
