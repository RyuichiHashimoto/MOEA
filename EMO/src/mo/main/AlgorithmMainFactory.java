package mo.main;

import experiments.Setting;
import mo.core.AlgorithmMain;
import mo.util.JMException;

public class AlgorithmMainFactory {
	public static AlgorithmMain getAlgorithmMain(String name,Setting parameters) throws JMException {
		if (name.equalsIgnoreCase("NSGAII")){
			return new NSGAIIMain(parameters);
		} if (name.equalsIgnoreCase("NSGAIIUpdate")){
			return new NSGAIIUPDateMain(parameters);
		}else if (name.equalsIgnoreCase("SMSEMOA")){
			return new SMSEMOAMain(parameters);
		} else if (name.equalsIgnoreCase("MOEAD")){
			return new MOEADMain(parameters);
		} else if (name.equalsIgnoreCase("NSGAIII_YY")){
			return new NSGAIIIMain(parameters);
		} else if (name.equalsIgnoreCase("SMSEMOAIGD")){
			return new SMSEMOAIGDMain(parameters);
		} else if (name.equalsIgnoreCase("NormalizeMOEAD")){
			return new NormalizeMOEADMain(parameters);
		} else if (name.equalsIgnoreCase("ParallelSMSEMOAIGD")){
			return new ParallelSMSEMOAIGDMain(parameters);
		} else if (name.equalsIgnoreCase("ParallelSMSEMOA")){
			return new ParallelSMSEMOAMain(parameters);
		}else if (name.equalsIgnoreCase("MOEADCDP")){
			return new MOEAD_CDPMain(parameters);
		}else if (name.equalsIgnoreCase("NSGAIICDP")){
			return new NSGAIICDP_Main(parameters);
		}else if (name.equalsIgnoreCase("SpeedUpNSGAII")){
			return new SpeedUp_NSGAIIMain(parameters);
		}else if (name.equalsIgnoreCase("NSGAIII_YY_main")){
			return new NSGAIICDP_Main(parameters);
		}/*
		  else if (name.equalsIgnoreCase("SBXCrossover2")){
			return new SBXCrossover2(parameters);
		} else if (name.equalsIgnoreCase("UniformCrossover")){
			return new UniformCrossover(parameters);
		}
		else if (name.equalsIgnoreCase("SinglePointCrossover"))
			return new SinglePointCrossover(parameters);
		else if (name.equalsIgnoreCase("PMXCrossover"))
			return new PMXCrossover(parameters);
		else if (name.equalsIgnoreCase("TwoPointsCrossover"))
			return new TwoPointsCrossover(parameters);
		else if (name.equalsIgnoreCase("HUXCrossover"))
			return new HUXCrossover(parameters);
		else if (name.equalsIgnoreCase("DifferentialEvolutionCrossover"))
			return new DifferentialEvolutionCrossover(parameters);
		else if (name.equalsIgnoreCase("BLXAlphaCrossover"))
			return new BLXAlphaCrossover(parameters);
		}*/ else{
			System.err.print("AlgorithmMainFactory.getAlgorithmMain. " + " AlgorithmMain '" + name + "' not found ");
			throw new JMException("Exception in " + name + ".getCrossoverOperator()");
		} // else
	} // getCrossoverOperator

}
