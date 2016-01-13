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
package org.eclipse.rap.addons.chart.demo.internal.data;

import java.util.Arrays;

import org.eclipse.swt.graphics.RGB;


public class Colors {

  private static final RGB[] CAT10_COLORS = {
    new RGB( 0x1f, 0x77, 0xb4 ),
    new RGB( 0xff, 0x7f, 0x0e ),
    new RGB( 0x2c, 0xa0, 0x2c ),
    new RGB( 0xd6, 0x27, 0x28 ),
    new RGB( 0x94, 0x67, 0xbd ),
    new RGB( 0x8c, 0x56, 0x4b ),
    new RGB( 0xe3, 0x77, 0xc2 ),
    new RGB( 0x7f, 0x7f, 0x7f ),
    new RGB( 0xbc, 0xbd, 0x22 ),
    new RGB( 0x17, 0xbe, 0xcf )
  };
  private static final RGB[] CAT20_COLORS = {
    new RGB( 0x1f, 0x77, 0xb4 ),
    new RGB( 0xae, 0xc7, 0xe8 ),
    new RGB( 0xff, 0x7f, 0x0e ),
    new RGB( 0xff, 0xbb, 0x78 ),
    new RGB( 0x2c, 0xa0, 0x2c ),
    new RGB( 0x98, 0xdf, 0x8a ),
    new RGB( 0xd6, 0x27, 0x28 ),
    new RGB( 0xff, 0x98, 0x96 ),
    new RGB( 0x94, 0x67, 0xbd ),
    new RGB( 0xc5, 0xb0, 0xd5 ),
    new RGB( 0x8c, 0x56, 0x4b ),
    new RGB( 0xc4, 0x9c, 0x94 ),
    new RGB( 0xe3, 0x77, 0xc2 ),
    new RGB( 0xf7, 0xb6, 0xd2 ),
    new RGB( 0x7f, 0x7f, 0x7f ),
    new RGB( 0xc7, 0xc7, 0xc7 ),
    new RGB( 0xbc, 0xbd, 0x22 ),
    new RGB( 0xdb, 0xdb, 0x8d ),
    new RGB( 0x17, 0xbe, 0xcf ),
    new RGB( 0x9e, 0xda, 0xe5 )
  };

  private final RGB[] colors;
  private int cursor;

  public Colors( RGB... colors ) {
    if( colors.length == 0 ) {
      throw new IllegalArgumentException( "Cannot create ColorSequence without any colors" );
    }
    this.colors = Arrays.copyOf( colors, colors.length );
  }

  public RGB get( int index ) {
    RGB color = colors[index];
    return new RGB( color.red, color.green, color.blue );
  }

  public RGB next() {
    return colors[cursor++ % colors.length];
  }

  public int size() {
    return colors.length;
  }

  public static Colors cat10Colors() {
    return new Colors( CAT10_COLORS );
  }

  public static Colors cat20Colors() {
    return new Colors( CAT20_COLORS );
  }

  public static String toCss( RGB color ) {
    return new StringBuilder()
      .append( "#" )
      .append( getHexStr( color.red ) )
      .append( getHexStr( color.green ) )
      .append( getHexStr( color.blue ) )
      .toString();
  }

  private static String getHexStr( int value ) {
    String hex = Integer.toHexString( value );
    return hex.length() == 1 ? "0" + hex : hex;
  }

}
