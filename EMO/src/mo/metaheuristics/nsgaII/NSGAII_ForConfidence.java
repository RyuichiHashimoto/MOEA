//  MOEAD.java
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

package mo.metaheuristics.nsgaII;

import java.util.HashMap;

import mo.core.Algorithm;
import mo.core.Operator;
import mo.core.Population;
import mo.core.Problem;
import mo.core.Solution;
import mo.operators.selection.ParentsSelection.BinaryTournament;
import mo.operators.selection.ParentsSelection.ParentsSelection;
import mo.util.DirectoryMaker;
import mo.util.JMException;
import mo.util.Permutation;
import mo.util.fileSubscription;
import mo.util.Comparator.NSGAIIComparator.NSGAIIComparator;
import mo.util.Comparator.NSGAIIComparator.NSGAIIComparatorBinary;
import mo.util.Comparator.NSGAIIComparator.NSGAIIComparatorDominance;
import mo.util.Comparator.NSGAIIComparator.NSGAIIComparatorNextGen;
import mo.util.Ranking.NDSRanking;


public class NSGAII_ForConfidence extends Algorithm {

	/**
	 * Stores the population size
	 */
	private int populationSize_;
	/**
	 * Stores the population
	 */
	private Population population_;

	private Population  offSpring_;

	private Population  merge_;

	private int generation;

	/**
	 * Dominance Counter
	 */
	private int dominancedSolutionCounter;

	private int[] dominancedSolutionCounterHistory;


	int evaluations_;

	Operator crossover_;
	Operator mutation_;


	int maxEvaluations_;

	int sizeOfNeiborhoodRepleaced_;
	int sizeOfMatingNeiborhood_;

	HashMap parameters;

	public NSGAII_ForConfidence(Problem problem) {
		super(problem);
	} // DMOEA

	private ParentsSelection selection_ ;//= new BinaryTournament();
	private NSGAIIComparator comparator_binary;
	private  NSGAIIComparator  comparator_Dominance;
	private NSGAIIComparator comparator_nextGen;
	private boolean outNormal_;
	private String directoryname;

	public void setting(){
		evaluations_  = 0;
		maxEvaluations_ = ((Integer) this.getInputParameter("maxEvaluations")).intValue();
		comparator_binary = new NSGAIIComparatorBinary(parameters);
		comparator_nextGen = new NSGAIIComparatorNextGen(parameters);
		comparator_Dominance = new NSGAIIComparatorDominance(parameters);
		isMAX_    = ((boolean)this.getInputParameter("ismax"));
		comparator_binary.setIs(isMAX_);
		comparator_nextGen.setIs(isMAX_);
		comparator_Dominance.setIs(isMAX_);
		selection_ = new BinaryTournament(null,comparator_binary);
	//	outNormal_ = ((boolean) this.getInputParameter("outputNormal"));
		outNormal_ = false;
		directoryname = ((String) this.getInputParameter("DirectoryName"));
		generation=0;
		populationSize_ = ((Integer)this.getInputParameter("populationSize"));
		crossover_ = operators_.get("crossover"); // default: DE crossover
		mutation_ = operators_.get("mutation"); // default: polynomial mutation
	}

	public Population execute() throws JMException, ClassNotFoundException {
		setting();
		int time = ((Integer) this.getInputParameter("times")).intValue();
		DirectoryMaker.Make(directoryname + "/DominancedCounter/");
		initPopulation();

		population_.printVariablesToFile(directoryname + "/InitialVAR/InitialVAR" + time + ".dat");

		population_.printObjectivesToFile(directoryname + "/InitialFUN/InitiaFUN" + time + ".dat");

		merge_ = new Population(populationSize_*2);

		NDSRanking ranking = new NDSRanking(isMAX_);
		ranking.setPop(population_);
		ranking.Ranking();
		for(int i=0;i<ranking.getworstrank();i++){
			CrowdingDistance(ranking.get(i));
		}
		ranking = null;
		int generation = 0;

		dominancedSolutionCounterHistory = new int[ maxEvaluations_/evaluations_  ];

		dominancedSolutionCounterHistory[generation] = getDominatedSolCount(population_);

		do {
			makeNextGeneration();
			generation++;
			merge_.clear();
			merge_.merge(population_);
			merge_.merge((offSpring_));

			GotoNextGeneration();

			dominancedSolutionCounterHistory[generation] = getDominatedSolCount(population_);

		} while (evaluations_ < maxEvaluations_ );

		population_.printVariablesToFile(directoryname + "/FinalVAR/FinalVAR" + time + ".dat");
		population_.printObjectivesToFile(directoryname + "/FinalFUN/FinalFUN" + time + ".dat");
		fileSubscription.printToFile(directoryname + "/DominancedCounter/Data" + time + ".dat",dominancedSolutionCounterHistory,1);

		//population_.printVariablesToFile("result/config/FinalVAR" + time + ".dat");
		return population_;
	}

	public int getDominatedSolCount(Population pop){
		NDSRanking rank = new NDSRanking(isMAX_);
		rank.setPop(pop);
		rank.Ranking();
		return populationSize_ - rank.get(0).size();
	}

	public boolean makeNextGeneration() throws JMException{
		offSpring_ = new Population(populationSize_);
		Solution[] parents = new Solution[2];
		Solution offSpring;
		for(int i=0;i < populationSize_;i++){

			int one =  (int)selection_.execute(population_);
			int two =  (int)selection_.execute(population_);

			parents[0] = population_.get(one);
			parents[1] = population_.get(two);

			offSpring = (Solution) crossover_.execute(parents);
			mutation_.execute(offSpring);
			problem_.repair(offSpring,null);
			problem_.evaluate(offSpring);
			evaluations_ = evaluations_ + 1;

			offSpring_.add(offSpring);
			if(evaluations_ == maxEvaluations_){
				return true;
			}
		}
		return false;
	}

	public void initPopulation() throws JMException, ClassNotFoundException {
		population_ = new Population(populationSize_);
 		for (int i = 0; i < populationSize_; i++) {
			Solution newSolution = new Solution(problem_);
			problem_.repair(newSolution,null);
			evaluations_++;
			problem_.evaluate(newSolution);
		 	population_.add(newSolution);
		}
	}



	private void GotoNextGeneration() throws JMException{
		NDSRanking Ranking = new NDSRanking(isMAX_);
		Ranking.setPop(merge_);
		Ranking.Ranking();
		population_ = new Population(populationSize_);


		int rank = 0;
		while (population_.size() + Ranking.get(rank).size()  <= populationSize_) {
			Population d = Ranking.get(rank++);
			d.AssignID();
			CrowdingDistance(d);
			population_.merge(d);
		}

		if(populationSize_ != population_.size()){
			Population ret = Ranking.get(rank);
			int []  permu = new int[ret.size()];
			ret.AssignID();
			CrowdingDistance(ret);
			Permutation.setPermutation(permu);

			Sort(permu,ret);
			int size = populationSize_ - population_.size();
			Population d = new Population(size);
			for(int i = 0;i<size;i++){
				d.add(ret.get(permu[i]));
			}

			assert d.size() + population_.size() == populationSize_ : "たのしいな" + ret.size() +"	"+ population_.size()+"	"+ populationSize_;
			permu = new int[d.size()];
			Permutation.setPermutation(permu);
			d.AssignID();
			CrowdingDistance(d);
			Sort(permu,d);

			for(int i=0;i<d.size();i++){
				population_.add(d.get(permu[i]));
			}
			}
			assert population_.size()  == populationSize_ :population_.size()  + "	" + populationSize_;
		}

	private void Sort(int[] permutation ,Population pop) throws JMException{
		for(int i=0;i<pop.size();i++){
			for(int j=0;j<pop.size();j++){
				int p = permutation[i];
				int q = permutation[j];
				if(comparator_binary.execute(pop.get(p),pop.get(q)) == 1){
					int empty = permutation[i];
					permutation[i] = permutation[j];
					permutation[j] = empty;
				}
			}
		}
	}

	private void CrowdingDistance(Population a){
		for(int i = 0;i< a.size();i++){
				a.get(i).setCrowedDistance(0.0);
		}
		int Objectives = a.get(0).getNumberOfObjectives();

		double max,min;
		double em;
		for(int key = 0;key< Objectives; key++){
				int [] e = a.sortObjectivereturnperm(key);
				a.get(e[0]).setCrowedDistance(Double.POSITIVE_INFINITY);
				a.get(e[a.size() - 1]).setCrowedDistance(Double.POSITIVE_INFINITY);
				max = a.get(e[a.size() - 1]).getObjective(key);
				min = a.get(e[0]).getObjective(key);
				if (max - min < 1.0E-14){
					continue;
				}

			for(int  n = 1;n < a.size() -1 ;n++){
					Solution sp = a.get(e[n]);
					em = sp.getCrowdDistance_();
					em = em + (a.get(e[n + 1]).getObjective(key)  - a.get(e[n - 1]).getObjective(key))/(max - min);
					sp.setCrowedDistance(em);
			  }
		}
	//	System.out.println();
	//	System.out.println("関数内");

	}


}
