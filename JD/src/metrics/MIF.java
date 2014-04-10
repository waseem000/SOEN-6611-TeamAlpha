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

public class MIF {
	
	private ToFile output = new ToFile("Method Inheritance Factor","MIF","1") ;
	double totalMI = 0 ;
	double totalMA =0;
	Double MIF;
	public  MIF(SystemObject system)
	{
		 Set<ClassObject> classes = system.getClassObjects();
		 for (ClassObject classObject : classes) 
		 {
			 ClassObject superClass = system.getClassObject(classObject.getSuperclass().getClassType());
			 if( superClass != null)
			 {
				 List<MethodObject> methods = superClass.getMethodList();
				 for (MethodObject method : methods)
				 {
					 if(method.isStatic())
						 methods.remove(method);
					 if(method.getSignature().contains("final"))
						 methods.remove(method);
				 }
				 double numberOfOverridableMethods =  methods.size();
				  double Mi = numberOfOverridableMethods- CheckOverridingMethods(classObject);
				  totalMI += Mi;
				  totalMA += classObject.getMethodList().size()- numberOfOverridableMethods + Mi;
			 }
		}
		  MIF= totalMI/totalMA;
		 MIF =Double.parseDouble(new DecimalFormat("##.###").format(MIF));
		  System.out.println("the value of MIF->>>>>>"+MIF);
		  String Convertor= MIF.toString();
		  csv_print(Convertor);
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
	
	private double CheckOverridingMethods(ClassObject classObject) 
	{
		
		List<MethodObject> methods = classObject.getMethodList();
        double countingOverridings=0;

		for(int i=0; i<methods.size(); i++)
		{
			MethodObject mI = methods.get(i);
			if (mI.overridesMethod())
			{
				countingOverridings+=1;
			}
		}
			return countingOverridings;
	}
}
