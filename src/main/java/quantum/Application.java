package quantum;

import static utils.Utils.input;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Application {

    private static final SortedMap<Reference, Exercise> exercises = new TreeMap<>();

    public static void main(String[] args) {
        buildExercisesMap();

        System.out.println("QUANTUM COMPUTING for Computer Scientists - Code Examples\n\n");

        printExercises();

        String lastChoice = "";
        while (true) {
            String input = input("Example to run [" + lastChoice + "] (q quit, l list): ");

            if ("l".equalsIgnoreCase(input)) {
                printExercises();
            }
            if ("q".equalsIgnoreCase(input)) {
                break;
            } else {
                Reference reference = Reference.fromString("".equals(input) ? lastChoice : input);
                Exercise exercise = null;

                if (reference != null) {
                    exercise = exercises.get(reference);
                }

                if (exercise != null) {
                    lastChoice = reference.toString();
                    exercise.execute();
                } else {
                    lastChoice = "";
                }
            }
        }
    }

    private static void printExercises() {
        exercises.forEach((key, value) -> System.out.println(key + " " + value.title()));
    }

    private static void buildExercisesMap() {
        findClasses("quantum",
                clazz -> !Modifier.isAbstract(clazz.getModifiers()) && Exercise.class.isAssignableFrom(clazz))
                .forEach(clazz -> {
                    Exercise exercise;
                    try {
                        exercise = (Exercise) clazz.getConstructor().newInstance();
                        exercises.put(exercise.getReference(), exercise);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static List<Class> findClasses(String packageName, Function<Class, Boolean> filter) {
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packageName);

            List<Class> classes = new ArrayList<>();

            while(resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();

                if ("file".equals(protocol)) {
                    findClasses(classes, new File(resource.getFile()), packageName, filter);
                } else if ("jar".equals(protocol)) {
                    String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                    findClassesInJar(classes, new File(jarPath), packageName, filter);
                } else {
                    throw new RuntimeException("Unexpected protocol: " + protocol);
                }
            }

            return classes;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void findClassesInJar(List<Class> classes, File file, String packageName, Function<Class, Boolean> filter) throws ClassNotFoundException, IOException {
        try (JarFile jar = new JarFile(file)) {
            Enumeration<JarEntry> entries = jar.entries();

            while(entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace("/", ".").replace(".class", "");
                    Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);

                    if (clazz.getPackage().getName().startsWith(packageName) && filter.apply(clazz)) {
                        classes.add(clazz);
                    }
                }
            }
        }
    }

    private static void findClasses(List<Class> classes, File dir, String packageName, Function<Class, Boolean> filter) throws ClassNotFoundException {
        if (!dir.exists()) {
            throw new RuntimeException("Directory does not exist: " + dir.getAbsolutePath());
        }

        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findClasses(classes, file, packageName + "." + file.getName(), filter);
                } else if (file.getName().endsWith(".class")) {
                    Class<?> clazz = Class.forName(packageName + "." + file.getName().replace(".class", ""));

                    if (filter.apply(clazz)) {
                        classes.add(clazz);
                    }
                }
            }
        }
    }
}
