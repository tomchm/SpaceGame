package com.tomchm.space;

import java.util.Random;

public class Tunneler {
	private int x, y, targetX, targetY, deltaX = 0, deltaY = 0, direction, prob = 0, movements = 0;
	private char[][] grid;
	private boolean foundEmpty = false, didTurn = false, didBranch = false;
	
	public Tunneler(int x, int y, int targetX, int targetY, char[][] grid){
		this.x = x;
		this.y = y;
		this.targetX = targetX;
		this.targetY = targetY;
		this.grid = grid;
		initialDirection();
		bore();
	}
	
	private void initialDirection(){
		Random r = new Random();
		boolean choice = r.nextBoolean();
		if(targetX > x){
			if(choice){
				changeDirection(0);
			}
			else{
				if(targetY > y){
					changeDirection(2);
				}
				else{
					changeDirection(3);
				}
			}
		}
		else{
			if(choice){
				changeDirection(1);
			}
			else{
				if(targetY > y){
					changeDirection(2);
				}
				else{
					changeDirection(3);
				}
			}
		}
	}
	
	private void bore() {
		boolean found = false;
		while(!found && (movements<10000)){
			
			if((direction == 0 || direction == 1) && (x == targetX)){
				if(targetY > y){
					changeDirection(2);
				}
				else if(y > targetY){
					changeDirection(3);
				}
				else{
					found = true;
				}
			}
			else if((direction == 2 || direction == 3) && (y == targetY)){
				if(targetX > x){
					changeDirection(0);
				}
				else if(x > targetX){
					changeDirection(1);
				}
				else{
					found = true;
				}
			}
			else{
				move();
			}
			
		}
		//System.out.println("Bore"+movements);
	}
	
	private void changeDirection(int direction){
		this.direction = direction;
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
		movements += 1;
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
