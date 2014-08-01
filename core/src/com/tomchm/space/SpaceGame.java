package com.tomchm.space;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SpaceGame extends ApplicationAdapter{
	
	int worldLength = 96, worldWidth = 96;
	int roomMax = 10, roomMin = 5;
	
	SpriteBatch batch;
	Texture img;
	TextureRegion[][] regions;
	TextureRegion black, red, blue, white;
	TextureRegion[] shade = new TextureRegion[8];
	TextureRegion[][] renderRegion;
	
	char[][] grid;
	int renderGrid[][];
	Room[] rooms;
	
	int totalFails = 0, totalConnects = 0, randomDoubleValue = 5, maxConnectivity = 1;
	private static int maxConnects = 2000000;
	double roomAverageX = 0, roomAverageY = 0;
	
	private ArrayList<RoomConnection> connections;
	
	
	private void blankGrid(){
		for (int i=0; i< grid.length; i++){
			for (int j=0; j < grid[0].length; j++){
				grid[i][j] = '.';
			}
		}
	}
	
	private boolean addRooms(int n){
		rooms = new Room[n];
		for (int i=0; i < n; i++){
			if(!addRectangle(i)){
				return false;
			}
		}
		return true;
	}
	
	private boolean addRectangle(int i){
		
		Random r = new Random();
		int maxX = grid.length-2*roomMax;
		int maxY = grid[0].length-3*roomMax;
		
		int x = 0, y = 0;
		
		boolean findEmpty = true;
		int width = triDist(roomMin, roomMax);
		int length = triDist(roomMin, roomMax);
		
		while(findEmpty && totalFails < 10000000){
			findEmpty = false;
			
			x = roomMax + r.nextInt(maxX);
			y = 2*roomMax + r.nextInt(maxY);
			
			Room test = new Room(width, length, x, y, i);
			for(int j = 0; j < i; j++){
				Room compareRoom = rooms[j];
				if(compareRoom.noOverlap(test) == false){
					findEmpty = true;
					totalFails += 1;
					break;
				}
			}
		}
		
		if(totalFails >= 10000000){
			return false;
		}

		rooms[i] = new Room(width, length, x, y, i);
		
		reAverageRooms(x, y, i);
		
		for( int xx=rooms[i].getLeft(); xx <= rooms[i].getRight(); xx++){
			for (int yy=rooms[i].getBottom(); yy <= rooms[i].getTop(); yy++){
				try{
					grid[xx][yy] = 'O';
				}
				catch(Exception e){
					System.out.println(totalFails + "Error found."+xx+","+yy);
				}
			}
		}
		return true;
		
	}
	
	private int triDist(int min, int max){
		int size = max - min;
		int randomMultiplier = size * (size + 1) / 2;
		Random r = new Random();
		int randomInt = r.nextInt(randomMultiplier);
		
		int number = 0;
		for (int i=size; randomInt >= 0; i--){
			randomInt -= i;
			number++;
		}
		
		return number + min;
	}
	
	private void reAverageRooms(int x, int y, int i){
		roomAverageX = ((roomAverageX * (i-1)) + x) / i;
		roomAverageY = ((roomAverageY * (i-1)) + y) / i;
	}
	
	private void addTunnels(){
		int n = connections.size();
		Random r = new Random();
		for(int i=0; i < n; i++){
			//int x = r.nextInt(connections.get(i).getRoomA().getWidth()) + connections.get(i).getRoomA().getLeft();
			//int y = r.nextInt(connections.get(i).getRoomA().getLength()) + connections.get(i).getRoomA().getBottom();
			//int targetX = r.nextInt(connections.get(i).getRoomB().getWidth()) + connections.get(i).getRoomB().getLeft();
			//int targetY = r.nextInt(connections.get(i).getRoomB().getLength()) + connections.get(i).getRoomB().getBottom();
			
			new Tunneler(connections.get(i).getRoomA(), connections.get(i).getRoomB(), grid);
		}
	}
	
	private void addPavers(){
		int n = connections.size();
		Random r = new Random();
		for(int i=0; i < n; i++){
			
			new Paver(connections.get(i).getRoomA(), grid, roomMax);
		}
	}
	
	private void addConnections(){
		int size = rooms.length;
		for(int i=0; i < size; i++){
			RoomConnection[] rc = new RoomConnection[size-1];
			int add = 0;
			for(int j=0; j < size-1; j++){
				if(i == j){
					add = 1;
				}
				rc[j] = new RoomConnection(rooms[i], rooms[j+add]);
				
			}
			rooms[i].addConnections(rc);
		}
	}
	
	private void finalizeConnections(){
		int size = rooms.length;
		Random r = new Random();
		for(int i=0; i < size; i++){
			int n = numPath(r.nextInt(100));
			RoomConnection[] minrc = rooms[i].mininumConnections(rooms.length-1);
			int num = 0;
			for(int j=0; num < n; j++){
				boolean canAdd = true;
				
				for(int k=0; k < connections.size(); k++){
					if(connections.get(k).compareTo(minrc[j]) == 0){
						canAdd = false;
					}
				}
				
				if(canAdd){
					connections.add(minrc[j]);
					num++;
				}
				
			}
		}
	}
	
	private void checkConnections(boolean verify){
		for(int i=0; i<rooms.length; i++){
			boolean check[] = new boolean[rooms.length];
			rooms[i].setConnectivity(checkHelper(rooms[i], check, verify));
		}
	}
	
	private int checkHelper(Room room, boolean check[], boolean verify){
		int n = 1;
		if(totalConnects > maxConnects){
			return n;
		}
		check[room.getID()] = true;
		for(int i=0; i<connections.size(); i++){
			Room test = connections.get(i).compareEnd(room);
			if(test != null){
				if(!check[test.getID()]){
					boolean checker[];
					if(verify){
						checker = check;
					}
					else{
						checker = new boolean[check.length];
						for(int j=0; j<check.length; j++){
							checker[j] = check[j];
						}
					}
					totalConnects += 1;
					n += checkHelper(test, checker, verify);
				}
			}
		}
		return n;
	}
	
	private int maxConnectivity(){
		int connectivity = 0;
		int newConnectivity = 0;
		for(Room room : rooms){
			newConnectivity = room.getConnectivity();
			if(newConnectivity > connectivity){
				connectivity = newConnectivity;
			}
		}
		return connectivity;
	}
	
	private boolean correctConnectivity(){
		int topConnectivity = maxConnectivity * 7 / 8;
		int lowConnectivity = maxConnectivity / 8;
		int topCount = 0, lowCount = 0;
		for(Room room : rooms){
			if(room.getConnectivity() >= topConnectivity){
				topCount++;
			}
			else if(room.getConnectivity() <= lowConnectivity){
				lowCount++;
			}
		}
		System.out.print("Count: "+topCount+","+lowCount+" ");
		return (topCount <= connections.size() / 8 && lowCount >= rooms.length / 16            );
	}
	
	private int numPath(int r){
		if(r < randomDoubleValue){
			return 2;
		}
		else if( r < 95){
			return 1;
		}
		return 3;
		
	}
	
	
	
	@Override
	public void create () {
		
		
		batch = new SpriteBatch();
		img = new Texture("tex_box.png");
		regions = TextureRegion.split(img, 8, 8);
		renderRegion = TextureRegion.split(new Texture("basic_tiles.png"), 16, 16);
		
		black = regions[0][0];
		red = regions[1][0];
		blue = regions[0][1];
		white = regions[1][1];
		
		for(int i=0; i<8; i++){
			shade[i] = regions[2][i];
		}
		
		grid = new char[worldWidth][worldLength];
		renderGrid = new int[worldWidth][worldLength];
		boolean verify = true;
		long curTime = System.nanoTime();
		while(verify){
			verify = createWorld();
		}
		
		drawWorld();
		
		System.out.println((System.nanoTime()-curTime)/1000000000.0);
		
	}
	
	private boolean createWorld(){
		System.gc();
		totalConnects = 0;
		totalFails = 0;
		connections = new ArrayList<RoomConnection>();
		blankGrid();
		if(!addRooms(grid.length * grid[0].length / 450)){
			return true; 
		}
		addConnections();
		finalizeConnections();
		//addTunnels();
		addPavers();
		checkConnections(true);
		boolean verify = false;
		
		for(int i=0; i<rooms.length; i++){
			if(rooms[i].getConnectivity() < rooms.length){
				verify = true;
				break;
			}
			
		}
		
		checkConnections(false);
		if(totalConnects >= maxConnects){
			verify = true;
		}
		
		else if(connections.size() > rooms.length / 2 * 3){
			verify = true;
		}
		else{
			maxConnectivity = maxConnectivity();
			/*
			if(!correctConnectivity()){
				verify = true;
			}
			*/
		}
		
		System.out.println("Value: "+verify+" "+connections.size());
		return verify;
	}
	
	private void drawWorld(){
		for (int i=0; i< renderGrid.length; i++){
			for (int j=0; j < renderGrid[0].length; j++){		
				if(grid[i][j] == 'X'){
					renderGrid[i][j] = 1002;	
				}
				else{
					renderGrid[i][j] = 8000;
				}
			}
		}
		
		for(Room room : rooms){
			room.removeDoors(grid);
			RoomRenderer.renderRoom(room, grid, renderGrid);
		}
	}
	
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		 if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
			 boolean verify = true;
			 while(verify){
					verify = createWorld();
				}
			 drawWorld();
		 }
		
		batch.begin();
		
		if(!Gdx.input.isKeyPressed(Input.Keys.ENTER)){
		
			for (int i=0; i< renderGrid.length; i++){
				for (int j=0; j < renderGrid[0].length; j++){
					batch.draw(renderRegion[renderGrid[i][j] / 1000][renderGrid[i][j] % 1000], i*16, j*16);
				}
			}
			
		}
		else{
			
			for(int i=0; i<rooms.length; i++){
				for( int xx=rooms[i].getLeft(); xx <= rooms[i].getRight(); xx++){
					for (int yy=rooms[i].getBottom(); yy <= rooms[i].getTop(); yy++){
						int color = rooms[i].getConnectivity() * 8 / (maxConnectivity+1);
						if(color >= 8){
							color = 7;
						}
						batch.draw(shade[color ], xx*8, yy*8);
					}
				}
			}
			
	
			for (int i=0; i< grid.length; i++){
				for (int j=0; j < grid[0].length; j++){
					if(grid[i][j] == '.'){
						batch.draw(black, i*8, j*8);
					}
					else if(grid[i][j] == 'X'){
						batch.draw(white, i*8, j*8);
					}
					else if(grid[i][j] == 'D'){
						batch.draw(blue, i*8, j*8);
					}
				}
			}
			
			
		
		//System.out.println(connections.size());
		
		}
		batch.end();
	}
	
	
}
