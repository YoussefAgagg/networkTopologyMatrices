package cadProject.networkTopologyMatrices;

import lombok.Data;
import lombok.ToString;


@Data
public class Branch {
	@ToString.Exclude
	private Node fromNode;
	@ToString.Exclude
	private Node toNode;
	private boolean isLink;
	private double voltage;
	private double current;
	private double impedance;
	private boolean isAdmittance;
	private String branchName;
}
