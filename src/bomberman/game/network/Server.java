package bomberman.game.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import bomberman.game.Level;
import bomberman.game.Settings;
import bomberman.game.character.BomberHuman;
import bomberman.game.objects.Bomb;

/**
 * Server that meets the network protocol.
 * 
 */
public class Server implements Reader {
	/**
	 * Scanner of socket's inputStream.
	 */
	private final Scanner in;
	/**
	 * PrintWriter of socket's outputStream.
	 */
	private final PrintWriter out;

	/**
	 * Constructs a Server object. Uses port 9001.
	 * 
	 * @param level
	 *            Valid level file generated by Level.toString() method.
	 * @throws IOException
	 *             on network issues.
	 */
	public Server(final String level) throws IOException {
		ServerSocket server = new ServerSocket(9001);

		Socket client = server.accept();
		final Scanner in;
		in = new Scanner(client.getInputStream());
		this.in = in;
		PrintWriter out = new PrintWriter(client.getOutputStream());
		this.out = out;
		out.println(level);
		out.println("poison");
		out.flush();

		while (!in.hasNext()) {

		}
		String tmp = in.nextLine();
		if (!tmp.equals("ack"))
			System.out
					.println("network debug print - answer to level was not as expected");

	}

	@Override
	public void read(final BomberHuman bman, final List<Bomb> bombs) {
		while (!in.hasNext()) {

		}

		for (int i = 0; i < 2; i++) {
			final String line = in.nextLine();
			if (line.startsWith("mv")) {
				String[] split = line.split(" ");
				int posX = Integer.parseInt(split[1]);
				int posY = Integer.parseInt(split[2]);
				bman.setPosX(posX);
				bman.setPosY(posY);
			} else if (line.startsWith("bomb")) {
				String[] split = line.split(" ");
				int flag = Integer.parseInt(split[1]);
				if (flag == 1) {
					bombs.add(new Bomb(bman.getPosX(), bman.getPosY(),
							Settings.TIMERCONSTANT));
				}
			}
		}
	}

	@Override
	public void write(final String str) {
		String[] split = str.split("\n");
		for (int i = 0; i < split.length; i++) {
			out.println(split[i]);
		}
		out.flush();

	}

	@Override
	@Deprecated
	public Level readLevel() {
		return null;
	}
}
