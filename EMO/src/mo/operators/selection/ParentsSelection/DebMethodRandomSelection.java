package mo.operators.selection.ParentsSelection;

import java.util.HashMap;

import mo.core.Population;
import mo.operators.selection.Selection;
import mo.util.Permutation;

public class DebMethodRandomSelection extends Selection{
	public DebMethodRandomSelection(HashMap<String, Object> parameters) {
		super(parameters);
		// TODO 自動生成されたコンストラクター・スタブ
	}


	/**
	 * Performs the operation
	 *
	 * @param object
	 *            Object representing a SolutionSet.
	 * @return an object representing an array with the selected parents
	 */
	public Object execute(Object object) {
		Population population = (Population) object;
		int[]  perm = new int[population.size()];
		Permutation.randomPermutation(perm);

		return perm;
	} // Execute


}
