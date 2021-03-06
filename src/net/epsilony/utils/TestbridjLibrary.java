/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.epsilony.utils;

import org.bridj.BridJ;
import org.bridj.Pointer;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
import org.bridj.ann.Name;
import org.bridj.ann.Runtime;
import org.bridj.cpp.CPPObject;
import org.bridj.cpp.CPPRuntime;

/**
 * Wrapper for library <b>testbridj</b><br> This file was autogenerated by <a
 * href="http://jnaerator.googlecode.com/">JNAerator</a>,<br> a tool written by
 * <a href="http://ochafik.com/">Olivier Chafik</a> that <a
 * href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few
 * opensource projects.</a>.<br> For help, please visit <a
 * href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a
 * href="http://bridj.googlecode.com/">BridJ</a> .
 */
@Library("testbridj")
@Runtime(CPPRuntime.class)
public class TestbridjLibrary {

    static {
        BridJ.register();
    }

    /**
     * This file was autogenerated by <a
     * href="http://jnaerator.googlecode.com/">JNAerator</a>,<br> a tool written
     * by <a href="http://ochafik.com/">Olivier Chafik</a> that <a
     * href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a
     * few opensource projects.</a>.<br> For help, please visit <a
     * href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a
     * href="http://bridj.googlecode.com/">BridJ</a> .
     */
    @Library("testbridj")
    public static class TestClass extends CPPObject {

        public TestClass() {
            super();
        }

        @Field(0)
        public int b() {
            return this.io.getIntField(this, 0);
        }

        @Field(0)
        public TestClass b(int b) {
            this.io.setIntField(this, 0, b);
            return this;
        }

        @Name("TestClass")
        public native int TestClass$2();

        public TestClass(Pointer pointer) {
            super(pointer);
        }
    }

    public static native int showB(Pointer<TestClass> t);

    public static native Pointer<TestClass> newInstance();
    
    public static void main(String[] args) {
        TestClass t1=new TestClass();
        Pointer<TestClass> tp2=newInstance();
        System.out.println("t1.b() = " + t1.b());
        System.out.println("tp2.get().b() = " + tp2.get().b());
    }
}
