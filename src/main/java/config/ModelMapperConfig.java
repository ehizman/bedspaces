package config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.module.jdk8.Jdk8Module;

@Getter
public class ModelMapperConfig {
    private static final ModelMapper mapper =  new ModelMapper();
    public static ModelMapper getMapper(){
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setAmbiguityIgnored(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);
        return mapper;
    }

}
