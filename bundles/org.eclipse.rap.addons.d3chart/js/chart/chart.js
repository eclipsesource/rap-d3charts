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

d3chart.Chart = function( parent, renderer ) {
  this._data = [];
  this._renderer = renderer;
  this._element = this.createElement( parent );
  this._svg = d3.select( this._element ).append( "svg" ).attr( "class", "chart" );
  rap.on( "render", function() {
    if( this._needsRender ) {
      this._renderer( this, this._data );
      this._needsRender = false;
    }
  }.bind( this ) );
  parent.addListener( "Resize", function() {
    this._resize( parent.getClientArea() );
  }.bind( this) );
  this._resize( parent.getClientArea() );
};

d3chart.Chart.prototype = {

  setConfig: function( config ) {
    for( var name in config ) {
      if (typeof this._renderer[name] === "function") {
        this._renderer[name]( config[name] );
      }
    }
    this._scheduleUpdate();
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

  notifySelection: function( index ) {
    var remoteObject = rap.getRemoteObject( this );
    remoteObject.notify( "Selection", { "index": index } );
  },

  _resize: function( clientArea ) {
    var width = clientArea[ 2 ];
    var height = clientArea[ 3 ];
    this._renderer.width( width ).height( height );
    this._svg.attr( "width", width ).attr( "height", height );
    this._scheduleUpdate();
  },

  _scheduleUpdate: function() {
    this._needsRender = true;
  },

  destroy: function() {
    var element = this._element;
    if( element.parentNode ) {
      element.parentNode.removeChild( element );
    }
  }

};

// UTILITIES

d3chart.addConfigOptions = function( target, config ) {
  for( var prop in config ) {
    addOption( target, config, prop );
  }
};

function addOption( target, config, prop ) {
  target[prop] = function( value ) {
    if (!arguments.length) {
      return config[prop];
    }
    config[prop] = value;
    return target;
  };
}

// TYPE HANDLER

rap.registerTypeHandler( "d3chart.Chart", {

  factory: function( properties ) {
    var parent = rap.getObject( properties.parent );
    var renderer = d3chart[properties.renderer]();
    return new d3chart.Chart( parent, renderer );
  },

  destructor: "destroy",

  properties: [ "config", "items" ],

  events: [ "Selection" ]

} );
