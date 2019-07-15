1

```mysql
select STUDENT_NO,STUDENT_NAME from HAND_STUDENT where STUDENT_NO not in(select a.STUDENT_NO from HAND_STUDENT a left join HAND_STUDENT_CORE b on a.STUDENT_NO=b.STUDENT_NO where b.COURSE_NO in (select COURSE_NO from HAND_COURSE c left join HAND_TEACHER d on c.TEACHER_NO=d.TEACHER_NO where d.TEACHER_NAME='谌燕'));

```

2

```mysql
select * from HAND_STUDENT where STUDENT_NO in (select STUDENT_NO from HAND_STUDENT_CORE group by  STUDENT_NO having count(STUDENT_NO)<(select count(*) from HAND_COURSE)) union select * from HAND_STUDENT where STUDENT_NO not in (select STUDENT_NO from HAND_STUDENT_CORE);

```

3

```mysql
select * from HAND_STUDENT where STUDENT_NO in (select a.STUDENT_NO from HAND_STUDENT_CORE a join HAND_STUDENT_CORE b on a.STUDENT_NO=b.STUDENT_NO and a.COURSE_NO='c001' and b.COURSE_NO='c002' where a.CORE>b.CORE);

```

4

```mysql
	
```

5

```mysql
年龄最小:
select STUDENT_NO,STUDENT_NAME,STUDENT_AGE from HAND_STUDENT where STUDENT_AGE=(select min(STUDENT_AGE) from HAND_STUDENT where curdate()-STUDENT_AGE>1992);
年龄最大:
select STUDENT_NO,STUDENT_NAME,STUDENT_AGE from HAND_STUDENT where STUDENT_AGE=(select max(STUDENT_AGE) from HAND_STUDENT where curdate()-STUDENT_AGE>1992);

```

6

```mysql
select case when a.CORE > 85 and a.core<100 then concat(b.COURSE_NO, ',', b.COURSE_NAME) end as '[100-85]',
    case when a.core>70 and a.core<85 then concat(b.COURSE_NO, ',', b.COURSE_NAME) end as '[85-70]',
    case when a.CORE > 60 and a.core<70 then concat(b.COURSE_NO, ',', b.COURSE_NAME) end as '[70-60]',
       case when a.CORE < 60 then concat(b.COURSE_NO, ',', b.COURSE_NAME) end as '[<60]'
from HAND_STUDENT_CORE a
         join HAND_COURSE b on a.COURSE_NO = b.COURSE_NO;
```

7

```mysql
select COURSE_NO,CORE from HAND_STUDENT_CORE group by COURSE_NO,CORE order by CORE desc ;
```

8

```

```

9

```

```

10

```

```

11

```mysql
select c.STUDENT_NO,STUDENT_NAME,d.COURSE_NAME,c.CORE,c.level as '等级' from(select a.STUDENT_NO, a.STUDENT_NAME,b.COURSE_NO, b.CORE, case when b.CORE > 90 then '优秀'
when b.CORE>=80 and b.CORE<90 then '良好'
when b.CORE>=60 and b.CORE<80 then '及格'
when b.CORE<60 then '不及格'
end as level
from HAND_STUDENT a
         join HAND_STUDENT_CORE b on a.STUDENT_NO = b.STUDENT_NO
where COURSE_NO in (select COURSE_NO from HAND_COURSE where COURSE_NAME = 'J2SE')) c join HAND_COURSE d on c.COURSE_NO=d.COURSE_NO;
```

12 

```mysql
select c.TEACHER_NO as '教师编号',c.TEACHER_NAME as 教师姓名,c.MANAGER_NO as '主管编号',d.TEACHER_NAME as '主管姓名' from (select a.TEACHER_NO as TEACHER_NO,a.TEACHER_NAME as TEACHER_NAME,a.MANAGER_NO as MANAGER_NO from HAND_TEACHER a left join HAND_TEACHER b on a.TEACHER_NO=b.MANAGER_NO and b.TEACHER_NAME='胡明星') as c left join HAND_TEACHER d on c.MANAGER_NO=d.TEACHER_NO;
```

13

