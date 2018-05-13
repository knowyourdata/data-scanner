package at.fwd.data_scanner.util.sort;

public class SortProperty {

	public static final int ASCENDING=1;
	public static final int DESCENDING=-1;
	
	private String attribute;
	private int mode;
	private boolean ignoreCase;
	
	public SortProperty() {
		
	}
	
	public SortProperty(String attribute) {
		this(attribute, ASCENDING, false);
	}
	
	public SortProperty(String attribute, int mode) {
		this(attribute, mode, false);
	}
	
	public SortProperty(String attribute, boolean ignoreCase) {
		this(attribute, ASCENDING, ignoreCase);
	}
	
	public SortProperty(String attribute, int mode, boolean ignoreCase) {
		this.attribute = attribute;
		this.mode = mode;
		this.ignoreCase = ignoreCase;
	}
	
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public boolean isIgnoreCase() {
		return ignoreCase;
	}
	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}
}
