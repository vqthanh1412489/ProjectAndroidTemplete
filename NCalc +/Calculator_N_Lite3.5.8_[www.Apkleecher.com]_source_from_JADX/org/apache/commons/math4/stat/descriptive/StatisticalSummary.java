package org.apache.commons.math4.stat.descriptive;

public interface StatisticalSummary {
    double getMax();

    double getMean();

    double getMin();

    long getN();

    double getStandardDeviation();

    double getSum();

    double getVariance();
}
