package mo.util.Comparator.MOEADComparator;


import java.util.HashMap;

import mo.core.Solution;
import mo.util.JMException;
import mo.util.ScalarzingFunction.ScalarzingFunction;
public class NomalMOEADComapator extends MOEADComparator {

	public NomalMOEADComapator(HashMap<String, Object> parameters) {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	public NomalMOEADComapator(HashMap<String, Object> parameters,ScalarzingFunction set) {
		super(parameters,set);
	}




	@Override
	public int execute(Object one, Object two) throws JMException {
		Solution solone = (Solution)one;
		Solution soltwo = (Solution)two;

		double scalar_one = ScalaringFunction_.execute(solone, weightedVector.get(), referencePoint.get());
		double scalar_two = ScalaringFunction_.execute(soltwo, weightedVector.get(), referencePoint.get());


		if (isMAX_== (scalar_one > scalar_two)){
			return 1;
		} else if  (isMAX_== (scalar_one < scalar_two)){
			return -1;
		}

		return 0;
	}


}
