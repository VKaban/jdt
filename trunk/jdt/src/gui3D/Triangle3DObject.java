package gui3D;

import java.util.Iterator;
import java.util.Vector;

import javax.media.j3d.Appearance;
import javax.media.j3d.Geometry;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleArray;
import javax.vecmath.*;

import delaunay_triangulation.Delaunay_Triangulation;
import delaunay_triangulation.Triangle_dt;

public class Triangle3DObject extends Shape3D {
	
	public Appearance _appearance;
	public TriangleArray _triangle_arr;
	delaunay_triangulation.BoundingBox _bound_box;
	
	public Triangle3DObject(Iterator<Triangle_dt> triangles_iter,int num_of_triangles, delaunay_triangulation.BoundingBox bound_box)
	{
		_appearance = null;
		_triangle_arr = null;
		_bound_box = bound_box;
		
		this.setGeometry(Triangle3DGeometry(triangles_iter, num_of_triangles));
        this.setAppearance(Triangle3DAppearance());
	}
	
	private Color3f get_col_by_height(int z)
	{
		int z_range = (int)(_bound_box.getMaxPoint().z() - _bound_box.getMinPoint().z());
		if (z < z_range/3)
		{
			float shade = (float)(0.2 + (z/(z_range/3))*0.4);
			return new Color3f(shade, 0f, 0f);
		}
		else if (z < (2*z_range)/3)
		{
			float shade = (float)(0.5 + ((z-z_range/3)/(z_range/3))*0.3);
			return new Color3f(shade, 0f, 0f);
		}
		else
		{	
			float shade = (float)(0.8 + ((z-(2*z_range)/3)/(z_range/3))*0.2);
			return new Color3f(shade, 0f, 0f);
		}
	}
	
	private Geometry Triangle3DGeometry(Iterator<Triangle_dt> triangles_iter,int num_of_triangles)
	{
		Color3f red   = new Color3f(1.0f, 0.0f, 0.0f);
	    Color3f green = new Color3f(0.0f, 1.0f, 0.0f);
	    Color3f blue  = new Color3f(0.0f, 0.0f, 1.0f);
	    
		_triangle_arr = new TriangleArray(3*num_of_triangles, TriangleArray.COORDINATES | TriangleArray.COLOR_3);
		
		System.out.println("DEBUG INFO number of triangles to render = "+num_of_triangles);
		
		int i=0;
		while ((i<num_of_triangles) && (triangles_iter.hasNext()))
		{
			Triangle_dt curr_triangle = triangles_iter.next();
				
			if((curr_triangle.p1() != null) && (curr_triangle.p2() != null) && (curr_triangle.p3() != null))
			{
				_triangle_arr.setCoordinate(3*i, new Point3d(curr_triangle.p1().x(), curr_triangle.p1().y(), curr_triangle.p1().z()));
				_triangle_arr.setCoordinate(3*i+1, new Point3d(curr_triangle.p2().x(), curr_triangle.p2().y(), curr_triangle.p2().z()));
				_triangle_arr.setCoordinate(3*i+2, new Point3d(curr_triangle.p3().x(), curr_triangle.p3().y(), curr_triangle.p3().z()));
				_triangle_arr.setColor(3*i, get_col_by_height((int)curr_triangle.p1().z()));
				_triangle_arr.setColor(3*i+1, get_col_by_height((int)curr_triangle.p2().z()));
				_triangle_arr.setColor(3*i+2, get_col_by_height((int)curr_triangle.p3().z()));
			//	triangle_arr.setColor(3*i+0, new Color3f(0.0f, 0.0f, (float)curr_triangle.p1().z()/50));
				//triangle_arr.setColor(3*i+1, new Color3f(0.0f, 0.0f, (float)curr_triangle.p1().z()/50));
				//triangle_arr.setColor(3*i+2, new Color3f(0.0f, 0.0f, (float)curr_triangle.p1().z()/50));
				i++;
			}
			else
			{
			//	System.out.println(curr_triangle.p1());
				//System.out.println(curr_triangle.p2());
				//System.out.println(curr_triangle.p3());
				
			}
		}
		System.out.println("DEBUG INFO successfuly displayed "+i+" Triangles");
		
		return _triangle_arr;
	}
	
	//TODO: all triangles should share the same appearance object
	private Appearance Triangle3DAppearance()
	{
		_appearance = new Appearance();
	    PolygonAttributes polyAttrib = new PolygonAttributes();

	    polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_FILL);
	    _appearance.setCapability(ALLOW_APPEARANCE_READ);
	    _appearance.setCapability(ALLOW_APPEARANCE_WRITE);
	    _appearance.setPolygonAttributes(polyAttrib);

	    return _appearance;
	}

}