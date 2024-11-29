package com.ouitrips.app;

import com.ouitrips.app.config.RsakeysConfig;
import com.ouitrips.app.utils.encryptutil.RSAKeysConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@EnableConfigurationProperties({RsakeysConfig.class, RSAKeysConfig.class})
class OuiTripsRESTApplicationTests {

}



