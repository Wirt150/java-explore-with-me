package ru.practicum.ewm_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_service.entity.Location;
import ru.practicum.ewm_service.repository.LocationRepository;
import ru.practicum.ewm_service.service.LocationService;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public Location create(final Location location) {
        return locationRepository.save(location);
    }
}
