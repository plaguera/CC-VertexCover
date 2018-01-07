package es.ull.cc.main;

/**
 * 3SAT Clause Component
 * @author Pedro Miguel Lagüera Cabrera
 * Dec 27, 2017
 * Component.java
 */
public class Component {
	
	private String value;
	private boolean isNegation;
	
	public Component(String value) {
		setValue(value);
		setNegation(getValue().startsWith("!"));
	}

	public String getValue() { return value; }
	private void setValue(String value) { this.value = value; }
	public boolean isNegation() { return isNegation; }
	private void setNegation(boolean isNegation) { this.isNegation = isNegation; }
	public String complementary() { return isNegation() ? getValue().substring(1) : "!" + getValue(); }

}
