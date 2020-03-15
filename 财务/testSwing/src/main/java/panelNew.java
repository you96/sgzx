import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
/**
 * @author goldt
 */
public class panelNew extends JFrame {

    private static final long serialVersionUID = -6740703588976621222L;
    Workbook workbook1 = null;
    Sheet sheet1;
    String fileType1;
    String fileName1;
    String filePath1;
    Workbook workbook2 = null;
    Sheet sheet2;
    String fileType2;
    String fileName2;
    String filePath2;
    JLabel jLabel = new JLabel();
    JLabel jLabel1 = new JLabel();
    JLabel jLabel2 = new JLabel();
    String defaultDirectory = "D:\\";

    public panelNew() {
        super("Excel比较工具");
        Container c = this.getContentPane();

        c.add(getPanel());

        this.setSize(670, 250);
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

        JLabel jLabel04 = new JLabel("关联列名",JLabel.CENTER);
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
        final JTextField jTextField2 = new JTextField();
        jTextField2.setBounds(10, 80, 180, 25);
        jP.add(jTextField2);


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

                }
            }
        });
        jP.add(jComboBox21);

        JButton uploadButton2 = new JButton("上传");
        uploadButton2.setBounds(200, 80, 80, 25);
        uploadButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearLabel();
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );//设置默认目录
                if(new File(defaultDirectory).exists()) {
                    jfc.setCurrentDirectory(new File(defaultDirectory));
                }
                jfc.showDialog(new JLabel(), "选择");
                File file=jfc.getSelectedFile();
                if(file.isFile() && (file.getName().endsWith(".xls")|| file.getName().endsWith(".xlsx"))){
                    defaultDirectory = file.getAbsolutePath();
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
        jP.add(uploadButton2);

        final JTextField jTextField21 = new JTextField();
        jTextField21.setBounds(400, 80, 210, 25);
        jP.add(jTextField21);

//      line 3

        JButton jButton3 = new JButton("比较");
        jButton3.setBounds(10, 115, 80, 25);
        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    clearLabel();

                    if(!jTextField11.getText().isEmpty() && !jTextField21.getText().isEmpty()){
                        int firstTableColumn = checkColumnExistInTable(sheet1, jTextField11);
                        int secondTableColumn = checkColumnExistInTable(sheet2, jTextField21);
                        CellStyle cellStyle = workbook1.createCellStyle();
                        Font redFont = workbook1.createFont();
                        redFont.setColor(Font.COLOR_RED);
                        cellStyle.setFont(redFont);
                        CellStyle cellStyle2 = workbook2.createCellStyle();
                        Font redFont2 = workbook2.createFont();
                        redFont2.setColor(Font.COLOR_RED);
                        cellStyle2.setFont(redFont2);
                        if(firstTableColumn > -1){
                            if(secondTableColumn > -1){
                                for (int i=0; i<sheet1.getPhysicalNumberOfRows(); i++){
                                    System.out.println("sheet1 row number: " + i);
                                    for (int j=0; j < sheet2.getPhysicalNumberOfRows(); j++){
                                        System.out.println("sheet2 row number: " + j);
                                        String cellValue1 = sheet1.getRow(i)!= null ? sheet1.getRow(i).getCell(firstTableColumn,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null ? sheet1.getRow(i).getCell(firstTableColumn,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).toString().trim() : null: null;
                                        String cellValue2 = sheet2.getRow(j)!=null ? sheet2.getRow(j).getCell(secondTableColumn,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)!=null?sheet2.getRow(j).getCell(secondTableColumn,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).toString().trim() : null : null;
                                        //TODO: need to check if it's contain or end with...

                                        cellValue1 = replaceBlank(cellValue1);
                                        cellValue2 = replaceBlank(cellValue2);

                                        if(!(cellValue1 == null || cellValue1.isEmpty()) && !(cellValue2 == null || cellValue2.isEmpty())  && (cellValue1.equals(cellValue2) || cellValue1.contains(cellValue2) || cellValue2.contains(cellValue1))){

//                                            sheet1.getRow(i).setRowStyle(cellStyle);
                                            for (int k=0; k < sheet1.getRow(i).getPhysicalNumberOfCells(); k++){
                                                System.out.println("sheet1 column number: " + k);
                                                if(sheet1.getRow(i).getCell(k,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)!= null) {
                                                    sheet1.getRow(i).getCell(k,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).setCellStyle(cellStyle) ;
                                                }
                                            }


                                            //sheet2.getRow(j).setRowStyle(cellStyle2);
                                            for (int l=0; l < sheet2.getRow(j).getPhysicalNumberOfCells(); l++){
                                                System.out.println("sheet2 column number: " + l);
                                                if(sheet2.getRow(j).getCell(l,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL) != null){
                                                    sheet2.getRow(j).getCell(l,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).setCellStyle(cellStyle2);
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
                                jLabel1.setText( filePath1+ fileName1 + " 改" + fileType1 );
                                jLabel2.setText( filePath2+ fileName2 + " 改" + fileType2 );
                            }else{
                                jLabel.setText("第二个关联列名找不到");
                            }
                        }else{
                            jLabel.setText("第一个关联列名找不到");
                        }
                    }else{
                        jLabel.setText("关联列名不能为空");
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException excep) {
                    excep.printStackTrace();
                } catch(Exception exception){
                    exception.printStackTrace();
                }

            }
        });
        jP.add(jButton3);

        jLabel.setBounds(10, 150, 600, 10);
        jLabel1.setBounds(10, 170, 600, 10);
        jLabel2.setBounds(10, 190, 600, 10);
        jP.add(jLabel);
        jP.add(jLabel1);
        jP.add(jLabel2);

        return jP;
    }

    private int checkColumnExistInTable(Sheet sheet, JTextField jTextField) {
        int columnNum = -1;
        for (Row row : sheet) {
            int index = 0;
            for (Cell cell : row) {
                //读取数据前设置单元格类型
                cell.setCellType(CellType.STRING);
                //String value = cell.getStringCellValue().trim().replaceAll("\\n","");
                String value = replaceBlank(cell.getStringCellValue().trim());
                if(value.equals(jTextField.getText().trim())){
                    columnNum = index;
                    break;
                }
                index++;
            }
            if(columnNum != -1){
                break;
            }
        }
        return columnNum;
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
        new panelNew();
    }
}
