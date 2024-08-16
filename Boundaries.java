package cmpt305;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Boundaries {
	Neighborhood hood; 	// neighbourhood object from Neighborhood class
	String area;		// Square KM area of neighbourhood
	ArrayList<Location> polygon;	// List of points (in order) to make boundary polygon 
	
	static ArrayList<Boundaries> boundariesList; // Full list of all Boundaries objects for city
	
	public Boundaries(Neighborhood hood, String area, ArrayList<Location> polygon) {
		this.hood = hood;
		this.area = area;
		this.polygon = polygon;
	}

	public static void getBoundaries() throws IOException {
        
        Scanner file = new Scanner(Paths.get("CSV/NEIGHBOURHOODS_SHAPE.csv").toAbsolutePath());
        boundariesList = new ArrayList<Boundaries>();

        if (file.hasNextLine()) {
            file.nextLine(); // skip first line containing headers
        }
        String line;
        
        while (file.hasNextLine()) {
        	
        	line = file.nextLine();
        	int start = line.indexOf("MULTIPOLYGON (((");
        	int end = line.indexOf(")))");
        	String[] twos = line.substring(start+16, end).split(",");
        	String name = line.substring(end+5);
        	String[] f1 = line.split(",");
        	String area = f1[0];
        	String id = f1[1];
        	
//        	System.out.println("Area: " + area + "   Hood ID:" + f1[1]);
        	
        	ArrayList<Location> points = new ArrayList<Location>();
        	for(String s : twos) {
        		if(s.substring(0, 1).contentEquals(" ")){
        			s = s.substring(1);
        		}
        		
        		if(s.contains("))")) {
        			s = s.substring(0, s.length()-2);
        			twos = s.split(" ");
            		points.add(new Location(Double.parseDouble(twos[1].toString()), Double.parseDouble(twos[0].toString())));
                	boundariesList.add(new Boundaries(new Neighborhood(Integer.parseInt(id),name, 0), area, points));
                	continue;
                	
        		} else if(s.contains("((")) {
        			s = s.substring(2, s.length());
        			points = new ArrayList<Location>();
        		}
        		
        		if(s.contains(")")) {
    				s = s.substring(0, s.length()-1);
            		twos = s.split(" ");
            		points.add(new Location(Double.parseDouble(twos[1].toString()), Double.parseDouble(twos[0].toString())));
            		break;
    			}
        		twos = s.split(" ");
        		points.add(new Location(Double.parseDouble(twos[1].toString()), Double.parseDouble(twos[0].toString())));
        	}
        	boundariesList.add(new Boundaries(new Neighborhood(Integer.parseInt(id),name, 0), area, points));
        }

        file.close();
	}

}
