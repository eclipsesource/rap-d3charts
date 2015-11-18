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

d3chart.PieChart = function( parent ) {
  d3chart.Chart.call( this, parent, {
    startAngle: 0,
    endAngle: 2 * Math.PI,
    outerRadius: 1,
    innerRadius: 0
  });
  this._arc = d3.svg.arc();
  this._layout = d3.layout.pie().sort( null )
    .value( function( item ) { return item.value || 0; } )
    .startAngle( this._config.startAngle )
    .endAngle( this._config.endAngle );
};

d3chart.PieChart.prototype = d3chart.extend({}, d3chart.Chart.prototype, {

  layout: function() {
    var centerX = this._width / 2;
    var centerY = this._height / 2;
    var maxRadius = Math.min( centerX, centerY ) - this._padding;
    this._layout.startAngle( this._config.startAngle );
    this._layout.endAngle( this._config.endAngle );
    this._arc
      .outerRadius( this._config.outerRadius * maxRadius )
      .innerRadius( this._config.innerRadius * maxRadius );
    this._layer = this.getLayer( "layer" );
    this._layer.attr( "transform", "translate(" + centerX + "," + centerY + ")" );
  },

  render: function() {
    var selection = this._layer.selectAll( "g.segment" )
      .data( this._layout( this._data ) );
    this._show( selection );
    this._updateSegments( selection );
    this._createSegments( selection.enter() );
    this._removeSegments( selection.exit() );
  },

  _createSegments: function( selection ) {
    var that = this;
    var newGroups = selection.append( "svg:g" )
      .attr( "class", "segment" )
      .attr( "opacity", 0.0 );
    this._createPaths( newGroups );
    this._createTexts( newGroups );
    newGroups.on( "click", function( datum, index ) { that._selectItem( index ); } );
    this._show( newGroups );
  },

  _createPaths: function( selection ) {
    var that = this;
    selection.append( "svg:path" )
      .attr( "fill", function( item ) { return item.data.color || "#000"; } )
      .attr( "d", that._arc )
      .each( function( datum ) {
        this._buffer = { startAngle: datum.startAngle, endAngle: datum.endAngle };
      } );
  },

  _createTexts: function( selection ) {
    var that = this;
    selection.append( "svg:text" )
      .attr( "opacity", 1.0 )
      .style( "font-family", "sans-serif" )
      .style( "font-size", "11px" )
      .style( "fill", "white" )
      .attr( "transform", function( datum ) { return "translate(" + that._arc.centroid( datum ).join( "," ) + ")"; } )
      .attr( "dy", ".35em" )
      .attr( "text-anchor", "middle" )
      .text( function( item ) { return item.data.text || ""; } );
  },

  _updateSegments: function( selection ) {
    this._updatePaths( selection.select( "path" ) );
    this._updateTexts( selection.select( "text" ) );
  },

  _updatePaths: function( selection ) {
    var that = this;
    selection
      .transition()
      .duration( 1000 )
      .attr( "fill", function( item ) { return item.data.color || "#000"; } )
      .attrTween( "d", function( datum ) {
        var previous = this._buffer;
        var interpolate = d3.interpolate( previous, datum );
        this._buffer = { startAngle: datum.startAngle, endAngle: datum.endAngle };
        return function( t ) {
          return that._arc( interpolate( t ) );
        };
      } );
  },

  _updateTexts: function( selection ) {
    var that = this;
    selection
      .transition()
      .duration( 500 )
      .attr( "opacity", 0.0 )
      .transition()
      .duration( 100 )
      .attr( "transform", function( datum ) { return "translate(" + that._arc.centroid( datum ).join( "," ) + ")"; })
      .attr( "dy", ".35em" )
      .attr( "text-anchor", "middle" )
      .text( function( item ) { return item.data.text || ""; } )
      .transition()
      .duration( 500 )
      .attr( "opacity", 1.0 );
  },

  _removeSegments: function( selection ) {
    selection
      .transition()
      .duration( 500 )
      .attr( "opacity", 0.0 )
      .remove();
  },

  _selectItem: function( index ) {
    var remoteObject = rap.getRemoteObject( this );
    remoteObject.notify( "Selection", { "index": index } );
  },

  _show: function( selection ) {
    selection.transition().duration( 1000 ).attr( "opacity", 1.0 );
  }

});

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.PieChart", {

  factory: function( properties ) {
    var parent = rap.getObject( properties.parent );
    return new d3chart.PieChart( parent );
  },

  destructor: "destroy",

  properties: [ "config", "items" ],

  events: [ "Selection" ]

} );
