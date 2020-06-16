/*author：李伟 time：18/10/5
*学号：1711350  计算机学院计科一班
*作业二：按日历格式输出当前月份的日历
*/
import java.util.Calendar;

public class CalendarNow{
	public static void main(String[]args){
		Calendar cal=Calendar.getInstance();
		//获取年、月、日信息
		 int year=cal.get(Calendar.YEAR);
		 int  month=cal.get(Calendar.MONTH);
		 int day=cal.get(Calendar.DAY_OF_MONTH);
		 int week=cal.get(Calendar.DAY_OF_WEEK)-1;//函数返回值为1-7，其中周日为1，周五为6，所以在这里有-1
		 int week_0=(week-day%7+1)%7;
		 int week0=week_0>0?week_0:7+week_0; //有可能出现week_0为负数的情况，而在计算机运算中-3%7=-3，程序会报错
		 int []Month={31,28,31,30,31,30,31,31,30,31,30,31};
		 if(year%4==0&&year%100!=0||year%400==0)
			 Month[1]=29;
		 System.out.println(year+"年"+(month+1)+"月"+day+"日");
		 //设置各月份的天数
		 System.out.println("Sun\t"+"Mon\t"+"Tue\t"+"Wed\t"+"Thu\t"+"Fri\t"+"Sat\t");
		 
		 int count=0;
		while(week0!=0)
		{
			System.out.print("\t");
			week0--;count++;
		}
		for(int i=0;i<Month[month];i++)
		{
			System.out.print((i+1)+"\t");
			count++;
			if(count%7==0)
				System.out.println();
		}
	}
}