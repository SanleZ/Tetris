//package tetris;

public abstract class Figures {
	private int[][] figure;
	private String name;

	public Figures(String name, int[][] figure) {
		this.figure = figure;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setArray(int[][] figure) {
		this.figure = figure;
	}

	public int[][] getArray() {
		return figure;
	}

	abstract void rotate();
}

class Figures1s extends Figures {
	private int[][] figure1s;

	Figures1s(String name, int[][] figure) {
		super(name, figure);
	}

	@Override
	void rotate() {
		figure1s = this.getArray();
		if (figure1s[0][0] == 1)
			this.setArray(new int[][] { { 0, 1, 1 }, { 1, 1, 0 }, { 0, 0, 0 } });
		else
			this.setArray(new int[][] { { 1, 0, 0 }, { 1, 1, 0 }, { 0, 1, 0 } });
	}
}

class Figures2s extends Figures {
	private int[][] figure2s;

	Figures2s(String name, int[][] figure) {
		super(name, figure);
	}

	@Override
	void rotate() {
		figure2s = this.getArray();
		if (figure2s[0][0] == 0)
			this.setArray(new int[][] { { 1, 1, 0 }, { 0, 1, 1 }, { 0, 0, 0 } });
		else
			this.setArray(new int[][] { { 0, 1, 0 }, { 1, 1, 0 }, { 1, 0, 0 } });
	}
}

class Figures3 extends Figures {
	private int[][] figure3;

	Figures3(String name, int[][] figure) {
		super(name, figure);
	}

	@Override
	void rotate() {
		int[][] figureR = new int[3][3];
		figure3 = this.getArray();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int newX = (int) Math.round((i - 1) * Math.cos(Math.PI / 2) - (j - 1) * Math.sin(Math.PI / 2));
				int newY = (int) Math.round((i - 1) * Math.sin(Math.PI / 2) - (j - 1) * Math.cos(Math.PI / 2));
				figureR[i][j] = figure3[newX + 1][newY + 1];

			}
		}
		this.setArray(figureR);
	}
}

class FiguresL extends Figures {
	private int[][] figureL;

	FiguresL(String name, int[][] figure) {
		super(name, figure);
	}

	@Override
	void rotate() {
		figureL = this.getArray();
		if (figureL[0][1] == 1)
			this.setArray(new int[][] { { 0, 0, 0, 0 }, { 1, 1, 1, 1 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } });
		else
			this.setArray(new int[][] { { 0, 1, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 0, 0 }, { 0, 1, 0, 0 } });
	}
}

class FiguresS extends Figures {
	FiguresS(String name, int[][] figure) {
		super(name, figure);
	}

	@Override
	void rotate() {
	}
}
