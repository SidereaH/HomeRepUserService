package ru.homerep.userservice.services;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.homerep.locationservice.LocationServiceGrpc;
import ru.homerep.locationservice.GetLocationRequest;
import ru.homerep.locationservice.GetLocationResponse;
import ru.homerep.locationservice.UpdateLocationRequest;
import ru.homerep.locationservice.UpdateLocationResponse;
import ru.homerep.userservice.models.GeoPair;
@Service
public class LocationServiceClient {

    @GrpcClient("location-service")
    private LocationServiceGrpc.LocationServiceBlockingStub locationServiceBlockingStub;

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

    public GeoPair getLocation(long userId) {
        GetLocationRequest request = GetLocationRequest.newBuilder()
                .setUserId(userId)
                .build();

        GetLocationResponse response = locationServiceBlockingStub.getLocation(request);
        return new GeoPair(response.getLat(), response.getLng());
    }
}
