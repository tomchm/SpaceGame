package com.tomchm.space;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Room {
	private int width, length, x, y, id;
	
	private int left, right, top, bottom;
	
	private int connectivity, doors = 0, overlap = 4;
	
	private RoomConnection[] connections;
	private ArrayList<Door> doorList = new ArrayList<Door>();
	
	
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
	
	public void addDoor(int x, int y){
		doorList.add(new Door(x,y));
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
		if(room.left <= right+overlap && room.left >= left-overlap){
			if(room.bottom >= bottom-overlap && room.bottom <= top+overlap){
				return false;
			}
			else if(room.top >= bottom-overlap && room.top <= top+overlap){
				return false;
			}
		}
		
		if(room.right <= right+overlap && room.right >= left-overlap){
			if(room.bottom >= bottom-overlap && room.bottom <= top+overlap){
				return false;
			}
			else if(room.top >= bottom-overlap && room.top <= top+overlap){
				return false;
			}
		}
		
		if(left <= room.right+overlap && left >= room.left-overlap){
			if(bottom >= room.bottom-overlap && bottom <= room.top+overlap){
				return false;
			}
			else if(top >= room.bottom-overlap && top <= room.top+overlap){
				return false;
			}
		}
		
		if(right <= room.right+overlap && right >= room.left-overlap){
			if(bottom >= room.bottom-overlap && bottom <= room.top+overlap){
				return false;
			}
			else if(top >= room.bottom-overlap && top <= room.top+overlap){
				return false;
			}
		}
		
		return true;
	}
	
	public void removeDoors(char[][] grid){
		Random r = new Random();
		int choice = r.nextInt(3)+1;
		while(doorList.size()>choice){
			int doorChoice = r.nextInt(doorList.size());
			doorList.get(doorChoice).removeDoor(grid);
			doorList.remove(doorChoice);
		}             
		for(int i=0;i<doorList.size();i++){
			doorList.get(i).replaceDoor(grid);
		}
	}
	
	private class Door{
		int x, y;
		
		Door(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		void removeDoor(char[][] grid){
			grid[x][y] = 'O';
		}
		
		void replaceDoor(char[][] grid){
			grid[x][y] = 'D';
		}
	}
}


