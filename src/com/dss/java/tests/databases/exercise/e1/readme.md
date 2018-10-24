### JDBCUtils
```
getConnection(String name)
releaseDB(ResultSet set, Statement statement, Connection connection)
update(String sql)  `update, insert, delete`
query(String sql)   'select'
```

### Student  `Bean类`
```$xslt 实体类对应于数据库中的属性
int flowID;
int type;
String idCard;
String examCard;
String studentName;
String location;
int grade;
```

### Main  `程序入口`
```
getStudentFromConsole()  // 从控制台获得Student信息
addStudent(Student student) {
    student -> sql;
    JDBCUtils.update(sql);
}
getStudentInfo(String sql)
```