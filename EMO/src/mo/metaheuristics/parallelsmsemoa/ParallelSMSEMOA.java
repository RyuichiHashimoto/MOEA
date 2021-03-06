package mo.metaheuristics.parallelsmsemoa;
import java.util.ArrayList;
import java.util.List;

import mo.core.Algorithm;
import mo.core.Operator;
import mo.core.Population;
import mo.core.Problem;
import mo.core.Solution;
import mo.operators.selection.ParentsSelection.ParentsSelection;
import mo.operators.selection.ParentsSelection.RandomSelectionWithoutReplacement;
import mo.util.Front;
import mo.util.JMException;
import mo.util.POINT;
import mo.util.Random;
import mo.util.Comparator.Comparator;
import mo.util.Comparator.SMSEMOAComparator.SMSEMOAComparator;
import mo.util.Comparator.SMSEMOAComparator.SMSEMOASelectionComparator;
import mo.util.Indicator.contribution.IHyperVolume;
import mo.util.Ranking.NDSRanking;


/****************************
 *
 * @author Ryuichi
 * 正規化についての注意
 * 正規化するときに0 devide にならないように注意する
 */

public class ParallelSMSEMOA extends Algorithm{

	public ParallelSMSEMOA(Problem problem) {
		super(problem);
		// TODO 自動生成されたコンストラクター・スタブ
	}


	NDSRanking ranking;
	List<Integer>  Sp_;
	int[]	Np_;
	List<List<Integer> > F_all;
	List<List<Integer> > Sp_all  ;
	private  boolean  isNorm = false;
	private int maxEvaluation_ ;
	int[] rank_;

	double[] referencePoint_;
	double[] nadirPoint_;
	double[] IdealPoint_;

	int worstRank_;
	int numberOfParents_;
	private SMSEMOAComparator comparator;
	private int populationSize_;

	int evaluations_;
	Operator crossover_;
	Operator mutation_;
	ParentsSelection ParentSelection_;
	private Population population_;
	private Population Merge_;
	String directoryname;

	int nCPU;

	boolean  isMaxProblem_ ;

	@Override
	public Population execute() throws JMException, ClassNotFoundException {
		setting();
		int time = ((Integer) this.getInputParameter("times")).intValue();
		initPopulation();



		population_.printVariablesToFile(directoryname + "/InitialVAR/InitialVAR" + time + ".dat");

		population_.printObjectivesToFile(directoryname + "/InitialFUN/InitiaFUN" + time + ".dat");


		System.out.print(maxEvaluation_ + " ");
		do {

			makeNextGeneration();

			GotoNextGeneration();

		}while(	evaluations_ <maxEvaluation_);

		System.out.print(evaluations_+ "");
		population_.printVariablesToFile(directoryname + "/FinalVAR/FinalVAR" + time + ".dat");
		population_.printObjectivesToFile(directoryname + "/FinalFUN/FinalFUN" + time + ".dat");
		return population_;
	}


	public int[] ParentsSelection(int[] d){
		assert numberOfParents_ == d.length: "permutation size is wrong permutation size is " + d.length   + "	" + "numberOfparents is " + numberOfParents_;

		for(int i=0;i<numberOfParents_;i++){
			d[i] = Random.nextIntIE(populationSize_);
		}
		return d;
	}

	public void GotoNextGeneration() throws JMException{

		Merge_.AssignID();

		ranking = new NDSRanking(isMAX_);
		ranking.setPop(Merge_);
		ranking.Ranking();

		Population empty = ranking.get(ranking.getworstrank()-1);
		int number = -1;

		// you don't have to calclate the Contribution.
		if(empty.size() == 1){
			number = empty.get(0).getID();
		}  else  {

			Front no = new Front(empty);
			if(isNorm)
				Normalaization(no);
			double[] empt = Allcalc.calclate(no, referencePoint_, comparator.get(), nCPU);
			empt = IHyperVolume.IHV(no);

			List<Integer> list = IHyperVolume.getLowestContribution(empt);
			int d = Random.nextIntIE(list.size());
			number = empty.get( list.get(d) ).getID();
		}

		Merge_.remove(number);
		population_ = new Population(Merge_);
		Merge_ = null;

		assert (population_.size() == populationSize_): "now pop size is " + population_.size() +" but hope " + populationSize_;
	}

	private void Normalaization(Front d){
		if(!isMaxProblem_){
			NormiaztionFormin(d);
		} else {
			assert false : "まだ　未設定";
		}
	}

	 public  List<Integer> outofReferencePoint(Front d) throws JMException{
		 Comparator test = IHyperVolume.getComparator();
		 List<Integer> list = new ArrayList<Integer>();
		for(int i=0; i < d.size();i++){
			if(!(test.execute(new Solution( d.get(i) ), new Solution (new POINT(referencePoint_) ) )   == 1) ){
				list.add(i);
			}
		}
		return list;
	 }

	 //最小化問題のみの正規化 //GEC
	private void NormiaztionFormin(Front no) {
		assert no.size() > 0 : "Front size is "+ no.size();
		assert no.getDimension() > 1 : "Front Dimesnion is "+ no.getDimension();
//		Front bestFront = new Front(getbestRankSolutionSet(Merge_));
		Front bestFront = new Front(ranking.get(0));

		double[] Ideal = getIdealPoint(bestFront);
		double[] Nadia = getNadiaPoint(bestFront);

		double[] vvv = new double[Ideal.length];
		for(int j=0;	j	<vvv.length;	j++){
			vvv[j] = Math.sqrt( (Nadia[j] -Ideal[j])*(Nadia[j] -Ideal[j])) > 1.0E-10 ? Math.sqrt( (Nadia[j] -Ideal[j])*(Nadia[j] -Ideal[j])): 1.0E-10;
		}


		for(int i=0;i<no.size();i++){
			POINT d = no.get(i);
			for(int j=0;j<no.getDimension();j++){
				double val = d.get(j);
				d.set(j, (val - Ideal[j]) / (vvv[j]));
			}
		}
	}

	public Population getWorstRankSolutionSet(Population merge){
		assert worstRank_ > 0 : "the worst rank is " + worstRank_;

		Population pop = new Population(merge.size());
		for(int i=0; i < merge.size() ;i ++ ){
			if(merge.get(i).getRank() == worstRank_){
				pop.add(new Solution( merge.get(i)));
			}
		}

		assert pop.size() > 0 : "the pop size is "  + pop.size();
		return pop;
	}



	public double[] getIdealPoint(Front d){
		double[] ret = new double[d.getDimension()];

		for(int i =0;i<d.getDimension();i++){
			ret[i] = d.get(0).get(i);
		}

		for(int i=1;i<d.size();i++){
			for(int j=0;j<d.getDimension();j++){
				ret[j] = comparator.better(d.get(i).get(j),ret[j]);
			}
		}
		return ret;
	}
	public double[] getNadiaPoint(Front d){

		double[] ret = new double[d.getDimension()];
		for(int i =0;i<d.getDimension();i++){
			ret[i] = d.get(0).get(i);
		}

		for(int i=1;i<d.size();i++){
			for(int j=0;j<d.getDimension();j++){
				ret[j] = comparator.worse(ret[j],d.get(i).get(j));
			}
		}

		return ret;

	}

	public Population getSameRankPop(Population me,int d){
		Population pop = new Population();
		for(int i=0; i < me.size() ;i ++ ){
			if(me.get(i).getRank() == d){
				pop.add(new Solution( me.get(i)));
			}
		}
		assert pop.size() > 0 : "the pop size is "  + pop.size();
		return pop;
	}

	public Population getbestRankSolutionSet(Population me){
		Population pop = new Population(me.size());
		for(int i=0; i < me.size() ;i ++ ){
			if(me.get(i).getRank() == 1){
				pop.add(new Solution( me.get(i)));
			}
		}
		assert pop.size() > 0 : "the pop size is "  + pop.size();
		return pop;
	}


	public void initPopulation() throws JMException, ClassNotFoundException {

 		for (int i = 0; i < populationSize_; i++) {
			Solution newSolution = new Solution(problem_);
			evaluations_++;
			problem_.repair(newSolution,null);
			problem_.evaluate(newSolution);
		 	population_.add(newSolution);
		} // for
	} // initPopulatiown
 // initPopulation

	public void makeNextGeneration() throws JMException {
		Merge_ = new Population(populationSize_ + 1);
		Merge_.merge(population_);

		Solution   child;
		Solution[] parents   = new Solution[2];

		int one  = (int) ParentSelection_.execute(population_);
		int two  = (int) ParentSelection_.execute(population_);

		parents[0] = population_.get(one);
		parents[1] = population_.get(two);

		child = (Solution) crossover_.execute(parents);
		mutation_.execute(child);
		problem_.repair(child ,null);

		problem_.evaluate(child);
		evaluations_ = evaluations_ + 1;
		Merge_.add(new Solution(child));

	}


	public void setting(){
		numberOfParents_ = 2;
		comparator = new SMSEMOASelectionComparator(null);
		evaluations_ = 0;
		populationSize_ = ((Integer)this.getInputParameter("populationSize"));//((Integer)this.getInputParameter("populationSize"));
		maxEvaluation_ = ((Integer)this.getInputParameter("maxEvaluations"));
		population_ = new Population(populationSize_);

//		ParentSelection_ = new BinaryTournament(null);
		ParentSelection_ = new RandomSelectionWithoutReplacement(null);
		ParentSelection_.setComparator(comparator);
		isMaxProblem_    = ((boolean)this.getInputParameter("ismax"));
		IHyperVolume.set(isMaxProblem_);
		double[] ref = ((double[]) this.getInputParameter("ReferencePoint"));
		referencePoint_ = new double[ref.length];
		for(int i=0;i<ref.length;i++){
			referencePoint_[i] = ref[i];
		}
		comparator.setIs(isMaxProblem_);
		IHyperVolume.setReferencePoint(referencePoint_);

		crossover_ = operators_.get("crossover"); // default: DE crossover
		mutation_ = operators_.get("mutation"); // default: polynomial mutation
		directoryname = ((String) this.getInputParameter("DirectoryName"));
		isNorm = ((boolean) this.getInputParameter("isNorm"));
		nCPU = ((int)this.getInputParameter("nCPU"));

	}

}
