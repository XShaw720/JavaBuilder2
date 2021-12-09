import java.io.*;
import java.util.*;

public class JavaBuilder2{
	
	public static void main(String[] args)throws Exception{
		int index=0, indexprev=-1, i, j;							//variable/ Arraylist declaration
		String line, splitter[], var, upperVar, type; 
		ArrayList<Class> classes=new ArrayList<>();
		
		Scanner s = new Scanner(new File(args[0]));	//open input file provided in commandline
		
		while(s.hasNextLine()){	//loops until EOF
			String className=s.nextLine();											//gets class name and position
			if(!className.matches("\t*[a-zA-Z]\\w*")){								//ensures a proper class name has been given
				if(className=="")													//in case there was simply too many newlines before classes
					continue;														//
				System.out.println("Error in class name: " + className);			//displays error message
				return;
			}
			for(index=0; className.charAt(0)=='\t'; index++)				//finds place in hierachy//
				className=className.substring(1);							//removes '\t' that was just counted
			if(index>indexprev+1)											//if theres too many '\t' it will correct index
				index=indexprev+1;											//
			
			while(index!=classes.size())									//removes all irrelevant class
				classes.remove(index);										//
			classes.add(new Class(className));								//add class to list
			
			FileWriter w=new FileWriter(new File(className + ".java"));		//opens file with class name for writting
			
			w.write("public class " + className);								//defines class
			if(index!=0)														//
				w.write(" extends " + classes.get(index-1).className + " ");	//defines it as a subclass if applicable
			w.write("{\n\n");
			
			while(s.hasNextLine()){																	//declares variables//
				line=s.nextLine();																	//
				if(line.equals(""))																	//meaning end of class def
					break;																			//
				if(!line.matches("\t*-?(public|private)( [a-zA-Z]\\w*){2}")){						//ensures proper format
					System.out.println("error in variable definition in class: " + className);		//
					return;																			//
				}																					//
				while(line.charAt(0)=='\t')															//
					line=line.substring(1);															//to remove the '\t's
				splitter=line.split(" ");															//
				if(line.charAt(0)=='-'){															//check if line starts with dash
					line=line.substring(1);															//if so remove it
					classes.get(index).toStringVars.add(splitter[2]);								//and add the variable name to the toString Override
				}																					//
				w.write("\t" + line + ";\n");														//writes variable declaration
				classes.get(index).addVariable(splitter[1], splitter[2]);							//saves type and name for later use
			}
			
			w.write("\n\tpublic " + className + "(){}\n\n");					//creates default constructor
			
			for(i=index; i>=0; i--){									//makes constructors allowing parameters to set each layer of superclass variables//
				w.write("\tpublic " + className + "(");					//
				for(j=index; j>=i; j--){								//
					w.write(classes.get(j)+"");							//writes parameters required
					if(j>i)												//
						w.write(", ");									//
				}														//
				w.write("){\n");										//
				if(i!=index){											//
					w.write("\t\tsuper(");								//calls super to intialize parent's variables
					for(j=index-1; j>=i; j--){							//
						line=classes.get(j).variables+"";				//
						w.write(line.substring(1, line.length()-1));	//provides required parameters
						if(j>i)											//
							w.write(", ");								//
					}													//
					w.write(");\n");									//
				}														//
				for(String temp: classes.get(index).variables)			//
					w.write("\t\tthis." + temp + "=" + temp + ";\n");	//initializes local variables
				w.write("\t}\n\n");										//
			}
			
			for(i=0; i<classes.get(index).variables.size(); i++){																		//creates getters and setters//
				var=classes.get(index).variables.get(i);																				//
				upperVar=var.substring(0,1).toUpperCase() + var.substring(1);															//
				type=classes.get(index).types.get(i);																					//
				w.write("\tpublic void set" + upperVar + "(" + type + " " + var + "){\n\t\tthis." + var + "=" + var + ";\n\t}\n\n");	//setters
				w.write("\tpublic " + type + " get" + upperVar + "(){\n\t\treturn " + var + ";\n\t}\n\n");								//getters
			}
			
			w.write("\t@Override\n\tpublic String toString(){\n\t\treturn ");															//Override toString method//
			if(classes.get(index).toStringVars.size()==0)																				//if no access modifiers started with a dash it wll just return the class name
				w.write("\"" + className + "\"");																						//
			else{																														//
				w.write("\"" + classes.get(index).toStringVars.get(0) + ": \" + " + classes.get(index).toStringVars.get(0));			//otherwise it will return all the ones that did
				for(i=1; i<classes.get(index).toStringVars.size(); i++)																	//
					w.write(" + \", " + classes.get(index).toStringVars.get(i) + ": \" + " + classes.get(index).toStringVars.get(i));	//	
			}																															//
			w.write(";\n\t}\n");																										//
			
			w.write("}");	//close class
			w.close();
			indexprev=index;
		}	//end of current class, loops back if there is another to define
		
		s.close();
	}
}