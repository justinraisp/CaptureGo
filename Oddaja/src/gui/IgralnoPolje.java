package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import logika.Igralec;
import logika.Stanje;
import logika.Zeton;
import splosno.Poteza;
import vodja.Vodja;
import vodja.VrstaIgralca;

@SuppressWarnings("serial")
public class IgralnoPolje extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	protected Color barvaPrvega;
	protected Color barvaDrugega;
	protected Color barvaPlosce;
	protected Color barvaRoba;
	protected Color barvaBorda;
	protected Stroke debelinaRoba;
	protected double polmer;
	protected double premer;
	public static JLabel naVrsti = new JLabel("");
	private JLabel koViolationLabel;

	private final static double SIRINA_CRTE = 0.08;

	public IgralnoPolje(int sirina, int visina) {
		super();

		// nastaviPolje(Vodja.igra.velikostPlosce);
		// napisNaVrsti(Vodja.igra.naPotezi);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		barvaPrvega = Color.BLACK;
		barvaDrugega = Color.WHITE;
		barvaRoba = Color.BLACK;
		barvaPlosce = Color.WHITE;
		this.setPreferredSize(new Dimension(sirina, visina));
		this.setBackground(barvaPlosce);
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);

	}

	public static void napisNaVrsti(Igralec igralec) {
		if (Vodja.igra.stanje == Stanje.ZMAGA_BELI && Vodja.igra.tipZmage < 1) {
			naVrsti.setText("Črni se je predal, zmagal je beli!");
		} else if (Vodja.igra.stanje == Stanje.ZMAGA_BELI && Vodja.igra.tipZmage == 1) {
			naVrsti.setText(String.format("Konec igre, zmagal je beli %d proti %d", Vodja.igra.beliTocke,
					Vodja.igra.crniTocke));
		} else if (Vodja.igra.stanje == Stanje.ZMAGA_BELI && Vodja.igra.tipZmage == 2) {
			naVrsti.setText(String.format("Konec igre, zmagal je beli!", Vodja.igra.beliTocke, Vodja.igra.crniTocke));
		} else if (Vodja.igra.stanje == Stanje.ZMAGA_CRNI && Vodja.igra.tipZmage < 1) {
			naVrsti.setText("Beli se je predal, zmagal je črni!");
		} else if (Vodja.igra.stanje == Stanje.ZMAGA_CRNI && Vodja.igra.tipZmage == 1) {
			naVrsti.setText(String.format("Konec igre, zmagal je črni %d proti %d", Vodja.igra.crniTocke,
					Vodja.igra.beliTocke));
		} else if (Vodja.igra.stanje == Stanje.ZMAGA_CRNI && Vodja.igra.tipZmage == 2) {
			naVrsti.setText(String.format("Konec igre, zmagal je črni!", Vodja.igra.crniTocke, Vodja.igra.beliTocke));
		} else {
			if (igralec == Igralec.ČRNI) {
				naVrsti.setText("Na vrsti je črni");
			} else
				naVrsti.setText("Na vrsti je beli");
		}
	}

	// polovica sirine kvadrata
	private int sirinaKvadrata() {
		return (Math.min(getWidth(), getHeight()) - 40) / (Vodja.igra.velikostPlosce);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (Vodja.igra != null) {
			int w = sirinaKvadrata();
			int odmikSirina = (getWidth() - (Vodja.igra.velikostPlosce * w)) / 2;
			int odmikVisina = (getHeight() - (Vodja.igra.velikostPlosce * w)) / 2;
			int polovica = sirinaKvadrata() / 2;
			Color barvaBorda = new Color(237, 204, 153);
			polmer = w / 4;
			premer = 2 * polmer;

			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			g2.setColor(barvaBorda); // Set the color of the background
			g2.fillRect(odmikSirina, odmikVisina, Vodja.igra.velikostPlosce * w, Vodja.igra.velikostPlosce * w);

			g2.setColor(barvaRoba);
			g2.setStroke(new BasicStroke((float) (w * SIRINA_CRTE)));

			for (int i = 0; i < Vodja.igra.velikostPlosce; i++) {
				g2.drawLine((i * w + odmikSirina + polovica), (int) (odmikVisina + polovica),
						(int) (i * w + odmikSirina + polovica),
						(int) (Vodja.igra.velikostPlosce * w + odmikVisina - polovica));
			}
			for (int j = 1; j < Vodja.igra.velikostPlosce + 1; j++) {
				g2.drawLine((int) (odmikSirina + polovica), (int) (j * w + odmikVisina - polovica),
						(int) ((Vodja.igra.velikostPlosce - 1) * w + odmikSirina + polovica),
						(int) (j * w + odmikVisina - polovica));
			}
			for (int i = 0; i < Vodja.igra.velikostPlosce + 1; i++) {
				for (int j = 0; j < Vodja.igra.velikostPlosce + 1; j++) {
					int x = (int) ((i) * w + odmikSirina);
					int y = (int) ((int) ((j) * w) + odmikVisina);
					if (Vodja.igra.polje.grid[i][j] == Zeton.CRNI) {
						g2.setColor(barvaPrvega);
						if (Vodja.igra.captureGo) {
							if (Vodja.igra.polje.isCaptured(i, j)) { // Uporabljali za capturego
								g2.setColor(Color.RED);
								Vodja.igra.stanje = Stanje.ZMAGA_BELI;
								napisNaVrsti(Vodja.igra.naPotezi);
							}
						}
						g2.fillOval(round(x - 4 * polmer), round(y - 4 * polmer), (int) (2 * premer),
								(int) (2 * premer));
					} else if (Vodja.igra.polje.grid[i][j] == Zeton.BELI) {
						// g2.setColor(Color.BLACK);
						g2.setColor(barvaDrugega);
						if (Vodja.igra.captureGo) {
							if (Vodja.igra.polje.isCaptured(i, j)) { // Uporabljali za capturego
								g2.setColor(Color.RED);
								Vodja.igra.stanje = Stanje.ZMAGA_CRNI;
								napisNaVrsti(Vodja.igra.naPotezi);
							}
						}
						g2.fillOval(round(x - 4 * polmer), round(y - 4 * polmer), (int) (2 * premer),
								(int) (2 * premer));
					}
				}
			}
		} else {
			naVrsti.setText("Izberite vrsto igre");
		}
	}

	public List<Point> izracunajPresecisca() {
		List<Point> presecisca = new ArrayList<>();
		double w = sirinaKvadrata();
		double odmikSirina = (getWidth() - (Vodja.igra.velikostPlosce * w)) / 2;
		double odmikVisina = (getHeight() - (Vodja.igra.velikostPlosce * w)) / 2;
		double polovica = w / 2;

		for (int i = 0; i < Vodja.igra.velikostPlosce; i++) {
			int x = (int) (i * w + odmikSirina + polovica);

			for (int j = 1; j < Vodja.igra.velikostPlosce + 1; j++) {
				int y = (int) (j * w + odmikVisina - polovica);
				Point presecisce = new Point(x, y);
				presecisca.add(presecisce);
			}
		}
		return presecisca;
	}

	private int round(double x) {
		return (int) (x + 0.5);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (Vodja.igra != null && Vodja.igra.stanje == Stanje.V_TEKU && Vodja.vrstaIgralca.get(Vodja.igra.naPotezi()) == vodja.VrstaIgralca.C) {
			Poteza updated = null;
			int w = sirinaKvadrata();
			int odmikSirina = (getWidth() - (Vodja.igra.velikostPlosce * w)) / 2;
			int odmikVisina = (getHeight() - (Vodja.igra.velikostPlosce * w)) / 2;
			int polovica = sirinaKvadrata() / 2;
			List<Point> presecisca = izracunajPresecisca();
			Point clickedPoint = e.getPoint();
			for (Point presecisce : presecisca) {
				double distance = clickedPoint.distance(presecisce);
				if (distance < polovica) {
					int x = (int) (1 + (presecisce.x - odmikSirina) / w);
					int y = (int) (1 + (presecisce.y - odmikVisina) / w);
					updated = new Poteza(x, y);
				}
			}
			Vodja.igrajClovekovoPotezo(updated);
			napisNaVrsti(Vodja.igra.naPotezi);
			if (updated != null && Vodja.igra.isKoRuleViolation(updated.x(), updated.y())) {
				naVrsti.setText("KO kršitev, izberite drugo potezo!");
				int delay = 5000; // 5 sekund kaze za napako

				Timer timer = new Timer(delay, event -> {
					napisNaVrsti(Vodja.igra.naPotezi);
					repaint();
				});
				timer.setRepeats(false);
				timer.start();
			}
			if (updated != null && Vodja.igra.isSuicideViolation(updated.x(), updated.y())) {
				naVrsti.setText("Samomor kršitev, izberite drugo potezo!");
				int delay = 5000; // 5 sekund kaze za napako

				Timer timer = new Timer(delay, event -> {
					napisNaVrsti(Vodja.igra.naPotezi);
					repaint();
				});
				timer.setRepeats(false);
				timer.start();
			}
			repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
