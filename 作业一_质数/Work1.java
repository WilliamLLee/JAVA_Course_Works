/*author：李伟 time： 18/10/5 
*学号：1711350  计算机学院计科一班
*作业一：显示2-100的所有质数，每五个显示在一行中
*/

public class Work1
{
	public static void main(String[] args)
	{
		int b=0;
		for(int i=2;i<=100;i++)
		{
			int a=2;
			
			for(int j=2;j<i;j++)
				if((i%j)!=0)
					a++;
			if(a==i)
			{
				b++;
				System.out.print(i+"\t");
				
				if((b%5)==0)
					System.out.println();
			}
		}
	}
}