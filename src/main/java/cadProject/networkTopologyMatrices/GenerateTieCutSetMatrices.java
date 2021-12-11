package cadProject.networkTopologyMatrices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.concurrent.Task;


public class GenerateTieCutSetMatrices extends Task<String>{
	private List<Branch>branchs;
	private List<Node>nodes;
	private boolean useTieSet;
	private List<Branch>Branchesrder;
	private CutTieSetMatrix calVoltagesAndCurrents;
	private List<Branch> links;
	private List<String>branchesName;


	public GenerateTieCutSetMatrices(List<Branch> branchs, List<Node> nodes, boolean useTieSet) {

		this.branchs = branchs;
		this.nodes = nodes;
		this.useTieSet = useTieSet;
		Branchesrder=branchs.stream().filter(b->!b.isLink())
				.collect(Collectors.toList());		
		links=branchs.stream().filter(b->b.isLink())
				.collect(Collectors.toList());

		Branchesrder.addAll(links);

		calVoltagesAndCurrents=new CutTieSetMatrix();
		branchesName=Branchesrder.stream()
				.map(b->b.getBranchName())
				.collect(Collectors.toList());

	}
	public boolean loopBranchesValues(Node start,Node end,Map<Branch,Integer>branches) {
		if(start==end)return true;
		List<Branch>list=start.getTreeBranches();
		boolean loop=false;
		for(Branch b:list) {
			if(branches.containsKey(b))continue;
			int value=1;
			Node n=null;
			if(b.getFromNode()==start) {
				n=b.getToNode();
			}else {
				n=b.getFromNode();
				value=-1;
			}
			branches.put(b,value);			
			loop=loopBranchesValues(n, end, branches);
			if(loop)break;
			else branches.remove(b);
		}
		return loop;
	}
	public void cutSetBranchesValues(Node n,Map<Branch,Integer>branchMap) {
		if(n.getTreeBranches().size()!=1)throw new  UnsupportedOperationException();
		Branch b=n.getTreeBranches().get(0);
		branchMap.put(b, 1);
		for(Branch l:n.getLinkBranches()) {
			if(l.getFromNode()==b.getFromNode()||l.getToNode()==b.getToNode()) {
				branchMap.put(l, 1);
			}else {
				branchMap.put(l, -1);
			}
		}
	}
	@Override
	protected String call() throws Exception {
		if(useTieSet) {
			return tieSet();
		}
		return cutSet();
	}
	private String cutSet() {
		double []C=getCutSetMatrix();
		double []Yb=getZorYMatrix();
		double []Ib=getIbMatrix();
		double[]Eb=getEbMatrix();
		String result=calVoltagesAndCurrents.useCutSet(C, branchs.size(), Yb, Ib, Eb,branchesName);
		return result;
	}
	private double[] getCutSetMatrix() {
		int size=branchs.size();
		List<Node>cutNodes=nodes.stream()
				.filter(n->n.getTreeBranches().size()==1)
				.collect(Collectors.toList());
		double[]C=new double[size*cutNodes.size()];
		if(branchs.size()-links.size()!=cutNodes.size())throw new UnsupportedOperationException("the number of cuts should be equals to the twigs");
		int i=0;
		for(Node n:cutNodes) {
			Map<Branch,Integer>map=new HashMap<>();

			cutSetBranchesValues(n, map);

			for(Branch branch:Branchesrder) {
				C[i++]=map.getOrDefault(branch, 0);
			}


		}
		return C;
	}
	private String tieSet() {
		double []B=getTieSetMatrix();
		double []Zb=getZorYMatrix();
		double []Ib=getIbMatrix();
		double[]Eb=getEbMatrix();
		String result=calVoltagesAndCurrents.useTieSet(B, branchs.size(), Zb, Ib, Eb,branchesName);
		return result;
	}
	private double[] getEbMatrix() {
		int size=branchs.size();
		double[]Eb=new double[size];
		int i=0;
		for(Branch b:Branchesrder) {
			Eb[i++]=b.getVoltage();

		}
		return Eb;
	}
	private double[] getIbMatrix() {
		int size=branchs.size();
		double[]Ib=new double[size];
		int i=0;
		for(Branch b:Branchesrder) {
			Ib[i++]=b.getCurrent();

		}
		return Ib;
	}
	private double[] getZorYMatrix() {
		int size=branchs.size();
		double[]Z=new double[size*size];
		int i=0;
		for(Branch b:Branchesrder) {
			var r=b.getImpedance();
			if(!useTieSet) {
				if(!b.isAdmittance()) {
					r=1.0/r;
				}
			}else {
				if(b.isAdmittance()) {
					r=1.0/r;
				}
			}

			Z[i+i*size]=r;
			i++;
		}
		return Z;
	}
	private double[] getTieSetMatrix() {
		int size=branchs.size();

		double[]B=new double[size*links.size()];
		int i=0;
		for(Branch b:links) {
			Map<Branch,Integer>map=new HashMap<>();
			map.put(b, 1);
			boolean vaildLoop=loopBranchesValues(b.getToNode()
					, b.getFromNode(), map);
			if(!vaildLoop)throw new UnsupportedOperationException("draw valid graph");
			for(Branch branch:Branchesrder) {
				B[i++]=map.getOrDefault(branch, 0);
			}


		}
		return B;
	}


}
