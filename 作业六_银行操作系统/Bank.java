/**Bank.java
实现银行系统模拟，多线程实现顾客存取现金的操作要求
2018/12/13  wrote by 李伟

基本设想：在银行线程中的run()函数中设置顾客线程，同时进行时间计数，通过random进行随机处理。
*/

import java.lang.Thread;		//线程
import java.util.Random;		//随机数生成
import java.util.Formatter;		//输出格式控制

class Data			//顾客数据类
{
		String name;int rest;int check_in;int check_out;
		Data(){};
		void set(String _name,int in,int out){name=_name; check_in=in;check_out=out;rest=in-out;}
		void save(int in){ check_in+=in;rest+=in;}
		void withDraw(int out){ check_out+=out;rest-=out;}
		synchronized void print1(){System.out.println("客户账户信息： 姓名"+name+" 存入现金总数"+check_in+" 支取现金总数"+check_out+" 账户余额"+rest);}
		java.util.Formatter formatter=new  Formatter(System.out);
		synchronized void print(){formatter.format("%-10s %-10s %-10s %-10s \n",name,check_in,check_out,rest);}
}

public class Bank extends Thread{
	//保存一些用户名
	String nameArray[]={"张三","李四","王五","赵六","钱七","孙博","武九","罗时","柴史奕","宋侍尔","王嘉尔","鹿晗","李易峰","杨紫","关晓彤"
	};
	//设置银行客户信息保存数据库
    Data [] dataOfConsumer=new Data[15];
	int numOfConsumer;		//记录银行账户已有的顾客数量
	Random random=new Random((int)(System.currentTimeMillis()));	//给random变量设置种子
	Bank()
	{
		for(int i=0;i<15;i++)
			dataOfConsumer[i]=new Data() ;
		for(int i=0;i<10;i++)
		{
			int out=random.nextInt(10000);
			dataOfConsumer[i].set(nameArray[i],out+random.nextInt(10000),out); //设置初始的账户信息
		}
		numOfConsumer=10;		//先创建十人的数据
	}
	public  synchronized void  saveMoney(String name,int num)		//使用关键字synchronized，防止访问函数的冲突现象，withdraw函数相同处理
	{
		boolean exist=false;		//设置变量标志顾客是否已经在银行开户
		for(int i=0;i<numOfConsumer;i++)
		{
			Data d=dataOfConsumer[i];
			if(d.name.equals(name))
			{
				d.check_in+=num;
				d.rest+=num;
				System.out.println(new java.util.Date().toString()+"："+name+"存入现金"+num+"元");
				d.print1();
				exist=true;
				break;
			}
		}
		if(exist==false)		//如客户未开户，且银行的dataOfconsumer数组还有空间，则为之开户，否则业务不能进行
		{
				if(numOfConsumer<15)
				{
					dataOfConsumer[numOfConsumer++].set(name,num,0);
					System.out.println(new java.util.Date().toString()+"："+name+"在本银行未开户，已为之开户并存入现金"+num+"元");
					dataOfConsumer[numOfConsumer-1].print1();
				}
				else
				{
					System.out.println(new java.util.Date().toString()+"："+name+"无法在本行开户,存入现金业务未能完成");
				}
		}
		return ;
	}

	public synchronized void  withdrawMoney(String name,int num)
	{
		boolean exist=false;
		for(int i=0;i<numOfConsumer;i++)
		{
			Data d=dataOfConsumer[i];
			if(d.name.equals(name))
			{
				exist=true;
				if(d.rest>num)
				{
					d.check_out+=num;
					d.rest-=num;
					System.out.println(new java.util.Date().toString()+"："+name+"取出现金"+num+"元");
					d.print1();
					break;
				}
				else		//余额不足，进行提醒
				{
					System.out.println(new java.util.Date().toString()+"："+name+"账户余额不足，支取现金业务未能完成");	
					d.print1();
					break;
				}
			}
		}
		if(exist==false)		//客户未开户，不能进行业务
		{
			System.out.println(new java.util.Date().toString()+"："+name+"用户在本行无账户信息！");	
		}
		return;
	}
	public void run()			//实现银行营业相关程序
	{
		int time=30;//设置银行运行时间为30秒
		System.out.println(new java.util.Date().toString()+": 银行开门营业！");
		while(true)
		{
			int gap=random.nextInt(10);			//顾客随机来进行业务，因此时间间隔设为随机值
			time-=gap;
			if(time<0)		//银行在时间到了的时候准时歇业
			{
				try{
					System.out.println("...\n"+new java.util.Date().toString()+" Waiting consumers！\n ...");
					sleep((gap+time)*1000);
				}catch(InterruptedException e){
					System.out.println("Interrupted!");
				}
				break;
			}
			try{			//银行在没有客户期间进行等待
				if(gap!=0)
				System.out.println("...\n"+new java.util.Date().toString()+":  Waiting consumers！\n ...");	//提示信息
				sleep(gap*1000);
			}catch(InterruptedException e){
				System.out.println("Interrupted!");
			}
			String nameTemp=nameArray[random.nextInt(15)];	//随机得到一位客户的姓名
			int command=random.nextInt(2)+1;	//随机产生存取业务
			int money=random.nextInt(10000);		//业务金额随机生成
			Thread t=new Thread(new Consumer(this,nameTemp,command,money),nameTemp);		//根据信息创建客户线程
			t.start();	//开始线程
			try {		//每个业务耗时一秒，线程暂停一秒
				sleep(1000);
			}catch(InterruptedException e){
			System.out.println("Interrupted!");
			}
			time--;				//每次业务耗时一秒
			if(time<0) break;
		}
		System.out.println(new java.util.Date().toString()+": 银行关门歇业！");
		System.out.println("银行客户余额及存取记录如下：");		//银行歇业后报告营业状况
		java.util.Formatter formatter=new  Formatter(System.out);
		formatter.format("%-10s %-10s %-10s %-10s \n","姓名 ","存入","支取 "," 账户余额 ");
		for(int i=0;i<numOfConsumer;i++)
			dataOfConsumer[i].print();
	}
	public static void main(String [] args)			//主函数入口
	{
		Bank bank=new Bank();
		bank.start();
	}
}

class Consumer implements Runnable{
	//顾客姓名
	String name;
	//银行客服
	Bank b;
	//标志该顾客是存钱还是取钱,1表示存钱，2表示取钱
	int mark;
	//业务涉及金额
	int numOfMoney;
	Consumer(Bank b,String name,int mark,int n){		//初始化
		this.b=b;
		this.name=name;
		this.mark=mark;
		numOfMoney=n;
	}
	public void run(){		//客户进行业务
		if(mark==1)
		{
			b.saveMoney(name,numOfMoney);
		}
		if(mark==2)
		{
			b.withdrawMoney(name,numOfMoney);
		}
	}
}
