package controller;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JToggleButton;

import model.ImageModel;
import view.ImageView;

public class WindowController implements ItemListener {

	private HashMap<String, JComponent> components;
	private ImageModel model;
	private NewZoneImageController nziController;
	private SelectZoneImageController sziController;
	private ImageView view;

	public WindowController(ImageModel model, ImageView view, NewZoneImageController nziController,
			SelectZoneImageController sziController) {
		this.components = new HashMap<String, JComponent>();
		this.model = model;
		this.view = view;
		this.nziController = nziController;
		this.sziController = sziController;
	}

	public void addComponent(String name, JComponent jc) {
		this.components.put(name, jc);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		for (Iterator<String> it = components.keySet().iterator(); it.hasNext();) {

			String next = it.next();
			JToggleButton jb = (JToggleButton) components.get(next);

			if (jb == e.getSource()) {

				if (next.equals("selectZone")) {

					if (jb.isSelected()) {

						// D�sactiver toutes fonctionalit� autre que la
						// s�lection de zones
						for (Iterator<String> it2 = components.keySet().iterator(); it2.hasNext();) {
							String next2 = it2.next();
							if (!next2.equals("selectZone"))
								((JToggleButton) this.components.get(next2)).setSelected(false);
						}
						this.nziController.setModel(null);
						this.nziController.setView(null);
						// Activer les fonctionalit�s de s�lection de zones
						this.sziController.setModel(this.model);
						this.sziController.setView(this.view);
					} else {
						this.sziController.setModel(null);
						this.sziController.setView(null);
					}
				}

				if (next.equals("newZone")) {

					if (jb.isSelected()) {
						// D�sactiver toutes fonctionalit� autre que la cr�ation
						// de
						// zones
						for (Iterator<String> it2 = components.keySet().iterator(); it2.hasNext();) {
							String next2 = it2.next();
							if (!next2.equals("newZone"))
								((JToggleButton) this.components.get(next2)).setSelected(false);
						}
						this.sziController.setModel(null);
						this.sziController.setView(null);
						// Activer les fonctionnalit�s de cr�ation de zones
						this.nziController.setModel(this.model);
						this.nziController.setView(this.view);
					} else {
						this.nziController.setModel(null);
						this.nziController.setView(null);
					}
				}
			}
		}
	}

}
