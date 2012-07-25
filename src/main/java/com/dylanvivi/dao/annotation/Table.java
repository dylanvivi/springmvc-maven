package com.dylanvivi.dao.annotation;

import java.lang.annotation.*;

/**
 * 数据库表名字
 * Applies to classes only
 * @author dylan
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	public String name() default "";
}
