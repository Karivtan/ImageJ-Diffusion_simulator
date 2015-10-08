import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import ij.WindowManager;
import java.util.Random;
import ij.ImagePlus;
import ij.text.*; // import ImageJ text package

public class Image_generator3 implements PlugIn {

static public int[][][] Imageray;
static public String[] Imagetext;
static public String Singleimageline;
public ImageStack stack;
static public double x;
	static ImagePlus imp;
static public double y;
static public double y2;
static public double x2;
static public double directionx;
static public double directiony;
static public String v;
static public double angle;
static public double remain;

static public double xold;
static public double yold;
static public double y2old;
static public double x2old;
public int nF;
public int nB;
public int nW;
public int nH;
public int nZ;
public int switchT;
public int radius;
public double Dconst;
public double vconst;

String[] moChoice={"Brownian", "Constrained", "Active+Brownian", "Active", "Still","brown+con"};

	public void run(String arg) {//1
		IJ.run("Close All", "");
		nB=5;
		nH=100;
		nW=100;
		nB=100;
		nZ=100;
		switchT=50;
		radius=10;
		Dconst=4;
		vconst=4;
		GenericDialog gd = new GenericDialog("Stack Averaging");
		gd.addNumericField("Number of foci=", nF, 0);//50
		gd.addNumericField("Heigth", nH, 0);//50
		gd.addNumericField("Width", nW, 0);//50
		gd.addNumericField("Time frames", nW, 0);//50
		gd.addNumericField("Border", nB,0);//50
		gd.addNumericField("Switch (if applicable)", switchT,0);//50
		gd.addNumericField("Radius of confinement", radius,0);//50
		gd.addNumericField("Diffusion Constant", Dconst,1);//50
		gd.addNumericField("speed of active diffusion", vconst,0);//50

		gd.addChoice("Type of movement: ", moChoice, moChoice[0]);
		gd.showDialog();
		nF=(int)gd.getNextNumber();
		nH=(int)gd.getNextNumber();
		nW=(int)gd.getNextNumber();
		nZ=(int)gd.getNextNumber();
		nB=(int)gd.getNextNumber();
		switchT=(int)gd.getNextNumber();
		radius=(int)gd.getNextNumber();
		Dconst=gd.getNextNumber();
		vconst=gd.getNextNumber();
		String moSet=gd.getNextChoice();
		Random rG = new Random();

		imp = IJ.createImage("Untitled", "8-bit Black", nH+nB+nB, nW+nB+nB, nZ);

		IJ.showMessage("Random Image generator","Starting generation!");
			Imageray= new int[nH+nB+nB][nW+nB+nB][nZ];
				v="";
		for (int g=0; g<nF;g++){ //2

			x2old=100;
			y2old=100;	
			xold=50;
			yold=50;
			x2=(int)(rG.nextDouble()*nW+nB);
			y2=(int)(rG.nextDouble()*nH+nB);
			xold=x2;
			yold=y2;

			if (moSet=="Brownian"){
				for (int z=1;z<nZ+1;z++){ //z, 3

					angle=rG.nextDouble()*2*Math.PI-Math.PI;
					x=(Math.cos(angle)*Math.sqrt(4*Dconst)+xold);
					y=(Math.sin(angle)*Math.sqrt(4*Dconst)+yold);
					xold=x;
					yold=y;

					IJ.run(imp, "Specify...", "width=7 height=7 x="+x+" y="+y+" slice="+z+" oval");
//		IJ.showMessage("My_Plugin","Hello world!\n"+x+"\n"+y);
					IJ.run(imp, "Fill", "slice");

				 IJ.showProgress((double)z/nZ);
				}
			} else if (moSet=="brown+con") {
				for (int z=1;z<switchT;z++){ //z, 3

					angle=rG.nextDouble()*2*Math.PI-Math.PI;
					x=(Math.cos(angle)*Math.sqrt(4*Dconst)+xold);
					y=(Math.sin(angle)*Math.sqrt(4*Dconst)+yold);
					xold=x;
					yold=y;

					IJ.run(imp, "Specify...", "width=3 height=3 x="+x+" y="+y+" slice="+z+" oval");
					IJ.run(imp, "Fill", "slice");

				IJ.showProgress((double)z/nZ);
				x2=xold;
				y2=yold;
				}
				for (int z=switchT;z<nZ+1;z++){ //z, 3

					angle=rG.nextDouble()*2*Math.PI-Math.PI;
					x=(Math.cos(angle)*Math.sqrt(4*Dconst)+xold);
					y=(Math.sin(angle)*Math.sqrt(4*Dconst)+yold);

					while (((x-x2)*(x-x2)+(y-y2)*(y-y2))>radius*radius){
//x<(x2-5)||x>(x2+5)||y<(y2-5)||y>(y2+5)){
					angle=rG.nextDouble()*2*Math.PI-Math.PI;
					x=(Math.cos(angle)*Math.sqrt(4*Dconst)+xold);
					y=(Math.sin(angle)*Math.sqrt(4*Dconst)+yold);
					}

					xold=x;
					yold=y;
				
					IJ.run(imp, "Specify...", "width=3 height=3 x="+x+" y="+y+" slice="+z+" oval");
					IJ.run(imp, "Fill", "slice");
				 IJ.showProgress((double)z/nZ);
				}//3



			} else if (moSet=="Constrained") {

				for (int z=1;z<nZ+1;z++){ //z, 3

					angle=rG.nextDouble()*2*Math.PI-Math.PI;
					x=(Math.cos(angle)*Math.sqrt(4*Dconst)+xold);
					y=(Math.sin(angle)*Math.sqrt(4*Dconst)+yold);

					while (((x-x2)*(x-x2)+(y-y2)*(y-y2))>radius*radius){
//					while (x<(x2-10)||x>(x2+10)||y<(y2-10)||y>(y2+10)){
					angle=rG.nextDouble()*2*Math.PI-Math.PI;
					x=(Math.cos(angle)*Math.sqrt(4*Dconst)+xold);
					y=(Math.sin(angle)*Math.sqrt(4*Dconst)+yold);
					}

					xold=x;
					yold=y;
				
					IJ.run(imp, "Specify...", "width=3 height=3 x="+x+" y="+y+" slice="+z+" oval");
					IJ.run(imp, "Fill", "slice");
				 IJ.showProgress((double)z/nZ);
				}//3
			} else if (moSet=="Active"){

				directionx = (Math.random()*2-1)*Math.sqrt(vconst);
				if (Math.random()>0.5){
				directiony = Math.sqrt(vconst-directionx*directionx);
				} else {
				directiony = -Math.sqrt(vconst-directionx*directionx);
				}
				for (int z=1;z<nZ+1;z++){ //z, 3
					x=(xold+directionx);
					y=(yold+directiony);
					xold=x;
					yold=y;

					IJ.run(imp, "Specify...", "width=3 height=3 x="+x+" y="+y+" slice="+z+" oval");
					IJ.run(imp, "Fill", "slice");
				 IJ.showProgress((double)z/nZ);
				}
			} else if (moSet=="Still") {
				x=(x2+rG.nextGaussian());
				y=(y2+rG.nextGaussian());
				for (int z=1;z<nZ+1;z++){
					IJ.run(imp, "Specify...", "width=3 height=3 x="+x+" y="+y+" slice="+z+" oval");
					IJ.run(imp, "Fill", "slice");
				 IJ.showProgress((double)z/nZ);
				}
			} else {
				directionx = (Math.random()*2-1)*Math.sqrt(vconst);
				if (Math.random()>0.5){
					directiony = Math.sqrt(vconst-directionx*directionx);
				} else {
					directiony = 0-Math.sqrt(vconst-directionx*directionx);
				}
//				IJ.showMessage("Random Image generator","Starting generation!\n"+directionx+";"+directiony);

				for (int z=1;z<nZ+1;z++){ //z, 3
					angle=rG.nextDouble()*2*Math.PI-Math.PI;
					x=(Math.cos(angle)*Math.sqrt(4*Dconst)+xold+directionx);
					y=(Math.sin(angle)*Math.sqrt(4*Dconst)+yold+directiony);
					xold=x;
					yold=y;
					IJ.run(imp, "Specify...", "width=3 height=3 x="+x+" y="+y+" slice="+z+" oval");
					IJ.run(imp, "Fill", "slice");

				 IJ.showProgress((double)z/nZ);
				}
			}
		}//1
		IJ.run(imp, "Select None", "");
//		IJ.run(imp, "Gaussian Blur...", "sigma=1 stack");
		imp.show();
		}


}
