import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class mainFrame{
    private JPanel jPanel;
    private JButton upload2Button;
    private JTextField textField1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton upload1Button;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JTextField textField2;
    private JComboBox comboBox5;
    private JComboBox comboBox6;
    private JButton compareButton;

    public mainFrame(){
        JFrame jFrame = new JFrame();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setTitle("Excel Tool");
        jFrame.setContentPane(jPanel);
        jFrame.setBounds(100, 100, 600, 600);
        textField1.setSize(200,10);
        upload1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
                jfc.showDialog(new JLabel(), "选择");
                File file=jfc.getSelectedFile();
                if(file.isDirectory()){
                    System.out.println("文件夹:"+file.getAbsolutePath());
                }else if(file.isFile()){
                    System.out.println("文件:"+file.getAbsolutePath());
                }
                System.out.println(jfc.getSelectedFile().getName());
            }
        });
    }

    public static void main(String[] args){
        new mainFrame();
    }




}
