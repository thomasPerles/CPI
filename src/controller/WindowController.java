package controller;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JToggleButton;

import model.ImageModel;

public class WindowController implements ItemListener {

    private HashMap<String, JComponent> components;
    private ImageModel model;
    private NewZoneImageController nziController;
    private SelectZoneImageController sziController;

    public WindowController(ImageModel model, NewZoneImageController nziController,
	    SelectZoneImageController sziController) {
	this.components = new HashMap<String, JComponent>();
	this.model = model;
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

			// Désactiver toutes fonctionalité autre que la
			// sélection de zones
			for (Iterator<String> it2 = components.keySet().iterator(); it2.hasNext();) {
			    String next2 = it2.next();
			    if (!next2.equals("selectZone"))
				((JToggleButton) this.components.get(next2)).setSelected(false);
			}
			this.nziController.setModel(null);

			// Activer les fonctionalités de sélection de zones
			this.sziController.setModel(this.model);
		    } else {
			this.sziController.setModel(null);
		    }
		}

		if (next.equals("newZone")) {

		    if (jb.isSelected()) {
			// Désactiver toutes fonctionalité autre que la création
			// de
			// zones
			for (Iterator<String> it2 = components.keySet().iterator(); it2.hasNext();) {
			    String next2 = it2.next();
			    if (!next2.equals("newZone"))
				((JToggleButton) this.components.get(next2)).setSelected(false);
			}
			this.sziController.setModel(null);
			// Activer les fonctionnalités de création de zones
			this.nziController.setModel(this.model);
		    } else {
			this.nziController.setModel(null);
		    }
		}
	    }
	}
    }

}
