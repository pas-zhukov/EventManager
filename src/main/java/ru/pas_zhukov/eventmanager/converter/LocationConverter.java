package ru.pas_zhukov.eventmanager.converter;

import ru.pas_zhukov.eventmanager.dto.request.LocationRequestDto;
import ru.pas_zhukov.eventmanager.entity.LocationEntity;
import ru.pas_zhukov.eventmanager.model.Location;

public class LocationConverter {

    public LocationEntity toEntity(Location location) {
        return new LocationEntity(location.getId(), location.getName(), location.getAddress(), location.getCapacity(), location.getDescription());
    }

    public Location toDomain(LocationEntity locationEntity) {
        return new Location(locationEntity.getId(), locationEntity.getName(), locationEntity.getAddress(), locationEntity.getCapacity(), locationEntity.getDescription());
    }

    public Location toDomain(LocationRequestDto locationRequestDto) {
        return new Location(locationRequestDto.getId(), locationRequestDto.getName(), locationRequestDto.getAddress(), locationRequestDto.getCapacity(), locationRequestDto.getDescription());
    }

    public Location toResponseDto(Location location) {
        return new Location(location.getId(), location.getName(), location.getAddress(), location.getCapacity(), location.getDescription());
    }
}
