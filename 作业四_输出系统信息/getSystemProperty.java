//：getSystemProperty.java

import java.util.Properties;
/**
第四次Java作业：获取系统信息，如命令行参数无则输出所有系统参数，否则按命令行输入参数输出相应的信息
*@author 1711350  李伟
*@version 2018/11/08
*/
public class getSystemProperty{
	/**主函数实现系统信息输出，输入命令行参数则输出参数要求的信息，否则全部输出系统信息
	*@param  args 命令行参数获取
	*@return NO returns
	*/
	 public static void main(String[] args)
	 {
		 System.out.println("以下输出系统参数信息：");
		if(args.length>0)
			for(int i=0;i<args.length;i++)
				System.out.println(System.getProperty(args[i]));
		else
		{	
			Properties properties=System.getProperties();
			for(String key:properties.stringPropertyNames())
				System.out.println(key+"="+properties.getProperty(key));
		}
	 }
}