import java.awt.Color;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.sound.sampled.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

class GraphicObject {
	
 BufferedImage img = null;
 int x = 0, y = 0;
 
 public GraphicObject(String name) {
 try {
 img = ImageIO.read(new File(name));
 } catch (IOException e) {
 System.out.println(e.getMessage());
 System.exit(0);
 }
 } public void update() {
 }
 public void draw(Graphics g) {
 g.drawImage(img, x, y, null);
 }
 public void keyPressed(KeyEvent event) {
 }
}
class Missile extends GraphicObject {
 boolean launched = false;
 public Missile(String name) {
 super(name);
 y = -200;
 }
 public void update() {
 if (launched)
 y -= 50;
 if (y < -100)
 launched = false;
 }
 public void keyPressed(KeyEvent event, int x, int y) {
 if (event.getKeyCode() == KeyEvent.VK_SPACE) {
 launched = true;
 this.x = x;
 this.y = y;
 }
 }
 public void getCrashed(Enemy enemy) {
 if (this.x < enemy.x + enemy.img.getWidth() &&
 this.x + this.img.getWidth() > enemy.x &&
 this.y < enemy.y + enemy.img.getHeight() && this.y + this.img.getHeight() > enemy.y) {
 enemy.hp -= 10;
 launched = false;
 y = -200;
 }
 }
 public void getCrashed(Enemy_2 enemy) {
 if (this.x < enemy.x + enemy.img.getWidth() &&
 this.x + this.img.getWidth() > enemy.x &&
 this.y < enemy.y + enemy.img.getHeight() &&
 this.y + this.img.getHeight() > enemy.y) {
 enemy.hp -= 20;
 launched = false;
 y = -200;
 }
 }
}
class Missile_2 extends GraphicObject {
int dx=10;
int dy=-30;
 boolean launched = false;
 public Missile_2(String name,Enemy_2 enemy_2) {
 super(name);
 x = enemy_2.x + enemy_2.img.getWidth() / 2;
 y = enemy_2.y + enemy_2.img.getHeight()/2;
 }
 public void update() {
 if (launched)
 y -= dy;
 
 }
 public void fireTowards(SpaceShip spaceShip) {
  int targetX = spaceShip.x + spaceShip.img.getWidth() / 2;
 int targetY = spaceShip.y + spaceShip.img.getHeight() / 2;
 
 
 launched = true;
 }
}
class Rock extends GraphicObject {
 public static final int WIDTH = 100;
 int dy = -25;
 private int getRandomX() {
 return new Random().nextInt(800);
 }
 public Rock(String name) {
 super(name);
 x = getRandomX();
 }
 public void update() {
 y -= dy;
 if (y < -800) {
 y = 0;
 x = getRandomX();
 }
 }
}
class Enemy extends GraphicObject {
 int hp = 30;
 int dx = -10;
 private SpaceShip spaceShip;
 public Enemy(String name, SpaceShip spaceShip) {
 super(name); x = 800;
 y = 0;
 this.spaceShip = spaceShip;
 }
 public void update() {
 x += dx;
 if (x < 0)
 dx = +10;
 if (x > 800)
 dx = -10;
 if(hp==0)
 {
 x=1000;
 
 }
 if (spaceShip.currentRound > 2) {
 hp = 0;
 x = 1000;
 y = 0;
 }
 
 int spaceshipCenterX = spaceShip.x + spaceShip.img.getWidth() / 2;
 int spaceshipCenterY = spaceShip.y + spaceShip.img.getHeight() / 2;
 if (Math.abs(x - spaceshipCenterX) < 100 && Math.abs(y - spaceshipCenterY) < 100) {
 
 spaceShip.hp = 0;
 spaceShip.x = 150;
 spaceShip.y = 350;
 //완전히 닿았을때 오류
 }
 }
}
 
class Enemy_2 extends GraphicObject {
 int hp = 20;
 int dx = 20; private SpaceShip targetSpaceShip;
 private Timer missileTimer;
 List<Missile_2> missiles;
 public Enemy_2(String name, SpaceShip targetSpaceShip) {
 super(name);
 x = 300;
 y = 0;
 this.targetSpaceShip = targetSpaceShip;
 this.missiles = new ArrayList<>();
 missileTimer = new Timer(500, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 fireMissile();
 }
 });
 missileTimer.start();
 }
 public void update() {
 if (targetSpaceShip.currentRound < 3) {
 
 x = 10000;
 y = 0;
 } else {
 
 x = 300;
 y = 0;
 updateMissiles(); 
 }
 if (targetSpaceShip.x < x) {
 x -= dx;
 } if(targetSpaceShip.x >x) {
 x += dx;
 }
 weWin(); }
 public void updateMissiles() {
 for (Missile_2 missile : missiles) {
 missile.update();
 }
 missiles.removeIf(missile -> missile.y < -800);
 }
 private void fireMissile() {
 Missile_2 missile = new Missile_2("missile_2.png",this);
 missile.x = this.x + this.img.getWidth() / 2 - missile.img.getWidth() / 2;
 missile.y = this.y + this.img.getHeight();
 missile.launched = true;
 missile.fireTowards(targetSpaceShip);
 missiles.add(missile);
 }
 private void weWin() {
 if (hp <= 0) {
 JOptionPane.showMessageDialog(null, "YOU WIN!", "Game Over", 
JOptionPane.INFORMATION_MESSAGE);
 System.exit(0);
 }
 }
}
class SpaceShip extends GraphicObject {
 int hp = 200;
 static int currentRound = 1;
 int speed = 10;
 public SpaceShip(String name) {
 super(name);
 x = 300;
 y = 650;
 } public void keyPressed(KeyEvent event) {
 switch (event.getKeyCode()) {
 case KeyEvent.VK_LEFT:
 x -= speed;
 break;
 case KeyEvent.VK_RIGHT:
 x += speed;
 break;
 case KeyEvent.VK_UP:
 y -= speed;
 if (y <= -100) {
 currentRound=currentRound+1;
 
 y = 700;
 }
 break;
 case KeyEvent.VK_DOWN:
 y += speed;
 break;
 }
 }
 public int getSpeed() {
 return speed;
 }
 public void getHit() {
 hp -= 20;
 if (hp <= 0) {
 hp = 0;
 
}
 }
}
class Background extends GraphicObject {
 private SpaceShip spaceShip;
 public Background(String name, int y, SpaceShip spaceShip) {
 super(name); this.y = y;
 this.spaceShip = spaceShip;
 }
 public void update() {
 y += spaceShip.getSpeed();
 if (y > img.getHeight()) {
 y = -img.getHeight();
 }
 }
}
class MyPanel extends JPanel {
 Clip bgmClip;
 Enemy enemy;
 SpaceShip spaceship;
 Missile missile;
 List<Rock> rocks;
 List<Background> backgrounds;
 Enemy_2 enemy2; 
 Missile_2 missile_2;
 List<Missile_2> missiles;
 private int getRandomX() {
 int minX = 0;
 int maxX = getWidth() - Rock.WIDTH;
 int gap = Rock.WIDTH * 2;
 return (int) (Math.random() * ((maxX - minX) / gap)) * gap + minX;
 }
 private void checkSpaceshipCollision() {
 if (spaceship.hp <= 0) {
 JOptionPane.showMessageDialog(this, "Game Over! Your spaceship has been destroyed.", 
"Game Over", JOptionPane.INFORMATION_MESSAGE);
 System.exit(0);
 }
 }
 public MyPanel() {
 super();
 setFocusable(true);
 playBGM("bgm/star_wars.wav"); rocks = new ArrayList<>();
 setBackground(Color.BLACK);
 backgrounds = new ArrayList<>();
 spaceship = new SpaceShip("razor crest.png");
 for (int i = 0; i < 4; i++) {
 Background background = new Background("background_" + (i + 1) + ".png", i * 800, 
spaceship);
 backgrounds.add(background);
 }
 for (int i = 0; i < 2; i++) {
 Rock rock = new Rock("rock.png");
 rock.y = 0;
 rock.x = getRandomX();
 rocks.add(rock);
 }
 enemy = new Enemy("enemy.png", spaceship);
 missile = new Missile("missile.png");
 enemy2 = new Enemy_2("enemy_2.png", spaceship);
 missiles = new ArrayList<>();
 Timer timer = new Timer(50, new ActionListener() {
 @Override
 public void actionPerformed(ActionEvent e) {
 for (Rock rock : rocks) {
 rock.update();
 }
 for (Background background : backgrounds) {
 background.update();
 }
 enemy.update();
 enemy2.update();
 spaceship.update();
 missile.getCrashed(enemy);
 missile.getCrashed(enemy2);
 missile.update(); 
 
 Missile_2 newMissile = new Missile_2("missile_2.png", enemy2);
 newMissile.fireTowards(spaceship);
 missiles.add(newMissile);
 
 
 for (Missile_2 missile_2 : new ArrayList<>(missiles)) {
 missile_2.update();
 }
 checkRockCollision();
 checkMissileCollision();
 for (Rock rock : new ArrayList<>(rocks)) {
 if (rock.y > getHeight()) {
 rocks.remove(rock);
 Rock newRock = new Rock("rock.png");
 newRock.x = getRandomX();
 newRock.y = 0;
 rocks.add(newRock);
 }
 }
 if (spaceship.y <= -100) {
 spaceship.x = 500;
 spaceship.y = 700;
 }
 checkSpaceshipCollision();
 spaceship.update();
 repaint();
 }
 });
 timer.start();
 addKeyListener(new KeyAdapter() {
 @Override
 public void keyPressed(KeyEvent event) {
 spaceship.keyPressed(event);
 missile.keyPressed(event, spaceship.x, spaceship.y); }
 });
 }
 
 private void checkRockCollision() {
 for (Rock rock : new ArrayList<>(rocks)) {
 if (spaceship.x < rock.x + rock.img.getWidth() &&
 spaceship.x + spaceship.img.getWidth() > rock.x &&
 spaceship.y < rock.y + rock.img.getHeight() &&
 spaceship.y + spaceship.img.getHeight() > rock.y) {
 spaceship.hp -= 7;
 if (spaceship.hp <= 0) {
 spaceship.hp = 0;
 spaceship.x = 150;
 spaceship.y = 350;
 }
 rocks.remove(rock);
 Rock newRock = new Rock("rock.png");
 newRock.x = getRandomX();
 rocks.add(newRock);
 }
 }
 }
 private void checkMissileCollision() {
 for (Missile_2 missile : new ArrayList<>(enemy2.missiles)) {
 if (spaceship.x < missile.x + missile.img.getWidth() &&
 spaceship.x + spaceship.img.getWidth() > missile.x &&
 spaceship.y < missile.y + missile.img.getHeight() &&
 spaceship.y + spaceship.img.getHeight() > missile.y) {
 spaceship.getHit();
 enemy2.missiles.remove(missile);
 }
 }
 } private void playBGM(String filePath) {
 try {
 File audioFile = new File(filePath);
 AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
 AudioFormat format = audioInputStream.getFormat();
 DataLine.Info info = new DataLine.Info(Clip.class, format);
 bgmClip = (Clip) AudioSystem.getLine(info);
 bgmClip.open(audioInputStream);
 bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
 bgmClip.start();
 } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
 e.printStackTrace();
 }
 }
 public void paint(Graphics g) {
 super.paint(g);
 for (Background background : backgrounds) {
 background.draw(g);
 }
 enemy.draw(g);
 spaceship.draw(g);
 missile.draw(g);
 enemy2.draw(g); 
 
 for (Rock rock : rocks) {
 rock.draw(g);
 }
 
 for (Missile_2 missile : missiles) {
 missile.draw(g);
 }
 g.setColor(Color.WHITE);
 g.drawString("Enemy HP: " + enemy.hp, 10, 20);
 g.drawString("Spaceship HP: " + spaceship.hp, 10, 40);
 g.drawString("BOSS HP: " + enemy2.hp, 10, 60);
 }
}public class Play_Frame extends JFrame {
 public Play_Frame() {
 setTitle("GG");
 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 MyPanel myPanel = new MyPanel();
 add(myPanel);
 setSize(800, 800);
 setVisible(true);
 setLocationRelativeTo(null);
 }
 public static void main(String[] args) {
 new Play_Frame();
 }
}