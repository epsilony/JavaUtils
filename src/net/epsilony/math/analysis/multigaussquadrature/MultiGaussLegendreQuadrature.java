/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.analysis.multigaussquadrature;

import net.epsilony.math.analysis.MultivariateRealFunction;
import static java.lang.Math.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.epsilony.math.analysis.GaussLegendreQuadrature;
import org.apache.commons.math.ArgumentOutsideDomainException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MathException;
import net.epsilony.math.util.IntervalGenerator;
import net.epsilony.math.analysis.GaussQuadrature;

/**
 *
 * @author li
 */
public class MultiGaussLegendreQuadrature implements GaussQuadrature, MultiGaussQuadrature {

    double[] xDirections;
    double[] yDirections;
    int[] xnPoints;
    int[] ynPoints;

    public MultiGaussLegendreQuadrature(int[] xnPoints, int[] ynPoints, double[] xDirection, double[] yDirection) {
        this.xDirections = xDirection;
        this.yDirections = yDirection;
        this.xnPoints = xnPoints;
        this.ynPoints = ynPoints;
    }

    @Override
    public double multiGaussQuadrature(MultivariateRealFunction fun, IntervalGenerator intervalGenerator) throws FunctionEvaluationException, ConvergenceException, MathException {
        double value = 0;
        /** the number j and i determains the screen grids number
         * 
         */
        double[] yPoints = null;
        double[] yCoefs = null;
        double[] xPoints = null;
        double[] xCoefs = null;
        double[][] lowerIntervals;
        double[][] upperIntervals;
        int[][] sign = new int[2][2];
        for (int j = 0; j < yDirections.length - 1; j++) {

            yPoints = GetGaussQuadraturePoints(ynPoints[j]);
            yCoefs = GetGaussQuadratureCoefs(ynPoints[j]);

            lowerIntervals = intervalGenerator.getIntervals(0, yDirections[j]);
            upperIntervals = intervalGenerator.getIntervals(0, yDirections[j + 1]);
            
            for (int l = 0; l < yPoints.length; l++) {
                yPoints[l] = (yDirections[j] + yDirections[j + 1]) / 2 + (yDirections[j + 1] - yDirections[j]) / 2 * yPoints[l];
            }
            
            for (int i = 0; i < xDirections.length - 1; i++) {
                xPoints = GetGaussQuadraturePoints(xnPoints[i]);
                xCoefs = GetGaussQuadratureCoefs(xnPoints[i]);

                /**decide whether grids are totally in domain or not
                 * 
                 */               
                sign[0][0] = choosePoints(upperIntervals, xDirections[i]);
                sign[1][0] = choosePoints(lowerIntervals, xDirections[i]);
                sign[0][1] = choosePoints(upperIntervals, xDirections[i + 1]);
                sign[1][1] = choosePoints(lowerIntervals, xDirections[i + 1]);

                if ((sign[0][0] == 0) && (sign[1][1] == 0) && (sign[0][1] == 0) && (sign[1][1] == 0)) {
                    //do nothing because the current grid is out of domain
                    continue;
                } /**out of the domain
                 * 
                 */
                else if ((sign[0][0] == 0) && (sign[0][1] == 0) && (sign[1][0] == 0) && (sign[1][1] == 1)) {
                    if (lowerIntervals[1][0] == xDirections[i + 1]) {
                        value += 0;
                    } else {
                        double[][] rightinterval = intervalGenerator.getIntervals(0, xDirections[i + 1]);
                        double[] Xlocation = new double[xPoints.length];
                        double[] Ylocation = new double[yPoints.length];
                        for (int p = 0; p < xPoints.length; p++) {
                            Xlocation[p] = (lowerIntervals[1][0] + xDirections[i + 1]) / 2 + (xDirections[i + 1] - lowerIntervals[1][0]) / 2 * xPoints[p];
                        }
                        for (int p = 0; p < yPoints.length; p++) {
                            Ylocation[p] = (yDirections[j] + (rightinterval[0][0] - yDirections[j]) / (xDirections[i + 1] - lowerIntervals[1][0]) * (Xlocation[p] - lowerIntervals[1][0]) + yDirections[j]) / 2 + (rightinterval[0][0] - yDirections[j]) / (xDirections[i + 1] - lowerIntervals[1][0]) * (Xlocation[p] - lowerIntervals[1][0]) / 2 * yPoints[p];
                        }
                        for (int p = 0; p < xPoints.length; p++) {
                            for (int q = 0; q < yPoints.length; q++) {
                                double[] Ploaction = {Xlocation[p], Ylocation[q]};
                                value += abs(xCoefs[p] * yCoefs[q] * ((rightinterval[0][0] - yDirections[j]) * (lowerIntervals[1][0] + xDirections[i + 1]) / 8 + (rightinterval[0][0] - yDirections[j]) * (xDirections[i + 1] - lowerIntervals[1][0]) / 8 * Xlocation[p] - (rightinterval[0][0] - yDirections[j]) * xDirections[i + 1] / 4) * fun.value(Ploaction));
                                System.out.println(+value);
                            }
                        }
                    }
                } else if ((sign[0][0] == 0) && (sign[0][1] == 1) && (sign[1][0] == 0) && (sign[1][1] == 0)) {
                    if (upperIntervals[1][0] == xDirections[i + 1]) {
                        value += 0;
                    } else {
                        double[][] rightinterval = intervalGenerator.getIntervals(0, xDirections[i + 1]);
                        double[] Xlocation = new double[xPoints.length];
                        double[] Ylocation = new double[yPoints.length];
                        for (int p = 0; p < xPoints.length; p++) {
                            Xlocation[p] = (upperIntervals[1][0] + xDirections[i + 1]) / 2 + (xDirections[i + 1] - upperIntervals[1][0]) / 2 * xPoints[p];
                        }
                        for (int p = 0; p < yPoints.length; p++) {
                            Ylocation[p] = (yDirections[j + 1] + (rightinterval[1][0] - yDirections[j + 1]) / (xDirections[i + 1] - upperIntervals[1][0]) * (Xlocation[p] - upperIntervals[1][0]) + yDirections[j + 1]) / 2 + (rightinterval[1][0] - yDirections[j + 1]) / (xDirections[i + 1] - upperIntervals[1][0]) * (Xlocation[p] - upperIntervals[1][0]) / 2 * yPoints[p];
                        }
                        for (int p = 0; p < xPoints.length; p++) {
                            for (int q = 0; q < yPoints.length; q++) {
                                double[] Ploaction = {Xlocation[p], Ylocation[q]};
                                value += abs(xCoefs[p] * yCoefs[q] * ((rightinterval[1][0] - yDirections[j + 1]) * (upperIntervals[1][0] + xDirections[i + 1]) / 8 + (rightinterval[1][0] - yDirections[j + 1]) * (xDirections[i + 1] - upperIntervals[1][0]) / 8 * Xlocation[p] - (rightinterval[1][0] - yDirections[j + 1]) * xDirections[i + 1] / 4) * fun.value(Ploaction));
                                System.out.println(+value);
                            }
                        }
                    }
                } else if ((sign[0][0] == 1) && (sign[0][1] == 0) && (sign[1][0] == 0) && (sign[1][1] == 0)) {

                    if (upperIntervals[0][0] == xDirections[i]) {
                        value += 0;
                    } else {
                        double[][] leftinterval = intervalGenerator.getIntervals(0, xDirections[i]);
                        double[] Xlocation = new double[xPoints.length];
                        double[] Ylocation = new double[yPoints.length];
                        for (int p = 0; p < xPoints.length; p++) {
                            Xlocation[p] = (upperIntervals[0][0] + xDirections[i]) / 2 + (xDirections[i] - upperIntervals[0][0]) / 2 * xPoints[p];
                        }
                        for (int p = 0; p < yPoints.length; p++) {
                            Ylocation[p] = (yDirections[j + 1] + (leftinterval[1][0] - yDirections[j + 1]) / (xDirections[i] - upperIntervals[0][0]) * (Xlocation[p] - upperIntervals[0][0]) + yDirections[j + 1]) / 2 + (leftinterval[1][0] - yDirections[j + 1]) / (xDirections[i] - upperIntervals[0][0]) * (Xlocation[p] - upperIntervals[0][0]) / 2 * yPoints[p];
                        }
                        for (int p = 0; p < xPoints.length; p++) {
                            for (int q = 0; q < yPoints.length; q++) {
                                double[] Ploaction = {Xlocation[p], Ylocation[q]};
                                value += abs(xCoefs[p] * yCoefs[q] * ((leftinterval[1][0] - yDirections[j + 1]) * (upperIntervals[0][0] + xDirections[i]) / 8 + (leftinterval[1][0] - yDirections[j + 1]) * (xDirections[i] - upperIntervals[0][0]) / 8 * Xlocation[p] - (leftinterval[1][0] - yDirections[j + 1]) * xDirections[i] / 4) * fun.value(Ploaction));
                                System.out.println(+value);
                            }
                        }
                    }
                } else if ((sign[0][0] == 1) && (sign[0][1] == 0) && (sign[1][0] == 0) && (sign[1][1] == 0)) {
                    if (lowerIntervals[0][0] == xDirections[i]) {
                        value += 0;
                    } else {
                        double[][] leftinterval = intervalGenerator.getIntervals(0, xDirections[i]);
                        double[] Xlocation = new double[xPoints.length];
                        double[] Ylocation = new double[yPoints.length];
                        for (int p = 0; p < xPoints.length; p++) {
                            Xlocation[p] = (lowerIntervals[0][0] + xDirections[i]) / 2 + (xDirections[i] - lowerIntervals[0][0]) / 2 * xPoints[p];
                        }
                        for (int p = 0; p < yPoints.length; p++) {
                            Ylocation[p] = (yDirections[j] + (leftinterval[0][0] - yDirections[j]) / (xDirections[i] - lowerIntervals[0][0]) * (Xlocation[p] - lowerIntervals[0][0]) + yDirections[j]) / 2 + (leftinterval[0][0] - yDirections[j]) / (xDirections[i] - lowerIntervals[0][0]) * (Xlocation[p] - lowerIntervals[0][0]) / 2 * yPoints[p];
                        }
                        for (int p = 0; p < xPoints.length; p++) {
                            for (int q = 0; q < yPoints.length; q++) {
                                double[] Ploaction = {Xlocation[p], Ylocation[q]};
                                value += abs(xCoefs[p] * yCoefs[q] * ((leftinterval[0][0] - yDirections[j]) * (lowerIntervals[0][0] + xDirections[i]) / 8 + (leftinterval[0][0] - yDirections[j]) * (xDirections[i] - lowerIntervals[0][0]) / 8 * Xlocation[p] - (leftinterval[0][0] - yDirections[j]) * xDirections[i] / 4) * fun.value(Ploaction));
                                System.out.println(+value);
                            }
                        }
                    }
                }/**triangle grid
                 * 
                 */
                else if ((sign[0][0] == 0) && (sign[0][1] == 0) && (sign[1][0] == 1) && (sign[1][1] == 1)) {
                    if ((lowerIntervals[0][0] == xDirections[i + 1]) && (lowerIntervals[1][0] == xDirections[i])) {
                        double[][] topinterval = intervalGenerator.getIntervals(0, (xDirections[i] + xDirections[i + 1]) / 2);
                        double[] Xlocation = new double[xPoints.length];
                        double[] Ylocation = new double[yPoints.length];
                        for (int p = 0; p < xPoints.length; p++) {
                            Xlocation[p] = (xDirections[i] + xDirections[i + 1]) / 2 + (xDirections[i + 1] - xDirections[i]) / 2 * xPoints[p];
                        }
                        for (int p = 0; p < yPoints.length; p++) {
                            Ylocation[p] = (yDirections[j] + topinterval[0][0]) / 2 + (topinterval[0][0] - yDirections[j]) / 2 * yPoints[p];
                        }
                        for (int p = 0; p < xPoints.length; p++) {
                            for (int q = 0; q < yPoints.length; q++) {
                                double[] Ploaction = {Xlocation[p], Ylocation[q]};
                                value += abs(xCoefs[p] * yCoefs[q] * (xDirections[i + 1] - xDirections[i]) * (topinterval[0][0] - yDirections[j]) / 4 * fun.value(Ploaction));
                                System.out.println(+value);
                            }
                        }
                    } else {
                        double[][] leftinterval = intervalGenerator.getIntervals(0, xDirections[i]);
                        double[][] rightinterval = intervalGenerator.getIntervals(0, xDirections[i + 1]);
                        double[] Xlocation = new double[xPoints.length];
                        double[] Ylocation = new double[yPoints.length];
                        for (int p = 0; p < xPoints.length; p++) {
                            Xlocation[p] = (xDirections[i] + xDirections[i + 1]) / 2 + (xDirections[i + 1] - xDirections[i]) / 2 * xPoints[p];
                        }
                        for (int p = 0; p < yPoints.length; p++) {
                            Ylocation[p] = (leftinterval[0][0] + (rightinterval[0][0] - leftinterval[0][0]) / (xDirections[i + 1] - xDirections[i]) * (Xlocation[p] - xDirections[i]) + leftinterval[0][0]) / 2 + (rightinterval[0][0] - leftinterval[0][0]) / (xDirections[i + 1] - xDirections[i]) * (Xlocation[p] - xDirections[i]) / 2 * yPoints[p];
                        }
                        for (int p = 0; p < xPoints.length; p++) {
                            for (int q = 0; q < yPoints.length; q++) {
                                double[] Ploaction = {Xlocation[p], Ylocation[q]};
                                value += abs(xCoefs[p] * yCoefs[q] * ((rightinterval[0][0] - leftinterval[0][0]) * (xDirections[i] + xDirections[i + 1]) / 8 + (rightinterval[0][0] - leftinterval[0][0]) * (xDirections[i + 1] - xDirections[i]) / 8 * Xlocation[p] - (rightinterval[0][0] - leftinterval[0][0]) * xDirections[i + 1] / 4) * fun.value( Ploaction));
                                System.out.println(+value);
                            }

                        }
                    }
                } else if ((sign[0][0] == 1) && (sign[0][1] == 0) && (sign[1][0] == 1) && (sign[1][1] == 0)) {
                    if ((lowerIntervals[0][0] == yDirections[j]) && (upperIntervals[0][0] == yDirections[j + 1])) {
                        double[][] rightinterval = intervalGenerator.getIntervals(0, (yDirections[j] + yDirections[j + 1]) / 2);
                        double[] Xlocation = new double[xPoints.length];
                        double[] Ylocation = new double[yPoints.length];
                        for (int p = 0; p < xPoints.length; p++) {
                            Xlocation[p] = (xDirections[i] + rightinterval[0][0]) / 2 + (rightinterval[0][0] - xDirections[i]) / 2 * xPoints[p];
                        }
                        for (int p = 0; p < yPoints.length; p++) {
                            Ylocation[p] = (yDirections[j] + yDirections[j + 1]) / 2 + (yDirections[j + 1] - yDirections[j]) / 2 * yPoints[p];
                        }
                        for (int p = 0; p < xPoints.length; p++) {
                            for (int q = 0; q < yPoints.length; q++) {
                                double[] Ploaction = {Xlocation[p], Ylocation[q]};
                                value += abs(xCoefs[p] * yCoefs[q] * (yDirections[j + 1] - yDirections[j]) * (rightinterval[0][0] - xDirections[i]) / 4 * fun.value(Ploaction));
                                System.out.println(+value);
                            }
                        }
                    } else {
                        double[] Xlocation = new double[xPoints.length];
                        double[] Ylocation = new double[yPoints.length];
                        for (int p = 0; p < yPoints.length; p++) {
                            Ylocation[p] = (yDirections[j + 1] + yDirections[j]) / 2 + (yDirections[j + 1] - yDirections[j]) / 2 * yPoints[p];
                        }
                        for (int p = 0; p < xPoints.length; p++) {
                            Xlocation[p] = ((lowerIntervals[0][0] - upperIntervals[0][0]) / (yDirections[j] - yDirections[j + 1]) * (Ylocation[p] - yDirections[j + 1]) + upperIntervals[0][0] + lowerIntervals[0][0]) / 2 + (lowerIntervals[0][0] - upperIntervals[0][0] - (lowerIntervals[0][0] - upperIntervals[0][0]) / (yDirections[j] - yDirections[j + 1]) * (Ylocation[p] - yDirections[j + 1])) * Xlocation[p] / 2;
                            for (int q = 0; q < yPoints.length; q++) {
                                double[] Ploaction = {Xlocation[p], Ylocation[q]};
                                value += abs(xCoefs[p] * yCoefs[q] * ((upperIntervals[0][0] - lowerIntervals[0][0]) * (yDirections[j] + yDirections[j + 1]) / 8 + (upperIntervals[0][0] - lowerIntervals[0][0]) * (yDirections[j] - yDirections[j + 1]) / 8 * Ylocation[p] - (upperIntervals[0][0] - lowerIntervals[0][0]) * yDirections[j] / 4) * fun.value(Ploaction));
                                System.out.println(+value);
                            }

                        }

                    }
                } else if ((sign[0][0] == 1) && (sign[0][1] == 1) && (sign[1][0] == 0) && (sign[1][1] == 0)) {
                    if ((lowerIntervals[0][0] == yDirections[j]) && (upperIntervals[0][0] == yDirections[j + 1])) {
                        double[][] bottominterval = intervalGenerator.getIntervals(0, (xDirections[i] + xDirections[i + 1]) / 2);
                        double[] Xlocation = new double[xPoints.length];
                        double[] Ylocation = new double[yPoints.length];
                        for (int p = 0; p < xPoints.length; p++) {
                            Xlocation[p] = (xDirections[i] + xDirections[i + 1]) / 2 + (xDirections[i + 1] - xDirections[i]) / 2 * xPoints[p];
                        }
                        for (int p = 0; p < yPoints.length; p++) {
                            Ylocation[p] = (yDirections[j + 1] + bottominterval[1][0]) / 2 + (yDirections[j + 1] - bottominterval[1][0]) / 2 * yPoints[p];
                        }
                        for (int p = 0; p < xPoints.length; p++) {
                            for (int q = 0; q < yPoints.length; q++) {
                                double[] Ploaction = {Xlocation[p], Ylocation[q]};
                                value += abs(xCoefs[p] * yCoefs[q] * (xDirections[i + 1] - xDirections[i]) * (yDirections[j + 1] - bottominterval[1][0]) / 4 * fun.value(Ploaction));
                                System.out.println(+value);
                            }
                        }
                    } else {
                        double[][] leftinterval = intervalGenerator.getIntervals(0, xDirections[i]);
                        double[][] rightinterval = intervalGenerator.getIntervals(0, xDirections[i + 1]);
                        double[] Xlocation = new double[xPoints.length];
                        double[] Ylocation = new double[yPoints.length];
                        for (int p = 0; p < xPoints.length; p++) {
                            Xlocation[p] = (xDirections[i] + xDirections[i + 1]) / 2 + (xDirections[i + 1] - xDirections[i]) / 2 * xPoints[p];
                        }
                        for (int p = 0; p < yPoints.length; p++) {
                            Ylocation[p] = (leftinterval[1][0] + (rightinterval[1][0] - leftinterval[1][0]) / (xDirections[i + 1] - xDirections[i]) * (xPoints[p] - xDirections[i]) + leftinterval[1][0]) / 2 + (rightinterval[1][0] - leftinterval[1][0]) / (xDirections[i + 1] - xDirections[i]) * (Xlocation[p] - leftinterval[1][0]) / 2 * yPoints[p];
                        }
                        for (int p = 0; p < xPoints.length; p++) {
                            for (int q = 0; q < yPoints.length; q++) {
                                double[] Ploaction = {Xlocation[p], Ylocation[q]};
                                value += abs(xCoefs[p] * yCoefs[q] * ((rightinterval[1][0] - leftinterval[1][0]) * (xDirections[i] + xDirections[i + 1]) / 8 + (rightinterval[1][0] - leftinterval[1][0]) * (xDirections[i + 1] - xDirections[i]) / 8 * Xlocation[p] - (rightinterval[1][0] - leftinterval[1][0]) * xDirections[i + 1] / 4) * fun.value(Ploaction));
                                System.out.println(+value);
                            }

                        }

                    }
                } else if ((sign[0][0] == 0) && (sign[0][1] == 1) && (sign[1][0] == 0) && (sign[1][1] == 1)) {
                    if ((lowerIntervals[1][0] == yDirections[j]) && (upperIntervals[1][0] == yDirections[j + 1])) {
                        double[][] leftinterval = intervalGenerator.getIntervals(0, (yDirections[j] + yDirections[j + 1]) / 2);
                        double[] Xlocation = new double[xPoints.length];
                        double[] Ylocation = new double[yPoints.length];
                        for (int p = 0; p < xPoints.length; p++) {
                            Xlocation[p] = (xDirections[i + 1] + leftinterval[0][0]) / 2 + (xDirections[i + 1] - leftinterval[0][0]) / 2 * xPoints[p];
                        }
                        for (int p = 0; p < yPoints.length; p++) {
                            Ylocation[p] = (yDirections[j] + yDirections[j + 1]) / 2 + (yDirections[j + 1] - yDirections[j]) / 2 * yPoints[p];
                        }
                        for (int p = 0; p < xPoints.length; p++) {
                            for (int q = 0; q < yPoints.length; q++) {
                                double[] Ploaction = {Xlocation[p], Ylocation[q]};
                                value += abs(xCoefs[p] * yCoefs[q] * (yDirections[j + 1] - yDirections[j]) * (xDirections[i + 1] - leftinterval[0][0]) / 4 * fun.value(Ploaction));
                                System.out.println(+value);

                            }
                        }
                    } else {

                        double[] Xlocation = new double[xPoints.length];
                        double[] Ylocation = new double[yPoints.length];
                        for (int p = 0; p < xPoints.length; p++) {
                            Ylocation[p] = (yDirections[i] + yDirections[i + 1]) / 2 + (yDirections[i + 1] - yDirections[i]) / 2 * xPoints[p];
                        }
                        for (int p = 0; p < yPoints.length; p++) {
                            Xlocation[p] = ((lowerIntervals[1][0] - upperIntervals[1][0]) / (yDirections[j] - yDirections[j + 1]) * (Ylocation[p] - yDirections[j + 1]) + upperIntervals[1][0] + lowerIntervals[1][0]) / 2 + (lowerIntervals[1][0] - upperIntervals[1][0] - (lowerIntervals[1][0] - upperIntervals[1][0]) / (yDirections[j] - yDirections[j + 1]) * (Ylocation[p] - yDirections[j + 1])) * Xlocation[p] / 2;
                            for (int q = 0; q < yPoints.length; q++) {
                                double[] Ploaction = {Xlocation[p], Ylocation[q]};
                                value += abs(xCoefs[p] * yCoefs[q] * ((upperIntervals[1][0] - lowerIntervals[1][0]) * (yDirections[j] + yDirections[j + 1]) / 8 + (upperIntervals[1][0] - lowerIntervals[1][0]) * (yDirections[j] - yDirections[j + 1]) / 8 * Ylocation[p] - (upperIntervals[1][0] - lowerIntervals[1][0]) * yDirections[j] / 4) * fun.value(Ploaction));
                                System.out.println(+value);
                            }

                        }

                    }
                } /**quadrangle region
                 * 
                 */
                else if ((sign[0][0] == 1) && (sign[0][1] == 1) && (sign[1][0] == 1) && (sign[1][1] == 0)) {
                    double[][] rightinterval = intervalGenerator.getIntervals(0, xDirections[i + 1]);
                    double[] Xlocation = new double[xPoints.length];
                    double[] Ylocation = new double[yPoints.length];
                    for (int p = 0; p < xPoints.length; p++) {
                        Xlocation[p] = (xDirections[i] + lowerIntervals[0][0]) / 2 + (lowerIntervals[0][0] - xDirections[i]) / 2 * xPoints[p];
                    }
                    for (int p = 0; p < yPoints.length; p++) {
                        Ylocation[p] = (rightinterval[1][0] + yDirections[j]) / 2 + (rightinterval[1][0] - yDirections[j]) / 2 * yPoints[p];
                    }
                    for (int p = 0; p < xPoints.length; p++) {
                        for (int q = 0; q < yPoints.length; q++) {
                            double[] Ploaction = {Xlocation[p], Ylocation[q]};
                            value += abs(xCoefs[p] * yCoefs[q] * (lowerIntervals[0][0] - xDirections[i]) * (rightinterval[1][0] - yDirections[j]) / 4 * fun.value(Ploaction));
                            System.out.println(+value);
                        }
                    }
                    for (int p = 0; p < xPoints.length; p++) {
                        Xlocation[p] = (xDirections[i + 1] + xDirections[i]) / 2 + (xDirections[i + 1] - xDirections[i]) / 2 * xPoints[p];
                    }
                    for (int p = 0; p < yPoints.length; p++) {
                        Ylocation[p] = (rightinterval[1][0] + yDirections[j + 1]) / 2 + (yDirections[j + 1] - rightinterval[1][0]) / 2 * yPoints[p];
                    }
                    for (int p = 0; p < xPoints.length; p++) {
                        for (int q = 0; q < yPoints.length; q++) {
                            double[] Ploaction = {Xlocation[p], Ylocation[q]};
                            value += abs(xCoefs[p] * yCoefs[q] * (xDirections[i + 1] - xDirections[i]) * (yDirections[j + 1] - rightinterval[1][0]) / 4 * fun.value( Ploaction));
                            System.out.println(+value);
                        }
                    }
                    for (int p = 0; p < xPoints.length; p++) {
                        Xlocation[p] = (lowerIntervals[0][0] + xDirections[i + 1]) / 2 + (xDirections[i + 1] - lowerIntervals[0][0]) / 2 * xPoints[p];
                    }
                    for (int p = 0; p < yPoints.length; p++) {
                        Ylocation[p] = (yDirections[j] + (rightinterval[1][0] - yDirections[j]) / (xDirections[i + 1] - lowerIntervals[0][0]) * (xPoints[p] - lowerIntervals[0][0]) + yDirections[j]) / 2 + (rightinterval[1][0] - yDirections[j]) / (xDirections[i + 1] - lowerIntervals[0][0]) * (Xlocation[p] - lowerIntervals[0][0]) / 2 * yPoints[p];
                    }
                    for (int p = 0; p < xPoints.length; p++) {
                        for (int q = 0; q < yPoints.length; q++) {
                            double[] Ploaction = {Xlocation[p], Ylocation[q]};
                            value += abs(xCoefs[p] * yCoefs[q] * ((rightinterval[1][0] - yDirections[j]) * (lowerIntervals[0][0] + xDirections[i + 1]) / 8 + (rightinterval[1][0] - yDirections[j]) * (xDirections[i + 1] - lowerIntervals[0][0]) / 8 * Xlocation[p] - (rightinterval[1][0] - yDirections[j]) * xDirections[i + 1] / 4) * fun.value( Ploaction));
                            System.out.println(+value);
                        }
                    }
                } else if ((sign[0][0] == 1) && (sign[0][1] == 1) && (sign[1][0] == 0) && (sign[1][1] == 1)) {
                    double[][] leftinterval = intervalGenerator.getIntervals(0, xDirections[i]);
                    double[] Xlocation = new double[xPoints.length];
                    double[] Ylocation = new double[yPoints.length];
                    for (int p = 0; p < xPoints.length; p++) {
                        Xlocation[p] = (xDirections[i + 1] + lowerIntervals[1][0]) / 2 + (xDirections[i + 1] - lowerIntervals[1][0]) / 2 * xPoints[p];
                    }
                    for (int p = 0; p < yPoints.length; p++) {
                        Ylocation[p] = (leftinterval[1][0] + yDirections[j]) / 2 + (leftinterval[1][0] - yDirections[j]) / 2 * yPoints[p];
                    }
                    for (int p = 0; p < xPoints.length; p++) {
                        for (int q = 0; q < yPoints.length; q++) {
                            double[] Ploaction = {Xlocation[p], Ylocation[q]};
                            value += abs(xCoefs[p] * yCoefs[q] * (xDirections[i + 1] - lowerIntervals[1][0]) * (leftinterval[1][0] - yDirections[j]) / 4 * fun.value(Ploaction));
                            System.out.println(+value);
                        }
                    }
                    for (int p = 0; p < xPoints.length; p++) {
                        Xlocation[p] = (xDirections[i + 1] + xDirections[i]) / 2 + (xDirections[i + 1] - xDirections[i]) / 2 * xPoints[p];
                    }
                    for (int p = 0; p < yPoints.length; p++) {
                        Ylocation[p] = (leftinterval[1][0] + yDirections[j + 1]) / 2 + (yDirections[j + 1] - leftinterval[1][0]) / 2 * yPoints[p];
                    }
                    for (int p = 0; p < xPoints.length; p++) {
                        for (int q = 0; q < yPoints.length; q++) {
                            double[] Ploaction = {Xlocation[p], Ylocation[q]};
                            value += abs(xCoefs[p] * yCoefs[q] * (xDirections[i + 1] - xDirections[i]) * (yDirections[j + 1] - leftinterval[1][0]) / 4 * fun.value( Ploaction));
                            System.out.println(+value);
                        }
                    }
                    for (int p = 0; p < xPoints.length; p++) {
                        Xlocation[p] = (lowerIntervals[1][0] + xDirections[i]) / 2 + (lowerIntervals[1][0] - xDirections[i]) / 2 * xPoints[p];
                    }
                    for (int p = 0; p < yPoints.length; p++) {
                        Ylocation[p] = (leftinterval[1][0] + (yDirections[j] - leftinterval[1][0]) / (lowerIntervals[1][0] - xDirections[i]) * (xPoints[p] - xDirections[i]) + leftinterval[1][0]) / 2 + (yDirections[j] - leftinterval[1][0]) / (lowerIntervals[1][0] - xDirections[i]) * (Xlocation[p] - xDirections[i]) / 2 * yPoints[p];
                    }

                    for (int p = 0; p < xPoints.length; p++) {
                        for (int q = 0; q < yPoints.length; q++) {
                            double[] Ploaction = {Xlocation[p], Ylocation[q]};
                            value += abs(xCoefs[p] * yCoefs[q] * ((yDirections[j] - leftinterval[1][0]) * (lowerIntervals[1][0] + xDirections[i]) / 8 + (yDirections[j] - leftinterval[1][0]) * (lowerIntervals[1][0] - xDirections[i]) / 8 * Xlocation[p] - (yDirections[j] - leftinterval[1][0]) * lowerIntervals[1][0] / 4) * fun.value(Ploaction));
                            System.out.println(+value);
                        }
                    }
                } else if ((sign[0][0] == 0) && (sign[0][1] == 1) && (sign[1][0] == 1) && (sign[1][1] == 1)) {
                    double[][] leftinterval = intervalGenerator.getIntervals(0, xDirections[i]);
                    double[] Xlocation = new double[xPoints.length];
                    double[] Ylocation = new double[yPoints.length];
                    for (int p = 0; p < xPoints.length; p++) {
                        Xlocation[p] = (xDirections[i + 1] + upperIntervals[0][0]) / 2 + (xDirections[i + 1] - upperIntervals[0][0]) / 2 * xPoints[p];
                    }
                    for (int p = 0; p < yPoints.length; p++) {
                        Ylocation[p] = (yDirections[j + 1] + leftinterval[0][0]) / 2 + (yDirections[j + 1] - leftinterval[0][0]) / 2 * yPoints[p];
                    }
                    for (int p = 0; p < xPoints.length; p++) {
                        for (int q = 0; q < yPoints.length; q++) {
                            double[] Ploaction = {Xlocation[p], Ylocation[q]};
                            value += abs(xCoefs[p] * yCoefs[q] * (xDirections[i + 1] - upperIntervals[0][0]) * (yDirections[j + 1] - leftinterval[0][0]) / 4 * fun.value(Ploaction));
                            System.out.println(+value);
                        }
                    }
                    for (int p = 0; p < xPoints.length; p++) {
                        Xlocation[p] = (xDirections[i + 1] + xDirections[i]) / 2 + (xDirections[i + 1] - xDirections[i]) / 2 * xPoints[p];
                    }
                    for (int p = 0; p < yPoints.length; p++) {
                        Ylocation[p] = (leftinterval[0][0] + yDirections[j]) / 2 + (leftinterval[0][0] - yDirections[j]) / 2 * yPoints[p];
                    }
                    for (int p = 0; p < xPoints.length; p++) {
                        for (int q = 0; q < yPoints.length; q++) {
                            double[] Ploaction = {Xlocation[p], Ylocation[q]};
                            value += abs(xCoefs[p] * yCoefs[q] * (xDirections[i + 1] - xDirections[i]) * (leftinterval[0][0] - yDirections[j]) / 4 * fun.value(Ploaction));
                            System.out.println(+value);
                        }
                    }
                    for (int p = 0; p < xPoints.length; p++) {
                        Xlocation[p] = (upperIntervals[0][0] + xDirections[i]) / 2 + (upperIntervals[0][0] - xDirections[i]) / 2 * xPoints[p];
                    }
                    for (int p = 0; p < yPoints.length; p++) {
                        Ylocation[p] = (leftinterval[0][0] + (yDirections[j + 1] - leftinterval[0][0]) / (upperIntervals[0][0] - xDirections[i]) * (xPoints[p] - xDirections[i]) + leftinterval[0][0]) / 2 + (yDirections[j + 1] - leftinterval[0][0]) / (upperIntervals[0][0] - xDirections[i]) * (Xlocation[p] - xDirections[i]) / 2 * yPoints[p];
                    }

                    for (int p = 0; p < xPoints.length; p++) {
                        for (int q = 0; q < yPoints.length; q++) {
                            double[] Ploaction = {Xlocation[p], Ylocation[q]};
                            value += abs(xCoefs[p] * yCoefs[q] * ((yDirections[j + 1] - leftinterval[0][0]) * (upperIntervals[0][0] + xDirections[i]) / 8 + (yDirections[j + 1] - leftinterval[0][0]) * (upperIntervals[0][0] - xDirections[i]) / 8 * Xlocation[p] - (yDirections[j + 1] - leftinterval[0][0]) * upperIntervals[0][0] / 4) * fun.value(Ploaction));
                            System.out.println(+value);
                        }
                    }
                } /**pentagon region
                 * 
                 */
                else if ((sign[0][0] == 1) && (sign[0][1] == 1) && (sign[1][0] == 1) && (sign[1][1] == 1)) {
                    double[] Xlocation = new double[xPoints.length];
                    double[] Ylocation = new double[yPoints.length];
                    for (int p = 0; p < xPoints.length; p++) {
                        Xlocation[p] = (xDirections[i] + xDirections[i + 1]) / 2 + (xDirections[i + 1] - xDirections[i]) / 2 * xPoints[p];
                    }
                    for (int p = 0; p < yPoints.length; p++) {
                        Ylocation[p] = (yDirections[j] + yDirections[j + 1]) / 2 + (yDirections[j + 1] - yDirections[j]) / 2 * yPoints[p];
                    }
                    for (int p = 0; p < xPoints.length; p++) {
                        for (int q = 0; q < yPoints.length; q++) {
                            double[] Ploaction = {Xlocation[p], Ylocation[q]};
                            value += abs(xCoefs[p] * yCoefs[q] * (xDirections[i + 1] - xDirections[i]) * (yDirections[j + 1] - yDirections[j]) / 4 * fun.value(Ploaction));
                            System.out.println(+value);
                        }
                    }
                }
            /**within domain
             * 
             */
            }
        }
        return value;
    }

   
    @Override
    public double[] GetGaussQuadraturePoints(int nPoints) throws ArgumentOutsideDomainException {

            return GaussLegendreQuadrature.getGaussLegendreQuadraturePoints(nPoints);

    }

 
    @Override
    public double[] GetGaussQuadratureCoefs(int nPoints) throws ArgumentOutsideDomainException  {
            return GaussLegendreQuadrature.getGaussLegendreQuadratureCoefficients(nPoints);
    }

    /**decide whether sample point is in the domain or not
     * 
     * @param intervalGenerator
     * @param PointsInGrids
     * @return
     */
    @Override
    public int choosePoints(double[][] Interval, double PointsInGrids) {
        int sign = 0;
        for (int i = 0; i < Interval[0].length; i++) {
            if (PointsInGrids >= Interval[1][i]) {
                if (PointsInGrids <= Interval[0][i]) {
                    sign = 1;
                }
            }
        }
        return sign;
    }

    @Override
    public void setGrids(double[] xs, double[] ys) {
        this.xDirections = xs;
        this.yDirections = ys;
    }

    @Override
    public double[][] getDomain(double[] lowerleft, double[] upperright) {
        double[][] domain = {
            {lowerleft[0], upperright[0]},
            {lowerleft[1], upperright[1]}
        };
        return domain;
    }
}
