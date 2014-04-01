package metrics;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;

import ast.Access;
import ast.ClassObject;
import ast.FieldObject;
import ast.SystemObject;
import ast.TypeObject;

public class AHF {
	private Double AHF;
	private Double total_number_of_classes;
	private SystemObject systemobj;
	private Integer attribute_tested=0;
	private Integer classes_tested=0;
	private Set<ClassObject> system_classes;
	private Integer public_attribute=0; 
	private Integer protected_attribute=0;
	private Integer private_attribute=0;
	private Integer default_attribute=0;
	private ToFile output = new ToFile("AHF","AHF", "5") ;

	public AHF(SystemObject system) throws IOException
	{
		systemobj=system;
		Set<ClassObject> classes =system.getClassObjects();

		total_number_of_classes = (double) system.getClassNumber();
		system_classes=classes;

		Double Attribute_visibility = 0.0;
		int number_of_class=1;
		Integer number_of_attributes=0;
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
				Attribute_visibility=Attribute_visibility + Attribute_Check(classObject);
				number_of_class++;
				number_of_attributes=number_of_attributes+classObject.getFieldList().size();
			}

		}

		System.out.println("number of attribute ="+number_of_attributes);
		print("number of attribute ="+number_of_attributes);
		System.out.println("number of attribute tested ="+attribute_tested);
		print("number of attribute ="+attribute_tested);
		System.out.println("number of classes tested ="+classes_tested +"out of "+ total_number_of_classes);
		print("number of classes tested ="+classes_tested +"out of "+ total_number_of_classes);

		System.out.println("public attributes ="+public_attribute+" of "+number_of_attributes);
		System.out.println("private attributes  ="+private_attribute+" of "+number_of_attributes);
		System.out.println("protected attributes  ="+protected_attribute+" of "+number_of_attributes);
		System.out.println("default attributes ="+default_attribute+" of "+number_of_attributes);

		System.out.println("V(Mmi) ="+Attribute_visibility);
		print("V(Mmi) ="+Attribute_visibility);

		Double temp_AHF =Math.abs(Calculate_AHF(Attribute_visibility,attribute_tested));
		AHF=((temp_AHF/100)*100);
		AHF=1-(AHF);
		AHF =Double.parseDouble(new DecimalFormat("##.###").format(AHF));
		System.out.println("->>>>>>AHF ="+AHF+"%");
		print("->>>>>>AHF ="+AHF+"%");
		csv_print(AHF.toString());
		output.close();

	}

	private Double Calculate_AHF(Double attribute_visibility, Integer numbner_of_attribute_class) {
		// TODO Auto-generated method stub
		Double attribute_hiding_factor;


		attribute_hiding_factor=(double) ((1-attribute_visibility)/numbner_of_attribute_class);
		return attribute_hiding_factor;
	}

	private double  Attribute_Check(ClassObject classObj) 
	{

		Integer _public=0; 
		Integer _protected=0;
		Integer _private=0;
		Integer _default=0;
		Integer temp=0;
		double visible=0;
		
		List<FieldObject> attributes = classObj.getFieldList();
		
		for (int i = 0; i < attributes.size(); i++) 
		{

			temp= isVisible(attributes.get(i));
			if(temp ==1)
			{
				visible=visible+ Attribute_Visibility((double) (classes_tested-1));
				_public= _public+1;
			}
			else if(temp ==3)
			{
				visible=visible+Attribute_Visibility((double)Inheritance_Visibilty(classObj));
				visible= visible+Attribute_Visibility((double)Package_Visibilty(classObj));
				_protected= _protected+1;
			}
			else if (temp==0)
			{
				_private=_private+1;
			}
			else if(temp==4)
			{
				visible= visible+Attribute_Visibility((double)Package_Visibilty(classObj));
				_default=_default+1;
			}

		}
		public_attribute= public_attribute+_public;
		protected_attribute= protected_attribute+_protected;
		private_attribute=private_attribute+_private;
		default_attribute=default_attribute+_default;

		System.out.println("public attributes ="+_public+" of "+attributes.size());
		System.out.println("private attributes ="+_private+" of "+attributes.size());
		System.out.println("protected attributes ="+_protected+" of "+attributes.size());
		System.out.println("default attributes ="+_default+" of "+attributes.size());
		return visible;
	}

	private Integer Package_Visibilty( ClassObject classObj) {

		String name_class = classObj.getName();
		IFile class_file = classObj.getIFile();
		String temp=class_file.getName().replace(class_file.getName().substring(class_file.getName().indexOf('.')), "");
		String package_name= name_class.replaceFirst(temp, "");		
		Integer classes_package=0;
		System.out.println("Package name-->"+package_name);
		System.out.println("attribute in class -->"+class_file.toString());

		for(ClassObject cls : system_classes)
		{
			if((cls.getName().contains(package_name))&& !(cls.equals(classObj)))
			{
				classes_package++;
			}
		}
		return classes_package;
	}

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
				while(superClassObject != null)
				{
					superclasses=superclasses+1;

					superClass =  superClassObject.getSuperclass();
					if(superClass==null)
					{
						break;
					}
					superClassType = superClass.getClassType();
					superClassObject = systemobj.getClassObject(superClassType);

				}
			}
		}
		return superclasses;

	}

	private Double Attribute_Visibility(Double number_of_classes) {

		double attribute_visibility ;
		attribute_visibility = number_of_classes/(classes_tested-1);
		return attribute_visibility;
	}

	private Integer isVisible(FieldObject attributeObj) {


		Access access_type = attributeObj.getAccess();
		String attribute_modifier= access_type.toString();


		if(	attribute_modifier.equals("public")) //public modifier =9
		{
			System.out.println("public");
			attribute_tested++;
			return 1;
		}
		else if(attribute_modifier.equals("private"))//private modifier =10
		{
			System.out.println("private");
			attribute_tested++;
			return 0;
		}
		else if(attribute_modifier.equals("protected")) // the default modifier which allow access to package 
		{
			System.out.println("protected");
			attribute_tested++;
			return 3;
		}
		else 
		{
			System.out.println("defult,"+attribute_modifier);
			attribute_tested++;
			return 4;
		}
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
