package zerobase.weather.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zerobase.weather.domain.Diary;

@NoArgsConstructor
@Getter
public class ResponseDiaryDto {
    private Integer id;
    private String weather;
    private String icon;
    private double temperature;
    private String text;
    private LocalDate date;

    @Builder
    public ResponseDiaryDto(Integer id, String weather, String icon, double temperature,
        String text, LocalDate date) {
        this.id = id;
        this.weather = weather;
        this.icon = icon;
        this.temperature = temperature;
        this.text = text;
        this.date = date;
    }

    public static ResponseDiaryDto of(Diary diary) {
        return ResponseDiaryDto.builder()
            .id(diary.getId())
            .weather(diary.getWeather())
            .icon(diary.getIcon())
            .temperature(diary.getTemperature())
            .text(diary.getText())
            .date(diary.getDate())
            .build();
    }

}
