package org.fathens.math

import org.specs2._
import org.specs2.matcher._
import org.scalacheck._

object NumUnitSpec extends Specification with ScalaCheck {
  def is = s2"""
  Value conservation

    Degrees                                              $vc01
    Radians                                              $vc02
    Meters                                               $vc03
    Millimeters                                          $vc04
    Killometers                                          $vc05
    Nanometers                                           $vc06
    Inch                                                 $vc07
    Pixel                                                $vc08

  Basic arithmetic operations

    addtion                                              $bo01
    subtraction                                          $bo02
    multiplication                                       $bo03
    division                                             $bo04

  Inch

    from milli-meters                                    $ic01
    to milli-meters                                      $ic02
    to nano-meters                                       $ic03
    to meters                                            $ic04
    to killo-meters                                      $ic05

  Pixel

    from inch                                            $px01
    to inch                                              $px02
"""

  implicit class SignificanceDouble(a: Double) {
    def must_=~(b: Double)(implicit s: SignificantFigures) = a must beCloseTo(b within s)
  }
  implicit val significance = SignificantFigures(10)

  def vc01 = prop { (a: Double) => Degrees(a).value must_== a }
  def vc02 = prop { (a: Double) => Radians(a).value must_== a }
  def vc03 = prop { (a: Double) => Meters(a).value must_== a }
  def vc04 = prop { (a: Double) => Millimeters(a).value must_== a }
  def vc05 = prop { (a: Double) => Killometers(a).value must_== a }
  def vc06 = prop { (a: Double) => Nanometers(a).value must_== a }
  def vc07 = prop { (a: Double) => Inch(a).value must_== a }
  def vc08 = prop { (a: Double, dpi: Double) => Pixel(a, dpi).value must_== a }

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

  def ic01 = prop { (a: Double) =>
    (Millimeters(a): Inch).value must_=~ a / 25.4
  }
  def ic02 = prop { (a: Double) =>
    (Inch(a): Millimeters).value must_=~ a * 25.4
  }
  def ic03 = prop { (a: Double) =>
    (Inch(a): Nanometers).value must_=~ a * 25.4 * 1e+6
  }
  def ic04 = prop { (a: Double) =>
    (Inch(a): Meters).value must_=~ a * 25.4 * 1e-3
  }
  def ic05 = prop { (a: Double) =>
    (Inch(a): Killometers).value must_=~ a * 25.4 * 1e-6
  }

  def px01 = prop { (a: Double, b: Int) =>
    val dpi = math.abs(b) / 100.0
    Pixel(dpi)(Inch(a)).value must_=~ a * dpi
  }.set(minTestsOk = 10000)
  def px02 = prop { (a: Double, b: Int) =>
    val dpi = math.abs(b) / 100.0
    Pixel(a, dpi).toInch.value must_=~ a / dpi
  }.set(minTestsOk = 10000)
}
