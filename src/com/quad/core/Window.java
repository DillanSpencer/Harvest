package com.quad.core;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window
{
	private JFrame frame;
	private Canvas canvas;
	private BufferedImage image;
	private Graphics g;
	private BufferStrategy bs;
	private GraphicsEnvironment env;
	private GraphicsDevice device;
	private DisplayMode dm;
	
	public Window(GameContainer gc)
	{
		image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		canvas = new Canvas();
		Dimension s = new Dimension((int)(gc.getWidth() * gc.getScale()), (int)(gc.getHeight() * gc.getScale()));
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s);
		canvas.setPreferredSize(s);
		
		frame = new JFrame(gc.getTitle());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		g = bs.getDrawGraphics();
	}
	
	public Window(GameContainer gc, boolean fullscreen){
		image = new BufferedImage(gc.getWidth(), gc.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		canvas = new Canvas();
		Dimension s = new Dimension((int)(gc.getWidth() * gc.getScale()), (int)(gc.getHeight() * gc.getScale()));
		canvas.setPreferredSize(s);
		canvas.setMaximumSize(s);
		canvas.setPreferredSize(s);
		
		frame = new JFrame(gc.getTitle());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		GraphicsEnvironment gren = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice grd = gren.getDefaultScreenDevice();


	   //frame.setUndecorated(true); 
	 // frame.setIgnoreRepaint(true); 
	   frame.setResizable(false);

	   grd.setFullScreenWindow(frame); 
	  
	   DisplayMode dm = new DisplayMode(800, 600, 32, DisplayMode.REFRESH_RATE_UNKNOWN);
	   try {
	     grd.setDisplayMode(dm);
	   }
	   catch (IllegalArgumentException e) {
	     System.out.println("Error");
	   }

	   frame.createBufferStrategy(2);

	   try { // 
	     Thread.sleep(1000); 
	   }
	   catch (InterruptedException ex) {}

	  

	   canvas.createBufferStrategy(2);
	   canvas.requestFocus();
	  // canvas.setIgnoreRepaint(true);
	   frame.setVisible(true);
	   
	   bs = canvas.getBufferStrategy(); 
	   g = bs.getDrawGraphics();
	}
	
	public void update()
	{
		g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		bs.show();
	}
	
	public void cleanUp()
	{
		g.dispose();
		bs.dispose();
		image.flush();
		frame.dispose();
	}

	public Canvas getCanvas()
	{
		return canvas;
	}

	public BufferedImage getImage()
	{
		return image;
	}
	
}
