package org.zonagamer.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.zonagamer.entities.DigitalVideogame;
import org.zonagamer.entities.PhysicalVideoGame;
import org.zonagamer.entities.Videogame;
import org.zonagamer.services.VideoGameService;

public class SearchTitleView {

    private Stage stage;
    private VideoGameService service;

    public SearchTitleView(Stage stage, VideoGameService service) {
        this.stage = stage;
        this.service = service;
    }

    public void show() {
        Stage window = new Stage();
        window.setTitle("🔍 Buscar por Título");

        Label header = new Label("🔍  BUSCAR POR TÍTULO");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ff6347; -fx-font-family: 'Courier New';");
        Label subheader = new Label("Encuentra un videojuego por su nombre exacto");
        subheader.setStyle("-fx-font-size: 11px; -fx-text-fill: #555; -fx-font-family: 'Courier New';");
        VBox headerBox = new VBox(4, header, subheader);
        headerBox.setPadding(new Insets(20, 20, 10, 20));
        headerBox.setStyle("-fx-background-color: #0a0a12; -fx-border-color: #ff6347; -fx-border-width: 0 0 1 0;");

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Escribe el título aquí...");
        txtSearch.setStyle("-fx-background-color: #111122; -fx-text-fill: #ff6347; -fx-prompt-text-fill: #444;" +
                "-fx-border-color: #ff6347; -fx-border-radius: 3px; -fx-background-radius: 3px;" +
                "-fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-pref-height: 40px;");

        Button btnSearch = new Button("🔍  BUSCAR");
        btnSearch.setStyle("-fx-background-color: #ff6347; -fx-text-fill: #000000;" +
                "-fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-cursor: hand;" +
                "-fx-pref-height: 40px; -fx-pref-width: 120px;");

        HBox searchBox = new HBox(10, txtSearch, btnSearch);
        searchBox.setPadding(new Insets(20));
        HBox.setHgrow(txtSearch, Priority.ALWAYS);

        VBox resultPanel = new VBox(12);
        resultPanel.setPadding(new Insets(20));
        resultPanel.setStyle("-fx-background-color: #0a0a12;");

        Label noResult = new Label("Ingresa un título y presiona BUSCAR");
        noResult.setStyle("-fx-text-fill: #444; -fx-font-family: 'Courier New'; -fx-font-size: 13px;");
        resultPanel.getChildren().add(noResult);

        btnSearch.setOnAction(e -> {
            resultPanel.getChildren().clear();
            Videogame game = service.findByTitle(txtSearch.getText().trim());
            if (game != null) {
                resultPanel.getChildren().add(buildResultCard(game));
            } else {
                Label notFound = new Label("❌ No se encontró ningún videojuego con ese título.");
                notFound.setStyle("-fx-text-fill: #ff4500; -fx-font-family: 'Courier New'; -fx-font-size: 13px;");
                resultPanel.getChildren().add(notFound);
            }
        });

        txtSearch.setOnAction(e -> btnSearch.fire());

        BorderPane layout = new BorderPane();
        layout.setTop(headerBox);
        layout.setCenter(new VBox(0, searchBox, resultPanel));
        layout.setStyle("-fx-background-color: #0f0f1a;");

        window.setScene(new Scene(layout, 600, 450));
        window.show();
    }

    private VBox buildResultCard(Videogame game) {
        Label found = new Label("✅ VIDEOJUEGO ENCONTRADO");
        found.setStyle("-fx-text-fill: #ff6347; -fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(8);
        grid.setPadding(new Insets(15));

        String[][] fields = {
                {"🎮 Título",        game.getTitle()},
                {"💲 Precio",        "$" + game.getPrice()},
                {"💰 Precio Final",  "$" + game.calculateFinalPrice()},
                {"🖥  Plataforma",   game.getPlatform()},
                {"📦 Stock",         String.valueOf(game.getStock())},
                {"🎭 Género",        game.getGenre()},
                {"📌 Tipo",          game instanceof DigitalVideogame ? "Digital" : "Físico"}
        };

        for (int i = 0; i < fields.length; i++) {
            Label key = new Label(fields[i][0]);
            key.setStyle("-fx-text-fill: #888; -fx-font-family: 'Courier New'; -fx-font-size: 12px;");
            Label val = new Label(fields[i][1]);
            val.setStyle("-fx-text-fill: #ffa500; -fx-font-family: 'Courier New'; -fx-font-size: 12px; -fx-font-weight: bold;");
            grid.add(key, 0, i);
            grid.add(val, 1, i);
        }

        if (game instanceof DigitalVideogame dg) {
            Label key = new Label("💾 SizeGB");
            key.setStyle("-fx-text-fill: #888; -fx-font-family: 'Courier New'; -fx-font-size: 12px;");
            Label val = new Label(dg.getSizeGB() + " GB");
            val.setStyle("-fx-text-fill: #ffa500; -fx-font-family: 'Courier New'; -fx-font-size: 12px;");
            grid.add(key, 0, fields.length);
            grid.add(val, 1, fields.length);
        } else if (game instanceof PhysicalVideoGame pg) {
            Label key = new Label("🔧 Condición");
            key.setStyle("-fx-text-fill: #888; -fx-font-family: 'Courier New'; -fx-font-size: 12px;");
            Label val = new Label(pg.getCondition());
            val.setStyle("-fx-text-fill: #ffa500; -fx-font-family: 'Courier New'; -fx-font-size: 12px;");
            grid.add(key, 0, fields.length);
            grid.add(val, 1, fields.length);
        }

        VBox card = new VBox(10, found, grid);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #111122; -fx-border-color: #ff6347; -fx-border-width: 1px; -fx-border-radius: 4px;");
        return card;
    }
}