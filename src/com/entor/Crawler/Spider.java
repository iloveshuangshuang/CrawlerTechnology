package com.entor.Crawler;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
/**
 * java实现爬虫
 */
public class Spider {
	//爬取链接 http://www.8btc.com/what-is-blockchain
	// String regex = "(http|https)://[\\w+\\.?/?]+\\.[A-Za-z]+";
	public static void spiderURL(String url,String regex,String filename){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	
		String time=sdf.format(new Date());
		URL realURL=null;
		URLConnection connection=null;
		BufferedReader br=null;
		PrintWriter pw=null;
		PrintWriter pw1=null;
		
		Pattern pattern=Pattern.compile(regex);
		try{
			realURL=new URL(url);
			connection=realURL.openConnection();
			//connection.connect();
 
			File fileDir = new File("D:/JSD1810/zd/Spider/"+time);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			//将爬取到的内容放到E盘相应目录下   
			pw = new PrintWriter(new FileWriter("D:/JSD1810/zd/Spider/"+time+"/"+filename+"Spider.txt"),true);
			pw1 = new PrintWriter(new FileWriter("D:/JSD1810/zd/Spider/"+time+"/"+filename+"SpiderURL.txt"),true);
			
			br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line=null;
			
			
			while((line=br.readLine())!=null){
				pw.println(line);
				Matcher matcher=pattern.matcher(line);
				while(matcher.find()){
					pw1.println(matcher.group());
				}
				
			}
			System.out.println("爬取成功！");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				br.close();
				pw.close();
				pw1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
    public static void main(String[] args) {
    	//String url="https://news.163.com/";
    	//String url="http://www.8btc.com/what-is-blockchain";

    	String url="https://wenku.baidu.com/view/a8a031c1bb4cf7ec4afed09c.html?re=view";
    	String regex= "(http|https)://[\\w+\\.?/?]+\\.[A-Za-z]+";
    	
    	//spiderURL(url,regex,"8btc");    	
    	spiderURL(url,regex,"wenku");
    						//加在创建的文本文档文件名前
    }
}