package main;

import java.awt.EventQueue;

import controller.NewZoneImageController;
import controller.SelectZoneImageController;
import controller.WindowController;
import gui.MainWindow;
import model.ImageModel;
import view.ImageView;

public class Main {

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					ImageModel model = new ImageModel();
					ImageView view = new ImageView(model);

					// Desactiver les fonctionalites d'interaction tant qu'une
					// image n'est pas chargee
					NewZoneImageController nziController = new NewZoneImageController();
					SelectZoneImageController sziController = new SelectZoneImageController();

					WindowController wcontroller = new WindowController(model, view, nziController, sziController);

					MainWindow window = new MainWindow(model, view, wcontroller);

					window.getFrame().setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
