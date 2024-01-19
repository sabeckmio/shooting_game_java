import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
class FirstPanel extends JPanel implements KeyListener{
FirstPanel()
{
setBackground(Color.BLACK);
addKeyListener(this);
setFocusable(true);
}
 public void keyPressed(KeyEvent event) {
 if (event.getKeyCode() == KeyEvent.VK_ENTER) {
 new Play_Frame();
 }
 }
 
 
public void paint(Graphics g) {
 super.paint(g);
 g.setFont(g.getFont().deriveFont(30f));
 
 g.setColor(Color.GREEN);
 
 String text = "A long time ago in a galaxy far, far away...."; int x = 100;
 int y = 400;
 g.drawString(text, x, y);
 g.setColor(Color.YELLOW);
 g.setFont(g.getFont().deriveFont(15f));
 String text_2="PRESS ENTER TO START";
 int x_1=300;
 int y_2=700;
 g.drawString(text_2, x_1, y_2);
 }
@Override
public void keyTyped(KeyEvent e) {
// TODO Auto-generated method stub
}
@Override
public void keyReleased(KeyEvent e) { 
// TODO Auto-generated method stub
}
}
public class First_Frame extends JFrame {
 public First_Frame() {
 setTitle("GG");
 setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 FirstPanel FirstPanel = new FirstPanel();
 add(FirstPanel);
 setSize(800, 800);
 setVisible(true);
 setLocationRelativeTo(null);
 } public static void main(String[] args) {
 new First_Frame();
 }
}
