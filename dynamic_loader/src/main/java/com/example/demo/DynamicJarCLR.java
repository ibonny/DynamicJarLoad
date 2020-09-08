package com.example.demo;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DynamicJarCLR implements CommandLineRunner {
    public String apiFunction() {
        return "This is a test.";
    }

    @Override
    public void run(String... args) throws Exception {
        /*
         * JarInputStream myJarFile = new JarInputStream(new
         * FileInputStream("target/demo-0.0.1-SNAPSHOT.jar"));
         * 
         * JarEntry entry;
         * 
         * while (true) { entry = myJarFile.getNextJarEntry();
         * 
         * if (entry == null) { break; }
         * 
         * if (entry.getName().endsWith(".class")) { String className =
         * entry.getName().replaceAll("/", "\\.");
         * 
         * String myClass = className.substring(0, className.lastIndexOf('.'));
         * 
         * System.out.println(myClass); } }
         * 
         * myJarFile.close();
         */

        URLClassLoader child = new URLClassLoader(
            new URL[] { new URL("file:///workspace/DynamicJarLoad/basic_jar/target/demo-0.0.1-SNAPSHOT.jar") },
            this.getClass().getClassLoader()
        );

        Class<?> classToLoad = Class.forName("com.example.demo.BasicJarImpl", true, child);

        Method method = classToLoad.getDeclaredMethod("apiFunction");

        Object instance = classToLoad.getDeclaredConstructor().newInstance();

        if (!(instance instanceof BasicJar)) {
            System.out.println("Wrong instance type.");

            System.exit(0);
        }

        Object result = method.invoke(instance);

        String result2 = ((BasicJar) instance).apiFunction();

        System.out.println("Result is: " + result);
        System.out.println("Result2 is: " + result2);

        System.exit(0);
    }
}