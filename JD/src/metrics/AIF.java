package metrics;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;

import ast.Access;
import ast.inheritance.*;
import ast.ClassObject;
import ast.FieldObject;
import ast.MethodObject;
import ast.SystemObject;
import ast.TypeObject;

public class AIF {
	private ToFile output = new ToFile("Attribute Inheritance Factor","AIF","1") ;
	double totalAI = 0 ;
	double totalAA =0;
	Double AIF;
	public  AIF(SystemObject system)
	{
		 Set<ClassObject> classes = system.getClassObjects();
		 for (ClassObject classObject : classes) 
		 {
			 TypeObject superClass_temp =  classObject.getSuperclass();
			 if(superClass_temp!=null)
			 {
				 ClassObject superClass = system.getClassObject(classObject.getSuperclass().getClassType());
				 if( superClass != null)
				 {
					 List<FieldObject> fields = superClass.getFieldList();
					 for (int i=0; i<fields.size();i++)
					 {
						 FieldObject field = fields.get(i);
					 
						 if(field.isStatic())
							 fields.remove(field);
						 if(field.getVariableDeclaration().toString().contains("final"))
							 fields.remove(field);
					 }
					 double numberOfOverridableFields =  fields.size();
					  double Ai = numberOfOverridableFields- CheckOverridingAttributes(classObject,fields);
					  totalAI += Ai;
					  totalAA += classObject.getMethodList().size()- numberOfOverridableFields + Ai;
				 }
			 }
		}
		  AIF= totalAI/totalAA;
		  AIF =Double.parseDouble(new DecimalFormat("##.###").format(AIF));
		  System.out.println("the value of AIF->>>>>>"+AIF);
		 
		  csv_print( AIF.toString());
			try {
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
	}
	
	private void csv_print(String text)
	{
		output.CSVFile_print(text);
	}
	private double CheckOverridingAttributes(ClassObject classObject,List<FieldObject> fields) 
	{
		
		List<FieldObject> classFields = classObject.getFieldList();
        double countingOverridings=0;

		for(int i=0; i<classFields.size(); i++)
		{
			FieldObject fI = classFields.get(i);
			if (fields.contains(fI))
			{
				countingOverridings+=1;
			}
		}
			return countingOverridings;
	}
}
