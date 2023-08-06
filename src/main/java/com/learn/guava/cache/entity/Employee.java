package com.learn.guava.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class Employee {
    private String name;
    private String dept;
    private String empId;
    private byte[] block = new byte[1024 * 1024];

    public Employee(String name, String dept, String empId) {
        this.name = name;
        this.dept = dept;
        this.empId = empId;
    }

    @Override
    protected void finalize() {
        System.out.println("emp: [" + name + "] will be finalized");
    }
}
