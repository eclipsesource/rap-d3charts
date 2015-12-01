
describe( "Chart", function() {

  var parent;
  var chart;
  var captor;
  var renderer;
  var generator;

  beforeEach( function() {
    rap.setup();
    parent = {
      _listeners: {},
      append: function() {},
      addListener: function( event, listener ) {
        if( this._listeners[ event ] === undefined ) {
          this._listeners[ event ] = [];
        }
        this._listeners[ event ].push( listener );
      },
      notify: function( event ) {
        var listeners = this._listeners[ event ];
        if( listeners ) {
          for( var i = 0; i < listeners.length; i++ ) {
            listeners[ i ]();
          }
        }
      },
      getClientArea: function() { return [ 20, 30, 200, 300 ]; }
    };
    renderer = jasmine.createSpy();
    renderer.width = jasmine.createSpy().and.returnValue(renderer);
    renderer.height = jasmine.createSpy().and.returnValue(renderer);
    generator = function() { return renderer; };
    captor = [];
  } );

  it( "creates a DOM element and appends it to its parent", function() {
    spyOn( parent, "append" );

    chart = new d3chart.Chart( parent, generator );

    expect( parent.append ).toHaveBeenCalled();
    expect( parent.append.calls.mostRecent().args[0] ).toEqual(jasmine.any(Element));
    expect( parent.append.calls.mostRecent().args[0].tagName ).toBe("DIV");
  } );

  describe( "svg element", function() {

    beforeEach( function() {
      chart = new d3chart.Chart( parent, generator );
    } );

    it( "is created", function() {
      expect( chart._svg.node().tagName ).toBe( "svg" );
      expect( chart._svg.node().parentNode ).toBe( chart._element );
    } );

    it( "has initial size", function() {
      expect( chart._svg.attr( "width" ) ).toBe( "200" );
      expect( chart._svg.attr( "height" ) ).toBe( "300" );
    } );

    it( "is resized on parent resize", function() {
      parent.getClientArea = function() { return [ 20, 30, 400, 600 ]; };

      parent.notify( "Resize" );

      expect( chart._svg.attr( "width" ) ).toBe( "400" );
      expect( chart._svg.attr( "height" ) ).toBe( "600" );
    } );

  } );

  describe( "setOption", function() {

    beforeEach( function() {
      chart = new d3chart.Chart( parent, generator );
    } );

    it( "calls method on renderer", function() {
      renderer.foo = jasmine.createSpy();

      chart.setOption( "foo", 23 );

      expect( renderer.foo ).toHaveBeenCalledWith( 23 );
    } );

    it( "calls nested method on renderer", function() {
      renderer.foo = { bar: jasmine.createSpy() };

      chart.setOption( "foo.bar", 23 );

      expect( renderer.foo.bar ).toHaveBeenCalledWith( 23 );
    } );

  } );

} );
