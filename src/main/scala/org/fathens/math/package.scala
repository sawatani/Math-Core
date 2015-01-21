package org.fathens

package object math {
  // Angular
  implicit def toRadians(d: Degrees) = Radians(d.value.toRadians)
  implicit def toDegrees(r: Radians) = Degrees(r.value.toDegrees)

  // Meter series
  implicit def toMeters(a: Length[_]) = Length[Meters](a)
  implicit def toKillometers(a: Length[_]) = Length[Killometers](a)
  implicit def toMillimeters(a: Length[_]) = Length[Millimeters](a)
  implicit def toNanometers(a: Length[_]) = Length[Nanometers](a)
  // Inch
  implicit def toInch(a: Length[_]) = Length[Inch](a)

  implicit class UnitDouble(d: Double) {
    def *[A <: NumUnit[A]](u: NumUnit[A]): A = u * d
  }
}
