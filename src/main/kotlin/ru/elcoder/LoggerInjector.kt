package ru.elcoder

import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.stereotype.Component

/**
 * Created by xuthus on 19.09.2018.
 */
@Component
class LoggerInjector : BeanPostProcessor {

    override fun postProcessBeforeInitialization(bean: Any?, name: String?): Any {
        if (bean != null) {
            try {
                val logField = bean.javaClass.getDeclaredField("log")
                if (logField != null) {
                    logField.isAccessible = true
                    logField.set(bean, LogFactory.getLog(bean.javaClass))
                }
            } catch (e: NoSuchFieldException) {
            }
        }
        return bean!!
    }

    override fun postProcessAfterInitialization(bean: Any?, name: String?): Any = bean!!

}