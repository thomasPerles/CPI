package controller;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import model.ImageModel;
import view.ImageView;

public abstract class ImageController implements MouseListener, MouseMotionListener, KeyListener {

	protected ImageModel model;
	protected ImageView view;

	
	public ImageController() {}
	
	/**
	 * Setter model
	 * @param model
	 */
	public void setModel(ImageModel model) {
		this.model = model;
	}

	/**
	 * Setter view
	 * @param view
	 */
	public void setView(ImageView view) {
		if (view != null) {
			this.view = view;
			this.view.addMouseListener(this);
			this.view.addMouseMotionListener(this);
			this.view.addKeyListener(this);
			//System.out.println("Listeners added");
		}
		if (view == null && this.view != null) {
			this.view.removeMouseListener(this);
			this.view.removeMouseMotionListener(this);
			this.view.removeKeyListener(this);
		}

	}

}
