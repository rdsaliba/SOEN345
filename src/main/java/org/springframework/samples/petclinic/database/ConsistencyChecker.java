package org.springframework.samples.petclinic.database;


import javax.swing.plaf.synth.SynthSeparatorUI;

import org.springframework.samples.petclinic.database.HashData;
import org.springframework.samples.petclinic.database.HashGenerationException;

public class ConsistencyChecker {

    //private static Logger log = LoggerFactory.getLogger(ConsistencyChecker.class);
	private String oldData [][];
	private String newData [][];
	private int	errorOccurance;
	private int totalRowChecked;
	
	public ConsistencyChecker(String oldData [][],String newData [][]) {
		this.oldData=oldData;
		this.newData=newData;
	}

	public void checkConsistency(String Table) throws HashGenerationException {
		errorOccurance = 0;
		totalRowChecked = 0;
		for (int i=0; i<oldData.length;i++)
		{
			totalRowChecked++;
			for (int j=0; j<oldData[0].length;j++) {
				String data_old = HashData.getHashFromString(oldData[i][j]);
				String data_new = HashData.getHashFromString(newData[i][j]);
				if (!(data_old.equals(data_new))) {
					System.out.println("Failed Migration");
					System.out.println(data_old + " not same as " + data_new);
					
					switch (Table) {
					case "owners"			: break;
					case "pets"				: break;
					case "specialties"		: break;
					case "vets"				: break;
					case "visits"			: break;
					case "vet_specialties"	: break;
					}
				}
			}
		}
		System.out.println("CHECK COMPLETE");
	}
	
	public void thresholdCheck() {
		System.out.println();
	}

	public void updateData() {}
}
