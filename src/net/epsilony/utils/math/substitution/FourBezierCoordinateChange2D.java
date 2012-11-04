/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.math.substitution;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.Matrix;
import static java.lang.Math.abs;

/**
 * 四条Bezier曲线围成的区域的坐标变换（不太成熟）
 * @author epsilonyuan@gmail.com
 */
public class FourBezierCoordinateChange2D implements BivariateMapper{
    //双三次Bezier曲线系数阵
    final public static double[][] M = {{-1, 3, -3, 1},
        {3, -6, 3, 0},
        {-3, 3, 0, 0},
        {1, 0, 0, 0}
    };
    //Area domain;
    final public static DenseMatrix MAT_M = new DenseMatrix(M);
    private DenseMatrix xCtrls = new DenseMatrix(4, 4);
    private DenseMatrix yCtrls = new DenseMatrix(4, 4);
    final private static int[][] cIs = {{0, 0}, {0, 1}, {0, 2}, {0, 3}, {1, 3}, {2, 3}, {3, 3}, {3, 2}, {3, 1}, {3, 0}, {2, 0}, {1, 0}};
    private int cI = 0;
    private Matrix parMatX,  parMatY;
    private Matrix tMat44 = new DenseMatrix(4, 4);
    private Matrix tMat442 = new DenseMatrix(4, 4);

    public void setup(double[][] domainCtrls) {
        clear();
        for (cI = 0; cI < cIs.length; cI++) {
            xCtrls.set(cIs[cI][0], cIs[cI][1], domainCtrls[cI][0]);
            yCtrls.set(cIs[cI][0], cIs[cI][1], domainCtrls[cI][1]);
        }
        generateMid4Ctrls();
        generateParMats();
    }

    public void setup(Shape domain) throws Exception {
        //this.domain=domain;
        PathIterator path = domain.getPathIterator(new AffineTransform());
        cI = 0;
        while (!path.isDone()) {
            switch (path.currentSegment(shapeData)) {
                case PathIterator.SEG_MOVETO:
                    if (cI != 0) {
                        throw new Exception("the domain must be singlur!");
                    }

                    xCtrls.set(0, 0, shapeData[0]);
                    yCtrls.set(0, 0, shapeData[1]);
                    tX = shapeData[0];
                    tY = shapeData[1];
                    cI++;
                    break;
                case PathIterator.SEG_LINETO:
                    if (cI >= cIs.length) {
                        throw new Exception("Seems that the domain has more than 4 borders");
                    }
                    for (int i = 1; i <= 3; i++) {
                        xCtrls.set(cIs[cI][0], cIs[cI][1], tX + (shapeData[0] - tX) / 3 * i);
                        yCtrls.set(cIs[cI][0], cIs[cI][1], tY + (shapeData[1] - tY) / 3 * i);
                        cI++;
                        if (cI >= cIs.length) {
                            if (shapeData[0] == xCtrls.get(0, 0) && shapeData[1] == yCtrls.get(0, 0)) {
                                break;
                            } else {
                                throw new Exception("Seems unclosed shape or the borders of shape is more than 4");
                            }
                        }
                    }
                    tX = shapeData[0];
                    tY = shapeData[1];
                    break;
                case PathIterator.SEG_QUADTO:
                    if (cI >= cIs.length) {
                        throw new Exception("Seems that the domain has more than 4 borders");
                    }
                    xCtrls.set(cIs[cI][0], cIs[cI][1], shapeData[0]);
                    yCtrls.set(cIs[cI][0], cIs[cI][1], shapeData[1]);
                    cI++;
                    xCtrls.set(cIs[cI][0], cIs[cI][1], shapeData[0]);
                    yCtrls.set(cIs[cI][0], cIs[cI][1], shapeData[1]);
                    cI++;
                    if (cI >= cIs.length) {
                        if (shapeData[2] == xCtrls.get(0, 0) && shapeData[3] == yCtrls.get(0, 0)) {
                            break;
                        } else {
                            throw new Exception("Seems Unclose shape or the borders of shape is more than 4");
                        }
                    }
                    xCtrls.set(cIs[cI][0], cIs[cI][1], shapeData[2]);
                    yCtrls.set(cIs[cI][0], cIs[cI][1], shapeData[3]);
                    cI++;
                    tX = shapeData[2];
                    tY = shapeData[3];
                    break;
                case PathIterator.SEG_CUBICTO:
                    if (cI >= cIs.length) {
                        throw new Exception("Seems that the domain has more than 4 borders");
                    }
                    for (int i = 0; i < 3; i++) {
                        if (cI >= cIs.length) {
                            if (i == 2 && shapeData[4] == xCtrls.get(0, 0) && shapeData[5] == yCtrls.get(0, 0)) {
                                break;
                            } else {
                                throw new Exception("Seems Unclose shape or the borders of domain is more than 4");
                            }
                        }
                        xCtrls.set(cIs[cI][0], cIs[cI][1], shapeData[i * 2]);
                        yCtrls.set(cIs[cI][0], cIs[cI][1], shapeData[i * 2 + 1]);
                        cI++;
                    }
                    tX = shapeData[4];
                    tY = shapeData[5];
                    break;
                case PathIterator.SEG_CLOSE:
                    if (cI >= cIs.length) {
                        break;
                    } else if (cI != cIs.length - 2) {
                        throw new Exception("Seems there is less than 4 borders of domain");
                    } else {
                        for (int i = 1; i <= 2; i++) {
                            xCtrls.set(cIs[cI][0], cIs[cI][1], tX + (xCtrls.get(0, 0) - tX) / 3 * i);
                            yCtrls.set(cIs[cI][0], cIs[cI][1], tY + (yCtrls.get(0, 0) - tY) / 3 * i);
                            cI++;
                        }
                    }
            }
            path.next();
        }
        generateMid4Ctrls();

        generateParMats();
    }

    /*enum BorderType {
    
    LINE, CUBIC_BEZIER, QUAD_BEZIER;
    }*/
    public void clear() {
        xCtrls.zero();
        yCtrls.zero();
        cI = 0;
    }
    private double[] shapeData = new double[6];
    private double tX,  tY;

    /**
     * 
     * @param domain 4 borders singular Area
     * @throws java.lang.Exception
     */
    public FourBezierCoordinateChange2D(Shape domain) throws Exception {
        setup(domain);
    }

    /**
     * 
     * @param domainCtrls {p00,p01,p02,p03,p31,p32,p33,p32,p31,p30,p20,p10}
     */
    public FourBezierCoordinateChange2D(double[][] domainCtrls) {
        setup(domainCtrls);
    }

    private void generateMid4Ctrls() {
        xCtrls.set(1, 1, xCtrls.get(1, 0) * 2 / 3 + xCtrls.get(1, 3) / 3);
        xCtrls.set(1, 2, xCtrls.get(1, 0) / 3 + xCtrls.get(1, 3) * 2 / 3);
        xCtrls.set(2, 1, xCtrls.get(2, 0) * 2 / 3 + xCtrls.get(2, 3) / 3);
        xCtrls.set(2, 2, xCtrls.get(2, 0) / 3 + xCtrls.get(2, 3) * 2 / 3);

        yCtrls.set(1, 1, yCtrls.get(0, 1) * 2 / 3 + yCtrls.get(3, 1) / 3);
        yCtrls.set(2, 1, yCtrls.get(0, 1) / 3 + yCtrls.get(3, 1) * 2 / 3);
        yCtrls.set(1, 2, yCtrls.get(0, 2) * 2 / 3 + yCtrls.get(3, 2) / 3);
        yCtrls.set(2, 2, yCtrls.get(0, 2) / 3 + yCtrls.get(3, 2) * 2 / 3);
    }

    public double[][] getXCtrls() {
        return Matrices.getArray(xCtrls);
    }

    public double[][] getYCtrls() {
        return Matrices.getArray(yCtrls);
    }
    /**
     * get Coordinate Change Result
     * @param u [-1,1]
     * @param v [-1,1]
     * @return {x,y,jaccobi value}
     */
    Matrix vecU = new DenseMatrix(4, 1);
    Matrix vecV = new DenseMatrix(4, 1);
    Matrix vecdU = new DenseMatrix(4, 1);
    Matrix vecdV = new DenseMatrix(4, 1);
    Matrix tMat14 = new DenseMatrix(1, 4);
    Matrix tMat11 = new DenseMatrix(1, 1);
    private double tu,  tv,  tdxu,  tdxv,  tdyu,  tdyv;
    private Matrix tMat;

    @Override
    public double[] getResults(double u, double v, double[] results) {
        u=(u+1)/2;
        v=(v+1)/2;
        tu = 1;
        tv = 1;
        vecU.zero();
        vecV.zero();
        
        for (int i = 0; i < 4; i++) {
            vecU.set(3 - i, 0, tu);
            vecV.set(3 - i, 0, tv);
            tu *= u;
            tv *= v;
        }
        vecdU.set(3, 0, 0);
        vecdV.set(3, 0, 0);
        for (int i = 0; i < 3; i++) {
            vecdU.set(2 - i, 0, vecU.get(3 - i, 0) * (i + 1));
            vecdV.set(2 - i, 0, vecV.get(3 - i, 0) * (i + 1));
        }

        tMat = vecU.transAmult(parMatX, tMat14);
        tMat = tMat.mult(vecV, tMat11);
        results[0] = tMat.get(0, 0);

        tMat = vecU.transAmult(parMatY, tMat14);
        tMat = tMat.mult(vecV, tMat11);
        results[1] = tMat.get(0, 0);



        tMat = vecdU.transAmult(parMatX, tMat14);
        tMat = tMat.mult(vecV, tMat11);
        tdxu = tMat.get(0, 0);

        tMat = vecdU.transAmult(parMatY, tMat14);
        tMat = tMat.mult(vecV, tMat11);
        tdyu = tMat.get(0, 0);

        tMat = vecU.transAmult(parMatX, tMat14);
        tMat = tMat.mult(vecdV, tMat11);
        tdxv = tMat.get(0, 0);

        tMat = vecU.transAmult(parMatY, tMat14);
        tMat = tMat.mult(vecdV, tMat11);
        tdyv = tMat.get(0, 0);
        results[2] =abs(tdxu * tdyv - tdxv * tdyu)/4;
//        System.out.println("results:" + Arrays.toString(results));
//        System.out.println("tdxu:" + tdxu);
//        System.out.println("tdxv:" + tdxv);
//        System.out.println("tdyu:" + tdyu);
//        System.out.println("tdyv:" + tdyv);
//        System.out.println("");


        return results;
    }

    private void generateParMats() {
        parMatX = MAT_M.mult(xCtrls, tMat44);
        parMatY = MAT_M.mult(yCtrls, tMat442);

        parMatX = parMatX.transBmult(MAT_M, new DenseMatrix(4, 4));
        parMatY = parMatY.transBmult(MAT_M, new DenseMatrix(4, 4));

//        System.out.println("ParMatX");
//        System.out.println(ArrayUtils.toString(Matrices.getArray(parMatX)));
//        System.out.println("ParMatY");
//        System.out.println(ArrayUtils.toString(Matrices.getArray(parMatY)));
    }
}
