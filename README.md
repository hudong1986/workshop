# workshop
一个轻巧的基于工厂生产作业的员工绩效管理系统
1.该系统是由本人独自编写代码完成的，原本是打算给创业的朋友做一个简单的工人绩效管理系统的，后来因为其它原因并没有真正使用，但该系统功能基本是全面的，
当然有java开发经验的同学可以很容易在上面添加修改功能。

2.系统由于是早期开发，并没有使用spring boot框架，就是原始的spring mvc 项目，用eclipse 4.6.1开发完成，使用前请先用根目录下的workshopdb.sql文件在
mysql里生成数据库和表，然后在db.properties里可以修改数据连接信息。

3. 功能的话主要是 对员工进行基本面工资、记件工资、考勤、请假、奖励等进行综合工资计算，员工可以查看自己的工资，人事及其它管理员有相应的权限功能，开发人员
可以通过查看源代码清楚的知道业务细节，本系统代码不多，有经验的java 开发人员可以很快理解并完善。
