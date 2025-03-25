package ru.homerep.userservice.services;

import com.google.cloud.location.Location;
import lombok.extern.slf4j.Slf4j;
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
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
                .setLocation(ru.homerep.locationservice.GeoPair.newBuilder().setLat(lat).setLng(lng).build())
                .build();
        log.info("Updating location for user {} to {}, {}", userId, lat, lng);
        UpdateLocationResponse response = locationServiceBlockingStub.updateLocation(request);
        if (!response.getSuccess()) {
            log.error("Failed to update location for user {}", userId);
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
        return new GeoPair(response.getLocation().getLat(), response.getLocation().getLng());
    }

    /**
     * Получает историю геолокаций пользователя за указанный период.
     *
     * @param userId    ID пользователя.
     * @param startTime Начало периода (в формате RFC3339).
     * @param endTime   Конец периода (в формате RFC3339).
     * @return Список объектов GeoPair с широтой и долготой.
     */public GeoPair[] getLocationHistory(long userId, String startTime, String endTime) {


        GetLocationHistoryRequest request = GetLocationHistoryRequest.newBuilder()
                .setUserId(userId)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .build();

        log.info("Getting location history for user {} from {} to {}", userId, startTime, endTime);
        GetLocationHistoryResponse response = locationServiceBlockingStub.getLocationHistory(request);
        log.info("resp"+ response.toString());
        List<String> locationTimestampList = response.getTimestampsList();
        List<ru.homerep.locationservice.GeoPair> locationList = response.getLocationsList();
        GeoPair[] history = new GeoPair[locationList.size()];

        for(int i = 0; i<locationTimestampList.size();i++){
            history[i] = new GeoPair(locationList.get(i).getLat(), locationList.get(i).getLng(), OffsetDateTime.parse(locationTimestampList.get(i)) );
        }
        return history;
    }

}