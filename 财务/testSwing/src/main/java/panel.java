import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class panel extends JFrame {

    private static final long serialVersionUID = -6740703588976621222L;
    Workbook workbook1 = null;
    Sheet sheet1;
    String fileType1;
    String fileName1;
    String filePath1;
    String fileType2;
    String fileName2;
    String filePath2;
    Workbook workbook2 = null;
    Sheet sheet2;
    int column1 = -1;
    int column2 = -1;
    int column3 = -1;
    int column4 = -1;
    JLabel jLabel = new JLabel();
    JFileChooser jfc=new JFileChooser();
    JProgressBar progressBar = new JProgressBar();
    private static int currentProgress = 0;

    public panel() {
        super("Excel比较工具");
        Container c = this.getContentPane();

        c.add(getPanel());

        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
        this.setSize(740, 200);
        this.setUndecorated(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel getPanel() {
        JPanel jP = new JPanel();
        jP.setOpaque(false);
        jP.setLayout(null);// 设置空布局，即绝对布局
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

        JLabel jLabel04 = new JLabel("相同列",JLabel.CENTER);
        jLabel04.setBounds(400, 10, 150, 25);
        jP.add(jLabel04);

        JLabel jLabel05 = new JLabel("比较列",JLabel.CENTER);
        jLabel05.setBounds(560, 10, 150, 25);
        jP.add(jLabel05);

//      line 1
        final JTextField jTextField1 = new JTextField();
        jTextField1.setBounds(10, 45, 180, 25);
        jP.add(jTextField1);

        final DefaultComboBoxModel model12 = new DefaultComboBoxModel();
        final DefaultComboBoxModel model13 = new DefaultComboBoxModel();
        JComboBox jComboBox12 = new JComboBox(model12);
        jComboBox12.setBounds(400, 45, 150, 25);
        jComboBox12.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // 选择的下拉框选项
                    clearLabel();
                    System.out.println("cloumn1: " + e.getItem());
                    for (int i=0;i<sheet1.getRow(0).getPhysicalNumberOfCells(); i++){
                        if(sheet1.getRow(0).getCell(i).toString() == e.getItem().toString()){
                            column1 = i;
                            break;
                        }
                    }
                }
            }
        });
        jP.add(jComboBox12);

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
                    model12.removeAllElements();
                    model13.removeAllElements();
                    for (int i=0;i<columnsNum;i++){
                        model12.addElement(sheet1.getRow(0).getCell(i));
                        model13.addElement(sheet1.getRow(0).getCell(i));
                    }
                }
            }
        });
        jP.add(jComboBox11);

        JButton jButton1 = new JButton("上传");
        jButton1.setBounds(200, 45, 80, 25);// 设置位置及大小
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearLabel();
                jfc.showDialog(new JLabel(), "选择");
                File file=jfc.getSelectedFile();
                if(file.isFile() && (file.getName().endsWith(".xls")|| file.getName().endsWith(".xlsx"))){
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
        jP.add(jButton1);

        JComboBox jComboBox13 = new JComboBox(model13);
        jComboBox13.setBounds(560, 45, 150, 25);
        jComboBox13.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // 选择的下拉框选项
                    clearLabel();
                    System.out.println("cloumn2: " + e.getItem());
                    for (int i=0;i<sheet1.getRow(0).getPhysicalNumberOfCells(); i++){
                        if(sheet1.getRow(0).getCell(i).toString() == e.getItem().toString()){
                            column2 = i;
                            break;
                        }
                    }
                }
            }
        });
        jP.add(jComboBox13);
//      line 2
        final JTextField jTextField2 = new JTextField();
        jTextField2.setBounds(10, 80, 180, 25);
        jP.add(jTextField2);

        final DefaultComboBoxModel model22 = new DefaultComboBoxModel();
        final DefaultComboBoxModel model23 = new DefaultComboBoxModel();
        JComboBox jComboBox22 = new JComboBox(model22);
        jComboBox22.setBounds(400, 80, 150, 25);
        jComboBox22.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // 选择的下拉框选项
                    clearLabel();
                    System.out.println("cloumn3: " + e.getItem());
                    for (int i=0;i<sheet2.getRow(0).getPhysicalNumberOfCells(); i++){
                        if(sheet2.getRow(0).getCell(i).toString() == e.getItem().toString()){
                            column3 = i;
                            break;
                        }
                    }
                }
            }
        });
        jP.add(jComboBox22);

        final DefaultComboBoxModel model21 = new DefaultComboBoxModel();
        JComboBox jComboBox21 = new JComboBox(model21);
        jComboBox21.setBounds(290, 80, 100, 25);
        jComboBox21.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // 选择的下拉框选项
                    clearLabel();
                    System.out.println(e.getItem());
                    sheet2 = workbook2.getSheet(e.getItem().toString());
                    int columnsNum = sheet2.getRow(0).getPhysicalNumberOfCells();
                    model22.removeAllElements();
                    model23.removeAllElements();
                    for (int i=0;i<columnsNum;i++){
                        model22.addElement(sheet2.getRow(0).getCell(i));
                        model23.addElement(sheet2.getRow(0).getCell(i));
                    }
                }
            }
        });
        jP.add(jComboBox21);

        JButton jButton2 = new JButton("上传");
        jButton2.setBounds(200, 80, 80, 25);
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearLabel();
                jfc.showDialog(new JLabel(), "选择");
                File file=jfc.getSelectedFile();
                if(file.isFile() && (file.getName().endsWith(".xls")|| file.getName().endsWith(".xlsx"))){
                    jTextField2.setText(file.getName());
                    FileInputStream execlstream =null;
                    try {
                        execlstream = new FileInputStream(file.getAbsolutePath());
                        if(file.getName().endsWith(".xls")){
                            workbook2 = new HSSFWorkbook(execlstream);
                            fileName2 = file.getName().replace(".xls","");
                            fileType2 = ".xls";
                        }else{
                            workbook2 = new XSSFWorkbook(execlstream);
                            fileName2 = file.getName().replace(".xlsx","");
                            fileType2 = ".xlsx";
                        }
                        filePath2 = file.getAbsolutePath().replace(fileName2+fileType2,"");
                        model21.removeAllElements();
                        for (int i=0;i<workbook2.getNumberOfSheets();i++){
                            model21.addElement(workbook2.getSheetName(i));
                        }
                        //jComboBox11.setPopupVisible(model1.getSize() > 0);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    jTextField2.setText("请选择正确的文件类型");
                }
                System.out.println(file.getAbsolutePath());
            }
        });
        jP.add(jButton2);

        JComboBox jComboBox23 = new JComboBox(model23);
        jComboBox23.setBounds(560, 80, 150, 25);
        jComboBox23.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // 选择的下拉框选项
                    clearLabel();
                    System.out.println("cloumn4: " + e.getItem());
                    for (int i=0;i<sheet2.getRow(0).getPhysicalNumberOfCells(); i++){
                        if(sheet2.getRow(0).getCell(i).toString() == e.getItem().toString()){
                            column4 = i;
                            break;
                        }
                    }
                }
            }
        });
        jP.add(jComboBox23);


        progressBar.setBounds(100,115,580,25); // 添加进度改变通知
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        // 设置当前进度值
        progressBar.setValue(currentProgress);
        // 绘制百分比文本（进度条中间显示的百分数）
        progressBar.setStringPainted(true);
        progressBar.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("当前进度值: " + progressBar.getValue() + "; " +
                        "进度百分比: " + progressBar.getPercentComplete());
            }
        });
        jP.add(progressBar);

        JButton jButton3 = new JButton("比较");
        jButton3.setBounds(10, 115, 80, 25);
        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    ArrayList<String> sameColumn1 = new ArrayList<String>();
                    ArrayList<String> sameColumn2 = new ArrayList();
                    clearLabel();
                    CellStyle cellStyle = workbook1.createCellStyle();
                    org.apache.poi.ss.usermodel.Font redFont = workbook1.createFont();
                    redFont.setColor(org.apache.poi.ss.usermodel.Font.COLOR_RED);
                    cellStyle.setFont(redFont);
                    CellStyle cellStyle2 = workbook2.createCellStyle();
                    org.apache.poi.ss.usermodel.Font redFont2 = workbook2.createFont();
                    redFont2.setColor(Font.COLOR_RED);
                    cellStyle2.setFont(redFont2);
                    if(column1 != -1 && column2 != -1 && column3 != -1 && column4 != -1){
                        int newColumnNum = sheet1.getRow(0).getPhysicalNumberOfCells() + 1;
                        sheet1.getRow(0).getCell(newColumnNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(sheet2.getRow(0).getCell(column4).toString());
                        //get distinct same column list
                        for (int i=0; i<sheet1.getPhysicalNumberOfRows(); i++){
//                            System.out.println("sheet1 row number: " + i);
                            // 设置当前进度值
                            Dimension d = progressBar.getSize();
                            Rectangle rect = new Rectangle(0, 0, d.width, d.height);
                            progressBar.setValue(i * 100 / sheet1.getPhysicalNumberOfRows());
                            progressBar.paintImmediately(rect);

                            for (int j=0; j < sheet2.getPhysicalNumberOfRows(); j++){
//                                System.out.println("sheet2 row number: " + j);
                                String cellValue1 = sheet1.getRow(i)!= null ? sheet1.getRow(i).getCell(column1,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null ? sheet1.getRow(i).getCell(column1,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).toString().trim() : null: null;
                                String cellValue2 = sheet2.getRow(j)!=null ? sheet2.getRow(j).getCell(column3,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)!=null?sheet2.getRow(j).getCell(column3,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).toString().trim() : null : null;
                                //TODO: need to check if it's contain or end with...

                                cellValue1 = replaceBlank(cellValue1);
                                cellValue2 = replaceBlank(cellValue2);

                                if(!(cellValue1 == null || cellValue1.isEmpty()) && !(cellValue2 == null || cellValue2.isEmpty())  && (cellValue1.equals(cellValue2) || cellValue1.contains(cellValue2) || cellValue2.contains(cellValue1))){

                                    //add same column to list if it doesn't exists, and add a new column at last...
                                    if(!sameColumn1.contains(cellValue1)){
                                        sameColumn1.add(cellValue1);
                                        double count = getCountNumFromSheet(sheet2, column3, column4, cellValue2);
                                        sheet1.getRow(i).getCell(newColumnNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellValue(count);
                                    }
                                    //add red color fro sheet1
                                    for (int k=0; k < sheet1.getRow(i).getPhysicalNumberOfCells(); k++){
//                                        System.out.println("sheet1 column number: " + k);
                                        if(sheet1.getRow(i).getCell(k,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)!= null) {
                                            sheet1.getRow(i).getCell(k,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).setCellStyle(cellStyle) ;
                                        }
                                    }
                                    //add red color for sheet2
                                    for (int l=0; l < sheet2.getRow(j).getPhysicalNumberOfCells(); l++){
//                                        System.out.println("sheet2 column number: " + l);
                                        if(sheet2.getRow(j).getCell(l,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null){
                                            sheet2.getRow(j).getCell(l,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).setCellStyle(cellStyle2);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //save files...
                    FileOutputStream outFile = new FileOutputStream(new File(filePath1+ fileName1 + " 改" + fileType1));
                    workbook1.write(outFile);
                    outFile = new FileOutputStream(new File(filePath2+ fileName2 + " 改" + fileType2));
                    workbook2.write(outFile);
                    outFile.close();
                    jLabel.setText("文件比对完成! ");
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException excep) {
                    excep.printStackTrace();
                }
            }
        });
        jP.add(jButton3);

        jLabel.setBounds(10, 150, 600, 25);
        jP.add(jLabel);


        return jP;
    }

    private double getCountNumFromSheet(Sheet sheet, int sameColumn, int countColumn, String sameColumnValue) {
        double result = 0;
        for (int i=0; i<sheet.getLastRowNum(); i++){
            String cellValue = sheet.getRow(i)!=null ? sheet.getRow(i).getCell(sameColumn,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)!=null?sheet.getRow(i).getCell(sameColumn,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).toString().trim() : null : null;
            if(!(cellValue == null || cellValue.isEmpty()) && cellValue.equals(sameColumnValue)){
                double countColumnValue = sheet.getRow(i)!=null ? sheet.getRow(i).getCell(countColumn,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)!=null?Double.valueOf(sheet.getRow(i).getCell(countColumn,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).toString().trim()) : 0 : 0;
                result += countColumnValue;
            }
        }
        return  result;
    }

    public String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public void clearLabel(){
        jLabel.setText("");
    }

    public static void main(String[] args) {
        new panel();
    }
}
