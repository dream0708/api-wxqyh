package com.jee.api.wxqyh.util;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtil {
    
    /**
     * 给属性赋值[默认包括了字段]
     * @param obj
     * @param proName
     * @param value
     * @throws IntrospectionException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    public static void setPropertyValue(Object obj,String proName,Object value) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
         BeanInfo beanInfo= Introspector.getBeanInfo(obj.getClass());
         for(PropertyDescriptor prop : beanInfo.getPropertyDescriptors()){
             if(prop.getName().equals(proName)){
                 Class<?> propType =prop.getReadMethod().getReturnType();
                 Object porpvalue = getValue(propType, value);
                 prop.getWriteMethod().invoke(obj, porpvalue);
                 return ;
             }
         }
         
         for(java.lang.reflect.Field field : obj.getClass().getFields()){
             if( field.getName().equals(proName)){
                 Object filedValue= getValue(field.getType(),value);    
                 field.set(obj, filedValue);            
                 return ;
             }
         }
    }
    
    /**
     * 得到属性的类别
     * @param obj
     * @param proName
     * @return 
     * @throws IntrospectionException 
     */
    public static Class<?> getPropertyType(Object obj,String proName) throws IntrospectionException{
         BeanInfo beanInfo= Introspector.getBeanInfo(obj.getClass());
         for(PropertyDescriptor prop : beanInfo.getPropertyDescriptors()){
             if(prop.getName().equals(proName)){
                return prop.getReadMethod().getReturnType();
             }
         }         
         for(java.lang.reflect.Field field : obj.getClass().getFields()){
             if( field.getName().equals(proName)){
                 return field.getType();
             }
         }
         return null;
    }
    
    /**
     * 把obj转成type类型
     * @param type
     * @param obj
     * @return
     */
    public static Object getValue(Class<?> type,Object obj){
        String className = type.getName();
        if(obj.getClass() == type){
            return obj;
        }
        if(type .equals(Double.class) ||className=="double"){
            return Double.parseDouble(obj.toString());
        }
        if(type==Float.class||className=="float"){
            return Float.parseFloat(obj.toString());
        }
        if(type==Integer.class||className=="int"){
            return Integer.parseInt(obj.toString());
        }
        if(type.equals( String.class)||className=="string"){
            return obj.toString();
        }
        if(type.equals(Boolean.class)||className=="boolean"){
            return Boolean.parseBoolean(obj.toString());
        }
        if(type.isEnum()){
            Class<?>[] params = new Class<?>[1];
            params[0] = String.class;
            try {
                return type.getDeclaredMethod("valueOf", params).invoke(null, obj.toString());
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        //if(type.equals(Enum))
        return null;
    }
    
}
