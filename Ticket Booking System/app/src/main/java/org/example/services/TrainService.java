package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.example.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TrainService {

    private Train train;
    private static List<Train> trainList;

    public static final String TRAIN_PATH = "app/src/main/java/org/example/localDb/trains.json";
    private static ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public TrainService() throws IOException {
        this.trainList = loadTrains();
    }

    public List<Train> loadTrains() throws IOException {
        File file = new File(TRAIN_PATH);
        return objectMapper.readValue(file, new TypeReference<List<Train>>() {});
    }

    public List<Train> searchTrains(String source, String destination)
    {
        return trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
    }

    public boolean validTrain(Train train, String source, String destination){
        List<String> stationOrder = train.getStations();

        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destinationIndex = stationOrder.indexOf(destination.toLowerCase());

        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }

    private static void saveTrainListToFile() throws IOException {
        File file = new File(TRAIN_PATH);
        objectMapper.writeValue(file, trainList);
    }

    public static boolean bookTicket(Train train, int row, int col) throws IOException {
        List<List<Integer>> seats = train.getSeats();
        if(seats.size() < row || seats.get(row).size() < col || row < 0 || col < 0 || seats.get(row).get(col) == 1){
            return false;
        }
        try {
            seats.get(row).set(col, 1);
            train.setSeats(seats);
            saveTrainListToFile();
            return true;
        } catch (Exception e) {
            System.out.println("Error in booking ticket");
            return false;
        }
    }

    public static boolean cancelTicket(Train train) throws IOException{
        try {
            List<List<Integer>> seats = train.getSeats();
            for (List<Integer> seat : seats) {
                for (Integer i : seat) {
                    if (i == 1) seat.set(i, 0);
                }
            }
            train.setSeats(seats);
            saveTrainListToFile();
            return true;
        } catch (IOException e) {
            System.out.println("Error in canceling ticket");
            return false;
        }
    }
}
