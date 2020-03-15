import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author goldt
 */
public class excelDeleteRow extends JFrame {

    private static final long serialVersionUID = -6740703588976621222L;
    Workbook workbook1 = null;
    Sheet sheet1;
    String fileType1;
    String fileName1;
    String filePath1;
    JLabel jLabel = new JLabel();
    JLabel jLabel1 = new JLabel();
    JLabel jLabel2 = new JLabel();
    String defaultDirectory = "D:\\";

    public excelDeleteRow() {
        super("Excel删除行工具");
        Container c = this.getContentPane();

        c.add(getPanel());

        this.setSize(670, 200);
        this.setUndecorated(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel getPanel() {
        JPanel jP = new JPanel();
        // 设置空布局，即绝对布局
        jP.setOpaque(false);
        jP.setLayout(null);
//      line 0
        JLabel jLabel01 = new JLabel("文件名",JLabel.CENTER);
        jLabel01.setBounds(10, 10, 180, 25);
        jP.add(jLabel01);

        JLabel jLabel02 = new JLabel("上传",JLabel.CENTER);
        jLabel02.setBounds(200, 10, 80, 25);
        jP.add(jLabel02);

        JLabel jLabel03 = new JLabel("表名",JLabel.CENTER);
        jLabel03.setBounds(290, 10, 100, 25);
        jP.add(jLabel03);

        JLabel jLabel04 = new JLabel("删除行开头",JLabel.CENTER);
        jLabel04.setBounds(400, 10, 210, 25);
        jP.add(jLabel04);


//      line 1
        final JTextField jTextField1 = new JTextField();
        jTextField1.setBounds(10, 45, 180, 25);
        jP.add(jTextField1);


        final DefaultComboBoxModel model11 = new DefaultComboBoxModel();
        JComboBox jComboBox11 = new JComboBox(model11);
        jComboBox11.setBounds(290, 45, 100, 25);
        jComboBox11.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // 选择的下拉框选项
                    clearLabel();
                    System.out.println(e.getItem());
                    sheet1 = workbook1.getSheet(e.getItem().toString());
                    int columnsNum = sheet1.getRow(0).getPhysicalNumberOfCells();
                }
            }
        });
        jP.add(jComboBox11);

        JButton uploadButton1 = new JButton("上传");
        uploadButton1.setBounds(200, 45, 80, 25);
        uploadButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearLabel();
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
                if(new File(defaultDirectory).exists()) {
                jfc.setCurrentDirectory(new File(defaultDirectory));
                }
                jfc.showDialog(new JLabel(), "选择");
                File file=jfc.getSelectedFile();
                if(file.isFile() && (file.getName().endsWith(".xls")|| file.getName().endsWith(".xlsx"))){
                    defaultDirectory = file.getAbsolutePath();
                    jTextField1.setText(file.getName());
                    FileInputStream execlstream =null;
                    try {
                        execlstream = new FileInputStream(file.getAbsolutePath());
                        if(file.getName().endsWith(".xls")){
                            workbook1 = new HSSFWorkbook(execlstream);
                            fileName1 = file.getName().replace(".xls","");
                            fileType1 = ".xls";
                        }else{
                            workbook1 = new XSSFWorkbook(execlstream);
                            fileName1 = file.getName().replace(".xlsx","");
                            fileType1 = ".xlsx";
                        }
                        filePath1 = file.getAbsolutePath().replace(fileName1+fileType1,"");
                        model11.removeAllElements();
                        for (int i=0;i<workbook1.getNumberOfSheets();i++){
                            model11.addElement(workbook1.getSheetName(i));
                        }
                        //jComboBox11.setPopupVisible(model1.getSize() > 0);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                //Sheet sheet = wb.getSheetAt(0);
                //Sheet sheet = wb.getSheet("Sheet1");
                //Row row = sheet.getRow(0);
                //Cell cell = row.getCell(1);
                }else{
                    jTextField1.setText("请选择正确的文件类型");
                }
                System.out.println(file.getAbsolutePath());
            }
        });
        jP.add(uploadButton1);

        final JTextField jTextField11 = new JTextField();
        jTextField11.setBounds(400, 45, 210, 25);
        jP.add(jTextField11);


//      line 2

        JButton jButton3 = new JButton("删行");
        jButton3.setBounds(10, 80, 80, 25);
        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    clearLabel();
                    //colummn number need to compare while looping...
                    int columnNumber = 0;
                    int rowNumber = sheet1.getPhysicalNumberOfRows();
                    for (int i=0; i<rowNumber; i++){
                        System.out.println("sheet1 row number: " + i);
                        String cellValue = sheet1.getRow(i)!= null ? sheet1.getRow(i).getCell(columnNumber,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null ? sheet1.getRow(i).getCell(columnNumber,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).toString().trim() : null: null;
                        String deleteValue = jTextField11.getText() !=  null ? !jTextField11.getText().trim().isEmpty() ? jTextField11.getText().trim() : null : null;
                        if(cellValue == null || cellValue.isEmpty() || (deleteValue != null && cellValue.startsWith(deleteValue))){
                            i = removeRow(sheet1, i);
                        }
                    }
                    //save files...
                    FileOutputStream outFile = new FileOutputStream(new File(filePath1+ fileName1 + " 删" + fileType1));
                    workbook1.write(outFile);
                    outFile.close();
                    jLabel.setText("文件删行完成! ");
                    jLabel1.setText( filePath1+ fileName1 + " 删" + fileType1 );
                } catch(Exception exception){
                    exception.printStackTrace();
                }

            }
        });
        jP.add(jButton3);

        jLabel.setBounds(10, 115, 600, 10);
        jLabel1.setBounds(10, 135, 600, 10);
        jLabel2.setBounds(10, 155, 600, 10);
        jP.add(jLabel);
        jP.add(jLabel1);
        jP.add(jLabel2);

        return jP;
    }

    public static int removeRow(Sheet sheet, int rowIndex) {
        try{
        int lastRowNum=sheet.getLastRowNum();
        if(rowIndex>=0&&rowIndex<lastRowNum){
            sheet.shiftRows(rowIndex+1,lastRowNum, -1);
            rowIndex--;
        }
        if(rowIndex==lastRowNum){
            Row removingRow=sheet.getRow(rowIndex);
            if(removingRow!=null){
                sheet.removeRow(removingRow);
                rowIndex--;
            }
        }}catch (Exception exc){
            exc.printStackTrace();
        }
        return rowIndex;
    }
    public void clearLabel(){
        jLabel.setText("");
        jLabel1.setText("");
        jLabel2.setText("");
    }

    public static void main(String[] args) {
        new excelDeleteRow();
    }
}
