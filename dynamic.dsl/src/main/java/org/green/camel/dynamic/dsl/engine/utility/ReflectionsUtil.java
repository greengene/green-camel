package org.green.camel.dynamic.dsl.engine.utility;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.Attribute;

public class ReflectionsUtil {
	public static Method getMethod(final StackTraceElement stackTraceElement) throws Exception {
	    final String stackTraceClassName = stackTraceElement.getClassName();
	    final String stackTraceMethodName = stackTraceElement.getMethodName();
	    final int stackTraceLineNumber = stackTraceElement.getLineNumber();
	    Class<?> stackTraceClass = Class.forName(stackTraceClassName);

	    // I am only using AtomicReference as a container to dump a String into, feel free to ignore it for now
	    final AtomicReference<String> methodDescriptorReference = new AtomicReference<String>();

	    String classFileResourceName = "/" + stackTraceClassName.replaceAll("\\.", "/") + ".class";
	    InputStream classFileStream = stackTraceClass.getResourceAsStream(classFileResourceName);

	    if (classFileStream == null) {
	        throw new RuntimeException("Could not acquire the class file containing for the calling class");
	    }

	    try {
	        ClassReader classReader = new ClassReader(classFileStream);
	        ClassVisitor visitor = new EmptyVisitor() {
                @Override
                public MethodVisitor visitMethod(int access, final String name, final String desc, String signature, String[] exceptions) {
                    if (!name.equals(stackTraceMethodName)) {
                        return null;
                    }

                    return new EmptyVisitor() {
                        @Override
                        public void visitLineNumber(int line, Label start) {
                            if (line == stackTraceLineNumber) {
                                methodDescriptorReference.set(desc);
                            }
                        }
                    };
                }
            };
	        classReader.accept(visitor, new Attribute[0], 0);
	    } finally {
	        classFileStream.close();
	    }

	    String methodDescriptor = methodDescriptorReference.get();

	    if (methodDescriptor == null) {
	        throw new RuntimeException("Could not find line " + stackTraceLineNumber);
	    }

	    for (Method method : stackTraceClass.getMethods()) {
	        if (stackTraceMethodName.equals(method.getName()) && methodDescriptor.equals(Type.getMethodDescriptor(method))) {
	            return method;
	        }
	    }

	    throw new RuntimeException("Could not find the calling method");
	}
}