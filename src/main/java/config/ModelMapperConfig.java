package config;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.spi.MatchingStrategy;

@Getter
public class ModelMapperConfig {
    private static ModelMapper mapper =  new ModelMapper();
    public static ModelMapper getMapper(){
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setAmbiguityIgnored(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        return mapper;
    }

}
