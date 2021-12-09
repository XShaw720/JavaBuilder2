import java.util.ArrayList;

public class Class{
	
	public String className;
	public ArrayList<String> types=new ArrayList<>();
	public ArrayList<String> variables=new ArrayList<>();
	public ArrayList<String> toStringVars=new ArrayList<>();

	public Class(){}
	
	public Class(String className){
		this.className=className;
	}
	
	public void addVariable(String type, String variable){
		types.add(type);
		variables.add(variable);
	}
	
	@Override
	public String toString(){
		String temp="";
		for(int i=0; i<types.size(); i++){
			temp+=  types.get(i) + " " + variables.get(i);
			if(i<types.size()-1)
				temp+=", ";
		}
		return temp;
	}
}