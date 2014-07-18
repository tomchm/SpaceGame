package com.tomchm.space;

import java.util.Random;

public class Tunneler {
	private int x, y, deltaX = 0, deltaY = 0, direction, prob = 0, movements = 0;
	private char[][] grid;
	private boolean foundEmpty = false, didTurn = false, didBranch = false;
	
	public Tunneler(int x, int y, char[][] grid){
		this.x = x;
		this.y = y;
		this.grid = grid;
		bore();
	}
	
	private void bore() {
		Random r = new Random();
		direction = r.nextInt(4);
		changeDirection(direction);
		
		while (movements < 100){
			if(outOfBounds()){
				break;
			}
			if(foundEmpty == false){
				if(grid[x+deltaX][y+deltaY] == 'O'){
					move();
				}
				else {
					foundEmpty = true;
					move();
					move();
				}
			}
			else {
				
				if(grid[x+deltaX][y+deltaY] == 'O'){
					break;
				}
				
				int choice = r.nextInt(100);
				if(choice < 90-prob){
					move();
					prob += 7;
				}
				else if(choice < 95){
					turn();
					prob = 0;
				}
				else{
					new Tunneler(x, y, grid);
					prob = 0;
				}
			}
		movements++;
		}
	}
	
	private void changeDirection(int direction){
		switch(direction){
		case 0:
			deltaX = 1;
			deltaY = 0;
			break;
		case 1:
			deltaX = -1;
			deltaY = 0;
			break;
		case 2:
			deltaX = 0;
			deltaY = 1;
			break;
		case 3:
			deltaX = 0;
			deltaY = -1;
			break;
		}
	}
	
	private void move() {
		
		if(!outOfBounds()){
			x += deltaX;
			y += deltaY;
			
			if(grid[x][y] != 'O'){
				grid[x][y] = 'X';
			}
		}
	}
	
	private void turn() {
		Random r = new Random();
		int flip = r.nextInt(2);
		if(direction <= 1){
			direction = 2 + flip;
		}
		else {
			direction = flip;
		}
		changeDirection(direction);
	}
	
	private boolean outOfBounds(){
		if(x + deltaX >= grid.length) return true;
		if(x + deltaX < 0) return true;
		if(y + deltaY >= grid[0].length) return true;
		if(y + deltaY < 0) return true;
		
		return false;
	}
}
