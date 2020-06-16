//：CDRentSystem.java
//1711350 李伟  2018/11/04完成

import java.io.*;  //文件流输入输出需要
import java.util.Scanner;
/**实现会员的信息保存*/
class Member{
	/**会员编号*/
	int id;
	/**会员名字*/
	String name;
	/**会员年龄*/
	int	age;
	/**会员租用的CD名称，默认每位会员最多组用10张CD*/
	String rent[]=new String[10];
	/**会员已经租用的CD数量*/
	int rentNumber=0;
	/**链接下一个节点*/
	Member nextMember;
	Member(){};
	//初始化函数
	Member(int _id,String _name,int _age)
	{
		name=_name;
		age=_age;
		id=_id;
	}
	/**判断会员租用CD数量是否达到上限
	*@return 返回是否达到上限
	*/
	boolean isfull(){
		if(rentNumber==10)
			return true;
		else
			return false;
	}
	/**对本会员租用信息的修改
	*@param  CDname 传入一个CD的名称
	*@return 返回是否修改成功
	*/
	boolean rentCD(String CDname)
	{
		if(!isfull())
		{
			rent[rentNumber++]=CDname;return true;
		}
		else
			return false;
	}
}

/**会员类:用链表形式保存会员数量以及所有会员信息*/
class Membership{
	/**统计会员人数*/
	private int count=0;
	private int money=0;			//保存会员所交的会费，设计为每人500元
	/**head、tail表示首位位置*/
	Member head,tail;
	/**插入函数
	*@param a 输入一个Member对象
	*@return 是否存在id冲突
	*/
	boolean insert(Member a){
		if(find(a.id))	return false;//不允许相同的id
		if(head==null)
		{	
			head=tail=a;
			count++;
			money+=500;
			return true;
		}
		tail.nextMember=a;
		tail=a;
		count++;
		money+=500;
		return true;		//成功添加
	}
	/**获取统计数据
	*@return 返回一个int数据,表示会员总数
	*/
	int getCount(){return count;}
	/**获取金钱统计数据
	*@return 返回一个int数据,表示店铺的收入
	*/
	int getMoney(){return money;}
	/**删除会员函数
	*@param id 输入一个会员编号
	*@return 返回能否成功删除
	*/
	boolean delete(int id){
		if(head.id==id)			//是否与第一个位置相同
		{
			head=head.nextMember;
			count--;
			return true;
		}
		Member temp=head;
		while(temp.nextMember!=null)
		{
			if(temp.nextMember.id==id)
			{
				temp.nextMember=temp.nextMember.nextMember;
				count--;
				return true;
			}
			temp=temp.nextMember;
		}
		return false;
	}
	/**显示当前会员信息函数	
	*同时也会显示会员租用了那些CD
	*/
	 void display ()
	{
		Member temp=head;
		System.out.println("ID"+"\t"+"NAME"+"\t"+"AGE"+"\t"+"RENTNUMBER"+"\t"+"RENTCDNAMES"+"\t");
		while(temp!=null)
		{
				System.out.print(temp.id+"\t"+temp.name+"\t"+temp.age+"\t"+temp.rentNumber+"\t");
				int i=temp.rentNumber;
				for(String str:temp.rent)
				{
					if(i==0) break;
					System.out.print(str);
					i--;	
				}
					System.out.println("");		//换行5
			temp=temp.nextMember;
		}				
	}
	/**查询函数
	*@param name 输入一个会员姓名
	*@return 返回是否存在目标查询会员
	*/
	boolean find (String name)
	{
		Member temp=head;
		while(temp!=null)
		{
			if(temp.name.equals(name))
			{
				return true;
			}
			temp=temp.nextMember;
		}
		return false;
	}
		/**重载查询函数
	*@param id  输入一个会员编号
	*@return 返回是否存在目标查询会员
	*/
	boolean find (int id)
	{
		Member temp=head;
		while(temp!=null)
		{
			if(temp.id==id)
			{
				return true;
			}
			temp=temp.nextMember;
		}
		return false;
	}
	/**修改会员租用CD信息
	*@param name 输入一个会员姓名
	*@param CDname CD名称
	*@return 返回存储会员是否能够租用此CD 
	*/
	boolean rent(String name,String CDname)
	{
		Member temp=head;
		while(temp!=null)
		{
			if(temp.name.equals(name))
				if(!temp.isfull())
				{
					return temp.rentCD(CDname);
				}
				else
					return false;
			temp=temp.nextMember;
		}
		return false;
	}
	/**修改会员租用CD信息--归还CD
	*@param name 输入一个会员姓名
	*@param CDname CD名称
	*@return 返回存储会员是否租用此CD，是返回true并且将CD名称在租用名录中删除
	*/
	boolean back(String name,String CDname)
	{
		Member temp=head;
		while(temp!=null)
		{
			if(temp.name.equals(name))
			{
				for(int i=0;i<10;i++)
				{
					if(CDname.equals(temp.rent[i]))
					{
						temp.rent[i]=null;//修改信息
						temp.rentNumber--;
						return true;
					}
				}
			}
			temp=temp.nextMember;
		}
		return false;
	}
}

/**CD的信息保存*/
class CD{
	/**CD已出租数量，默认为0*/
	int rentnum=0;
	/**CD编号
	*一般代表进货时间
	*/   
	int id;
	/**CD价格*/
	int price;
	/**CD名称*/
	String name;
	/**CD库存*/
	int	number;
	/**链接下一个节点*/
	CD nextCD;
	CD(){};
	CD(int _id,int _price,String _name,int _number)
	{
		id=_id;
		name=_name;
		price=_price;
		number=_number;
	}
}

/**CD店CD库存及经营信息类:用链表形式所有CD信息*/
class CDstore{
	/**head、tail表示首位位置*/
	private int count=0;		//cd种类
	private int money=0;		//建立店铺金钱数为0，记录卖出CD的收入
	CD head,tail;
	/**出租CD
	*@param VIPS 会员信息系统接入
	*@param VIPname 租用者
	*@param  CDname 预备租用CD名称
	*@return 返回该租用CD者能否顺利租用这个CD
	*/
	boolean rent(Membership VIPS,String VIPname,String CDname)
	{
		if(!VIPS.find(VIPname)||this.find(CDname)==-1||this.find(CDname)==0)//租用者不是会员或者库存中没有可租用的CD
			return false;
		if(VIPS.rent(VIPname,CDname))		//会员能租用
		{
			CD temp=head;
			while(temp!=null)
			{
				if(temp.name.equals(CDname))
				{
					temp.rentnum++;		//该CD已租出数量加1
					return true;
				}
				temp=temp.nextCD;
			}
		}		
		return false;	//会员租用数量已满		
	}
	/**进货函数，大规模购进库存中没有的存货，用于建立CD库存时使用
	*@param a 输入一个CD对象
	*/
	void insert(CD a){
		if(head==null)
		{	
			head=tail=a;
			count++;
			return ;
		}
		tail.nextCD=a;
		tail=a;
		count++;
	}
	/**补充不足货物函数，后期购进库存已存在的存货
	*@param 　name　输入一个CD对象名称
	*@return 如果该CD不存在库存中则返回false
	*/
	boolean insert(String name){
		CD temp=head;
		while(temp!=null)
		{
			if(temp.name.equals(name))
			{
				temp.number++;
				return true;
			}
			temp=temp.nextCD;
		}
		return false;
	}
	/**归还CD函数
	*@param VIPS  接入VIP记录系统
	*@param VIPname 输入一个会员名
	*@param CDname 输入一个CD名
	*@return 返回否成功归还
	*/
	boolean back(Membership VIPS,String VIPname,String CDname){
		if(!VIPS.find(VIPname)||this.find(CDname)==0)//租用者不是会员或者库存中不存在此类CD
			return false;
		if(VIPS.back(VIPname,CDname))
		{
			CD temp=head;
			while(temp!=null)
			{
				if(temp.name.equals(CDname))
				{
					temp.rentnum--;		//该CD已租出数量-1		
					return true;		//成功归还
				}
				temp=temp.nextCD;
			}
		}
		return false;	//归还失败
	}
	/**获取统计数据
	*@return 返回一个int数据，表示店内CD的种类数
	*/
	int getCount(){return count;}
	/**获取金钱统计数据
	*@return 返回一个int数据,表示店铺的收入
	*/
	int getMoney(){return money;}
	/**卖出cd
	*@param name 输入一个CD名称
	*@param number　卖出该CD 的数量
	*@return 返回一个int变量，显示卖出交易是否能够完成，若库存不足则交易失败返回0，否则返回售出此CD的收入
	*/
	int sell(String name,int number){
		if(find(name)==0|find(name)==-1) return 0;
		CD temp=head;
		while(temp!=null)
		{
			if(temp.nextCD.name.equals(name))
			{
				if((temp.nextCD.number-temp.nextCD.rentnum)>number)
				{
					temp.nextCD.number-=number;
					money+=temp.nextCD.price*number;
					return temp.nextCD.price*number;
				}
				else 
					if((temp.nextCD.number-temp.nextCD.rentnum)==number)//没有在租情况，直接售空
					{	if(temp.rentnum==0)	//没有在租情况，直接售空
						{	
							temp.nextCD=temp.nextCD.nextCD;
							count--;
							money+=temp.nextCD.price*number;
							return temp.nextCD.price*number;
						}
						else
						{
							temp.nextCD.number-=number;
							money+=temp.nextCD.price*number;
							return temp.nextCD.price*number;
						}
					}
				else return 0;//库存不足
			}
			temp=temp.nextCD;
		} 
		return 0;
	}
	/**查询函数
	*@param name 输入一个CD名称
	*@return  返回是否存在目标查询CD,存在返回1，不存在返回0，因租用导致可租用CD数为零的返回-1
	*/
	int find (String name)
	{
		CD temp=head;
		while(temp!=null)
		{
			if(temp.name.equals(name))		//注意使用equals（）和==的区别
				if(temp.number-temp.rentnum>0)
					return 1;
				else
					return -1;
			temp=temp.nextCD;
		}
		return 0;
	}
	/**显示当前CD库存信息函数*/
	void display ()
	{
		CD temp=head;
		System.out.println("ID"+"\t"+"NAME"+"\t"+"PRICE"+"\t"+"NUMBER"+"\t"+"RENTNUMBER"+"\t");
		while(temp!=null)
		{
			System.out.println(temp.id+"\t"+temp.name+"\t"+temp.price+"\t"+temp.number+"\t"+temp.rentnum+"\t");
			temp=temp.nextCD;
		}
	}
}

/**CDRentSystem主类
*@author 李伟  学号：1711350
*@version Java课程第三次作业 
*@version 实现对CD店出租CD以及会员管理相关操作的设计
*@version 2018/11/2
*/
public class CDRentSystem {
	/**创建店铺  建立会员信息以及CD初始库存
	*@param vipImformationPath 传入保存VIP信息的文件路径
	*@param vips 传入Vip的系统保存链表
	*@param CDImformationPath Cd信息文件路径
	*@param cdstore 保存CD信息的数据结构
	*@return boolean 返回是否创建成功
	*/
	static boolean openStore(String vipImformationPath,Membership vips,String CDImformationPath,CDstore cdstore)	//从文件中读取基本信息，方便调试
	{
		//读取会员信息
		  File file1 = new File(vipImformationPath);
			if(! file1.exists()){
				System.out.println("对不起，不包含指定路径的文件");
				return false;
			}else{
			//根据指定路径的File对象创建Scanner对象
				try {
					Scanner sc1 = new Scanner(file1);
					while(sc1.hasNext()){          //循环扫描读取文件中的数据
						String str1 = sc1.next();         //根据读取文件的内容创建String 对象
						String str2= sc1.next();
						String str3=sc1.next();
						Member vip=new Member(Integer.parseInt(str1),str2,Integer.parseInt(str3));
						
						//vip.printImf(); //输出读取内容
						
						vips.insert(vip);	//存入链表
						//System.out.println("#");
					}
				}                          //关闭流
				catch (Exception e) {
                // TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//读取CD的信息
			 File file2 = new File(CDImformationPath);
			if(! file2.exists()){
				System.out.println("对不起，不包含指定路径的文件");
				return false;
			}else{
				//根据指定路径的File对象创建Scanner对象
				try {
					Scanner sc2 = new Scanner(file2);
					while(sc2.hasNext()){          //循环扫描读取文件中的数据
						String str1 = sc2.next();         //根据读取文件的内容创建String 对象
						String str2 = sc2.next();  
						String str3 = sc2.next();  
						String str4 = sc2.next();  
						CD cd=new CD(Integer.parseInt(str1),Integer.parseInt(str2),str3,Integer.parseInt(str4));
						cdstore.insert(cd);		//存入CD类链表
						//cd.printImf();//输出读取内容
						            
						//System.out.println("#");
					}
				} catch (Exception e) {
                // TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
	}
	/**获取操作帮助*/
	static void help(){
		System.out.println("-----------------------------------------------");//输出操作提示
		System.out.println("0、获取操作帮助目录。");
		System.out.println("1、获取会员信息。");
		System.out.println("2、获取CD库存信息。");
		System.out.println("3、添加会员。");
		System.out.println("4、删除会员。");
		System.out.println("5、出租CD，每位会员限租10张CD。");
		System.out.println("6、归还CD。");
		System.out.println("7、出售CD。");
		System.out.println("8、购入新品类的CD。");
		System.out.println("9、补充旧品类CD库存。");
		System.out.println("10、获取店内收入信息。");
		System.out.println("11、退出CD租售系统。");
		System.out.println("在>>后面输入操作指令的序号！");
		System.out.println("-----------------------------------------------");
	}
	/**
	/**CDRentSystem descriptions
	*主要实现对CD会员租售系统的功能测试
	* @param args array of string arguements
	* @exception exceptions No exceptions thrown
	*/
	public static void main(String []args)
	{
		System.out.println("李氏CD租售店开张了！");
		help();
		CDstore cdStore=new CDstore();Membership VIPS=new Membership();			//声明并初始化对象
		openStore( "vipImformation.txt", VIPS,"CDImformation.txt", cdStore);			//开店，从文件中获取信息建立基本的会员名录和CD库存
		System.out.print(">>");
		Scanner sc=new Scanner(System.in);int command;
		exit:while(true){			//不断循环
		//---------------输入判断----------------------------------
			 if(sc.hasNextInt())			//判断输入是否为数字序号
				 command=sc.nextInt();
			else
			{
				System.out.println("输入有误，请输入数字操作序号！");
				sc.next();			//跳过当前错误输入。
				System.out.print(">>");
				continue;			//跳过当前循环
			}
		//----------------------------------------------------------
			switch(command)
			{
			case 0:
				help();					//获取操作帮助
				break;
				case 1:
				VIPS.display();			//显示会员信息信息
				break;
			case 2:
				cdStore.display();		//显示CD库存信息
				break;
			case 3:				//添加会员
				System.out.print("请依次输入会员编号（一般为7位数字）、姓名、年龄:");
				int id=sc.nextInt();String name=sc.next();int age=sc.nextInt();
				if(VIPS.insert(new Member(id,name,age)))
					System.out.println("添加会员成功！");
				else
					System.out.println("添加失败！会员编号已存在！");
				break;
			case 4:					//删除会员
				System.out.print("请删除会员的id：");
				int id1=sc.nextInt();
				if(VIPS.delete(id1))
					System.out.println("删除会员成功！");
				else
					System.out.println("删除失败！会员编号不存在！");
				break;
			case 5:					//出租
				System.out.print("请依次输入租用者名字、CD名称:");
				String VIPname=sc.next(),CDname=sc.next();
				if(cdStore.rent(VIPS,VIPname,CDname))
					System.out.println(VIPname+"成功租用"+CDname);
				else
					System.out.println("租用CD失败！请检查CD库存以及该会员是否达到租用上限。");
				break;
			case 6:					//归还CD
				System.out.print("请依次输入租用者名字、待归还CD名称:");
				String VIPname1=sc.next(),CDname1=sc.next();
				if(cdStore.back(VIPS,VIPname1,CDname1))
					System.out.println(VIPname1+"成功归还"+CDname1);
				else
					System.out.println("归还CD失败！请检查CD库存以及输入是否有误。");
				break;
			case 7:					//出售
				System.out.print("请依次输入欲出售CD名称、出售数量:");
				String CDname2=sc.next();int number0=sc.nextInt();
				if(cdStore.sell(CDname2,number0)!=0)
					System.out.println("成功出售"+CDname2+number0+"张");
				else
					System.out.println("出售CD失败！请检查CD库存。");
				break;
			case 8:						//添加新品种CD
				System.out.print("请依次输入CD编号（如20181104）、名称、价格、数量:");
				int id2=sc.nextInt();String name2=sc.next();int price2=sc.nextInt();int number2=sc.nextInt();
				cdStore.insert(new CD(id2,price2,name2,number2));
				System.out.println("添加CD成功！");
				break;
			case 9:					//添加已有库存
				System.out.print("请输入要添加CD的名称、添加数量:");
				String name3=sc.next();int number =sc.nextInt();
				if(cdStore.find(name3)!=0)
				{
					while(number-->0)
				cdStore.insert(name3);
				System.out.println("添加CD成功！");
				}
				else
					System.out.println("添加CD失败，不存在此库存CD！");
				break;
			case 10:					//获取收入信息
				System.out.println("店内总收入:"+(cdStore.getMoney()+VIPS.getMoney())+"元");
				System.out.println("$$会员会费收入:"+VIPS.getMoney()+"元");
				System.out.println("$$销售CD收入:"+cdStore.getMoney()+"元");
				break;
			case 11:
				System.out.println("退出CD租售系统！");
				break exit;			//跳出while()
			default :
				System.out.println("输入指令有误，可以输入0序号获取指令帮助！");
				break;
			}
			System.out.print(">>");
		}
	}
}//:~

