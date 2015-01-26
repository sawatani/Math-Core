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

  Other operations

    mod                                                  $op01
    pow                                                  $op02
    sqrt                                                 $op03
    cbrt                                                 $op04
    abs                                                  $op05
    plus sign                                            $op06
    minus sign                                           $op07
    compare                                              $op08

  Convertible

    equals                                               $co01
    convert                                              $co02

  Inch

    from milli-meters                                    $ic01
    to milli-meters                                      $ic02
    to nano-meters                                       $ic03
    to meters                                            $ic04
    to killo-meters                                      $ic05

  Pixel

    from inch                                            $px01
    to inch                                              $px02

  Radians

    from degrees                                         $rd01
    to degrees                                           $rd02

  Normalize angulars

    always inside 0 - 2π                                 $fa01
    0-2π    => it self                                   $fa02
    over 2π => decrease 2π            　                 $fa03
    under 0 => increase 2π              　               $fa04
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

  def op01 = prop { (a: Double, b: Double) =>
    Meters(a) % b must_== Meters(a % b)
  }
  def op02 = prop { (a: Double, b: Double) =>
    Meters(a) ^ b must_== Meters(scala.math.pow(a, b))
  }
  def op03 = prop { (a: Double) =>
    Meters(a).sqrt must_== Meters(scala.math.sqrt(a))
  }
  def op04 = prop { (a: Double) =>
    Meters(a).cbrt must_== Meters(scala.math.cbrt(a))
  }
  def op05 = prop { (a: Double) =>
    Meters(a).abs must_== Meters(scala.math.abs(a))
  }
  def op06 = prop { (a: Double) =>
    +Meters(a) must_== Meters(+a)
  }
  def op07 = prop { (a: Double) =>
    -Meters(a) must_== Meters(-a)
  }
  def op08 = prop { (a: Double, b: Double) =>
    Meters(a) > Meters(b) must_== a > b
  }

  def co01 = prop { (a: Double) =>
    Radians(a * scala.math.Pi / 180) must_== Degrees(a)
  }
  def co02 = prop { (a: Double) =>
    (Radians(a): Degrees).value must_=~ a * 180 / scala.math.Pi
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

  def rd01 = prop { (a: Double) =>
    (Degrees(a): Radians).value must_=~ a * scala.math.Pi / 180
  }
  def rd02 = prop { (a: Double) =>
    (Radians(a): Degrees).value must_=~ a * 180 / scala.math.Pi
  }
  def fa01 = prop { (d: Double) =>
    Radians(d).normalize.value must beBetween(0.0, 2 * Pi.value).excludingEnd
  }
  def fa02 = Prop.forAll(Gen.choose(0.0, 2 * math.Pi - 0.00001)) { (d: Double) =>
    Radians(d).normalize.value must_=~ d
  }
  def fa03 = Prop.forAll(Gen.choose(0.0, 2 * math.Pi - 0.00001)) { (d: Double) =>
    (Pi2 + Radians(d)).normalize.value must_=~ d
  }
  def fa04 = Prop.forAll(Gen.choose(0.0, 2 * math.Pi - 0.00001)) { (d: Double) =>
    (-Pi2 + Radians(d)).normalize.value must_=~ d
  }
}
