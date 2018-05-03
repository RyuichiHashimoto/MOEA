package mo.main;

import java.util.HashMap;

import javax.naming.NameNotFoundException;

import experiments.Setting;
import mo.core.AlgorithmMain;
import mo.core.Operator;
import mo.core.Problem;
import mo.metaheuristics.NSGAII_CDP.NSGAII_CDP;
import mo.operators.crossover.CrossoverFactory;
import mo.operators.mutation.MutationFactory;
import mo.problems.MOP.MOPFactory;
import mo.util.JMException;

public class NSGAIICDP_Main extends AlgorithmMain{

	public NSGAIICDP_Main(Setting test) {
		super(test);
	}



	@Override
	public void setParameter() throws NameNotFoundException, ClassNotFoundException, JMException {


		String Problemname = setting_.getAsStr("Problem");
		int OBJ = setting_.getAsInt("Objectives");
		int NumberOfRun = setting_.getAsInt("NumberOfTrial");

        Problem problem; // The problem to solve
		Operator crossover = null; // Crossover operator
		Operator mutation = null; // Mutation operator

		HashMap parameters; // Operator parameters
		HashMap d = new HashMap();
		d.put("numberOfObjectives",OBJ);
		problem = MOPFactory.getMOP(Problemname,setting_,"NSGAII_CDP");
		algorithm = new NSGAII_CDP(problem);
		String dddname = problem.getNumberOfObjectives()  + "OBJ";
		algorithm.setInputParameter("populationSize", setting_.getAsInt("populationSize"));

		DirectoryName = "result/NSGAII_CDP/" + Problemname+"/"+dddname;
		algorithm.setInputParameter("DirectoryName",  DirectoryName);



		algorithm.setInputParameter("maxEvaluations", setting_.getAsInt("maxEvaluations"));
		algorithm.setInputParameter("numberOfParents", 2);
//		algorithm.setInputParameter("SMSEMOA", 2);
		parameters = new HashMap();
		parameters.put("Mutationprobability",setting_.getAsDouble("MutationProbability"));
		parameters.put("MutationdistributionIndex",setting_.getAsDouble("MutationDistribution"));
//		algorithm.setInputParameter("outputNormal",setting_.getAsBool("outputNormal"));

		try {
			mutation = MutationFactory.getMutationOperator(setting_.getAsStr("MutationName"), parameters);
		} catch (JMException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		parameters.put("Crossoverprobability",setting_.getAsDouble("CrossoverProbability"));
		parameters.put("CrossoverdistributionIndex",setting_.getAsDouble("CrossoverDistribution"));
		try {
			crossover = CrossoverFactory.getCrossoverOperator(setting_.getAsStr("CrossoverName"), parameters);
		} catch (JMException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.setInputParameter("ismax", setting_.getAsBool("isMax"));
		algorithm.setInputParameter("isNorm", setting_.getAsBool("IsNorm"));


	}








}
