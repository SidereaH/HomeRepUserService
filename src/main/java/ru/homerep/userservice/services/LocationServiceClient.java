package ru.homerep.userservice.services;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.homerep.locationservice.LocationServiceGrpc;
import ru.homerep.locationservice.GetLocationRequest;
import ru.homerep.locationservice.GetLocationResponse;
import ru.homerep.locationservice.UpdateLocationRequest;
import ru.homerep.locationservice.UpdateLocationResponse;
import ru.homerep.locationservice.GetLocationHistoryRequest;
import ru.homerep.locationservice.GetLocationHistoryResponse;
import ru.homerep.userservice.models.GeoPair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceClient {

    @GrpcClient("location-service")
    private LocationServiceGrpc.LocationServiceBlockingStub locationServiceBlockingStub;

    /**
     * Обновляет геолокацию пользователя.
     *
     * @param userId ID пользователя.
     * @param lat    Широта.
     * @param lng    Долгота.
     * @throws RuntimeException Если обновление не удалось.
     */
    public void updateLocation(long userId, double lat, double lng) {
        UpdateLocationRequest request = UpdateLocationRequest.newBuilder()
                .setUserId(userId)
                .setLat(lat)
                .setLng(lng)
                .build();

        UpdateLocationResponse response = locationServiceBlockingStub.updateLocation(request);
        if (!response.getSuccess()) {
            throw new RuntimeException("Failed to update location");
        }
    }

    /**
     * Получает текущую геолокацию пользователя.
     *
     * @param userId ID пользователя.
     * @return Объект GeoPair с широтой и долготой.
     */
    public GeoPair getLocation(long userId) {
        GetLocationRequest request = GetLocationRequest.newBuilder()
                .setUserId(userId)
                .build();

        GetLocationResponse response = locationServiceBlockingStub.getLocation(request);
        return new GeoPair(response.getLat(), response.getLng());
    }

    /**
     * Получает историю геолокаций пользователя за указанный период.
     *
     * @param userId    ID пользователя.
     * @param startTime Начало периода (в формате RFC3339).
     * @param endTime   Конец периода (в формате RFC3339).
     * @return Список объектов GeoPair с широтой и долготой.
     */public List<GeoPair> getLocationHistory(long userId, LocalDateTime startTime, LocalDateTime endTime) {
        String startTimeStr = startTime.format(DateTimeFormatter.ISO_DATE_TIME);
        String endTimeStr = endTime.format(DateTimeFormatter.ISO_DATE_TIME);

        GetLocationHistoryRequest request = GetLocationHistoryRequest.newBuilder()
                .setUserId(userId)
                .setStartTime(startTimeStr)
                .setEndTime(endTimeStr)
                .build();

        GetLocationHistoryResponse response = locationServiceBlockingStub.getLocationHistory(request);

        return response.getLocationsList().stream()
                .map(geoPair -> new GeoPair(geoPair.getLat(), geoPair.getLng()))
                .collect(Collectors.toList());
    }

}