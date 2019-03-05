package com.entor.web;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import okio.ForwardingTimeout;

public class Spider {
	
	public static String path = "http://www.yada.com.cn/";	//�Ŵ﹫˾����
	public static int num = -1,sum = 0;
	/**
	 * �����ĸ��ļ��ࣨ���Ӵ洢��ͼƬ���棬�ļ��洢���������Ӵ洢��
	 */
	public static File aLinkFile,imgLinkFile,docLinkFile,errorLinkFile;
	/**
	 * 
	 * @param path		Ŀ���ַ
	 */
	public static void getAllLinks(String path){
		Document doc = null;
		try{
			doc = Jsoup.parse(HttpUtil.get(path));
		}catch (Exception e){
			//���յ��������ӣ�404ҳ�棩
			writeTxtFile(errorLinkFile, path+"\r\n");	//д����������ռ��ļ�
			num++;	
			if(sum>num){	//����ļ�������sum������num(��ǰ����)���������
				getAllLinks(getFileLine(aLinkFile, num));
			}
			return;
		}
		Elements aLinks = doc.select("a[href]");
		Elements imgLinks = doc.select("img[src]");
		System.out.println("��ʼ���ӣ�"+path);
		for(Element element:aLinks){
			String url =element.attr("href");
			//�ж������Ƿ����������ͷ
			if(!url.contains("http://")&&!url.contains("https://")){
				//���������	����<a href="xitongshow.php?cid=67&id=113" />
				//����Ҫ����ǰ׺	http://www.yada.com.cn/xitongshow.php?cid=67&id=113
				//����404
				url = Spider.path+url;
			}
			//����ļ���û��������ӣ����������в�����javascript:�����(��Ϊ�е�����js�﷨��ת)
			if(!readTxtFile(aLinkFile).contains(url)
					&&!url.contains("javascript")){	
				//·�����������ҳ������--->��ֹ��������վ
				if(url.contains(Spider.path)){		
					//�жϸ�a��ǩ���������ļ�����������
					if(url.contains(".doc")||url.contains(".exl")
							||url.contains(".exe")||url.contains(".apk")
							||url.contains(".mp3")||url.contains(".mp4")){
						//д���ļ��У��ļ���+�ļ�����
						writeTxtFile(docLinkFile, element.text()+"\r\n\t"+url+"\r\n");
					}else{
						//������д���ļ�
						writeTxtFile(aLinkFile, url+"\r\n");
						sum++;	//��������+1
					}
					System.out.println("\t"+element.text()+"��\t"+url);
				}
			}
		}
		//ͬʱץȡ��ҳ��ͼƬ����
		for(Element element:imgLinks){
			String srcStr = element.attr("src");
			if(!srcStr.contains("http://")&&!srcStr.contains("https://")){//û��������ͷ
				srcStr = Spider.path+srcStr;
			}
			if(!readTxtFile(imgLinkFile).contains(srcStr)){	
				//��ͼƬ����д���ļ���
				writeTxtFile(imgLinkFile, srcStr+"\r\n");
			}
		}
		num++;
		if(sum>num){
			getAllLinks(getFileLine(aLinkFile, num));
		}
	}
	
	/**
	 * ��ȡ�ļ�����
	 * @param file	�ļ���
	 * @return	�ļ�����
	 */
	public static String readTxtFile(File file){
		String result = "";		//��ȡ�Y��
		String thisLine = "";	//ÿ�ζ�ȡ����
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			try {
				while((thisLine=reader.readLine())!=null){
					result += thisLine+"\n";
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * д������
	 * @param file	�ļ���
	 * @param urlStr	Ҫд����ı�
	 */
	public static void writeTxtFile(File file,String urlStr){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
			writer.write(urlStr);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ�ļ�ָ�����������ݣ����������ȡ��ǰҪ��������
	 * @param file	Ŀ���ļ�
	 * @param num	ָ��������
	 */
	public static String getFileLine(File file,int num){
		String thisLine = "";
		int thisNum = 0 ;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((thisLine = reader.readLine())!=null){
				if(num == thisNum){
					return thisLine;
				}
					thisNum++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * ��ȡ�ļ����������ж������ӣ�
	 * @param file	�ļ���
	 * @return	������
	 */
	public static int getFileCount(File file){
		int count = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while(reader.readLine()!=null){	//�����ļ���
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	
	public static void main(String[] args) {
		aLinkFile = new File("D:/JSD1810/zd/Test/ALinks.txt");
		imgLinkFile = new File("D:/JSD1810/zd/Test/ImgLinks.txt");	
		docLinkFile = new File("D:/JSD1810/zd/Test/DocLinks.txt");
		errorLinkFile = new File("D:/JSD1810/zd/Test/ErrorLinks.txt");
		//������洢�ĸ��ļ����󣬷��������ͬ����
		File[] files = new File[]{aLinkFile,imgLinkFile,docLinkFile,errorLinkFile};
		try {
			for(File file: files){
				if(file.exists())	//����ļ�����
					file.delete();	//����ɾ��
				file.createNewFile();	//�ٴ���
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		long startTime = System.currentTimeMillis();    //��ȡ��ʼʱ��
		Spider.getAllLinks(path);	//��ʼ��ȡĿ������
		System.out.println(""
				+ "��������������������������������������ȡ����������������������������������������"
				+ "\nĿ����ַ��"+path
				+ "\n����������"+sum+"��"
				+ "\nͼƬ������"+getFileCount(imgLinkFile)+"��"
				+ "\n�ļ�������"+getFileCount(docLinkFile)+"��");
		writeTxtFile(aLinkFile, "����������"+getFileCount(aLinkFile)+"��");
		writeTxtFile(imgLinkFile, "ͼƬ������"+getFileCount(imgLinkFile)+"��");
		writeTxtFile(docLinkFile, "�ļ�������"+getFileCount(docLinkFile)+"��");
		writeTxtFile(errorLinkFile, "��������������"+getFileCount(errorLinkFile)+"��");
		long endTime = System.currentTimeMillis();    //��ȡ����ʱ��
		System.out.println("\n��������ʱ�䣺" + (endTime - startTime) + "ms");    //�����������ʱ��
	}
}