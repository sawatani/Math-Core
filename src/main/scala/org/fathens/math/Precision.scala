package org.fathens.math

object Precision {
  /**
   * Significant Figures
   */
  case class SignificantFigures(digit: Int) {
    def apply(v: Double) = BigDecimal(v, new java.math.MathContext(digit))
    def compare(a: Double, b: Double) = this(a) == this(b)
  }

  def compare(a: Double, b: Double)(implicit psf$: SignificantFigures) = {
    (a.isNaN && b.isNaN) || (a.isInfinity && b.isInfinity) || psf$.compare(a, b)
  }
}
