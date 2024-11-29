package com.ouitrips.app.services.places;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class TSPService {

public List<Integer> findMinDistanceRoute(List<List<Double>> distanceMatrix) {
    int numberOfCities = distanceMatrix.size();
    boolean[] visited = new boolean[numberOfCities];
    List<Integer> routeList = new ArrayList<>(); // Change to a List
    int currentCity = 0; // Start from the first city (0)
    visited[0] = true;
    routeList.add(currentCity); // Add the starting city

    for (int i = 1; i < numberOfCities; i++) {
        int nextCity = -1;
        double minDistance = Double.MAX_VALUE;

        // Find the nearest unvisited city
        for (int j = 0; j < numberOfCities; j++) {
            if (!visited[j] && distanceMatrix.get(currentCity).get(j) < minDistance) {
                minDistance = distanceMatrix.get(currentCity).get(j);
                nextCity = j;
            }
        }

        // Mark the found city as visited
        visited[nextCity] = true;
        routeList.add(nextCity); // Add to the route
        currentCity = nextCity;
    }


    return routeList;
}
}
