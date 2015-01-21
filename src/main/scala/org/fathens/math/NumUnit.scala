package org.fathens.math

import scala.reflect.runtime.{ universe => ru }

object NumUnit {
  /**
   * Create another instance of given value
   */
  def apply[A: ru.TypeTag](v: Double): A = ru.runtimeMirror(getClass.getClassLoader)
    .reflectClass(
      ru.typeOf[A].typeSymbol.asClass
    ).reflectConstructor(
        ru.typeOf[A].decl(ru.termNames.CONSTRUCTOR).asMethod
      )(v).asInstanceOf[A]
}
sealed abstract class NumUnit[A: ru.TypeTag] {
  /**
   * Numeric value
   */
  val value: Double
  
  def +(b: NumUnit[A]) = NumUnit[A](this.value + b.value)
  def -(b: NumUnit[A]) = NumUnit[A](this.value - b.value)
  def *(b: Double) = NumUnit[A](this.value * b)
  def /(b: Double) = NumUnit[A](this.value / b)
}

case class Degrees(value: Double) extends NumUnit[Degrees] {
  def asRadians = Radians(value.toRadians)
}
case class Radians(value: Double) extends NumUnit[Radians] {
  def asDegrees = Degrees(value.toDegrees)
}
