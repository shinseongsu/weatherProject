package zerobase.weather.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zerobase.weather.domain.DateWeather;

@Getter
@NoArgsConstructor
public class WeatherDto {
    private LocalDate nowDate;
    private String main;
    private String icon;
    private Double temp;

    @Builder
    public WeatherDto(LocalDate nowDate, String main, String icon, Double temp) {
        this.nowDate = nowDate;
        this.main = main;
        this.icon = icon;
        this.temp = temp;
    }

    public static WeatherDto of(String main, String icon, Double temp) {
        return WeatherDto.builder()
            .nowDate(LocalDate.now())
            .main(main)
            .icon(icon)
            .temp(temp)
            .build();
    }

    public DateWeather toEntity() {
        return DateWeather.builder()
            .date(nowDate)
            .weather(main)
            .icon(icon)
            .temperature(temp)
            .build();
    }

}
