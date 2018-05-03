//  WFG3.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package mo.problems.MOP.WFG;

import java.util.HashMap;
import java.util.Map;

import mo.core.Solution;
import mo.util.JMException;

/**
 * This class implements the WFG3 problem
 * Reference: Simon Huband, Luigi Barone, Lyndon While, Phil Hingston
 *            A Scalable Multi-objective Test Problem Toolkit.
 *            Evolutionary Multi-Criterion Optimization:
 *            Third International Conference, EMO 2005.
 *            Proceedings, volume 3410 of Lecture Notes in Computer Science
 */
public class WFG3 extends WFG{

  /**
   * Creates a default WFG3 instances with
   * 2 position-related parameters
   * 4 distance-related parameters
   * and 2 objectives
   * @param solutionType The solution type must "Real" or "BinaryReal".
   */
  public WFG3(String solutionType) throws ClassNotFoundException {
    this(2, 4, 2) ;
  } // WFG3

 /**
  * Creates a WFG3 problem instance
  * @param k Number of position parameters
  * @param l Number of distance parameters
  * @param M Number of objective functions
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public WFG3 (Integer k, Integer l, Integer M) throws ClassNotFoundException {
    super(k,l,M);
    problemName_ = "WFG3";

    S_ = new int[M_];
    for (int i = 0; i < M_; i++) {
      S_[i] = 2 * (i+1);
    }

    A_ = new int[M_-1];
    A_[0] = 1;
    for (int i = 1; i < M_-1; i++) {
      A_[i] = 0;
    }
    variableType_  = 2 ;
  } // WFG3
	public WFG3(HashMap<String, Object> parameters) throws ClassNotFoundException {
		this( (Integer) parameters.get("K"),   (Integer) parameters.get("I"),(Integer) parameters.get("M"));
	}

  /**
  * Evaluates a solution
  * @param z The solution to evaluate
  * @return double [] with the evaluation results
  */
  public double [] evaluate(double [] z){
    double [] y;

    y = normalise(z);
    y = t1(y,k_);
    y = t2(y,k_);
    y = t3(y,k_,M_);

    double [] result = new double[M_];
    double [] x = calculate_x(y);
    for (int m = 1; m <= M_ ; m++) {
      result [m-1] = D_*x[M_-1] + S_[m-1] * (new Shapes()).linear(x,m);
    }

    return result;
  } // evaluate

  /**
   * WFG3 t1 transformation
   */
  public double [] t1(double [] z, int k){
    double [] result = new double[z.length];

    System.arraycopy(z, 0, result, 0, k);

    for (int i = k; i < z.length; i++) {
      result[i] = (new Transformations()).s_linear(z[i],(double)0.35);
    }

    return result;
  } // t1


  /**
   * WFG3 t2 transformation
   */
  public double [] t2(double [] z, int k){
    double [] result = new double[z.length];

    System.arraycopy(z, 0, result, 0, k);

    int l = z.length - k;
    for (int i = k+1; i <= k + l/2; i++){
      int head = k + 2*(i - k) - 1;
      int tail = k + 2*(i - k);
      double [] subZ = subVector(z,head-1,tail-1);

      result[i-1] = (new Transformations()).r_nonsep(subZ,2);
    }
    return result;
  } // t2

  /**
  * WFG3 t3 transformation
  */
  public double [] t3(double [] z, int k, int M){
    double [] result = new double[M];
    double [] w      = new double[z.length];

    for (int i = 0; i < z.length; i++) {
      w[i] = (double)1.0;
    }

    for (int i = 1; i <= M-1; i++){
      int head = (i - 1)*k/(M-1) + 1;
      int tail = i * k / (M - 1);
      double [] subZ = subVector(z,head-1,tail-1);
      double [] subW = subVector(w,head-1,tail-1);

      result[i-1] = (new Transformations()).r_sum(subZ,subW);
    }

    int l = z.length - k;
    int head = k + 1;
    int tail = k + l / 2;
    double [] subZ = subVector(z,head-1,tail-1);
    double [] subW = subVector(w,head-1,tail-1);
    result[M-1] = (new Transformations()).r_sum(subZ,subW);

    return result;
  } // t3

  /**
  * Evaluates a solution
  * @param solution The solution to evaluate
   * @throws JMException
  */
  public final void evaluate(Solution solution) throws JMException {
	    double [] variables = new double[getNumberOfVariables()];

	    for (int i = 0; i < getNumberOfVariables(); i++) {
	      variables[i] = (double)solution.getValue(i);
	    }

	    double [] sol = evaluate(variables);

	    for (int i = 0; i < sol.length; i++) {
	      solution.setObjective(i,sol[i]);
	    }
	  } // evaluate

  @Override
  public void repair(Solution d, Map<String, Object> a) {
  }


} // WFG3
