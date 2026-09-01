package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.example.entities.Ticket;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class UserBookingService {
    private User user;

    private List<User> userList;

    private static final String USERS_PATH = "app/src/main/java/org/example/localDb/users.json";

    private ObjectMapper objectMappper = new ObjectMapper();

    {
        objectMappper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMappper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public UserBookingService() throws IOException {
        this.userList = loadUsers();
    }

    public UserBookingService(User user) throws IOException {
        this.user = user;
        this.userList = loadUsers();
    }

    public List<User> loadUsers() throws IOException {
        File users = new File(USERS_PATH);
        return objectMappper.readValue(users, new TypeReference<List<User>>(){});

    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        if (foundUser.isPresent()) {
            this.user = foundUser.get();
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException{
        File usersFile = new File(USERS_PATH);
        objectMappper.writeValue(usersFile, userList);
    }

    public void fetchBooking(){
        user.printTicketsBooked();
    }

    public boolean cancelBooking (String ticketId) throws IOException{
        List<Ticket> tickets = user.getTicketsBooked();
        try {
            for (Ticket ticket : tickets) {
                if (ticket.getTicketId().equals(ticketId)) {
                    tickets.remove(ticket);
                    if (TrainService.cancelTicket(ticket.getTrain())) {
                        user.setTicketsBooked(tickets);
                        saveUserListToFile();
                        return true;
                    }
                    break;
                }
            }
            return false;
        }
        catch (IOException ex){
            System.out.println("Error in canceling ticket");
            return Boolean.FALSE;
        }
    }

    public List<Train> getTrains(String source, String destination){
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        }catch(IOException e){
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train){
       return train.getSeats();
    }

    public boolean bookTrainSeat(Train train, int row, int col, String source, String destination, Date travelDate){
        try {
            if (TrainService.bookTicket(train, row, col)) {
                Ticket ticket = new Ticket(UUID.randomUUID().toString().substring(0,8), user.getUserId(), source, destination, travelDate, train);
                user.getTicketsBooked().add(ticket);
                saveUserListToFile();
                return true;
            }
            return false;
        }catch (Exception e){
            System.out.println("Error in booking ticket");
            return false;
        }
    }
}
