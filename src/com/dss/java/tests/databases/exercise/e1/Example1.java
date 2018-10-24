package com.dss.java.tests.databases.exercise.e1;

/**
 * FileName: Example1
 * Author: Chris
 * Date: 2018/10/24 15:08
 * Description: Example 1
 */

import com.dss.java.tests.databases.utils.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import static junit.framework.TestCase.fail;


/**
 * 1. 创建数据库表
 */
public class Example1 {

    public static void main(String[] args) {
        Example example = new Example();
        try {
            example.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        fail();
    }

    static class Example {
        Scanner scanner;

        /**
         * 程序入口
         */
        void start() throws SQLException {
            // 系统初始化
            //init();
            int input = -1;
            scanner = new Scanner(System.in);
            do {
                printMenu();
                input = inputInt();
                switch (input) {
                    case 1:
                        insertData();
                        break;
                    case 2:
                        int result = queryData();
                        break;
                    case 0:
                        System.out.println("退出系统..");
                        break;
                    default:
                        break;
                }
            } while (input != 0);
            scanner.close();
        }

        /**
         * 查询信息
         */
        private int queryData() {
            printQueryMenu();
            char c = inputChar();
            System.out.println("c = " + c);
            while (c != 'a' && c != 'b') {
                System.out.println("您的输入有误，请重新输入...");
                c = inputChar();
            }
            switch (c) {
                case 'a':
//                    queryStudentByExamCard();
                    break;
                case 'b':
                    //queryStudentByIDCard();
                    break;
                default:
                    break;
            }
            return -1;
        }

        /**
         * 查询菜单
         */
        private void printQueryMenu() {
            System.out.println("#####  请选择要输入的类型:  ######");
            System.out.println("#########  a. 准考证号  ##########");
            System.out.println("#########  b. 身份证号  ##########");
        }

        /**
         * 插入一条数据
         */
        private void insertData() throws SQLException {
            // 先获得要插入的学生的信息
            ExamStudent student = getExamStudentInfo();
            insertExamStudent(student);
        }

        /**
         * 插入学生信息
         *
         * @param student
         */
        private void insertExamStudent(ExamStudent student) throws SQLException {
            // 获得数据库连接
            Connection connection = getConnection();
            // 获得Statement对象
            Statement statement = connection.createStatement();
            // 得到插入的SQL语句
            String sql = getInsertSQL(student);
            // 执行SQL语句
            statement.executeUpdate(sql);
            // 释放连接
            JDBCUtils.releaseConnection(statement, connection);
        }

        private String getInsertSQL(ExamStudent student) {
            String sqlFormat = "insert into examstudent(Type, IDCard, ExamCard, StudentName, Location, Grade) values(%i, %s, %s, %s, %s, %i)";
            String sql = String.format(sqlFormat, student.getType(), student.getIDCard(), student.getExamCard(), student.getStudentName(), student.getLocation(), student.getGrade());
            System.out.println("sql = " + sql);
            return sql;
        }

        /**
         * 获得要插入的学生信息
         *
         * @return
         */
        private ExamStudent getExamStudentInfo() {
            ExamStudent student = new ExamStudent();
            System.out.println("请输入考生的详细信息:");
            System.out.print("Type:");
            student.setType(inputInt());
            System.out.print("IDCard:");
            student.setIDCard(inputString());
            System.out.print("ExamCard:");
            student.setExamCard(inputString());
            System.out.print("StudentName:");
            student.setStudentName(inputString());
            System.out.print("Location:");
            student.setLocation(inputString());
            System.out.print("Grade:");
            student.setGrade(inputInt());
            System.out.println("信息录入成功！");
            return student;
        }

        /**
         * 系统初始化
         */
        private void init() {
            try {
                createDatabase();
                initData();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void printMenu() {
            System.out.println("##################################");
            System.out.println("#########  1. 录入成绩  ##########");
            System.out.println("#########  2. 查询成绩  ##########");
            System.out.println("#########  0. 退出      ##########");
            System.out.println("##################################");
        }

        private int inputInt() {
            return scanner.nextInt();
        }

        private String inputString() {
            return scanner.nextLine();
        }

        private char inputChar() {
            return scanner.next().charAt(0);
        }

        /**
         * 1. 创建数据表
         */
        private void createDatabase() throws SQLException, IOException, ClassNotFoundException {
            // 1.建立连接
//        Connection connection = JDBCUtils.getConnection();
            Connection connection = getConnection();
            // 2. 获得Statement对象
            Statement statement = connection.createStatement();
            // 3. 准备创建数据库的SQL语句
            String sql = "create table if not exists examstudent(FlowID  int primary key auto_increment comment '流水号', " +
                    "Type int comment '四级/六级', IDCard varchar(18) comment '身份证号码'," +
                    "ExamCard  varchar(15) comment '准考证号码', StudentName  varchar(20) comment '学生姓名', " +
                    "Location  varchar(20) comment '区域', Grade int comment '成绩');";
            // 4. 执行SQL语句
            statement.executeUpdate(sql);
            // 5. 释放资源
            JDBCUtils.releaseConnection(statement, connection);
        }

        /**
         * 2. 插入数据
         */
        private void initData() throws SQLException {
            // 1. 获取数据库连接
            Connection connection = getConnection();
            // 2. 获得Statement对象
            Statement statement = connection.createStatement();
            // 3. 编写SQL语句
            String sql = "insert into examstudent values(1,4,'412824195263214584', '200523164754000', '张锋', '郑州', 85)," +
                    "(2, 4, '222224195263214584', '200523164754001', '孙朋', '大连', 56)," +
                    "(3, 6, '342824195263214584', '200523164754002', '刘明', '沈阳', 72)," +
                    "(4, 6, '100824195263214584', '200523164754003', '赵虎', '哈尔滨', 95)," +
                    "(5, 4, '454524195263214584', '200523164754004', '杨丽', '北京', 64)," +
                    "(6, 4, '854524195263214584', '200523164754005', '王小红', '太原', 60)";
            // 4. 执行SQL语句
            statement.executeUpdate(sql);
            // 5. 释放连接
            JDBCUtils.releaseConnection(statement, connection);
        }

        /**
         * 获取数据库连接对象
         */
        private Connection getConnection() {
            InputStream is = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            Connection connection = null;
            try {
                // 获取数据库连接属性
                properties.load(is);
                String driverClass = properties.getProperty("driver");
                String jdbcUrl = properties.getProperty("jdbcUrl");
                String user = properties.getProperty("user");
                String password = properties.getProperty("password");

                // 加载驱动
                Class.forName(driverClass);
                // 连接数据库
                connection = DriverManager.getConnection(jdbcUrl, user, password);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return connection;
        }
    }

    static class ExamStudent {
        private int mFlowID;
        private int mType;
        private String mIDCard;
        private String mExamCard;
        private String mStudentName;
        private String mLocation;
        private int mGrade;

        public int getFlowID() {
            return mFlowID;
        }

        public void setFlowID(int mFlowID) {
            this.mFlowID = mFlowID;
        }

        public int getType() {
            return mType;
        }

        public void setType(int mType) {
            this.mType = mType;
        }

        public String getIDCard() {
            return mIDCard;
        }

        public void setIDCard(String mIDCard) {
            this.mIDCard = mIDCard;
        }

        public String getExamCard() {
            return mExamCard;
        }

        public void setExamCard(String mExamCard) {
            this.mExamCard = mExamCard;
        }

        public String getStudentName() {
            return mStudentName;
        }

        public void setStudentName(String mStudentName) {
            this.mStudentName = mStudentName;
        }

        public String getLocation() {
            return mLocation;
        }

        public void setLocation(String mLocation) {
            this.mLocation = mLocation;
        }

        public int getGrade() {
            return mGrade;
        }

        public void setGrade(int mGrade) {
            this.mGrade = mGrade;
        }
    }
}
