import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.plaf.synth.SynthSeparatorUI;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.security.*;
public class ConsistencyChecker {
	
    //private static Logger log = LoggerFactory.getLogger(ConsistencyChecker.class);
	private String oldData [][];
	private String newData [][];
	
	public ConsistencyChecker(String oldData [][],String newData [][]) {
		this.oldData=oldData;
		this.newData=newData;
	}
	
	public void checkConsistency() throws HashGenerationException {
		
		for (int i=0; i<oldData.length;i++)
		{
			for (int j=0; j<oldData[0].length;j++) {
				String data_old = HashData.getHashFromString(oldData[i][j]);
				String data_new = HashData.getHashFromString(newData[i][j]);
				if (!(data_old.equals(data_new))) {
					System.out.println("Failed Migration");
					System.out.println(data_old + " not same as " + data_new);
					//newData[i][j]= oldData[i][j];
					//updateData(newData[i][j]);
				}
			}
		}
		System.out.println("CHECK COMPLETE");
	}
	
	public void updateData() {}
}
