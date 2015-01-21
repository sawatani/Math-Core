package org.fathens.math

import org.specs2._

object NumUnitSpec extends Specification with ScalaCheck {
  def is = s2"""
  Value conservation 　　　　　                           
    Degrees                                             $vc01
    Radians                                             $vc02

  Basic arithmetic operations

    addtion                                             $bo01
    subtraction                                         $bo02
    multiplication                                      $bo03
    division                                            $bo04
"""

  def vc01 = prop { (a: Double) => Degrees(a).value must_== a }
  def vc02 = prop { (a: Double) => Radians(a).value must_== a }

  def bo01 = prop { (a: Double, b: Double) =>
    Degrees(a) + Degrees(b) must_== Degrees(a + b)
  }
  def bo02 = prop { (a: Double, b: Double) =>
    Degrees(a) - Degrees(b) must_== Degrees(a - b)
  }
  def bo03 = prop { (a: Double, b: Double) =>
    Degrees(a) * b must_== Degrees(a * b)
  }
  def bo04 = prop { (a: Double, b: Double) =>
    Degrees(a) / b must_== Degrees(a / b)
  }
}
