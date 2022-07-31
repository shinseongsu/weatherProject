package zerobase.weather.schedule;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.api.WeatherApi;
import zerobase.weather.repository.DateWeatherRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DiaryScheduleService {

    private final DateWeatherRepository dateWeatherRepository;
    private final WeatherApi weatherApi;

    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDate() throws Exception {
        log.info("[Schdule started] : 날씨를 가져오는 스케줄링 입니다. Time: {}", LocalDateTime.now().toString());
        dateWeatherRepository.save(weatherApi.getWeatherFromApi().toEntity());
    }

}
