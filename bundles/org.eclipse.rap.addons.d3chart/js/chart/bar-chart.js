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

d3chart.BarChart = function( parent ) {
  d3chart.Chart.call( this, parent, {
    barWidth: 25,
    spacing: 2
  });
};

d3chart.BarChart.prototype = d3chart.extend({}, d3chart.Chart.prototype, {

  layout: function() {
    this._layer = this.getLayer( "layer" );
  },

  render: function() {
    this._xScale = d3.scale.linear().domain( [ 0, 1 ] ).range( [ 0, this._width - this._padding * 2 ] );
    var selection = this._layer.selectAll( "g.item" ).data( this._data );
    this._createElements( selection.enter() );
    this._updateElements( selection );
    this._removeElements( selection.exit() );
  },

  _createElements: function( selection ) {
    var that = this;
    var items = selection.append( "svg:g" )
      .attr( "class", "item" )
      .attr( "opacity", 1.0 );
    items.on( "click", function( datum, index ) { that._selectItem( index ); } );
    this._createBars( items );
    this._createTexts( items );
  },

  _createBars: function( selection ) {
    var that = this;
    selection.append( "svg:rect" )
      .attr( "x", that._padding )
      .attr( "y", function( item, index ) { return that._getOffset( index ); } )
      .attr( "width", 0 )
      .attr( "height", that._config.barWidth )
      .attr( "fill", function( item ) { return item.color || "#000"; } );
  },

  _createTexts: function( selection ) {
    var that = this;
    selection.append( "svg:text" )
      .attr( "x", that._padding )
      .attr( "y", function( item, index ) { return that._getOffset( index ) + that._config.barWidth / 2; } )
      .attr( "text-anchor", "left" )
      .attr( "dy", ".35em" )
      .style( "font-family", "sans-serif" )
      .style( "font-size", "11px" );
  },

  _updateElements: function( selection ) {
    this._updateBars( selection.select( "rect" ) );
    this._updateTexts( selection.select( "text" ) );
  },

  _updateBars: function( selection ) {
    var that = this;
    selection
      .transition()
      .duration( 1000 )
      .attr( "y", function( item, index ) { return that._getOffset( index ); } )
      .attr( "width", function( item ) { return that._xScale( item.value || 0 ); } )
      .attr( "height", that._config.barWidth )
      .attr( "fill", function( item ) { return item.color || "#000"; } );
  },

  _updateTexts: function( selection ) {
    var that = this;
    selection
      .transition()
      .duration( 1000 )
      .attr( "x", function( item ) { return that._padding + 6 + that._xScale( item.value || 0 ); } )
      .attr( "y", function( item, index ) { return that._getOffset( index ) + that._config.barWidth / 2; } )
      .text( function( item ) { return item.text || ""; } );
  },

  _removeElements: function( selection ) {
    selection
      .transition()
      .duration( 400 )
      .attr( "opacity", 0.0 )
      .remove();
  },

  _selectItem: function( index ) {
    var remoteObject = rap.getRemoteObject( this );
    remoteObject.notify( "Selection", { "index": index } );
  },

  _getOffset: function( index ) {
    return this._padding + index * ( this._config.barWidth + this._config.spacing );
  }

});

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.BarChart", {

  factory: function( properties ) {
    var parent = rap.getObject( properties.parent );
    return new d3chart.BarChart( parent );
  },

  destructor: "destroy",

  properties: [ "config", "items" ],

  events: [ "Selection" ]

} );
