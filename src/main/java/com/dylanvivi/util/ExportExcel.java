package com.dylanvivi.util;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.lemon.lang.Strings;

/**
 * 
 * 
 * @author dylan
 * 
 * @param <T>
 *            应用泛型，代表任意一个符合javabean风格的类
 *            注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 *            byte[]表jpg格式的图片数据
 */
public class ExportExcel<T> {
	
	

	public void exportExcel(Collection<T> dataset, OutputStream out) {
		exportExcel("导出EXCEL文档", null, dataset, out, "yyyy-MM-dd");
	}

	public void exportExcel(String[] headers, Collection<T> dataset,
			OutputStream out) {
		exportExcel("导出EXCEL文档", headers, dataset, out, "yyyy-MM-dd");
	}
	
	public void exportExcel(String[] headers, Collection<T> dataset,String title,
			OutputStream out,String[] cols) {
		exportExcel(title, headers, dataset, out, "yyyy-MM-dd",cols);
	}

	public void exportExcel(String[] headers, Collection<T> dataset,
			OutputStream out, String pattern) {
		exportExcel("导出EXCEL文档", headers, dataset, out, pattern);
	}

	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */
	@SuppressWarnings("unchecked")
	public void exportExcel(String title, String[] headers,
			Collection<T> dataset, OutputStream out, String pattern) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
				0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("admin");

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				Field field = fields[i];
				String fieldName = field.getName();
				String getMethodName = "get"
						+ fieldName.substring(0, 1).toUpperCase()
						+ fieldName.substring(1);
				try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName,
							new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					// if (value instanceof Integer) {
					// int intValue = (Integer) value;
					// cell.setCellValue(intValue);
					// } else if (value instanceof Float) {
					// float fValue = (Float) value;
					// textValue = new HSSFRichTextString(
					// String.valueOf(fValue));
					// cell.setCellValue(textValue);
					// } else if (value instanceof Double) {
					// double dValue = (Double) value;
					// textValue = new HSSFRichTextString(
					// String.valueOf(dValue));
					// cell.setCellValue(textValue);
					// } else if (value instanceof Long) {
					// long longValue = (Long) value;
					// cell.setCellValue(longValue);
					// }
					/*
					if (value instanceof Boolean) {
						boolean bValue = (Boolean) value;
						textValue = "男";
						if (!bValue) {
							textValue = "女";
						}
					}
					*/
					if(value instanceof Double || value instanceof Float){
						String fvalue = value.toString();
						textValue = Strings.trimFloat(fvalue, 2);
					}else if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else if (value instanceof byte[]) {
						// 有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为80px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (short) (35.7 * 80));
						// sheet.autoSizeColumn(i);
						byte[] bsValue = (byte[]) value;
						HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
								1023, 255, (short) 6, index, (short) 6, index);
						anchor.setAnchorType(2);
						patriarch.createPicture(anchor, workbook.addPicture(
								bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
					} else {
						// 其它数据类型都当作字符串简单处理
						if(value == null){
							textValue = null;
						}else{
							textValue = value.toString();
						}
						
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^//d+(//.//d+)?$");
						Matcher matcher = p.matcher(textValue);
						if (matcher.matches()) {
							// 是数字当作double处理
							cell.setCellValue(Double.parseDouble(textValue));
						} else {
							HSSFRichTextString richString = new HSSFRichTextString(
									textValue);
							HSSFFont font3 = workbook.createFont();
							font3.setColor(HSSFColor.BLUE.index);
							richString.applyFont(font3);
							cell.setCellValue(richString);
						}
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
			}

		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private HSSFCellStyle createStyle(HSSFWorkbook workbook,short fillForegroundColor,short fillPattern,short borderBottom,short borderleft,short borderright,short bordertop,short alignment) {
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(fillForegroundColor);
		style.setFillPattern(fillPattern);
		style.setBorderBottom(borderBottom);
		style.setBorderLeft(borderleft);
		style.setBorderRight(borderright);
		style.setBorderTop(bordertop);
		style.setAlignment(alignment);
		return style;
	}
	
	private HSSFFont createFont(HSSFWorkbook workbook,short color,short bold){
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(color);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(bold);
		return font;
	}
	
	/**
	 * 输出财务数据报表
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * 
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void exportExcel(String title, String[] headers,
			Collection<T> dataset, OutputStream out) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = createStyle(workbook, HSSFColor.GOLD.index, HSSFCellStyle.SOLID_FOREGROUND, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
		//生成一个字体
		HSSFFont font = createFont(workbook, HSSFColor.VIOLET.index, HSSFFont.BOLDWEIGHT_BOLD);
		//字体设置给样式
		
		// 生成并设置另一个样式
		HSSFCellStyle style2 = createStyle(workbook, HSSFColor.WHITE.index, HSSFCellStyle.SOLID_FOREGROUND, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		
		//生成数字类型样式(三位小数)
        HSSFCellStyle style3 =  createStyle(workbook,  HSSFColor.WHITE.index, HSSFCellStyle.SOLID_FOREGROUND, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFDataFormat format = workbook.createDataFormat();
        style3.setDataFormat(format.getFormat("0.000"));
         
       //生成数字类型样式(整数)
        HSSFCellStyle style4 =  createStyle(workbook,  HSSFColor.WHITE.index, HSSFCellStyle.SOLID_FOREGROUND, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
 		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style4.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
        
        //产生标题行 ---> 短彩广告实际执行额统计
        HSSFRow title_row = sheet.createRow(0);
        title_row.setHeightInPoints(22);
        HSSFCell title_cell = title_row.createCell(0);

        title_cell.setCellValue(new HSSFRichTextString(title));
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, headers.length -1);
        sheet.addMergedRegion(region);

        //产生标题行 -----> 收入、成本、预估、佣金、备注
        HSSFRow title_row1 = sheet.createRow(1);
        HSSFCell title_cell1 = title_row1.createCell(0);
        title_cell1.setCellValue(new HSSFRichTextString("收入"));
        
        CellRangeAddress region1 = new CellRangeAddress(1, 1, 0, 6);
        sheet.addMergedRegion(region1);
        
        CellRangeAddress region2 = new CellRangeAddress(1, 1, 7, 10);
        sheet.addMergedRegion(region2);
        
        CellRangeAddress region3 = new CellRangeAddress(1, 1, 11, 17);
        sheet.addMergedRegion(region3);
        
        CellRangeAddress region4 = new CellRangeAddress(1, 1, 18, 21);
        //起始行，结束行，起始列，结束列
        sheet.addMergedRegion(region4);
        
        //合并单元格样式
        
        HSSFCellStyle style_title = workbook.createCellStyle();
		// 设置这些样式
        style_title.setFillForegroundColor(HSSFColor.LIME.index);
        style_title.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style_title.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
        style_title.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        style_title.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
        style_title.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
        style_title.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
		// 生成一个字体
		HSSFFont font_title = workbook.createFont();
		font_title.setColor(HSSFColor.BLACK.index);
		font_title.setFontHeightInPoints((short) 16);
		font_title.setBoldweight(HSSFCellStyle.BORDER_MEDIUM);
		font_title.setFontName("宋体");
		font_title.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		
		style_title.setFont(font_title);
		
		
		//抬头样式
		
		 HSSFCellStyle styletitle = workbook.createCellStyle();
		// 设置这些样式
		 styletitle.setFillForegroundColor(HSSFColor.WHITE.index);
		 styletitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		 styletitle.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		 styletitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		 
		 styletitle.setFont(font_title);
	        
		
		
		
		//设置合并单元格格式
		title_cell.setCellStyle(styletitle);
        title_cell1.setCellStyle(style_title);
        
        for(int i=1;i<headers.length;i++){  
        	title_cell1 = title_row1.createCell(i);  
        	if(i == 7){
        		title_cell1.setCellValue(new HSSFRichTextString("成本"));
        		
        	}else if(i == 11){
        		title_cell1.setCellValue(new HSSFRichTextString("预估"));
        	}else if(i == 18){
        		title_cell1.setCellValue(new HSSFRichTextString("佣金"));
        	}else if(i == 22){
        		title_cell1.setCellValue(new HSSFRichTextString("备注"));
        	}else{
        		title_cell1.setCellValue(new HSSFRichTextString(""));
        	}
        	title_cell1.setCellStyle(style_title);  
        } 
        
		
		
		// 产生表格标题行
		HSSFRow row = sheet.createRow(2);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 2;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				Field field = fields[i];
				String fieldName = field.getName();
				String getMethodName = "get"
						+ fieldName.substring(0, 1).toUpperCase()
						+ fieldName.substring(1);
				try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName,
							new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if(value instanceof Double || value instanceof Float){
						String fvalue = value.toString();
						textValue = Strings.trimFloat(fvalue, 3);
					}else if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						textValue = sdf.format(date);
					} else {
						// 其它数据类型都当作字符串简单处理
						if(value == null){
							textValue = null;
						}else{
							textValue = value.toString();
						}
						
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^\\d+\\.\\d+?$");
						Matcher matcher = p.matcher(textValue);
						Pattern pn = Pattern.compile("^\\d+$");
						Matcher matcherPN = pn.matcher(textValue);
						if (matcher.matches()) {
							// 是数字当作double处理
							cell.setCellStyle(style3);
							cell.setCellValue(Double.parseDouble(textValue));
						}else if(matcherPN.matches()){
							cell.setCellStyle(style4);
							cell.setCellValue(Double.parseDouble(textValue));
						} else {

							cell.setCellValue(textValue);
						}
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
			}

		}

		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 * 
	 * @param cols
	 * 			  计算合计值列名(A,B,C....)，不计算填0
	 */
	@SuppressWarnings("unchecked")
	public void exportExcel(String title, String[] headers,
			Collection<T> dataset, OutputStream out, String pattern,String[] cols) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);
		// 生成一个样式
		HSSFCellStyle style = createStyle(workbook, HSSFColor.SKY_BLUE.index, HSSFCellStyle.SOLID_FOREGROUND, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
		//生成一个字体
		HSSFFont font = createFont(workbook, HSSFColor.VIOLET.index, HSSFFont.BOLDWEIGHT_BOLD);
		//字体设置给样式
		
		// 生成并设置另一个样式
		HSSFCellStyle style2 = createStyle(workbook, HSSFColor.LIGHT_YELLOW.index, HSSFCellStyle.SOLID_FOREGROUND, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		
		//生成数字类型样式(三位小数)
        HSSFCellStyle style3 =  createStyle(workbook, HSSFColor.LIGHT_YELLOW.index, HSSFCellStyle.SOLID_FOREGROUND, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
		style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFDataFormat format = workbook.createDataFormat();
        style3.setDataFormat(format.getFormat("0.000"));
         
       //生成数字类型样式(整数)
        HSSFCellStyle style4 =  createStyle(workbook, HSSFColor.LIGHT_YELLOW.index, HSSFCellStyle.SOLID_FOREGROUND, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.BORDER_THIN, HSSFCellStyle.ALIGN_CENTER);
 		style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style4.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
         
		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
				0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString("comment"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("admin");

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				Field field = fields[i];
				String fieldName = field.getName();
				String getMethodName = "get"
						+ fieldName.substring(0, 1).toUpperCase()
						+ fieldName.substring(1);
				try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName,
							new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = null;
					if(value instanceof Double || value instanceof Float){
						String fvalue = value.toString();
						textValue = Strings.trimFloat(fvalue, 2);
					}else if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						textValue = sdf.format(date);
					} else if (value instanceof byte[]) {
						// 有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为80px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (short) (35.7 * 80));
						// sheet.autoSizeColumn(i);
						byte[] bsValue = (byte[]) value;
						HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
								1023, 255, (short) 6, index, (short) 6, index);
						anchor.setAnchorType(2);
						patriarch.createPicture(anchor, workbook.addPicture(
								bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
					} else {
						// 其它数据类型都当作字符串简单处理
						if(value == null){
							textValue = null;
						}else{
							textValue = value.toString();
						}
						
					}
					// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
					if (textValue != null) {
						Pattern p = Pattern.compile("^\\d+\\.\\d+?$");
						Matcher matcher = p.matcher(textValue);
						Pattern pn = Pattern.compile("^\\d+$");
						Matcher matcherPN = pn.matcher(textValue);
						if (matcher.matches()) {
							// 是数字当作double处理
							cell.setCellStyle(style3);
							cell.setCellValue(Double.parseDouble(textValue));
						}else if(matcherPN.matches()){
							cell.setCellStyle(style4);
							cell.setCellValue(Double.parseDouble(textValue));
						} else {

							cell.setCellValue(textValue);
						}
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
			}

		}
		//增加合计行
		row = sheet.createRow(index+1);
		for(int i = 0; i < cols.length; i++){
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style2);
			HSSFFont font4 = workbook.createFont();
			font4.setColor(HSSFColor.BLACK.index);
			font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			style2.setFont(font4);
			String formula = null;
			if(i == 0){
				HSSFRichTextString richString = new HSSFRichTextString(
						"合计");
				
				richString.applyFont(font4);
				cell.setCellValue(richString);
				
			}else if("0".equals(cols[i])){ //不计算
				cell.setCellValue(new HSSFRichTextString(""));
			}else{
				formula = "SUM("+cols[i]+"2:"+cols[i]+(index+1)+")";
				cell.setCellFormula(formula);
			}
		}
		
		
		
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}