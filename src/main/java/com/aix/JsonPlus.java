package com.aix;

import com.aix.parser.DefaultParser;
import com.aix.parser.Parser;
import com.aix.parser.TypeRef;
import com.aix.parser.ast.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonPlus {

    public static Expression parseToExpression(String json) {
        Parser parser = new DefaultParser(json);
        return parser.parse();
    }

    public static <T> T parse(String json, TypeRef<T> typeRef) {
        if (json == null) return null;
        json = json.trim();
        if (!json.startsWith("{") && !json.startsWith("[")) return null;
        Expression expression = parseToExpression(json);
        Type type = typeRef.getType();
        return convertToType(expression,type);
    }

    public static String toJson(Object obj) {
        Expression expression = toExpression(obj);
        return expression.toJson();
    }

    private static <T> T convertToType(Expression expression, Type type) {
        if (type instanceof ParameterizedType pt) {
            return convertToRawType(expression,pt.getRawType(),pt.getActualTypeArguments());
        }
        return convertToRawType(expression,type,new Type[]{Object.class});
    }

    private static <T> T convertToRawType(Expression expression, Type rawType, Type[] actualTypeArguments) {
        if (rawType instanceof Class<?> clazz) {
            boolean isObjectType = clazz.isAssignableFrom(Object.class);
            boolean isObjectToListType = isObjectType && expression instanceof ArrayExpression;
            boolean isObjectToMapType = isObjectType && expression instanceof ObjectExpression;
            boolean isObjectToBaseType = isObjectType && !(isObjectToListType || isObjectToMapType);
            if(isObjectToListType) {
                return handleListConversion(expression, actualTypeArguments[0]);
            } else if(isObjectToMapType) {
                return handleMapConversion(expression, actualTypeArguments[0]);
            }else if(
                isObjectToBaseType ||
                clazz.isAssignableFrom(String.class) ||
                clazz.isAssignableFrom(boolean.class) ||
                clazz.isAssignableFrom(Boolean.class) ||
                clazz.isAssignableFrom(byte.class) ||
                clazz.isAssignableFrom(Byte.class) ||
                clazz.isAssignableFrom(short.class) ||
                clazz.isAssignableFrom(Short.class) ||
                clazz.isAssignableFrom(int.class) ||
                clazz.isAssignableFrom(Integer.class) ||
                clazz.isAssignableFrom(long.class) ||
                clazz.isAssignableFrom(Long.class) ||
                clazz.isAssignableFrom(float.class) ||
                clazz.isAssignableFrom(Float.class) ||
                clazz.isAssignableFrom(double.class) ||
                clazz.isAssignableFrom(Double.class) ||
                clazz.isAssignableFrom(BigDecimal.class)
            ){
                return handleBaseConversion(expression,clazz);
            } else if(clazz.isAssignableFrom(List.class)){
                return handleListConversion(expression, actualTypeArguments[0]);
            } else if(clazz.isAssignableFrom(Map.class)){
                return handleMapConversion(expression, actualTypeArguments[1]);
            } else if(expression instanceof ObjectExpression objectExpression){
                return handleClassObjectConversion(objectExpression, clazz);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T handleListConversion(Expression expression, Type childrenType) {
        if (expression instanceof ArrayExpression arrayExpression) {
            return (T) arrayExpression.getMembers().stream()
                    .map(member -> convertToType(member, childrenType))
                    .collect(Collectors.toList());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T handleMapConversion(Expression expression, Type valueType) {
        if (expression instanceof ObjectExpression objectExpression) {
            Map<String, Object> data = new LinkedHashMap<>();
            for (Map.Entry<String, Expression> entry : objectExpression.getMembers().entrySet()) {
                data.put(entry.getKey(), convertToType(entry.getValue(), valueType));
            }
            return (T) data;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T handleBaseConversion(Expression expression, Class<?> clazz) {
        if (expression instanceof NumberExpression numberExpression)
        {
            BigDecimal value = new BigDecimal(numberExpression.getValue());
            if (clazz.isAssignableFrom(BigDecimal.class)) {
            } else if (clazz.isAssignableFrom(byte.class) || clazz.isAssignableFrom(Byte.class)) {
                return (T) Byte.valueOf(value.byteValue());
            } else if (clazz.isAssignableFrom(short.class) || clazz.isAssignableFrom(Short.class)) {
                return (T) Short.valueOf(value.shortValue());
            } else if (clazz.isAssignableFrom(int.class) || clazz.isAssignableFrom(Integer.class)) {
                return (T) Integer.valueOf(value.intValue());
            } else if (clazz.isAssignableFrom(long.class) || clazz.isAssignableFrom(Long.class)) {
                return (T) Long.valueOf(value.longValue());
            } else if (clazz.isAssignableFrom(float.class) || clazz.isAssignableFrom(Float.class)) {
                return (T) Float.valueOf(value.floatValue());
            } else if (clazz.isAssignableFrom(double.class) || clazz.isAssignableFrom(Double.class)) {
                return (T) Double.valueOf(value.doubleValue());
            }
            return (T) value;
        } else if (expression instanceof StringExpression stringExpression) {
        return (T) stringExpression.getValue();
        } else if (expression instanceof BoolExpression boolExpression) {
            return (T) Boolean.valueOf(boolExpression.isValue());
        } else if (expression instanceof NullExpression nullExpression) {
            return null;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T handleClassObjectConversion(ObjectExpression objectExpression, Class<?> clazz) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            Field[] declaredFields = clazz.getDeclaredFields();
            Map<String, Expression> members = objectExpression.getMembers();
            for (Field declaredField : declaredFields) {
                Expression expression = members.get(declaredField.getName());
                if (expression == null) continue;
                Type type = declaredField.getGenericType();
                declaredField.setAccessible(true);
                declaredField.set(instance, convertToType(expression, type));
            }
            return (T) instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Expression toExpression(Object obj) {
        if (obj == null) {
            return NullExpression.CONST_NULL;
        } else if (obj instanceof Boolean objBool) {
            return objBool ? BoolExpression.CONST_TRUE : BoolExpression.CONST_FALSE;
        } else if (obj instanceof List<?> objList) {
            List<Expression> members = new ArrayList<>(objList.size());
            for (Object o : objList) {
                members.add(toExpression(o));
            }
            return new ArrayExpression(members);
        } else if (obj instanceof Map<?,?> objMap) {
            Map<String,Expression> members = new LinkedHashMap<>(objMap.size());
            for (Map.Entry<?, ?> entry : objMap.entrySet()) {
                members.put(entry.getKey().toString(), toExpression(entry.getValue()));
            }
            return new ObjectExpression(members);
        } else if (obj instanceof String objString) {
            return new StringExpression(objString);
        } else if(
            obj instanceof Byte ||
            obj instanceof Short ||
            obj instanceof Integer ||
            obj instanceof Long ||
            obj instanceof Float ||
            obj instanceof Double ||
            obj instanceof BigDecimal
        ) {
            return new NumberExpression(obj.toString());
        } else {
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            Map<String,Expression> members = new LinkedHashMap<>(declaredFields.length);
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                try {
                    Expression value = toExpression(declaredField.get(obj));
                    members.put(declaredField.getName(),value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return new ObjectExpression(members);
        }
    }

}
