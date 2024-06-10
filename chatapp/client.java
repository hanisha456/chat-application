import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
public class client extends JFrame {
    Socket client;
    BufferedReader br;
    PrintWriter pw;

        JLabel l= new JLabel("chat chat");
        JTextArea ta= new JTextArea();
        JTextField tf= new JTextField();
        Font font= new Font("Bebas Neue",Font.PLAIN, 32);

        public client()
        {
            try {
                
              System.out.println("sending request");
              client = new Socket("192.168.1.9",7777);
              System.out.println("established"); 
              br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            pw = new PrintWriter(client.getOutputStream());
            createGUI();
            handleEvents();
            reading();
            writing();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void handleEvents(){
            tf.addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    System.out.println("key released"+e.getKeyCode());
                    if(e.getKeyCode()==10&&!client.isClosed()){
                       String contentToSend= tf.getText();
                       pw.println(contentToSend);
                       ta.append("Me :"+contentToSend+"\n");
                       if(contentToSend=="exit"){
                        tf.setEnabled(false);
                       }
                       pw.flush();
                       tf.setText("");
                       tf.requestFocus();
                    }
                }
            });
        }
         
        private void createGUI(){
                this.setTitle("text jet");
                this.setSize(500,500);
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setLocationRelativeTo(null) ;
                heading.setFont(font);
                msgarea.setFont(font);
                msgfield.setFont(font);
                heading.setHorizontalAlignment(SwingConstants.CENTER); 
                heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
                heading.setIcon(new ImageIcon("istockphoto-641830698-612x612.jpg"));
                heading.setHorizontalTextPosition(SwingConstants.CENTER);
                heading.setVerticalTextPosition(SwingConstants.BOTTOM);
                msgfield.setHorizontalAlignment(SwingConstants.CENTER);
                this.setLayout(new BorderLayout());
                this.add(heading,BorderLayout.NORTH);
                this.add(msgarea,BorderLayout.CENTER);
                this.add(msgfield,BorderLayout.SOUTH);
                this.setVisible(true);
        }
        public void reading(){
            Runnable r1=()-> {
                 System.out.println("ready to read");
                 try{
                 while(!socket.isClosed()){
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("terminated the chat");
                        JOptionPane.showMessageDialog(this, "server terminated the chat");
                        msgfield.setEnabled(false);
                        socket.close();
                        break;
                    }
                    msgarea.append("server:" +msg+"\n");
                    System.out.println("client:"+msg);
                 }
                }
                catch(Exception e){
                    e.printStackTrace();
                 }
                };
            new Thread(r1).start();
        }
         public void writing(){
            Runnable r2 =()-> {
                System.out.println("write started");
                try{
              while(true && !socket.isClosed()){
                    BufferedReader b1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = b1.readLine();
                    out.println(content);
                    out.flush();
              }
            }
            catch(Exception e){
                e.printStackTrace();
             }
            };
            new Thread (r2).start();
         }
    
        public static void main(String [] args)
        {
            System.out.println("this is client...");
            new client();
         }
    
}
