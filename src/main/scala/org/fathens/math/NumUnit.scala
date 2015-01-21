package org.fathens.math

import scala.reflect.runtime.{ universe => ru }

object NumUnit {
  def apply[A: ru.TypeTag](v: Double): A = {
    val typeA = ru.typeOf[A]
    ru.runtimeMirror(getClass.getClassLoader)
      .reflectClass(
        typeA.typeSymbol.asClass
      ).reflectConstructor(
          typeA.decl(ru.termNames.CONSTRUCTOR).asMethod
        )(v).asInstanceOf[A]
  }
}
abstract class NumUnit[A <: NumUnit[A]](implicit tag: ru.TypeTag[A]) extends Ordered[A] {
  /**
   * Concrete value
   */
  val value: Double
  /**
   * Create another instance of given value
   */
  protected[math] def withValue(v: Double) = NumUnit[A](v)

  // Basic arithmetic operations

  def +(b: NumUnit[A]) = withValue(this.value + b.value)
  def -(b: NumUnit[A]) = withValue(this.value - b.value)
  def *(b: Double) = withValue(this.value * b)
  def /(b: Double) = withValue(this.value / b)

  def %(b: Double) = withValue(this.value % b)
  def ^(b: Double) = withValue(scala.math.pow(this.value, b))

  def unary_+ = this
  def unary_- = withValue(-this.value)

  def compare(b: A) = if (this.value < b.value) -1 else if (this.value > b.value) 1 else 0
}

/**
 * Represent angular
 */
abstract class Angular[A <: NumUnit[A]](implicit tag: ru.TypeTag[A]) extends NumUnit[A]

case class Degrees(value: Double) extends Angular[Degrees]
case class Radians(value: Double) extends Angular[Radians]

/**
 * Represent length
 */
object Length {
  def apply[A <: Length[A]](b: Length[_])(implicit typeTag: ru.TypeTag[A]): A = {
    NumUnit(b.value * b.rateToMeters / NumUnit(0).rateToMeters)
  }
}
abstract class Length[A <: NumUnit[A]](val rateToMeters: Double)(implicit tag: ru.TypeTag[A]) extends NumUnit[A]

object Inch {
  val rateToMeters = 2.54e-2
}
case class Inch(value: Double) extends Length[Inch](Inch.rateToMeters)
object Pixel {
  def apply(dpi: Double)(i: Inch): Pixel = Pixel(i.value * dpi, dpi)
}
case class Pixel(value: Double, dpi: Double) extends Length[Pixel](Inch.rateToMeters / dpi) {
  override protected[math] def withValue(v: Double) = Pixel(v, dpi)
}

/**
 * Meter series
 */
case class Meters(value: Double) extends Length[Meters](1)
case class Killometers(value: Double) extends Length[Killometers](1e+3)
case class Millimeters(value: Double) extends Length[Millimeters](1e-3)
case class Nanometers(value: Double) extends Length[Nanometers](1e-9)
