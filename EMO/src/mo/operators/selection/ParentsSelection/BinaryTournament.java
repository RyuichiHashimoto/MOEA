package mo.operators.selection.ParentsSelection;

import java.util.HashMap;

import mo.core.Population;
import mo.util.JMException;
import mo.util.Random;
import mo.util.Comparator.Comparator;

public class BinaryTournament extends ParentsSelection{



	public BinaryTournament(HashMap<String, Object> parameters , Comparator d) {
		super(parameters);
		comparator_   = d;
	}

	public BinaryTournament(HashMap<String, Object> parameters) {
		super(parameters);
	}

	@Override
	public Object execute(Object object) throws JMException {
		Population d = (Population)(object);
		int ret;
		int a = 0;
		int b = 0;
		int size = d.size();
		a = Random.nextIntIE(0, size);
		do {
			b = Random.nextIntIE(0,size);
		}while( a == b);
		if(comparator_.execute(d.get(a), d.get(b)) == 1){
			ret = a;
		} else {
			ret = b;
		}
		return ret;
	}

	/*public Object execute(Object object) throws JMException {
		Population test  = (Population)object;




		return null;
	}*/

}
