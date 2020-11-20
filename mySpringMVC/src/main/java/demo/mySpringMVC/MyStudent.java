package demo.mySpringMVC;

import ioc.annotation.Component;
import ioc.annotation.Scope;

@Component("demo.mySpringMVC.MyStudent")
@Scope("prototype")
public class MyStudent {
	private String idString;
    public String getIdString() {
		return idString;
	}
	public void setIdString(String idString) {
		this.idString = idString;
	}
	private int age;
    private String nameString;
    private String classString;
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getNameString() {
		return nameString;
	}
	public void setNameString(String nameString) {
		this.nameString = nameString;
	}
	public String getClassString() {
		return classString;
	}
	public void setClassString(String classString) {
		this.classString = classString;
	}
}
