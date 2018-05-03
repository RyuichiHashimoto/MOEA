
package mo.problems.MOP.CF;

import java.util.HashMap;
import java.util.Map;

import mo.core.Problem;
import mo.core.Solution;
import mo.util.JMException;


/**
 * Class representing problem DTLZ1
 */
public class CF10 extends Problem {
 /**
  * Creates a default DTLZ1 problem (7 variables and 3 objectives)
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public CF10(String solutionType) throws ClassNotFoundException {
    this(7, 3);
  } // DTLZ1
  public CF10(HashMap<String, Object> parameters) {
		this((Integer) parameters.get("numberOfVariables"),(Integer) parameters.get("numberOfObjectives"));
	}
  /**
  * Creates a DTLZ1 problem instance
  * @param numberOfVariables Number of variables
  * @param numberOfObjectives Number of objective functions
  * @param solutionType The solution type must "Real" or "BinaryReal".
  */
  public CF10(  int  numberOfVariables,  int  numberOfObjectives) {
	 numberOfVariables_  = numberOfVariables;
    numberOfObjectives_ = 3;
    problemName_        = "CF10";
    numberOfConstraint_ = 1;

    lowerLimit_ = new double[numberOfVariables_];
    upperLimit_ = new double[numberOfVariables_];
    for (int var = 0; var < numberOfVariables; var++){
      lowerLimit_[var] = -2.0;
      upperLimit_[var] = 2.0;
    } //for
    int var = 0;
    lowerLimit_[var] = 0.0;
    upperLimit_[var] = 1.0;
    var = 1;
    lowerLimit_[var] = 0.0;
    upperLimit_[var] = 1.0;

    	variableType_  = 2 ;

  }
  public void evaluate(Solution solution) throws JMException {
	    double [] x = new double[numberOfVariables_];
	    double [] f = new double[numberOfObjectives_];
	    double [] c = new double[numberOfConstraint_];

	    int k = numberOfVariables_ - numberOfObjectives_ + 1;
	    int nx = numberOfVariables_;
	    double sum1 = 0.0, sum2 = 0.0,sum3 = 0.0;
		int count1 = 0, count2 = 0,count3 = 0;
		double yj;
		double N = 2.0, a = 3.0;
		for(int i = 0;i < x.length;i++){
			x[i] = solution.getValue(i);
		}


		N = 2.0; a = 1.0;
		double hj;
		sum1 = sum2 = sum3 = 0.0;
		count1 = count2 = count3 = 0;
		for (int j = 3; j <= nx; j++)
		{
			yj = x[j - 1] - 2.0*x[1] * Math.sin(2.0*Math.PI*x[0] + j*Math.PI / nx);
			hj = 4.0*yj*yj - Math.cos(8.0*Math.PI*yj) + 1.0;
			if (j % 3 == 1)
			{
				sum1 += hj;
				count1++;
			}
			else if (j % 3 == 2)
			{
				sum2 += hj;
				count2++;
			}
			else
			{
				sum3 += hj;
				count3++;
			}
		}
		f[0] = Math.cos(0.5*Math.PI*x[0])*Math.cos(0.5*Math.PI*x[1]) + 2.0*sum1 / (double)count1;
		f[1] = Math.cos(0.5*Math.PI*x[0])*Math.sin(0.5*Math.PI*x[1]) + 2.0*sum2 / (double)count2;
		f[2] = Math.sin(0.5*Math.PI*x[0]) + 2.0*sum3 / (double)count3;
		c[0] = (f[0] * f[0] + f[1] * f[1]) / (1 - f[2] * f[2]) - a*Math.sin(N*Math.PI*((f[0] * f[0] - f[1] * f[1]) / (1 - f[2] * f[2]) + 1.0)) - 1.0;
	    for (int i = 0; i < numberOfObjectives_; i++)
	      solution.setObjective(i,f[i]);
//	    System.out.println(x);
	    solution.setConstrain(0, c[0]  > 0.0 ? 0.0 : -1*c[0] );

	    solution.calctotalCalc();
  }
  public double MYSIGN(double x){
	  return x > 0.0 ? 1.0 :-1.0;
  }


  @Override
  public void repair(Solution d, Map<String, Object> a) {
  }

} //evaluate
