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

d3chart.barChart = function( widget ) {

  var xScale = d3.scale.linear().domain( [ 0, 1 ] );
  var config = {
    width: 0,
    height: 0,
    margin: 0,
    barWidth: 25,
    spacing: 2
  };

  function render( selection ) {
    xScale.range( [ 0, config.width - config.margin * 2 ] );
    var items = selection.selectAll( "g.item" ).data( selection.datum() );
    createElements( items.enter() );
    updateElements( items );
    removeElements( items.exit() );
  }

  d3chart.addConfigOptions( render, config );

  return render;

  function createElements( selection ) {
    var items = selection.append( "svg:g" )
      .attr( "class", "item" )
      .attr( "opacity", 1.0 );
    items.on( "click", function( datum, index ) { widget.notifySelection( index ); } );
    // createBars
    items.append( "svg:rect" )
      .attr( "x", config.margin )
      .attr( "y", function( item, index ) { return getOffset( index ); } )
      .attr( "width", 0 )
      .attr( "height", config.barWidth )
      .attr( "fill", function( item ) { return item.color || "#000"; } );
    // createTexts
    items.append( "svg:text" )
      .attr( "x", config.margin )
      .attr( "y", function( item, index ) { return getOffset( index ) + config.barWidth / 2; } )
      .attr( "text-anchor", "left" )
      .attr( "dy", ".35em" )
      .style( "font-family", "sans-serif" )
      .style( "font-size", "11px" );
  }

  function updateElements( selection ) {
    // updateBars
    selection.select( "rect" )
      .transition()
      .duration( 1000 )
      .attr( "y", function( item, index ) { return getOffset( index ); } )
      .attr( "width", function( item ) { return xScale( item.value || 0 ); } )
      .attr( "height", config.barWidth )
      .attr( "fill", function( item ) { return item.color || "#000"; } );
    // updateTexts
    selection.select( "text" )
      .transition()
      .duration( 1000 )
      .attr( "x", function( item ) { return config.margin + 6 + xScale( item.value || 0 ); } )
      .attr( "y", function( item, index ) { return getOffset( index ) + config.barWidth / 2; } )
      .text( function( item ) { return item.text || ""; } );
  }

  function removeElements( selection ) {
    selection
      .transition()
      .duration( 400 )
      .attr( "opacity", 0.0 )
      .remove();
  }

  function getOffset( index ) {
    return config.margin + index * ( config.barWidth + config.spacing );
  }

};
