module cadProject.networkTopologyMatrices {
    requires javafx.controls;
    requires javafx.fxml;
	requires ejml.simple;
	requires ejml.core;
	requires javafx.base;
	requires javafx.graphics;
	requires lombok;
	



    opens cadProject.networkTopologyMatrices to javafx.fxml;
    exports cadProject.networkTopologyMatrices;
}
