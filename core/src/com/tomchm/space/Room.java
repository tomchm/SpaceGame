package com.tomchm.space;

public class Room {
	private int width, length, x, y;
	
	private int left, right, top, bottom;
	
	public Room(int width, int length, int x, int y){
		this.width = width;
		this.length = length;
		this.x = x;
		this.y = y;
		
		assignBorders();
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
		
		return true;
	}
}
