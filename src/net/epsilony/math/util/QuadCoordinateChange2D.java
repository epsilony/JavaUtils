/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.math.util;
import net.epsilony.math.analysis.DifferentiableUnivariateVectorFunction;
import net.epsilony.math.analysis.UnivariateVectorFunction;
import org.apache.commons.math.FunctionEvaluationException;
import static java.lang.Math.abs;
/**
 * @deprecated 用CoonsCoordinateChange2D，该类的设计目前不是很成熟
 * [-1,1]X[-1,1]的正方形区域，映射到四个定义域为[0,1]参数线直线l1,l2,l3,l4所围成的四边
 * 可用于四个直线围成的四边形的Gauss积分。则u,v为一对边上的曲线参数-0.5,
 * @author epsilon
 */
public class QuadCoordinateChange2D implements BivariateMapper{

    private double x1,  y1,  x2,  y2,  x3,  y3,  x4,  y4;
    double[][] pts;
    private double x,  y;
    private double t1,  t2,  t3,  t4,  t5,  vx31,  vx42,  vy31,  vy42;
    private double tdnom,  tnum;
    DifferentiableUnivariateVectorFunction[] funs;
    UnivariateVectorFunction[] derivatedFuns;

    public void setup(DifferentiableUnivariateVectorFunction[] funs){
        this.funs = funs;
        for(int i=0;i<4;i++){
            derivatedFuns[i]=funs[i].vectorDerivative();
        }
    }
    
    public QuadCoordinateChange2D(DifferentiableUnivariateVectorFunction[] funs) {
        setup(funs);
    }

    private void generateTempletVars() {
        tdnom = x3 * y4 - x3 * y2 - x1 * y4 + x1 * y2 - x4 * y3 + x2 * y3 + y1 * x4 - y1 * x2;
        tnum = x1 * y4 - x1 * y2 - x2 * y4 - y1 * x4 + y1 * x2 + y2 * x4;
        t1 = tdnom * tdnom;
        t2 = -x2 * y4 + y2 * x4 + x3 * y4 - x3 * y2 - x4 * y3 + x2 * y3;
        t3 = x1 * y4 - x1 * y2 - x2 * y4 - y1 * x4 + y1 * x2 + y2 * x4;
        t4 = -x1 * y4 + x3 * y4 - x4 * y3 - x3 * y1 + x1 * y3 + y1 * x4;
        t5 = -x2 * y3 + y1 * x2 + x1 * y3 - x3 * y1 - x1 * y2 + x3 * y2;
        vx31 = x3 - x1;
        vx42 = x4 - x2;
        vy31 = y3 - y1;
        vy42 = y4 - y2;
    }

    private void generateCrossPt() {
        x = x1 - vx31 * tnum / tdnom;
        y = y1 - vy31 * tnum / tdnom;
    }
    double pXx1, pXx2, pXx3, pXx4, pYx1, pYx2, pYx3, pYx4, pXy1, pXy2, pXy3, pXy4, pYy1, pYy2, pYy3, pYy4;

    private void generatePartitialDiffs() {
        pXx1 = -vx42 * vy31 * t2 / t1;
        pXy1 = vx31 * vx42 * t2 / t1;
        pXx3 = vx42 * vy31 * t3 / t1;
        pXy3 = -vx31 * vx42 * t3 / t1;
        pYx1 = -vy31 * vy42 * t2 / t1;
        pYy1 = vy42 * vx31 * t2 / t1;
        pYx3 = vy31 * vy42 * t3 / t1;
        pYy3 = -vy42 * vx31 * t3 / t1;

        pXx2 = vx31 * vy42 * t4 / t1;
        pXy2 = -vx31 * vx42 * t4 / t1;
        pXx4 = -vx31 * vy42 * t5 / t1;
        pXy4 = vx31 * vx42 * t5 / t1;
        pYx2 = vy31 * vy42 * t4 / t1;
        pYy2 = -vy31 * vx42 * t4 / t1;
        pYx4 = -vy31 * vy42 * t5 / t1;
        pYy4 = vy31 * vx42 * t5 / t1;
    }
    private double[][] diffs;
    private double pXu,  pXv,  pYu,  pYv;

    private double generateJaccobi() {
        pXu = pXx1 * diffs[0][0] + pXy1 * diffs[0][1] + pXx3 * diffs[2][0] + pXy3 * diffs[2][1];
        pYu = pYx1 * diffs[0][0] + pYy1 * diffs[0][1] + pYx3 * diffs[2][0] + pYy3 * diffs[2][1];
        pXv = pXx2 * diffs[1][0] + pXy2 * diffs[1][1] + pXx4 * diffs[3][0] + pXy4 * diffs[3][1];
        pYv = pYx2 * diffs[1][0] + pYy2 * diffs[1][1] + pYx4 * diffs[3][0] + pYy4 * diffs[3][1];
        return abs((pXu * pYv - pXv * pYu)/4);
    }

    /**
     * get Coordinate Change Result
     * @param u [-1,1]
     * @param v [-1,1]
     * @return {x,y,jaccobi value}
     */
    @Override
    public double[] getResults(double u, double v, double[] results) throws FunctionEvaluationException {
        u=(u+1)/2;
        v=(v+1)/2;
        generatePoints(u, v);
        generateDiffs(u, v);
        generateTempletVars();
        generateCrossPt();
        generatePartitialDiffs();
        results[0]=x;
        results[1]=y;
        results[2]=generateJaccobi();
        return results;
    }

    /**
     * 
     * @param u
     * @param v
     * @return {{x1(u),y1(u)},{x2(v),y2(v)},{x3(u),y3(u)},{x4(v),y4(v)}}
     */
    private double []pt=new double[2];
    private void generatePoints(double u, double v) throws FunctionEvaluationException{
        funs[0].values(u, pt);
        x1=pt[0];
        y1=pt[1];
        funs[1].values(v, pt);
        x2=pt[0];
        y2=pt[1];
        funs[2].values(u, pt);
        x3=pt[0];
        y3=pt[1];
        funs[3].values(v, pt);
        x4=pt[0];
        y4=pt[1];
    }

    /**
     * 
     * @param u
     * @param v
     * @return{{dx1/du,dy1/du},{dx2/dv,dy2/dv},{dx3/du,dy3/du},{dx4/dv,dy4/dv}}
     */
    private void generateDiffs(double u, double v) throws FunctionEvaluationException{
        derivatedFuns[0].values(u, pt);
        diffs[0][0]=pt[0];
        diffs[0][1]=pt[1];
        derivatedFuns[1].values(v, pt);
        diffs[1][0]=pt[0];
        diffs[1][1]=pt[1];
        derivatedFuns[2].values(u, pt);
        diffs[2][0]=pt[0];
        diffs[2][1]=pt[1];
        derivatedFuns[3].values(v, pt);
        diffs[3][0]=pt[0];
        diffs[3][1]=pt[1];
    }
}
/**
 * result of mfile 
 * x =

x1-(-x1+x3)*(x1*y4-x1*y2-x2*y4-y1*x4+y1*x2+y2*x4)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)



y =

y1-(y3-y1)*(x1*y4-x1*y2-x2*y4-y1*x4+y1*x2+y2*x4)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)



pXx1 =

-(x4-x2)*(y3-y1)*(-x2*y4+y2*x4+x3*y4-x3*y2-x4*y3+x2*y3)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2



pXy1 =

(-x1+x3)*(x4-x2)*(-x2*y4+y2*x4+x3*y4-x3*y2-x4*y3+x2*y3)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2



pXx3 =

(x1*y4-x1*y2-x2*y4-y1*x4+y1*x2+y2*x4)*(x4-x2)*(y3-y1)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2



pXy3 =

(x1-x3)*(x1*y4-x1*y2-x2*y4-y1*x4+y1*x2+y2*x4)*(x4-x2)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2



pYx1 =

-(y3-y1)*(y4-y2)*(-x2*y4+y2*x4+x3*y4-x3*y2-x4*y3+x2*y3)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2



pYy1 =

(y4-y2)*(-x1+x3)*(x3*y4-x3*y2-x2*y4+y2*x4-x4*y3+x2*y3)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2



pYx3 =

(y3-y1)*((y4-y2)*x1+y1*x2+y2*x4-x2*y4-y1*x4)/((y2-y4)*x1+x3*y4-x3*y2+y1*x4-y1*x2-x4*y3+x2*y3)^2*(y4-y2)



pYy3 =

-(x1*y4-x1*y2-x2*y4-y1*x4+y1*x2+y2*x4)*(y4-y2)*(-x1+x3)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2



pXx2 =

(-x1+x3)*(y4-y2)*(-x1*y4+x3*y4-x4*y3-x3*y1+x1*y3+y1*x4)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2



pXy2 =

-(-x1+x3)*(x4-x2)*(-x1*y4+x3*y4-x4*y3-x3*y1+x1*y3+y1*x4)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2



pXx4 =

-(-x1+x3)*(y4-y2)*(-x2*y3+y1*x2+x1*y3-x3*y1-x1*y2+x3*y2)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2



pXy4 =

(-x1+x3)*(-x2+x4)*(-x2*y3+y1*x2+x1*y3-x3*y1-x1*y2+x3*y2)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2



pYx2 =

(y3-y1)*(y4-y2)*(-x1*y4+x3*y4+x1*y3-x3*y1-x4*y3+y1*x4)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2



pYy2 =

-(y3-y1)*(-x2+x4)*(-x1*y4+x3*y4+x1*y3-x3*y1-x4*y3+y1*x4)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2



pYx4 =

-(y3-y1)*(y4-y2)*(-x2*y3+y1*x2+x1*y3-x3*y1-x1*y2+x3*y2)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2



pYy4 =

(y3-y1)*(-x2+x4)*(-x2*y3+y1*x2+x1*y3-x3*y1-x1*y2+x3*y2)/(x3*y4-x3*y2-x1*y4+x1*y2-x4*y3+x2*y3+y1*x4-y1*x2)^2

 */
