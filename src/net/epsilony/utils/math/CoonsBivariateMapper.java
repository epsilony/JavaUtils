/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils.math;

import java.util.Collection;
import net.epsilony.utils.java2d.PathInfoCalculator;
import net.epsilony.utils.java2d.PathInfoNode;
import org.apache.commons.math.FunctionEvaluationException;
import static java.lang.Math.abs;

/**
 * 利用线性Coon曲线的计算，将u,v正方形区域（u,v属于[-1,1])映射到一个由四个参数线段所围成的区域，
 * 四个参数线段的参数值为（u+1)/2或(v+1)/2具体的映射公式参考文献：计算机图形学（第三版）孙家广等编著，
 * 清华大学出版社第六章曲线和曲面6.3.4第343－344页
 * @author epsilonyuan@gmail.com
 */
final public class CoonsBivariateMapper implements BivariateMapper {

    PathInfoCalculator[] pICs = new PathInfoCalculator[4];
    double[] starts = new double[8];
    

    {
        for (int i = 0; i < 4; i++) {
            pICs[i] = new PathInfoCalculator();
        }
    }

    public void setInfos(PathInfoNode[] inputs) {
        for (int i = 0; i < 4; i++) {
            pICs[i].setPathInfoNode(inputs[i]);
            starts[i * 2] = inputs[i].datas[0];
            starts[i * 2 + 1] = inputs[i].datas[1];
        }
    }

    public void setInfos(Collection<PathInfoNode> inputs) {
        int i = 0;
        for (PathInfoNode p : inputs) {
            pICs[i].setPathInfoNode(p);
            starts[i * 2] = p.datas[0];
            starts[i * 2 + 1] = p.datas[1];
            i++;
        }
    }
    double[] p0v = new double[2];
    double[] p1v = new double[2];
    double[] pu0 = new double[2];
    double[] pu1 = new double[2];
    double[] d0v = new double[2];
    double[] d1v = new double[2];
    double[] du0 = new double[2];
    double[] du1 = new double[2];

    @Override
    public double[] getResults(double u, double v, double[] results) {
        try {
            u = (u + 1) / 2;
            v = (v + 1) / 2;

            pICs[0].value(v, p0v);
            pICs[2].value(1 - v, p1v);
            pICs[1].value(u, pu1);
            pICs[3].value(1 - u, pu0);
            results[0] = p0v[0] * (1 - u) + p1v[0] * u + pu0[0] * (1 - v) + pu1[0] * v - ((starts[0] * (1 - v) + starts[2] * v) * (1 - u) + (starts[6] * (1 - v) + starts[4] * v) * u);
            results[1] = p0v[1] * (1 - u) + p1v[1] * u + pu0[1] * (1 - v) + pu1[1] * v - ((starts[1] * (1 - v) + starts[3] * v) * (1 - u) + (starts[7] * (1 - v) + starts[5] * v) * u);

            double dxu, dxv, dyu, dyv;
            pICs[0].vectorDerivative().value(v, d0v);
            pICs[2].vectorDerivative().value(1 - v, d1v);
            pICs[1].vectorDerivative().value(u, du1);
            pICs[3].vectorDerivative().value(1 - u, du0);
            dxu = -p0v[0] + p1v[0] - du0[0] * (1 - v) + du1[0] * v - (-(starts[0] * (1 - v) + starts[2] * v) + (starts[6] * (1 - v) + starts[4] * v));
            dyu = -p0v[1] + p1v[1] - du0[1] * (1 - v) + du1[1] * v - (-(starts[1] * (1 - v) + starts[3] * v) + (starts[7] * (1 - v) + starts[5] * v));

            dxv = d0v[0] * (1 - u) - d1v[0] * u - pu0[0] + pu1[0] - ((-starts[0] + starts[2]) * (1 - u) + (-starts[6] + starts[4]) * u);
            dyv = d0v[1] * (1 - u) - d1v[1] * u - pu0[1] + pu1[1] - ((-starts[1] + starts[3]) * (1 - u) + (-starts[7] + starts[5]) * u);
            
            results[2] = abs(dxu * dyv - dxv * dyu) / 4;
            return results;
        } catch (FunctionEvaluationException ex) {
        }finally{
            return null;
        }
    }
}
