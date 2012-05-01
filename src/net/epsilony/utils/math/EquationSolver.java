/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.math;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;

/**
 *
 * @author epsilonyuan@gmail.com
 */
public interface EquationSolver {
    DenseVector solve(Matrix matrix,DenseVector Vector);
}
