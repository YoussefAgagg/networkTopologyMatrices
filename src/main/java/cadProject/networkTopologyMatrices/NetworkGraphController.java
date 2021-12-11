package cadProject.networkTopologyMatrices;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javafx.scene.shape.QuadCurve;

public class NetworkGraphController {

	@FXML
	private RadioButton nodeRadioButton;

	@FXML
	private ToggleGroup graphElementsRadioButtons;

	@FXML
	private RadioButton branchRadioButton;

	@FXML
	private RadioButton linkRadioButton;

	@FXML
	private Pane graphPaneContainer;

	@FXML
	private TextArea resultTextArea;

	private Point2D branchStartedPoint;
	private Point2D branchEndedPoint;

	private List<javafx.scene.Node>paneNodes;
	private GraphElement graphElementSelected=GraphElement.NODE;

	private Circle moveCircle;

	private QuadCurve qudraticCurve;
	private Label branchLable;
	private char letter='a';

	private List<Node>nodes=new ArrayList<Node>();
	private List<Branch>branches=new ArrayList<Branch>();

	private Branch branch;
	private Arrow arrow;
	private final double[] arrowShape = new double[] { 0,0,5,10,-5,10 };
	private ExecutorService executorService=Executors.newSingleThreadExecutor();
	public void initialize() {
		paneNodes=new ArrayList<>();
		nodeRadioButton.setUserData(GraphElement.NODE);
		branchRadioButton.setUserData(GraphElement.BRANCH);
		linkRadioButton.setUserData(GraphElement.LINK);
		moveCircle=new Circle(7);
		moveCircle.setFill(Color.RED);
		moveCircle.setOnMouseDragged(e->{
			moveCircle.setCenterX(e.getX());
			moveCircle.setCenterY( e.getY());
			qudraticCurve.setControlX(e.getX());
			qudraticCurve.setControlY(e.getY());
			branchLable.setLayoutX(e.getX());
			branchLable.setLayoutY(e.getY());
			arrow.update();
		});
		moveCircle.setVisible(false);
		graphPaneContainer.getChildren().add(moveCircle);
		resultTextArea.managedProperty().bind(resultTextArea.visibleProperty());

	}
	@FXML
	void clearButtonClicked(ActionEvent event) {
		letter='a';
		moveCircle.setVisible(false);
		nodes.clear();
		branches.clear();
		paneNodes.clear();
		graphElementSelected=GraphElement.NODE;
		nodeRadioButton.setSelected(true);
		graphPaneContainer.getChildren().clear();;
		graphPaneContainer.getChildren().add(moveCircle);
		branchStartedPoint=branchEndedPoint=null;
		resultTextArea.setVisible(false);

	}

	@FXML
	void cutSetButttonClicked(ActionEvent event) {

		if(validateGraph()) {
			Task<String>task=new GenerateTieCutSetMatrices(branches, nodes, false);
			task.setOnSucceeded(v->{
				resultTextArea.setVisible(true);
				resultTextArea.setText(task.getValue());
			});
			task.setOnFailed(e->{
				showErrorDialog(task.getException());
				//task.getException().printStackTrace();
			});
			executorService.execute(task);
		}else {
			showErrorDialog(new Exception("draw a valid graph"));
		}

	}

	@FXML
	void newtworkElementRadioButtonSelected(ActionEvent event) {
		graphElementSelected= (GraphElement) graphElementsRadioButtons.getSelectedToggle().getUserData();
	}

	@FXML
	void paneContainerMousePressed(MouseEvent event) {
		if(graphElementSelected==GraphElement.NODE) {   		
			Circle c=new Circle(5);
			Node node=new Node();
			nodes.add(node);
			c.setCenterX(event.getX());
			c.setCenterY(event.getY());

			paneNodes.add(c);

			c.setOnMousePressed(e->{
				if(graphElementSelected!=GraphElement.NODE) {
					c.setStroke(Color.BLUE);
					if(branchStartedPoint==null) {
						branchStartedPoint=new Point2D(c.getCenterX(), c.getCenterY());
						branch=new Branch();
						branch.setFromNode(node);
					}else  {
						for(int i=0;i<paneNodes.size();i++) {
							if(paneNodes.get(i) instanceof Circle) {
								Circle circle=(Circle) paneNodes.get(i);
								circle.setStroke(null);
							}

						}

						branchEndedPoint=new Point2D(c.getCenterX(), c.getCenterY());
						if(!branchEndedPoint.equals(branchStartedPoint)) {
							branch.setToNode(node);
							branchData();
							if(graphElementSelected==GraphElement.BRANCH) {

								drawBranch(branchStartedPoint, branchEndedPoint,false);


							}
							else if(graphElementSelected==GraphElement.LINK) {
								branch.setLink(true);
								drawBranch(branchStartedPoint, branchEndedPoint,true);


							}

						}
						branchEndedPoint=null;
						branchStartedPoint=null;

					}
				}
			});
			graphPaneContainer.getChildren().add(c);

		}

	}

	private void drawBranch(Point2D p1, Point2D p2, boolean isDashed) {
		moveCircle.setVisible(true);
		//Drawing a quadratic curve
		qudraticCurve = new QuadCurve();
		branchLable=new Label(letter++ +"");
		paneNodes.add(qudraticCurve);
		paneNodes.add(branchLable);
		
		qudraticCurve.setStartX(p1.getX());
		qudraticCurve.setStartY(p1.getY());


		Point2D mid=p1.midpoint(p2);


		moveCircle.setCenterX(mid.getX());
		moveCircle.setCenterY( mid.getY());
		branchLable.setLayoutX(mid.getX());
		branchLable.setLayoutY(mid.getY());

		double x=p1.getX();
		double y=p1.getY();
		if(isDashed)
			qudraticCurve.getStrokeDashArray().addAll(2d);
		
		qudraticCurve.setControlX(x);
		qudraticCurve.setControlY(y);
		qudraticCurve.setEndX(p2.getX());
		qudraticCurve.setEndY(p2.getY());
		//Setting other properties
		qudraticCurve.setFill(null);
		qudraticCurve.setStroke(Color.BLACK);

		arrow=new Arrow(qudraticCurve, 1f,arrowShape);
		paneNodes.add(arrow);
		graphPaneContainer.getChildren().add(0,arrow);
		graphPaneContainer.getChildren().add(0,qudraticCurve);
		graphPaneContainer.getChildren().add(0,branchLable);


	}

	private void branchData() {
		Dialog<Void> dialog = new Dialog<>();
		dialog.setTitle("Branch Sources values");
		dialog.setHeaderText("Please specify…");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK);
		TextField currentTextField = new TextField();
		currentTextField.setPromptText("branch current");
		TextField voltageTextField = new TextField();
		voltageTextField.setPromptText("branch voltage");
		TextField resistanceTextField = new TextField();
		resistanceTextField.setPromptText("branch Impedance or admittance");

		dialogPane.setContent(new VBox(8, currentTextField, voltageTextField, resistanceTextField));
		
		final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);

		btOk.addEventFilter(
				ActionEvent.ACTION, 
				event -> {
					try {
						var current=0d;
						if(!currentTextField.getText().isBlank())
							current=Double.parseDouble(currentTextField.getText());
						var voltage=0d;
						if(!voltageTextField.getText().isBlank())
							voltage=Double.parseDouble(voltageTextField.getText());
						var impedance=0d;
						if(!resistanceTextField.getText().isBlank()) {
							String s=resistanceTextField.getText();
							if(s.endsWith("s")) {
								s=s.substring(0, s.indexOf("s"));
								branch.setAdmittance(true);
							}
							impedance=Double.parseDouble(s);
						}
						branch.setCurrent(current);
						branch.setVoltage(voltage);
						branch.setImpedance(impedance);
						branch.setBranchName(branchLable.getText());
						if(branch.isLink()) {
							branch.getFromNode().getLinkBranches().add(branch);
							branch.getToNode().getLinkBranches().add(branch);
						}else {
							branch.getFromNode().getTreeBranches().add(branch);
							branch.getToNode().getTreeBranches().add(branch);
						}
						branches.add(branch);
					}catch (Exception e) {
						
						showErrorDialog(e);
						event.consume();

					}


				}
				);
		dialog.setOnCloseRequest(e->{
			//remove the branch;
			if(branch.getBranchName()==null) {
				moveCircle.setVisible(false);
				removeElement(true);
			}


		});
		dialog.show();

	}
	private void removeElement(boolean isBranch) {
		if(!paneNodes.isEmpty()) {
			if(isBranch) {
				letter--;
				graphPaneContainer.getChildren().remove(paneNodes.remove(paneNodes.size()-1));
				graphPaneContainer.getChildren().remove(paneNodes.remove(paneNodes.size()-1));
			}
			graphPaneContainer.getChildren().remove(paneNodes.remove(paneNodes.size()-1));
		}
	}
	@FXML
	void tieSetButtonClicked(ActionEvent event) {
		if(validateGraph()) {
			Task<String>task=new GenerateTieCutSetMatrices(branches, nodes, true);
			task.setOnSucceeded(v->{

				resultTextArea.setVisible(true);
				resultTextArea.setText(task.getValue());
			});
			task.setOnFailed(e->{
				showErrorDialog(task.getException());
			});
			executorService.execute(task);
		}else {
			showErrorDialog(new Exception("draw a valid graph"));
		}
	}

	@FXML
	void undoButtonClicked(ActionEvent event) {
		moveCircle.setVisible(false);
		if(!paneNodes.isEmpty()) {
			if(paneNodes.get(paneNodes.size()-1)instanceof Arrow) {
				removeElement(true);
				Branch b=branches.remove(branches.size()-1);
				if(b.isLink()) {
					b.getFromNode().getLinkBranches().remove(b);
					b.getToNode().getLinkBranches().remove(b);
				}else {
					b.getFromNode().getTreeBranches().remove(b);
					b.getToNode().getTreeBranches().remove(b);
				}
			}else if(paneNodes.get(paneNodes.size()-1)instanceof Circle){
				branchStartedPoint=branchEndedPoint=null;
				removeElement(false);
				nodes.remove(nodes.size()-1);
			}else {
				removeElement(false);
			}
		}

	}
	private void  showErrorDialog(Throwable ex) {



		resultTextArea.setVisible(false);
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Dialog");
		alert.setHeaderText("Look, an Exception Happend");
		alert.setContentText(ex.getMessage());

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();

	}
	public boolean validateGraph() {
		System.out.println("node size "+nodes.size());
		long treeBranches=branches.stream()
				.filter(b->!b.isLink())
				.count();
		if(treeBranches!=nodes.size()-1)return false;
		else System.out.println(treeBranches);

		long count=nodes.stream().filter(n->{
			return n.getLinkBranches().size()+n.getTreeBranches().size()<2;
		}).count();
		System.out.println(count);
		if(count!=0)return false;
		return true;
		
	}
	public void close() {
		executorService.shutdown();
	}

}
