package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JavaExecuShell {

    public  static  void excutorShell(String taskID){


        String cmd = "sh /root/test/test.sh  "+ taskID;
        System.out.println("cmd = "+ cmd);
        //........

    }
	public static void main(String[] args) {
		try {
			//准备脚本

			String cmd = "sh /root/test/test.sh "+args[0]+" "+args[1];
			System.out.println("cmd = "+ cmd);
			Process proc = Runtime.getRuntime().exec(cmd);
			/**
			 * 可执行程序的输出可能会比较多，而运行窗口的输出缓冲区有限，会造成waitFor一直阻塞。
			 * 解决的办法是，利用Java提供的Process类提供的getInputStream,getErrorStream方法
			 * 让Java虚拟机截获被调用程序的标准输出、错误输出，在waitfor()命令之前读掉输出缓冲区中的内容。
			 */
			String flag ;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while ( (flag=bufferedReader.readLine()) != null){
				System.out.println("result ---- "+flag);
			}
			bufferedReader.close();
			/**
			 * 等待脚本执行完成
			 */
			proc.waitFor();
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
