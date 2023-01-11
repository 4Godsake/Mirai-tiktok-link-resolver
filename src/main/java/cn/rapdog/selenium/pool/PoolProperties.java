package cn.rapdog.selenium.pool;

import lombok.Data;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.WebDriver;

@Data
public class PoolProperties {





    public static GenericObjectPoolConfig<WebDriver> getPoolConfig(){
        //todo 对象池配置
        GenericObjectPoolConfig<WebDriver> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(5);
        return poolConfig;
    }
}
