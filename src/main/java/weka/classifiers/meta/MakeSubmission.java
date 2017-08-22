package weka.classifiers.meta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import weka.classifiers.SingleClassifierEnhancer;
import weka.core.BatchPredictor;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.OptionMetadata;


public class MakeSubmission extends SingleClassifierEnhancer {

	/** For serialization 	 **/
	private static final long serialVersionUID = 4105475391588293683L;

	/** The attribute id index **/
	protected int m_id = 1;


	/** The output file to save predictions  */
	protected File m_outFile = new File("-- set me --");


	/**
	 * Returns a string describing this filter.
	 * 
	 * @return a description of the filter suitable for displaying in the
	 *         explorer/experimenter gui
	 */	
	public String globalInfo() {
		return "Class for saving the predictions made by a classifier into a file.\n";
	}


	@Override
	public void buildClassifier(Instances data) throws Exception {
		m_Classifier.buildClassifier(data);


	}


	public boolean implementsMoreEfficientBatchPrediction() {
		return true; // So that WEKA will use distributionsForInstances() rather than distributionForInstance()
	}


	public double[][] distributionsForInstances(Instances data) throws Exception {

		PrintWriter pw = new PrintWriter(new FileOutputStream(
				this.m_outFile, 
				true /* append = true */));


		double[][] distributions =  ((BatchPredictor)m_Classifier).distributionsForInstances(data);


		int i=0;
		for(Instance inst:data){

			pw.print((int)inst.value(this.m_id-1)+ "\t");

			for(int j=0;j<distributions[i].length;j++){
				pw.print(distributions[i][j]);
				if(j<distributions[i].length-1)
					pw.print("\t");
				else
					pw.println();				
			}




			i++;

		}



		pw.close();


		return distributions;
	}


	@OptionMetadata(
			displayName = "outFile",
			description = "The file to save the predictions.",
			commandLineParamName = "outFile", commandLineParamSynopsis = "-outFile <string>",
			displayOrder = 1)
	public File getOutFile() { return m_outFile; }
	public void setOutFile(File lexiconFile) { m_outFile = lexiconFile; }



	@OptionMetadata(
			displayName = "id",
			description = "The id attribute index (starting from 1).",
			commandLineParamName = "id", commandLineParamSynopsis = "-id <string>",
			displayOrder = 1)
	public int getId() {
		return m_id;
	}
	public void setId(int m_idAtt) {
		this.m_id = m_idAtt;
	}


}