package com.dss.java.tests.databases.bean;

import org.junit.Test;

/**
 * User: DSS
 * Date: 2018/10/24
 * Time: 21:57
 * Tag: Student
 */
public class Student {
    private int mFlowID;
    private int mType;
    private String mIDCard;
    private String mExamCard;
    private String mStudentName;
    private String mLocation;
    private int mGrade;

    @Override
    public String toString() {
        return "Student{" +
                "mFlowID=" + mFlowID +
                ", mType=" + mType +
                ", mIDCard='" + mIDCard + '\'' +
                ", mExamCard='" + mExamCard + '\'' +
                ", mStudentName='" + mStudentName + '\'' +
                ", mLocation='" + mLocation + '\'' +
                ", mGrade=" + mGrade +
                '}';
    }

    public Student(int flowID, int type, String IDCard, String examCard, String studentName, String location, int grade) {
        mFlowID = flowID;
        mType = type;
        mIDCard = IDCard;
        mExamCard = examCard;
        mStudentName = studentName;
        mLocation = location;
        mGrade = grade;
    }

    public Student() {
    }

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

    @Test
    public void testGetSql() {
        Student student = new Student();
        student.setFlowID(10);
        student.setType(4);
        student.setIDCard("11111611420");
        student.setExamCard("1234567890123456");
        student.setStudentName("张三");
        student.setLocation("华盛顿");
        student.setGrade(90);
        String sql = getInsertSQL(student);
        System.out.println("sql = " + sql);
    }

    private String getInsertSQL(Student student) {
        String sqlFormat = "insert into examstudent(Type, IDCard, ExamCard, StudentName, Location, Grade) values(%d, %s, %s, %s, %s, %d)";
        String sql = String.format(sqlFormat, student.getType(), student.getIDCard(), student.getExamCard(), student.getStudentName(), student.getLocation(), student.getGrade());
        System.out.println("sql = " + sql);
        return sql;
    }
}
