package controller;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import model.ImageModel;
import view.ImageView;

public abstract class ImageController implements MouseListener, MouseMotionListener, KeyListener {

    protected ImageModel model;
    protected ImageView view;

    public ImageController(ImageModel model, ImageView view) {
	this.model = model;
	this.view = view;
	view.addMouseListener(this);
	view.addMouseMotionListener(this);
	view.addKeyListener(this);
    }

    public void setModel(ImageModel model) {
	this.model = model;
    }

}
