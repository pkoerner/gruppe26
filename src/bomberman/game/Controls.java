package bomberman.game;

import java.util.List;

import bomberman.game.character.BomberHuman;
import bomberman.game.objects.Bomb;
import bomberman.gui.StdDraw;

public class Controls {

	private final int[][] field;
	private final int TILESIZE;
	private final int height;
	private final int width;

	// private final int COLLISIONBOX = 10;
	private final int COLLISIONBOX = 0;
	private final int BOMBTICKS = 300;

	public Controls(final Board board, final int TILESIZE) {
		this.field = board.getField();
		this.TILESIZE = TILESIZE;
		this.height = board.getHeight();
		this.width = board.getWidth();
	}

	public boolean canMoveThere(final char direction, final BomberHuman b) {
		final char[] enabledDirections = { 'w', 'a', 's', 'd' };
		if (!arrayContainsChar(enabledDirections, direction))
			throw new IllegalArgumentException(
					"this method does not support this key input");

		final int posX = b.getPosX();
		final int posY = b.getPosY();
		final int arrayPosX = getArrayPos(posX);
		final int arrayPosY = getArrayPos(posY);
		final int speed = b.getSpeed();

		// FIXME: okay, that's dirty here. refactor me.
		switch (direction) {
		case 'w':
			final int newArrayPosY = getArrayPos(posY + speed + COLLISIONBOX);
			if (newArrayPosY < height)
				if (field[newArrayPosY][arrayPosX] == 0)
					return true;
			break;
		case 's':
			final int newArrayPosY2 = getArrayPos(posY - speed - COLLISIONBOX);
			if (newArrayPosY2 >= 0)
				if (field[newArrayPosY2][arrayPosX] == 0)
					return true;
			break;
		case 'a':
			final int newArrayPosX = getArrayPos(posX - speed - COLLISIONBOX);
			if (newArrayPosX >= 0)
				if (field[arrayPosY][newArrayPosX] == 0)
					return true;
			break;
		case 'd':
			final int newArrayPosX2 = getArrayPos(posX + speed + COLLISIONBOX);
			if (newArrayPosX2 < width)
				if (field[arrayPosY][newArrayPosX2] == 0)
					return true;
			break;
		}
		return false;
	}

	private boolean arrayContainsChar(final char[] arr, final char c) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == c)
				return true;
		}
		return false;
	}

	public int getArrayPos(final int pos) {
		if (pos < 0)
			return -1;
		return pos / TILESIZE;
	}

	public void doSomethingWithInput(final BomberHuman bman,
			final List<Bomb> bombs) {

		if (StdDraw.hasNextKeyTyped()) {
			final char c = StdDraw.nextKeyTyped();

			switch (c) {
			case 'w':
				if (canMoveThere('w', bman))
					bman.moveUp();
				else
					bman.moveVertically(TILESIZE - 1 - COLLISIONBOX
							- (bman.getPosY() % TILESIZE));
				break;
			case 's':
				if (canMoveThere('s', bman))
					bman.moveDown();
				else
					bman.moveVertically(COLLISIONBOX
							- (bman.getPosY() % TILESIZE));
				break;
			case 'a':
				if (canMoveThere('a', bman))
					bman.moveLeft();
				else
					bman.moveHorizontally(COLLISIONBOX
							- (bman.getPosX() % TILESIZE));
				break;
			case 'd':
				if (canMoveThere('d', bman))
					bman.moveRight();
				else
					bman.moveHorizontally(TILESIZE - 1 - COLLISIONBOX
							- (bman.getPosX() % TILESIZE));
				break;
			case 'e':
				dropBomb(bman, bombs);
			}
		}
	}

	private void dropBomb(final BomberHuman bman, final List<Bomb> bombs) {
		final int posX = bman.getPosX();
		final int posY = bman.getPosY();
		final Bomb b = new Bomb(posX, posY, BOMBTICKS);
		bombs.add(b);
	}
}
