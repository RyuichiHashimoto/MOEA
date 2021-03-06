package mo.util.Indicator.Hypervolume;

import mo.util.Front;
import mo.util.POINT;
import mo.util.ReferencePoint;
import mo.util.Comparator.DominationComparator;
import mo.util.Indicator.Indicator;

public abstract class HypervolumeCalculator extends Indicator {

	protected ReferencePoint referencePoint_;


	int nObj = 0;

	protected static DominationComparator Dominator = new DominationComparator(null);

	public HypervolumeCalculator(){

	}


	public HypervolumeCalculator(double[] ReferencePoint){
		referencePoint_ = new ReferencePoint(ReferencePoint);
	}

	public HypervolumeCalculator(ReferencePoint ReferencePoint){
		referencePoint_ = new ReferencePoint(ReferencePoint);
	}

	public void setReferencePoint(ReferencePoint d ){
		referencePoint_ = new ReferencePoint(d);
	}

	public void setReferencePoint(double[] ReferencePoint){
		referencePoint_ = new ReferencePoint(ReferencePoint);
	}

	public POINT getReferencePoint(){
		return referencePoint_;
	}


	public void setMAXProblem(){
		Dominator.setIsMAX();
		isMAXproblem_ = true;
	}
	public void setMINProblem(){
		Dominator.setIsMIN();
		isMAXproblem_ = false;
	}

	public Front nds(Front pop){
//		return nonDominatedSelection.nonDominatedSelection(pop, Dominator.get());
		return null;
	}
	public Front ndswithoutsamepoint(Front pop){
	//	return nonDominatedSelection.nonDominatedSelectionWithoutSamePoint(pop, Dominator.get());
		return null;
	}
	public double inclhv(POINT d){
//		assert d.getDimension() <= nObj : "the dimension of POINT is " + d.getDimension();
		double ret = 1.0;
		for(int i=0;i<nObj;i++){
			ret = ret * (d.get(i) - referencePoint_.get(i));
		}

		return ret;
	}



	public double iex2(Front points) {
		assert points.size() == 2 : "Point size is " + points.size();
		double union = 1;

		for (int i = 0; i < nObj; i++) {
			union *= Dominator.worse(points.get(0).get(i), points.get(1).get(i)) - referencePoint_.get(i);
		}
		return (inclhv(points.get(0)) - union) + inclhv(points.get(1));
	}

}
