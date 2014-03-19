package metrics;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.internal.core.PackageFragment;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;

import ast.Access;
import ast.ClassObject;
import ast.MethodInvocationObject;
import ast.MethodObject;
import ast.SystemObject;
import ast.TypeObject;

/*
 * Encapsulation : Method Hiding Factor (MHF)
 */
public class MHF {

	private Double MHF;
	private Double total_number_of_classes;
	private SystemObject systemobj;
	private Integer methods_tested=0;
	private Integer classes_tested=0;
	private Set<ClassObject> system_classes;
	private Integer public_methods=0; 
	private Integer protected_methods=0;
	private Integer private_methods=0;
	private Integer default_methods=0;
	private ToFile output = new ToFile("MHF","MHF", "5") ;
	public MHF(SystemObject system) throws IOException
	{
		systemobj=system;
		Set<ClassObject> classes =system.getClassObjects();
	
		total_number_of_classes = (double) system.getClassNumber();
		system_classes=classes;
		//Double methods_visibility; 
		Double visibility_for_all_classes = 0.0;
		int number_of_class=1;
		Integer number_of_methods=0;
		for(ClassObject classObject : classes)
		{
			if(!classObject.isInterface())
			{
				classes_tested++;
			}

		}
		for(ClassObject classObject : classes)
		{
			if(!classObject.isInterface())
			{

				System.out.println(number_of_class+" - "+classes.size()+"   "+classObject.getName());
				visibility_for_all_classes=visibility_for_all_classes + Methods_Check(classObject);
				number_of_class++;
				number_of_methods=number_of_methods+classObject.getMethodList().size();
			}

		}
		//System.out.println("visible_for_all_classes = "+visibility_for_all_classes);

		/*for(ClassObject classObject : classes)
		{
			number_of_methods=number_of_methods+classObject.getMethodList().size();

		}*/
		System.out.println("number of methods ="+number_of_methods);
		print("number of methods ="+number_of_methods);
		System.out.println("number of methods tested ="+methods_tested);
		print("number of methods ="+methods_tested);
		System.out.println("number of classes tested ="+classes_tested +"out of "+ total_number_of_classes);
		print("number of classes tested ="+classes_tested +"out of "+ total_number_of_classes);
		
		System.out.println("public methods ="+public_methods+" of "+number_of_methods);
		System.out.println("private_methods ="+private_methods+" of "+number_of_methods);
		System.out.println("protected_methods methods ="+protected_methods+" of "+number_of_methods);
		System.out.println("default_methods methods ="+default_methods+" of "+number_of_methods);
		
		System.out.println("V(Mmi) ="+visibility_for_all_classes);
		print("V(Mmi) ="+visibility_for_all_classes);
		//visibility_for_all_classes=visibility_for_all_classes/classes_tested;
		Double temp_MHF =Math.abs(Calculate_MHF(visibility_for_all_classes,methods_tested));
		MHF=((temp_MHF/100)*100);
		MHF=1-(MHF);
		MHF =Double.parseDouble(new DecimalFormat("##.###").format(MHF));
		System.out.println("->>>>>>MHF ="+MHF+"%");
		print("->>>>>>MHF ="+MHF+"%");
		csv_print(MHF.toString());
		output.close();

	}

	private Double Calculate_MHF(Double methods_visibility, Integer numbner_of_methods_class) {
		// TODO Auto-generated method stub
		Double method_hiding_factor;


		method_hiding_factor=(double) ((1-methods_visibility)/numbner_of_methods_class);
		return method_hiding_factor;
	}

	private double Methods_Check(ClassObject classObj) //return V(Mmi) value
	{
		//calculate the value of visible(Mmi,Cj) for a class "classobj"
		Integer _public=0; 
		Integer _protected=0;
		Integer _private=0;
		Integer _default=0;
		Integer temp=0;
		double visible=0;
		List<MethodObject> methods = classObj.getMethodList();

		for (int i = 0; i < methods.size(); i++) 
		{

			temp= isVisible(methods.get(i));
			if(temp ==1)
			{
				visible=visible+ Method_Visibility((double) (classes_tested-1));//(methods.get(i),classObj);//
				_public= _public+1;
			}
			else if(temp ==3)
			{
				visible=visible+Method_Visibility((double)Inheritance_Visibilty(classObj));
				visible= visible+Method_Visibility((double)Package_Visibilty(classObj));
				_protected= _protected+1;
			}
			else if (temp==0)
			{
				_private=_private+1;
			}
			else if(temp==4)
			{
				visible= visible+Method_Visibility((double)Package_Visibilty(classObj));
				_default=_default+1;
			}

		}
		public_methods= public_methods+_public;
		protected_methods= protected_methods+_protected;
		private_methods=private_methods+_private;
		default_methods=default_methods+_default;
		
		System.out.println("public methods ="+_public+" of "+methods.size());
		System.out.println("private_methods ="+_private+" of "+methods.size());
		System.out.println("protected_methods methods ="+_protected+" of "+methods.size());
		System.out.println("default_methods methods ="+_default+" of "+methods.size());
		return visible;
	}

	private Integer Package_Visibilty( ClassObject classObj) {

		String name_class = classObj.getName();
		IFile class_file = classObj.getIFile();
		String temp=class_file.getName().replace(class_file.getName().substring(class_file.getName().indexOf('.')), "");
		String package_name= name_class.replaceFirst(temp, "");		
		Integer classes_package=0;
		System.out.println("Package name-->"+package_name);
		System.out.println("method in class -->"+class_file.toString());

		for(ClassObject cls : system_classes)
		{
			//	System.out.println(cls.getName()+"    "+cls.getIFile().toString());
			if((cls.getName().contains(package_name))&& !(cls.equals(classObj)))
			{
				classes_package++;
			}
		}


		return classes_package;
	}

	/*private double Method_Visibility(MethodObject methodObject,ClassObject classObj) {
		// TODO Auto-generated method stub
		MethodInvocationObject methodsinv = methodObject.generateMethodInvocation();
		Integer num=0;
		if (systemobj.containsMethodInvocation(methodsinv, classObj))
		{
			num++;
			//return 1;
		}

		return num;
	}*/

	private Integer Inheritance_Visibilty( ClassObject classObj) {


		Integer superclasses =0;
	
		TypeObject superClass =  classObj.getSuperclass();
		if(superClass!=null)
		{

			String superClassType = superClass.getClassType();
			ClassObject superClassObject = systemobj.getClassObject(superClassType);

			if(superClassObject==null)
			{
				return superclasses;
			}
			else
			{
				while(superClassObject/*.getSuperclass()*/ != null)
				{
					superclasses=superclasses+1;

					superClass =  superClassObject.getSuperclass();
					if(superClass==null)
					{
						break;
					}
					superClassType = superClass.getClassType();
					superClassObject = systemobj.getClassObject(superClassType);

					/*if(superClassObject==null)
					{
						break;
					}*/
				}
			}
		}
		return superclasses;

	}

	private Double Method_Visibility(Double number_of_classes) {

		double methods_visibility ;
		methods_visibility = number_of_classes/(classes_tested-1);
		return methods_visibility;
	}

	private Integer isVisible(MethodObject methodObj) {

		MethodDeclaration signature;
		Access access = methodObj.getAccess();
		signature=methodObj.getMethodDeclaration();
		int modifier =signature.getModifiers();
		List modifiers_list= signature.modifiers();
		List modifiers_sublist=modifiers_list.subList(0, modifiers_list.size());

		String method_modifier;

		if(modifiers_sublist.size()==0) // if their is no modifier it is considered public
		{
			methods_tested++;
			return 4;
		}
		else
		{
			for(int i=0; i<modifiers_sublist.size(); i++)
			{
				method_modifier=modifiers_sublist.get(i).toString();


				if(	method_modifier.equals("public")) //public modifier =9
				{
					System.out.println("public");
					methods_tested++;
					return 1;
				}
				else if(method_modifier.equals("private"))//private modifier =10
				{
					System.out.println("private");
					methods_tested++;
					return 0;
				}
				else if(method_modifier.equals("protected")) // the default modifier which allow access to package 
				{
					System.out.println("protected");
					methods_tested++;
					return 3;//1;
				}
				else if(!(method_modifier.startsWith("@"))&&!(method_modifier.equals("final")||method_modifier.equals("abstract")||method_modifier.equals("static")||method_modifier.equals("synchronized"))) // the default modifier which allow access to package 
				{
					System.out.println("defult,"+method_modifier);
					methods_tested++;
					return 4;
				}
				if((method_modifier.equals("final")||method_modifier.equals("abstract")||method_modifier.equals("static")||method_modifier.equals("synchronized"))&& (modifiers_sublist.size()==1 || i==modifiers_sublist.size()-1))
				{
					System.out.println("defult,"+method_modifier);
					methods_tested++;
					return 4;
				}
				if(method_modifier.startsWith("@") && i==modifiers_sublist.size()-1)
				{
					methods_tested++;
					System.out.println("defult,"+method_modifier);
					return 4;
				}
			}
		}
		System.out.println(methodObj.getClassName()+"  -error outside the loop");
		return -1; //anything that is not modifier


	}
	
	private void print (String text) throws IOException
	{
		output.TextFile_print(text);
	}
	private void csv_print(String text)
	{
		output.CSVFile_print(text);
	}

}
