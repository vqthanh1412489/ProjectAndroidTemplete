package com.google.common.math;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import edu.hws.jcm.data.ExpressionProgram;
import edu.jas.ps.UnivPowerSeriesRing;
import io.github.kexanie.library.R;
import java.math.RoundingMode;
import org.apache.commons.math4.analysis.integration.BaseAbstractUnivariateIntegrator;
import org.apache.commons.math4.dfp.Dfp;
import org.apache.commons.math4.distribution.PoissonDistribution;
import org.apache.commons.math4.random.EmpiricalDistribution;
import org.apache.commons.math4.random.ValueServer;
import org.apache.commons.math4.stat.descriptive.DescriptiveStatistics;
import org.matheclipse.core.interfaces.IExpr;

@GwtCompatible(emulated = true)
public final class IntMath {
    @VisibleForTesting
    static final int FLOOR_SQRT_MAX_INT = 46340;
    @VisibleForTesting
    static final int MAX_POWER_OF_SQRT2_UNSIGNED = -1257966797;
    @VisibleForTesting
    static int[] biggestBinomials;
    private static final int[] factorials;
    @VisibleForTesting
    static final int[] halfPowersOf10;
    @VisibleForTesting
    static final byte[] maxLog10ForLeadingZeros;
    @VisibleForTesting
    static final int[] powersOf10;

    static /* synthetic */ class 1 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode;

        static {
            $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    public static boolean isPowerOfTwo(int x) {
        int i = 1;
        int i2 = x > 0 ? 1 : 0;
        if (((x - 1) & x) != 0) {
            i = 0;
        }
        return i & i2;
    }

    @VisibleForTesting
    static int lessThanBranchFree(int x, int y) {
        return (((x - y) ^ -1) ^ -1) >>> 31;
    }

    public static int log2(int x, RoundingMode mode) {
        MathPreconditions.checkPositive(UnivPowerSeriesRing.DEFAULT_NAME, x);
        switch (1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case ValueServer.REPLAY_MODE /*1*/:
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
                break;
            case IExpr.DOUBLEID /*2*/:
            case ValueServer.EXPONENTIAL_MODE /*3*/:
                break;
            case IExpr.DOUBLECOMPLEXID /*4*/:
            case ValueServer.CONSTANT_MODE /*5*/:
                return 32 - Integer.numberOfLeadingZeros(x - 1);
            case R.styleable.Toolbar_contentInsetEnd /*6*/:
            case R.styleable.Toolbar_contentInsetLeft /*7*/:
            case IExpr.INTEGERID /*8*/:
                int leadingZeros = Integer.numberOfLeadingZeros(x);
                return lessThanBranchFree(MAX_POWER_OF_SQRT2_UNSIGNED >>> leadingZeros, x) + (31 - leadingZeros);
            default:
                throw new AssertionError();
        }
        return 31 - Integer.numberOfLeadingZeros(x);
    }

    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int log10(int x, RoundingMode mode) {
        MathPreconditions.checkPositive(UnivPowerSeriesRing.DEFAULT_NAME, x);
        int logFloor = log10Floor(x);
        int floorPow = powersOf10[logFloor];
        switch (1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case ValueServer.REPLAY_MODE /*1*/:
                boolean z;
                if (x == floorPow) {
                    z = true;
                } else {
                    z = false;
                }
                MathPreconditions.checkRoundingUnnecessary(z);
                return logFloor;
            case IExpr.DOUBLEID /*2*/:
            case ValueServer.EXPONENTIAL_MODE /*3*/:
                return logFloor;
            case IExpr.DOUBLECOMPLEXID /*4*/:
            case ValueServer.CONSTANT_MODE /*5*/:
                return logFloor + lessThanBranchFree(floorPow, x);
            case R.styleable.Toolbar_contentInsetEnd /*6*/:
            case R.styleable.Toolbar_contentInsetLeft /*7*/:
            case IExpr.INTEGERID /*8*/:
                return logFloor + lessThanBranchFree(halfPowersOf10[logFloor], x);
            default:
                throw new AssertionError();
        }
    }

    private static int log10Floor(int x) {
        int y = maxLog10ForLeadingZeros[Integer.numberOfLeadingZeros(x)];
        return y - lessThanBranchFree(x, powersOf10[y]);
    }

    static {
        maxLog10ForLeadingZeros = new byte[]{(byte) 9, (byte) 9, (byte) 9, (byte) 8, (byte) 8, (byte) 8, (byte) 7, (byte) 7, (byte) 7, (byte) 6, (byte) 6, (byte) 6, (byte) 6, (byte) 5, (byte) 5, (byte) 5, (byte) 4, (byte) 4, (byte) 4, (byte) 3, (byte) 3, (byte) 3, (byte) 3, (byte) 2, (byte) 2, (byte) 2, (byte) 1, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        powersOf10 = new int[]{1, 10, 100, EmpiricalDistribution.DEFAULT_BIN_COUNT, Dfp.RADIX, 100000, 1000000, PoissonDistribution.DEFAULT_MAX_ITERATIONS, 100000000, 1000000000};
        halfPowersOf10 = new int[]{3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT};
        factorials = new int[]{1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600};
        biggestBinomials = new int[]{BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT, BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT, AccessibilityNodeInfoCompat.ACTION_CUT, 2345, 477, 193, com.getkeepsafe.taptargetview.R.styleable.AppCompatTheme_ratingBarStyleSmall, 75, 58, 49, 43, 39, 37, 35, 34, 34, 33};
    }

    @GwtIncompatible("failing tests")
    public static int pow(int b, int k) {
        int i = 0;
        MathPreconditions.checkNonNegative("exponent", k);
        switch (b) {
            case ExpressionProgram.MINUS /*-2*/:
                if (k < 32) {
                    return (k & 1) == 0 ? 1 << k : -(1 << k);
                } else {
                    return 0;
                }
            case DescriptiveStatistics.INFINITE_WINDOW /*-1*/:
                if ((k & 1) != 0) {
                    return -1;
                }
                return 1;
            case ValueServer.DIGEST_MODE /*0*/:
                if (k == 0) {
                    return 1;
                }
                return 0;
            case ValueServer.REPLAY_MODE /*1*/:
                return 1;
            case IExpr.DOUBLEID /*2*/:
                if (k < 32) {
                    i = 1 << k;
                }
                return i;
            default:
                int accum = 1;
                while (true) {
                    switch (k) {
                        case ValueServer.DIGEST_MODE /*0*/:
                            return accum;
                        case ValueServer.REPLAY_MODE /*1*/:
                            return b * accum;
                        default:
                            if ((k & 1) == 0) {
                                i = 1;
                            } else {
                                i = b;
                            }
                            accum *= i;
                            b *= b;
                            k >>= 1;
                    }
                }
        }
    }

    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int sqrt(int x, RoundingMode mode) {
        MathPreconditions.checkNonNegative(UnivPowerSeriesRing.DEFAULT_NAME, x);
        int sqrtFloor = sqrtFloor(x);
        switch (1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case ValueServer.REPLAY_MODE /*1*/:
                boolean z;
                if (sqrtFloor * sqrtFloor == x) {
                    z = true;
                } else {
                    z = false;
                }
                MathPreconditions.checkRoundingUnnecessary(z);
                return sqrtFloor;
            case IExpr.DOUBLEID /*2*/:
            case ValueServer.EXPONENTIAL_MODE /*3*/:
                return sqrtFloor;
            case IExpr.DOUBLECOMPLEXID /*4*/:
            case ValueServer.CONSTANT_MODE /*5*/:
                return sqrtFloor + lessThanBranchFree(sqrtFloor * sqrtFloor, x);
            case R.styleable.Toolbar_contentInsetEnd /*6*/:
            case R.styleable.Toolbar_contentInsetLeft /*7*/:
            case IExpr.INTEGERID /*8*/:
                return sqrtFloor + lessThanBranchFree((sqrtFloor * sqrtFloor) + sqrtFloor, x);
            default:
                throw new AssertionError();
        }
    }

    private static int sqrtFloor(int x) {
        return (int) Math.sqrt((double) x);
    }

    public static int divide(int p, int q, RoundingMode mode) {
        boolean z = true;
        Preconditions.checkNotNull(mode);
        if (q == 0) {
            throw new ArithmeticException("/ by zero");
        }
        int div = p / q;
        int rem = p - (q * div);
        if (rem == 0) {
            return div;
        }
        boolean increment;
        int signum = ((p ^ q) >> 31) | 1;
        switch (1.$SwitchMap$java$math$RoundingMode[mode.ordinal()]) {
            case ValueServer.REPLAY_MODE /*1*/:
                if (rem != 0) {
                    z = false;
                }
                MathPreconditions.checkRoundingUnnecessary(z);
                break;
            case IExpr.DOUBLEID /*2*/:
                break;
            case ValueServer.EXPONENTIAL_MODE /*3*/:
                if (signum < 0) {
                    increment = true;
                } else {
                    increment = false;
                }
                break;
            case IExpr.DOUBLECOMPLEXID /*4*/:
                increment = true;
                break;
            case ValueServer.CONSTANT_MODE /*5*/:
                if (signum > 0) {
                    increment = true;
                } else {
                    increment = false;
                }
                break;
            case R.styleable.Toolbar_contentInsetEnd /*6*/:
            case R.styleable.Toolbar_contentInsetLeft /*7*/:
            case IExpr.INTEGERID /*8*/:
                int absRem = Math.abs(rem);
                int cmpRemToHalfDivisor = absRem - (Math.abs(q) - absRem);
                if (cmpRemToHalfDivisor != 0) {
                    if (cmpRemToHalfDivisor > 0) {
                        increment = true;
                    } else {
                        increment = false;
                    }
                    break;
                }
                if (mode != RoundingMode.HALF_UP) {
                    if ((((div & 1) != 0 ? 1 : 0) & (mode == RoundingMode.HALF_EVEN ? 1 : 0)) == 0) {
                        increment = false;
                        break;
                    }
                }
                increment = true;
            default:
                throw new AssertionError();
        }
        increment = false;
        return increment ? div + signum : div;
    }

    public static int mod(int x, int m) {
        if (m <= 0) {
            throw new ArithmeticException("Modulus " + m + " must be > 0");
        }
        int result = x % m;
        return result >= 0 ? result : result + m;
    }

    public static int gcd(int a, int b) {
        MathPreconditions.checkNonNegative("a", a);
        MathPreconditions.checkNonNegative("b", b);
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        int aTwos = Integer.numberOfTrailingZeros(a);
        a >>= aTwos;
        int bTwos = Integer.numberOfTrailingZeros(b);
        b >>= bTwos;
        while (a != b) {
            int delta = a - b;
            int minDeltaOrZero = delta & (delta >> 31);
            a = (delta - minDeltaOrZero) - minDeltaOrZero;
            b += minDeltaOrZero;
            a >>= Integer.numberOfTrailingZeros(a);
        }
        return a << Math.min(aTwos, bTwos);
    }

    public static int checkedAdd(int a, int b) {
        long result = ((long) a) + ((long) b);
        MathPreconditions.checkNoOverflow(result == ((long) ((int) result)));
        return (int) result;
    }

    public static int checkedSubtract(int a, int b) {
        long result = ((long) a) - ((long) b);
        MathPreconditions.checkNoOverflow(result == ((long) ((int) result)));
        return (int) result;
    }

    public static int checkedMultiply(int a, int b) {
        long result = ((long) a) * ((long) b);
        MathPreconditions.checkNoOverflow(result == ((long) ((int) result)));
        return (int) result;
    }

    public static int checkedPow(int b, int k) {
        boolean z = false;
        MathPreconditions.checkNonNegative("exponent", k);
        switch (b) {
            case ExpressionProgram.MINUS /*-2*/:
                if (k < 32) {
                    z = true;
                }
                MathPreconditions.checkNoOverflow(z);
                return (k & 1) == 0 ? 1 << k : -1 << k;
            case DescriptiveStatistics.INFINITE_WINDOW /*-1*/:
                if ((k & 1) != 0) {
                    return -1;
                }
                return 1;
            case ValueServer.DIGEST_MODE /*0*/:
                if (k == 0) {
                    return 1;
                }
                return 0;
            case ValueServer.REPLAY_MODE /*1*/:
                return 1;
            case IExpr.DOUBLEID /*2*/:
                if (k < 31) {
                    z = true;
                }
                MathPreconditions.checkNoOverflow(z);
                return 1 << k;
            default:
                int accum = 1;
                while (true) {
                    switch (k) {
                        case ValueServer.DIGEST_MODE /*0*/:
                            return accum;
                        case ValueServer.REPLAY_MODE /*1*/:
                            return checkedMultiply(accum, b);
                        default:
                            if ((k & 1) != 0) {
                                accum = checkedMultiply(accum, b);
                            }
                            k >>= 1;
                            if (k > 0) {
                                int i;
                                int i2;
                                if (-46340 <= b) {
                                    i = 1;
                                } else {
                                    i = 0;
                                }
                                if (b <= FLOOR_SQRT_MAX_INT) {
                                    i2 = 1;
                                } else {
                                    i2 = 0;
                                }
                                MathPreconditions.checkNoOverflow(i2 & i);
                                b *= b;
                            }
                    }
                }
        }
    }

    public static int factorial(int n) {
        MathPreconditions.checkNonNegative("n", n);
        return n < factorials.length ? factorials[n] : BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT;
    }

    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int binomial(int n, int k) {
        MathPreconditions.checkNonNegative("n", n);
        MathPreconditions.checkNonNegative("k", k);
        Preconditions.checkArgument(k <= n, "k (%s) > n (%s)", Integer.valueOf(k), Integer.valueOf(n));
        if (k > (n >> 1)) {
            k = n - k;
        }
        if (k >= biggestBinomials.length || n > biggestBinomials[k]) {
            return BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT;
        }
        switch (k) {
            case ValueServer.DIGEST_MODE /*0*/:
                return 1;
            case ValueServer.REPLAY_MODE /*1*/:
                return n;
            default:
                long result = 1;
                for (int i = 0; i < k; i++) {
                    result = (result * ((long) (n - i))) / ((long) (i + 1));
                }
                return (int) result;
        }
    }

    public static int mean(int x, int y) {
        return (x & y) + ((x ^ y) >> 1);
    }

    private IntMath() {
    }
}
