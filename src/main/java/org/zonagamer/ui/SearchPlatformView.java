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
import java.util.List;

public class SearchPlatformView {

    private Stage stage;
    private VideoGameService service;

    public SearchPlatformView(Stage stage, VideoGameService service) {
        this.stage = stage;
        this.service = service;
    }

    public void show() {
        Stage window = new Stage();
        window.setTitle("🎮 Buscar por Plataforma");

        Label header = new Label("🎮  BUSCAR POR PLATAFORMA");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ff8c00; -fx-font-family: 'Courier New';");
        Label subheader = new Label("Filtra los videojuegos por su plataforma");
        subheader.setStyle("-fx-font-size: 11px; -fx-text-fill: #555; -fx-font-family: 'Courier New';");
        VBox headerBox = new VBox(4, header, subheader);
        headerBox.setPadding(new Insets(20, 20, 10, 20));
        headerBox.setStyle("-fx-background-color: #0a0a12; -fx-border-color: #ff8c00; -fx-border-width: 0 0 1 0;");

        ComboBox<String> cbPlatform = new ComboBox<>();
        cbPlatform.getItems().addAll("PC", "PlayStation 5", "PlayStation 4", "Xbox Series X", "Xbox One", "Nintendo Switch");
        cbPlatform.setEditable(true);
        cbPlatform.setPromptText("Selecciona o escribe una plataforma...");
        cbPlatform.setStyle("-fx-background-color: #111122; -fx-text-fill: #ff8c00;" +
                "-fx-border-color: #ff8c00; -fx-font-family: 'Courier New'; -fx-font-size: 13px;" +
                "-fx-pref-height: 40px;");

        Button btnSearch = new Button("🎮  FILTRAR");
        btnSearch.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: #000000;" +
                "-fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-cursor: hand;" +
                "-fx-pref-height: 40px; -fx-pref-width: 120px;");

        HBox searchBox = new HBox(10, cbPlatform, btnSearch);
        searchBox.setPadding(new Insets(20));
        HBox.setHgrow(cbPlatform, Priority.ALWAYS);

        TableView<Videogame> table = new TableView<>();
        table.setStyle("-fx-background-color: #0a0a12;");

        TableColumn<Videogame, String> colTitle = new TableColumn<>("Título");
        colTitle.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        colTitle.setMinWidth(180);

        TableColumn<Videogame, String> colPrice = new TableColumn<>("Precio Final");
        colPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("$" + data.getValue().calculateFinalPrice()));

        TableColumn<Videogame, String> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getStock())));

        TableColumn<Videogame, String> colGenre = new TableColumn<>("Género");
        colGenre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGenre()));

        table.getColumns().addAll(colTitle, colPrice, colStock, colGenre);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label countLabel = new Label("Resultados: 0");
        countLabel.setStyle("-fx-text-fill: #ff8c00; -fx-font-family: 'Courier New'; -fx-font-size: 12px;");

        btnSearch.setOnAction(e -> {
            String platform = cbPlatform.getValue();
            if (platform != null && !platform.trim().isEmpty()) {
                List<Videogame> results = service.findByPlatform(platform.trim());
                table.setItems(FXCollections.observableArrayList(results));
                countLabel.setText("Resultados: " + results.size());
            }
        });

        HBox footer = new HBox(countLabel);
        footer.setPadding(new Insets(10, 20, 10, 20));
        footer.setStyle("-fx-background-color: #0a0a12; -fx-border-color: #ff8c00; -fx-border-width: 1 0 0 0;");

        BorderPane layout = new BorderPane();
        layout.setTop(headerBox);
        layout.setCenter(new VBox(0, searchBox, table));
        layout.setBottom(footer);
        layout.setStyle("-fx-background-color: #0f0f1a;");

        window.setScene(new Scene(layout, 750, 500));
        window.show();
    }
}