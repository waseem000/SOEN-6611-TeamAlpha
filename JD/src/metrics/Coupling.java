package metrics;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.MethodInvocation;

import ast.ClassObject;
import ast.FieldInstructionObject;
import ast.MethodInvocationObject;
import ast.MethodObject;
import ast.SystemObject;
import ast.FieldObject;

public class Coupling { // class start here !!
private Map<String, Integer> CouplingMap; // create Map for CouplingFactorMap.
private ToFile output = new ToFile("Coupling","CF","1") ;

public Coupling(SystemObject system) 
{ // Constructor start here !!
	CouplingMap = new HashMap<String, Integer>();	
	Set<ClassObject> classes = system.getClassObjects();
	double computeCoupling =0;
	Double CouplingFactor;
	List<String> classesNames = system.getClassNames();
	double TotalNumberOfclasses = system.getClassNumber();
	for(ClassObject classObject : classes) 
	   {
		//Attributes_Check(classObject);
		computeCoupling += Methods_Check(classObject, classesNames)+ Attributes_Check(classObject);
	
	   }
	CouplingFactor = computeCoupling/(Math.pow(TotalNumberOfclasses,2.0)-TotalNumberOfclasses);
	CouplingFactor =Double.parseDouble(new DecimalFormat("##.###").format(CouplingFactor));
	System.out.println(""+CouplingFactor);
	csv_print(CouplingFactor.toString());
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
 
private int Attributes_Check(ClassObject classObject) 
{
	int CountingVariable=0;
	List<String> classesNames = new ArrayList<String>();
	classesNames.add(classObject.getName());
	List<MethodObject> methods= classObject.getMethodList();
	for(int i=0; i<methods.size(); i++) 
	{
		MethodObject mI = methods.get(i);
		List<FieldInstructionObject> attributesI= mI.getFieldInstructions();
		for(int j=0;j<attributesI.size();j++)
		{
			FieldInstructionObject attributeI = attributesI.get(j);
			if (classObject.getName()!= attributeI.getOwnerClass())
			{
				CountingVariable+=1;
				classesNames.add(attributeI.getOwnerClass());
			}
			
		}
	}
	return CountingVariable;
}


private int Methods_Check(ClassObject classObject, List<String> systemClassesNames) 
{
	
	List<MethodObject> methods= classObject.getMethodList();
	int coupling =0;
	List<String> classesNames = new ArrayList<String>();
	classesNames.add(classObject.getName());
	for(int i=0; i<methods.size(); i++) 
	{
		MethodObject mI = methods.get(i);
		mI.getUsedLocalVariables();
		List<MethodInvocationObject> MethodInvocations= mI.getMethodInvocations();
		for(int j=0;j<MethodInvocations.size();j++)
		{
			
		MethodInvocationObject MethodInvocationI = MethodInvocations.get(j);
		if((!classesNames.contains(MethodInvocationI.getOriginClassName()))&& systemClassesNames.contains(MethodInvocationI.getOriginClassName()))
		{
		  coupling+=1;
		  classesNames.add(MethodInvocationI.getOriginClassName());
		}
		//System.out.println("The class name is: "+classObject.getName()+":="+"method name is: "+mI.getName()+" :Origin Class Name is  "+MethodInvocationI.getOriginClassName());
		
		}
		
	}
	return coupling;
} 



}//class end here !!
