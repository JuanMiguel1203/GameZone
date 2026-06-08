package org.zonagamer.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.zonagamer.entities.Videogame;
import org.zonagamer.services.VideoGameService;

public class ListGamesView {

    private Stage stage;
    private VideoGameService service;

    public ListGamesView(Stage stage, VideoGameService service) {
        this.stage = stage;
        this.service = service;
    }

    public void show() {
        Stage window = new Stage();
        window.setTitle("📋 Catálogo de Videojuegos");

        Label header = new Label("📋  CATÁLOGO COMPLETO");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ffa500; -fx-font-family: 'Courier New';");
        Label subheader = new Label("Todos los videojuegos disponibles en tienda");
        subheader.setStyle("-fx-font-size: 11px; -fx-text-fill: #555; -fx-font-family: 'Courier New';");

        VBox headerBox = new VBox(4, header, subheader);
        headerBox.setPadding(new Insets(20, 20, 10, 20));
        headerBox.setStyle("-fx-background-color: #0a0a12; -fx-border-color: #ffa500; -fx-border-width: 0 0 1 0;");

        TableView<Videogame> table = new TableView<>();
        table.setStyle("-fx-background-color: #0a0a12;");

        TableColumn<Videogame, String> colTitle = new TableColumn<>("Título");
        colTitle.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        colTitle.setMinWidth(180);

        TableColumn<Videogame, String> colPrice = new TableColumn<>("Precio");
        colPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("$" + data.getValue().getPrice()));

        TableColumn<Videogame, String> colFinalPrice = new TableColumn<>("Precio Final");
        colFinalPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("$" + data.getValue().calculateFinalPrice()));

        TableColumn<Videogame, String> colPlatform = new TableColumn<>("Plataforma");
        colPlatform.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPlatform()));

        TableColumn<Videogame, String> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getStock())));

        TableColumn<Videogame, String> colGenre = new TableColumn<>("Género");
        colGenre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGenre()));

        table.getColumns().addAll(colTitle, colPrice, colFinalPrice, colPlatform, colStock, colGenre);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setItems(FXCollections.observableArrayList(service.getAllVideoGames()));

        Label countLabel = new Label("Total: " + service.getAllVideoGames().size() + " videojuegos");
        countLabel.setStyle("-fx-text-fill: #ffa500; -fx-font-family: 'Courier New'; -fx-font-size: 12px;");

        HBox footer = new HBox(countLabel);
        footer.setPadding(new Insets(10, 20, 10, 20));
        footer.setStyle("-fx-background-color: #0a0a12; -fx-border-color: #ffa500; -fx-border-width: 1 0 0 0;");

        BorderPane layout = new BorderPane();
        layout.setTop(headerBox);
        layout.setCenter(table);
        layout.setBottom(footer);
        layout.setStyle("-fx-background-color: #0f0f1a;");

        window.setScene(new Scene(layout, 850, 500));
        window.show();
    }
}