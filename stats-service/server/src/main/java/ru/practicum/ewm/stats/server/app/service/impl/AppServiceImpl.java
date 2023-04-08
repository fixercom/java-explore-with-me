package ru.practicum.ewm.stats.server.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.server.app.model.App;
import ru.practicum.ewm.stats.server.app.repository.AppRepository;
import ru.practicum.ewm.stats.server.app.service.AppService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppServiceImpl implements AppService {

    private final AppRepository appRepository;

    @Override
    public Optional<App> findAppByName(String name) {
        return appRepository.findByName(name);
    }

    @Override
    @Transactional
    public App createApp(String name) {
        App app = App.builder().name(name).build();
        return appRepository.save(app);
    }

}
