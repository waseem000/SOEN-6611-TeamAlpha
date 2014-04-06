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
	
	double totalMI = 0 ;
	double totalMA =0;
	public  MIF(SystemObject system)
	{
		 Set<ClassObject> classes = system.getClassObjects();
		 InheritanceDetection inheritanceDetection = new InheritanceDetection(system);
		 for (ClassObject classObject : classes) {
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
		 
	}
	
	private int CheckOverridingMethods(ClassObject classObject) 
	{
		
		List<MethodObject> methods = classObject.getMethodList();
        double countingOverridings=0;
        double countingNewmethods=0;

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
