package main;

import java.awt.EventQueue;

import controller.ImageController;
import gui.MainWindow;
import model.ImageModel;
import view.ImageView;

public class Main {

	public static void main(String[] args) {

		// Initialise model
		// ImageModel model = new ImageModel("res-test/bearbull.bmp");
		ImageModel model = new ImageModel();

		// Initialise view
		ImageView view = new ImageView(model);

		// Initialise controller sans modèle car le modèle manque une image
		ImageController imageController = new ImageController(null, view);

		// Faire connaître le contrôleur à la vue
		view.setController(imageController);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow(model, view, imageController);
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
