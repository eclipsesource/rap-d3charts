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

d3chart.barChart = function() {

  var xScale = d3.scale.linear().domain( [ 0, 1 ] );

  return {

    layout: function() {},

    render: function( chart, data ) {
      xScale.range( [ 0, chart._width - chart._padding * 2 ] );
      var selection = chart.getLayer( "layer" ).selectAll( "g.item" ).data( data );
      createElements( selection.enter(), chart );
      updateElements( selection, chart );
      removeElements( selection.exit() );
    }

  };

  function createElements( selection, chart ) {
    var items = selection.append( "svg:g" )
      .attr( "class", "item" )
      .attr( "opacity", 1.0 );
    items.on( "click", function( datum, index ) { chart.notifySelection( index ); } );
    createBars( items, chart );
    createTexts( items, chart );
  }

  function createBars( selection, chart ) {
    selection.append( "svg:rect" )
      .attr( "x", chart._padding )
      .attr( "y", function( item, index ) { return getOffset( chart, index ); } )
      .attr( "width", 0 )
      .attr( "height", chart._config.barWidth )
      .attr( "fill", function( item ) { return item.color || "#000"; } );
  }

  function createTexts( selection, chart ) {
    selection.append( "svg:text" )
      .attr( "x", chart._padding )
      .attr( "y", function( item, index ) { return getOffset( chart, index ) + chart._config.barWidth / 2; } )
      .attr( "text-anchor", "left" )
      .attr( "dy", ".35em" )
      .style( "font-family", "sans-serif" )
      .style( "font-size", "11px" );
  }

  function updateElements( selection, chart ) {
    updateBars( selection.select( "rect" ), chart );
    updateTexts( selection.select( "text" ), chart );
  }

  function updateBars( selection, chart ) {
    selection
      .transition()
      .duration( 1000 )
      .attr( "y", function( item, index ) { return getOffset( chart, index ); } )
      .attr( "width", function( item ) { return xScale( item.value || 0 ); } )
      .attr( "height", chart._config.barWidth )
      .attr( "fill", function( item ) { return item.color || "#000"; } );
  }

  function updateTexts( selection, chart ) {
    selection
      .transition()
      .duration( 1000 )
      .attr( "x", function( item ) { return chart._padding + 6 + xScale( item.value || 0 ); } )
      .attr( "y", function( item, index ) { return getOffset( chart, index ) + chart._config.barWidth / 2; } )
      .text( function( item ) { return item.text || ""; } );
  }

  function removeElements( selection ) {
    selection
      .transition()
      .duration( 400 )
      .attr( "opacity", 0.0 )
      .remove();
  }

  function getOffset( chart, index ) {
    return chart._padding + index * ( chart._config.barWidth + chart._config.spacing );
  }

};
