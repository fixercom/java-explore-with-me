package ru.practicum.ewm.stats.server.app.service;

import ru.practicum.ewm.stats.server.app.model.App;

import java.util.Optional;

public interface AppService {

    Optional<App> findAppByName(String name);

    App createApp(String name);

}
