/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.util.ui.geom;

import java.awt.geom.PathIterator;
import java.util.List;
import static java.lang.Math.*;

/**
 *
 * @author epsilon
 */
public class G1PathInfosCalculator {

    private final static PathInfoCalculator pathInfoCalculator = new PathInfoCalculator(null);
    List<PathInfoNode> infos;

    public void setInfos(List<PathInfoNode> infos) throws Exception {
        this.infos = infos;
        generateGapLens();
    }
    double[] gapLens = null;
    double sumLen;
    int gapLensSize;

    private void generateGapLens() throws Exception {
        if (null == gapLens || gapLens.length < infos.size()) {
            gapLens = new double[infos.size()];
        }
        gapLensSize = infos.size();
        int i = 1;
        PathInfoNode tp = null;
        double tLen=1;
        sumLen = 1;
        gapLens[0]=1;
        for (PathInfoNode p : infos) {
            if(null==tp){
                tp=p;
                continue;
            }
            tLen*=getStartSpeed(p)/getEndSpeed(tp);
            gapLens[i]=tLen;           
            sumLen+=tLen;
            i++;
            tp=p;
        }
    }

    private double getEndSpeed(PathInfoNode pn) throws Exception {
        switch (pn.segType) {
            case PathIterator.SEG_CUBICTO:
                return 3*sqrt((pn.datas[6] - pn.datas[4]) * (pn.datas[6] - pn.datas[4]) + (pn.datas[7] - pn.datas[5]) * (pn.datas[7] - pn.datas[5]));
            case PathIterator.SEG_LINETO:
                return sqrt((pn.datas[2] - pn.datas[0]) * (pn.datas[2] - pn.datas[0]) + (pn.datas[3] - pn.datas[1]) * (pn.datas[3] - pn.datas[1]));
            case PathIterator.SEG_QUADTO:
                return 3*sqrt((pn.datas[4] - pn.datas[2]) * (pn.datas[4] - pn.datas[2]) + (pn.datas[5] - pn.datas[3]) * (pn.datas[5] - pn.datas[3]));
            default:
                throw new Exception("Bad type of PathInfoNode: " + pn.segType + " " + pn);
        }
    }

    private double getStartSpeed(PathInfoNode pn) throws Exception {
        if(pn.segType!=PathIterator.SEG_LINETO){
        return 3*sqrt((pn.datas[2] - pn.datas[0]) * (pn.datas[2] - pn.datas[0]) + (pn.datas[3] - pn.datas[1]) * (pn.datas[3] - pn.datas[1]));
        }else{
            return sqrt((pn.datas[2] - pn.datas[0]) * (pn.datas[2] - pn.datas[0]) + (pn.datas[3] - pn.datas[1]) * (pn.datas[3] - pn.datas[1]));
        }
    }
}
