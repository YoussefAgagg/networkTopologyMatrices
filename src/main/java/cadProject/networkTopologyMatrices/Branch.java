package cadProject.networkTopologyMatrices;


public class Branch {
	private Node fromNode;
	private Node toNode;
	private boolean isLink;
	private double voltage;
	private double current;
	private double impedance;
	private boolean isAdmittance;
	private String branchName;

	@Override
	public String toString() {
		return "Branch{" +
				"isLink=" + isLink +
				", voltage=" + voltage +
				", current=" + current +
				", impedance=" + impedance +
				", isAdmittance=" + isAdmittance +
				", branchName='" + branchName + '\'' +
				'}';
	}

	public Node getFromNode() {
		return fromNode;
	}

	public void setFromNode(Node fromNode) {
		this.fromNode = fromNode;
	}

	public Node getToNode() {
		return toNode;
	}

	public void setToNode(Node toNode) {
		this.toNode = toNode;
	}

	public boolean isLink() {
		return isLink;
	}

	public void setLink(boolean link) {
		isLink = link;
	}

	public double getVoltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}

	public double getCurrent() {
		return current;
	}

	public void setCurrent(double current) {
		this.current = current;
	}

	public double getImpedance() {
		return impedance;
	}

	public void setImpedance(double impedance) {
		this.impedance = impedance;
	}

	public boolean isAdmittance() {
		return isAdmittance;
	}

	public void setAdmittance(boolean admittance) {
		isAdmittance = admittance;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
}
