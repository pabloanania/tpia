/*
 * Copyright (c) 2012-2014, Ing. Gabriel Barrera <gmbarrera@gmail.com>
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above 
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES 
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR 
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES 
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN 
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF 
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package ia.battle.core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class WarriorLoader {
	
	@SuppressWarnings({ "unchecked", "resource" }) 
	public WarriorManager createWarriorManager(URL classLocation, String className) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {

		ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();

		URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { classLocation }, currentThreadClassLoader);
		
		Class<? extends WarriorManager> clazz = (Class<? extends WarriorManager>) urlClassLoader.loadClass(className);

		return clazz.newInstance();
	}
	
	@SuppressWarnings("resource")
	public ArrayList<String> getAllClasses(String jarFile, String superclassName) throws FileNotFoundException, IOException, ClassNotFoundException {
		
		ArrayList<String> list = new ArrayList<>();
		
		JarInputStream jar = new JarInputStream(new FileInputStream(jarFile));
		JarEntry entry;

		while ((entry = jar.getNextJarEntry()) != null) {

			if (entry.getName().endsWith(".class") && !entry.isDirectory()) {

				String className = entry.getName();

				className = className.replace("\\", ".").replace("/", ".");
				className = className.substring(0, className.length() - 6);

				list.add(className);		
			}
		}

		
		ArrayList<String> classesList = new ArrayList<>();
		
		ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();

		URLClassLoader urlClassLoader = new URLClassLoader(new URL[] { new URL("file:///" + jarFile) }, currentThreadClassLoader);
		
		for(String className : list) {
			
			Class<?> clazz = urlClassLoader.loadClass(className);
			if (clazz != null && clazz.getSuperclass() != null && clazz.getSuperclass().getName().equals(superclassName))
				classesList.add(className);
		}
		
		return classesList;
	}
	
}
