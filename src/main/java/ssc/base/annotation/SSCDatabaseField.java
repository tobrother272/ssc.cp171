/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author PC
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SSCDatabaseField {
    String sql_col_name();
    String sql_col_type();
    String view_name() default "";
    int view_type();
    int txt_index() default -1;
    boolean sql_col_key() default false;
    boolean sql_col_foren_key() default false;
    boolean not_null() default false;
    boolean txt_format() default false;
    String start_value() default "";
    int sql_col_tyle_length() default 0;
    String[] defaultValue() default {};
    
}


