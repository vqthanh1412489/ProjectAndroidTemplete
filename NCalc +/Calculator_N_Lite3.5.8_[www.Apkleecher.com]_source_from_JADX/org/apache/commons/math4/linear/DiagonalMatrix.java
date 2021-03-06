package org.apache.commons.math4.linear;

import java.io.Serializable;
import java.lang.reflect.Array;
import org.apache.commons.math4.exception.DimensionMismatchException;
import org.apache.commons.math4.exception.NotStrictlyPositiveException;
import org.apache.commons.math4.exception.NullArgumentException;
import org.apache.commons.math4.exception.NumberIsTooLargeException;
import org.apache.commons.math4.exception.OutOfRangeException;
import org.apache.commons.math4.util.FastMath;
import org.apache.commons.math4.util.MathUtils;
import org.apache.commons.math4.util.Precision;

public class DiagonalMatrix extends AbstractRealMatrix implements Serializable {
    private static final long serialVersionUID = 20121229;
    private final double[] data;

    public DiagonalMatrix(int dimension) throws NotStrictlyPositiveException {
        super(dimension, dimension);
        this.data = new double[dimension];
    }

    public DiagonalMatrix(double[] d) {
        this(d, true);
    }

    public DiagonalMatrix(double[] d, boolean copyArray) throws NullArgumentException {
        double[] dArr;
        MathUtils.checkNotNull(d);
        if (copyArray) {
            dArr = (double[]) d.clone();
        } else {
            dArr = d;
        }
        this.data = dArr;
    }

    public RealMatrix createMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException, DimensionMismatchException {
        if (rowDimension == columnDimension) {
            return new DiagonalMatrix(rowDimension);
        }
        throw new DimensionMismatchException(rowDimension, columnDimension);
    }

    public RealMatrix copy() {
        return new DiagonalMatrix(this.data);
    }

    public DiagonalMatrix add(DiagonalMatrix m) throws MatrixDimensionMismatchException {
        MatrixUtils.checkAdditionCompatible(this, m);
        int dim = getRowDimension();
        double[] outData = new double[dim];
        for (int i = 0; i < dim; i++) {
            outData[i] = this.data[i] + m.data[i];
        }
        return new DiagonalMatrix(outData, false);
    }

    public DiagonalMatrix subtract(DiagonalMatrix m) throws MatrixDimensionMismatchException {
        MatrixUtils.checkSubtractionCompatible(this, m);
        int dim = getRowDimension();
        double[] outData = new double[dim];
        for (int i = 0; i < dim; i++) {
            outData[i] = this.data[i] - m.data[i];
        }
        return new DiagonalMatrix(outData, false);
    }

    public DiagonalMatrix multiply(DiagonalMatrix m) throws DimensionMismatchException {
        MatrixUtils.checkMultiplicationCompatible(this, m);
        int dim = getRowDimension();
        double[] outData = new double[dim];
        for (int i = 0; i < dim; i++) {
            outData[i] = this.data[i] * m.data[i];
        }
        return new DiagonalMatrix(outData, false);
    }

    public RealMatrix multiply(RealMatrix m) throws DimensionMismatchException {
        if (m instanceof DiagonalMatrix) {
            return multiply((DiagonalMatrix) m);
        }
        MatrixUtils.checkMultiplicationCompatible(this, m);
        int nRows = m.getRowDimension();
        int nCols = m.getColumnDimension();
        double[][] product = (double[][]) Array.newInstance(Double.TYPE, new int[]{nRows, nCols});
        for (int r = 0; r < nRows; r++) {
            for (int c = 0; c < nCols; c++) {
                product[r][c] = this.data[r] * m.getEntry(r, c);
            }
        }
        return new Array2DRowRealMatrix(product, false);
    }

    public double[][] getData() {
        int dim = getRowDimension();
        double[][] out = (double[][]) Array.newInstance(Double.TYPE, new int[]{dim, dim});
        for (int i = 0; i < dim; i++) {
            out[i][i] = this.data[i];
        }
        return out;
    }

    public double[] getDataRef() {
        return this.data;
    }

    public double getEntry(int row, int column) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        return row == column ? this.data[row] : 0.0d;
    }

    public void setEntry(int row, int column, double value) throws OutOfRangeException, NumberIsTooLargeException {
        if (row == column) {
            MatrixUtils.checkRowIndex(this, row);
            this.data[row] = value;
            return;
        }
        ensureZero(value);
    }

    public void addToEntry(int row, int column, double increment) throws OutOfRangeException, NumberIsTooLargeException {
        if (row == column) {
            MatrixUtils.checkRowIndex(this, row);
            double[] dArr = this.data;
            dArr[row] = dArr[row] + increment;
            return;
        }
        ensureZero(increment);
    }

    public void multiplyEntry(int row, int column, double factor) throws OutOfRangeException {
        if (row == column) {
            MatrixUtils.checkRowIndex(this, row);
            double[] dArr = this.data;
            dArr[row] = dArr[row] * factor;
        }
    }

    public int getRowDimension() {
        return this.data.length;
    }

    public int getColumnDimension() {
        return this.data.length;
    }

    public double[] operate(double[] v) throws DimensionMismatchException {
        return multiply(new DiagonalMatrix(v, false)).getDataRef();
    }

    public double[] preMultiply(double[] v) throws DimensionMismatchException {
        return operate(v);
    }

    public RealVector preMultiply(RealVector v) throws DimensionMismatchException {
        double[] vectorData;
        if (v instanceof ArrayRealVector) {
            vectorData = ((ArrayRealVector) v).getDataRef();
        } else {
            vectorData = v.toArray();
        }
        return MatrixUtils.createRealVector(preMultiply(vectorData));
    }

    private void ensureZero(double value) throws NumberIsTooLargeException {
        if (!Precision.equals(0.0d, value, 1)) {
            throw new NumberIsTooLargeException(Double.valueOf(FastMath.abs(value)), Integer.valueOf(0), true);
        }
    }

    public DiagonalMatrix inverse() throws SingularMatrixException {
        return inverse(0.0d);
    }

    public DiagonalMatrix inverse(double threshold) throws SingularMatrixException {
        if (isSingular(threshold)) {
            throw new SingularMatrixException();
        }
        double[] result = new double[this.data.length];
        for (int i = 0; i < this.data.length; i++) {
            result[i] = 1.0d / this.data[i];
        }
        return new DiagonalMatrix(result, false);
    }

    public boolean isSingular(double threshold) {
        for (double equals : this.data) {
            if (Precision.equals(equals, 0.0d, threshold)) {
                return true;
            }
        }
        return false;
    }
}
