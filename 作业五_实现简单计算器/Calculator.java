//~Calculator.java demo

/**2018/11/28 1711350 liwei
~Calculator.java  
users :
	本计算器能够实现考虑优先级的四则运算、取模和三角函数运算，并能够输入十进制的幂形式，
	同时实现了数据存储、存储求和求差以及提取内存数据的功能，
	在计算器中还能够实现负号的改变，错误输入的提醒，修改功能，同时能够添加了不同的删除功能。
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;
import java.lang.Number.*;
import java.text.DecimalFormat;

class MyFrame extends Frame{
	//设置输出数据的格式
	DecimalFormat df=new DecimalFormat("###.########");
	//建立两个栈，保存数据以及运算符实现优先级的考虑
	Stack<Double> n_stack=new Stack<Double>();
	Stack<String> c_stack=new Stack<String>();
	//保存按键的key值
		String[] str={
			"Mod","10^x","Π","CE","C","Del","sin","MC","1","2","3","+","cos","MR","4","5","6","-","tan","MS","7","8","9","x","+/-","M+","0",".","=","/"
		};
		//创建文本框以及按钮
	JTextField mt=new JTextField(10),
		it1=new JTextField(20),
		it2=new JTextField(20);
	JButton [] keys=new JButton[30];
	//记录计算器异常状态
	boolean worse=false;
	//加减乘除以及取模运算
	double func(double a,double b,String character)
	{
		if(character.equals("+"))
			return a+b;
		else if(character.equals("-"))
			return a-b;
		else if(character.equals("x"))
			return a*b;
		else if(character.equals("/"))
			return a/b;
		else if(character.equals("Mod"))
			return a%b;
		else return 0;
	}
	//三角函数运算重载函数
	double func(String character,double a)
	{
		if(character.equals("cos"))
			return Math.cos(Math.PI*(a/180));
		else if(character.equals("sin"))
			return Math.sin(Math.PI*(a/180));
		else if(character.equals("tan"))
			return Math.tan(Math.PI*(a/180));
		else if(character.equals("10^x"))
			return Math.pow(10,a);
		else return 0;
	}
	//注册监听器
	class depress implements ActionListener{
		public void actionPerformed(ActionEvent e)
		{
			//获取响应按钮的key值
			String keyName=((JButton)e.getSource()).getText();
			//对数字的处理
			for(int i=0;i<10;i++)
				if(keyName.equals(Integer.toString(i)))
				{
					if(it2.getText().equals("")||it2.getText().equals("0")||it2.getText().substring(0,1).equals("="))
						it2.setText(keyName);
					else
						it2.setText(it2.getText()+keyName);
				}
				//对小数点的处理
			if(keyName.equals("."))
			{
				if(it2.getText().indexOf(".",0)==-1)		//这样做使得在一次数据输入中只能够点击一次小数点，避免输入错误
					it2.setText(it2.getText()+keyName);
				return;
			}
			//对加减乘除以及取模运算符的处理
			if(keyName.equals("+")||keyName.equals("-")||keyName.equals("x")||keyName.equals("/")||keyName.equals("Mod"))
			{
				Double gD;
				if(it2.getText().substring(0,1).equals("="))
					gD=Double.valueOf(it2.getText().substring(1,it2.getText().length()));
				else
					gD=Double.valueOf(it2.getText());
				if(!c_stack.isEmpty()&&(c_stack.peek().equals("/")||c_stack.peek().equals("Mod"))&&Double.valueOf(it2.getText())==0)
				{
					it2.setText("除数或被模数不能为0,请按CE清除后重新输入");
					for(int i=0;i<30;i++)
						if(i!=3)
						keys[i].setEnabled(false);
					worse=true;
					return;
				}
				n_stack.push(gD);
				if(!c_stack.isEmpty())
					if(c_stack.peek().equals("cos")||c_stack.peek().equals("sin")||c_stack.peek().equals("tan")||c_stack.peek().equals("10^x"))
							n_stack.push(func(c_stack.pop(),n_stack.pop()));
					else
					if(c_stack.peek().equals("x")||c_stack.peek().equals("/")||c_stack.peek().equals("Mod"))
						{
							double b=n_stack.pop(),a=n_stack.pop();
							n_stack.push(func(a,b,c_stack.pop()));
						}
				c_stack.push(keyName);		
				it1.setText(it1.getText()+" "+df.format(gD)+" "+keyName);
				it2.setText("0");
				return;
			}
			//对清除按钮的处理，清除所有数据
			if(keyName.equals("C"))
			{
				it1.setText("");
				it2.setText("0");
				while(!c_stack.isEmpty())
					c_stack.pop();
				while(!n_stack.isEmpty())
					n_stack.pop();
				return;
			}
			//清除输入框
			if(keyName.equals("CE"))
			{
				it2.setText("0");
				if(worse==true)
				{
					for(int i=0;i<30;i++)
						keys[i].setEnabled(true);
					worse=false;
				}
				return;
			}
			//删除键
			if(keyName.equals("Del"))
			{
				if(it2.getText().length()!=0)
					it2.setText(it2.getText().substring(0,it2.getText().length()-1));
				return;
			}
			//圆周率Π
			if(keyName.equals("Π"))
			{			
				it2.setText("3.1415926");
				return;
			}
			//等于号响应
			if(keyName.equals("="))
			{
				if(!c_stack.isEmpty()&&(c_stack.peek().equals("/")||c_stack.peek().equals("Mod"))&&Double.valueOf(it2.getText())==0)
				{
					it2.setText("除数不能为0,请按CE清除后重新输入");
					for(int i=0;i<30;i++)
						if(i!=3)
						keys[i].setEnabled(false);
					worse=true;
					return;
				}
				if(!it2.getText().substring(0,1).equals("="))
					n_stack.push(Double.valueOf(it2.getText()));
				else 
					return;
				while(!c_stack.isEmpty()){
					if(c_stack.peek().equals("cos")||c_stack.peek().equals("sin")||c_stack.peek().equals("tan")||c_stack.peek().equals("10^x"))
							n_stack.push(func(c_stack.pop(),n_stack.pop()));
					else
						{
							double b=n_stack.pop(),a=n_stack.pop();
							n_stack.push(func(a,b,c_stack.pop()));
						}
				}
					it2.setText("= "+df.format(Double.valueOf(n_stack.pop())));
				it1.setText("");
				return;
			}
			//负号
			if(keyName.equals("+/-"))
			{
				if(Double.valueOf(it2.getText())>0)
				{
				it2.setText("-"+it2.getText());
				}
				else
					it2.setText(it2.getText().substring(1,it2.getText().length()));
				return;
			}
			//三角函数
			if(keyName.equals("cos")||keyName.equals("sin")||keyName.equals("tan")||keyName.equals("10^x"))
			{
				c_stack.push(keyName);
				if(keyName.equals("10^x"))
					it1.setText(it1.getText()+"10^");
				else
					it1.setText(it1.getText()+keyName);
				return ;
			}
			//保存当前结果与输入数据并与存储的数据相加，显示在内存显示框
			if(keyName.equals("M+"))
			{
				String res=it2.getText().substring(0,1).equals("=")?it2.getText().substring(1,it2.getText().length())
				:it2.getText();
				if(mt.getText().equals(""))
					mt.setText(res);
				else
					mt.setText(df.format(func(Double.valueOf(mt.getText()),Double.valueOf(res),"+")));
				return;
			}
			//存减
			if(keyName.equals("M-"))
			{
				if(mt.getText().equals(""))
					mt.setText(it2.getText());
				else
					mt.setText(df.format(func(Double.valueOf(mt.getText()),Double.valueOf(mt.getText()),"-")));
				return;
			}
			//内存清除
			if(keyName.equals("MC"))
			{
				mt.setText("");
				return;
			}
			//存储输入框的数据
			if(keyName.equals("MS"))
			{
				mt.setText(it2.getText().substring(0,1).equals("=")?it2.getText().substring(1,it2.getText().length())
				:it2.getText());
				return;
			}
			//将内存显示在输入框
			if(keyName.equals("MR"))
			{
				it2.setText(mt.getText().equals("")?"= 0":mt.getText());
				return;
			}
		}
	}
		
	public MyFrame()
	{
		super("Calculator");
		//设置窗口关闭动作
	addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e)
		{
        System.exit(0);
		}
	});//关闭窗口
	//添加面板，用来放置按钮
	JPanel kp=new JPanel(new GridLayout(5,6));
	int i=0;
	//添加按钮
	for(String s:str)
	{
		keys[i++]=new JButton(s);
	}
	//为按钮注册监听器
	for(int j=0;j<30;j++)
	{
		keys[j].addActionListener(new depress());
		kp.add(keys[j]);
	}
	//布局
		Box bh=Box.createHorizontalBox();
		Box iv=Box.createVerticalBox();
		mt.setEditable(false);
		it1.setEditable(false);
		it2.setEditable(false);
		it1.setHorizontalAlignment(JTextField.RIGHT);
		it2.setHorizontalAlignment(JTextField.RIGHT);
		it2.setText("0");		//数字输入框初始化为0
		iv.add(it1);iv.add(it2);
		bh.add(mt);
		bh.add(Box.createHorizontalStrut(10));
		bh.add(iv);
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(bh);
		add(kp);
		super.pack();
		setResizable(false);		//设置窗口大小不可变
		setVisible(true);//设置窗口可见
	}
}

public class Calculator{
	public  static void main(String[]args)
	{
		MyFrame calculator=new MyFrame();//调用窗口进行动作
	}	
	
}