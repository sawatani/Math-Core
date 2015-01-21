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
    def *[U <: NumUnit[U]](u: U): U = u * d
  }

  // Trigonometric Functions
  def sin(θ: Radians) = scala.math.sin(θ.value)
  def cos(θ: Radians) = scala.math.cos(θ.value)
  def tan(θ: Radians) = scala.math.tan(θ.value)
  def asin(v: Double) = Radians(scala.math.asin(v))
  def acos(v: Double) = Radians(scala.math.acos(v))
  def atan(v: Double) = Radians(scala.math.atan(v))
  def atan2(x: Double, y: Double) = Radians(scala.math.atan2(x, y))

  // Calculations
  def sqrt[U <: NumUnit[U]](u: U): U = u withValue scala.math.sqrt(u.value)
  def cbrt[U <: NumUnit[U]](u: U): U = u withValue scala.math.cbrt(u.value)
  def abs[U <: NumUnit[U]](u: U): U = u withValue scala.math.abs(u.value)

  def min[U <: NumUnit[U]](a: U, b: U): U = if (a <= b) a else b
  def max[U <: NumUnit[U]](a: U, b: U): U = if (a >= b) a else b
  

  def ss[U <: NumUnit[U]](a: U, b: U): U = if (a != b) a else b
}
