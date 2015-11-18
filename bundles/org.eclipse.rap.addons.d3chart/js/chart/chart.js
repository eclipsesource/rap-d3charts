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

d3chart = {};

d3chart.Chart = function( parent, config ) {
  this._data = [];
  this._config = config;
  this._element = this.createElement( parent );
  this._padding = 20;
  this._svg = d3.select( this._element ).append( "svg" ).attr( "class", "chart" );
  this._needsLayout = true;
  rap.on( "render", function() {
    if( this._needsRender ) {
      if( this._needsLayout ) {
        this.layout();
        this._needsLayout = false;
      }
      this.render();
      this._needsRender = false;
    }
  }.bind( this ) );
  parent.addListener( "Resize", function() {
    this._resize( parent.getClientArea() );
  }.bind( this) );
  this._resize( parent.getClientArea() );
};

d3chart.Chart.prototype = {

  render: function() {
  },

  layout: function() {
  },

  setConfig: function( config ) {
    this._config = config;
    this._scheduleUpdate( true );
  },

  setItems: function( data ) {
    this._data = data;
    this._scheduleUpdate();
  },

  createElement: function( parent ) {
    var element = document.createElement( "div" );
    element.style.position = "absolute";
    element.style.left = "0";
    element.style.top = "0";
    element.style.width = "100%";
    element.style.height = "100%";
    parent.append( element );
    return element;
  },

  getLayer: function( name ) {
    var layer = this._svg.select( "g." + name );
    if( layer.empty() ) {
      this._svg.append( "svg:g" ).attr( "class", name );
      layer = this._svg.select( "g." + name );
    }
    return layer;
  },

  _resize: function( clientArea ) {
    this._width = clientArea[ 2 ];
    this._height = clientArea[ 3 ];
    this._svg.attr( "width", this._width ).attr( "height", this._height );
    this._scheduleUpdate( true );
  },

  _scheduleUpdate: function( needsLayout ) {
    if( needsLayout ) {
      this._needsLayout = true;
    }
    this._needsRender = true;
  },

  destroy: function() {
    var element = this._element;
    if( element.parentNode ) {
      element.parentNode.removeChild( element );
    }
  }

};

d3chart.extend = function( target ) {
  for (var i = 1; i < arguments.length; i++) {
    var source = arguments[i];
    for (var name in source) {
      target[name] = source[name];
    }
  }
  return target;
};
