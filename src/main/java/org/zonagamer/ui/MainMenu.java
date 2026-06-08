package org.zonagamer.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.zonagamer.services.VideoGameService;

public class MainMenu {

    private Stage stage;
    private VideoGameService service;
    private BorderPane view;

    public MainMenu(Stage stage) {
        this.stage = stage;
        this.service = new VideoGameService();
        buildView();
    }

    private void buildView() {
        VBox sidebar = buildSidebar();

        GridPane grid = buildGrid();

        view = new BorderPane();
        view.setLeft(sidebar);
        view.setCenter(grid);
        view.setStyle("-fx-background-color: #0f0f1a;");
    }

    private VBox buildSidebar() {
        Label logo = new Label("GAME\nZONE");
        logo.setStyle(
                "-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #ff4500;" +
                        "-fx-font-family: 'Courier New'; -fx-text-alignment: center;"
        );
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#ff4500"));
        glow.setRadius(20);
        glow.setSpread(0.5);
        logo.setEffect(glow);

        Label version = new Label("v1.0.0");
        version.setStyle("-fx-text-fill: #555; -fx-font-family: 'Courier New'; -fx-font-size: 11px;");

        Label tagline = new Label("▸ Tu tienda de videojuegos ◂");
        tagline.setStyle("-fx-text-fill: #ffa500; -fx-font-family: 'Courier New'; -fx-font-size: 11px;");

        Label pixel1 = new Label("█▀█ ▀▀█");
        pixel1.setStyle("-fx-text-fill: #ff4500; -fx-font-family: 'Courier New'; -fx-font-size: 13px;");
        Label pixel2 = new Label("█▄█ ▄▄█");
        pixel2.setStyle("-fx-text-fill: #ffa500; -fx-font-family: 'Courier New'; -fx-font-size: 13px;");

        Label infoTitle = new Label("─── INFO ───");
        infoTitle.setStyle("-fx-text-fill: #555; -fx-font-family: 'Courier New'; -fx-font-size: 11px;");

        Label info1 = new Label("◉ Gestión de catálogo");
        Label info2 = new Label("◉ Ventas en tiempo real");
        Label info3 = new Label("◉ Persistencia JSON");
        Label info4 = new Label("◉ Búsqueda avanzada");
        for (Label l : new Label[]{info1, info2, info3, info4}) {
            l.setStyle("-fx-text-fill: #888; -fx-font-family: 'Courier New'; -fx-font-size: 11px;");
        }

        Button btnExit = new Button("[ SALIR ]");
        btnExit.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #ff0000;" +
                        "-fx-border-color: #ff0000; -fx-border-width: 1px;" +
                        "-fx-font-family: 'Courier New'; -fx-font-size: 12px; -fx-cursor: hand;" +
                        "-fx-pref-width: 140px;"
        );
        btnExit.setOnMouseEntered(e -> btnExit.setStyle(
                "-fx-background-color: #ff0000; -fx-text-fill: #ffffff;" +
                        "-fx-border-color: #ff0000; -fx-border-width: 1px;" +
                        "-fx-font-family: 'Courier New'; -fx-font-size: 12px; -fx-cursor: hand;" +
                        "-fx-pref-width: 140px;"
        ));
        btnExit.setOnMouseExited(e -> btnExit.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #ff0000;" +
                        "-fx-border-color: #ff0000; -fx-border-width: 1px;" +
                        "-fx-font-family: 'Courier New'; -fx-font-size: 12px; -fx-cursor: hand;" +
                        "-fx-pref-width: 140px;"
        ));
        btnExit.setOnAction(e -> stage.close());

        VBox sidebar = new VBox(18, logo, pixel1, pixel2, tagline, version,
                new Label(""), infoTitle, info1, info2, info3, info4,
                new Label(""), btnExit);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPadding(new Insets(40, 20, 40, 20));
        sidebar.setStyle(
                "-fx-background-color: #0a0a12;" +
                        "-fx-border-color: #ff4500;" +
                        "-fx-border-width: 0 2 0 0;"
        );
        sidebar.setMinWidth(180);
        sidebar.setMaxWidth(180);
        return sidebar;
    }

    private GridPane buildGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(40));
        grid.setAlignment(Pos.CENTER);

        Label header = new Label("◀ MENÚ PRINCIPAL ▶");
        header.setStyle(
                "-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffa500;" +
                        "-fx-font-family: 'Courier New';"
        );
        Label subheader = new Label("Selecciona una opción para continuar");
        subheader.setStyle("-fx-font-size: 12px; -fx-text-fill: #555; -fx-font-family: 'Courier New';");

        VBox headers = new VBox(5, header, subheader);
        headers.setAlignment(Pos.CENTER);
        grid.add(headers, 0, 0, 2, 1);

        VBox card1 = buildCard("🗂", "GESTIONAR", "Crear, editar y eliminar\nvideojuegos del catálogo", "#ff4500",
                e -> new CrudView(stage, service).show());
        VBox card2 = buildCard("📋", "CATÁLOGO", "Ver todos los videojuegos\ndisponibles en tienda", "#ffa500",
                e -> new ListGamesView(stage, service).show());
        VBox card3 = buildCard("🔍", "BUSCAR TÍTULO", "Encuentra un juego\npor su nombre exacto", "#ff6347",
                e -> new SearchTitleView(stage, service).show());
        VBox card4 = buildCard("🎮", "BUSCAR PLATAFORMA", "Filtra juegos por\nsu plataforma", "#ff8c00",
                e -> new SearchPlatformView(stage, service).show());
        VBox card5 = buildCard("💰", "VENDER", "Registra una nueva\nventa al cliente", "#ffd700",
                e -> new SellView(stage, service).show());
        VBox card6 = buildCard("📊", "HISTORIAL", "Consulta todas las\nventas realizadas", "#ff4500",
                e -> new SalesView(stage, service).show());

        grid.add(card1, 0, 1);
        grid.add(card2, 1, 1);
        grid.add(card3, 0, 2);
        grid.add(card4, 1, 2);
        grid.add(card5, 0, 3);
        grid.add(card6, 1, 3);

        grid.setStyle("-fx-background-color: #0f0f1a;");
        return grid;
    }

    private VBox buildCard(String icon, String title, String description, String color, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 30px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + color + ";" +
                        "-fx-font-family: 'Courier New';"
        );

        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #888; -fx-font-family: 'Courier New'; -fx-text-alignment: center;");
        descLabel.setWrapText(true);
        descLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Button btn = new Button("▶ ABRIR");
        btn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + color + ";" +
                        "-fx-border-color: " + color + "; -fx-border-width: 1px;" +
                        "-fx-font-family: 'Courier New'; -fx-font-size: 11px; -fx-cursor: hand;" +
                        "-fx-pref-width: 100px;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + color + "; -fx-text-fill: #000000;" +
                        "-fx-border-color: " + color + "; -fx-border-width: 1px;" +
                        "-fx-font-family: 'Courier New'; -fx-font-size: 11px; -fx-cursor: hand;" +
                        "-fx-pref-width: 100px;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: " + color + ";" +
                        "-fx-border-color: " + color + "; -fx-border-width: 1px;" +
                        "-fx-font-family: 'Courier New'; -fx-font-size: 11px; -fx-cursor: hand;" +
                        "-fx-pref-width: 100px;"
        ));
        btn.setOnAction(action);

        VBox card = new VBox(10, iconLabel, titleLabel, descLabel, btn);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setMinWidth(220);
        card.setMinHeight(160);
        card.setStyle(
                "-fx-background-color: #0a0a12;" +
                        "-fx-border-color: " + color + ";" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 4px;" +
                        "-fx-background-radius: 4px;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web(color));
        shadow.setRadius(10);
        shadow.setSpread(0.0);

        card.setOnMouseEntered(e -> card.setEffect(shadow));
        card.setOnMouseExited(e -> card.setEffect(null));

        return card;
    }

    public BorderPane getView() { return view; }
}