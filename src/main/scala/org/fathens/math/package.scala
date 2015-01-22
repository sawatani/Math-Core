package org.fathens

package object math {
  // Angular
  implicit def toRadians(a: Angular[_]): Radians = a.convertTo[Radians]
  implicit def toDegrees(a: Angular[_]): Degrees = a.convertTo[Degrees]

  // Meter series
  implicit def toMeters(a: LengthMeasure[_]): Meters = a.convertTo[Meters]
  implicit def toKillometers(a: LengthMeasure[_]): Killometers = a.convertTo[Killometers]
  implicit def toMillimeters(a: LengthMeasure[_]): Millimeters = a.convertTo[Millimeters]
  implicit def toNanometers(a: LengthMeasure[_]): Nanometers = a.convertTo[Nanometers]
  // Inch
  implicit def toInch(a: LengthMeasure[_]): Inch = a.convertTo[Inch]

  implicit class UnitDouble(d: Double) {
    def *[U <: NumUnit[U]](u: U): U = u * d
  }

  // Trigonometric Functions
  def sin(θ: Radians): Double = scala.math.sin(θ.value)
  def cos(θ: Radians): Double = scala.math.cos(θ.value)
  def tan(θ: Radians): Double = scala.math.tan(θ.value)
  def asin(v: Double): Radians = Radians(scala.math.asin(v))
  def acos(v: Double): Radians = Radians(scala.math.acos(v))
  def atan(v: Double): Radians = Radians(scala.math.atan(v))
  def atan2(x: Double, y: Double): Radians = Radians(scala.math.atan2(x, y))

  // Camparations
  def min[U <: NumUnit[U]](a: U, b: U): U = if (a <= b) a else b
  def max[U <: NumUnit[U]](a: U, b: U): U = if (a >= b) a else b

  implicit val precision = Precision.SignificantFigures(10)
}
