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
abstract class NumUnit[A: ru.TypeTag] {
  /**
   * Concrete value
   */
  val value: Double
  /**
   * Create another instance of given value
   */
  protected def withValue(v: Double) = NumUnit[A](v)

  // Basic arithmetic operations

  def +(b: NumUnit[A]) = withValue(this.value + b.value)
  def -(b: NumUnit[A]) = withValue(this.value - b.value)
  def *(b: Double) = withValue(this.value * b)
  def /(b: Double) = withValue(this.value / b)
}

/**
 * Represent angular
 */
abstract class Angular[A: ru.TypeTag] extends NumUnit[A]

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
abstract class Length[A: ru.TypeTag](val rateToMeters: Double) extends NumUnit[A]

object Inch {
  val rateToMeters = 2.54e-2
}
case class Inch(value: Double) extends Length[Inch](Inch.rateToMeters)
object Pixel {
  def apply(dpi: Double)(i: Inch): Pixel = Pixel(i.value * dpi, dpi)
}
case class Pixel(value: Double, dpi: Double) extends Length[Pixel](Inch.rateToMeters / dpi) {
  override protected def withValue(v: Double) = Pixel(v, dpi)
}

/**
 * Meter series
 */
sealed abstract class MeterUnit[A: ru.TypeTag](override val rateToMeters: Double) extends Length[A](rateToMeters)
case class Meters(value: Double) extends MeterUnit[Meters](1)
case class Killometers(value: Double) extends MeterUnit[Killometers](1e+3)
case class Millimeters(value: Double) extends MeterUnit[Millimeters](1e-3)
case class Nanometers(value: Double) extends MeterUnit[Nanometers](1e-9)
