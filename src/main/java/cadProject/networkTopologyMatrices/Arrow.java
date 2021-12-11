package cadProject.networkTopologyMatrices;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;
import javafx.scene.transform.Rotate;

public  class Arrow extends Polygon {

	public double rotate;
	public float t;
	QuadCurve curve;
	Rotate rz;



	@SuppressWarnings("exports")
	public Arrow( QuadCurve curve, float t, double... arg0) {
		super(arg0);
		this.curve = curve;
		this.t = t;
		init();
	}

	private void init() {

		setFill(null);
		setStroke(Color.BLACK);

		rz = new Rotate();

		rz.setAxis(Rotate.Z_AXIS);

		getTransforms().addAll(rz);

		update();
	}

	public void update() {
		double size = Math.max(curve.getBoundsInLocal().getWidth(), curve.getBoundsInLocal().getHeight());
		double scale = size / 4d;

		Point2D ori = eval(curve, t);
		Point2D tan = evalDt(curve, t).normalize().multiply(scale);

		setTranslateX(ori.getX());
		setTranslateY(ori.getY());

		double angle = Math.atan2( tan.getY(), tan.getX());

		angle = Math.toDegrees(angle);

		// arrow origin is top => apply offset
		double offset = -90;
		if( t > 0.5)
			offset = +90;

		rz.setAngle(angle + offset);

	}

	/**
	 * Evaluate the cubic curve at a parameter 0<=t<=1, returns a Point2D
	 * @param c the CubicCurve 
	 * @param t param between 0 and 1
	 * @return a Point2D 
	 */
	private Point2D eval(QuadCurve c, float t){
		Point2D p=new Point2D(Math.pow(1-t,3)*c.getStartX()+
				3*t*Math.pow(1-t,2)*c.getControlX()+
				3*(1-t)*t*t*c.getControlX()+
				Math.pow(t, 3)*c.getEndX(),
				Math.pow(1-t,3)*c.getStartY()+
				3*t*Math.pow(1-t, 2)*c.getControlY()+
				3*(1-t)*t*t*c.getControlY()+
				Math.pow(t, 3)*c.getEndY());
		return p;
	}

	/**
	 * Evaluate the tangent of the cubic curve at a parameter 0<=t<=1, returns a Point2D
	 * @param c the CubicCurve 
	 * @param t param between 0 and 1
	 * @return a Point2D 
	 */
	private Point2D evalDt(QuadCurve c, float t){
		Point2D p=new Point2D(-3*Math.pow(1-t,2)*c.getStartX()+
				3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getControlX()+
				3*((1-t)*2*t-t*t)*c.getControlX()+
				3*Math.pow(t, 2)*c.getEndX(),
				-3*Math.pow(1-t,2)*c.getStartY()+
				3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getControlY()+
				3*((1-t)*2*t-t*t)*c.getControlY()+
				3*Math.pow(t, 2)*c.getEndY());
		return p;
	}
}
