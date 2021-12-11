package cadProject.networkTopologyMatrices;

import java.util.ArrayList;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Node {
	private List<Branch>treeBranches;
	private List<Branch>linkBranches;
	public Node() {
		// TODO Auto-generated constructor stub
		treeBranches=new ArrayList<Branch>();
		linkBranches=new ArrayList<Branch>();
	}

}
