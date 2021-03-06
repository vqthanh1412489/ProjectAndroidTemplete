package org.apache.commons.math4.complex;

import com.example.duy.calculator.geom2d.util.Angle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math4.FieldElement;
import org.apache.commons.math4.exception.NotPositiveException;
import org.apache.commons.math4.exception.NullArgumentException;
import org.apache.commons.math4.exception.util.LocalizedFormats;
import org.apache.commons.math4.util.FastMath;
import org.apache.commons.math4.util.MathUtils;
import org.apache.commons.math4.util.Precision;

public class Complex implements FieldElement<Complex>, Serializable {
    public static final Complex I;
    public static final Complex INF;
    public static final Complex NaN;
    public static final Complex ONE;
    public static final Complex ZERO;
    private static final long serialVersionUID = -6195664516687396620L;
    private final double imaginary;
    private final transient boolean isInfinite;
    private final transient boolean isNaN;
    private final double real;

    static {
        I = new Complex(0.0d, 1.0d);
        NaN = new Complex(Double.NaN, Double.NaN);
        INF = new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        ONE = new Complex(1.0d, 0.0d);
        ZERO = new Complex(0.0d, 0.0d);
    }

    public Complex(double real) {
        this(real, 0.0d);
    }

    public Complex(double real, double imaginary) {
        boolean z;
        boolean z2 = true;
        this.real = real;
        this.imaginary = imaginary;
        if (Double.isNaN(real) || Double.isNaN(imaginary)) {
            z = true;
        } else {
            z = false;
        }
        this.isNaN = z;
        if (this.isNaN || !(Double.isInfinite(real) || Double.isInfinite(imaginary))) {
            z2 = false;
        }
        this.isInfinite = z2;
    }

    public double abs() {
        if (this.isNaN) {
            return Double.NaN;
        }
        if (isInfinite()) {
            return Double.POSITIVE_INFINITY;
        }
        double q;
        if (FastMath.abs(this.real) < FastMath.abs(this.imaginary)) {
            if (this.imaginary == 0.0d) {
                return FastMath.abs(this.real);
            }
            q = this.real / this.imaginary;
            return FastMath.abs(this.imaginary) * FastMath.sqrt((q * q) + 1.0d);
        } else if (this.real == 0.0d) {
            return FastMath.abs(this.imaginary);
        } else {
            q = this.imaginary / this.real;
            return FastMath.abs(this.real) * FastMath.sqrt((q * q) + 1.0d);
        }
    }

    public Complex add(Complex addend) throws NullArgumentException {
        MathUtils.checkNotNull(addend);
        if (this.isNaN || addend.isNaN) {
            return NaN;
        }
        return createComplex(this.real + addend.getReal(), this.imaginary + addend.getImaginary());
    }

    public Complex add(double addend) {
        if (this.isNaN || Double.isNaN(addend)) {
            return NaN;
        }
        return createComplex(this.real + addend, this.imaginary);
    }

    public Complex conjugate() {
        if (this.isNaN) {
            return NaN;
        }
        return createComplex(this.real, -this.imaginary);
    }

    public Complex divide(Complex divisor) throws NullArgumentException {
        MathUtils.checkNotNull(divisor);
        if (this.isNaN || divisor.isNaN) {
            return NaN;
        }
        double c = divisor.getReal();
        double d = divisor.getImaginary();
        if (c == 0.0d && d == 0.0d) {
            return NaN;
        }
        if (divisor.isInfinite() && !isInfinite()) {
            return ZERO;
        }
        if (FastMath.abs(c) < FastMath.abs(d)) {
            double q = c / d;
            double denominator = (c * q) + d;
            return createComplex(((this.real * q) + this.imaginary) / denominator, ((this.imaginary * q) - this.real) / denominator);
        }
        q = d / c;
        denominator = (d * q) + c;
        return createComplex(((this.imaginary * q) + this.real) / denominator, (this.imaginary - (this.real * q)) / denominator);
    }

    public Complex divide(double divisor) {
        if (this.isNaN || Double.isNaN(divisor)) {
            return NaN;
        }
        if (divisor == 0.0d) {
            return NaN;
        }
        if (Double.isInfinite(divisor)) {
            return !isInfinite() ? ZERO : NaN;
        } else {
            return createComplex(this.real / divisor, this.imaginary / divisor);
        }
    }

    public Complex reciprocal() {
        if (this.isNaN) {
            return NaN;
        }
        if (this.real == 0.0d && this.imaginary == 0.0d) {
            return INF;
        }
        if (this.isInfinite) {
            return ZERO;
        }
        if (FastMath.abs(this.real) < FastMath.abs(this.imaginary)) {
            double q = this.real / this.imaginary;
            double scale = 1.0d / ((this.real * q) + this.imaginary);
            return createComplex(scale * q, -scale);
        }
        q = this.imaginary / this.real;
        scale = 1.0d / ((this.imaginary * q) + this.real);
        return createComplex(scale, (-scale) * q);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Complex)) {
            return false;
        }
        Complex c = (Complex) other;
        if (c.isNaN) {
            return this.isNaN;
        }
        if (MathUtils.equals(this.real, c.real) && MathUtils.equals(this.imaginary, c.imaginary)) {
            return true;
        }
        return false;
    }

    public static boolean equals(Complex x, Complex y, int maxUlps) {
        return Precision.equals(x.real, y.real, maxUlps) && Precision.equals(x.imaginary, y.imaginary, maxUlps);
    }

    public static boolean equals(Complex x, Complex y) {
        return equals(x, y, 1);
    }

    public static boolean equals(Complex x, Complex y, double eps) {
        return Precision.equals(x.real, y.real, eps) && Precision.equals(x.imaginary, y.imaginary, eps);
    }

    public static boolean equalsWithRelativeTolerance(Complex x, Complex y, double eps) {
        return Precision.equalsWithRelativeTolerance(x.real, y.real, eps) && Precision.equalsWithRelativeTolerance(x.imaginary, y.imaginary, eps);
    }

    public int hashCode() {
        if (this.isNaN) {
            return 7;
        }
        return ((MathUtils.hash(this.imaginary) * 17) + MathUtils.hash(this.real)) * 37;
    }

    public double getImaginary() {
        return this.imaginary;
    }

    public double getReal() {
        return this.real;
    }

    public boolean isNaN() {
        return this.isNaN;
    }

    public boolean isInfinite() {
        return this.isInfinite;
    }

    public Complex multiply(Complex factor) throws NullArgumentException {
        MathUtils.checkNotNull(factor);
        if (this.isNaN || factor.isNaN) {
            return NaN;
        }
        if (Double.isInfinite(this.real) || Double.isInfinite(this.imaginary) || Double.isInfinite(factor.real) || Double.isInfinite(factor.imaginary)) {
            return INF;
        }
        return createComplex((this.real * factor.real) - (this.imaginary * factor.imaginary), (this.real * factor.imaginary) + (this.imaginary * factor.real));
    }

    public Complex multiply(int factor) {
        if (this.isNaN) {
            return NaN;
        }
        if (Double.isInfinite(this.real) || Double.isInfinite(this.imaginary)) {
            return INF;
        }
        return createComplex(this.real * ((double) factor), this.imaginary * ((double) factor));
    }

    public Complex multiply(double factor) {
        if (this.isNaN || Double.isNaN(factor)) {
            return NaN;
        }
        if (Double.isInfinite(this.real) || Double.isInfinite(this.imaginary) || Double.isInfinite(factor)) {
            return INF;
        }
        return createComplex(this.real * factor, this.imaginary * factor);
    }

    public Complex negate() {
        if (this.isNaN) {
            return NaN;
        }
        return createComplex(-this.real, -this.imaginary);
    }

    public Complex subtract(Complex subtrahend) throws NullArgumentException {
        MathUtils.checkNotNull(subtrahend);
        if (this.isNaN || subtrahend.isNaN) {
            return NaN;
        }
        return createComplex(this.real - subtrahend.getReal(), this.imaginary - subtrahend.getImaginary());
    }

    public Complex subtract(double subtrahend) {
        if (this.isNaN || Double.isNaN(subtrahend)) {
            return NaN;
        }
        return createComplex(this.real - subtrahend, this.imaginary);
    }

    public Complex acos() {
        if (this.isNaN) {
            return NaN;
        }
        return add(sqrt1z().multiply(I)).log().multiply(I.negate());
    }

    public Complex asin() {
        if (this.isNaN) {
            return NaN;
        }
        return sqrt1z().add(multiply(I)).log().multiply(I.negate());
    }

    public Complex atan() {
        if (this.isNaN) {
            return NaN;
        }
        return add(I).divide(I.subtract(this)).log().multiply(I.divide(createComplex(2.0d, 0.0d)));
    }

    public Complex cos() {
        if (this.isNaN) {
            return NaN;
        }
        return createComplex(FastMath.cos(this.real) * FastMath.cosh(this.imaginary), (-FastMath.sin(this.real)) * FastMath.sinh(this.imaginary));
    }

    public Complex cosh() {
        if (this.isNaN) {
            return NaN;
        }
        return createComplex(FastMath.cosh(this.real) * FastMath.cos(this.imaginary), FastMath.sinh(this.real) * FastMath.sin(this.imaginary));
    }

    public Complex exp() {
        if (this.isNaN) {
            return NaN;
        }
        double expReal = FastMath.exp(this.real);
        return createComplex(FastMath.cos(this.imaginary) * expReal, FastMath.sin(this.imaginary) * expReal);
    }

    public Complex log() {
        if (this.isNaN) {
            return NaN;
        }
        return createComplex(FastMath.log(abs()), FastMath.atan2(this.imaginary, this.real));
    }

    public Complex pow(Complex x) throws NullArgumentException {
        MathUtils.checkNotNull(x);
        return log().multiply(x).exp();
    }

    public Complex pow(double x) {
        return log().multiply(x).exp();
    }

    public Complex sin() {
        if (this.isNaN) {
            return NaN;
        }
        return createComplex(FastMath.sin(this.real) * FastMath.cosh(this.imaginary), FastMath.cos(this.real) * FastMath.sinh(this.imaginary));
    }

    public Complex sinh() {
        if (this.isNaN) {
            return NaN;
        }
        return createComplex(FastMath.sinh(this.real) * FastMath.cos(this.imaginary), FastMath.cosh(this.real) * FastMath.sin(this.imaginary));
    }

    public Complex sqrt() {
        if (this.isNaN) {
            return NaN;
        }
        if (this.real == 0.0d && this.imaginary == 0.0d) {
            return createComplex(0.0d, 0.0d);
        }
        double t = FastMath.sqrt((FastMath.abs(this.real) + abs()) / 2.0d);
        if (this.real >= 0.0d) {
            return createComplex(t, this.imaginary / (2.0d * t));
        }
        return createComplex(FastMath.abs(this.imaginary) / (2.0d * t), FastMath.copySign(1.0d, this.imaginary) * t);
    }

    public Complex sqrt1z() {
        return createComplex(1.0d, 0.0d).subtract(multiply(this)).sqrt();
    }

    public Complex tan() {
        if (this.isNaN || Double.isInfinite(this.real)) {
            return NaN;
        }
        if (this.imaginary > 20.0d) {
            return createComplex(0.0d, 1.0d);
        }
        if (this.imaginary < -20.0d) {
            return createComplex(0.0d, -1.0d);
        }
        double real2 = 2.0d * this.real;
        double imaginary2 = 2.0d * this.imaginary;
        double d = FastMath.cos(real2) + FastMath.cosh(imaginary2);
        return createComplex(FastMath.sin(real2) / d, FastMath.sinh(imaginary2) / d);
    }

    public Complex tanh() {
        if (this.isNaN || Double.isInfinite(this.imaginary)) {
            return NaN;
        }
        if (this.real > 20.0d) {
            return createComplex(1.0d, 0.0d);
        }
        if (this.real < -20.0d) {
            return createComplex(-1.0d, 0.0d);
        }
        double real2 = 2.0d * this.real;
        double imaginary2 = 2.0d * this.imaginary;
        double d = FastMath.cosh(real2) + FastMath.cos(imaginary2);
        return createComplex(FastMath.sinh(real2) / d, FastMath.sin(imaginary2) / d);
    }

    public double getArgument() {
        return FastMath.atan2(getImaginary(), getReal());
    }

    public List<Complex> nthRoot(int n) throws NotPositiveException {
        if (n <= 0) {
            throw new NotPositiveException(LocalizedFormats.CANNOT_COMPUTE_NTH_ROOT_FOR_NEGATIVE_N, Integer.valueOf(n));
        }
        List<Complex> result = new ArrayList();
        if (this.isNaN) {
            result.add(NaN);
        } else if (isInfinite()) {
            result.add(INF);
        } else {
            double nthRootOfAbs = FastMath.pow(abs(), 1.0d / ((double) n));
            double slice = Angle2D.M_2PI / ((double) n);
            double innerPart = getArgument() / ((double) n);
            for (int k = 0; k < n; k++) {
                result.add(createComplex(nthRootOfAbs * FastMath.cos(innerPart), nthRootOfAbs * FastMath.sin(innerPart)));
                innerPart += slice;
            }
        }
        return result;
    }

    protected Complex createComplex(double realPart, double imaginaryPart) {
        return new Complex(realPart, imaginaryPart);
    }

    public static Complex valueOf(double realPart, double imaginaryPart) {
        if (Double.isNaN(realPart) || Double.isNaN(imaginaryPart)) {
            return NaN;
        }
        return new Complex(realPart, imaginaryPart);
    }

    public static Complex valueOf(double realPart) {
        if (Double.isNaN(realPart)) {
            return NaN;
        }
        return new Complex(realPart);
    }

    protected final Object readResolve() {
        return createComplex(this.real, this.imaginary);
    }

    public ComplexField getField() {
        return ComplexField.getInstance();
    }

    public String toString() {
        return "(" + this.real + ", " + this.imaginary + ")";
    }
}
