package com.vestel.dss.mode;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

/**
 * FileName: TestFactoryMode
 * Author: Chris
 * Date: 2018/9/4 10:21
 * Description: Test Factory Mode
 */
public class TestFactoryMode {
    public static void main(String[] args) {
        WorkFactoryImpl workFactory = new WorkFactoryImpl();
        TeacherWorkFactory factory = (TeacherWorkFactory) workFactory.getWorkFactory();
        Work work = factory.getWork();
        work.doWork();
        TeacherWorkFactory teacherWorkFactory = new TeacherWorkFactory();

    }
    @Test
    public void test() {
        System.out.println("true = " + true);
        List list = null;
        Iterator iterator = list.iterator();
    }

}


interface Work{
    void doWork();
}
class WorkFactory{}

class TeacherWork implements Work {

    @Override
    public void doWork() {

    }
}

class StudentWork implements Work {
    @Override
    public void doWork() {

    }
}

interface IWork {
    Work getWork();
}
interface IWorkFactory{
    WorkFactory getWorkFactory();
}

class WorkFactoryImpl implements IWorkFactory{

    @Override
    public WorkFactory getWorkFactory() {
        if (Boolean.TRUE == true) {
            return new TeacherWorkFactory();
        }
        return new StudentWorkFactory();
    }
}

class TeacherWorkFactory extends WorkFactory implements IWork {

    @Override
    public Work getWork() {
        return new TeacherWork();
    }
}

class StudentWorkFactory extends WorkFactory implements IWork {

    @Override
    public Work getWork() {
        return new StudentWork();
    }
}
