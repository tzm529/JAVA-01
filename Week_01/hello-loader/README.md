## 1. 字节码文件分析

Main.java 源文件代码如下：
```java
public class Main{

	public static void main(String ...args){
		int total = 0;
		for(int i = 0; i < 100; i++){
			if(i%2 == 0){
				total++;
			}
		}
		System.out.printf("total = %d", total);
	}

}
```

使用javac -g Main.java 编译后结果如下：
```
Compiled from "Main.java"
public class Main {
  public Main();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String...);
    Code:
       0: iconst_0
       1: istore_1
       2: iconst_0
       3: istore_2
       4: iload_2
       5: bipush        100
       7: if_icmpge     25
      10: iload_2
      11: iconst_2
      12: irem
      13: ifne          19
      16: iinc          1, 1
      19: iinc          2, 1
      22: goto          4
      25: getstatic     #7                  // Field java/lang/System.out:Ljava/io/PrintStream;
      28: ldc           #13                 // String total = %d
      30: iconst_1
      31: anewarray     #2                  // class java/lang/Object
      34: dup
      35: iconst_0
      36: iload_1
      37: invokestatic  #15                 // Method java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
      40: aastore
      41: invokevirtual #21                 // Method java/io/PrintStream.printf:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/io/PrintStream;
      44: pop
      45: return
}

main方法中Code块中关键命令简单分析：
    偏移4： iload_2 将i变量从局部变量中入栈
    偏移5： bipush 命令将for循环的上限值入栈
    偏移6:  if_icmpge 比较栈顶两int类型值，若iload_2入栈的值大于等于bipush入栈的值，后则跳转至偏移25的位置
    偏移12: irem 将栈顶两int类型数取模，结果入栈
    偏移13: ifne 若栈顶int类型值不为0则跳转至偏移19的位置，否则执行接下来的iinc命令
    偏移16: total变量加1
    偏移19: i变量加1
    偏移22: goto 跳转至偏移4的位置，进行下一次循环
```

## 2. 画一张图，展示 Xmx、Xms、Xmn、Metaspace、DirectMemory、Xss 这些内存参数的关系

1. 参数定义
```
-Xmx, 指定最大堆内存。 如 -Xmx4g. 这只是限制了 Heap 部分的最大值为4g。
这个内存不包括栈内存，也不包括堆外使用的内存。

-Xms, 指定堆内存空间的初始大小。 如 -Xms4g。 而且指定的内存大小，并
不是操作系统实际分配的初始值，而是GC先规划好，用到才分配。 专用服务
器上需要保持 –Xms 和 –Xmx 一致，否则应用刚启动可能就有好几个 FullGC。
当两者配置不一致时，堆内存扩容可能会导致性能抖动。

-Xmn, 等价于 -XX:NewSize，使用 G1 垃圾收集器 不应该 设置该选项，在其
他的某些业务场景下可以设置。官方建议设置为 -Xmx 的 1/2 ~ 1/4.
-XX：MaxPermSize=size, 这是 JDK1.7 之前使用的。Java8 默认允许的
Meta空间无限大，此参数无效。

-XX：MaxMetaspaceSize=size, Java8 默认不限制 Meta 空间, 一般不允许设
置该选项。

-XX：MaxDirectMemorySize=size，系统可以使用的最大堆外内存，这个参
数跟 -Dsun.nio.MaxDirectMemorySize 效果相同。

-Xss, 设置每个线程栈的字节数，影响栈的深度。 例如 -Xss1m 指定线程栈为
1MB，与-XX:ThreadStackSize=1m 等价

堆：主要用于存储实例化的对象、数组。由JVM动态分配内存空间，一个JVM只有一个堆内存，线程之间可以共享数据，
   在堆中分配的内存，由java虚拟机的自动垃圾回收器来处理。

栈：主要用于存储局部变量和对象的引用变量，每个线程都会有一个独立的栈空间，所以线程之间是不共享数据的。
```

2. 参数关系图(按PPT上相关资料结合个人理解，不是100%确定)：

![关系图](https://pic.youwa.net.cn/20210108120623Q9XN5243059P5YR7Z70W.jpg)
   

3. 疑问：
```
1. 有些参数资料上将堆分为三块：新生代、老年代及持久代。课件上是将堆划分为新生代和老年代两块，
   将持久代划到了非堆区，不确定哪个是最新的结构？

```

