package org.zonagamer.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.zonagamer.entities.Sale;
import org.zonagamer.entities.Videogame;
import org.zonagamer.services.VideoGameService;

public class SellView {

    private Stage stage;
    private VideoGameService service;

    public SellView(Stage stage, VideoGameService service) {
        this.stage = stage;
        this.service = service;
    }

    public void show() {
        Stage window = new Stage();
        window.setTitle("💰 Realizar Venta");

        Label header = new Label("💰  REALIZAR VENTA");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ffd700; -fx-font-family: 'Courier New';");
        Label subheader = new Label("Registra una nueva venta al cliente");
        subheader.setStyle("-fx-font-size: 11px; -fx-text-fill: #555; -fx-font-family: 'Courier New';");
        VBox headerBox = new VBox(4, header, subheader);
        headerBox.setPadding(new Insets(20, 20, 10, 20));
        headerBox.setStyle("-fx-background-color: #0a0a12; -fx-border-color: #ffd700; -fx-border-width: 0 0 1 0;");

        // Panel izquierdo - formulario
        Label formTitle = new Label("─── DATOS DE VENTA ───");
        formTitle.setStyle("-fx-text-fill: #ffd700; -fx-font-family: 'Courier New'; -fx-font-size: 12px;");

        TextField txtTitle = new TextField();
        txtTitle.setPromptText("Título del videojuego...");
        txtTitle.setStyle("-fx-background-color: #111122; -fx-text-fill: #ffd700; -fx-prompt-text-fill: #444;" +
                "-fx-border-color: #ffd700; -fx-border-radius: 3px; -fx-background-radius: 3px;" +
                "-fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-pref-height: 38px;");

        Spinner<Integer> spnQty = new Spinner<>(1, 999, 1);
        spnQty.setStyle("-fx-background-color: #111122; -fx-font-family: 'Courier New';");
        spnQty.setEditable(true);

        Label lblQty = new Label("Cantidad:");
        lblQty.setStyle("-fx-text-fill: #888; -fx-font-family: 'Courier New'; -fx-font-size: 12px;");
        Label lblTitle = new Label("Título del juego:");
        lblTitle.setStyle("-fx-text-fill: #888; -fx-font-family: 'Courier New'; -fx-font-size: 12px;");

        Button btnPreview = new Button("👁  VISTA PREVIA");
        btnPreview.setStyle("-fx-background-color: transparent; -fx-text-fill: #ffd700;" +
                "-fx-border-color: #ffd700; -fx-border-width: 1px;" +
                "-fx-font-family: 'Courier New'; -fx-font-size: 12px; -fx-cursor: hand;" +
                "-fx-pref-width: 180px; -fx-pref-height: 36px;");

        Button btnSell = new Button("💰  CONFIRMAR VENTA");
        btnSell.setStyle("-fx-background-color: #ffd700; -fx-text-fill: #000000;" +
                "-fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-cursor: hand;" +
                "-fx-pref-width: 180px; -fx-pref-height: 36px; -fx-font-weight: bold;");

        VBox formPanel = new VBox(12, formTitle, lblTitle, txtTitle, lblQty, spnQty, btnPreview, btnSell);
        formPanel.setPadding(new Insets(20));
        formPanel.setMinWidth(280);
        formPanel.setStyle("-fx-background-color: #0a0a12; -fx-border-color: #ffd700; -fx-border-width: 0 1 0 0;");

        VBox resultPanel = new VBox(12);
        resultPanel.setPadding(new Insets(20));
        resultPanel.setStyle("-fx-background-color: #0a0a12;");

        Label placeholder = new Label("El resumen de la venta\naparecerá aquí.");
        placeholder.setStyle("-fx-text-fill: #333; -fx-font-family: 'Courier New'; -fx-font-size: 13px; -fx-text-alignment: center;");
        placeholder.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        resultPanel.getChildren().add(placeholder);
        resultPanel.setAlignment(Pos.CENTER);

        btnPreview.setOnAction(e -> {
            resultPanel.getChildren().clear();
            resultPanel.setAlignment(Pos.TOP_LEFT);
            Videogame game = service.findByTitle(txtTitle.getText().trim());
            if (game != null) {
                int qty = spnQty.getValue();
                double total = game.calculateFinalPrice() * qty;
                Label previewTitle = new Label("─── VISTA PREVIA ───");
                previewTitle.setStyle("-fx-text-fill: #ffd700; -fx-font-family: 'Courier New'; -fx-font-size: 12px;");

                GridPane grid = new GridPane();
                grid.setHgap(20);
                grid.setVgap(8);
                String[][] fields = {
                        {"🎮 Juego",       game.getTitle()},
                        {"🖥  Plataforma", game.getPlatform()},
                        {"📦 Stock actual",String.valueOf(game.getStock())},
                        {"💲 Precio unit.", "$" + game.calculateFinalPrice()},
                        {"🔢 Cantidad",    String.valueOf(qty)},
                        {"💰 TOTAL",       "$" + total}
                };
                for (int i = 0; i < fields.length; i++) {
                    Label k = new Label(fields[i][0]);
                    k.setStyle("-fx-text-fill: #888; -fx-font-family: 'Courier New'; -fx-font-size: 12px;");
                    Label v = new Label(fields[i][1]);
                    v.setStyle("-fx-text-fill: " + (i == 5 ? "#ffd700" : "#ffa500") + "; -fx-font-family: 'Courier New'; -fx-font-size: 12px;" + (i == 5 ? "-fx-font-weight: bold;" : ""));
                    grid.add(k, 0, i);
                    grid.add(v, 1, i);
                }
                resultPanel.getChildren().addAll(previewTitle, grid);
            } else {
                Label notFound = new Label("❌ Juego no encontrado.");
                notFound.setStyle("-fx-text-fill: #ff4500; -fx-font-family: 'Courier New';");
                resultPanel.getChildren().add(notFound);
            }
        });

        btnSell.setOnAction(e -> {
            try {
                Sale sale = service.sellVideoGame(txtTitle.getText().trim(), spnQty.getValue());
                resultPanel.getChildren().clear();
                resultPanel.setAlignment(Pos.TOP_LEFT);

                Label successTitle = new Label("✅ VENTA REGISTRADA");
                successTitle.setStyle("-fx-text-fill: #ffd700; -fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-font-weight: bold;");

                GridPane grid = new GridPane();
                grid.setHgap(20);
                grid.setVgap(8);
                String[][] fields = {
                        {"🆔 ID",          sale.getId().substring(0, 8) + "..."},
                        {"🎮 Juego",       sale.getVideoGame().getTitle()},
                        {"📦 Cantidad",    String.valueOf(sale.getQuantity())},
                        {"💲 Precio unit.", "$" + sale.getUnitPrice()},
                        {"💰 TOTAL",       "$" + sale.getTotal()},
                        {"📅 Fecha",       sale.getSaleDate().toString().substring(0, 19)}
                };
                for (int i = 0; i < fields.length; i++) {
                    Label k = new Label(fields[i][0]);
                    k.setStyle("-fx-text-fill: #888; -fx-font-family: 'Courier New'; -fx-font-size: 12px;");
                    Label v = new Label(fields[i][1]);
                    v.setStyle("-fx-text-fill: " + (i == 4 ? "#ffd700" : "#ffa500") + "; -fx-font-family: 'Courier New'; -fx-font-size: 12px;" + (i == 4 ? "-fx-font-weight: bold;" : ""));
                    grid.add(k, 0, i);
                    grid.add(v, 1, i);
                }
                resultPanel.getChildren().addAll(successTitle, grid);
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("⚠ Error");
                alert.setHeaderText(null);
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });

        HBox content = new HBox(0, formPanel, resultPanel);
        HBox.setHgrow(resultPanel, Priority.ALWAYS);

        BorderPane layout = new BorderPane();
        layout.setTop(headerBox);
        layout.setCenter(content);
        layout.setStyle("-fx-background-color: #0f0f1a;");

        window.setScene(new Scene(layout, 750, 480));
        window.show();
    }
}