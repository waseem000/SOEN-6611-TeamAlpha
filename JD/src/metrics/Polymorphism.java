package metrics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ast.ClassObject;
import ast.FieldInstructionObject;
import ast.MethodObject;
import ast.SystemObject;

public class Polymorphism {
	
	private Map<String, Integer> PolymorphismMap;

	public Polymorphism(SystemObject system) 
	{
		PolymorphismMap = new HashMap<String, Integer>();
		
		Set<ClassObject> classes = system.getClassObjects();
		double NumberOfOverRidingMethods=0;
		double NumberOfNewMethods=0;
		
		for(ClassObject classObject : classes) 
		{
			NumberOfOverRidingMethods+=CheckOverridingMethods(classObject);
			NumberOfNewMethods += CheckONewMethods(classObject);

		}
		// here we should write the formula for Polymorphism Factor.
		
		
		
	}

	private double CheckOverridingMethods(ClassObject classObject) 
	{
		
		List<MethodObject> methods = classObject.getMethodList();
        double countingOverridings=0;
        double countingNewmethods=0;

		for(int i=0; i<methods.size(); i++)
		{
			MethodObject mI = methods.get(i);
			if (mI.overridesMethod() == true)
			{
				countingOverridings+=1;
			}else if(mI.overridesMethod() == false)
			{
				countingNewmethods+=1;
			
			}
		}
			return countingOverridings;
	}
	/// here CheckONewMethods function
	private double CheckONewMethods(ClassObject classObject) 
	{
		
		List<MethodObject> methods = classObject.getMethodList();
        double countingNewmethods=0;

		for(int i=0; i<methods.size(); i++)
		{
			MethodObject mI = methods.get(i);
			 if(mI.overridesMethod() == false)
			{
				countingNewmethods+=1;
			
			}
		}
			return countingNewmethods;
	}
	
	}