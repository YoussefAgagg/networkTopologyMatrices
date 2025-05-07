package cadProject.networkTopologyMatrices;

import java.util.ArrayList;

import java.util.List;

public class Node {
	private List<Branch>treeBranches;
	private List<Branch>linkBranches;
	public Node() {
		// TODO Auto-generated constructor stub
		treeBranches=new ArrayList<Branch>();
		linkBranches=new ArrayList<Branch>();
	}

	@Override
	public String toString() {
		return "Node{" +
				"treeBranches=" + treeBranches +
				", linkBranches=" + linkBranches +
				'}';
	}

	public List<Branch> getTreeBranches() {
		return treeBranches;
	}

	public List<Branch> getLinkBranches() {
		return linkBranches;
	}
}
