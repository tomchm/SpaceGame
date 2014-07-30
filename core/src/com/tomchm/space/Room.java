package com.tomchm.space;

import java.util.Arrays;

public class Room {
	private int width, length, x, y, id;
	
	private int left, right, top, bottom;
	
	private int connectivity;
	
	private RoomConnection[] connections;
	
	public Room(int width, int length, int x, int y, int id){
		this.width = width;
		this.length = length;
		this.x = x;
		this.y = y;
		this.id = id;
		
		connectivity = 0;
		assignBorders();
	}
	
	public int getID(){
		return id;
	}
	
	private void assignBorders(){
		int widthMid = width / 2;
		int lengthMid = length / 2;
		
		left = x - widthMid;
		right = left + width;
		bottom = y - lengthMid;
		top = bottom + length;
	}
	
	public int getLeft() {
		return left;
	}
	
	public int getRight() {
		return right;
	}
	
	public int getTop() {
		return top;
	}
	
	public int getBottom() {
		return bottom;
	}
	
	public int getWidth() {
		return width;
	}

	public int getLength() {
		return length;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setConnectivity(int connectivity){
		this.connectivity = connectivity;
	}
	
	public int getConnectivity(){
		return connectivity;
	}
	

	
	public void addConnections(RoomConnection[] rc){
		connections = rc;
	}
	
	public RoomConnection[] mininumConnections(int n){
		RoomConnection[] minrc = new RoomConnection[n];
		Arrays.sort(connections);
		for(int i=0; i<n; i++){
			minrc[i] = connections[i];
		}
		return minrc;
	}
	
	public boolean noOverlap(Room room){
		if(room.left <= right+1 && room.left >= left-1){
			if(room.bottom >= bottom-1 && room.bottom <= top+1){
				return false;
			}
			else if(room.top >= bottom-1 && room.top <= top+1){
				return false;
			}
		}
		
		if(room.right <= right+1 && room.right >= left-1){
			if(room.bottom >= bottom-1 && room.bottom <= top+1){
				return false;
			}
			else if(room.top >= bottom-1 && room.top <= top+1){
				return false;
			}
		}
		
		if(left <= room.right+1 && left >= room.left-1){
			if(bottom >= room.bottom-1 && bottom <= room.top+1){
				return false;
			}
			else if(top >= room.bottom-1 && top <= room.top+1){
				return false;
			}
		}
		
		if(right <= room.right+1 && right >= room.left-1){
			if(bottom >= room.bottom-1 && bottom <= room.top+1){
				return false;
			}
			else if(top >= room.bottom-1 && top <= room.top+1){
				return false;
			}
		}
		
		return true;
	}
}
