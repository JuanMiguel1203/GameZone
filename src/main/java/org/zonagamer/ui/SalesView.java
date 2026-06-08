package org.zonagamer.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.zonagamer.entities.Sale;
import org.zonagamer.services.VideoGameService;

import java.util.List;

public class SalesView {

    private Stage stage;
    private VideoGameService service;

    public SalesView(Stage stage, VideoGameService service) {
        this.stage = stage;
        this.service = service;
    }

    public void show() {
        Stage window = new Stage();
        window.setTitle("📊 Historial de Ventas");

        Label header = new Label("📊  HISTORIAL DE VENTAS");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ff4500; -fx-font-family: 'Courier New';");
        Label subheader = new Label("Todas las ventas registradas en el sistema");
        subheader.setStyle("-fx-font-size: 11px; -fx-text-fill: #555; -fx-font-family: 'Courier New';");
        VBox headerBox = new VBox(4, header, subheader);
        headerBox.setPadding(new Insets(20, 20, 10, 20));
        headerBox.setStyle("-fx-background-color: #0a0a12; -fx-border-color: #ff4500; -fx-border-width: 0 0 1 0;");

        TableView<Sale> table = new TableView<>();
        table.setStyle("-fx-background-color: #0a0a12;");

        TableColumn<Sale, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getId().substring(0, 8) + "..."));
        colId.setMinWidth(100);

        TableColumn<Sale, String> colGame = new TableColumn<>("Videojuego");
        colGame.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getVideoGame().getTitle()));
        colGame.setMinWidth(180);

        TableColumn<Sale, String> colQty = new TableColumn<>("Cant.");
        colQty.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getQuantity())));

        TableColumn<Sale, String> colUnit = new TableColumn<>("Precio Unit.");
        colUnit.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                "$" + data.getValue().getUnitPrice()));

        TableColumn<Sale, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                "$" + data.getValue().getTotal()));

        TableColumn<Sale, String> colDate = new TableColumn<>("Fecha");
        colDate.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getSaleDate().toString().substring(0, 19)));

        table.getColumns().addAll(colId, colGame, colQty, colUnit, colTotal, colDate);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<Sale> sales = service.getAllSales();
        table.setItems(FXCollections.observableArrayList(sales));

        double totalGeneral = sales.stream().mapToDouble(Sale::getTotal).sum();
        Label totalLabel = new Label("Total vendido: $" + totalGeneral + "  |  Ventas: " + sales.size());
        totalLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-font-weight: bold;");

        HBox footer = new HBox(totalLabel);
        footer.setPadding(new Insets(12, 20, 12, 20));
        footer.setStyle("-fx-background-color: #0a0a12; -fx-border-color: #ff4500; -fx-border-width: 1 0 0 0;");

        BorderPane layout = new BorderPane();
        layout.setTop(headerBox);
        layout.setCenter(table);
        layout.setBottom(footer);
        layout.setStyle("-fx-background-color: #0f0f1a;");

        window.setScene(new Scene(layout, 900, 500));
        window.show();
    }
}