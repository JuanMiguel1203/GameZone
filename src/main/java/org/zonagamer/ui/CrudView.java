package org.zonagamer.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.zonagamer.entities.DigitalVideogame;
import org.zonagamer.entities.PhysicalVideoGame;
import org.zonagamer.entities.Videogame;
import org.zonagamer.services.VideoGameService;
import java.util.List;

public class CrudView {

    private Stage stage;
    private VideoGameService service;
    private TableView<Videogame> table;

    public CrudView(Stage stage, VideoGameService service) {
        this.stage = stage;
        this.service = service;
    }

    public void show() {
        Stage window = new Stage();
        window.setTitle("🗂 Gestionar Videojuegos");

        Label header = new Label("🗂  GESTIÓN DE VIDEOJUEGOS");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ff4500; -fx-font-family: 'Courier New';");
        Label subheader = new Label("Administra el catálogo completo de la tienda");
        subheader.setStyle("-fx-font-size: 11px; -fx-text-fill: #555; -fx-font-family: 'Courier New';");
        VBox headerBox = new VBox(4, header, subheader);
        headerBox.setPadding(new Insets(20, 20, 10, 20));
        headerBox.setStyle("-fx-background-color: #0a0a12; -fx-border-color: #ff4500; -fx-border-width: 0 0 1 0;");

        table = new TableView<>();
        table.setStyle("-fx-background-color: #0a0a12; -fx-border-color: #333;");

        TableColumn<Videogame, String> colTitle = new TableColumn<>("Título");
        colTitle.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        colTitle.setMinWidth(150);

        TableColumn<Videogame, String> colPrice = new TableColumn<>("Precio");
        colPrice.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("$" + data.getValue().getPrice()));

        TableColumn<Videogame, String> colPlatform = new TableColumn<>("Plataforma");
        colPlatform.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPlatform()));

        TableColumn<Videogame, String> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getStock())));

        TableColumn<Videogame, String> colGenre = new TableColumn<>("Género");
        colGenre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGenre()));

        TableColumn<Videogame, String> colType = new TableColumn<>("Tipo");
        colType.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue() instanceof DigitalVideogame ? "💾 Digital" : "📦 Físico"));

        table.getColumns().addAll(colTitle, colPrice, colPlatform, colStock, colGenre, colType);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        refreshTable();

        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: #0a0a12; -fx-tab-min-width: 120px;");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabDigital = new Tab("💾 Digital");
        Tab tabFisico  = new Tab("📦 Físico");

        TextField txtTitle    = styledField("Título del juego");
        TextField txtPrice    = styledField("Precio (ej: 50000)");
        TextField txtPlatform = styledField("Plataforma (PC, PS5...)");
        TextField txtStock    = styledField("Stock disponible");
        TextField txtGenre    = styledField("Género (Acción, RPG...)");

        TextField txtSizeGB   = styledField("Tamaño en GB (ej: 80)");
        TextField txtDownload = styledField("Plataforma de descarga");

        TextField txtCondition   = styledField("Condición (nuevo/usado)");
        TextField txtDistributor = styledField("Distribuidor");

        VBox commonFields = new VBox(8,
                fieldRow("📌 Título",     txtTitle),
                fieldRow("💲 Precio",     txtPrice),
                fieldRow("🖥  Plataforma", txtPlatform),
                fieldRow("📦 Stock",      txtStock),
                fieldRow("🎭 Género",     txtGenre)
        );
        commonFields.setPadding(new Insets(10));

        VBox digitalFields = new VBox(8,
                commonFields,
                fieldRow("💾 Tamaño GB",   txtSizeGB),
                fieldRow("⬇ Descarga en", txtDownload)
        );
        digitalFields.setPadding(new Insets(10));
        digitalFields.setStyle("-fx-background-color: #0a0a12;");

        VBox fisicoFields = new VBox(8,
                commonFields,
                fieldRow("🔧 Condición",   txtCondition),
                fieldRow("🏭 Distribuidor", txtDistributor)
        );
        fisicoFields.setPadding(new Insets(10));
        fisicoFields.setStyle("-fx-background-color: #0a0a12;");

        tabDigital.setContent(new ScrollPane(digitalFields));
        tabFisico.setContent(new ScrollPane(fisicoFields));
        tabPane.getTabs().addAll(tabDigital, tabFisico);

        Button btnAdd    = actionButton("➕ AGREGAR",    "#ff4500", "#cc3700");
        Button btnUpdate = actionButton("✏ ACTUALIZAR", "#ffa500", "#cc8400");
        Button btnDelete = actionButton("🗑 ELIMINAR",  "#ff0000", "#cc0000");
        Button btnClear  = actionButton("✖ LIMPIAR",   "#555555", "#333333");

        btnAdd.setOnAction(e -> {
            try {
                boolean isDigital = tabPane.getSelectionModel().getSelectedItem() == tabDigital;
                Videogame game = isDigital
                        ? new DigitalVideogame(txtTitle.getText().trim(), Double.parseDouble(txtPrice.getText().trim()),
                        txtPlatform.getText().trim(), Integer.parseInt(txtStock.getText().trim()),
                        txtGenre.getText().trim(), Double.parseDouble(txtSizeGB.getText().trim()),
                        txtDownload.getText().trim())
                        : new PhysicalVideoGame(txtTitle.getText().trim(), Double.parseDouble(txtPrice.getText().trim()),
                        txtPlatform.getText().trim(), Integer.parseInt(txtStock.getText().trim()),
                        txtGenre.getText().trim(), txtCondition.getText().trim(), txtDistributor.getText().trim());
                service.addVideoGame(game, isDigital ? "Digital" : "Físico");
                refreshTable();
                showSuccess("✅ Videojuego agregado correctamente.");
            } catch (Exception ex) { showAlert(ex.getMessage()); }
        });

        btnDelete.setOnAction(e -> {
            Videogame selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                service.deleteVideoGame(selected.getTitle());
                refreshTable();
                showSuccess("✅ Videojuego eliminado.");
            } else showAlert("Selecciona un videojuego de la tabla.");
        });

        btnUpdate.setOnAction(e -> {
            Videogame selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    boolean isDigital = tabPane.getSelectionModel().getSelectedItem() == tabDigital;
                    Videogame game = isDigital
                            ? new DigitalVideogame(txtTitle.getText().trim(), Double.parseDouble(txtPrice.getText().trim()),
                            txtPlatform.getText().trim(), Integer.parseInt(txtStock.getText().trim()),
                            txtGenre.getText().trim(), Double.parseDouble(txtSizeGB.getText().trim()),
                            txtDownload.getText().trim())
                            : new PhysicalVideoGame(txtTitle.getText().trim(), Double.parseDouble(txtPrice.getText().trim()),
                            txtPlatform.getText().trim(), Integer.parseInt(txtStock.getText().trim()),
                            txtGenre.getText().trim(), txtCondition.getText().trim(), txtDistributor.getText().trim());
                    service.updateVideoGame(selected.getTitle(), game, isDigital ? "Digital" : "Físico");
                    refreshTable();
                    showSuccess("✅ Videojuego actualizado.");
                } catch (Exception ex) { showAlert(ex.getMessage()); }
            } else showAlert("Selecciona un videojuego de la tabla.");
        });

        btnClear.setOnAction(e -> {
            for (TextField tf : new TextField[]{txtTitle, txtPrice, txtPlatform, txtStock, txtGenre, txtSizeGB, txtDownload, txtCondition, txtDistributor})
                tf.clear();
        });

        HBox btnBox = new HBox(10, btnAdd, btnUpdate, btnDelete, btnClear);
        btnBox.setPadding(new Insets(10));
        btnBox.setStyle("-fx-background-color: #0a0a12; -fx-border-color: #ff4500; -fx-border-width: 1 0 0 0;");

        VBox rightPanel = new VBox(0, tabPane, btnBox);
        rightPanel.setMinWidth(380);
        rightPanel.setMaxWidth(380);
        rightPanel.setStyle("-fx-background-color: #0a0a12; -fx-border-color: #ff4500; -fx-border-width: 0 0 0 1;");

        HBox content = new HBox(0, table, rightPanel);
        HBox.setHgrow(table, Priority.ALWAYS);

        BorderPane layout = new BorderPane();
        layout.setTop(headerBox);
        layout.setCenter(content);
        layout.setStyle("-fx-background-color: #0f0f1a;");

        window.setScene(new Scene(layout, 1000, 560));
        window.show();
    }

    private HBox fieldRow(String label, TextField field) {
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: #888; -fx-font-family: 'Courier New'; -fx-font-size: 11px; -fx-min-width: 120px;");
        HBox row = new HBox(8, lbl, field);
        row.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(field, Priority.ALWAYS);
        return row;
    }

    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(service.getAllVideoGames()));
    }

    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-background-color: #111122; -fx-text-fill: #ffa500; -fx-prompt-text-fill: #444;" +
                "-fx-border-color: #333; -fx-border-radius: 3px; -fx-background-radius: 3px;" +
                "-fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        tf.setMaxWidth(Double.MAX_VALUE);
        return tf;
    }

    private Button actionButton(String text, String color, String hoverColor) {
        Button btn = new Button(text);
        String normal = "-fx-background-color: transparent; -fx-text-fill: " + color + ";" +
                "-fx-border-color: " + color + "; -fx-border-width: 1px;" +
                "-fx-font-family: 'Courier New'; -fx-font-size: 12px; -fx-cursor: hand; -fx-pref-height: 34px;";
        String hover  = "-fx-background-color: " + hoverColor + "; -fx-text-fill: #ffffff;" +
                "-fx-border-color: " + color + "; -fx-border-width: 1px;" +
                "-fx-font-family: 'Courier New'; -fx-font-size: 12px; -fx-cursor: hand; -fx-pref-height: 34px;";
        btn.setStyle(normal);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(normal));
        return btn;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("⚠ Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("✅ Éxito");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}