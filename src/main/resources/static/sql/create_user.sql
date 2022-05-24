-- qs用户创建及授权
select * from mysql.user;
create user qs@'%' identified with mysql_native_password by 'qs@520';
grant alter,select,insert,update,delete on qs.* to qs@'%' with grant option;
flush privileges;