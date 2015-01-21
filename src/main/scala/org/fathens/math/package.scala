package org.fathens

import scala.reflect.runtime.{ universe => ru }

package object math {
  // Angular
  implicit def toRadians(d: Degrees) = Radians(d.value.toRadians)
  implicit def toDegrees(r: Radians) = Degrees(r.value.toDegrees)
  
  // Meter series
  implicit def toMeters(a: MeterUnit[_]) = MeterUnit[Meters](a)
  implicit def toKillometers(a: MeterUnit[_]) = MeterUnit[Killometers](a)
  implicit def toMillimeters(a: MeterUnit[_]) = MeterUnit[Millimeters](a)
  implicit def toNanometers(a: MeterUnit[_]) = MeterUnit[Nanometers](a)
  
  // Pixel
  implicit def toInch(px: Pixel) = Inch(px.value / px.dpi)
  // Inch
  implicit def toInch(a: Meters) = Inch(a)
  implicit def toInch(a: Killometers) = Inch(a)
  implicit def toInch(a: Millimeters) = Inch(a)
  implicit def toInch(a: Nanometers) = Inch(a)
}
