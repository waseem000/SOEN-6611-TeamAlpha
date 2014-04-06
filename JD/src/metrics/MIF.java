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
import ast.SystemObject;
import ast.TypeObject;

public class MIF {
	
	
	public  MIF(SystemObject system)
	{
		 Set<ClassObject> classes = system.getClassObjects();
		 InheritanceDetection inheritanceDetection = new InheritanceDetection(system);
		 for (ClassObject classObject : classes) {
			 InheritanceTree inheritanceTree = inheritanceDetection.getTree(classObject.getName());
			 list< 
		}
	}
}
