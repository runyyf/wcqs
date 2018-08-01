package com.yyf.wcqs.repository;

import com.yyf.wcqs.domain.Weather;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "weatherRepository")
public interface WeatherRepository {
    @Select("select *from weather")
    @Results({
            @Result(property = "cityName",column = "city_name"),
            @Result(property = "highTemperature",column = "high_temperature"),
            @Result(property = "lowTemperature",column = "low_temperature"),
            @Result(property = "typeDesc",column = "type_desc"),
            @Result(property = "dateCreate",column = "date_create"),
            @Result(property = "dateUpdate",column = "date_update")
    })
    List<Weather> getAll();

    @Select("select *from weather where city_name=#{cityName}")
    @Results({
            @Result(property = "cityName",column = "city_name"),
            @Result(property = "highTemperature",column = "high_temperature"),
            @Result(property = "lowTemperature",column = "low_temperature"),
            @Result(property = "typeDesc",column = "type_desc"),
            @Result(property = "dateCreate",column = "date_create"),
            @Result(property = "dateUpdate",column = "date_update")
    })
    Weather getByCityName(String cityName);

    @Update("update weather set high_temperature=#{highTemperature},low_temperature=#{lowTemperature},fx=#{fx},type_desc=#{typeDesc}," +
            "fl=#{fl},notice=#{notice},ganmao=#{ganmao},temperature=#{temperature},humidity=#{humidity},date_update=#{dateUpdate}" +
            "where id =#{id}")
    void update(Weather weather);
}
