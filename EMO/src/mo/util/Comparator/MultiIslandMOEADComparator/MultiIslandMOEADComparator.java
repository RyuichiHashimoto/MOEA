package mo.util.Comparator.MultiIslandMOEADComparator;

import java.util.HashMap;

import mo.util.ReferencePoint;
import mo.util.WeightedVector;
import mo.util.Comparator.Comparator;
import mo.util.ScalarzingFunction.ScalarzingFunction;

public abstract class MultiIslandMOEADComparator extends Comparator {



	public MultiIslandMOEADComparator(HashMap<String, Object> parameters) {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public MultiIslandMOEADComparator(HashMap<String, Object> parameters,ScalarzingFunction[] d ) {
		super(parameters);
		ScalaringFunction_ = d;
		NowScalaringFunction_ = null;
	}

	protected ScalarzingFunction[] ScalaringFunction_;

	protected ScalarzingFunction NowScalaringFunction_;

	protected ReferencePoint referencePoint;

	protected WeightedVector weightedVector;


	public ScalarzingFunction getScalarzingFunction(int key){return ScalaringFunction_[key];};





	public void setRefernecePoint(double[] d){
		setRefernecePoint(new ReferencePoint(d));
	}

	public void setRefernecePoint(ReferencePoint d){
		referencePoint = d;
	}

	public void setWeightedVector(double[] d){
		setWeightedVector(new WeightedVector(d));
	}

	public void setWeightedVector(WeightedVector d){
		weightedVector = d;
	}





}
