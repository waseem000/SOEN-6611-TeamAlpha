package metrics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.tree.DefaultMutableTreeNode;
import ast.inheritance.*;

import ast.ClassObject;
import ast.FieldInstructionObject;
import ast.MethodObject;
import ast.SystemObject;
import ast.inheritance.InheritanceDetection;
import ast.inheritance.InheritanceTree;

public class Polymorphism {
	
	private Map<String, Integer> PolymorphismMap;
	private InheritanceDetection DescendedClasses;
	public Polymorphism(SystemObject system) 
	{
		PolymorphismMap = new HashMap<String, Integer>();
		
		Set<ClassObject> classes = system.getClassObjects();
		double NumberOfOverRidingMethods=0;
		double NumberOfNewMethods=0;
		DescendedClasses = new InheritanceDetection(system);
		double DC = 0;
		Double polymorphism = 0;
		
		
		
		for(ClassObject classObject : classes) 
		{
			NumberOfOverRidingMethods+=CheckOverridingMethods(classObject);
			NumberOfNewMethods += CheckONewMethods(classObject);
			NumberOfNewMethods += classObject.getNumberOfMethods() - NumberOfOverRidingMethods;
			NumberOfDescendedClasses (classObject);
			DC += CheckDescendedClasses(classObject);
			
			

		}
		// here we should write the formula for Polymorphism Factor.
		polymorphism = ((NumberOfNewMethods * NumberOfOverRidingMethods) / (NumberOfNewMethods * DC));
		System.out.println(polymorphism);
		
		
	}

	private void NumberOfDescendedClasses(ClassObject classObject) {
		
		InheritanceTree Tree = DescendedClasses.getTree(classObject.getName());
		
		// TODO Auto-generated method stub
		
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
	
	private int CheckDescendedClasses(ClassObject classObject) 
	{
		DefaultMutableTreeNode rootclass;
		int numberOfChildern=0;
		InheritanceTree tree = DescendedClasses.getTree(classObject.getName());
		if(tree!=null)
		{
			 rootclass = tree.getRootNode();
			 numberOfChildern=rootclass.getChildCount();
		}
		return numberOfChildern;
	}
}