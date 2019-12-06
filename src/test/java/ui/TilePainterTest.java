package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.InOrder;
import org.mockito.Matchers;
import org.mockito.Mockito;

import engine.GameEngine;
import tiles.TileType;
import values.TileColorMap;
import values.TunableParameters;

public class TilePainterTest {

	private final int TILE_WIDTH = 10;
	private final int TILE_HEIGHT = 20;
	private final int X = 2;
	private final int Y = 3;
	private int tileWidth = 100;
	private int tileHeight = 120;

	Graphics graphics;
	TilePainter tilePainter;
	GameEngine gameEngine;
	GamePanel gamePanel;
	TileType tileType = TileType.NPC;

	@Before
	public void setUp() {
		tilePainter = new TilePainter();
		graphics = Mockito.mock(Graphics.class);
		gamePanel = Mockito.mock(GamePanel.class);
		gameEngine = Mockito.mock(GameEngine.class);
	}

	@Test
	public void paint_tiles() {
		GameEngine game = Mockito.mock(GameEngine.class);
		Mockito.when(game.getLevelHorizontalDimension()).thenReturn(X);
		Mockito.when(game.getLevelVerticalDimension()).thenReturn(Y);
		Mockito.when(game.getTileFromCoordinates(1, 1)).thenReturn(TileType.NOT_PASSABLE);
		Mockito.when(game.getTileFromCoordinates(AdditionalMatchers.not(Matchers.eq(1)),
				AdditionalMatchers.not(Matchers.eq(1)))).thenReturn(TileType.PASSABLE);

		tilePainter.paintTiles(graphics, game, TILE_WIDTH, TILE_HEIGHT);

		InOrder inOrder = Mockito.inOrder(graphics);
		inOrder.verify(graphics).setColor(TileColorMap.get(TileType.PASSABLE));
		inOrder.verify(graphics).fillRect(0, 0, 10, 20);
		inOrder.verify(graphics).fillRect(0, 20, 10, 20);
		inOrder.verify(graphics).fillRect(0, 40, 10, 20);
		inOrder.verify(graphics).fillRect(10, 0, 10, 20);
		inOrder.verify(graphics).setColor(TileColorMap.get(TileType.NOT_PASSABLE));
		inOrder.verify(graphics).fillRect(10, 20, 10, 20);
		inOrder.verify(graphics).fillRect(10, 40, 10, 20);

	}

	@Test
	public void paint_player() {

		tilePainter.paintPlayer(graphics, X, Y, TILE_WIDTH, TILE_HEIGHT, TileType.PLAYER);

		Mockito.verify(graphics).fillRect(20, 60, 10, 20);
	}

	@Test
	public void show_win() {
		gamePanel.paint(graphics);
		if (gameEngine.isWin()) {
			graphics.setColor(Color.RED);
			graphics.setFont(new Font("Times New Roman", Font.BOLD, 50));
			graphics.drawString("YOU WIN", TunableParameters.SCREEN_WIDTH / 2 - 125,
					TunableParameters.SCREEN_HEIGHT / 2);
			Mockito.verify(graphics).drawString("YOU WIN", 900, 600);
		}

	}

}
