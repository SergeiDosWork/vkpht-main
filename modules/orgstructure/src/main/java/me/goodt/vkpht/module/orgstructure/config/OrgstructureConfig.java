package me.goodt.vkpht.module.orgstructure.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class OrgstructureConfig {

    @Value("${appConfig.roleParsing.roleMode}")
    protected String roleMode;

    @Value("${appConfig.roleParsing.included-delimiter}")
    protected Boolean includedDelimiter;
}
