package cadProject.networkTopologyMatrices;

import java.text.DecimalFormat;
import java.util.List;

import org.ejml.simple.SimpleMatrix;


public class CutTieSetMatrix {
	private DecimalFormat decimalFormat=new DecimalFormat("#.####");
	
	
	public String useTieSet(double[]B,int branches,double[]Zb,double[]Ib,double[]Eb,List<String>branchesName){
		System.out.println("--------tie set---------");
		var BMatrix=new SimpleMatrix(B.length/branches,branches,true,B);
		var BTranspose=BMatrix.transpose();
		var ZbMatrix=new SimpleMatrix(branches,branches,true,Zb);
		var IbMatrix=new SimpleMatrix(branches,1,true,Ib);
		var EbMatrix=new SimpleMatrix(branches,1,true,Eb);

		//the equation is  B*Zb*BTranspose*Il=B*(Eb-Zb*Ib)
		var rightSide=BMatrix.mult(EbMatrix.minus(ZbMatrix.mult(IbMatrix)));//B*(Eb-Zb*Ib)
		var leftSide=BMatrix.mult(ZbMatrix).mult(BTranspose);//B*Zb*BTranspose
		var Il=leftSide.invert().mult(rightSide);//calculate Il
		var Jb=BTranspose.mult(Il);//Jb=BTranspose*Il
		var Vb=ZbMatrix.mult(Jb.plus(IbMatrix)).minus(EbMatrix);//Vb=Zb*(Jb+Ib)-Eb
		System.out.println(branchesName);
		System.out.println("--------B Matrix---------");
		BMatrix.print();
		System.out.println("--------Zb Matrix---------");
		ZbMatrix.print();
		System.out.println("--------Ib Matrix---------");
		IbMatrix.print();
		System.out.println("--------Eb Matrix---------");
		EbMatrix.print();
		System.out.println("--------Il Matrix---------");
		Il.print();
		System.out.println("--------Jb Matrix---------");
		Jb.print();
		System.out.println("--------Vb Matrix---------");
		Vb.print();
		System.out.println("--------end tie set---------");
		leftSide.print();
		rightSide.print();
		return formatOutput(Jb, Vb,branchesName);
	}
	public String useCutSet(double[]C,int branches,double[]Yb,double[]Ib,double[]Eb,List<String>branchesName){
		System.out.println("------------cut set--------------");
		var CMatrix=new SimpleMatrix(C.length/branches,branches,true,C);
		var CTranspose=CMatrix.transpose();
		var YbMatrix=new SimpleMatrix(branches,branches,true,Yb);
		var IbMatrix=new SimpleMatrix(branches,1,true,Ib);
		var EbMatrix=new SimpleMatrix(branches,1,true,Eb);
		
		//the equation is C*Yb*CTranspose*En=C*(Ib-Yb*Eb)
		var rightSide=CMatrix.mult(IbMatrix.minus(YbMatrix.mult(EbMatrix)));//C*(Ib-Yb*Eb)
		var leftSide=CMatrix.mult(YbMatrix).mult(CTranspose);//C*Yb*CTranspose
		var En=leftSide.invert().mult(rightSide);//calculate En
		var Vb=CTranspose.mult(En);//Vb=CTranspose*En
		var Jb=YbMatrix.mult(Vb.plus(EbMatrix)).minus(IbMatrix);//Jb=Yb*(Vb+Eb)-Ib
		System.out.println(branchesName);
		System.out.println("--------C Matrix---------");
		CMatrix.print();
		System.out.println("--------Yb Matrix---------");
		YbMatrix.print();
		System.out.println("--------Ib Matrix---------");
		IbMatrix.print();
		System.out.println("--------Eb Matrix---------");
		EbMatrix.print();
		System.out.println("--------En Matrix---------");
		En.print();
		System.out.println("--------Vb Matrix---------");
		Vb.print();
		System.out.println("--------Jb Matrix---------");
		Jb.print();
		System.out.println("--------end cut set---------");
		leftSide.print();
		rightSide.print();
		return formatOutput(Jb, Vb,branchesName);
	}
	private String formatOutput(SimpleMatrix Jb,SimpleMatrix Vb,List<String>branchesName) {
		var sb=new StringBuilder("current: ");
		sb.append(System.lineSeparator());
		for(int i=0;i<Jb.numRows()*Jb.numCols();i++) {
			
			sb.append("I"+branchesName.get(i)+"=");
			sb.append(decimalFormat.format(Jb.get(i)));
			sb.append(System.lineSeparator());
			
		}
		sb.append("voltage: ");
		sb.append(System.lineSeparator());
		for(int i=0;i<Vb.numRows()*Vb.numCols();i++) {
			sb.append("V"+branchesName.get(i)+"=");
			sb.append(decimalFormat.format(Vb.get(i)));
			sb.append(System.lineSeparator());
			
		}
		
		return sb.toString();
	}

}
