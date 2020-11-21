package ioc.support;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
 

public class ObjectUtil {
  
	//返回对象的深拷贝
    public static Object objToObj(Object source, Class<?> targetClazz) throws InstantiationException, IllegalAccessException {
        Object target = targetClazz.newInstance();
        Class sourceClazz = source.getClass();
 
        Field[] sourceFields = getAllFields(sourceClazz);
        Field[] targetFields = getAllFields(targetClazz);
        for (Field sourceField : sourceFields) {
            sourceField.setAccessible(true);
            for (Field targetField : targetFields) {
                if (targetField.getName().equals(sourceField.getName()) && targetField.getType() == sourceField.getType()) {
                    int mod = targetField.getModifiers();
                    if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                        continue;
                    }
                    targetField.setAccessible(true);
                    targetField.set(target, sourceField.get(source));
                    break;
                }
            }
        }
        return target;
    }
 
    public static Field[] getAllFields(final Class<?> cls) {
        final List<Field> allFieldsList = getAllFieldsList(cls);
        return allFieldsList.toArray(new Field[allFieldsList.size()]);
    }
 
    public static List<Field> getAllFieldsList(final Class<?> cls) {
        final List<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
                allFields.add(field);
            }
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }
 
    
}