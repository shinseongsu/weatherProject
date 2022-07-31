package zerobase.weather.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.api.WeatherApi;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.dto.ResponseDiaryDto;
import zerobase.weather.error.InvalidDate;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DiarySerivce {

    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;
    private final WeatherApi weatherApi;

    public ResponseDiaryDto createDate(LocalDate date, String text) throws Exception {
        log.info("started to create diary");
        DateWeather dateWeather = getDateWeather(date);

        Diary diary = diaryRepository.save(Diary.builder()
            .weather(dateWeather.getWeather())
            .icon(dateWeather.getIcon())
            .temperature(dateWeather.getTemperature())
            .text(text)
            .date(date)
            .build());

        log.info("end to create diary");
        return ResponseDiaryDto.of(diary);
    }

    private DateWeather getDateWeather(LocalDate date) throws Exception {
        List<DateWeather> dateWeatherListFromDB = dateWeatherRepository.findAllByDate(date);
        if(!dateWeatherListFromDB.isEmpty()) {
            return dateWeatherListFromDB.get(0);
        }
        return weatherApi.getWeatherFromApi().toEntity();
    }

    @Transactional(readOnly = true)
    public List<ResponseDiaryDto> readDiary(LocalDate date) {
        // TODO 나중에는 하드 코딩 말고 다른게 수정 하기
        if(date.isAfter(LocalDate.ofYearDay(3050, 1))) {
            log.error("[파라미터 에러] : 너무 미래 날짜 입니다.");
            throw new InvalidDate();
        }

        return diaryRepository.findAllByDate(date)
            .stream()
            .map(ResponseDiaryDto::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ResponseDiaryDto> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate)
            .stream()
            .map(ResponseDiaryDto::of)
            .collect(Collectors.toList());
    }

    public ResponseDiaryDto updateDiary(LocalDate date, String text) {
        Diary nowDiary = diaryRepository.getFirstByDate(date);
        nowDiary.updateText(text);
        return ResponseDiaryDto.of(nowDiary);
    }

    public void deleteDiary(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }

}
