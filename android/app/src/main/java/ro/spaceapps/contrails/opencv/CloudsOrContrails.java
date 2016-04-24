package ro.spaceapps.contrails.opencv;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.LineSegmentDetector;
import org.opencv.objdetect.CascadeClassifier;

public class CloudsOrContrails {


	public void run() throws IOException {
	    System.out.println("\nRunning DetectFaceDemo");

        //________________
	    BufferedImage image = ImageIO.read(new File("D:/Projects/NasaSpaceEvents/NasaSpaceEvents/src/main/resources/sky3.png"));
	    long time = new Date().getTime();
	    String result = "D:/Projects/NasaSpaceEvents/NasaSpaceEvents/src/main/resources/sky3_" + time + ".png";
	    
	    byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	    
	    Mat src = new Mat(image.getHeight(),image.getWidth(), CvType.CV_8UC3);
        src.put(0, 0, data);
        
    	filterLines(image, src);
 		
 		 // Save the visualized detection.
 	    System.out.println(String.format("Writing %s", result));
 	    Imgcodecs.imwrite(result, src);
	  }

	private void filterLines(BufferedImage image, Mat src) {
		Mat canny = new Mat(image.getHeight(),image.getWidth(), CvType.CV_8UC3);
 		Mat lines = new Mat();
 		
 		Imgproc.Canny(src, canny, 50, 10, 3, true);
 		Imgproc.HoughLinesP(canny, lines, 1, Math.PI / 180, 100, 20, 10);
 		
 		int size = lines.rows();
 		double[][] extractedLines = new double[size][];
 		
 		for(int i = 0; i < lines.rows(); i++) {
 			extractedLines[i] = lines.get(i, 0);
 		}
 		
 		for(int i=0; i< 5; i++) {
 			extractedLines = iterativeGetLines(extractedLines, true);
 		}
 		extractedLines = iterativeGetLines(extractedLines, false);
 		
 		for(int i = 0; i < extractedLines.length; i++) {
 			double[] curentLine = extractedLines[i];
 			if (curentLine != null) {
 				Imgproc.line(src, new Point(curentLine[0], curentLine[1]), new Point(curentLine[2], curentLine[3]), new Scalar(255, 0, 0));
 			}
 		}
	}
	  
	private double[][] iterativeGetLines(double[][] extractedLines, boolean withParallels){
		int pos = 0;
		Set<LineDTO> set = new HashSet<>();
		
		for(int i = 0; i < extractedLines.length ; i++) {
 			double[] curentLine = extractedLines[i];
 			if (curentLine == null) {
 				continue;
 			}
 			double angle = getAngle(curentLine);
 			boolean findParallelLine = false;
 			
 			for(int j = 0; j < extractedLines.length; j++) {
 				double[] secondLine = extractedLines[j];
 				if (secondLine == null) {
 	 				continue;
 	 			}
 				double angle2 = getAngle(secondLine);
 				
 				double angleDiff = Math.abs(Math.abs(angle) - Math.abs(angle2));
 				// these lines are almost parallel
 				if (i != j && angleDiff < 2) {
 					Point p1x = new Point(curentLine[0], curentLine[1]);
 					Point p1y = new Point(curentLine[2], curentLine[3]);
 					Point p2x = new Point(secondLine[0], secondLine[1]);
 					Point p2y = new Point(secondLine[2], secondLine[3]);
 					double d1 = getDistanceFromLineToPoint(curentLine, p2x);
 					double d2 = getDistanceFromLineToPoint(curentLine, p2y);
 					double d3 = getDistanceFromLineToPoint(secondLine, p1x);
 					double d4 = getDistanceFromLineToPoint(secondLine, p1y);
 				
 					// get min distance between lines
 					double min = Math.min(d1, d2);
 					min = Math.min(min, d3);
 					min = Math.min(min, d4);
 					
 					// these lines are almost parallel and close to each other
 					if (min < 4) {
 						double maxDistanceBP = 0;
 						double minDistanceBP = Double.MAX_VALUE;
 						Point pt1 = null;
 						Point pt2 = null;
 						double d_p1x_p2x = getDistanceBetweenTwoPoints(p1x, p2x);
 						double d_p1x_p2y = getDistanceBetweenTwoPoints(p1x, p2y);
 						double d_p1y_p2x = getDistanceBetweenTwoPoints(p1y, p2x);
 						double d_p1y_p2y = getDistanceBetweenTwoPoints(p1y, p2y);
 						
 						minDistanceBP = Math.min(minDistanceBP, d_p1x_p2x);
 						minDistanceBP = Math.min(minDistanceBP, d_p1x_p2y);
 						minDistanceBP = Math.min(minDistanceBP, d_p1y_p2x);
 						minDistanceBP = Math.min(minDistanceBP, d_p1y_p2y);
 						
 						// verify if the distance between the closest points is not too long
 						if (minDistanceBP <= 70) {
 							// get the points that make the longest line
 							if (d_p1x_p2x >= maxDistanceBP) {
 	 							maxDistanceBP = d_p1x_p2x;
 	 							pt1 = p1x;
 	 							pt2 = p2x;
 	 						}
 	 						
 	 						if (d_p1x_p2y >= maxDistanceBP) {
 	 							maxDistanceBP = d_p1x_p2y;
 	 							pt1 = p1x;
 	 							pt2 = p2y;
 	 						}
 	 						if (d_p1y_p2x >= maxDistanceBP) {
 	 							maxDistanceBP = d_p1y_p2x;
 	 							pt1 = p1y;
 	 							pt2 = p2x;
 	 						}
 	 						
 	 						if (d_p1y_p2y >= maxDistanceBP) {
 	 							maxDistanceBP = d_p1y_p2y;
 	 							pt1 = p1y;
 	 							pt2 = p2y;
 	 						}
 	 						
 	 						double[] newLine = new double[4];
 	 						newLine[0] = pt1.x;
 	 						newLine[1] = pt1.y;
 	 						newLine[2] = pt2.x;
 	 						newLine[3] = pt2.y;
 	 						
 	 						if (withParallels) {
 	 							LineDTO  lineDTO = new LineDTO(pt1, pt2);
 	 							set.add(lineDTO);
 	 						}
 	 						
 	 						findParallelLine = true;
 						}
 					}
 				}
 			}
 			
 			if(!findParallelLine) {
 				double[] newLine = new double[4];
				newLine[0] = curentLine[0];
				newLine[1] = curentLine[1];
				newLine[2] = curentLine[2];
				newLine[3] = curentLine[3];
				
				LineDTO  lineDTO = new LineDTO(new Point(curentLine[0], curentLine[1]), new Point(curentLine[2], curentLine[3]));
				set.add(lineDTO);
 			}
 		}
		
		double[][] result = new double[set.size()][];
		for (LineDTO lineDTO : set) {
			double[] line = lineDTO.getLine();
			result[pos] = line;
			pos++;
		}
		
		return result;
	}
	
	private double getAngle(double[] val) {
		double x1 = val[0];
		double y1 = val[1];
		double x2 = val[2];
		double y2 = val[3];
		double angle = Math.atan2(y2 - y1, x2 - x1) * 180.0 / Math.PI;
		
		return angle;
	}

	private double getDistanceFromLineToPoint(double[] line, Point p) {
		double x1 = line[0];
		double y1 = line[1];
		double x2 = line[2];
		double y2 = line[3];
		
		double d = Math.abs((y2-y1)* p.x - (x2 - x1)*p.y + x2*y1 - y2*x1) / Math.sqrt(Math.pow(y2-y1,2) + Math.pow(x2 - x1,2));
		return d;
	}
	
	private double getDistanceBetweenTwoPoints(Point p1, Point p2) {
		return Math.sqrt((p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y));
	}
	
	public static void main(String[] args) {
		System.out.println("Hello, OpenCV");

		// Load the native library.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		try {
			new CloudsOrContrails().run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
