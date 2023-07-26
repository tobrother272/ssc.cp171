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

/**
 * type :
 * 0 - String only
 * 1 - String file 
 * 2 - String folder 
 * 
 * @author PC
 */


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SettingField {
     String pre_name();
     String type();
     boolean has_view() default false;
     String label() default "";
     String place_holder() default "";
}
