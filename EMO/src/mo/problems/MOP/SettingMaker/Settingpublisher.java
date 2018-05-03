package mo.problems.MOP.SettingMaker;

import java.util.List;

public class Settingpublisher {

	public static List<Integer>   getprograming(String Algorithmname,String problemname,int numberOfObj){


		if(Algorithmname.equalsIgnoreCase("NSGAII")){
			return NSGAIISetting.getSetting(numberOfObj, problemname);
		} else if (Algorithmname.equalsIgnoreCase("MOEAD")){
			return MOEADSetting.getSetting(numberOfObj, problemname);
		} else if (Algorithmname.equalsIgnoreCase("SMSEMOA")){
			return SMSEMOASetting.getSetting(numberOfObj, problemname);
		} else if (Algorithmname.equalsIgnoreCase("ParallelSMSEMOA")){
			return SMSEMOASetting.getSetting(numberOfObj, problemname);
		}  else if (Algorithmname.equalsIgnoreCase("SMSEMOAIGD")){
			return SMSEMOAIGDSetting.getSetting(numberOfObj, problemname);
		}  else if (Algorithmname.equalsIgnoreCase("ParallelSMSEMOAIGD")){
			return SMSEMOAIGDSetting.getSetting(numberOfObj, problemname);
		}  else if (Algorithmname.equalsIgnoreCase("NSGAIII_YY")){
			return NSGAIIISetting.getSetting(numberOfObj, problemname);
		 }else if (Algorithmname.equalsIgnoreCase("NormalizeMOEAD")){
			return NormalizeMOEADSetting.getSetting(numberOfObj, problemname);
		}


		assert false : Algorithmname +" is not found";
		return null;
	}



}
