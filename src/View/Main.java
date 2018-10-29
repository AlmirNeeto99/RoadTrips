
package View;

import Exceptions.*;
import Model.*;
import Facade.Facade;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author AlmirNeto
 */
public class Main extends Application{
    
    private static User actualUser;
    private static Facade facade;
    private static Scene main;
    private static Scene signUp;
    private static Scene userScene;
    private static Scene adminScene;
    private static Scene addCityScene;
    private static Scene addInterScene;
    private static Scene addRoadBetween2CitiesScene;
    private static Scene addRoadBetween2InterSectionsScene;
    private static Scene addRoadBetweenCityAndInterScene;
    private static Scene addPlaceToEatScene;
    private static Scene createTripScene;
    private static Scene addCityToTripScene;
    private static Scene removeCityFromTripScene;
    private static Scene tripsScene;
    private static Scene userSettingsScene;
    private static Scene startTripScene;
    private static Scene citiesInfoScene;
    private static Scene removePlaceToEatScene;
    /**
     * Main method.
     * @param args 
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException, DuplicatedDataException{
        facade = new Facade();
        try{
            facade.loadDataFile();
        } catch (IOException | ClassNotFoundException ex) {
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        drawSignUpScene(primaryStage);
        drawUserScene(primaryStage);
        drawAdminScene(primaryStage);
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(140, 140, 140, 140));
        
        main = new Scene(grid, 720, 650);
        
        Label title = new Label("RoadTrips");
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        
        Text cpf = new Text("CPF: ");
        grid.add(cpf, 0, 2);
        
        TextField cpfGetter = new TextField();
        grid.add(cpfGetter, 1, 2);
        
        Text password = new Text("Password: ");
        grid.add(password, 0, 4);
        
        PasswordField pwGetter = new PasswordField();
        grid.add(pwGetter, 1, 4);
        
        BorderPane bp = new BorderPane();
        grid.add(bp, 0, 6, 2, 1);
        Button signIn = new Button("SignIn");
        signIn.setOnAction((ActionEvent event)->{
            if(cpfGetter.getText().isEmpty() || pwGetter.getText().isEmpty()){
                alert("There are Empty Fields.");
            }
            else{
                User user = null;
                try{
                    user = facade.doLogin(cpfGetter.getText(), pwGetter.getText());
                    if(user == null){
                        alert("Password incorrect.");
                        pwGetter.setText("");
                    }
                    else if(user.getType() == UserType.USER){
                        actualUser = user;
                        cpfGetter.setText("");
                        pwGetter.setText("");
                        alertInfo("Welcome, "+actualUser.getName().toUpperCase());
                        primaryStage.setScene(userScene);
                    }
                    else{
                        actualUser = user;
                        cpfGetter.setText("");
                        pwGetter.setText("");
                        alertInfo("Welcome, "+actualUser.getName().toUpperCase());
                        primaryStage.setScene(adminScene);
                    }
                } catch (NotFoundException ex) {
                    alert("There's no User registered with this CPF.");
                    cpfGetter.setText("");
                    pwGetter.setText("");
                } catch (NoSuchAlgorithmException ex){
                } catch (UnsupportedEncodingException ex){
                }  
            }
        });
        bp.setCenter(signIn);
        
        Text register = new Text("Don't have an Account? CREATE ONE");
        register.setOnMouseEntered((MouseEvent event)->{
            register.setCursor(Cursor.HAND);
            register.setFill(Color.BLUE);
            register.setUnderline(true);
        });
        register.setOnMouseExited((MouseEvent event)->{
            register.setFill(Color.BLACK);
            register.setUnderline(false);
        });
        register.setOnMouseClicked((MouseEvent event)->{
            register.setCursor(Cursor.DEFAULT);
            cpfGetter.setText("");
            pwGetter.setText("");
            primaryStage.setScene(signUp);
        });
        BorderPane registerBP = new BorderPane();
        grid.add(registerBP, 0, 8, 2, 1);
        registerBP.setCenter(register);
        
        Text importFile = new Text("Import City and Inter File");
        importFile.setOnMouseEntered((MouseEvent event)->{
            importFile.setCursor(Cursor.HAND);
            importFile.setFill(Color.BLUE);
            importFile.setUnderline(true);
        });
        importFile.setOnMouseExited((MouseEvent event)->{
            importFile.setFill(Color.BLACK);
            importFile.setUnderline(false);
        });
        importFile.setOnMouseClicked((MouseEvent event)->{
            importFile.setCursor(Cursor.DEFAULT);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
        
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if(selectedFile != null){
                try {
                    facade.readFirstFile(selectedFile.getPath());
                    alertInfo("Imported with Success!");
                } catch (IOException ex) {
                    alert("It's Impossible to Read City File.");
                }
            }
        });
        
        BorderPane importBP = new BorderPane();
        grid.add(importBP, 0, 10, 2, 1);
        importBP.setCenter(importFile);
        
        Text importRoadsFile = new Text("Import Roads File");
        importRoadsFile.setOnMouseEntered((MouseEvent event)->{
            importRoadsFile.setCursor(Cursor.HAND);
            importRoadsFile.setFill(Color.BLUE);
            importRoadsFile.setUnderline(true);
        });
        importRoadsFile.setOnMouseExited((MouseEvent event)->{
            importRoadsFile.setFill(Color.BLACK);
            importRoadsFile.setUnderline(false);
        });
        importRoadsFile.setOnMouseClicked((MouseEvent event)->{
            importRoadsFile.setCursor(Cursor.DEFAULT);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
        
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if(selectedFile != null){
                try {
                    facade.readRoads(selectedFile.getPath());
                    alertInfo("Imported with Success!");
                } catch (IOException ex) {
                    alert("It's Impossible to Read Roads File.");
                }
            }
        });
        
        BorderPane roadFileBp = new BorderPane();
        grid.add(roadFileBp, 0, 12, 2, 1);
        roadFileBp.setCenter(importRoadsFile);
        
        
        BorderPane footerPane = new BorderPane();
        Label footer = new Label("® Developed by Jaevillen and Almir");
        footer.setOnMouseClicked((MouseEvent event)->{
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if(desktop != null && desktop.isSupported(Desktop.Action.BROWSE)){
                try {
                    desktop.browse(new URI("https://github.com/AlmirNeeto99"));
                    desktop.browse(new URI("https://github.com/JaevillenOliveira"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        footer.setFont(Font.font("Comic Sans MS",  12));
        footerPane.setCenter(footer);
        grid.add(footerPane, 0, 27, 2, 1);
        
        primaryStage.setTitle("RoadTrips");
        primaryStage.setHeight(650);
        primaryStage.setWidth(720);
        primaryStage.setResizable(false);
        
        primaryStage.setOnCloseRequest((WindowEvent event)->{
            try {
                facade.saveDataFile();
            } catch (IOException ex) {
                ex.getStackTrace();
            }
        });
        
        Image image = new Image("View/icon.jpg");
        primaryStage.getIcons().add(image);
        primaryStage.setScene(main);
        primaryStage.show();  

    }
    
    private static void drawSignUpScene(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setVgap(10);
        grid.setPadding(new Insets(60, 60, 60, 60));
        
        signUp = new Scene(grid, 720, 650);
        
        Label title = new Label("Register");
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        
        Text name = new Text("Name: ");
        grid.add(name, 0, 2);
        
        TextField nameGetter = new TextField();
        grid.add(nameGetter, 1, 2);
        
        Text cpf = new Text("CPF: ");
        grid.add(cpf, 0, 4);
        
        TextField cpfGetter = new TextField();
        grid.add(cpfGetter, 1, 4);
        
        Text email = new Text("E-Mail: ");
        grid.add(email, 0, 6);
        
        TextField emailGetter = new TextField();
        grid.add(emailGetter, 1, 6);
        
        Text password = new Text("Password: ");
        grid.add(password, 0, 8);
        
        PasswordField pwGetter = new PasswordField();
        grid.add(pwGetter, 1, 8);
        
        Text Cpassword = new Text("Confirm Password: ");
        grid.add(Cpassword, 0, 10);
        
        PasswordField CpwGetter = new PasswordField();
        grid.add(CpwGetter, 1, 10);
        
        BorderPane bpButton = new BorderPane();
        
        Button register = new Button("Register");
        register.setOnAction((ActionEvent event)->{
            if(nameGetter.getText().isEmpty() || cpfGetter.getText().isEmpty() || pwGetter.getText().isEmpty() || CpwGetter.getText().isEmpty()){
                alert("There are Empty Fields.");
            }
            else if(!pwGetter.getText().equals(CpwGetter.getText())){
                alert("Confirmation Password doesn't Match.");
                pwGetter.setText("");
                CpwGetter.setText("");
            }
            else{
                try{
                    facade.newUser(nameGetter.getText(), pwGetter.getText(), cpfGetter.getText(), emailGetter.getText());
                    alertInfo("Sucessfully Registered");
                    nameGetter.setText("");
                    cpfGetter.setText("");
                    emailGetter.setText("");
                    pwGetter.setText("");
                    CpwGetter.setText("");
                    primaryStage.setScene(main);
                }
                catch(DuplicatedDataException dde){
                    alert("There's already an User with this CPF.");
                    nameGetter.setText("");
                    cpfGetter.setText("");
                    emailGetter.setText("");
                    pwGetter.setText("");
                    CpwGetter.setText("");
                } catch (NoSuchAlgorithmException ex) { 
                } catch (UnsupportedEncodingException ex) {   
                }
            }
        });
        bpButton.setLeft(register);
        
        Button cancel = new Button("Cancel");
        cancel.setTextFill(Color.RED);
        cancel.setOnAction((ActionEvent event)->{
            nameGetter.setText("");
            cpfGetter.setText("");
            emailGetter.setText("");
            pwGetter.setText("");
            CpwGetter.setText("");
            primaryStage.setScene(main);
        });
        bpButton.setRight(cancel);
        grid.add(bpButton, 0, 12, 2, 1);
        
        BorderPane footerPane = new BorderPane();
        Label footer = new Label("® Developed by Jaevillen and Almir");
        footer.setFont(Font.font("Comic Sans MS",  12));
        footerPane.setCenter(footer);
        grid.add(footerPane, 0, 32, 2, 1);
    }
    
    private static void drawUserScene(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 0, 0, 0));
        
        userScene = new Scene(grid, 720, 650);
        
        ButtonBar btnBar = new ButtonBar();
        
        Button logOut = new Button("LogOut");
        logOut.setTextFill(Color.RED);
        logOut.setOnAction((ActionEvent event)->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Exit");
            alert.setContentText("Are you Sure?");
            alert.showAndWait();
            ButtonType btnType = alert.getResult();
            if(btnType.getButtonData() == ButtonData.OK_DONE){
                primaryStage.setScene(main);
            }
        });
        
        for(int i = 0; i < 53; i++){
            btnBar.getButtons().add(new Text());
        }
        Button settings = new Button("Settings");
        settings.setOnAction((ActionEvent event)->{
            drawUserSettingsScene(primaryStage);
            primaryStage.setScene(userSettingsScene);
        });
        btnBar.getButtons().add(settings);
        
        btnBar.getButtons().add(logOut);
        grid.add(btnBar, 0, 0, 10, 1);
         
        Label title = new Label("User Menu");
        title.setPadding(new Insets(60, 170, -40, 230));
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 1, 10, 1);
        
        BorderPane bp = new BorderPane();
        grid.add(bp, 0, 2, 10, 1);
        
        FlowPane fp = new FlowPane(Orientation.VERTICAL);
        fp.setVgap(10);
        fp.setHgap(10);
        fp.setAlignment(Pos.CENTER);
        fp.setColumnHalignment(HPos.CENTER);
        bp.setTop(fp);
        
        Button createTrip = new Button("Create Trip");
        createTrip.setMinWidth(200);
        createTrip.setOnAction((ActionEvent event)->{
            drawCreateTripScene(primaryStage);
            primaryStage.setScene(createTripScene);
        });
        
        Button addCityToTrip = new Button("Add City to Trip");
        addCityToTrip.setMinWidth(200);
        addCityToTrip.setOnAction((ActionEvent event)->{
            if(!facade.haveCities() && !facade.haveTrips(actualUser)){
                alert("There's no City registered in System and There's no Trip registered in your Account.");
            }
            else if(!facade.haveTrips(actualUser)){
                alert("There's no Trips registered in your account.");
            }
            else if(!facade.haveCities()){
                alert("There's no City registered in System.");
            }
            else{
                drawAddCityToTripScene(primaryStage);
                primaryStage.setScene(addCityToTripScene);
            }
        });
        
        Button removeCityFromTrip = new Button("Remove City From Trip");
        removeCityFromTrip.setMinWidth(200);
        removeCityFromTrip.setOnAction((ActionEvent event)->{
            if(!facade.haveTrips(actualUser)){
                alert("There's no Trips registered in your account.");
            }
            else{
                drawRemoveCityFromTripScene(primaryStage);
                primaryStage.setScene(removeCityFromTripScene);
            }
        });
        
        Button manageTrips = new Button("Manage Trips");
        manageTrips.setMinWidth(200);
        manageTrips.setOnAction((ActionEvent event)->{
            if(!facade.haveTrips(actualUser)){
                alert("There's no Trips registered in your account.");
            }
            else{
                drawTripScene(primaryStage);
                primaryStage.setScene(tripsScene);
            }
        });
        
        Button startTrip = new Button("Start Trip");
        startTrip.setMinWidth(200);
        startTrip.setOnAction((ActionEvent event)->{
            if(!facade.haveTrips(actualUser)){
                alert("There's no Trips registered in your account.");
            }
            else{
                drawStartTripScene(primaryStage);
                primaryStage.setScene(startTripScene);
            }
        });
        
        Button citiesInfo = new Button("Cities Infos");
        citiesInfo.setMinWidth(200);
        citiesInfo.setOnAction((ActionEvent event)->{
            if(!facade.haveCities()){
                alert("There's no Cities registered in System.");
            }
            else{
                drawCitiesInfoScene(primaryStage);
                primaryStage.setScene(citiesInfoScene);
            }
        });
        
        fp.getChildren().add(createTrip);
        fp.getChildren().add(addCityToTrip);
        fp.getChildren().add(removeCityFromTrip);
        fp.getChildren().add(manageTrips);
        fp.getChildren().add(startTrip);
        fp.getChildren().add(citiesInfo);
    }
    
    private static void drawAdminScene(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 0, 0, 0));
        
        adminScene = new Scene(grid, 720, 650);
        
        ButtonBar btnBar = new ButtonBar();
        Button logOut = new Button("LogOut");
        logOut.setTextFill(Color.RED);
        logOut.setOnAction((ActionEvent event)->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Exit");
            alert.setContentText("Are you Sure?");
            alert.showAndWait();
            ButtonType btnType = alert.getResult();
            if(btnType.getButtonData() == ButtonData.OK_DONE){
                primaryStage.setScene(main);
            }
        });
        
        for(int i = 0; i < 53; i++){
            btnBar.getButtons().add(new Text());
        }
        btnBar.getButtons().add(logOut);
        grid.add(btnBar, 0, 0, 10, 1);
        
        Label title = new Label("Menu Admin");
        title.setPadding(new Insets(60, 170, -40, 230));
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 1, 10, 1);
        
        BorderPane bp = new BorderPane();
        grid.add(bp, 0, 2, 10, 1);
        
        FlowPane fp = new FlowPane(Orientation.VERTICAL);
        fp.setVgap(10);
        fp.setAlignment(Pos.CENTER);
        fp.setColumnHalignment(HPos.CENTER);
        bp.setTop(fp);
        
        Button addCity = new Button("AddCity");
        addCity.setMinWidth(135);
        addCity.setOnAction((ActionEvent event)->{
            drawAddCityScene(primaryStage);
            primaryStage.setScene(addCityScene);
        });
        
        Button addInter = new Button("AddIntersection");
        addInter.setMinWidth(135);
        addInter.setOnAction((ActionEvent event)->{
            drawAddInterScene(primaryStage);
            primaryStage.setScene(addInterScene);
        });
        
        
        Button addRoad = new Button("AddRoad");
        addRoad.setMinWidth(135);
        addRoad.setOnAction((ActionEvent event)->{
            if(facade.haveCities()){
                drawAddRoadBetWeen2CitiesScene(primaryStage);
                primaryStage.setScene(addRoadBetween2CitiesScene);
            }
            else{
                alert("There's no City Registered in System.");
            }
        });
        
        
        Button addRoadBetweenTwoInter = new Button("AddRoadBwInters");
        addRoadBetweenTwoInter.setMinWidth(135);
        addRoadBetweenTwoInter.setOnAction((ActionEvent event)->{
            if(facade.hasInter()){
                drawAddRoadBetween2InterSectionsScene(primaryStage);
                primaryStage.setScene(addRoadBetween2InterSectionsScene);
            }
            else{
                alert("There's no Inter Registered in System.");
            }
        });
        
        Button addRoadBetweenCityAndInter = new Button("AddRoadBwCity&Inter");
        addRoadBetweenCityAndInter.setMinWidth(135);
        addRoadBetweenCityAndInter.setOnAction((ActionEvent event)->{
            if(facade.hasInter() && facade.haveCities()){
                drawAddRoadBetweenCityAndInterScene(primaryStage);
                primaryStage.setScene(addRoadBetweenCityAndInterScene);
            }
            else if(!facade.hasInter()){
                alert("There's no Inter Registered in System.");
            }
            else if(!facade.haveCities()){
                alert("There's no City Registered in System.");
            }
        });
        
        Button addPlaceToEat = new Button("Add Place to Eat");
        addPlaceToEat.setMinWidth(135);
        addPlaceToEat.setOnAction((ActionEvent event)->{
            if(facade.haveCities()){
                drawAddPlaceToEatScene(primaryStage);
                primaryStage.setScene(addPlaceToEatScene);
            }
            else{
                alert("There's no City Registered in System.");
            }
        });
        
        Button removePlaceToEat = new Button("Remove Place to Eat");
        removePlaceToEat.setMinWidth(135);
        removePlaceToEat.setOnAction((ActionEvent event)->{
            if(facade.haveCities()){
                drawRemoveEatPlace(primaryStage);
                primaryStage.setScene(removePlaceToEatScene);
            }
            else{
                alert("There's no City Registered in System.");
            }
        });
        
        fp.getChildren().add(addCity);
        fp.getChildren().add(addInter);
        fp.getChildren().add(addRoad);
        fp.getChildren().add(addRoadBetweenTwoInter);
        fp.getChildren().add(addRoadBetweenCityAndInter);
        fp.getChildren().add(addPlaceToEat); 
        fp.getChildren().add(removePlaceToEat);
        
    }
    
    private static void drawAddCityScene(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(60, 60, 60, 60));
        
        addCityScene = new Scene(grid, 720, 650);
        
        Label title = new Label("Adding City");
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        
        Text cityName = new Text("City Name: ");
        grid.add(cityName, 0 ,2);
        
        TextField cityNameGetter = new TextField();
        grid.add(cityNameGetter, 1, 2);
        
        Text cityCode = new Text("City Code: ");
        grid.add(cityCode, 0, 4);
        
        TextField cityCodeGetter = new TextField();
        grid.add(cityCodeGetter, 1, 4);
        
        Text longitude = new Text("Longitude: ");
        grid.add(longitude, 0, 6);
        
        TextField longitudeGetter = new TextField();
        grid.add(longitudeGetter, 1, 6);
        
        Text latitude = new Text("Latitude: ");
        grid.add(latitude, 0, 8);
        
        TextField latitudeGetter = new TextField();
        grid.add(latitudeGetter, 1, 8);
        
        Text population = new Text("Population: ");
        grid.add(population, 0, 10);
        
        TextField popGetter = new TextField();
        grid.add(popGetter, 1, 10);
        
        BorderPane btnPane = new BorderPane();
        grid.add(btnPane, 0, 12, 2, 1);
        
        Button add = new Button("Add");
        add.setOnAction((ActionEvent event)->{
            boolean isOk = true;
            if(cityNameGetter.getText().isEmpty() || cityCodeGetter.getText().isEmpty() || latitudeGetter.getText().isEmpty() || longitudeGetter.getText().isEmpty()){
                alert("There are Empty Fields.");
            }
            else{
                int code = 0;
                try{
                    code = Integer.parseInt(cityCodeGetter.getText());
                }
                catch(NumberFormatException nfx){
                    alert("Type a Number in \"City Code\" Field.");
                    cityCodeGetter.setText("");
                    isOk = false;
                }
                
                double latitudeDouble = 0 ;
                try{
                    latitudeDouble = Double.parseDouble(latitudeGetter.getText());
                }
                catch(NumberFormatException nfx){
                    alert("Type a Number in \"Latitude\" Field like this: X.XXX");
                    latitudeGetter.setText("");
                    isOk = false;
                }
                
                double longitudeDouble = 0;
                try{
                    longitudeDouble = Double.parseDouble(longitudeGetter.getText());
                }
                catch(NumberFormatException nfx){
                    alert("Type a Number in \"Longitude\" Field like this: X.XXX");
                    longitudeGetter.setText("");
                    isOk = false;
                }
                
                double popDouble = 0;
                try{
                    popDouble = Double.parseDouble(popGetter.getText());
                }
                catch(NumberFormatException nfx){
                    alert("Type a Number in \"Population\" Field like this: X.XXX");
                    popGetter.setText("");
                    isOk = false;
                }
                if(isOk == true){
                    try{
                        facade.addCity(cityNameGetter.getText(), latitudeDouble, longitudeDouble, code, popGetter.getText());
                        alertInfo("Successfully Registered!");
                        cityNameGetter.setText("");
                        cityCodeGetter.setText("");
                        latitudeGetter.setText("");
                        longitudeGetter.setText("");
                        popGetter.setText("");
                        primaryStage.setScene(adminScene);
                    } catch (DuplicateEntryException ex) {
                        alert("There's already a City with this Code.");
                        cityNameGetter.setText("");
                        cityCodeGetter.setText("");
                        latitudeGetter.setText("");
                        longitudeGetter.setText("");
                        popGetter.setText("");
                    }
                }
            } 
        });
        btnPane.setLeft(add);
        
        Button cancel = new Button("Cancel");
        cancel.setTextFill(Color.RED);
        cancel.setOnAction((ActionEvent event)->{
            cityNameGetter.setText("");
            cityCodeGetter.setText("");
            popGetter.setText("");
            longitudeGetter.setText("");
            latitudeGetter.setText("");
            primaryStage.setScene(adminScene);
        });
        btnPane.setRight(cancel);
    }
    
    private static void drawAddInterScene(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(60, 60, 60, 60));
        
        addInterScene = new Scene(grid, 720, 650);
        
        Label title = new Label("Adding Inter");
        title.setPadding(new Insets(0, 0, 60, 0));
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        
        Text interType = new Text("InterType: ");
        grid.add(interType, 0 ,2);
        
        ComboBox typeBox = new ComboBox();
        typeBox.setPromptText("InterType");
        grid.add(typeBox, 1, 2);
        
        typeBox.getItems().add(TypeIntersection.CROSSING);
        typeBox.getItems().add(TypeIntersection.SEMAPHORE);
        typeBox.getItems().add(TypeIntersection.ROTULA);
        
        Text interCode = new Text("Inter Code: ");
        grid.add(interCode, 0, 4);
        
        TextField interCodeGetter = new TextField();
        grid.add(interCodeGetter, 1, 4);
        
        Text longitude = new Text("Longitude: ");
        grid.add(longitude, 0, 6);
        
        TextField longitudeGetter = new TextField();
        grid.add(longitudeGetter, 1, 6);
        
        Text latitude = new Text("Latitude: ");
        grid.add(latitude, 0, 8);
        
        TextField latitudeGetter = new TextField();
        grid.add(latitudeGetter, 1, 8);
        
        BorderPane btnBp = new BorderPane();
        grid.add(btnBp, 0, 12, 2, 1);
        
        Button add = new Button("Add");
        add.setOnAction((ActionEvent event)->{
            boolean isOk = true;
            if(interCodeGetter.getText().isEmpty() || longitudeGetter.getText().isEmpty() || latitudeGetter.getText().isEmpty()){
                alert("There are Empty Fields.");
            }
            else if(typeBox.getSelectionModel().getSelectedItem() == null){
                alert("Select a Type in \"IntersectionType\" Field.");
            }
            else{
                TypeIntersection type = (TypeIntersection)typeBox.getSelectionModel().getSelectedItem();
                int code = 0;
                try{
                    code = Integer.parseInt(interCodeGetter.getText());
                }
                catch(NumberFormatException nfx){
                    alert("Type a Number in \"InterCode\" Field like this: X");
                    interCodeGetter.setText("");
                    isOk = false;
                }
                
                double latitudeDouble = 0 ;
                try{
                    latitudeDouble = Double.parseDouble(latitudeGetter.getText());
                }
                catch(NumberFormatException nfx){
                    alert("Type a Number in \"Latitude\" Field like this: X.XXX");
                    latitudeGetter.setText("");
                    isOk = false;
                }
                
                double longitudeDouble = 0;
                try{
                    longitudeDouble = Double.parseDouble(longitudeGetter.getText());
                }
                catch(NumberFormatException nfx){
                    alert("Type a Number in \"Longitude\" Field like this: X.XXX");
                    longitudeGetter.setText("");
                    isOk = false;
                }
                if(isOk == true){
                    try{
                    facade.addIntersection(type, latitudeDouble, longitudeDouble, code);
                    alertInfo("Intersection Added with Success.");
                    primaryStage.setScene(adminScene);
                    } catch (DuplicateEntryException ex) {
                        alert("There's already an Intersection with this Code.");
                        longitudeGetter.setText("");
                        latitudeGetter.setText("");
                        interCodeGetter.setText("");
                    }
                }
            }
        });
        
        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event)->{
            primaryStage.setScene(adminScene);
            interCodeGetter.setText("");
            latitudeGetter.setText("");
            longitude.setText("");
        });
        cancel.setTextFill(Color.RED);
        
        btnBp.setLeft(add);
        btnBp.setRight(cancel);
    }
    
    private static void drawAddRoadBetWeen2CitiesScene(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(100, 100, 100, 100));
        
        addRoadBetween2CitiesScene = new Scene(grid, 720, 650);
        
        Label title = new Label("Adding Road");
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        
        LinkedList<City> cities = null;
        
        try{
            cities = facade.getCities();
        } catch (TheresNoCityException ex) { 
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        ComboBox firstCbox = new ComboBox();
        ComboBox secondCbox = new ComboBox();

        firstCbox.setPromptText("Cities");
            
        secondCbox.setPromptText("Cities");
        for(City city : cities){
            firstCbox.getItems().add(city);
        }

        for(City city : cities){
            secondCbox.getItems().add(city);
        }
        
        Text roadSource = new Text("City that Starts: ");
        grid.add(roadSource, 0, 2);
        
        grid.add(firstCbox, 1, 2);
        
        Text roadDestiny = new Text("City  that Ends: ");
        grid.add(roadDestiny, 0, 4);    
        
        grid.add(secondCbox, 1, 4);
        
        Text roadSize = new Text("Length in KM: ");
        grid.add(roadSize, 0, 6);
        
        TextField roadSizeGetter = new TextField();
        grid.add(roadSizeGetter, 1, 6);
        
        BorderPane btnBP= new BorderPane();
        grid.add(btnBP, 0, 8, 2, 1);
        
        Button add = new Button("Add");
        add.setOnAction((ActionEvent event)->{
            boolean isOk = true;
            
            if(firstCbox.getSelectionModel().getSelectedItem() == null){
                alert("Select an City in \"City Field\".");
                isOk = false;
            }
            else if(secondCbox.getSelectionModel().getSelectedItem() == null){
                alert("Select an City in \"City Field\".");
                isOk = false;
            }
            double sizeDouble = 0;
            try{
                sizeDouble = Double.parseDouble(roadSizeGetter.getText());
            }
            catch(NumberFormatException nfx){
                isOk = false;
                alert("Type a Number in \"Road Lenght\" like this: X.XXX");
                roadSizeGetter.setText("");
            }
            if(isOk == true){
                City cityA = (City)firstCbox.getSelectionModel().getSelectedItem();
            
                City cityB = (City)secondCbox.getSelectionModel().getSelectedItem();
                try{
                    facade.addRoad(cityA, cityB, sizeDouble);
                    alertInfo("Road added Successfully.");
                    roadSizeGetter.setText("");
                    primaryStage.setScene(adminScene);
                } catch (DuplicateEntryException ex) {
                    alert("There's already a Road between these Cities.");
                } catch (AlreadyHasAdjacency ex) {
                   alert("There's already a Road between these Cities.");
                } catch (LoopIsNotAllowedException ex) {
                   alert("You can't add a Road on The Same Cities.");
                } catch (InexistentVertexException ex) {
                    //Shouldn't enter here.
                } catch (InexistentEntryException ex) {
                    //Shouldn't enter here.
                }
            }
                        
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event)->{
            primaryStage.setScene(adminScene);
            roadSizeGetter.setText("");
        });
        cancel.setTextFill(Color.RED);
        
        btnBP.setLeft(add);
        btnBP.setRight(cancel); 
        
    }
    
    private static void drawAddRoadBetween2InterSectionsScene(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(100, 100, 100, 100));
        
        addRoadBetween2InterSectionsScene = new Scene(grid, 720, 650);
        
        Label title = new Label("Adding Road");
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        
        LinkedList<Intersection> inter = null;
        
        try{
            inter = facade.getInter();
        } catch (TheresNoInterException ex) {
            //Will never enter here.
        }
        ComboBox firstCbox = new ComboBox();
        ComboBox secondCbox = new ComboBox();

        firstCbox.setPromptText("Intersection: ");
            
        secondCbox.setPromptText("Intersection");
        for(Intersection Inter : inter){
            firstCbox.getItems().add(Inter);
        }

        for(Intersection Inter : inter){
            secondCbox.getItems().add(Inter);
        }
        
        Text roadSource = new Text("Inter that Starts: ");
        grid.add(roadSource, 0, 2);
        
        grid.add(firstCbox, 1, 2);
        
        Text roadDestiny = new Text("Inter  that Ends: ");
        grid.add(roadDestiny, 0, 4);    
        
        grid.add(secondCbox, 1, 4);
        
        Text roadSize = new Text("Length in KM: ");
        grid.add(roadSize, 0, 6);
        
        TextField roadSizeGetter = new TextField();
        grid.add(roadSizeGetter, 1, 6);
        
        BorderPane btnBP= new BorderPane();
        grid.add(btnBP, 0, 8, 2, 1);
        
        Button add = new Button("Add");
        add.setOnAction((ActionEvent event)->{
            boolean isOk = true;
            if(firstCbox.getSelectionModel().getSelectedItem() == null){
                alert("Select an Inter in \"Inter Field\".");
                isOk = false;
            }
            else if(secondCbox.getSelectionModel().getSelectedItem() == null){
                alert("Select an Inter in \"Inter Field\".");
                isOk = false;
            }
            double sizeDouble = 0;
            try{
                sizeDouble = Double.parseDouble(roadSizeGetter.getText());
            }
            catch(NumberFormatException nfx){
                isOk = false;
                alert("Type a Number in \"Road Lenght\" like this: X.XXX");
                roadSizeGetter.setText("");
            }
            if(isOk == true){
                Intersection interA = (Intersection)firstCbox.getSelectionModel().getSelectedItem();
            
                Intersection interB = (Intersection)secondCbox.getSelectionModel().getSelectedItem();
                try{
                    facade.addRoad(interA, interB, sizeDouble);
                    alertInfo("Road added Successfully.");
                    roadSizeGetter.setText("");
                    primaryStage.setScene(adminScene);
                } catch (DuplicateEntryException ex) {
                    alert("There's already a Road between these Inters.");
                } catch (AlreadyHasAdjacency ex) {
                   alert("There's already a Road between these Inters.");
                } catch (LoopIsNotAllowedException ex) {
                   alert("You can't add a Road on The Same Inter.");
                } catch (InexistentVertexException ex) {
                    //Shouldn't enter here.
                }
            }
                        
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event)->{
            primaryStage.setScene(adminScene);
            roadSizeGetter.setText("");
        });
        cancel.setTextFill(Color.RED);
        
        btnBP.setLeft(add);
        btnBP.setRight(cancel); 
        
    }
    
    private static void drawAddRoadBetweenCityAndInterScene(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(100, 100, 100, 100));
        
        addRoadBetweenCityAndInterScene = new Scene(grid, 720, 650);
        
        Label title = new Label("Adding Road");
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        
        LinkedList<City> cities = null;
        try{
            cities = facade.getCities();
        } catch (TheresNoCityException ex) { 
            //Will never enter here.
        }
        
        LinkedList<Intersection> inter = null;
        try{
            inter = facade.getInter();
        } catch (TheresNoInterException ex) {
            //Will never enter here.
        }
        ComboBox firstCbox = new ComboBox();
        ComboBox secondCbox = new ComboBox();

        firstCbox.setPromptText("Cities");
            
        secondCbox.setPromptText("Intersection");
        
        for(City city : cities){
            firstCbox.getItems().add(city);
        }

        for(Intersection Inter : inter){
            secondCbox.getItems().add(Inter);
        }
        
        Text roadSource = new Text("City: ");
        grid.add(roadSource, 0, 2);
        
        grid.add(firstCbox, 1, 2);
        
        Text roadDestiny = new Text("Inter: ");
        grid.add(roadDestiny, 0, 4);    
        
        grid.add(secondCbox, 1, 4);
        
        Text roadSize = new Text("Length in KM: ");
        grid.add(roadSize, 0, 6);
        
        TextField roadSizeGetter = new TextField();
        grid.add(roadSizeGetter, 1, 6);
        
        BorderPane btnBP= new BorderPane();
        grid.add(btnBP, 0, 8, 2, 1);
        
        Button add = new Button("Add");
        add.setOnAction((ActionEvent event)->{
            boolean isOk = true;
            if(firstCbox.getSelectionModel().getSelectedItem() == null){
                alert("Select an City in \"City Field\".");
                isOk = false;
            }
            else if(secondCbox.getSelectionModel().getSelectedItem() == null){
                alert("Select an Inter in \"Inter Field\".");
                isOk = false;
            }
            double sizeDouble = 0;
            try{
                sizeDouble = Double.parseDouble(roadSizeGetter.getText());
            }
            catch(NumberFormatException nfx){
                isOk = false;
                alert("Type a Number in \"Road Lenght\" like this: X.XXX");
                roadSizeGetter.setText("");
            }
            if(isOk == true){
                City cityA = (City)firstCbox.getSelectionModel().getSelectedItem();
                Intersection interA = (Intersection)secondCbox.getSelectionModel().getSelectedItem();
                
                try{
                    facade.addRoad(interA, cityA, sizeDouble);
                    alertInfo("Road Added Successfully.");
                    roadSizeGetter.setText("");
                    primaryStage.setScene(adminScene);
                } catch (DuplicateEntryException ex) {
                    alert("There's already a Road between this City and Inter.");
                } catch (AlreadyHasAdjacency ex) {
                    alert("There's already a Road between this City and Inter.");
                } catch (InexistentVertexException ex) {
                    //Will never enter Here.
                } catch (LoopIsNotAllowedException ex) {
                    //Will never enter Here.
                }
            }
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event)->{
            primaryStage.setScene(adminScene);
            roadSizeGetter.setText("");
        });
        cancel.setTextFill(Color.RED);
        
        btnBP.setLeft(add);
        btnBP.setRight(cancel); 
    }
    
    private static void drawAddPlaceToEatScene(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(100, 100, 100, 100));
        
        addPlaceToEatScene = new Scene(grid, 720, 650);
        
        Label title = new Label("Adding PlaceToEat");
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        
        LinkedList<City> cities = null;
        try{
            cities = facade.getCities();
        } catch (TheresNoCityException ex) {
            alert("There's no City registered in System.");
            primaryStage.setScene(adminScene);
        }
        
        ComboBox firstCbox = new ComboBox();
        firstCbox.setPromptText("Cities");
        
        for(City city : cities){
            firstCbox.getItems().add(city);
        }
        
        Text city = new Text("Cities: ");
        grid.add(city, 0, 2);
        
        grid.add(firstCbox, 1, 2);
        
        Text placeName = new Text("Place Name: ");
        grid.add(placeName, 0, 4);
        
        TextField placeNameGetter = new TextField();
        grid.add(placeNameGetter, 1, 4);
        
        Text address = new Text("Adress: ");
        grid.add(address, 0, 6);
        
        TextField addressGetter = new TextField();
        grid.add(addressGetter, 1, 6);
        
        Text rate = new Text("Rate: ");
        grid.add(rate, 0, 8);
        
        ComboBox rateCB = new ComboBox();
        rateCB.setPromptText("Rating");
        
        rateCB.getItems().add(Rate.PÉSSIMO);
        rateCB.getItems().add(Rate.RUIM);
        rateCB.getItems().add(Rate.REGULAR);
        rateCB.getItems().add(Rate.BOM);
        rateCB.getItems().add(Rate.ÓTIMO);
        
        grid.add(rateCB, 1, 8);
        
        BorderPane btnBp = new BorderPane();
        grid.add(btnBp, 0, 10, 2, 1);
        
        Button add = new Button("Add");
        add.setOnAction((ActionEvent event)->{
            
            boolean isOk = true;
            
            if(placeNameGetter.getText().isEmpty() || addressGetter.getText().isEmpty()){
                alert("There's Empty Fields");
                placeNameGetter.setText("");
                addressGetter.setText("");
            }
            else{
                if(firstCbox.getSelectionModel().getSelectedItem() == null){
                    alert("Please, select a City on Selector Field.");
                    isOk = false;
                }
                
                if(rateCB.getSelectionModel().getSelectedItem() == null){
                    alert("Please, select a Rate on Selector Field.");
                    isOk = false;
                }
                
                if(isOk == true){
                    City cityA = (City)firstCbox.getSelectionModel().getSelectedItem();
                    Rate rateCity = (Rate)rateCB.getSelectionModel().getSelectedItem();
                    try{
                        facade.addEatPoint(cityA.getCode(), placeNameGetter.getText(), addressGetter.getText(), rateCity);
                        alertInfo("Place added Successfully.");
                        placeNameGetter.setText("");
                        addressGetter.setText("");
                        primaryStage.setScene(adminScene);
                    } catch (InexistentEntryException ex) {
                        //Shouldn't enter here.
                    }
                }
            }
        });
        btnBp.setLeft(add);
        
        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event)->{
            placeNameGetter.setText("");
            addressGetter.setText("");
            primaryStage.setScene(adminScene);
        });
        cancel.setTextFill(Color.RED);
        btnBp.setRight(cancel);
    }
    
    private static void drawCreateTripScene(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(100, 100, 100, 100));
        
        createTripScene = new Scene(grid, 720, 650);
        
        Label title = new Label("Creating Trip");
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        
        Text name = new Text("Trip Name: ");
        grid.add(name, 0, 4);
        
        TextField nameGetter = new TextField();
        grid.add(nameGetter, 1, 4);
        
        Text initialDate = new Text("Initial Date: ");
        grid.add(initialDate, 0, 6);
        
        DatePicker date = new DatePicker();
        grid.add(date, 1, 6);
        
        BorderPane btnBp = new BorderPane();
        grid.add(btnBp, 0, 8, 2, 1);
        
        Button create = new Button("Create");
        create.setOnAction((ActionEvent event)->{
            if(nameGetter.getText().isEmpty() || date.getValue() == null){
                alert("There are Empty Fields.");
            }
            else{
                try{
                    LocalDate localDate = date.getValue();
                    int day = localDate.getDayOfMonth();
                    int month = localDate.getMonthValue();
                    int year = localDate.getYear();
                    
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    facade.startTrip(actualUser.getCpf(), nameGetter.getText(), calendar);
                    alertInfo("Trip Created with Sucess.");
                    primaryStage.setScene(userScene);
                } catch (NotFoundException ex) {
                    //Shouldn't enter here.
                } catch (DuplicateEntryException ex) {
                    alert("There's already a trip with this name.");
                    nameGetter.setText("");
                }
            }
        });
        create.setMinWidth(100);
        btnBp.setLeft(create);
        
        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event)->{
            nameGetter.setText("");
            primaryStage.setScene(userScene);
        });
        cancel.setTextFill(Color.RED);
        cancel.setMinWidth(100);
        
        btnBp.setRight(cancel);
    }
    
    private static void drawAddCityToTripScene(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(60, 60, 60, 60));
        
        addCityToTripScene = new Scene(grid, 720, 650);
        
        Label title = new Label("Add City to Trip");
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        
        LinkedList<Trip> userTrips = null;
        
        userTrips = facade.getUserTrips(actualUser);

        ComboBox tripsBox = new ComboBox();
        tripsBox.setPromptText("Trips");
        
        for(Trip trip : userTrips){
            tripsBox.getItems().add(trip.getName());
        }
        
        Text trips = new Text("Your Trips: ");
        grid.add(trips, 0, 2);
        
        grid.add(tripsBox, 1, 2);
        
        LinkedList<City> cities = null;
        
        try{
            cities = facade.getCities();
        } catch (TheresNoCityException ex) {
            //Will never enter here.
        }
        
        ComboBox citiesBox = new ComboBox();
        citiesBox.setPromptText("Cities");
        
        for(City city : cities){
            citiesBox.getItems().add(city.getCode()+"-"+city.getName());
        }
        
        Text citiesText = new Text("Cities: ");
        grid.add(citiesText, 0 , 4);
        grid.add(citiesBox, 1, 4);
        
        Text inDateText = new Text("Date in: ");
        grid.add(inDateText, 0, 6);
        
        DatePicker inDate = new DatePicker();
        grid.add(inDate, 1 ,6);
        
        Text outDateText = new Text("Date out: ");
        grid.add(outDateText, 0, 8);
        
        DatePicker outDate = new DatePicker();
        grid.add(outDate, 1, 8);
        
        Button add = new Button("Add");
        add.setOnAction((ActionEvent event)->{
            boolean isOk = true;
            if(tripsBox.getSelectionModel().getSelectedItem() == null){
                alert("Select a Trip in \"Trip Box\".");
                isOk = false;
            }
            else if(citiesBox.getSelectionModel().getSelectedItem() == null){
                alert("Select a City in \"City Box\".");
                isOk = false;
            }
            else if(inDate.getValue() == null){
                alert("Select a Date in \"Date Box\".");
                isOk = false;
            }
            else if(outDate.getValue() == null){
                alert("Select a Date in \"Date Box\".");
                isOk = false;
            }
            if(isOk == true){
                String tripName = (String)tripsBox.getSelectionModel().getSelectedItem();
                String fullCity = (String)citiesBox.getSelectionModel().getSelectedItem();
                String newCityNameA = "";
                int cityCode = 0;
                    for(int i = 0; fullCity.charAt(i) != '-'; i++){
                        newCityNameA += fullCity.charAt(i);
                    }
                    try{
                        cityCode = Integer.parseInt(newCityNameA);
                    }
                    catch(NumberFormatException nfx){
                        //Shouldn't enter here.
                    }
                LocalDate inDateGetter = inDate.getValue();
                int inDay = inDateGetter.getDayOfMonth();
                int inMonth = inDateGetter.getMonthValue();
                int inYear = inDateGetter.getYear();
                
                LocalDate outDateGetter = outDate.getValue();
                int outDay = outDateGetter.getDayOfMonth();
                int outMonth = outDateGetter.getMonthValue();
                int outYear = outDateGetter.getYear();
                
                Calendar in = Calendar.getInstance();
                in.set(inYear, inMonth, inDay);
                
                Calendar out = Calendar.getInstance();
                out.set(outYear, outMonth, outDay);
                
                try{
                    facade.insertCityInTrip(actualUser.getCpf(), tripName, in, out, cityCode);
                    alertInfo("City added with Success.");
                    primaryStage.setScene(userScene);
                } catch (InexistentEntryException ex) {
                    //Will never enter here.
                } catch (NotFoundException ex) {
                    //Will never enter here.
                }
                
            }
        });
        
        Button cancel = new Button("Cancel");
        cancel.setTextFill(Color.RED);
        cancel.setOnAction((ActionEvent event)->{
            primaryStage.setScene(userScene);
        });
        
        BorderPane btnBp = new BorderPane();
        grid.add(btnBp, 0, 10, 2, 1);
        
        btnBp.setLeft(add);
        btnBp.setRight(cancel);
        
    }
    
    private static void drawRemoveCityFromTripScene(Stage primaryStage){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(60, 60, 60, 60));
        
        removeCityFromTripScene = new Scene(grid, 720, 650);
        
        Label title = new Label("Add City to Trip");
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        
        LinkedList<Trip> userTrips;
        
        userTrips = facade.getUserTrips(actualUser);
        ComboBox tripsBox = new ComboBox();
        tripsBox.setPromptText("Trips");
        if(userTrips.size() > 0){
            for(Trip trip : userTrips){
                tripsBox.getItems().add(trip.getName());
            }
        }
        Text trips = new Text("Your Trips: ");
        grid.add(trips, 0, 2);
        
        grid.add(tripsBox, 1, 2);
        
        ComboBox cityBox = new ComboBox();
        cityBox.setPromptText("Cities");
        
        Button select = new Button("Select");
        grid.add(select, 2, 2);
        
        Text cities = new Text("Cities: ");
        grid.add(cities, 0, 4);
        cities.setVisible(false);
        
        grid.add(cityBox, 1, 4);
        cityBox.setVisible(false);
        
        BorderPane btnBp = new BorderPane();
        grid.add(btnBp, 0 , 6, 2, 1);
        
        Button remove = new Button("Remove");
        remove.setOnAction((ActionEvent event)->{
            if(tripsBox.getSelectionModel().getSelectedItem() == null){
                alert("Select a Trip in \"Trip Box\" Field.");
            }
            else if(cityBox.getSelectionModel().getSelectedItem() == null){
                alert("Select a City in \"City Box\" Field.");
            }
            else{
            String name = (String) tripsBox.getSelectionModel().getSelectedItem();
            String cityName = (String)cityBox.getSelectionModel().getSelectedItem();
            
            String newCityNameA = "";
            int cityCode = 0;
                for(int i = 0; cityName.charAt(i) != '-'; i++){
                    newCityNameA += cityName.charAt(i);
                }
                try{
                    cityCode = Integer.parseInt(newCityNameA);
                }
                catch(NumberFormatException nfx){
                    //Shouldn't enter here.
                }
            
            Trip test = new Trip(name);
            int position = userTrips.indexOf(test);
            Trip trip = userTrips.get(position);
            
            try{
                facade.removeCityFromTrip(trip, cityCode);
                alertInfo("City removed with Success.");
                primaryStage.setScene(userScene);
            }   catch (InexistentEntryException ex) {
                    //Will never enter here.
            }

            }
        });
        
        Button cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event)->{
            primaryStage.setScene(userScene);
        });
        cancel.setTextFill(Color.RED);
        
        btnBp.setLeft(remove);
        btnBp.setRight(cancel);
        
        btnBp.setVisible(false);
        
        select.setOnAction((ActionEvent event)->{
            String name = (String) tripsBox.getSelectionModel().getSelectedItem();
            
            Trip test = new Trip(name);
            int position = userTrips.indexOf(test);
            Trip trip = userTrips.get(position);
            
            ArrayList<PitStop> spots = trip.getSpots();
            
            for(PitStop ps : spots){
                cityBox.getItems().add(ps.getCity().getCode()+"-"+ps.getCity().getName());
            }
            
            btnBp.setVisible(true);
            cityBox.setVisible(true);
            cities.setVisible(true);
        });
    }
    
    private static void drawTripScene(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(60, 60, 60, 60));
        
        tripsScene = new Scene(grid, 720, 650);
        
        Label title = new Label("Trips");
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setPadding(new Insets(0, 0, 0, 230));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        
        LinkedList<Trip> userTrips;
        
        userTrips = facade.getUserTrips(actualUser);
        ComboBox tripsBox = new ComboBox();
        tripsBox.setTranslateX(200);
        tripsBox.setPromptText("Trips");
        if(userTrips.size() > 0){
            for(Trip trip : userTrips){
                tripsBox.getItems().add(trip.getName());
            }
        }
        Text trips = new Text("Your Trips: ");
        trips.setTranslateX(200);
        grid.add(trips, 0, 2);
        
        grid.add(tripsBox, 1, 2);
        
        Button select = new Button("Select");
        grid.add(select, 2, 2);
        
        BorderPane bp = new BorderPane();
        grid.add(bp, 0, 4, 2, 1);
        
        ScrollPane sp = new ScrollPane();
        sp.setMinWidth(600);
        
        ListView viewTrips = new ListView();
        viewTrips.setMinWidth(600);
        viewTrips.setOrientation(Orientation.VERTICAL);
        
        bp.setCenter(sp);
        sp.setContent(viewTrips);
        sp.setVisible(false);
        viewTrips.setVisible(false);
        
        select.setOnAction((ActionEvent event)->{
            if(tripsBox.getSelectionModel().getSelectedItem() == null){
                alert("Select a Trip in \"Trip Box\" Field.");
            }
            else{
                viewTrips.getItems().clear();
                String name = (String) tripsBox.getSelectionModel().getSelectedItem();
            
                Trip test = new Trip(name);
                int position = userTrips.indexOf(test);
                Trip trip = userTrips.get(position);
                
                ArrayList<PitStop> ps = trip.getSpots();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                for(PitStop psA : ps){
                    String cell = "-> City: "+psA.getCity() + " " +"-> Arrive: "+ df.format(psA.getTimeIn().getTime()) + " " +"-> Departure: "+df.format(psA.getTimeOut().getTime());
                    viewTrips.getItems().add(cell);
                }
                viewTrips.setVisible(true);
                sp.setVisible(true);
            }
        });
        
        Button back = new Button("Back");
        back.setTextFill(Color.RED);
        back.setOnAction((ActionEvent event)->{
            primaryStage.setScene(userScene);
        });
        back.setTranslateX(275);
        bp.setBottom(back);
    }
    
    private static void drawUserSettingsScene(Stage primaryStage){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(60, 60, 60, 60));
        
        userSettingsScene = new Scene(grid, 720, 650);
        
        Label title = new Label("Settings");
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        
        Text oldPw = new Text("Old Password: ");
        grid.add(oldPw, 0, 2);
        
        PasswordField oldPwGetter = new PasswordField();
        grid.add(oldPwGetter, 1, 2);
        
        Text newPw = new Text("New Password: ");
        grid.add(newPw, 0, 4);
        
        PasswordField newPwGetter = new PasswordField();
        grid.add(newPwGetter, 1, 4);
        
        Text CnewPw = new Text("New Password: ");
        grid.add(CnewPw, 0, 6);
        
        PasswordField CnewPwGetter = new PasswordField();
        grid.add(CnewPwGetter, 1, 6);
        
        Button apply = new Button("Apply");
        apply.setOnAction((ActionEvent event)->{
            if(oldPwGetter.getText().isEmpty() || newPwGetter.getText().isEmpty() || CnewPwGetter.getText().isEmpty()){
                alert("There are Empty Field");
                oldPwGetter.setText("");
                newPwGetter.setText("");
                CnewPwGetter.setText("");
            }
            else if(!newPwGetter.getText().equals(CnewPwGetter.getText())){
                alert("Confirm Password Doesn't match.");
                newPwGetter.setText("");
                CnewPwGetter.setText("");
            }
            else{
                boolean changed = false;
                try{
                    changed = facade.changeUserPassword(actualUser, oldPwGetter.getText(), newPwGetter.getText());
                } catch (NoSuchAlgorithmException ex) {
                } catch (UnsupportedEncodingException ex) {
                }
                if(changed == false){
                    alert("Your Old Password is Wrong.");
                    oldPwGetter.setText("");
                    newPwGetter.setText("");
                    CnewPwGetter.setText("");
                }
                else{
                    alertInfo("Password changed successfully.");
                    oldPwGetter.setText("");
                    newPwGetter.setText("");
                    CnewPwGetter.setText("");
                }
            }
        
        });
        apply.setTextFill(Color.GREEN);
        grid.add(apply, 0, 8);
        
        Button delete = new Button("Delete Account");
        delete.setOnAction((ActionEvent event)->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Delete Account?");
            alert.setContentText("Are you Sure?");
            alert.showAndWait();
            ButtonType type = alert.getResult();
            
            if(type.getButtonData() == ButtonData.OK_DONE){
                facade.removeUser(actualUser);
                actualUser = null;
                alert("Account Deleted.");
                primaryStage.setScene(main);
                oldPwGetter.setText("");
                newPwGetter.setText("");
                CnewPwGetter.setText("");
            }
        });
        delete.setTextFill(Color.RED);
        grid.add(delete, 1, 8);
        
        Button back = new Button("Back");
        back.setOnAction((ActionEvent event)->{
            oldPwGetter.setText("");
            newPwGetter.setText("");
            CnewPwGetter.setText("");
            primaryStage.setScene(userScene);
        });
        grid.add(back, 2, 8);
        
        
    }
    
    private static void drawStartTripScene(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(60, 60, 60, 60));
        
        startTripScene = new Scene(grid, 720, 650);
        
        Label title = new Label("Start Trip");
        title.setPadding(new Insets(0, 0, 0, 180));
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        
        Text trips = new Text("Your Trips: ");
        trips.setTranslateX(200);
        grid.add(trips, 0, 2);
        
        LinkedList<Trip> userTrips;
        
        userTrips = facade.getUserTrips(actualUser);
        ComboBox tripsBox = new ComboBox();
        tripsBox.setTranslateX(200);
        tripsBox.setPromptText("Trips");
        if(userTrips.size() > 0){
            for(Trip trip : userTrips){
                tripsBox.getItems().add(trip.getName());
            }
        }
        grid.add(tripsBox, 1, 2);
        Button select = new Button("Select");
        grid.add(select, 2, 2);
        
        BorderPane bp = new BorderPane();
        grid.add(bp, 0, 4, 2, 1);
        
        ScrollPane sp = new ScrollPane();
        sp.setMinWidth(600);
        
        ListView viewTrips = new ListView();
        viewTrips.setMinWidth(600);
        viewTrips.setOrientation(Orientation.VERTICAL);
        
        bp.setCenter(sp);
        sp.setContent(viewTrips);
        sp.setVisible(false);
        viewTrips.setVisible(false);
        
        select.setOnAction((ActionEvent event)->{
            if(tripsBox.getSelectionModel().getSelectedItem() == null){
                alert("Select a Trip in \"Trip Box\" Field.");
            }
            else{
                viewTrips.getItems().clear();
                String name = (String) tripsBox.getSelectionModel().getSelectedItem();
            
                Trip test = new Trip(name);
                int position = userTrips.indexOf(test);
                Trip trip = userTrips.get(position);
                
                ArrayList<PitStop> ps = trip.getSpots();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Iterator it;
                try{
                    it = facade.shortestPath(actualUser.getCpf(), name);
                    while(it.hasNext()){
                        EntryDjikstra entry = (EntryDjikstra) it.next();
                        viewTrips.getItems().add(((City)((Vertex) entry.getCur()).getVertex()).getName());
                    }
                } catch (NotFoundException ex) {
                    //Will never enter here.
                } catch (InexistentEntryException ex) {
                    //Will never enter here.
                } catch (DuplicateEntryException ex) {
                    //Will never enter here.
                } catch (InsufficientSpotsException ex) {
                    alert("There too few spots to Make an Way.");
                    primaryStage.setScene(userScene);
                } catch (TheresNoEntryException ex) {
                    //Will never enter here.
                } catch (NoWaysException ex) {
                    alert("There's no Way between some Cities.");
                    primaryStage.setScene(userScene);
                }
                viewTrips.setVisible(true);
                sp.setVisible(true);
            }
        });
        
        Button back = new Button("Back");
        back.setTextFill(Color.RED);
        back.setOnAction((ActionEvent event)->{
            primaryStage.setScene(userScene);
        });
        back.setTranslateX(275);
        bp.setBottom(back); 
    }
    
    private static void drawCitiesInfoScene(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(60, 60, 60, 60));
        
        citiesInfoScene = new Scene(grid, 720, 650);
        
        Label title = new Label("Cities Info");
        title.setPadding(new Insets(0, 0, 0, 60));
        title.setFont(Font.font("Comic Sans MS",  50));
        title.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(title, 0, 0, 2, 1);
        LinkedList<City> cities = null;
        
        try {
            cities = facade.getCities();
        } catch (TheresNoCityException ex) {
            //Will never enter here.
        }
        
        Text citiesText = new Text("Cities: ");
        grid.add(citiesText, 0, 2);
        
        ComboBox citiesBox = new ComboBox();
        citiesBox.setPromptText("Cities");
        for(City city : cities){
            citiesBox.getItems().add(city);
        }
        grid.add(citiesBox, 1, 2);
        
        Text cityNameText = new Text("Name: ");
        grid.add(cityNameText, 0, 5);
        
        Text cityName = new Text();
        grid.add(cityName, 1, 5);
        
        Text cityCodeText = new Text("Code: ");
        grid.add(cityCodeText, 0, 7);
        
        Text cityCode = new Text();
        grid.add(cityCode, 1, 7);
        
        Text popText = new Text("Population: ");
        grid.add(popText, 0, 9);
        
        Text pop = new Text();
        grid.add(pop, 1, 9);
        
        Text latText = new Text("Latitude: ");
        grid.add(latText, 0, 11);
        
        Text latitude = new Text();
        grid.add(latitude, 1, 11);
        
        Text longiText = new Text("Longitude: ");
        grid.add(longiText, 0, 13);
        
        Text longitude = new Text();
        grid.add(longitude, 1, 13);
        
        ListView list = new ListView();
        grid.add(list, 0, 15, 2, 1);
        
        cityNameText.setVisible(false);
        cityCodeText.setVisible(false);
        popText.setVisible(false);
        latText.setVisible(false);
        longiText.setVisible(false);
        list.setVisible(false);
        
        Button ok = new Button("Ok");
        ok.setOnAction((ActionEvent event)->{
            if(citiesBox.getSelectionModel().getSelectedItem() == null){
                alert("Select a City in \"City Box\".");
            }
            else{
                City city = (City) citiesBox.getSelectionModel().getSelectedItem();
                if(city.getPlaceEat() == null){
                    list.getItems().clear();
                    list.getItems().add("THERE'S NO PLACE TO EAT AT THIS CITY.");
                }
                else{
                    list.getItems().clear();
                    Iterator it = city.getPlaceEat().iterator();
                    while(it.hasNext()){
                        EatPoint eat = (EatPoint)it.next();
                        list.getItems().add("-> Name: "+eat.getName()+"  -> Address: "+eat.getAdress()+"  -> Rating: "+eat.getRate());
                    }
                }
                
                cityName.setText("");
                cityName.setText(city.getName());
                
                cityCode.setText("");
                String code = ""+city.getCode();
                cityCode.setText(code);
                
                pop.setText("");
                String population = ""+city.getPopulation();
                pop.setText(population);
                
                latitude.setText("");
                String lat = ""+city.getLatitude();
                latitude.setText(lat);

                longitude.setText("");
                String longi = ""+city.getLongitude();
                longitude.setText(longi);
                
                
                cityNameText.setVisible(true);
                cityCodeText.setVisible(true);
                popText.setVisible(true);
                latText.setVisible(true);
                longiText.setVisible(true);
                list.setVisible(true);
            }
        });
        grid.add(ok, 3, 2);
        
        Button back = new Button("Back");
        back.setTextFill(Color.RED);
        back.setOnAction((ActionEvent event)->{
            primaryStage.setScene(userScene);
        });
        
        grid.add(back, 4, 2);
    }
    
    private static void drawRemoveEatPlace(Stage primaryStage){
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(60, 60, 60, 60));
        
        removePlaceToEatScene = new Scene(grid, 720, 650);
        
        
        Label removePlaceToEat = new Label("RemoveEatPlace");
        removePlaceToEat.setFont(Font.font("Comic Sans MS",  30));
        removePlaceToEat.setTextFill(Color.MEDIUMTURQUOISE);
        grid.add(removePlaceToEat, 0, 0, 3, 1);
        
        Text selectCity = new Text("Select a City: ");
        grid.add(selectCity, 0, 2);
        
        ComboBox selectCityBox = new ComboBox();
        selectCityBox.setPromptText("Cities");
        
        if(!facade.haveCities()){
            selectCityBox.getItems().clear();
            selectCityBox.getItems().add("There's no Cities.");
        }
        else{
            selectCityBox.getItems().clear();
            LinkedList<City> cities = null;
            try {
                cities = facade.getCities();
            } catch (TheresNoCityException ex) {
            }
            for(City city : cities){
                selectCityBox.getItems().add(city);
            }
        }
        
        grid.add(selectCityBox, 1, 2);
        
        Text selectEatPlace = new Text("Select a Place: ");
        grid.add(selectEatPlace, 0, 4);
        
        ComboBox selectEatPlaceBox = new ComboBox();
        selectEatPlaceBox.setPromptText("Places");
        
        selectEatPlaceBox.getItems().clear();
        grid.add(selectEatPlaceBox, 1, 4);
        
        selectEatPlace.setVisible(false);
        selectEatPlaceBox.setVisible(false);
        
        Button removeEatPlaceB = new Button("Remove");
        removeEatPlaceB.setTextFill(Color.RED);
        removeEatPlaceB.setVisible(false);
        removeEatPlaceB.setOnAction((ActionEvent event)->{
            if(selectEatPlaceBox.getSelectionModel().getSelectedItem() == null){
                alert("Select a EatPoint in \"EatPlace Field\".");
            }
            else if(selectEatPlaceBox.getSelectionModel().getSelectedItem() instanceof String){
                //If the User select the message that there's no City, this button won't do nothing.
            }
            else{
                City city = (City)selectCityBox.getSelectionModel().getSelectedItem();
                EatPoint eat = (EatPoint)selectEatPlaceBox.getSelectionModel().getSelectedItem();
                
                try {
                    facade.removeEatPoint(city, eat);
                    alertInfo("Place Removed successfully.");
                    primaryStage.setScene(adminScene);
                } catch (ThereNoPlaceToEat ex) {
                    //Will never enter here.
                }
            }
        });
        
        grid.add(removeEatPlaceB, 2, 4);
        
        Button selectCityB = new Button("Select");
        selectCityB.setOnAction((ActionEvent event)->{
            if(selectCityBox.getSelectionModel().getSelectedItem() == null){
                alert("Select a City in \"City Field\".");
            }
            else if(selectCityBox.getSelectionModel().getSelectedItem() instanceof String){
                //If the User select the message that there's no City, this button won't do nothing.
            }
            else{
                City city = (City)selectCityBox.getSelectionModel().getSelectedItem();
                ArrayList<EatPoint> eat = facade.getEatPlaces(city);
                if(eat == null){
                    alert("This City doesn't have Place to Eat.");
                    selectEatPlaceBox.getItems().clear();
                    selectEatPlaceBox.getItems().add("There's no Place to Eat.");
                }
                else{
                    selectEatPlaceBox.getItems().clear();
                    for(EatPoint eatP : eat){
                        selectEatPlaceBox.getItems().add(eatP);
                    }
                    selectEatPlace.setVisible(true);
                    selectEatPlaceBox.setVisible(true);
                    removeEatPlaceB.setVisible(true);
                }
            }
        });
        grid.add(selectCityB, 2, 2);
        
        Button back = new Button("Back");
        back.setTextFill(Color.RED);
        back.setOnAction((ActionEvent event)->{
            primaryStage.setScene(adminScene);
        });
        BorderPane bp = new BorderPane();
        bp.setCenter(back);
        grid.add(bp, 1, 8);
    }
    
    private static void alert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error!");
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private static void alertInfo(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Info!");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
