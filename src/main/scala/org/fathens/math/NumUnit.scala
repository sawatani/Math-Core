package org.fathens.math

import scala.reflect.runtime.{ universe => ru }

/**
 * Measuring Numeric Unit
 */
abstract class NumUnit[A <: NumUnit[A]](implicit tag$: ru.TypeTag[A]) extends Ordered[A] {
  /**
   * Concrete value
   */
  protected val value: Double
  
  def toInt: Int = value.toInt
  def toLong: Long = value.toLong
  def toDouble: Double = value
  def toBigDecimal: BigDecimal = BigDecimal(value)
  
  /**
   * Create another instance of given value
   */
  protected def withValue(v: Double) = NumUnit[A](v)

  // Basic arithmetic operations

  def +(b: A) = withValue(this.value + b.value)
  def -(b: A) = withValue(this.value - b.value)
  def *(b: A) = this.value * b.value
  def /(b: A) = this.value / b.value
  def *(b: Double) = withValue(this.value * b)
  def /(b: Double) = withValue(this.value / b)

  def %(b: A) = withValue(this.value % b.value)
  def %(b: Double) = withValue(this.value % b)
  def ^(b: Double) = withValue(scala.math.pow(this.value, b))

  def sqrt = withValue(scala.math.sqrt(this.value))
  def cbrt = withValue(scala.math.cbrt(this.value))
  def abs = withValue(scala.math.abs(this.value))

  def unary_+ = withValue(+this.value)
  def unary_- = withValue(-this.value)

  def compare(b: A) = if (this.value < b.value) -1 else if (this.value > b.value) 1 else 0
}
object NumUnit {
  /**
   * Create new instance of given type and value
   */
  private def apply[U: ru.TypeTag](v: Double): U = {
    val typeA = ru.typeOf[U]
    ru.runtimeMirror(getClass.getClassLoader)
      .reflectClass(
        typeA.typeSymbol.asClass
      ).reflectConstructor(
          typeA.decl(ru.termNames.CONSTRUCTOR).asMethod
        )(v).asInstanceOf[U]
  }
  /**
   * Grouping trait of convertible each other
   */
  trait Convertible[G <: Convertible[G, _], A <: NumUnit[A]] { self: NumUnit[A] =>
    val rateToStandard: Double

    override def equals(b: Any) = b match {
      case b: Convertible[G, _] => Precision.compare(this.toStandard, b.toStandard)
      case _                    => false
    }
    def convertTo[U <: NumUnit[U]](implicit tag$: ru.TypeTag[U], e$: U <:< Convertible[G, U]): U = {
      NumUnit[U](0).asInstanceOf[Convertible[G, U]].fromStandard(this.toStandard)
    }
    private def toStandard: Double = this.value * this.rateToStandard
    private def fromStandard(v: Double): A = withValue(v / this.rateToStandard)
  }
}

/**
 * Represent angular
 */
abstract class Angular[A <: Angular[A]](val rateToStandard: Double)(implicit tag$: ru.TypeTag[A]) extends NumUnit[A] with NumUnit.Convertible[Angular[_], A] {
  def normalize = (this % Pi2 + Pi2) % Pi2
}
case class Degrees(protected val value: Double) extends Angular[Degrees](scala.math.Pi * 2 / 360)
case class Radians(protected val value: Double) extends Angular[Radians](1)

/**
 * Represent length
 */
abstract class Length[A <: Length[A]](implicit tag$: ru.TypeTag[A]) extends NumUnit[A]

object Pixel {
  def apply(dpi: Double)(i: Inch): Pixel = Pixel(i.toDouble * dpi, dpi)
}
case class Pixel(protected val value: Double, dpi: Double) extends NumUnit[Pixel] {
  override protected def withValue(v: Double) = Pixel(v, dpi)
  def toInch = Inch(value / dpi)
}

abstract class LengthMeasure[A <: LengthMeasure[A]](val rateToStandard: Double)(implicit tag$: ru.TypeTag[A]) extends Length[A] with NumUnit.Convertible[LengthMeasure[_], A]

case class Inch(protected val value: Double) extends LengthMeasure[Inch](2.54e-2)

/**
 * Meter series
 */
case class Meters(protected val value: Double) extends LengthMeasure[Meters](1)
case class Killometers(protected val value: Double) extends LengthMeasure[Killometers](1e+3)
case class Millimeters(protected val value: Double) extends LengthMeasure[Millimeters](1e-3)
case class Nanometers(protected val value: Double) extends LengthMeasure[Nanometers](1e-9)
