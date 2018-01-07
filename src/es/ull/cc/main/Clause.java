package es.ull.cc.main;

public class Clause {
	
	private Component[] components;
	
	public Clause(Component value1, Component value2, Component value3) {
		setComponents(new Component[3]);
		components[0] = value1;
		components[1] = value2;
		components[2] = value3;
	}

	public Component[] getComponents() { return components; }
	private void setComponents(Component[] components) { this.components = components; }
	
	public String toString() {
		return "(" + components[0].getValue() + " ^ " + components[1].getValue() + " ^ " + components[2].getValue() + ")";
	}

}
